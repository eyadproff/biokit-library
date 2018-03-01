package sa.gov.nic.bio.biokit.websocket;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

import javax.websocket.CloseReason;

public interface WebsocketLogger
{
	void logConnectionOpening();
	void logConnectionClosure(CloseReason closeReason);
	void logError(Throwable t);
	void logNewMessage(Message message);
}