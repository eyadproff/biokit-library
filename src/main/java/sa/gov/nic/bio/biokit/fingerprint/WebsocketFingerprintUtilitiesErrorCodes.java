package sa.gov.nic.bio.biokit.fingerprint;

public enum WebsocketFingerprintUtilitiesErrorCodes
{
	L0004_00001, L0004_00002, L0004_00003, L0004_00004, L0004_00005, L0004_00006, L0004_00007, L0004_00008, L0004_00009,
	L0004_00010, L0004_00011, L0004_00012;
	
	public final String getCode()
	{
		return name().replace("_", "-");
	}
}