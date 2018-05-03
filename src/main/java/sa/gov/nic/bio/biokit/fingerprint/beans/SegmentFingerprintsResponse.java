package sa.gov.nic.bio.biokit.fingerprint.beans;

import sa.gov.nic.bio.biokit.websocket.beans.DMFingerData;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.List;

public class SegmentFingerprintsResponse
{
	public static class SuccessCodes
	{
		public static final int SUCCESS = 100;
	}
	
	public static class FailureCodes
	{
		public static final int SEGMENTATION_FAILED = 114;
		public static final int WRONG_NO_OF_EXPECTED_FINGERS = 108;
	}
	
	private int returnCode;
	private String returnMessage;
	private List<DMFingerData> fingerData;
	
	public SegmentFingerprintsResponse(Message message)
	{
		if(message != null)
		{
			this.returnCode = message.getReturnCode();
			this.returnMessage = message.getReturnMessage();
			this.fingerData = message.getDmSegmentedFingers();
		}
	}
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public String getReturnMessage(){return returnMessage;}
	public void setReturnMessage(String returnMessage){this.returnMessage = returnMessage;}
	
	public List<DMFingerData> getFingerData(){return fingerData;}
	public void setFingerData(List<DMFingerData> fingerData){this.fingerData = fingerData;}
	
	@Override
	public String toString()
	{
		return "SegmentFingerprintsResponse{" + "returnCode=" + returnCode + ", returnMessage='" + returnMessage +
			   '\'' + ", fingerData=" + fingerData + '}';
	}
	
	public String toShortString()
	{
		return "SegmentFingerprintsResponse{" + "returnCode=" + returnCode + ", returnMessage='" + returnMessage +
				'\'' + ", fingerData=" + DMFingerData.shortenDMFingerDataList(fingerData) + '}';
	}
}