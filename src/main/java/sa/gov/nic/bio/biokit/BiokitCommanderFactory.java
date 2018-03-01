package sa.gov.nic.bio.biokit;

import sa.gov.nic.bio.biokit.fingerprint.WebsocketFingerprintUtilitiesServiceImpl;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

public final class BiokitCommanderFactory
{
    public static BiokitCommander getBiokitCommander(AsyncClientProxy<Message> asyncClientProxy)
    {
        return new WebsocketBiokitCommanderImpl(asyncClientProxy);
    }
}