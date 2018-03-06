package sa.gov.nic.bio.biokit.websocket.beans;

import java.util.List;

public class DMFingerData
{
	private int minutiaeCount;
	private int nfiqQuality;
	private String finger;
	private int position;
	private int intensity;
	private String roundingBox;
	private boolean duplicate;
	private String template;
	
	public int getMinutiaeCount(){return minutiaeCount;}
	public void setMinutiaeCount(int minutiaeCount){this.minutiaeCount = minutiaeCount;}
	
	public int getNfiqQuality(){return nfiqQuality;}
	public void setNfiqQuality(int nfiqQuality){this.nfiqQuality = nfiqQuality;}
	
	public String getFinger(){return finger;}
	public void setFinger(String finger){this.finger = finger;}
	
	public int getPosition(){return position;}
	public void setPosition(int position){this.position = position;}
	
	public int getIntensity(){return intensity;}
	public void setIntensity(int intensity){this.intensity = intensity;}
	
	public String getRoundingBox(){return roundingBox;}
	public void setRoundingBox(String roundingBox){this.roundingBox = roundingBox;}
	
	public boolean isDuplicate(){return duplicate;}
	public void setDuplicate(boolean duplicate){this.duplicate = duplicate;}
	
	public String getTemplate(){return template;}
	public void setTemplate(String template){this.template = template;}
	
	@Override
	public String toString()
	{
		return "DMFingerData{" + "minutiaeCount=" + minutiaeCount + ", nfiqQuality=" + nfiqQuality + ", finger='" +
				finger + '\'' + ", position=" + position + ", intensity=" + intensity + ", roundingBox='" +
				roundingBox + '\'' + ", duplicate=" + duplicate + ", template='" + template + '\'' + '}';
	}
	
	public String toShortString()
	{
		return "DMFingerData{" + "minutiaeCount=" + minutiaeCount + ", nfiqQuality=" + nfiqQuality +
			   ", finger=isNull?'" + (finger == null) + '\'' + ", position=" + position + ", intensity=" + intensity +
			   ", roundingBox='" + roundingBox + '\'' + ", duplicate=" + duplicate + ", template=isNull?'" +
			   (template == null) + '\'' + '}';
	}
	
	public static String shortenDMFingerDataList(List<DMFingerData> list)
	{
		if(list == null) return null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		for(int i = 0; i < list.size(); i++)
		{
			DMFingerData dmFingerData = list.get(i);
			sb.append(dmFingerData.toShortString());
			if(i < list.size() - 1) sb.append(",");
		}
		
		sb.append("]");
		
		return sb.toString();
	}
}
