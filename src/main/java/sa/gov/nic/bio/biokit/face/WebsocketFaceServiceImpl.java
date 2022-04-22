package sa.gov.nic.bio.biokit.face;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.commons.TaskResponse;
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
    public Future<TaskResponse<InitializeResponse>> initialize()
    {
        Callable<TaskResponse<InitializeResponse>> callable = () -> {
            String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());

            Message message = new Message();
            message.setTransactionId(transactionId);
            message.setType(ServiceType.FACE.getType());
            message.setOperation(WebsocketCommand.INITIALIZE_DEVICE.getCommand());

            AsyncConsumer consumer = new AsyncConsumer();
            consumer.setTransactionId(transactionId);
            asyncClientProxy.registerConsumer(consumer);


            //SEND #####################################################################################
            boolean successfullySent = false;
            try
            {
                asyncClientProxy.sendCommandAsync(message);
                successfullySent = true;
            }
            catch(RequestException e)
            {
                String errorCode = WebsocketFaceErrorCodes.L0002_00001.getCode();
                return TaskResponse.failure(errorCode, e);
            }
            catch(NotConnectedException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                String errorCode = WebsocketFaceErrorCodes.L0002_00002.getCode();
                return TaskResponse.failure(errorCode, e);
            }
            finally
            {
                if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
            }
            //END #####################################################################################
            try
            {
                TaskResponse<Message> taskResponse = consumer.processResponses(null,
                                                                               asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                return TaskResponse.cast(taskResponse,
                                         new TaskResponse.TypeCaster<InitializeResponse, Message>()
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
                return TaskResponse.failure(errorCode, e);
            }
            catch(TimeoutException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                String errorCode = WebsocketFaceErrorCodes.L0002_00004.getCode();
                return TaskResponse.failure(errorCode, e);
            }
            finally
            {
                asyncClientProxy.unregisterConsumer(consumer);
            }
        };
        
        FutureTask<TaskResponse<InitializeResponse>> futureTask = new FutureTask<TaskResponse<InitializeResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
    
    @Override
    public Future<TaskResponse<InitializeResponse>> deinitialize(final String currentDeviceName)
    {
        Callable<TaskResponse<InitializeResponse>> callable = new Callable<TaskResponse<InitializeResponse>>()
        {
            @Override
            public TaskResponse<InitializeResponse> call() throws TimeoutException, NotConnectedException, CancellationException
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
                    return TaskResponse.failure(errorCode, e);
                }
                catch(NotConnectedException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00006.getCode();
                    return TaskResponse.failure(errorCode, e);
                }
                finally
                {
                    if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
                }
                
                try
                {
                    TaskResponse<Message> taskResponse = consumer.processResponses(null,
                                                                                   asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                    return TaskResponse.cast(taskResponse,
                                             new TaskResponse.TypeCaster<InitializeResponse, Message>()
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
                    return TaskResponse.failure(errorCode, e);
                }
                catch(TimeoutException | CancellationException e)
                {
                    throw e;
                } catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00008.getCode();
                    return TaskResponse.failure(errorCode, e);
                }
                finally
                {
                    asyncClientProxy.unregisterConsumer(consumer);
                }
            }
        };
        
        FutureTask<TaskResponse<InitializeResponse>> futureTask =
                                                        new FutureTask<TaskResponse<InitializeResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
    
    @Override
    public Future<TaskResponse<CaptureFaceResponse>> captureFace(final String currentDeviceName,
                                                                 final boolean applyIcao)
    {
        Callable<TaskResponse<CaptureFaceResponse>> callable = () -> {
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
                return TaskResponse.failure(errorCode, e);
            }
            catch(NotConnectedException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                String errorCode = WebsocketFaceErrorCodes.L0002_00010.getCode();
                return TaskResponse.failure(errorCode, e);
            }
            finally
            {
                if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
            }

            try
            {
                TaskResponse<Message> taskResponse = consumer.processResponses(null,
                                                                               asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                return TaskResponse.cast(taskResponse,
                        CaptureFaceResponse::new);
            }
            catch(InterruptedException e)
            {
                String errorCode = WebsocketFaceErrorCodes.L0002_00011.getCode();
                return TaskResponse.failure(errorCode, e);
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
                return TaskResponse.failure(errorCode, e);
            }
            finally
            {
                asyncClientProxy.unregisterConsumer(consumer);
            }
        };
        
        FutureTask<TaskResponse<CaptureFaceResponse>> futureTask =
                                                    new FutureTask<TaskResponse<CaptureFaceResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
    
    @Override
    public Future<TaskResponse<FaceStartPreviewResponse>> startPreview(final String currentDeviceName,
                                                                       final ResponseProcessor<LivePreviewingResponse> responseProcessor)
    {
        Callable<TaskResponse<FaceStartPreviewResponse>> callable =
                () -> {
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
                        return TaskResponse.failure(errorCode, e);
                    }
                    catch(NotConnectedException e)
                    {
                        throw e;
                    }
                    catch(Exception e)
                    {
                        String errorCode = WebsocketFaceErrorCodes.L0002_00014.getCode();
                        return TaskResponse.failure(errorCode, e);
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

                        TaskResponse<Message> taskResponse = consumer.processResponses(rp,
                                                                                       asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                        return TaskResponse.cast(taskResponse,
                                                 new TaskResponse.TypeCaster<FaceStartPreviewResponse, Message>()
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
                        return TaskResponse.failure(errorCode, e);
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
                        return TaskResponse.failure(errorCode, e);
                    }
                    finally
                    {
                        asyncClientProxy.unregisterConsumer(consumer);
                    }
                };
        
        FutureTask<TaskResponse<FaceStartPreviewResponse>> futureTask =
                                                    new FutureTask<TaskResponse<FaceStartPreviewResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
    
    @Override
    public Future<TaskResponse<FaceStopPreviewResponse>> stopPreview(final String currentDeviceName)
    {
        Callable<TaskResponse<FaceStopPreviewResponse>> callable =
                () -> {
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
                        return TaskResponse.failure(errorCode, e);
                    }
                    catch(NotConnectedException e)
                    {
                        throw e;
                    }
                    catch(Exception e)
                    {
                        String errorCode = WebsocketFaceErrorCodes.L0002_00018.getCode();
                        return TaskResponse.failure(errorCode, e);
                    }
                    finally
                    {
                        if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
                    }

                    try
                    {
                        TaskResponse<Message> taskResponse = consumer.processResponses(null,
                                                                                       asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
                        return TaskResponse.cast(taskResponse,
                                                 new TaskResponse.TypeCaster<FaceStopPreviewResponse, Message>()
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
                        return TaskResponse.failure(errorCode, e);
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
                        return TaskResponse.failure(errorCode, e);
                    }
                    finally
                    {
                        asyncClientProxy.unregisterConsumer(consumer);
                    }
                };
        
        FutureTask<TaskResponse<FaceStopPreviewResponse>> futureTask = new FutureTask<TaskResponse<FaceStopPreviewResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }
}