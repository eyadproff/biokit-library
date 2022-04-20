package sa.gov.nic.bio.biokit.websocket;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.biokit.AsyncConsumer;
import sa.gov.nic.bio.biokit.exceptions.AlreadyConnectedException;
import sa.gov.nic.bio.biokit.exceptions.ConnectionException;
import sa.gov.nic.bio.biokit.exceptions.JsonMappingException;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;
import sa.gov.nic.bio.biokit.utils.JsonMapper;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@ClientEndpoint
public class WebsocketClient extends AsyncClientProxy<Message>
{
    public static final AtomicLong ID_GENERATOR = new AtomicLong();
    private static final Logger LOGGER = Logger.getLogger(WebsocketClient.class.getName());
    private static final int UPDATE_RETURN_CODE = 777;
    
    private int maxTextMessageBufferSize;
    private int maxBinaryMessageBufferSize;
    private WebSocketContainer webSocketContainer;
    private JsonMapper<Message> jsonMapper;
    private ClosureListener closureListener;
    private WebsocketLogger websocketLogger;
    private UpdateListener updateListener;
    private Session session;

    public WebsocketClient(String websocketServerUrl, int maxTextMessageBufferSize, int maxBinaryMessageBufferSize,
                           int responseTimeoutSeconds, JsonMapper<Message> jsonMapper, ClosureListener closureListener,
                           WebsocketLogger websocketLogger, UpdateListener updateListener)
    {
        super(websocketServerUrl, responseTimeoutSeconds);

        this.maxTextMessageBufferSize = maxTextMessageBufferSize;
        this.maxBinaryMessageBufferSize = maxBinaryMessageBufferSize;
        this.webSocketContainer = ContainerProvider.getWebSocketContainer();
        this.jsonMapper = jsonMapper;
        this.closureListener = closureListener;
        this.websocketLogger = websocketLogger;
        this.updateListener = updateListener;
    }
    
    public void setClosureListener(ClosureListener closureListener){this.closureListener = closureListener;}
    public void setUpdateListener(UpdateListener updateListener){this.updateListener = updateListener;}
    
    @Override
    public synchronized void connect() throws AlreadyConnectedException, ConnectionException
    {
        if(isConnected()) throw new AlreadyConnectedException();
    
        try
        {
            session = webSocketContainer.connectToServer(this, new URI(serverUrl));
        }
        catch(Exception e)
        {
            if(e instanceof DeploymentException){
                //try again after
                int tries = 0;boolean connected = false;
                while (tries<=3 && !connected) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        session = webSocketContainer.connectToServer(this, new URI(serverUrl));
                        connected = true;
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                        tries++;
                    } catch (DeploymentException deploymentException) {
                        deploymentException.printStackTrace();
                        tries++;
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        tries++;
                    } catch (URISyntaxException uriSyntaxException) {
                        uriSyntaxException.printStackTrace();
                        tries++;
                    }
                }
                if(!connected){
                    throw new ConnectionException("Failed to connect to the websocket server: " + serverUrl, e);
                }
            }else {
                throw new ConnectionException("Failed to connect to the websocket server: " + serverUrl, e);
            }
        }
        
        session.setMaxTextMessageBufferSize(maxTextMessageBufferSize);
        session.setMaxBinaryMessageBufferSize(maxBinaryMessageBufferSize);
    }
    
    @Override
    public synchronized void disconnect() throws NotConnectedException, ConnectionException
    {
        if(!isConnected()) throw new NotConnectedException();
    
        try
        {
            session.close();
        }
        catch(IOException e)
        {
            throw new ConnectionException("Failure occurs on closing the connection with the websocket server: " +
                                          serverUrl, e);
        }
        finally
        {
            session = null;
        }
    }
    
    @Override
    public synchronized boolean isConnected()
    {
        return session != null && session.isOpen();
    }

    @Override
    public synchronized void sendCommandAsync(Message message) throws NotConnectedException, RequestException
    {
        if(!isConnected()) throw new NotConnectedException();
        
        try
        {
            String json = jsonMapper.toJson(message);
            session.getBasicRemote().sendText(json);
        }
        catch(Exception e)
        {
            throw new RequestException("Failed during sending the command: " + message, e);
        }
    }
    
    @OnOpen
    public void onOpen()
    {
        if(websocketLogger != null) websocketLogger.logConnectionOpening();
    }

    @OnClose
    public void onClose(CloseReason closeReason)
    {
        String closeCode = null;
        String reasonPhrase = null;
    
        if(closeReason != null)
        {
            if(closeReason.getCloseCode() != null) closeCode = closeReason.getCloseCode().toString();
            reasonPhrase = closeReason.getReasonPhrase();
        }
        
        if(websocketLogger != null) websocketLogger.logConnectionClosure(closeCode, reasonPhrase);
        LOGGER.info("WebsocketClient.onClose(): closeCode = " + closeCode + ", reasonPhrase = " + reasonPhrase);
        
        this.session = null;
    
        AsyncConsumer[] asyncConsumers = new AsyncConsumer[consumers.size()];
        consumers.toArray(asyncConsumers);
        
        for(AsyncConsumer consumer : asyncConsumers)
        {
            consumer.cancel();
            consumers.remove(consumer);
        }
        
        closureListener.onClose(closeCode, reasonPhrase);
    }

    @OnError
    public void onError(Throwable t)
    {
        if(websocketLogger != null) websocketLogger.logError(t);
        
        String errorCode = WebsocketErrorCodes.L0001_00001.getCode();
        
        if(t instanceof Exception) publishFailureResponse(errorCode, (Exception) t);
        else t.printStackTrace();
    }

    @OnMessage
    public void onMessage(String json)
    {
        LOGGER.fine("New message: " + json);
        
        try
        {
            Message message = jsonMapper.fromJson(json);
            if(websocketLogger != null) websocketLogger.logNewMessage(message);
            
            if(message.getReturnCode() == UPDATE_RETURN_CODE)
            {
                if(updateListener != null) updateListener.newUpdate();
            }
            else
            {
                String transactionId = message.getTransactionId();
                publishSuccessResponse(transactionId, message);
            }
        }
        catch(JsonMappingException e)
        {
            String errorCode = WebsocketErrorCodes.L0001_00002.getCode();
            publishFailureResponse(errorCode, e);
        }
        catch(Exception e)
        {
            String errorCode = WebsocketErrorCodes.L0001_00003.getCode();
            publishFailureResponse(errorCode, e);
        }
    }

    private void publishSuccessResponse(String transactionId, Message message)
    {
        AsyncConsumer[] asyncConsumers = new AsyncConsumer[consumers.size()];
        consumers.toArray(asyncConsumers);
    
        for(AsyncConsumer consumer : asyncConsumers)
        {
            if(consumer.getTransactionId().equals(transactionId))
            {
                consumer.consume(message);
                if(message.isEnd()) consumers.remove(consumer);
            }
        }
    }

    private void publishFailureResponse(String errorCode, Exception exception)
    {
        AsyncConsumer[] asyncConsumers = new AsyncConsumer[consumers.size()];
        consumers.toArray(asyncConsumers);
    
        for(AsyncConsumer consumer : asyncConsumers)
        {
            consumer.reportError(errorCode, exception);
            consumers.remove(consumer);
        }
    }
}