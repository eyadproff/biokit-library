package sa.gov.nic.bio.biokit.face;

import sa.gov.nic.bio.biokit.websocket.beans.Message;


public class FaceStopPreviewResponse
{
    public static class SuccessCodes
    {
        public static final int SUCCESS = 100;
    }
    
    private int returnCode;
    
    public FaceStopPreviewResponse(){}
    
    public FaceStopPreviewResponse(Message message)
    {
        if(message != null)
        {
            this.returnCode = message.getReturnCode();
        }
    }
    
    public int getReturnCode(){return returnCode;}
    public void setReturnCode(int returnCode){this.returnCode = returnCode;}
    
    @Override
    public String toString()
    {
        return "FaceStopPreviewResponse{" + "returnCode=" + returnCode + '}';
    }
}
