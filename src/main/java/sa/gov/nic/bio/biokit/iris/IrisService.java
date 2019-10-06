package sa.gov.nic.bio.biokit.iris;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.beans.LivePreviewingResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.CaptureFingerprintResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.FingerprintStopPreviewResponse;
import sa.gov.nic.bio.biokit.iris.beans.CaptureIrisResponse;
import sa.gov.nic.bio.commons.TaskResponse;

import java.util.List;
import java.util.concurrent.Future;

public interface IrisService extends DeviceService
{
	Future<TaskResponse<InitializeResponse>> initialize();
	Future<TaskResponse<CaptureIrisResponse>> capture(String currentDeviceName, int position);
}