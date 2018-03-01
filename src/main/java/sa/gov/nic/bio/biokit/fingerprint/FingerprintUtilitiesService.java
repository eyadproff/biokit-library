package sa.gov.nic.bio.biokit.fingerprint;

import sa.gov.nic.bio.biokit.beans.ServiceResponse;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.fingerprint.beans.DuplicatedFingerprintsResponse;

import java.util.Map;
import java.util.concurrent.Future;

public interface FingerprintUtilitiesService
{
    Future<ServiceResponse<DuplicatedFingerprintsResponse>> findDuplicatedFingerprints(Map<Integer, String> gallery, Map<Integer, String> probes);
}