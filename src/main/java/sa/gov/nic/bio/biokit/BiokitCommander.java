package sa.gov.nic.bio.biokit;

import sa.gov.nic.bio.biokit.beans.ServiceResponse;
import sa.gov.nic.bio.biokit.beans.ShutdownResponse;
import sa.gov.nic.bio.biokit.beans.UpdateResponse;

import java.util.concurrent.Future;

public interface BiokitCommander
{
	Future<ServiceResponse<ShutdownResponse>> shutdown();
	Future<ServiceResponse<UpdateResponse>> update();
}