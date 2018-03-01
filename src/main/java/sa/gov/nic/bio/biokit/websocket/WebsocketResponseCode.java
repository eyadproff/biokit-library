package sa.gov.nic.bio.biokit.websocket;

public enum WebsocketResponseCode
{
    END_OF_RESPONSE("111");

    private final String responseCode;

    WebsocketResponseCode(String responseCode)
    {
        this.responseCode = responseCode;
    }

    public String getResponseCode()
    {
        return responseCode;
    }
}