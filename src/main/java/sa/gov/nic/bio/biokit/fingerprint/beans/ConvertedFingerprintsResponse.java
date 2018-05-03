package sa.gov.nic.bio.biokit.fingerprint.beans;

import sa.gov.nic.bio.biokit.websocket.beans.DMFingerData;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertedFingerprintsResponse
{
	public static class SuccessCodes
	{
		public static final int SUCCESS = 100;
	}
	
	public static class FailureCodes
	{
		public static final int FAILED_TO_CONVERT_WSQ_TO_IMAGE = 119;
	}
	
	private int returnCode;
	private String returnMessage;
	private Map<Integer, String> fingerprintImagesMap;
	
	public ConvertedFingerprintsResponse(Message message)
	{
		if(message != null)
		{
			this.returnCode = message.getReturnCode();
			this.returnMessage = message.getReturnMessage();
			this.fingerprintImagesMap = new HashMap<Integer, String>();
			
			List<DMFingerData> dmSegmentedFingers = message.getDmSegmentedFingers();
			for(DMFingerData dmFingerData : dmSegmentedFingers)
			{
				fingerprintImagesMap.put(dmFingerData.getPosition(), dmFingerData.getFinger());
			}
		}
	}
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public String getReturnMessage(){return returnMessage;}
	public void setReturnMessage(String returnMessage){this.returnMessage = returnMessage;}
	
	public Map<Integer, String> getFingerprintImagesMap(){return fingerprintImagesMap;}
	public void setFingerprintImagesMap(Map<Integer, String> fingerprintImagesMap)
																	{this.fingerprintImagesMap = fingerprintImagesMap;}
	
	@Override
	public String toString()
	{
		return "ConvertedFingerprintsResponse{" + "returnCode=" + returnCode + ", returnMessage='" + returnMessage +
			   '\'' + ", fingerprintImagesMap=" + fingerprintImagesMap + '}';
	}
	
	public String toShortString()
	{
		return "ConvertedFingerprintsResponse{" + "returnCode=" + returnCode + ", returnMessage='" + returnMessage +
			   '\'' + ", fingerprintImagesMap.size()=" +
			   (fingerprintImagesMap != null ? fingerprintImagesMap.size() : null) + '}';
	}
}