package sa.gov.nic.bio.biokit.face;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.beans.LivePreviewingResponse;
import sa.gov.nic.bio.commons.ServiceResponse;
import sa.gov.nic.bio.biokit.face.beans.CaptureFaceResponse;
import sa.gov.nic.bio.biokit.face.beans.FaceStartPreviewResponse;

import java.util.concurrent.Future;

public interface FaceService extends DeviceService
{
    Future<ServiceResponse<InitializeResponse>> initialize();
    Future<ServiceResponse<InitializeResponse>> deinitialize(String currentDeviceName);
    Future<ServiceResponse<FaceStartPreviewResponse>> startPreview(String currentDeviceName,
                                                       ResponseProcessor<LivePreviewingResponse> responseProcessor);
    Future<ServiceResponse<FaceStopPreviewResponse>> stopPreview(String currentDeviceName);
    Future<ServiceResponse<CaptureFaceResponse>> captureFace(String currentDeviceName, boolean applyIcao);
}