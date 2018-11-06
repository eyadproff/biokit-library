package sa.gov.nic.bio.biokit.websocket;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

public interface WebsocketLogger
{
	void logConnectionOpening();
	void logConnectionClosure(String closeCode, String reasonPhrase);
	void logError(Throwable t);
	void logNewMessage(Message message);
}