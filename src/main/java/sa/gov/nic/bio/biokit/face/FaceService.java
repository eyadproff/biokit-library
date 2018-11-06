package sa.gov.nic.bio.biokit.face;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.beans.LivePreviewingResponse;
import sa.gov.nic.bio.commons.TaskResponse;
import sa.gov.nic.bio.biokit.face.beans.CaptureFaceResponse;
import sa.gov.nic.bio.biokit.face.beans.FaceStartPreviewResponse;

import java.util.concurrent.Future;

public interface FaceService extends DeviceService
{
    Future<TaskResponse<InitializeResponse>> initialize();
    Future<TaskResponse<InitializeResponse>> deinitialize(String currentDeviceName);
    Future<TaskResponse<FaceStartPreviewResponse>> startPreview(String currentDeviceName,
                                                                ResponseProcessor<LivePreviewingResponse> responseProcessor);
    Future<TaskResponse<FaceStopPreviewResponse>> stopPreview(String currentDeviceName);
    Future<TaskResponse<CaptureFaceResponse>> captureFace(String currentDeviceName, boolean applyIcao);
}