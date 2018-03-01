package sa.gov.nic.bio.biokit;

import sa.gov.nic.bio.biokit.face.FaceService;
import sa.gov.nic.bio.biokit.face.WebsocketFaceServiceImpl;
import sa.gov.nic.bio.biokit.fingerprint.FingerprintService;
import sa.gov.nic.bio.biokit.fingerprint.WebsocketFingerprintServiceImpl;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

public final class DeviceServiceFactory
{
    public static FaceService getFaceService(AsyncClientProxy<Message> asyncClientProxy)
    {
        return new WebsocketFaceServiceImpl(asyncClientProxy);
    }
    
    public static FingerprintService getFingerprintService(AsyncClientProxy<Message> asyncClientProxy)
    {
        return new WebsocketFingerprintServiceImpl(asyncClientProxy);
    }
}