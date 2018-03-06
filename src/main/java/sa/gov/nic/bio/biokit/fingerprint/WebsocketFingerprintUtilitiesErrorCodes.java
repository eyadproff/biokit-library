package sa.gov.nic.bio.biokit.fingerprint;

public enum WebsocketFingerprintUtilitiesErrorCodes
{
	L0004_00001, L0004_00002, L0004_00003, L0004_00004;
	
	public final String getCode()
	{
		return name().replace("_", "-");
	}
}