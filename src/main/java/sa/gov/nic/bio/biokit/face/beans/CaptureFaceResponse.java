package sa.gov.nic.bio.biokit.face.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

public class CaptureFaceResponse
{
	public static class SuccessCodes
	{
		public static final int SUCCESS = 100;
	}
	
	public static class FailureCodes
	{
		public static final int DEVICE_NOT_FOUND_OR_UNPLUGGED = -3;
		public static final int EXCEPTION_WHILE_CAPTURING = 11;
	}
	
	public static class IcaoCodes
	{
		public static final String SUCCESS = "SUCCESS";
		public static final String FACE_NOT_DETECTED = "FACE_NOT_DETECTED";
		public static final String MULTIPLE_FACES_DETECTED = "MULTIPLE_FACES_DETECTED";
		public static final String YAW_ERROR = "YAW_ERROR";
		public static final String PITCH_ERROR = "PITCH_ERROR";
		public static final String ROLL_ERROR = "ROLL_ERROR";
		public static final String SHADOW_ERROR = "SHADOW_ERROR";
		public static final String RIGHT_EYE_CLOSED = "RIGHT_EYE_CLOSED";
		public static final String LEFT_EYE_CLOSED = "LEFT_EYE_CLOSED";
		public static final String EYE_GAZE_ERROR = "EYE_GAZE_ERROR";
	}
	
	private String transactionId;
	private int returnCode;
	private String returnMessage;
    private String capturedImage;
	private String croppedImage;
	private String icaoErrorMessage;
    
    public CaptureFaceResponse(){}
	
	public CaptureFaceResponse(Message message)
	{
		if(message != null)
		{
			this.transactionId = message.getTransactionId();
			this.returnCode = message.getReturnCode();
			this.returnMessage = message.getReturnMessage();
			this.capturedImage = message.getFinalImage();
			this.croppedImage = message.getIcaoImage();
			this.icaoErrorMessage = message.getIcaoErrorMessage();
		}
	}
	
	public String getTransactionId(){return transactionId;}
	public void setTransactionId(String transactionId){this.transactionId = transactionId;}
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public String getReturnMessage(){return returnMessage;}
	public void setReturnMessage(String returnMessage){this.returnMessage = returnMessage;}
	
	public String getCapturedImage(){return capturedImage;}
	public void setCapturedImage(String capturedImage){this.capturedImage = capturedImage;}
	
	public String getCroppedImage(){return croppedImage;}
	public void setCroppedImage(String croppedImage){this.croppedImage = croppedImage;}
	
	public String getIcaoErrorMessage(){return icaoErrorMessage;}
	public void setIcaoErrorMessage(String icaoErrorMessage){this.icaoErrorMessage = icaoErrorMessage;}
	
	@Override
	public String toString()
	{
		return "CaptureFaceResponse{" + "transactionId='" + transactionId + '\'' + ", returnCode=" + returnCode +
			   ", returnMessage='" + returnMessage + '\'' + ", capturedImage='" + capturedImage + '\'' +
			   ", croppedImage='" + croppedImage + '\'' + ", icaoErrorMessage='" + icaoErrorMessage + '\'' + '}';
	}
	
	public String toShortString()
	{
		return "CaptureFaceResponse{" + "transactionId='" + transactionId + '\'' + ", returnCode=" + returnCode +
			   ", returnMessage='" + returnMessage + '\'' + ", capturedImage=isNull?'" + (capturedImage == null) +
			   '\'' + ", croppedImage=isNull?'" + (croppedImage == null) + '\'' + ", icaoErrorMessage='" +
			   icaoErrorMessage + '\'' + '}';
	}
}