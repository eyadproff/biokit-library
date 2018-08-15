package sa.gov.nic.bio.biokit;

import sa.gov.nic.bio.biokit.face.FaceService;
import sa.gov.nic.bio.biokit.face.WebsocketFaceServiceImpl;
import sa.gov.nic.bio.biokit.fingerprint.FingerprintService;
import sa.gov.nic.bio.biokit.fingerprint.WebsocketFingerprintServiceImpl;
import sa.gov.nic.bio.biokit.passport.PassportScannerService;
import sa.gov.nic.bio.biokit.passport.WebsocketPassportScannerServiceImpl;
import sa.gov.nic.bio.biokit.scanner.ScannerService;
import sa.gov.nic.bio.biokit.scanner.WebsocketScannerServiceImpl;
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
    
    public static PassportScannerService getPassportScannerService(AsyncClientProxy<Message> asyncClientProxy)
    {
        return new WebsocketPassportScannerServiceImpl(asyncClientProxy);
    }
    
    public static ScannerService getScannerService(AsyncClientProxy<Message> asyncClientProxy)
    {
        return new WebsocketScannerServiceImpl(asyncClientProxy);
    }
}