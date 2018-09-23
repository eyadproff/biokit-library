package sa.gov.nic.bio.biokit.fingerprint;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.beans.LivePreviewingResponse;
import sa.gov.nic.bio.commons.ServiceResponse;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.fingerprint.beans.CaptureFingerprintResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.FingerprintStopPreviewResponse;
import sa.gov.nic.bio.biokit.websocket.ServiceType;
import sa.gov.nic.bio.biokit.websocket.WebsocketClient;
import sa.gov.nic.bio.biokit.websocket.WebsocketCommand;
import sa.gov.nic.bio.biokit.AsyncConsumer;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class WebsocketFingerprintServiceImpl implements FingerprintService
{
	private ExecutorService executorService = Executors.newCachedThreadPool();
    private AsyncClientProxy<Message> asyncClientProxy;

    public WebsocketFingerprintServiceImpl(AsyncClientProxy<Message> asyncClientProxy)
    {
        this.asyncClientProxy = asyncClientProxy;
    }
	
	@Override
	public Future<ServiceResponse<InitializeResponse>> initialize(final int position)
	{
		Callable<ServiceResponse<InitializeResponse>> callable = new Callable<ServiceResponse<InitializeResponse>>()
		{
			@Override
			public ServiceResponse<InitializeResponse> call() throws TimeoutException, NotConnectedException, CancellationException
			{
				String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
				
				Message message = new Message();
				message.setTransactionId(transactionId);
				message.setType(ServiceType.FINGERPRINT.getType());
				message.setOperation(WebsocketCommand.INITIALIZE_DEVICE.getCommand());
				message.setPosition(position);
				
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
					String errorCode = WebsocketFingerprintErrorCodes.L0003_00001.getCode();
					return ServiceResponse.failure(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketFingerprintErrorCodes.L0003_00002.getCode();
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
					String errorCode = WebsocketFingerprintErrorCodes.L0003_00003.getCode();
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
					String errorCode = WebsocketFingerprintErrorCodes.L0003_00004.getCode();
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
	public Future<ServiceResponse<InitializeResponse>> deinitialize(final int position, final String currentDeviceName)
	{
		Callable<ServiceResponse<InitializeResponse>> callable = new Callable<ServiceResponse<InitializeResponse>>()
		{
			@Override
			public ServiceResponse<InitializeResponse> call() throws TimeoutException, NotConnectedException,
			                                                         CancellationException
			{
				String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
				
				Message message = new Message();
				message.setTransactionId(transactionId);
				message.setType(ServiceType.FINGERPRINT.getType());
				message.setOperation(WebsocketCommand.DEINITIALIZE_DEVICE.getCommand());
				message.setCurrentDeviceName(currentDeviceName);
				message.setPosition(position);
				
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
					String errorCode = WebsocketFingerprintErrorCodes.L0003_00005.getCode();
					return ServiceResponse.failure(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketFingerprintErrorCodes.L0003_00006.getCode();
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
					String errorCode = WebsocketFingerprintErrorCodes.L0003_00007.getCode();
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
					String errorCode = WebsocketFingerprintErrorCodes.L0003_00008.getCode();
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
    public Future<ServiceResponse<CaptureFingerprintResponse>> startPreviewAndAutoCapture(
    		final String currentDeviceName, final int position, final int expectedFingersCount,
		    final List<Integer> missingFingers, final boolean noTimeout, final boolean segmentedWsqRequired,
		    final ResponseProcessor<LivePreviewingResponse> responseProcessor)
    {
	    Callable<ServiceResponse<CaptureFingerprintResponse>> callable =
			                                            new Callable<ServiceResponse<CaptureFingerprintResponse>>()
	    {
		    @Override
		    public ServiceResponse<CaptureFingerprintResponse> call() throws TimeoutException, NotConnectedException,
		                                                                     CancellationException
		    {
			    String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
			
			    Message message = new Message();
			    message.setTransactionId(transactionId);
			    message.setType(ServiceType.FINGERPRINT.getType());
			    message.setOperation(WebsocketCommand.CAPTURE.getCommand());
			    message.setPosition(position);
			    message.setSegmentationRequired(true);
			    message.setWsqRequired(true);
			    message.setExpectedFingersCount("" + expectedFingersCount);
			    message.setMissingFingersList(missingFingers);
			    message.setCurrentDeviceName(currentDeviceName);
			    message.setNoTimeout(noTimeout);
			    message.setSegmentedWsqRequired(segmentedWsqRequired);
			
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
				    String errorCode = WebsocketFingerprintErrorCodes.L0003_00009.getCode();
				    return ServiceResponse.failure(errorCode, e);
			    }
			    catch(NotConnectedException e)
			    {
				    throw e;
			    }
			    catch(Exception e)
			    {
				    String errorCode = WebsocketFingerprintErrorCodes.L0003_00010.getCode();
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
				                                new ServiceResponse.TypeCaster<CaptureFingerprintResponse, Message>()
				    {
					    @Override
					    public CaptureFingerprintResponse cast(Message m)
					    {
						    return new CaptureFingerprintResponse(m);
					    }
				    });
			    }
			    catch(InterruptedException e)
			    {
				    String errorCode = WebsocketFingerprintErrorCodes.L0003_00011.getCode();
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
				    String errorCode = WebsocketFingerprintErrorCodes.L0003_00012.getCode();
				    return ServiceResponse.failure(errorCode, e);
			    }
			    finally
			    {
				    asyncClientProxy.unregisterConsumer(consumer);
			    }
		    }
	    };
	
	    FutureTask<ServiceResponse<CaptureFingerprintResponse>> futureTask = new FutureTask<ServiceResponse<CaptureFingerprintResponse>>(callable);
	    executorService.submit(futureTask);
	    return futureTask;
    }

    @Override
    public Future<ServiceResponse<FingerprintStopPreviewResponse>> cancelCapture(final String currentDeviceName,
                                                                                 final int position)
    {
	    Callable<ServiceResponse<FingerprintStopPreviewResponse>> callable =
			                                        new Callable<ServiceResponse<FingerprintStopPreviewResponse>>()
	    {
		    @Override
		    public ServiceResponse<FingerprintStopPreviewResponse> call() throws TimeoutException,
		                                                                         NotConnectedException,
		                                                                         CancellationException
		    {
			    String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
			
			    Message message = new Message();
			    message.setTransactionId(transactionId);
			    message.setType(ServiceType.FINGERPRINT.getType());
			    message.setOperation(WebsocketCommand.CANCEL_CAPTURE.getCommand());
			    message.setPosition(position);
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
				    String errorCode = WebsocketFingerprintErrorCodes.L0003_00013.getCode();
				    return ServiceResponse.failure(errorCode, e);
			    }
			    catch(NotConnectedException e)
			    {
				    throw e;
			    }
			    catch(Exception e)
			    {
				    String errorCode = WebsocketFingerprintErrorCodes.L0003_00014.getCode();
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
			                                new ServiceResponse.TypeCaster<FingerprintStopPreviewResponse, Message>()
				    {
					    @Override
					    public FingerprintStopPreviewResponse cast(Message m)
					    {
						    return new FingerprintStopPreviewResponse(m);
					    }
				    });
			    }
			    catch(InterruptedException e)
			    {
				    String errorCode = WebsocketFingerprintErrorCodes.L0003_00015.getCode();
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
				    String errorCode = WebsocketFingerprintErrorCodes.L0003_00016.getCode();
				    return ServiceResponse.failure(errorCode, e);
			    }
			    finally
			    {
				    asyncClientProxy.unregisterConsumer(consumer);
			    }
		    }
	    };
	
	    FutureTask<ServiceResponse<FingerprintStopPreviewResponse>> futureTask = new FutureTask<ServiceResponse<FingerprintStopPreviewResponse>>(callable);
	    executorService.submit(futureTask);
	    return futureTask;
    }
}