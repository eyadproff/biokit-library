package sa.gov.nic.bio.biokit.iris;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.iris.beans.CaptureIrisResponse;
import sa.gov.nic.bio.biokit.iris.beans.IrisiStopCaptureResponse;
import sa.gov.nic.bio.commons.TaskResponse;

import java.util.concurrent.Future;

public interface IrisService extends DeviceService
{
	Future<TaskResponse<InitializeResponse>> initialize();
	Future<TaskResponse<CaptureIrisResponse>> capture(String currentDeviceName, int position);
	Future<TaskResponse<IrisiStopCaptureResponse>> cancelCapture(String currentDeviceName, int position);
}