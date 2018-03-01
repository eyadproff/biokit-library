package sa.gov.nic.bio.biokit.fingerprint;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.biokit.beans.ServiceResponse;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.fingerprint.beans.DuplicatedFingerprintsResponse;
import sa.gov.nic.bio.biokit.websocket.ServiceType;
import sa.gov.nic.bio.biokit.websocket.WebsocketClient;
import sa.gov.nic.bio.biokit.AsyncConsumer;
import sa.gov.nic.bio.biokit.websocket.WebsocketCommand;
import sa.gov.nic.bio.biokit.websocket.beans.DMFingerData;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class WebsocketFingerprintUtilitiesServiceImpl implements FingerprintUtilitiesService
{
	private ExecutorService executorService = Executors.newCachedThreadPool();
    private AsyncClientProxy<Message> asyncClientProxy;

    public WebsocketFingerprintUtilitiesServiceImpl(AsyncClientProxy<Message> asyncClientProxy)
    {
        this.asyncClientProxy = asyncClientProxy;
    }

	@Override
	public Future<ServiceResponse<DuplicatedFingerprintsResponse>> findDuplicatedFingerprints(final Map<Integer, String> gallery, final Map<Integer, String> probes)
	{
		final Callable<ServiceResponse<DuplicatedFingerprintsResponse>> callable = new Callable<ServiceResponse<DuplicatedFingerprintsResponse>>()
		{
			@Override
			public ServiceResponse<DuplicatedFingerprintsResponse> call() throws TimeoutException, NotConnectedException
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
					dmFingerData.setPosition(entry.getKey());
					dmFingerData.setTemplate(entry.getValue());
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
					String errorCode = "L0004-00001";
					return ServiceResponse.failureResponse(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = "L0004-00002";
					return ServiceResponse.failureResponse(errorCode, e);
				}
				finally
				{
					if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
				}
				
				try
				{
					ServiceResponse<Message> messageServiceResponse = consumer.processResponses(null, asyncClientProxy.getResponseTimeoutSeconds(), TimeUnit.SECONDS);
					return ServiceResponse.cast(messageServiceResponse, new ServiceResponse.TypeCaster<DuplicatedFingerprintsResponse, Message>()
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
					String errorCode = "L0004-00003";
					return ServiceResponse.failureResponse(errorCode, e);
				}
				catch(TimeoutException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = "L0004-00004";
					return ServiceResponse.failureResponse(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<ServiceResponse<DuplicatedFingerprintsResponse>> futureTask = new FutureTask<ServiceResponse<DuplicatedFingerprintsResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
}