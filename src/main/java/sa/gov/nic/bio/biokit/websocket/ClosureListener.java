package sa.gov.nic.bio.biokit.websocket;

import javax.websocket.CloseReason;

public interface ClosureListener
{
	void onClose(CloseReason closeReason);
}