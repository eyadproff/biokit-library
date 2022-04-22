package sa.gov.nic.bio.biokit.websocket;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.biokit.AsyncConsumer;
import sa.gov.nic.bio.biokit.exceptions.*;
import sa.gov.nic.bio.biokit.signalr.SignalRClient;
import sa.gov.nic.bio.biokit.utils.JsonMapper;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import javax.websocket.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@ClientEndpoint
public class WebsocketClient extends AsyncClientProxy<Message>
{
    public static final AtomicLong ID_GENERATOR = new AtomicLong();
    private static final Logger LOGGER = Logger.getLogger(WebsocketClient.class.getName());
    private static final int UPDATE_RETURN_CODE = 777;

    private WebSocketContainer webSocketContainer;
    private JsonMapper<Message> jsonMapper;
    private ClosureListener closureListener;
    private WebsocketLogger websocketLogger;
    private UpdateListener updateListener;


    public WebsocketClient(String websocketServerUrl, int maxTextMessageBufferSize, int maxBinaryMessageBufferSize,
                           int responseTimeoutSeconds, JsonMapper<Message> jsonMapper, ClosureListener closureListener,
                           WebsocketLogger websocketLogger, UpdateListener updateListener)
    {
        super("http://localhost:5000/cameraOperationHub", responseTimeoutSeconds);

        SignalRClient.init();


        this.closureListener = closureListener;
        this.websocketLogger = websocketLogger;

    }
    
    public void setClosureListener(ClosureListener closureListener){this.closureListener = closureListener;}
    public void setUpdateListener(UpdateListener updateListener){this.updateListener = updateListener;}
    
    @Override
    public synchronized void connect() throws AlreadyConnectedException, ConnectionException
    {

    
        try
        {
            SignalRClient.startConnection();
            if(isConnected()) throw new AlreadyConnectedException();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    
    @Override
    public synchronized void disconnect() throws NotConnectedException, ConnectionException
    {
        SignalRClient.stopConnection();

    }
    
    @Override
    public synchronized boolean isConnected()
    {
        return SignalRClient.isConnected();
    }

    @Override
    public synchronized void sendCommandAsync(Message message) throws NotConnectedException, RequestException
    {
        if(!isConnected()) throw new NotConnectedException();
        
        try
        {
            String json = jsonMapper.toJson(message);

            if(message.getOperation().equals(WebsocketCommand.SHUTDOWN.getCommand())){
                //no need to shutdown
            }

if(message.getOperation().equals(WebsocketCommand.START_PREVIEW.getCommand())){

}
if(message.getOperation() == WebsocketCommand.STOP_PREVIEW.getCommand()){

}
if(message.getOperation() == WebsocketCommand.CANCEL_CAPTURE.getCommand()){

}
if(message.getOperation() == WebsocketCommand.CAPTURE.getCommand()){

}
if(message.getOperation() == WebsocketCommand.INITIALIZE_DEVICE.getCommand()){

}
if(message.getOperation() == WebsocketCommand.DEINITIALIZE_DEVICE.getCommand()){

}
if(message.getOperation() == WebsocketCommand.FIND_DUPLICATES.getCommand()){

}
if(message.getOperation() == WebsocketCommand.GET_SEGMENTED_FINGERS.getCommand()){

}
if(message.getOperation() == WebsocketCommand.CONVERT_WSQ_TO_IMAGE.getCommand()){

}
if(message.getOperation() == WebsocketCommand.CONVERT_IMAGE_TO_WSQ.getCommand()){

}
if(message.getOperation() == WebsocketCommand.SHUTDOWN.getCommand()){

}
if(message.getOperation() == WebsocketCommand.UPDATER.getCommand()){

}
if(message.getOperation() == WebsocketCommand.SCAN_TENPRINT.getCommand()){

}
if(message.getOperation() == WebsocketCommand.GET_ICAO_IMAGE.getCommand()){

}


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