package sa.gov.nic.bio.biokit.fingerprint.beans;

import sa.gov.nic.bio.biokit.websocket.beans.DMFingerData;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.List;

public class CaptureFingerprintResponse
{
	public static class SuccessCodes
	{
		public static final int SUCCESS = 100;
	}
	
	public static class FailureCodes
	{
		public static final int DEVICE_NOT_FOUND_OR_UNPLUGGED = -11;
		public static final int DEVICE_BUSY = 106;
		public static final int WRONG_NUMBER_OF_EXPECTED_FINGERS = 108;
		public static final int SEGMENTATION_FAILED = 114;
		public static final int WSQ_CONVERSION_FAILED = 109;
		public static final int FAILED_TO_CAPTURE_FINAL_IMAGE = 113;
		public static final int EXCEPTION_IN_FINGER_HANDLER_CAPTURE = -1001;
	}
	
	private String transactionId;
	private int position;
    private int returnCode;
	private String returnMessage;
	private String capturedImage;
	private boolean isWrongSlap;
	private List<DMFingerData> fingerData;
    
    public CaptureFingerprintResponse(){}
	
	public CaptureFingerprintResponse(Message message)
	{
		if(message != null)
		{
			this.transactionId = message.getTransactionId();
			this.position = message.getPosition();
			this.returnCode = message.getReturnCode();
			this.returnMessage = message.getReturnMessage();
			this.capturedImage = message.getFinalImage();
			this.isWrongSlap = message.isWrongSlap();
			this.fingerData = message.getDmSegmentedFingers();
		}
	}
	
	public String getTransactionId(){return transactionId;}
	public void setTransactionId(String transactionId){this.transactionId = transactionId;}
	
	public int getPosition(){return position;}
	public void setPosition(int position){this.position = position;}
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public String getReturnMessage(){return returnMessage;}
	public void setReturnMessage(String returnMessage){this.returnMessage = returnMessage;}
	
	public String getCapturedImage(){return capturedImage;}
	public void setCapturedImage(String capturedImage){this.capturedImage = capturedImage;}
	
	public boolean isWrongSlap(){return isWrongSlap;}
	public void setWrongSlap(boolean wrongSlap){isWrongSlap = wrongSlap;}
	
	public List<DMFingerData> getFingerData(){return fingerData;}
	public void setFingerData(List<DMFingerData> fingerData){this.fingerData = fingerData;}
	
	@Override
	public String toString()
	{
		return "CaptureFingerprintResponse{" + "transactionId='" + transactionId + '\'' + ", position=" + position +
			   ", returnCode=" + returnCode + ", returnMessage='" + returnMessage + '\'' + ", capturedImage='" +
			   capturedImage + '\'' + ", isWrongSlap=" + isWrongSlap + ", fingerData=" + fingerData + '}';
	}
	
	public String toShortString()
	{
		return "CaptureFingerprintResponse{" + "transactionId='" + transactionId + '\'' + ", position=" + position +
			   ", returnMessage='" + returnMessage + '\'' + ", returnCode=" + returnCode + ", capturedImage='" +
			   capturedImage + '\'' + ", isWrongSlap=" + isWrongSlap + ", fingerData=" +
			   DMFingerData.shortenDMFingerDataList(fingerData) + '}';
	}
}
