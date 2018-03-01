package sa.gov.nic.bio.biokit.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

public class StartPreviewResponse
{
	private String previewImage;
    
    public StartPreviewResponse(){}
    
    public StartPreviewResponse(Message message)
    {
    	if(message != null)
	    {
		    this.previewImage = message.getPreviewImage();
	    }
    }
    
    public String getPreviewImage(){return previewImage;}
    public void setPreviewImage(String previewImage){this.previewImage = previewImage;}
	
	@Override
	public String toString()
	{
		return "StartPreviewResponse{" + "previewImage='" + previewImage + '\'' + '}';
	}
}
