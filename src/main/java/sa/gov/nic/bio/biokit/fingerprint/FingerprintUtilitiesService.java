package sa.gov.nic.bio.biokit.fingerprint;

import sa.gov.nic.bio.biokit.beans.ServiceResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.ConvertedFingerprintsResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.DuplicatedFingerprintsResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.SegmentFingerprintsResponse;
import sa.gov.nic.bio.biokit.websocket.beans.DMFingerData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface FingerprintUtilitiesService
{
    Future<ServiceResponse<DuplicatedFingerprintsResponse>> findDuplicatedFingerprints(Map<Integer, String> gallery,
                                                                                       Map<Integer, String> probes);
    Future<ServiceResponse<SegmentFingerprintsResponse>> segmentSlap(String slapImageBase64, String slapImageFormat,
                                                                     int position, int expectedFingersCount,
                                                                     List<Integer> missingFingers);
    Future<ServiceResponse<ConvertedFingerprintsResponse>> convertWsqToImages(Map<Integer, String> fingerprintWsqMap);
}