package sa.gov.nic.bio.biokit.fingerprint.beans;

import sa.gov.nic.bio.biokit.websocket.beans.Message;


public class FingerprintStopPreviewResponse
{
    public static class SuccessCodes
    {
        public static final int SUCCESS = 100;
    }
    
    public static class FailureCodes
    {
        public static final int NOT_CAPTURING_NOW = 11;
        public static final int EXCEPTION_IN_FINGER_HANDLER_CANCEL_CAPTURE = -1003;
        public static final int EXCEPTION = -5;
    }
    
    private int returnCode;
    private String returnMessage;
    
    public FingerprintStopPreviewResponse(){}
    
    public FingerprintStopPreviewResponse(Message message)
    {
        if(message != null)
        {
            this.returnCode = message.getReturnCode();
            this.returnMessage = message.getReturnMessage();
        }
    }
    
    public int getReturnCode(){return returnCode;}
    public void setReturnCode(int returnCode){this.returnCode = returnCode;}
    
    public String getReturnMessage(){return returnMessage;}
    public void setReturnMessage(String returnMessage){this.returnMessage = returnMessage;}
    
    @Override
    public String toString()
    {
        return "FingerprintStopPreviewResponse{" + "returnCode=" + returnCode + ", returnMessage='" +
               returnMessage + '\'' + '}';
    }
}
