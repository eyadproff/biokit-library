package sa.gov.nic.bio.biokit.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

public class UpdateResponse
{
	public static class SuccessCodes
	{
		public static final int GOING_TO_UPDATE = 700;
		public static final int NO_UPDATE_AVAILABLE = 701;
	}
	private int returnCode;
    
    public UpdateResponse(){}
    
    public UpdateResponse(Message message)
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
		return "UpdateResponse{" + "returnCode=" + returnCode + '}';
	}
}