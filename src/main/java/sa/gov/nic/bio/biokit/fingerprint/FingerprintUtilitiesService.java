package sa.gov.nic.bio.biokit.fingerprint;

import sa.gov.nic.bio.commons.TaskResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.ConvertedFingerprintImagesResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.ConvertedFingerprintWsqResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.DuplicatedFingerprintsResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.SegmentFingerprintsResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface FingerprintUtilitiesService
{
    Future<TaskResponse<DuplicatedFingerprintsResponse>> findDuplicatedFingerprints(Map<Integer, String> gallery,
                                                                                    Map<Integer, String> probes);
    Future<TaskResponse<SegmentFingerprintsResponse>> segmentSlap(String slapImageBase64, String slapImageFormat,
                                                                  int position, int expectedFingersCount,
                                                                  List<Integer> missingFingers);
    Future<TaskResponse<ConvertedFingerprintImagesResponse>> convertWsqToImages(Map<Integer, String> fingerprintWsqMap);
    Future<TaskResponse<ConvertedFingerprintWsqResponse>> convertImagesToWsq(Map<Integer, String> fingerprintImagesMap);
}