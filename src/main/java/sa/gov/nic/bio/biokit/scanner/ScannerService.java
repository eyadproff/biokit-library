package sa.gov.nic.bio.biokit.scanner;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.commons.TaskResponse;
import sa.gov.nic.bio.biokit.scanner.beans.ScanResponse;

import java.util.concurrent.Future;

public interface ScannerService extends DeviceService
{
    Future<TaskResponse<ScanResponse>> scan();
}