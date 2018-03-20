package sa.gov.nic.bio.biokit.fingerprint;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.beans.ServiceResponse;
import sa.gov.nic.bio.biokit.beans.LivePreviewingResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.CaptureFingerprintResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.FingerprintStopPreviewResponse;

import java.util.List;
import java.util.concurrent.Future;

public interface FingerprintService extends DeviceService
{
	Future<ServiceResponse<InitializeResponse>> initialize(int position);
	Future<ServiceResponse<InitializeResponse>> deinitialize(int position, String currentDeviceName);
	Future<ServiceResponse<CaptureFingerprintResponse>> startPreviewAndAutoCapture(String currentDeviceName,
                                       int position, int expectedFingersCount, List<Integer> missingFingers,
                                       boolean noTimeout, ResponseProcessor<LivePreviewingResponse> responseProcessor);
	Future<ServiceResponse<FingerprintStopPreviewResponse>> cancelCapture(String currentDeviceName, int position);
}