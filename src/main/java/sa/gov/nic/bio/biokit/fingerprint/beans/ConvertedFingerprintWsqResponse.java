package sa.gov.nic.bio.biokit.fingerprint.beans;

import sa.gov.nic.bio.biokit.websocket.beans.DMFingerData;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertedFingerprintWsqResponse
{
	public static class SuccessCodes
	{
		public static final int SUCCESS = 100;
	}
	
	public static class FailureCodes
	{
	
	}
	
	private int returnCode;
	private String returnMessage;
	private Map<Integer, String> fingerprintWsqMap;
	
	public ConvertedFingerprintWsqResponse(Message message)
	{
		if(message != null)
		{
			this.returnCode = message.getReturnCode();
			this.returnMessage = message.getReturnMessage();
			this.fingerprintWsqMap = new HashMap<Integer, String>();
			
			List<DMFingerData> dmSegmentedFingers = message.getDmSegmentedFingers();
			for(DMFingerData dmFingerData : dmSegmentedFingers)
			{
				fingerprintWsqMap.put(dmFingerData.getPosition(), dmFingerData.getFingerWsqImage());
			}
		}
	}
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public String getReturnMessage(){return returnMessage;}
	public void setReturnMessage(String returnMessage){this.returnMessage = returnMessage;}
	
	public Map<Integer, String> getFingerprintWsqMap(){return fingerprintWsqMap;}
	public void setFingerprintWsqMap(Map<Integer, String> fingerprintWsqMap)
																	{this.fingerprintWsqMap = fingerprintWsqMap;}
	
	@Override
	public String toString()
	{
		return "ConvertedFingerprintWsqResponse{" + "returnCode=" + returnCode + ", returnMessage='" +
			   returnMessage + '\'' + ", fingerprintWsqMap=" + fingerprintWsqMap + '}';
	}
	
	public String toShortString()
	{
		return "ConvertedFingerprintWsqResponse{" + "returnCode=" + returnCode + ", returnMessage='" +
			   returnMessage + '\'' + ", fingerprintWsqMap.size()=" +
			   (fingerprintWsqMap != null ? fingerprintWsqMap.size() : null) + '}';
	}
}