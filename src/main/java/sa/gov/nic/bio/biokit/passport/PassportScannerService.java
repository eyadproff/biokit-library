package sa.gov.nic.bio.biokit.passport;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.commons.TaskResponse;
import sa.gov.nic.bio.biokit.passport.beans.CapturePassportResponse;

import java.util.concurrent.Future;

public interface PassportScannerService extends DeviceService
{
    Future<TaskResponse<InitializeResponse>> initialize();
    Future<TaskResponse<InitializeResponse>> deinitialize(String currentDeviceName);
    Future<TaskResponse<CapturePassportResponse>> capture(String currentDeviceName);
}