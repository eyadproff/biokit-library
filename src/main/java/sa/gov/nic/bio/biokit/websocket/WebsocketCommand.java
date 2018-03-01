package sa.gov.nic.bio.biokit.websocket;

public enum WebsocketCommand
{
    START_PREVIEW("START_PREVIEW"),
    STOP_PREVIEW("STOP_PREVIEW"),
    CANCEL_CAPTURE("CANCEL_CAPTURE"),
    CAPTURE("CAPTURE"),
    INITIALIZE_DEVICE("init_device"),
    DEINITIALIZE_DEVICE("deinitialize"),
    FIND_DUPLICATES("find_duplicate"),
    SHUTDOWN("shutdown"),
    UPDATER("update");

    private final String command;

    WebsocketCommand(String command)
    {
        this.command = command;
    }

    public String getCommand()
    {
        return command;
    }
}