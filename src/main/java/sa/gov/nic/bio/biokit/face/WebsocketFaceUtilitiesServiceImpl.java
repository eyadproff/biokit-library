package sa.gov.nic.bio.biokit.face;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.biokit.AsyncConsumer;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.face.beans.GetIcaoImageResponse;
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

public class WebsocketFaceUtilitiesServiceImpl implements FaceUtilitiesService
{
	private ExecutorService executorService = Executors.newCachedThreadPool();
    private AsyncClientProxy<Message> asyncClientProxy;

    public WebsocketFaceUtilitiesServiceImpl(AsyncClientProxy<Message> asyncClientProxy)
    {
        this.asyncClientProxy = asyncClientProxy;
    }
	
	@Override
	public Future<TaskResponse<GetIcaoImageResponse>> getIcaoImage(final String photoBase64)
	{
		final Callable<TaskResponse<GetIcaoImageResponse>> callable =
																	new Callable<TaskResponse<GetIcaoImageResponse>>()
		{
			@Override
			public TaskResponse<GetIcaoImageResponse> call() throws TimeoutException,
			                                                        NotConnectedException,
			                                                        CancellationException
			{
				String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
				
				Message message = new Message();
				message.setTransactionId(transactionId);
				message.setType(ServiceType.FACE.getType());
				message.setOperation(WebsocketCommand.GET_ICAO_IMAGE.getCommand());
				message.setFinalImage(photoBase64);
				
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
					String errorCode = WebsocketFaceUtilitiesErrorCodes.L0008_00001.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketFaceUtilitiesErrorCodes.L0008_00002.getCode();
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
					                         new TaskResponse.TypeCaster<GetIcaoImageResponse, Message>()
					{
					    @Override
					    public GetIcaoImageResponse cast(Message m)
					    {
					        return new GetIcaoImageResponse(m);
					    }
					});
				}
				catch(InterruptedException e)
				{
					String errorCode = WebsocketFaceUtilitiesErrorCodes.L0008_00003.getCode();
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
					String errorCode = WebsocketFaceUtilitiesErrorCodes.L0008_00004.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<TaskResponse<GetIcaoImageResponse>> futureTask =
														new FutureTask<TaskResponse<GetIcaoImageResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
}