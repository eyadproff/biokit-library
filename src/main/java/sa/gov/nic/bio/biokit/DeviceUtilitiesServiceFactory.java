package sa.gov.nic.bio.biokit;

import sa.gov.nic.bio.biokit.face.FaceUtilitiesService;
import sa.gov.nic.bio.biokit.face.WebsocketFaceUtilitiesServiceImpl;
import sa.gov.nic.bio.biokit.fingerprint.FingerprintUtilitiesService;
import sa.gov.nic.bio.biokit.fingerprint.WebsocketFingerprintUtilitiesServiceImpl;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

public final class DeviceUtilitiesServiceFactory
{
    public static FaceUtilitiesService getFaceUtilitiesService(AsyncClientProxy<Message> asyncClientProxy)
    {
        return new WebsocketFaceUtilitiesServiceImpl(asyncClientProxy);
    }
    
    public static FingerprintUtilitiesService getFingerprintUtilitiesService(AsyncClientProxy<Message> asyncClientProxy)
    {
        return new WebsocketFingerprintUtilitiesServiceImpl(asyncClientProxy);
    }
}