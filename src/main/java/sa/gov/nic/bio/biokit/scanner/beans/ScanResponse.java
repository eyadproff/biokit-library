package sa.gov.nic.bio.biokit.scanner.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

public class ScanResponse
{
	public static class SuccessCodes
	{
		public static final int SUCCESS = 100;
	}
	
	public static class FailureCodes
	{
		public static final int DEVICE_NOT_FOUND_OR_UNPLUGGED = 105;
		public static final int FAILED_TO_SCAN_TENPRINT_IMAGE = 122;
	}
	
	private String transactionId;
	private int returnCode;
	private String finalImage;
    
    public ScanResponse(){}
	
	public ScanResponse(Message message)
	{
		if(message != null)
		{
			this.transactionId = message.getTransactionId();
			this.returnCode = message.getReturnCode();
			this.finalImage = message.getFinalImage();
		}
	}
	
	public String getTransactionId(){return transactionId;}
	public void setTransactionId(String transactionId){this.transactionId = transactionId;}
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public String getFinalImage(){return finalImage;}
	public void setFinalImage(String finalImage){this.finalImage = finalImage;}
	
	@Override
	public String toString()
	{
		return "ScanResponse{" + "transactionId='" + transactionId + '\'' + ", returnCode=" + returnCode +
			   ", finalImage='" + finalImage + '\'' + '}';
	}
	
	public String toShortString()
	{
		return "ScanResponse{" + "transactionId='" + transactionId + '\'' + ", returnCode=" + returnCode +
				", finalImage.isNull?='" + (finalImage == null) + '\'' + '}';
	}
}