package sa.gov.nic.bio.biokit;

public enum WebsocketBiokitCommanderErrorCodes
{
	L0005_00001, L0005_00002, L0005_00003, L0005_00004, L0005_00005, L0005_00006, L0005_00007, L0005_00008;
	
	public final String getCode()
	{
		return name().replace("_", "-");
	}
}