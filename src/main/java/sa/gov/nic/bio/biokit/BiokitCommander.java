package sa.gov.nic.bio.biokit;

import sa.gov.nic.bio.commons.TaskResponse;
import sa.gov.nic.bio.biokit.beans.ShutdownResponse;
import sa.gov.nic.bio.biokit.beans.UpdateResponse;

import java.util.concurrent.Future;

public interface BiokitCommander
{
	Future<TaskResponse<ShutdownResponse>> shutdown();
	Future<TaskResponse<UpdateResponse>> update();
}