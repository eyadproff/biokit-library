package sa.gov.nic.bio.biokit.face.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

public class FaceStartPreviewResponse
{
	public static class SuccessCodes
	{
		public static final int SUCCESS = 100;
	}
	
	public static class FailureCodes
	{
		public static final int EXCEPTION_WHILE_GETTING_PREVIEW_IMAGE = -3;
		public static final int DEVICE_NOT_FOUND_OR_UNPLUGGED = 105;
		public static final int DEVICE_BUSY = 106;
	}
	
	private int returnCode;
	private String returnMessage;
    
    public FaceStartPreviewResponse(){}
    
    public FaceStartPreviewResponse(Message message)
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
		return "FaceStartPreviewResponse{" + "returnCode=" + returnCode + ", returnMessage='" + returnMessage +
			   '\'' + '}';
	}
}