package sa.gov.nic.bio.biokit.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

public class ShutdownResponse
{
	public static class SuccessCodes
	{
		public static final int BIOKIT_IS_SHUTTING_DOWN = 300;
	}
	private int returnCode;
	private String returnMessage;
    
    public ShutdownResponse(){}
    
    public ShutdownResponse(Message message)
    {
    	if(message != null)
	    {
		    this.returnCode = message.getReturnCode();
		    this.returnMessage = message.getReturnMessage();
	    }
    }
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public String getReturnMessage(){return returnMessage;}
	public void setReturnMessage(String returnMessage){this.returnMessage = returnMessage;}
	
	@Override
	public String toString()
	{
		return "ShutdownResponse{" + "returnCode=" + returnCode + ", returnMessage='" + returnMessage + '\'' + '}';
	}
}