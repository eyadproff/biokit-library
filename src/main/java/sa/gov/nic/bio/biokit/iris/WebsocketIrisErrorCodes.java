package sa.gov.nic.bio.biokit.iris;

public enum WebsocketIrisErrorCodes
{
	L0009_00001, L0009_00002, L0009_00003, L0009_00004, L0009_00005, L0009_00006, L0009_00007, L0009_00008;
	
	public final String getCode()
	{
		return name().replace("_", "-");
	}
}