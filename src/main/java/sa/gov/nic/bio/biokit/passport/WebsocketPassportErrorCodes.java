package sa.gov.nic.bio.biokit.passport;

public enum WebsocketPassportErrorCodes
{
	L0006_00001, L0006_00002, L0006_00003, L0006_00004, L0006_00005, L0006_00006, L0006_00007, L0006_00008, L0006_00009,
	L0006_00010, L0006_00011, L0006_00012;
	
	public final String getCode()
	{
		return name().replace("_", "-");
	}
}