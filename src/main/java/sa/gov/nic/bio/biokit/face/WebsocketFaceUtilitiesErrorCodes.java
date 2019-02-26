package sa.gov.nic.bio.biokit.face;

public enum WebsocketFaceUtilitiesErrorCodes
{
	L0008_00001, L0008_00002, L0008_00003, L0008_00004;
	
	public final String getCode()
	{
		return name().replace("_", "-");
	}
}