package sa.gov.nic.bio.biokit.face;

import sa.gov.nic.bio.biokit.face.beans.GetIcaoImageResponse;
import sa.gov.nic.bio.commons.TaskResponse;

import java.util.concurrent.Future;

public interface FaceUtilitiesService
{
    Future<TaskResponse<GetIcaoImageResponse>> getIcaoImage(String photoBase64);
}