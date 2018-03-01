package sa.gov.nic.bio.biokit.exceptions;

public class AlreadyConnectedException extends Exception
{
	public AlreadyConnectedException(){}
	
	public AlreadyConnectedException(String message)
	{
		super(message);
	}
	
	public AlreadyConnectedException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public AlreadyConnectedException(Throwable cause)
	{
		super(cause);
	}
}