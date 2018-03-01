package sa.gov.nic.bio.biokit.exceptions;

public class AlreadyStartedException extends Exception
{
	public AlreadyStartedException(){}
	
	public AlreadyStartedException(String message)
	{
		super(message);
	}
	
	public AlreadyStartedException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public AlreadyStartedException(Throwable cause)
	{
		super(cause);
	}
}