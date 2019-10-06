package sa.gov.nic.bio.biokit.iris.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

public class CaptureIrisResponse
{
	public static class SuccessCodes
	{
		public static final int SUCCESS = 100;
	}
	
	public static class FailureCodes
	{
		public static final int EXCEPTION_WHILE_CAPTURING = -11;
		public static final int DEVICE_NOT_FOUND_OR_UNPLUGGED = 105;
		public static final int DEVICE_BUSY = 106;
	}
	
	private String transactionId;
    private int returnCode;
	private String returnMessage;
	private String rightIrisImageBase64;
	private String leftIrisImageBase64;
    
    public CaptureIrisResponse(){}
	
	public CaptureIrisResponse(Message message)
	{
		if(message != null)
		{
			this.transactionId = message.getTransactionId();
			this.returnCode = message.getReturnCode();
			this.returnMessage = message.getReturnMessage();
			this.rightIrisImageBase64 = message.getRightIrisImage();
			this.leftIrisImageBase64 = message.getLeftIrisImage();
		}
	}
	
	public String getTransactionId(){return transactionId;}
	public void setTransactionId(String transactionId){this.transactionId = transactionId;}
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public String getReturnMessage(){return returnMessage;}
	public void setReturnMessage(String returnMessage){this.returnMessage = returnMessage;}
	
	public String getRightIrisImageBase64(){return rightIrisImageBase64;}
	public void setRightIrisImageBase64(String rightIrisImageBase64){this.rightIrisImageBase64 = rightIrisImageBase64;}
	
	public String getLeftIrisImageBase64(){return leftIrisImageBase64;}
	public void setLeftIrisImageBase64(String leftIrisImageBase64){this.leftIrisImageBase64 = leftIrisImageBase64;}
	
	@Override
	public String toString()
	{
		return "CaptureIrisResponse{" + "transactionId='" + transactionId + '\'' + ", returnCode=" + returnCode +
			   ", returnMessage='" + returnMessage + '\'' + ", rightIrisImageBase64" + "='" + rightIrisImageBase64 +
			   '\'' + ", leftIrisImageBase64='" + leftIrisImageBase64 + '\'' + '}';
	}
	
	public String toShortString()
	{
		return "CaptureIrisResponse{" + "transactionId='" + transactionId + '\'' + ", returnCode=" + returnCode +
			   ", returnMessage='" + returnMessage + '\'' + ", rightIrisImageBase64=isNull?" + "='" +
			   (rightIrisImageBase64 == null) + '\'' + ", leftIrisImageBase64isNull?='" + (leftIrisImageBase64 == null) +
			   '\'' + '}';
	}
}
