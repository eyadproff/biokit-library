package sa.gov.nic.bio.biokit.passport;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.commons.ServiceResponse;
import sa.gov.nic.bio.biokit.passport.beans.CapturePassportResponse;

import java.util.concurrent.Future;

public interface PassportScannerService extends DeviceService
{
    Future<ServiceResponse<InitializeResponse>> initialize();
    Future<ServiceResponse<InitializeResponse>> deinitialize(String currentDeviceName);
    Future<ServiceResponse<CapturePassportResponse>> capture(String currentDeviceName);
}