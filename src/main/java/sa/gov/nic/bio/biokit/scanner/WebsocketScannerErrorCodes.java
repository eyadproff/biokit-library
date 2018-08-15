package sa.gov.nic.bio.biokit.scanner;

public enum WebsocketScannerErrorCodes
{
	L0007_00001, L0007_00002, L0007_00003, L0007_00004;
	
	public final String getCode()
	{
		return name().replace("_", "-");
	}
}