package sa.gov.nic.bio.biokit.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

public class InitializeResponse
{
	public static class SuccessCodes
	{
		public static final int SUCCESS = 100;
	}
	
	public static class FailureCodes
	{
		public static final int DEVICE_BUSY = 106;
		public static final int DEVICE_NOT_FOUND_OR_UNPLUGGED = 105;
		public static final int EXCEPTION = -1005;
		public static final int EXCEPTION2 = -2;
	}
	
    private int returnCode;
    private String returnMessage;
    private String currentDeviceName;
    
    public InitializeResponse(){}
    
    public InitializeResponse(Message message)
    {
    	if(message != null)
	    {
		    this.returnCode = message.getReturnCode();
		    this.returnMessage = message.getReturnMessage();
		    this.currentDeviceName = message.getCurrentDeviceName();
	    }
    }
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public String getReturnMessage(){return returnMessage;}
	public void setReturnMessage(String returnMessage){this.returnMessage = returnMessage;}
	
	public String getCurrentDeviceName(){return currentDeviceName;}
	public void setCurrentDeviceName(String currentDeviceName){this.currentDeviceName = currentDeviceName;}
	
	@Override
	public String toString()
	{
		return "InitializeResponse{" + "returnCode=" + returnCode + ", returnMessage='" + returnMessage + '\'' +
			   ", currentDeviceName='" + currentDeviceName + '\'' + '}';
	}
}