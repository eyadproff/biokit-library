package sa.gov.nic.bio.biokit.fingerprint;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.biokit.beans.ServiceResponse;
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
	public Future<ServiceResponse<DuplicatedFingerprintsResponse>> findDuplicatedFingerprints(
												final Map<Integer, String> gallery, final Map<Integer, String> probes)
	{
		final Callable<ServiceResponse<DuplicatedFingerprintsResponse>> callable =
													new Callable<ServiceResponse<DuplicatedFingerprintsResponse>>()
		{
			@Override
			public ServiceResponse<DuplicatedFingerprintsResponse> call() throws TimeoutException,
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
					return ServiceResponse.failureResponse(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00002.getCode();
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
				                            new ServiceResponse.TypeCaster<DuplicatedFingerprintsResponse, Message>()
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00004.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<ServiceResponse<DuplicatedFingerprintsResponse>> futureTask =
										new FutureTask<ServiceResponse<DuplicatedFingerprintsResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
	
	@Override
	public Future<ServiceResponse<SegmentFingerprintsResponse>> segmentSlap(final String slapImageBase64,
	                                                                        final String slapImageFormat,
	                                                                        final int position,
	                                                                        final int expectedFingersCount,
	                                                                        final List<Integer> missingFingers)
	{
		final Callable<ServiceResponse<SegmentFingerprintsResponse>> callable =
													new Callable<ServiceResponse<SegmentFingerprintsResponse>>()
		{
			@Override
			public ServiceResponse<SegmentFingerprintsResponse> call() throws TimeoutException,
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
					return ServiceResponse.failureResponse(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00006.getCode();
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
					new ServiceResponse.TypeCaster<SegmentFingerprintsResponse, Message>()
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00008.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<ServiceResponse<SegmentFingerprintsResponse>> futureTask =
											new FutureTask<ServiceResponse<SegmentFingerprintsResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
	
	@Override
	public Future<ServiceResponse<ConvertedFingerprintImagesResponse>> convertWsqToImages(
																final Map<Integer, String> fingerprintWsqMap)
	{
		final Callable<ServiceResponse<ConvertedFingerprintImagesResponse>> callable =
												new Callable<ServiceResponse<ConvertedFingerprintImagesResponse>>()
		{
			@Override
			public ServiceResponse<ConvertedFingerprintImagesResponse> call() throws TimeoutException,
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
					return ServiceResponse.failureResponse(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00010.getCode();
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
					new ServiceResponse.TypeCaster<ConvertedFingerprintImagesResponse, Message>()
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00012.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<ServiceResponse<ConvertedFingerprintImagesResponse>> futureTask =
											new FutureTask<ServiceResponse<ConvertedFingerprintImagesResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
	
	@Override
	public Future<ServiceResponse<ConvertedFingerprintWsqResponse>> convertImagesToWsq(
																		final Map<Integer, String> fingerprintImagesMap)
	{
		final Callable<ServiceResponse<ConvertedFingerprintWsqResponse>> callable =
														new Callable<ServiceResponse<ConvertedFingerprintWsqResponse>>()
		{
			@Override
			public ServiceResponse<ConvertedFingerprintWsqResponse> call() throws TimeoutException,
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
					return ServiceResponse.failureResponse(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00014.getCode();
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
				                            new ServiceResponse.TypeCaster<ConvertedFingerprintWsqResponse, Message>()
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
					String errorCode = WebsocketFingerprintUtilitiesErrorCodes.L0004_00016.getCode();
					return ServiceResponse.failureResponse(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<ServiceResponse<ConvertedFingerprintWsqResponse>> futureTask =
											new FutureTask<ServiceResponse<ConvertedFingerprintWsqResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
}