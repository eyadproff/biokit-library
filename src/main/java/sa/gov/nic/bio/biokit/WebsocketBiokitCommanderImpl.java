package sa.gov.nic.bio.biokit;

import sa.gov.nic.bio.biokit.beans.ServiceResponse;
import sa.gov.nic.bio.biokit.beans.ShutdownResponse;
import sa.gov.nic.bio.biokit.beans.UpdateResponse;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
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

public class WebsocketBiokitCommanderImpl implements BiokitCommander
{
	private ExecutorService executorService = Executors.newCachedThreadPool();
    private AsyncClientProxy<Message> asyncClientProxy;

    WebsocketBiokitCommanderImpl(AsyncClientProxy<Message> asyncClientProxy)
    {
        this.asyncClientProxy = asyncClientProxy;
    }
	
	@Override
	public Future<ServiceResponse<ShutdownResponse>> shutdown()
	{
		final Callable<ServiceResponse<ShutdownResponse>> callable = new Callable<ServiceResponse<ShutdownResponse>>()
		{
			@Override
			public ServiceResponse<ShutdownResponse> call() throws TimeoutException, NotConnectedException,
			                                                       CancellationException
			{
				String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
				
				Message message = new Message();
				message.setTransactionId(transactionId);
				message.setOperation(WebsocketCommand.SHUTDOWN.getCommand());
				
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
					String errorCode = WebsocketBiokitCommanderErrorCodes.L0005_00001.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketBiokitCommanderErrorCodes.L0005_00002.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
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
					                                        new ServiceResponse.TypeCaster<ShutdownResponse, Message>()
					{
						@Override
						public ShutdownResponse cast(Message m)
						{
							return new ShutdownResponse(m);
						}
					});
				}
				catch(InterruptedException e)
				{
					String errorCode = WebsocketBiokitCommanderErrorCodes.L0005_00003.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
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
					String errorCode = WebsocketBiokitCommanderErrorCodes.L0005_00004.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<ServiceResponse<ShutdownResponse>> futureTask = new FutureTask<ServiceResponse<ShutdownResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
	
	@Override
	public Future<ServiceResponse<UpdateResponse>> update()
	{
		final Callable<ServiceResponse<UpdateResponse>> callable = new Callable<ServiceResponse<UpdateResponse>>()
		{
			@Override
			public ServiceResponse<UpdateResponse> call() throws TimeoutException, NotConnectedException,
			                                                     CancellationException
			{
				String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
				
				Message message = new Message();
				message.setTransactionId(transactionId);
				message.setOperation(WebsocketCommand.UPDATER.getCommand());
				
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
					String errorCode = WebsocketBiokitCommanderErrorCodes.L0005_00005.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketBiokitCommanderErrorCodes.L0005_00006.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
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
				                                            new ServiceResponse.TypeCaster<UpdateResponse, Message>()
					{
						@Override
						public UpdateResponse cast(Message m)
						{
							return new UpdateResponse(m);
						}
					});
				}
				catch(InterruptedException e)
				{
					String errorCode = WebsocketBiokitCommanderErrorCodes.L0005_00007.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
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
					String errorCode = WebsocketBiokitCommanderErrorCodes.L0005_00008.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<ServiceResponse<UpdateResponse>> futureTask = new FutureTask<ServiceResponse<UpdateResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
}