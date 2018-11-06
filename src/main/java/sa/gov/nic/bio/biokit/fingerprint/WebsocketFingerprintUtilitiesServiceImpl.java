package sa.gov.nic.bio.biokit.fingerprint;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.commons.TaskResponse;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.fingerprint.beans.ConvertedFingerprintImagesResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.ConvertedFingerprintWsqResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.DuplicatedFingerprintsResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.SegmentFingerprintsResponse;
import sa.gov.nic.bio.biokit.websocket.ServiceType;
import sa.gov.nic.bio.biokit.websocket.WebsocketClient;
import sa.gov.nic.bio.biokit.AsyncConsumer;
import sa.gov.nic.bio.biokit.websocket.WebsocketCommand;
import sa.gov.nic.bio.biokit.websocket.beans.DMFingerData;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class WebsocketFingerprintUtilitiesServiceImpl implements FingerprintUtilitiesService
{
	private ExecutorService executorService = Executors.newCachedThreadPool();
    private AsyncClientProxy<Message> asyncClientProxy;

    public WebsocketFingerprintUtilitiesServiceImpl(AsyncClientProxy<Message> asyncClientProxy)
    {
        this.asyncClientProxy = asyncClientProxy;
    }

	@Override
	public Future<TaskResponse<DuplicatedFingerprintsResponse>> findDuplicatedFingerprints(
												final Map<Integer, String> gallery, final Map<Integer, String> probes)
	{
		final Callable<TaskResponse<DuplicatedFingerprintsResponse>> callable =
													new Callable<TaskResponse<DuplicatedFingerprintsResponse>>()
		{
			@Override
			public TaskResponse<DuplicatedFingerprintsResponse> call() throws TimeoutException,
			                                                                  NotConnectedException,
			                                                                  CancellationException
			{
				String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
				
				Message message = new Message();
				message.setTransactionId(transactionId);
				message.setType(ServiceType.FINGERPRINT.getType());
				message.setOperation(WebsocketCommand.FIND_DUPLICATES.getCommand());
				
				List<DMFingerData> galleryFingers = new ArrayList<DMFingerData>();
				List<DMFingerData> probeFingers = new ArrayList<DMFingerData>();
				
				for(Map.Entry<Integer, String> entry : gallery.entrySet())
				{
					DMFingerData dmFingerData = new DMFingerData();
					dmFingerData.setPosition(entry.getKey());
					dmFingerData.setTemplate(entry.getValue());
					galleryFingers.add(dmFingerData);
				}
				
				for(Map.Entry<Integer, String> entry : probes.entrySet())
				{
					DMFingerData dmFingerData = new DMFingerData();
					dmFingerData.setTemplate(entry.getValue());
					dmFingerData.setPosition(entry.getKey());
					probeFingers.add(dmFingerData);
				}
				
				message.setGalleryFingers(galleryFingers);
				message.setProbeFingers(probeFingers);
				
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00001.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00002.getCode();
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
					                         new TaskResponse.TypeCaster<DuplicatedFingerprintsResponse, Message>()
					{
						@Override
						public DuplicatedFingerprintsResponse cast(Message m)
						{
							return new DuplicatedFingerprintsResponse(m);
						}
					});
				}
				catch(InterruptedException e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00003.getCode();
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00004.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<TaskResponse<DuplicatedFingerprintsResponse>> futureTask =
										new FutureTask<TaskResponse<DuplicatedFingerprintsResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
	
	@Override
	public Future<TaskResponse<SegmentFingerprintsResponse>> segmentSlap(final String slapImageBase64,
	                                                                     final String slapImageFormat,
	                                                                     final int position,
	                                                                     final int expectedFingersCount,
	                                                                     final List<Integer> missingFingers)
	{
		final Callable<TaskResponse<SegmentFingerprintsResponse>> callable =
													new Callable<TaskResponse<SegmentFingerprintsResponse>>()
		{
			@Override
			public TaskResponse<SegmentFingerprintsResponse> call() throws TimeoutException,
			                                                               NotConnectedException,
			                                                               CancellationException
			{
				String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
				
				Message message = new Message();
				message.setTransactionId(transactionId);
				message.setType(ServiceType.FINGERPRINT.getType());
				message.setOperation(WebsocketCommand.GET_SEGMENTED_FINGERS.getCommand());
				message.setSlapImageForSegmentation(slapImageBase64);
				message.setImageFormat(slapImageFormat);
				message.setPosition(position);
				message.setExpectedFingersCount(String.valueOf(expectedFingersCount));
				message.setMissingFingersList(missingFingers);
				
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00005.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00006.getCode();
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
					                         new TaskResponse.TypeCaster<SegmentFingerprintsResponse, Message>()
					{
					    @Override
					    public SegmentFingerprintsResponse cast(Message m)
					    {
					        return new SegmentFingerprintsResponse(m);
					    }
					});
				}
				catch(InterruptedException e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00007.getCode();
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00008.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<TaskResponse<SegmentFingerprintsResponse>> futureTask =
											new FutureTask<TaskResponse<SegmentFingerprintsResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
	
	@Override
	public Future<TaskResponse<ConvertedFingerprintImagesResponse>> convertWsqToImages(
																final Map<Integer, String> fingerprintWsqMap)
	{
		final Callable<TaskResponse<ConvertedFingerprintImagesResponse>> callable =
												new Callable<TaskResponse<ConvertedFingerprintImagesResponse>>()
		{
			@Override
			public TaskResponse<ConvertedFingerprintImagesResponse> call() throws TimeoutException,
			                                                                      NotConnectedException,
			                                                                      CancellationException
			{
				String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
				
				List<DMFingerData> dmFingerDataList = new ArrayList<DMFingerData>();
				
				for(Map.Entry<Integer, String> entry : fingerprintWsqMap.entrySet())
				{
					DMFingerData dmFingerData = new DMFingerData();
					dmFingerData.setPosition(entry.getKey());
					dmFingerData.setFingerWsqImage(entry.getValue());
					dmFingerDataList.add(dmFingerData);
				}
				
				Message message = new Message();
				message.setTransactionId(transactionId);
				message.setType(ServiceType.FINGERPRINT.getType());
				message.setOperation(WebsocketCommand.CONVERT_WSQ_TO_IMAGE.getCommand());
				message.setDmSegmentedFingers(dmFingerDataList);
				
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00009.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00010.getCode();
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
					                         new TaskResponse.TypeCaster<ConvertedFingerprintImagesResponse, Message>()
					{
					    @Override
					    public ConvertedFingerprintImagesResponse cast(Message m)
					    {
					        return new ConvertedFingerprintImagesResponse(m);
					    }
					});
				}
				catch(InterruptedException e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00011.getCode();
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00012.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<TaskResponse<ConvertedFingerprintImagesResponse>> futureTask =
											new FutureTask<TaskResponse<ConvertedFingerprintImagesResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
	
	@Override
	public Future<TaskResponse<ConvertedFingerprintWsqResponse>> convertImagesToWsq(
																		final Map<Integer, String> fingerprintImagesMap)
	{
		final Callable<TaskResponse<ConvertedFingerprintWsqResponse>> callable =
														new Callable<TaskResponse<ConvertedFingerprintWsqResponse>>()
		{
			@Override
			public TaskResponse<ConvertedFingerprintWsqResponse> call() throws TimeoutException,
			                                                                   NotConnectedException,
			                                                                   CancellationException
			{
				String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
				
				List<DMFingerData> dmFingerDataList = new ArrayList<DMFingerData>();
				
				for(Map.Entry<Integer, String> entry : fingerprintImagesMap.entrySet())
				{
					DMFingerData dmFingerData = new DMFingerData();
					dmFingerData.setPosition(entry.getKey());
					dmFingerData.setFinger(entry.getValue());
					dmFingerDataList.add(dmFingerData);
				}
				
				Message message = new Message();
				message.setTransactionId(transactionId);
				message.setType(ServiceType.FINGERPRINT.getType());
				message.setOperation(WebsocketCommand.CONVERT_IMAGE_TO_WSQ.getCommand());
				message.setDmSegmentedFingers(dmFingerDataList);
				
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00013.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00014.getCode();
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
					                         new TaskResponse.TypeCaster<ConvertedFingerprintWsqResponse, Message>()
					{
					    @Override
					    public ConvertedFingerprintWsqResponse cast(Message m)
					    {
					        return new ConvertedFingerprintWsqResponse(m);
					    }
					});
				}
				catch(InterruptedException e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00015.getCode();
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00016.getCode();
					return TaskResponse.failure(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<TaskResponse<ConvertedFingerprintWsqResponse>> futureTask =
											new FutureTask<TaskResponse<ConvertedFingerprintWsqResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
}