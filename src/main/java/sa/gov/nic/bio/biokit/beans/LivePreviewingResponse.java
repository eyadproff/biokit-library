package sa.gov.nic.bio.biokit.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;

public class LivePreviewingResponse
{
	private String previewImage;
    
    public LivePreviewingResponse(){}
    
    public LivePreviewingResponse(Message message)
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
		return "LivePreviewingResponse{" + "previewImage='" + previewImage + '\'' + '}';
	}
}
