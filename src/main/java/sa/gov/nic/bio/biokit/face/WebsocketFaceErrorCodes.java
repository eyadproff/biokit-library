package sa.gov.nic.bio.biokit.face;

public enum WebsocketFaceErrorCodes
{
	L0002_00001, L0002_00002, L0002_00003, L0002_00004, L0002_00005, L0002_00006, L0002_00007, L0002_00008, L0002_00009,
	L0002_00010, L0002_00011, L0002_00012, L0002_00013, L0002_00014, L0002_00015, L0002_00016, L0002_00017, L0002_00018,
	L0002_00019, L0002_00020;
	
	public final String getCode()
	{
		return name().replace("_", "-");
	}
}