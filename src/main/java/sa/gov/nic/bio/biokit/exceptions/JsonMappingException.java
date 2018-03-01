package sa.gov.nic.bio.biokit.exceptions;

public class JsonMappingException extends Exception
{
	public JsonMappingException(){}
	
	public JsonMappingException(String message)
	{
		super(message);
	}
	
	public JsonMappingException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public JsonMappingException(Throwable cause)
	{
		super(cause);
	}
}