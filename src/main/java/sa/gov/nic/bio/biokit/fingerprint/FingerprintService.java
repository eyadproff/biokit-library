package sa.gov.nic.bio.biokit.fingerprint;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.commons.TaskResponse;
import sa.gov.nic.bio.biokit.beans.LivePreviewingResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.CaptureFingerprintResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.FingerprintStopPreviewResponse;

import java.util.List;
import java.util.concurrent.Future;

public interface FingerprintService extends DeviceService
{
	Future<TaskResponse<InitializeResponse>> initialize(int position);
	Future<TaskResponse<InitializeResponse>> deinitialize(int position, String currentDeviceName);
	Future<TaskResponse<CaptureFingerprintResponse>> startPreviewAndAutoCapture(String currentDeviceName,
	                                                                            int position, int expectedFingersCount, List<Integer> missingFingers,
	                                                                            boolean noTimeout, boolean segmentedWsqRequired, boolean segmentationRequired,
	                                                                            ResponseProcessor<LivePreviewingResponse> responseProcessor);
	Future<TaskResponse<FingerprintStopPreviewResponse>> cancelCapture(String currentDeviceName, int position);
}