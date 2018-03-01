package sa.gov.nic.bio.biokit.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

public class ShutdownResponse
{
	public static class SuccessCodes
	{
		public static final int BIOKIT_IS_SHUTTING_DOWN = 300;
	}
	private int returnCode;
    
    public ShutdownResponse(){}
    
    public ShutdownResponse(Message message)
    {
    	if(message != null)
	    {
		    this.returnCode = message.getReturnCode();
	    }
    }
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	@Override
	public String toString()
	{
		return "ShutdownResponse{" + "returnCode=" + returnCode + '}';
	}
}