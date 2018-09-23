package sa.gov.nic.bio.biokit.face;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.commons.ServiceResponse;
import sa.gov.nic.bio.biokit.beans.LivePreviewingResponse;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.face.beans.CaptureFaceResponse;
import sa.gov.nic.bio.biokit.face.beans.FaceStartPreviewResponse;
import sa.gov.nic.bio.biokit.websocket.ServiceType;
import sa.gov.nic.bio.biokit.websocket.WebsocketClient;
import sa.gov.nic.bio.biokit.websocket.WebsocketCommand;
import sa.gov.nic.bio.biokit.AsyncConsumer;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

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
                    String errorCode = WebsocketFaceErrorCodes.L0002_00001.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                catch(NotConnectedException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00002.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                finally
                {
                    if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
                }
                
                try
                {
                    ServiceResponse<Message> messageServiceResponse = consumer.processResponses(null,
                                                                                                asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                    return ServiceResponse.cast(messageServiceResponse,
                                                        new ServiceResponse.TypeCaster<InitializeResponse, Message>()
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
                    String errorCode = WebsocketFaceErrorCodes.L0002_00003.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                catch(TimeoutException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00004.getCode();
                    return ServiceResponse.failure(errorCode, e);
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
            public ServiceResponse<InitializeResponse> call() throws TimeoutException, NotConnectedException, CancellationException
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
                    String errorCode = WebsocketFaceErrorCodes.L0002_00005.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                catch(NotConnectedException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00006.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                finally
                {
                    if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
                }
                
                try
                {
                    ServiceResponse<Message> messageServiceResponse = consumer.processResponses(null,
                                                        asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                    return ServiceResponse.cast(messageServiceResponse,
                                                        new ServiceResponse.TypeCaster<InitializeResponse, Message>()
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
                    String errorCode = WebsocketFaceErrorCodes.L0002_00007.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                catch(TimeoutException e)
                {
                    throw e;
                }
                catch(CancellationException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00008.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                finally
                {
                    asyncClientProxy.unregisterConsumer(consumer);
                }
            }
        };
        
        FutureTask<ServiceResponse<InitializeResponse>> futureTask =
                                                        new FutureTask<ServiceResponse<InitializeResponse>>(callable);
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
            public ServiceResponse<CaptureFaceResponse> call() throws TimeoutException, NotConnectedException,
                                                                      CancellationException
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
                    String errorCode = WebsocketFaceErrorCodes.L0002_00009.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                catch(NotConnectedException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00010.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                finally
                {
                    if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
                }
                
                try
                {
                    ServiceResponse<Message> messageServiceResponse = consumer.processResponses(null,
                                                        asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                    return ServiceResponse.cast(messageServiceResponse,
                                                        new ServiceResponse.TypeCaster<CaptureFaceResponse, Message>()
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
                    String errorCode = WebsocketFaceErrorCodes.L0002_00011.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                catch(TimeoutException e)
                {
                    throw e;
                }
                catch(CancellationException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00012.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                finally
                {
                    asyncClientProxy.unregisterConsumer(consumer);
                }
            }
        };
        
        FutureTask<ServiceResponse<CaptureFaceResponse>> futureTask =
                                                    new FutureTask<ServiceResponse<CaptureFaceResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
    
    @Override
    public Future<ServiceResponse<FaceStartPreviewResponse>> startPreview(final String currentDeviceName,
                                                  final ResponseProcessor<LivePreviewingResponse> responseProcessor)
    {
        Callable<ServiceResponse<FaceStartPreviewResponse>> callable =
                                                            new Callable<ServiceResponse<FaceStartPreviewResponse>>()
        {
            @Override
            public ServiceResponse<FaceStartPreviewResponse> call() throws TimeoutException, NotConnectedException,
                                                                           CancellationException
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
                    String errorCode = WebsocketFaceErrorCodes.L0002_00013.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                catch(NotConnectedException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00014.getCode();
                    return ServiceResponse.failure(errorCode, e);
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
                            LivePreviewingResponse livePreviewingResponse = new LivePreviewingResponse(response);
                            responseProcessor.processResponse(livePreviewingResponse);
                        }
                    };
                    
                    ServiceResponse<Message> messageServiceResponse = consumer.processResponses(rp,
                                                        asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                    return ServiceResponse.cast(messageServiceResponse,
                                                    new ServiceResponse.TypeCaster<FaceStartPreviewResponse, Message>()
                    {
                        @Override
                        public FaceStartPreviewResponse cast(Message m)
                        {
                            return new FaceStartPreviewResponse(m);
                        }
                    });
                }
                catch(InterruptedException e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00015.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                catch(TimeoutException e)
                {
                    throw e;
                }
                catch(CancellationException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00016.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                finally
                {
                    asyncClientProxy.unregisterConsumer(consumer);
                }
            }
        };
        
        FutureTask<ServiceResponse<FaceStartPreviewResponse>> futureTask =
                                                    new FutureTask<ServiceResponse<FaceStartPreviewResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
    
    @Override
    public Future<ServiceResponse<FaceStopPreviewResponse>> stopPreview(final String currentDeviceName)
    {
        Callable<ServiceResponse<FaceStopPreviewResponse>> callable =
                                                            new Callable<ServiceResponse<FaceStopPreviewResponse>>()
        {
            @Override
            public ServiceResponse<FaceStopPreviewResponse> call() throws TimeoutException, NotConnectedException,
                                                                          CancellationException
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
                    String errorCode = WebsocketFaceErrorCodes.L0002_00017.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                catch(NotConnectedException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00018.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                finally
                {
                    if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
                }
                
                try
                {
                    ServiceResponse<Message> messageServiceResponse = consumer.processResponses(null,
                                                        asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                    return ServiceResponse.cast(messageServiceResponse,
                                                    new ServiceResponse.TypeCaster<FaceStopPreviewResponse, Message>()
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
                    String errorCode = WebsocketFaceErrorCodes.L0002_00019.getCode();
                    return ServiceResponse.failure(errorCode, e);
                }
                catch(TimeoutException e)
                {
                    throw e;
                }
                catch(CancellationException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00020.getCode();
                    return ServiceResponse.failure(errorCode, e);
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