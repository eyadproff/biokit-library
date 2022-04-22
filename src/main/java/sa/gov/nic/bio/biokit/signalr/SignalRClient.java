package sa.gov.nic.bio.biokit.signalr;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import sa.gov.nic.bio.biokit.face.signalr.CapturedImage;


import java.time.LocalDateTime;
import java.util.Timer;
import java.util.function.Consumer;

public class SignalRClient {
    static String lastConnectionState = "";
    static String SIGNALR_HUB_URL = "http://localhost:5000/cameraOperationHub";
    static String SIGNALR_HUB_URL_CANON = "cameraOperationHub";
    static String SIGNALR_HUB_URL_CROSSMATCH = "crossmatchOperationHub";
    public static String HUB_METHOD_NK_STATUS = "nkStatus";
    public static String HUB_METHOD_STREAM_CROSSMATCH = "lscanStream";
    public static String HUB_METHOD_IMAGE_CROSSMATCH = "lscanImage";
    public static String HUB_METHOD_STREAM_CAMERA = "canonStream";
    public static String HUB_METHOD_IMAGE_CAMERA = "canonImage";

    public static HubConnection hubConnection = null;

    public  static void init() {
        try {
            startConnection();
            if (hubConnection != null) {
                if ("".equalsIgnoreCase(lastConnectionState)) {
                    lastConnectionState = hubConnection.getConnectionState().toString();
                }
                String currentState = hubConnection.getConnectionState().toString();
                if (!lastConnectionState.isEmpty() && !currentState.equalsIgnoreCase(lastConnectionState)) {
                    lastConnectionState = currentState;
                    // handle connection status for color logic
                    if (HubConnectionState.CONNECTED.toString().equals(currentState)) {

                    } else {

                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isConnected(){
        return hubConnection.getConnectionState() != HubConnectionState.DISCONNECTED && hubConnection.getConnectionState() != HubConnectionState.CONNECTING;
    }

    public static boolean startConnection() {
        System.out.println(">>>>>>> hubConnection.start");
        try {

            hubConnection = HubConnectionBuilder.create(SIGNALR_HUB_URL).build();

            hubConnection.start();
            if(hubConnection.getConnectionState()== HubConnectionState.DISCONNECTED)
                return false;


            hubConnection.on(HUB_METHOD_NK_STATUS,
                    (sentMsg) -> {
                try {
                    System.out.println(">>>> Recevied message from hub on::" + LocalDateTime.now() + ":::connectionId::" + hubConnection.getConnectionId() + ":");
                    System.out.println(">>>>>>>> Message received::" + sentMsg);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            },String.class);


            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void updateHUBURL() {
        try {

            stopConnection();

            hubConnection = HubConnectionBuilder.create(SIGNALR_HUB_URL).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentDevice() {
        String result = "";

        if (SIGNALR_HUB_URL != null) {
            if (SIGNALR_HUB_URL.endsWith(SIGNALR_HUB_URL_CANON)) {
                result = DeviceType.CANON_DEVICE;
            } else if (SIGNALR_HUB_URL.endsWith(SIGNALR_HUB_URL_CROSSMATCH)) {
                result = DeviceType.CROSSMATCH_DEVICE;
            }
        }

        return result;
    }
    public static void stopConnection() {
        System.out.println(">>>>>>>>>>>>> in stopConnection");

            try {
                if (hubConnection != null) {
                    hubConnection.stop().doOnError(Throwable::printStackTrace);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            try {
                if (hubConnection != null) {
                    hubConnection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        System.out.println(">>>>>>>>>>>>> after stopConnection");
    }

    static class DeviceType {
        public static final String CANON_DEVICE = "canon";
        public static final String CROSSMATCH_DEVICE = "crossmatch";
    }

    public static void registerCaptureImageEventListener(Consumer<CapturedImage> capturedImageConsumer) {
        if (hubConnection != null) {
            String targetStream = null;
            if (getCurrentDevice().equals(DeviceType.CANON_DEVICE)) {
                targetStream = HUB_METHOD_IMAGE_CAMERA;
            } else if (getCurrentDevice().equals(DeviceType.CROSSMATCH_DEVICE)) {
                targetStream = HUB_METHOD_IMAGE_CROSSMATCH;
            }

            if (targetStream != null) {
                // Make sure to register once per connection as multi listeners will consume memory and are useless.
                hubConnection.on(targetStream, capturedImageConsumer::accept, CapturedImage.class);
                /*hubConnection.on(targetStream, (sentCapImage) -> {
                    try {
                        SwingUtilities.invokeLater(() -> {
                            InputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(sentCapImage.getBase64String()));
                            BufferedImage image = null;
                            try {
                                image = ImageIO.read(in);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }, CapturedImage.class);*/
            }
        }
    }

    public static void registerLiveViewEventListener(Consumer<CapturedImage> capturedImageConsumer) {
        if (hubConnection != null) {

            String targetStream = null;
            if (getCurrentDevice().equals(DeviceType.CANON_DEVICE)) {
                targetStream = HUB_METHOD_STREAM_CAMERA;
            } else if (getCurrentDevice().equals(DeviceType.CROSSMATCH_DEVICE)) {
                targetStream = HUB_METHOD_STREAM_CROSSMATCH;
            }

            if (targetStream != null) {
                // Make sure to register once per connection as multi listeners will consume memory and are useless.
                hubConnection.on(targetStream, capturedImageConsumer::accept, CapturedImage.class);

                /*// Make sure to register once per connection as multi listeners will consume memory and are useless.
                hubConnection.on(targetStream, (sentLiveFrameImage) -> {
                    try {

                            Thread parseImageThread = new Thread(() -> {
                                InputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(sentLiveFrameImage.getBase64String()));
                                BufferedImage imageFromBase64 = null;
                                try {
                                    imageFromBase64 = ImageIO.read(in);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            // temp workaround of lagging base64 decoding.
                            parseImageThread.setPriority(Thread.MAX_PRIORITY);
                            parseImageThread.start();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }, CapturedImage.class);*/
            }

        }
    }

    public static void captureFaceImage(){
        registerCaptureImageEventListener();
        registerLiveViewEventListener();

        if (getCurrentDevice().equals(DeviceType.CANON_DEVICE)) {
            hubConnection.invoke("startCapturing", new CapturingInfo("Canon", "", false));
        } else if (getCurrentDevice().equals(DeviceType.CROSSMATCH_DEVICE)) {
            hubConnection.invoke("startCapturing", new CapturingInfo("LScan", "LeftIndex", true));
        }
    }


}
