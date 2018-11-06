package sa.gov.nic.bio.biokit.websocket;

public interface ClosureListener
{
	void onClose(String closeCode, String reasonPhrase);
}