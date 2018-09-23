package sa.gov.nic.bio.biokit.passport;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.biokit.AsyncConsumer;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.commons.ServiceResponse;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.passport.beans.CapturePassportResponse;
import sa.gov.nic.bio.biokit.websocket.ServiceType;
import sa.gov.nic.bio.biokit.websocket.WebsocketClient;
import sa.gov.nic.bio.biokit.websocket.WebsocketCommand;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class WebsocketPassportScannerServiceImpl implements PassportScannerService
{
	private ExecutorService executorService = Executors.newCachedThreadPool();
    private AsyncClientProxy<Message> asyncClientProxy;

    public WebsocketPassportScannerServiceImpl(AsyncClientProxy<Message> asyncClientProxy)
    {
        this.asyncClientProxy = asyncClientProxy;
    }
	
	@Override
	public Future<ServiceResponse<InitializeResponse>> initialize()
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
				message.setType(ServiceType.PASSPORT.getType());
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
					String errorCode = WebsocketPassportErrorCodes.L0006_00001.getCode();
					return ServiceResponse.failure(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketPassportErrorCodes.L0006_00002.getCode();
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
					String errorCode = WebsocketPassportErrorCodes.L0006_00003.getCode();
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
					String errorCode = WebsocketPassportErrorCodes.L0006_00004.getCode();
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
	public Future<ServiceResponse<InitializeResponse>> deinitialize(final String currentDeviceName)
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
				message.setType(ServiceType.PASSPORT.getType());
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
					String errorCode = WebsocketPassportErrorCodes.L0006_00005.getCode();
					return ServiceResponse.failure(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketPassportErrorCodes.L0006_00006.getCode();
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
					String errorCode = WebsocketPassportErrorCodes.L0006_00007.getCode();
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
					String errorCode = WebsocketPassportErrorCodes.L0006_00008.getCode();
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
    public Future<ServiceResponse<CapturePassportResponse>> capture(final String currentDeviceName)
    {
	    Callable<ServiceResponse<CapturePassportResponse>> callable =
			                                            new Callable<ServiceResponse<CapturePassportResponse>>()
	    {
		    @Override
		    public ServiceResponse<CapturePassportResponse> call() throws TimeoutException, NotConnectedException,
		                                                                  CancellationException
		    {
			    String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
			
			    Message message = new Message();
			    message.setTransactionId(transactionId);
			    message.setType(ServiceType.PASSPORT.getType());
			    message.setOperation(WebsocketCommand.CAPTURE.getCommand());
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
				    String errorCode = WebsocketPassportErrorCodes.L0006_00009.getCode();
				    return ServiceResponse.failure(errorCode, e);
			    }
			    catch(NotConnectedException e)
			    {
				    throw e;
			    }
			    catch(Exception e)
			    {
				    String errorCode = WebsocketPassportErrorCodes.L0006_00010.getCode();
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
				                                new ServiceResponse.TypeCaster<CapturePassportResponse, Message>()
				    {
					    @Override
					    public CapturePassportResponse cast(Message m)
					    {
						    return new CapturePassportResponse(m);
					    }
				    });
			    }
			    catch(InterruptedException e)
			    {
				    String errorCode = WebsocketPassportErrorCodes.L0006_00011.getCode();
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
				    String errorCode = WebsocketPassportErrorCodes.L0006_00012.getCode();
				    return ServiceResponse.failure(errorCode, e);
			    }
			    finally
			    {
				    asyncClientProxy.unregisterConsumer(consumer);
			    }
		    }
	    };
	
	    FutureTask<ServiceResponse<CapturePassportResponse>> futureTask =
			                                        new FutureTask<ServiceResponse<CapturePassportResponse>>(callable);
	    executorService.submit(futureTask);
	    return futureTask;
    }
}