package sa.gov.nic.bio.biokit.face;

import sa.gov.nic.bio.biokit.*;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.beans.ServiceResponse;
import sa.gov.nic.bio.biokit.beans.StartPreviewResponse;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.face.beans.CaptureFaceResponse;
import sa.gov.nic.bio.biokit.websocket.ServiceType;
import sa.gov.nic.bio.biokit.websocket.WebsocketClient;
import sa.gov.nic.bio.biokit.websocket.WebsocketCommand;
import sa.gov.nic.bio.biokit.AsyncConsumer;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.concurrent.*;

public class WebsocketFaceServiceImpl implements FaceService
{
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private AsyncClientProxy<Message> asyncClientProxy;
    
    public WebsocketFaceServiceImpl(AsyncClientProxy<Message> asyncClientProxy)
    {
        this.asyncClientProxy = asyncClientProxy;
    }
    
    @Override
    public Future<ServiceResponse<InitializeResponse>> initialize()
    {
        Callable<ServiceResponse<InitializeResponse>> callable = new Callable<ServiceResponse<InitializeResponse>>()
        {
            @Override
            public ServiceResponse<InitializeResponse> call() throws TimeoutException, NotConnectedException
            {
                String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
                
                Message message = new Message();
                message.setTransactionId(transactionId);
                message.setType(ServiceType.FACE.getType());
                message.setOperation(WebsocketCommand.INITIALIZE_DEVICE.getCommand());
                
                AsyncConsumer consumer = new AsyncConsumer();
                consumer.setTransactionId(transactionId);
                asyncClientProxy.registerConsumer(consumer);
                
                boolean successfullySent = false;
                try
                {
                    asyncClientProxy.sendCommandAsync(message);
                    successfullySent = true;
                }
                catch(RequestException e)
                {
                    String errorCode = "L0002-00001";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                catch(NotConnectedException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = "L0002-00002";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                finally
                {
                    if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
                }
                
                try
                {
                    ServiceResponse<Message> messageServiceResponse = consumer.processResponses(null, asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                    return ServiceResponse.cast(messageServiceResponse, new ServiceResponse.TypeCaster<InitializeResponse, Message>()
                    {
                        @Override
                        public InitializeResponse cast(Message m)
                        {
                            return new InitializeResponse(m);
                        }
                    });
                }
                catch(InterruptedException e)
                {
                    String errorCode = "L0002-00003";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                catch(TimeoutException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = "L0002-00004";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                finally
                {
                    asyncClientProxy.unregisterConsumer(consumer);
                }
            }
        };
        
        FutureTask<ServiceResponse<InitializeResponse>> futureTask = new FutureTask<ServiceResponse<InitializeResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
    
    @Override
    public Future<ServiceResponse<InitializeResponse>> deinitialize(final String currentDeviceName)
    {
        Callable<ServiceResponse<InitializeResponse>> callable = new Callable<ServiceResponse<InitializeResponse>>()
        {
            @Override
            public ServiceResponse<InitializeResponse> call() throws TimeoutException, NotConnectedException
            {
                String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
                
                Message message = new Message();
                message.setTransactionId(transactionId);
                message.setType(ServiceType.FACE.getType());
                message.setOperation(WebsocketCommand.DEINITIALIZE_DEVICE.getCommand());
                message.setCurrentDeviceName(currentDeviceName);
                
                AsyncConsumer consumer = new AsyncConsumer();
                consumer.setTransactionId(transactionId);
                asyncClientProxy.registerConsumer(consumer);
                
                boolean successfullySent = false;
                try
                {
                    asyncClientProxy.sendCommandAsync(message);
                    successfullySent = true;
                }
                catch(RequestException e)
                {
                    String errorCode = "L0002-00005";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                catch(NotConnectedException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = "L0002-00006";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                finally
                {
                    if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
                }
                
                try
                {
                    ServiceResponse<Message> messageServiceResponse = consumer.processResponses(null, asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                    return ServiceResponse.cast(messageServiceResponse, new ServiceResponse.TypeCaster<InitializeResponse, Message>()
                    {
                        @Override
                        public InitializeResponse cast(Message m)
                        {
                            return new InitializeResponse(m);
                        }
                    });
                }
                catch(InterruptedException e)
                {
                    String errorCode = "L0002-00007";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                catch(TimeoutException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = "L0002-00008";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                finally
                {
                    asyncClientProxy.unregisterConsumer(consumer);
                }
            }
        };
        
        FutureTask<ServiceResponse<InitializeResponse>> futureTask = new FutureTask<ServiceResponse<InitializeResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
    
    @Override
    public Future<ServiceResponse<CaptureFaceResponse>> captureFace(final String currentDeviceName,
                                                                    final boolean applyIcao)
    {
        Callable<ServiceResponse<CaptureFaceResponse>> callable = new Callable<ServiceResponse<CaptureFaceResponse>>()
        {
            @Override
            public ServiceResponse<CaptureFaceResponse> call() throws TimeoutException, NotConnectedException
            {
                String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
                
                Message message = new Message();
                message.setTransactionId(transactionId);
                message.setType(ServiceType.FACE.getType());
                message.setOperation(WebsocketCommand.CAPTURE.getCommand());
                message.setCurrentDeviceName(currentDeviceName);
                message.setNeedIcaoCropping(applyIcao);
                
                AsyncConsumer consumer = new AsyncConsumer();
                consumer.setTransactionId(transactionId);
                asyncClientProxy.registerConsumer(consumer);
                
                boolean successfullySent = false;
                try
                {
                    asyncClientProxy.sendCommandAsync(message);
                    successfullySent = true;
                }
                catch(RequestException e)
                {
                    String errorCode = "L0002-00009";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                catch(NotConnectedException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = "L0002-00010";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                finally
                {
                    if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
                }
                
                try
                {
                    ServiceResponse<Message> messageServiceResponse = consumer.processResponses(null, asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                    return ServiceResponse.cast(messageServiceResponse, new ServiceResponse.TypeCaster<CaptureFaceResponse, Message>()
                    {
                        @Override
                        public CaptureFaceResponse cast(Message m)
                        {
                            return new CaptureFaceResponse(m);
                        }
                    });
                }
                catch(InterruptedException e)
                {
                    String errorCode = "L0002-00011";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                catch(TimeoutException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = "L0002-00012";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                finally
                {
                    asyncClientProxy.unregisterConsumer(consumer);
                }
            }
        };
        
        FutureTask<ServiceResponse<CaptureFaceResponse>> futureTask = new FutureTask<ServiceResponse<CaptureFaceResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
    
    @Override
    public Future<ServiceResponse<Void>> startPreview(final String currentDeviceName, final ResponseProcessor<StartPreviewResponse> responseProcessor)
    {
        Callable<ServiceResponse<Void>> callable = new Callable<ServiceResponse<Void>>()
        {
            @Override
            public ServiceResponse<Void> call() throws TimeoutException, NotConnectedException
            {
                String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
                
                Message message = new Message();
                message.setTransactionId(transactionId);
                message.setType(ServiceType.FACE.getType());
                message.setOperation(WebsocketCommand.START_PREVIEW.getCommand());
                message.setCurrentDeviceName(currentDeviceName);
                
                AsyncConsumer consumer = new AsyncConsumer();
                consumer.setTransactionId(transactionId);
                asyncClientProxy.registerConsumer(consumer);
                
                boolean successfullySent = false;
                try
                {
                    asyncClientProxy.sendCommandAsync(message);
                    successfullySent = true;
                }
                catch(RequestException e)
                {
                    String errorCode = "L0002-00013";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                catch(NotConnectedException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = "L0002-00014";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                finally
                {
                    if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
                }
                
                try
                {
                    ResponseProcessor<Message> rp = new ResponseProcessor<Message>()
                    {
                        @Override
                        public void processResponse(Message response)
                        {
                            StartPreviewResponse startPreviewResponse = new StartPreviewResponse(response);
                            responseProcessor.processResponse(startPreviewResponse);
                        }
                    };
                    
                    ServiceResponse<Message> messageServiceResponse = consumer.processResponses(rp, asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                    return ServiceResponse.cast(messageServiceResponse, null);
                }
                catch(InterruptedException e)
                {
                    String errorCode = "L0002-00015";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                catch(TimeoutException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = "L0002-00016";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                finally
                {
                    asyncClientProxy.unregisterConsumer(consumer);
                }
            }
        };
        
        FutureTask<ServiceResponse<Void>> futureTask = new FutureTask<ServiceResponse<Void>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
    
    @Override
    public Future<ServiceResponse<FaceStopPreviewResponse>> stopPreview(final String currentDeviceName)
    {
        Callable<ServiceResponse<FaceStopPreviewResponse>> callable = new Callable<ServiceResponse<FaceStopPreviewResponse>>()
        {
            @Override
            public ServiceResponse<FaceStopPreviewResponse> call() throws TimeoutException, NotConnectedException
            {
                String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
                
                Message message = new Message();
                message.setTransactionId(transactionId);
                message.setType(ServiceType.FACE.getType());
                message.setOperation(WebsocketCommand.STOP_PREVIEW.getCommand());
                message.setCurrentDeviceName(currentDeviceName);
                
                AsyncConsumer consumer = new AsyncConsumer();
                consumer.setTransactionId(transactionId);
                asyncClientProxy.registerConsumer(consumer);
                
                boolean successfullySent = false;
                try
                {
                    asyncClientProxy.sendCommandAsync(message);
                    successfullySent = true;
                }
                catch(RequestException e)
                {
                    String errorCode = "L0002-00017";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                catch(NotConnectedException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = "L0002-00018";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                finally
                {
                    if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
                }
                
                try
                {
                    ServiceResponse<Message> messageServiceResponse = consumer.processResponses(null, asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                    return ServiceResponse.cast(messageServiceResponse, new ServiceResponse.TypeCaster<FaceStopPreviewResponse, Message>()
                    {
                        @Override
                        public FaceStopPreviewResponse cast(Message m)
                        {
                            return new FaceStopPreviewResponse(m);
                        }
                    });
                }
                catch(InterruptedException e)
                {
                    String errorCode = "L0002-00019";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                catch(TimeoutException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = "L0002-00020";
                    return ServiceResponse.failureResponse(errorCode, e);
                }
                finally
                {
                    asyncClientProxy.unregisterConsumer(consumer);
                }
            }
        };
        
        FutureTask<ServiceResponse<FaceStopPreviewResponse>> futureTask = new FutureTask<ServiceResponse<FaceStopPreviewResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
}