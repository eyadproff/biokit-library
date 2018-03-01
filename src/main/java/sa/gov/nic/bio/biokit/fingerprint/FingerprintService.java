package sa.gov.nic.bio.biokit.fingerprint;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.beans.ServiceResponse;
import sa.gov.nic.bio.biokit.beans.StartPreviewResponse;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.fingerprint.beans.CaptureFingerprintResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.FingerprintStopPreviewResponse;

import java.util.List;
import java.util.concurrent.Future;

public interface FingerprintService extends DeviceService
{
	Future<ServiceResponse<InitializeResponse>> initialize(int position);
	Future<ServiceResponse<InitializeResponse>> deinitialize(final int position, String currentDeviceName);
	Future<ServiceResponse<CaptureFingerprintResponse>> startPreviewAndAutoCapture(String currentDeviceName, int position, int expectedFingersCount, List<Integer> missingFingers, ResponseProcessor<StartPreviewResponse> responseProcessor) throws TimeoutException, NotConnectedException;
	Future<ServiceResponse<FingerprintStopPreviewResponse>> cancelCapture(String currentDeviceName, int position) throws TimeoutException, NotConnectedException;
}