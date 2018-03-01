package sa.gov.nic.bio.biokit;

import sa.gov.nic.bio.biokit.fingerprint.FingerprintUtilitiesService;
import sa.gov.nic.bio.biokit.fingerprint.WebsocketFingerprintUtilitiesServiceImpl;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

public final class DeviceUtilitiesServiceFactory
{
    public static FingerprintUtilitiesService getFingerprintUtilitiesService(AsyncClientProxy<Message> asyncClientProxy)
    {
        return new WebsocketFingerprintUtilitiesServiceImpl(asyncClientProxy);
    }
}