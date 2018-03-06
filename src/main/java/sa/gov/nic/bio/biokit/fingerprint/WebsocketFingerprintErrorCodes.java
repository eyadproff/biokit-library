package sa.gov.nic.bio.biokit.fingerprint;

public enum WebsocketFingerprintErrorCodes
{
	L0003_00001, L0003_00002, L0003_00003, L0003_00004, L0003_00005, L0003_00006, L0003_00007, L0003_00008, L0003_00009,
	L0003_00010, L0003_00011, L0003_00012, L0003_00013, L0003_00014, L0003_00015, L0003_00016;
	
	public final String getCode()
	{
		return name().replace("_", "-");
	}
}