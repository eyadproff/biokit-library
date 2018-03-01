package sa.gov.nic.bio.biokit.face;

import sa.gov.nic.bio.biokit.DeviceService;
import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.beans.ServiceResponse;
import sa.gov.nic.bio.biokit.beans.StartPreviewResponse;
import sa.gov.nic.bio.biokit.face.beans.CaptureFaceResponse;

import java.util.concurrent.Future;

public interface FaceService extends DeviceService
{
    Future<ServiceResponse<InitializeResponse>> initialize();
    Future<ServiceResponse<InitializeResponse>> deinitialize(String currentDeviceName);
    Future<ServiceResponse<Void>> startPreview(String currentDeviceName, ResponseProcessor<StartPreviewResponse> responseProcessor);
    Future<ServiceResponse<FaceStopPreviewResponse>> stopPreview(String currentDeviceName);
    Future<ServiceResponse<CaptureFaceResponse>> captureFace(String currentDeviceName, boolean applyIcao);
}