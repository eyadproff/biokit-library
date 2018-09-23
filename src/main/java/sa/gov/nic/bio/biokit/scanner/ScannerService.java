package sa.gov.nic.bio.biokit.scanner;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.commons.ServiceResponse;
import sa.gov.nic.bio.biokit.scanner.beans.ScanResponse;

import java.util.concurrent.Future;

public interface ScannerService extends DeviceService
{
    Future<ServiceResponse<ScanResponse>> scan();
}