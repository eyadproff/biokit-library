package sa.gov.nic.bio.biokit.iris;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.biokit.AsyncConsumer;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.iris.beans.CaptureIrisResponse;
import sa.gov.nic.bio.biokit.websocket.ServiceType;
import sa.gov.nic.bio.biokit.websocket.WebsocketClient;
import sa.gov.nic.bio.biokit.websocket.WebsocketCommand;
import sa.gov.nic.bio.biokit.websocket.beans.Message;
import sa.gov.nic.bio.commons.TaskResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class WebsocketIrisServiceImpl implements IrisService
{
	private ExecutorService executorService = Executors.newCachedThreadPool();
    private AsyncClientProxy<Message> asyncClientProxy;

    public WebsocketIrisServiceImpl(AsyncClientProxy<Message> asyncClientProxy)
    {
        this.asyncClientProxy = asyncClientProxy;
    }
	
	@Override
	public Future<TaskResponse<InitializeResponse>> initialize()
	{
		Callable<TaskResponse<InitializeResponse>> callable = new Callable<TaskResponse<InitializeResponse>>()
		{
			@Override
			public TaskResponse<InitializeResponse> call() throws TimeoutException, NotConnectedException, CancellationException
			{
				String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
				
				Message message = new Message();
				message.setTransactionId(transactionId);
				message.setType(ServiceType.IRIS.getType());
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
					String errorCode = WebsocketIrisErrorCodes.L0009_00001.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketIrisErrorCodes.L0009_00002.getCode();
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
					String errorCode = WebsocketIrisErrorCodes.L0009_00003.getCode();
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
					String errorCode = WebsocketIrisErrorCodes.L0009_00004.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<TaskResponse<InitializeResponse>> futureTask = new FutureTask<TaskResponse<InitializeResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
	
	@Override
    public Future<TaskResponse<CaptureIrisResponse>> capture(final String currentDeviceName, final int position)
    {
	    Callable<TaskResponse<CaptureIrisResponse>> callable =
			                                            new Callable<TaskResponse<CaptureIrisResponse>>()
	    {
		    @Override
		    public TaskResponse<CaptureIrisResponse> call() throws TimeoutException, NotConnectedException,
		                                                                  CancellationException
		    {
			    String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
			
			    Message message = new Message();
			    message.setTransactionId(transactionId);
			    message.setType(ServiceType.IRIS.getType());
			    message.setOperation(WebsocketCommand.CAPTURE.getCommand());
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
				    String errorCode = WebsocketIrisErrorCodes.L0009_00005.getCode();
				    return TaskResponse.failure(errorCode, e);
			    }
			    catch(NotConnectedException e)
			    {
				    throw e;
			    }
			    catch(Exception e)
			    {
				    String errorCode = WebsocketIrisErrorCodes.L0009_00006.getCode();
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
				                             new TaskResponse.TypeCaster<CaptureIrisResponse, Message>()
				    {
					    @Override
					    public CaptureIrisResponse cast(Message m)
					    {
						    return new CaptureIrisResponse(m);
					    }
				    });
			    }
			    catch(InterruptedException e)
			    {
				    String errorCode = WebsocketIrisErrorCodes.L0009_00007.getCode();
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
				    String errorCode = WebsocketIrisErrorCodes.L0009_00008.getCode();
				    return TaskResponse.failure(errorCode, e);
			    }
			    finally
			    {
				    asyncClientProxy.unregisterConsumer(consumer);
			    }
		    }
	    };
	
	    FutureTask<TaskResponse<CaptureIrisResponse>> futureTask = new FutureTask<TaskResponse<CaptureIrisResponse>>(callable);
	    executorService.submit(futureTask);
	    return futureTask;
    }
}