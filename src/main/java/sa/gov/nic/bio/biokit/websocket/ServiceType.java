package sa.gov.nic.bio.biokit.websocket;

public enum ServiceType
{
    FINGERPRINT("FINGER"), FACE("FACE"), IRIS("IRIS"), PASSPORT("PASSPORT");

    private final String type;

    ServiceType(String type)
    {
        this.type = type;
    }

    public final String getType()
    {
        return type;
    }
}