package sa.gov.nic.bio.biokit.fingerprint.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.Map;

public class DuplicatedFingerprintsResponse
{
	public static class SuccessCodes
	{
		public static final int SUCCESS = 100;
	}
	
	public static class FailureCodes
	{
		public static final int FAILED_TO_FIND_DUPLICATES = 110;
	}
	
	private int returnCode;
	private Map<Integer, Boolean> duplicatedFingers;
	
	public DuplicatedFingerprintsResponse(Message message)
	{
		if(message != null)
		{
			this.returnCode = message.getReturnCode();
			this.duplicatedFingers = message.getDuplicateList();
		}
	}
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public Map<Integer, Boolean> getDuplicatedFingers(){return duplicatedFingers;}
	public void setDuplicatedFingers(Map<Integer, Boolean> duplicatedFingers){this.duplicatedFingers = duplicatedFingers;}
	
	@Override
	public String toString()
	{
		return "DuplicatedFingerprintsResponse{" + "returnCode=" + returnCode +
				", duplicatedFingers=" + duplicatedFingers + '}';
	}
}