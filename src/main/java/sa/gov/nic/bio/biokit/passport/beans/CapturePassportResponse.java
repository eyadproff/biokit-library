package sa.gov.nic.bio.biokit.passport.beans;

import sa.gov.nic.bio.biokit.websocket.beans.MRZData;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

public class CapturePassportResponse
{
	public static class SuccessCodes
	{
		public static final int SUCCESS = 100;
	}
	
	public static class FailureCodes
	{
		public static final int DEVICE_NOT_FOUND_OR_UNPLUGGED = 105;
		public static final int NO_DOCUMENT_FOUND_OR_FAILED_TO_READ = 120;
		public static final int EXCEPTION_WHILE_CAPTURING = -11;
	}
	
	private String transactionId;
	private int returnCode;
    private MRZData mrzData;
    
    public CapturePassportResponse(){}
	
	public CapturePassportResponse(Message message)
	{
		if(message != null)
		{
			this.transactionId = message.getTransactionId();
			this.returnCode = message.getReturnCode();
			this.mrzData = message.getMrzData();
		}
	}
	
	public String getTransactionId(){return transactionId;}
	public void setTransactionId(String transactionId){this.transactionId = transactionId;}
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public MRZData getMrzData(){return mrzData;}
	public void setMrzData(MRZData mrzData){this.mrzData = mrzData;}
	
	@Override
	public String toString()
	{
		return "CapturePassportResponse{" + "transactionId='" + transactionId + '\'' + ", returnCode=" + returnCode +
			   ", mrzData=" + mrzData + '}';
	}
}