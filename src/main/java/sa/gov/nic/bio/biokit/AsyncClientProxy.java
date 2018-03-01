package sa.gov.nic.bio.biokit;

import sa.gov.nic.bio.biokit.exceptions.AlreadyConnectedException;
import sa.gov.nic.bio.biokit.exceptions.ConnectionException;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.RequestException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AsyncClientProxy<T>
{
	protected List<AsyncConsumer> consumers = Collections.synchronizedList(new ArrayList<AsyncConsumer>());
	protected final String serverUrl;
	private final int responseTimeoutSeconds;
	
	public AsyncClientProxy(String serverUrl, int responseTimeoutSeconds)
	{
		this.serverUrl = serverUrl;
		this.responseTimeoutSeconds = responseTimeoutSeconds;
	}
	
	public int getResponseTimeoutSeconds(){return responseTimeoutSeconds;}
	
	public void registerConsumer(AsyncConsumer consumer)
	{
		consumers.add(consumer);
	}
	
	public void unregisterConsumer(AsyncConsumer consumer)
	{
		consumers.remove(consumer);
	}
	
	public abstract void connect() throws AlreadyConnectedException, ConnectionException;
	public abstract void disconnect() throws NotConnectedException, ConnectionException;
	public abstract boolean isConnected();
	public abstract void sendCommandAsync(T command) throws NotConnectedException, RequestException;
}