package sa.gov.nic.bio.biokit.websocket;

public enum WebsocketErrorCodes
{
	L0001_00001, L0001_00002, L0001_00003;
	
	public final String getCode()
	{
		return name().replace("_", "-");
	}
}