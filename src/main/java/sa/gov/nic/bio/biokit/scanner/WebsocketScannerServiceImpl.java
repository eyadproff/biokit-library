package sa.gov.nic.bio.biokit.scanner;

import sa.gov.nic.bio.biokit.AsyncClientProxy;
import sa.gov.nic.bio.biokit.AsyncConsumer;
import sa.gov.nic.bio.commons.ServiceResponse;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.scanner.beans.ScanResponse;
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

public class WebsocketScannerServiceImpl implements ScannerService
{
	private ExecutorService executorService = Executors.newCachedThreadPool();
    private AsyncClientProxy<Message> asyncClientProxy;

    public WebsocketScannerServiceImpl(AsyncClientProxy<Message> asyncClientProxy)
    {
        this.asyncClientProxy = asyncClientProxy;
    }
	
	@Override
	public Future<ServiceResponse<ScanResponse>> scan()
	{
		Callable<ServiceResponse<ScanResponse>> callable = new Callable<ServiceResponse<ScanResponse>>()
		{
			@Override
			public ServiceResponse<ScanResponse> call() throws TimeoutException, NotConnectedException,
                                                               CancellationException
			{
				String transactionId = String.valueOf(WebsocketClient.ID_GENERATOR.incrementAndGet());
				
				Message message = new Message();
				message.setTransactionId(transactionId);
				message.setType(ServiceType.FINGERPRINT.getType());
				message.setOperation(WebsocketCommand.SCAN_TENPRINT.getCommand());
				
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
					String errorCode = WebsocketScannerErrorCodes.L0007_00001.getCode();
					return ServiceResponse.failure(errorCode, e);
				}
				catch(NotConnectedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					String errorCode = WebsocketScannerErrorCodes.L0007_00002.getCode();
					return ServiceResponse.failure(errorCode, e);
				}
				finally
				{
					if(!successfullySent) asyncClientProxy.unregisterConsumer(consumer);
				}
				
				try
				{
					ServiceResponse<Message> messageServiceResponse = consumer.processResponses(null,
                                                                                            1, TimeUnit.HOURS);
					return ServiceResponse.cast(messageServiceResponse,
					                                            new ServiceResponse.TypeCaster<ScanResponse, Message>()
					{
					    @Override
					    public ScanResponse cast(Message m)
					    {
					        return new ScanResponse(m);
					    }
					});
				}
				catch(InterruptedException e)
				{
					String errorCode = WebsocketScannerErrorCodes.L0007_00003.getCode();
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
					String errorCode = WebsocketScannerErrorCodes.L0007_00004.getCode();
					return ServiceResponse.failure(errorCode, e);
				}
				finally
				{
					asyncClientProxy.unregisterConsumer(consumer);
				}
			}
		};
		
		FutureTask<ServiceResponse<ScanResponse>> futureTask = new FutureTask<ServiceResponse<ScanResponse>>(callable);
		executorService.submit(futureTask);
		return futureTask;
	}
}