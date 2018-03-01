package sa.gov.nic.bio.biokit.exceptions;

public class NotConnectedException extends Exception
{
	public NotConnectedException(){}
	
	public NotConnectedException(String message)
	{
		super(message);
	}
	
	public NotConnectedException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public NotConnectedException(Throwable cause)
	{
		super(cause);
	}
}