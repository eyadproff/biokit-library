package sa.gov.nic.bio.biokit.demo;

import com.google.gson.Gson;
import sa.gov.nic.bio.bcl.utils.BclUtils;
import sa.gov.nic.bio.bcl.utils.CancelCommand;
import sa.gov.nic.bio.biokit.BiokitCommander;
import sa.gov.nic.bio.biokit.BiokitCommanderFactory;
import sa.gov.nic.bio.biokit.DeviceServiceFactory;
import sa.gov.nic.bio.biokit.DeviceUtilitiesServiceFactory;
import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.beans.LivePreviewingResponse;
import sa.gov.nic.bio.biokit.beans.ServiceResponse;
import sa.gov.nic.bio.biokit.beans.ShutdownResponse;
import sa.gov.nic.bio.biokit.beans.UpdateResponse;
import sa.gov.nic.bio.biokit.exceptions.AlreadyConnectedException;
import sa.gov.nic.bio.biokit.exceptions.AlreadyStartedException;
import sa.gov.nic.bio.biokit.exceptions.ConnectionException;
import sa.gov.nic.bio.biokit.exceptions.JsonMappingException;
import sa.gov.nic.bio.biokit.exceptions.NotConnectedException;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.face.FaceService;
import sa.gov.nic.bio.biokit.face.FaceStopPreviewResponse;
import sa.gov.nic.bio.biokit.face.beans.CaptureFaceResponse;
import sa.gov.nic.bio.biokit.face.beans.FaceStartPreviewResponse;
import sa.gov.nic.bio.biokit.fingerprint.FingerprintService;
import sa.gov.nic.bio.biokit.fingerprint.beans.DuplicatedFingerprintsResponse;
import sa.gov.nic.bio.biokit.fingerprint.beans.FingerprintStopPreviewResponse;
import sa.gov.nic.bio.biokit.fingerprint.FingerprintUtilitiesService;
import sa.gov.nic.bio.biokit.fingerprint.beans.CaptureFingerprintResponse;
import sa.gov.nic.bio.biokit.utils.JsonMapper;
import sa.gov.nic.bio.biokit.websocket.ClosureListener;
import sa.gov.nic.bio.biokit.websocket.UpdateListener;
import sa.gov.nic.bio.biokit.websocket.WebsocketClient;
import sa.gov.nic.bio.biokit.websocket.WebsocketLogger;
import sa.gov.nic.bio.biokit.websocket.beans.DMFingerData;
import sa.gov.nic.bio.biokit.websocket.beans.Message;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.websocket.CloseReason;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SuppressWarnings("unchecked")
public class Demo
{
	private static final long RUNTIME_ID = System.currentTimeMillis();
	private static final int BIO_KIT_PORT = 6178;
	
	private static final Integer[] POSITIONS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15};
	
	private static WebsocketClient websocketClient;
	private static FaceService faceService;
	private static FingerprintService fingerprintService;
	private static FingerprintUtilitiesService fingerprintUtilitiesService;
	private static BiokitCommander biokitCommander;
	
	private static String faceDeviceName;
	private static String fingerprintDeviceName;
	private static int currentFingerprintDeviceCapturingPosition;
	private static Map<Integer, String> segmentedFingerTemplateMap = new HashMap<Integer, String>();
	
	private static JFrame frame;
	private static JDialog dialog;
	private static JTextArea txtWebsocketLogger;
	private static JTextArea txtDemoLogger;
	private static List<JCheckBox> cboFingers;
	private static JCheckBox cboApplyIcao;
	private static JCheckBox cboTrimBase64Text;
	private static JCheckBox cboAutoScrollDemoLogger;
	private static JCheckBox cboAutoScrollWebsocketLogger;
	private static JLabel lblDeviceConnectivity;
	private static JLabel lblFacePreview;
	private static JLabel lblFaceCaptured;
	private static JLabel lblFaceCropped;
	private static JLabel lblFingerprintPreview;
	private static JLabel lblFingerprintCaptured;
	private static JButton btnRightSlapFindDuplicates;
	private static JButton btnLeftSlapFindDuplicates;
	private static JButton btnThumbsFindDuplicates;
	
	private static JTextField txtIcaoResult;
	private static JTextField txtIsWrongSlap;
	
	private static JLabel lblRightThumb;
	private static JTextField txtNfiqRightThuumb;
	private static JTextField txtMinutiaeCountRightThuumb;
	private static JTextField txtIntensityRightThuumb;
	private static JLabel lblRightIndex;
	private static JTextField txtNfiqRightIndex;
	private static JTextField txtMinutiaeCountRightIndex;
	private static JTextField txtIntensityRightIndex;
	private static JLabel lblRightMiddle;
	private static JTextField txtNfiqRightMiddle;
	private static JTextField txtMinutiaeCountRightMiddle;
	private static JTextField txtIntensityRightMiddle;
	private static JLabel lblRightRing;
	private static JTextField txtNfiqRightRing;
	private static JTextField txtMinutiaeCountRightRing;
	private static JTextField txtIntensityRightRing;
	private static JLabel lblRightLittle;
	private static JTextField txtNfiqRightLittle;
	private static JTextField txtMinutiaeCountRightLittle;
	private static JTextField txtIntensityRightLittle;
	
	private static JLabel lblLeftThumb;
	private static JTextField txtNfiqLeftThuumb;
	private static JTextField txtMinutiaeCountLeftThuumb;
	private static JTextField txtIntensityLeftThuumb;
	private static JLabel lblLeftIndex;
	private static JTextField txtNfiqLeftIndex;
	private static JTextField txtMinutiaeCountLeftIndex;
	private static JTextField txtIntensityLeftIndex;
	private static JLabel lblLeftMiddle;
	private static JTextField txtNfiqLeftMiddle;
	private static JTextField txtMinutiaeCountLeftMiddle;
	private static JTextField txtIntensityLeftMiddle;
	private static JLabel lblLeftRing;
	private static JTextField txtNfiqLeftRing;
	private static JTextField txtMinutiaeCountLeftRing;
	private static JTextField txtIntensityLeftRing;
	private static JLabel lblLeftLittle;
	private static JTextField txtNfiqLeftLittle;
	private static JTextField txtMinutiaeCountLeftLittle;
	private static JTextField txtIntensityLeftLittle;
	
    public static void main(String[] args)
    {
    	String websocketServerUrl = "ws://localhost:" + BIO_KIT_PORT + "/BioKit/server";
	    int maxTextMessageBufferSize = 10050000;
	    int maxBinaryMessageBufferSize = 10050000;
	    int responseTimeoutSeconds = 30;
	
	    JsonMapper<Message> jsonMapper = new JsonMapper<Message>()
	    {
		    @Override
		    public Message fromJson(String json) throws JsonMappingException
		    {
		    	try
			    {
				    return new Gson().fromJson(json, Message.class);
			    }
			    catch(Exception e)
			    {
			    	throw new JsonMappingException(e);
			    }
		    }
		
		    @Override
		    public String toJson(Message object) throws JsonMappingException
		    {
			    try
			    {
				    return new Gson().toJson(object);
			    }
			    catch(Exception e)
			    {
				    throw new JsonMappingException(e);
			    }
		    }
	    };
	    
	    ClosureListener closureListener = new ClosureListener()
	    {
		    @Override
		    public void onClose(CloseReason closeReason)
		    {
		    	logDemo("Websocket is disconnected!");
			    lblDeviceConnectivity.setText("Disconnected");
		    }
	    };
	    
	    WebsocketLogger websocketLogger = new WebsocketLogger()
	    {
		    @Override
		    public void logConnectionOpening()
		    {
			    logWebsocket("Opened a new websocket connection.");
		    }
		
		    @Override
		    public void logConnectionClosure(CloseReason closeReason)
		    {
			    CloseReason.CloseCode closeCode = closeReason.getCloseCode();
			    String reasonPhrase = closeReason.getReasonPhrase();
			
			    logWebsocket("Closed the websocket connection: closeCode = " + closeCode +
		                 (reasonPhrase != null && !reasonPhrase.isEmpty() ? ", reasonPhrase = " + reasonPhrase : ""));
		    }
		
		    @Override
		    public void logError(Throwable t)
		    {
			    logWebsocket(t);
		    }
		
		    @Override
		    public void logNewMessage(final Message message)
		    {
			    if(cboTrimBase64Text.isSelected()) logWebsocket("New websocket message: " +
			                                                    message.toShortString() + ".");
			    else logWebsocket("New websocket message: " + message + ".");
		    }
	    };
	    
	    websocketClient = new WebsocketClient(websocketServerUrl, maxTextMessageBufferSize, maxBinaryMessageBufferSize,
	                                          responseTimeoutSeconds, jsonMapper, closureListener, websocketLogger,
	                                          new UpdateListener()
	    {
		    @Override
		    public void newUpdate()
		    {
			    logDemo("There is a new update for Biokit!");
		    }
	    });
	    
	    faceService = DeviceServiceFactory.getFaceService(websocketClient);
	    fingerprintService = DeviceServiceFactory.getFingerprintService(websocketClient);
	    fingerprintUtilitiesService = DeviceUtilitiesServiceFactory.getFingerprintUtilitiesService(websocketClient);
	    biokitCommander = BiokitCommanderFactory.getBiokitCommander(websocketClient);
	
	    SwingUtilities.invokeLater(new Runnable()
	    {
		    @Override
		    public void run()
		    {
			    try
			    {
				    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			    }
			    catch(Exception e)
			    {
				    e.printStackTrace();
			    }
			
			    frame = new JFrame("Biometrics Kit Demo");
			    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			    frame.addWindowListener(new WindowAdapter()
			    {
				    @Override
				    public void windowClosed(WindowEvent e)
				    {
					    try
					    {
						    websocketClient.disconnect();
					    }
					    catch(NotConnectedException e1)
					    {
						    logDemo("Websocket is not connected!");
					    }
					    catch(ConnectionException e1)
					    {
						    logDemo("Failed to disconnect!");
						    e1.printStackTrace();
					    }
				    }
			    });
			
			    JPanel mainPanel = createDemoPanel();
			    frame.setContentPane(mainPanel);
			    frame.pack();
			
			    frame.setLocationRelativeTo(null);
			    frame.setVisible(true);
			
			    System.out.println(frame.getSize());
			    frame.setMinimumSize(frame.getSize());
		    }
	    });
    }
    
    private static void logDemo(String text)
    {
	    logTextArea(txtDemoLogger, cboAutoScrollDemoLogger, text);
    }
	
	private static void logDemo(Throwable t)
	{
		logTextArea(txtDemoLogger, cboAutoScrollDemoLogger, t);
	}
	
	private static void logWebsocket(String text)
	{
		logTextArea(txtWebsocketLogger, cboAutoScrollWebsocketLogger, text);
	}
	
	private static void logWebsocket(Throwable t)
	{
		logTextArea(txtWebsocketLogger, cboAutoScrollWebsocketLogger, t);
	}
	
	private static void logTextArea(final JTextArea textArea, final JCheckBox cboAutoScroll, final String text)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				int oldCaretPosition = textArea.getCaretPosition();
				textArea.append(text + "\n");
				if(cboAutoScroll.isSelected()) textArea.setCaretPosition(textArea.getDocument().getLength());
				else textArea.setCaretPosition(oldCaretPosition);
			}
		});
	}
	
	private static void logTextArea(final JTextArea textArea, final JCheckBox cboAutoScroll, Throwable t)
	{
		final StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				int oldCaretPosition = textArea.getCaretPosition();
				textArea.append("Exception occurred:\n" + sw + "\n");
				if(cboAutoScroll.isSelected()) textArea.setCaretPosition(textArea.getDocument().getLength());
				else textArea.setCaretPosition(oldCaretPosition);
			}
		});
	}
    
    private static JPanel createDemoPanel()
    {
    	JPanel panMain = new JPanel(new BorderLayout());
    	
	    JTabbedPane tabbedPane = new JTabbedPane();
	    tabbedPane.addTab("Face", createFaceTab());
	    tabbedPane.addTab("Fingerprint", createFingerprintTab());
	    panMain.add(tabbedPane, BorderLayout.CENTER);
	    
	    JPanel panTop = createTopPanel();
	    panMain.add(panTop, BorderLayout.NORTH);
	    
	    JPanel panLogger = createLoggerPanel();
	    panMain.add(panLogger, BorderLayout.SOUTH);
	    
    	return panMain;
    }
	
	private static JPanel createTopPanel()
	{
		JPanel panTop = new JPanel(new GridLayout(1, 2));
		
		JPanel panWebsocket = new JPanel();
		panWebsocket.setBorder(BorderFactory.createTitledBorder("Websocket"));
		
		JButton btnConnect = new JButton("Connect");
		JButton btnDisconnect = new JButton("Disconnect");
		panWebsocket.add(btnConnect);
		panWebsocket.add(btnDisconnect);
		
		btnConnect.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onWebsocketConnect();
			}
		});
		
		btnDisconnect.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onWebsocketDisconnect();
			}
		});
		
		JLabel label = new JLabel("Websocket Connectivity:");
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		lblDeviceConnectivity = new JLabel("Disconnected");
		panWebsocket.add(label);
		panWebsocket.add(lblDeviceConnectivity);
		
		JPanel panBiokit = new JPanel();
		panBiokit.setBorder(BorderFactory.createTitledBorder("Biokit Commands"));
		
		JButton btnStartBiokit = new JButton("Start");
		JButton btnShutdownBiokit = new JButton("Shutdown");
		JButton btnUpdateBiokit = new JButton("Update");
		panBiokit.add(btnStartBiokit);
		panBiokit.add(btnShutdownBiokit);
		panBiokit.add(btnUpdateBiokit);
		
		btnStartBiokit.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onStartBiokit();
			}
		});
		
		btnShutdownBiokit.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onShutdownBiokit();
			}
		});
		
		btnUpdateBiokit.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onUpdateBiokit();
			}
		});
		
		panTop.add(panWebsocket);
		panTop.add(panBiokit);
		
		return panTop;
	}
	
	private static JPanel createLoggerPanel()
	{
		JPanel panLogger = new JPanel(new BorderLayout());
		panLogger.setBorder(BorderFactory.createTitledBorder("The Logger"));
		
		cboTrimBase64Text = new JCheckBox("Trim base64 text");
		cboTrimBase64Text.setSelected(true);
		
		cboAutoScrollDemoLogger = new JCheckBox("Auto-scroll demo logger");
		cboAutoScrollDemoLogger.setSelected(true);
		cboAutoScrollDemoLogger.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(cboAutoScrollDemoLogger.isSelected()) txtDemoLogger.setCaretPosition(
																			txtDemoLogger.getDocument().getLength());
			}
		});
		
		cboAutoScrollWebsocketLogger = new JCheckBox("Auto-scroll websocket logger");
		cboAutoScrollWebsocketLogger.setSelected(true);
		cboAutoScrollWebsocketLogger.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(cboAutoScrollWebsocketLogger.isSelected()) txtWebsocketLogger.setCaretPosition(
																		txtWebsocketLogger.getDocument().getLength());
			}
		});
		
		JPanel panOptions = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panOptions.add(cboTrimBase64Text);
		panOptions.add(cboAutoScrollDemoLogger);
		panOptions.add(cboAutoScrollWebsocketLogger);
		
		txtWebsocketLogger = new JTextArea(7, 0);
		txtWebsocketLogger.setEditable(false);
		txtWebsocketLogger.setFont(txtWebsocketLogger.getFont().deriveFont(12f));
		JScrollPane spWebsocketLogger = new JScrollPane(txtWebsocketLogger);
		spWebsocketLogger.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		spWebsocketLogger.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		txtDemoLogger = new JTextArea(7, 0);
		txtDemoLogger.setEditable(false);
		txtDemoLogger.setFont(txtDemoLogger.getFont().deriveFont(12f));
		JScrollPane spDemoLogger = new JScrollPane(txtDemoLogger);
		spDemoLogger.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		spDemoLogger.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Demo Logger", spDemoLogger);
		tabbedPane.addTab("Websocket Logger", spWebsocketLogger);
		panLogger.add(panOptions, BorderLayout.NORTH);
		panLogger.add(tabbedPane, BorderLayout.CENTER);
		
		return panLogger;
	}
    
    private static JPanel createFaceTab()
	{
		JPanel panMain = new JPanel(new BorderLayout());
		JPanel panCenter = new JPanel(new BorderLayout());
		JPanel panSouth = new JPanel();
		
		panMain.add(panCenter, BorderLayout.CENTER);
		panMain.add(panSouth, BorderLayout.SOUTH);
		
		ImageIcon imgFacePreview = new ImageIcon();
		lblFacePreview = new JLabel(imgFacePreview);
		
		ImageIcon imgFaceCaptured = new ImageIcon();
		lblFaceCaptured = new JLabel(imgFaceCaptured);
		
		ImageIcon imgFaceCropped = new ImageIcon();
		lblFaceCropped = new JLabel(imgFaceCropped);
		
		JPanel panFacePreview = new JPanel(new BorderLayout());
		panFacePreview.add(lblFacePreview, BorderLayout.CENTER);
		panFacePreview.setBorder(BorderFactory.createTitledBorder("Face Preview"));
		
		JPanel panFaceCaptured = new JPanel(new BorderLayout());
		panFaceCaptured.add(lblFaceCaptured, BorderLayout.CENTER);
		panFaceCaptured.setBorder(BorderFactory.createTitledBorder("Face Captured"));
		
		JPanel panFaceCropped = new JPanel(new BorderLayout());
		panFaceCropped.add(lblFaceCropped, BorderLayout.CENTER);
		panFaceCropped.setBorder(BorderFactory.createTitledBorder("Face Cropped"));
		
		JPanel panInput = new JPanel();
		panFacePreview.add(panInput, BorderLayout.SOUTH);
		
		JPanel panOutput = new JPanel();
		panFaceCropped.add(panOutput, BorderLayout.SOUTH);
		
		cboApplyIcao = new JCheckBox("Apply ICAO");
		cboApplyIcao.setSelected(true);
		panInput.add(cboApplyIcao);
		
		JLabel lblIcaoResult = new JLabel("ICAO Result:");
		txtIcaoResult = new JTextField(20);
		txtIcaoResult.setEditable(false);
		panOutput.add(lblIcaoResult);
		panOutput.add(txtIcaoResult);
		
		JSplitPane innerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panFaceCaptured, panFaceCropped);
		innerSplitPane.setResizeWeight(0.8);
		
		JSplitPane outerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panFacePreview, innerSplitPane);
		outerSplitPane.setResizeWeight(0.35);
		
		panCenter.add(outerSplitPane, BorderLayout.CENTER);
		
		JButton btnInitialize = new JButton("Initialize");
		JButton btnDeinitializeDevice = new JButton("Deinitialize");
		JButton btnStartPreview = new JButton("Start preview");
		JButton btnCapture = new JButton("Capture");
		JButton btnStopPreview = new JButton("Stop preview");
		JButton btnClearFields = new JButton("Clear fields");
		panSouth.add(btnInitialize);
		panSouth.add(btnDeinitializeDevice);
		panSouth.add(btnStartPreview);
		panSouth.add(btnCapture);
		panSouth.add(btnStopPreview);
		panSouth.add(btnClearFields);
		
		btnInitialize.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onFaceDeviceInitialize();
			}
		});
		
		btnDeinitializeDevice.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onDeinitializeFaceDevice();
			}
		});
		
		btnStartPreview.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onFaceStartPreview();
			}
		});
		
		btnCapture.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onFaceCapture();
			}
		});
		
		btnStopPreview.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onFaceStopPreview();
			}
		});
		
		btnClearFields.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onFaceFieldsClear();
			}
		});
		
		
		return panMain;
	}
	
	private static JPanel createFingerprintTab()
	{
		JPanel panMain = new JPanel(new BorderLayout());
		JPanel panCenter = new JPanel(new BorderLayout());
		JPanel panSouth = new JPanel();
		
		panMain.add(panCenter, BorderLayout.CENTER);
		panMain.add(panSouth, BorderLayout.SOUTH);
		
		ImageIcon imgFingerprintPreview = new ImageIcon();
		lblFingerprintPreview = new JLabel(imgFingerprintPreview);
		
		ImageIcon imgFingerprintCaptured = new ImageIcon();
		lblFingerprintCaptured = new JLabel(imgFingerprintCaptured);
		
		JPanel panSegmentedFingers = new JPanel(new GridBagLayout());
		JPanel panFingerprintPreview = new JPanel(new BorderLayout());
		panFingerprintPreview.setBorder(BorderFactory.createTitledBorder("Fingerprint Preview"));
		JPanel panFingerprintCaptured = new JPanel(new BorderLayout());
		panFingerprintCaptured.setBorder(BorderFactory.createTitledBorder("Fingerprint Captured"));
		panFingerprintCaptured.setPreferredSize(new Dimension(0, 189));
		
		JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panFingerprintPreview,
		                                                panFingerprintCaptured);
		horizontalSplitPane.setResizeWeight(0.3);
		
		JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane,
		                                              panSegmentedFingers);
		verticalSplitPane.setResizeWeight(0.7);
		panCenter.add(verticalSplitPane, BorderLayout.CENTER);
		
		panFingerprintPreview.add(lblFingerprintPreview, BorderLayout.CENTER);
		panFingerprintCaptured.add(lblFingerprintCaptured, BorderLayout.CENTER);
		
		JPanel panInput = new JPanel();
		panFingerprintPreview.add(panInput, BorderLayout.SOUTH);
		
		JLabel lblPosition = new JLabel("Position:");
		final JComboBox cboPosition = new JComboBox(POSITIONS);
		cboPosition.setSelectedItem("13");
		
		cboFingers = new ArrayList<JCheckBox>();
		cboFingers.add(new JCheckBox("1", true));
		cboFingers.add(new JCheckBox("2", true));
		cboFingers.add(new JCheckBox("3", true));
		cboFingers.add(new JCheckBox("4", true));
		cboFingers.add(new JCheckBox("5", true));
		cboFingers.add(new JCheckBox("6", true));
		cboFingers.add(new JCheckBox("7", true));
		cboFingers.add(new JCheckBox("8", true));
		cboFingers.add(new JCheckBox("9", true));
		cboFingers.add(new JCheckBox("10", true));
		
		panInput.add(lblPosition);
		panInput.add(cboPosition);
		
		for(JCheckBox cboFinger : cboFingers) panInput.add(cboFinger);
		
		JPanel panOutput = new JPanel();
		panFingerprintCaptured.add(panOutput, BorderLayout.SOUTH);
		
		JLabel lblIsWrongSlap = new JLabel("isWrongSlap:");
		txtIsWrongSlap = new JTextField(10);
		
		txtIsWrongSlap.setEditable(false);
		
		panOutput.add(lblIsWrongSlap);
		panOutput.add(txtIsWrongSlap);
		
		int TEXT_WIDTH = 3;
		
		JButton btnInitialize = new JButton("Initialize");
		JButton btnDeinitializeDevice = new JButton("Deinitialize");
		JButton btnStartPreviewAndAutoCapture = new JButton("Start preview & auto-capture");
		JButton btnStopPreview = new JButton("Stop preview");
		JButton btnClearFields = new JButton("Clear fields");
		panSouth.add(btnInitialize);
		panSouth.add(btnDeinitializeDevice);
		panSouth.add(btnStartPreviewAndAutoCapture);
		panSouth.add(btnStopPreview);
		panSouth.add(btnClearFields);
		
		lblRightThumb = new JLabel();
		txtNfiqRightThuumb = new JTextField(TEXT_WIDTH);
		txtMinutiaeCountRightThuumb = new JTextField(TEXT_WIDTH);
		txtIntensityRightThuumb = new JTextField(TEXT_WIDTH);
		lblRightIndex = new JLabel();
		txtNfiqRightIndex = new JTextField(TEXT_WIDTH);
		txtMinutiaeCountRightIndex = new JTextField(TEXT_WIDTH);
		txtIntensityRightIndex = new JTextField(TEXT_WIDTH);
		lblRightMiddle = new JLabel();
		txtNfiqRightMiddle = new JTextField(TEXT_WIDTH);
		txtMinutiaeCountRightMiddle = new JTextField(TEXT_WIDTH);
		txtIntensityRightMiddle = new JTextField(TEXT_WIDTH);
		lblRightRing = new JLabel();
		txtNfiqRightRing = new JTextField(TEXT_WIDTH);
		txtMinutiaeCountRightRing = new JTextField(TEXT_WIDTH);
		txtIntensityRightRing = new JTextField(TEXT_WIDTH);
		lblRightLittle = new JLabel();
		txtNfiqRightLittle = new JTextField(TEXT_WIDTH);
		txtMinutiaeCountRightLittle = new JTextField(TEXT_WIDTH);
		txtIntensityRightLittle = new JTextField(TEXT_WIDTH);
		
		JPanel panRightThumb = createSegmentedFingerPanel("Right Thumb", lblRightThumb, txtNfiqRightThuumb,
		                                                  txtMinutiaeCountRightThuumb, txtIntensityRightThuumb);
		JPanel panRightIndex = createSegmentedFingerPanel("Right Index", lblRightIndex, txtNfiqRightIndex,
		                                                  txtMinutiaeCountRightIndex, txtIntensityRightIndex);
		JPanel panRightMiddle = createSegmentedFingerPanel("Right Middle", lblRightMiddle, txtNfiqRightMiddle,
		                                                   txtMinutiaeCountRightMiddle, txtIntensityRightMiddle);
		JPanel panRightRing = createSegmentedFingerPanel("Right Ring", lblRightRing, txtNfiqRightRing,
		                                                 txtMinutiaeCountRightRing, txtIntensityRightRing);
		JPanel panRightLittle = createSegmentedFingerPanel("Right Little", lblRightLittle, txtNfiqRightLittle,
		                                                   txtMinutiaeCountRightLittle, txtIntensityRightLittle);
		
		lblLeftThumb = new JLabel();
		txtNfiqLeftThuumb = new JTextField(TEXT_WIDTH);
		txtMinutiaeCountLeftThuumb = new JTextField(TEXT_WIDTH);
		txtIntensityLeftThuumb = new JTextField(TEXT_WIDTH);
		lblLeftIndex = new JLabel();
		txtNfiqLeftIndex = new JTextField(TEXT_WIDTH);
		txtMinutiaeCountLeftIndex = new JTextField(TEXT_WIDTH);
		txtIntensityLeftIndex = new JTextField(TEXT_WIDTH);
		lblLeftMiddle = new JLabel();
		txtNfiqLeftMiddle = new JTextField(TEXT_WIDTH);
		txtMinutiaeCountLeftMiddle = new JTextField(TEXT_WIDTH);
		txtIntensityLeftMiddle = new JTextField(TEXT_WIDTH);
		lblLeftRing = new JLabel();
		txtNfiqLeftRing = new JTextField(TEXT_WIDTH);
		txtMinutiaeCountLeftRing = new JTextField(TEXT_WIDTH);
		txtIntensityLeftRing = new JTextField(TEXT_WIDTH);
		lblLeftLittle = new JLabel();
		txtNfiqLeftLittle = new JTextField(TEXT_WIDTH);
		txtMinutiaeCountLeftLittle = new JTextField(TEXT_WIDTH);
		txtIntensityLeftLittle = new JTextField(TEXT_WIDTH);
		
		JPanel panLeftThumb = createSegmentedFingerPanel("Left Thumb", lblLeftThumb, txtNfiqLeftThuumb,
		                                                 txtMinutiaeCountLeftThuumb, txtIntensityLeftThuumb);
		JPanel panLeftIndex = createSegmentedFingerPanel("Left Index", lblLeftIndex, txtNfiqLeftIndex,
		                                                 txtMinutiaeCountLeftIndex, txtIntensityLeftIndex);
		JPanel panLeftMiddle = createSegmentedFingerPanel("Left Middle", lblLeftMiddle, txtNfiqLeftMiddle,
		                                                  txtMinutiaeCountLeftMiddle, txtIntensityLeftMiddle);
		JPanel panLeftRing = createSegmentedFingerPanel("Left Ring", lblLeftRing, txtNfiqLeftRing,
		                                                txtMinutiaeCountLeftRing, txtIntensityLeftRing);
		JPanel panLeftLittle = createSegmentedFingerPanel("Left Little", lblLeftLittle, txtNfiqLeftLittle,
		                                                  txtMinutiaeCountLeftLittle, txtIntensityLeftLittle);
		
		btnRightSlapFindDuplicates = new JButton("Find duplicates in right slap");
		btnRightSlapFindDuplicates.setEnabled(false);
		btnLeftSlapFindDuplicates = new JButton("Find duplicates in left slap");
		btnLeftSlapFindDuplicates.setEnabled(false);
		btnThumbsFindDuplicates = new JButton("Find duplicates in thumbs");
		btnThumbsFindDuplicates.setEnabled(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.1;
		c.weighty = 1.0;
		c.gridy = 0;
		
		panSegmentedFingers.add(panLeftLittle, c);
		panSegmentedFingers.add(panLeftRing, c);
		panSegmentedFingers.add(panLeftMiddle, c);
		panSegmentedFingers.add(panLeftIndex, c);
		panSegmentedFingers.add(panLeftThumb, c);
		panSegmentedFingers.add(panRightThumb, c);
		panSegmentedFingers.add(panRightIndex, c);
		panSegmentedFingers.add(panRightMiddle, c);
		panSegmentedFingers.add(panRightRing, c);
		panSegmentedFingers.add(panRightLittle, c);
		
		c.gridy = 1;
		c.weighty = 0;
		c.weightx = 0.4;
		c.gridwidth = 4;
		panSegmentedFingers.add(btnLeftSlapFindDuplicates, c);
		
		c.weightx = 0.2;
		c.gridwidth = 2;
		panSegmentedFingers.add(btnThumbsFindDuplicates, c);
		
		c.weightx = 0.4;
		c.gridwidth = 4;
		panSegmentedFingers.add(btnRightSlapFindDuplicates, c);
		
		cboPosition.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for(JCheckBox cboFinger : cboFingers)
				{
					cboFinger.setSelected(true);
					cboFinger.setVisible(false);
				}
				
				Integer selectedPosition = (Integer) cboPosition.getSelectedItem();
				if(selectedPosition != null && selectedPosition >= 1 && selectedPosition <= 10)
				{
					JCheckBox cboFinger = cboFingers.get(selectedPosition - 1);
					cboFinger.setEnabled(false);
					cboFinger.setVisible(true);
				}
				else if(selectedPosition != null && selectedPosition == 13)
				{
					int minFinger = 2;
					int maxFinger = 5;
					
					for(int i = minFinger - 1; i < maxFinger; i++)
					{
						JCheckBox cboFinger = cboFingers.get(i);
						cboFinger.setEnabled(true);
						cboFinger.setVisible(true);
					}
				}
				else if(selectedPosition != null && selectedPosition == 14)
				{
					int minFinger = 7;
					int maxFinger = 10;
					
					for(int i = minFinger - 1; i < maxFinger; i++)
					{
						JCheckBox cboFinger = cboFingers.get(i);
						cboFinger.setEnabled(true);
						cboFinger.setVisible(true);
					}
				}
				else if(selectedPosition != null && selectedPosition == 15)
				{
					JCheckBox cboFinger = cboFingers.get(0);
					cboFinger.setEnabled(true);
					cboFinger.setVisible(true);
					
					cboFinger = cboFingers.get(5);
					cboFinger.setEnabled(true);
					cboFinger.setVisible(true);
				}
			}
		});
		
		cboPosition.setSelectedItem(13);
		
		btnLeftSlapFindDuplicates.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onFindDuplicates(14);
			}
		});
		
		btnThumbsFindDuplicates.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onFindDuplicates(15);
			}
		});
		
		btnRightSlapFindDuplicates.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onFindDuplicates(13);
			}
		});
		
		btnInitialize.addActionListener(new ActionListener()
		{
            @Override
            public void actionPerformed(ActionEvent e)
            {
	            int position = Integer.parseInt(cboPosition.getItemAt(cboPosition.getSelectedIndex()).toString());
	            onFingerprintDeviceInitialize(position);
            }
        });

        btnStartPreviewAndAutoCapture.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
	            int position = Integer.parseInt(cboPosition.getItemAt(cboPosition.getSelectedIndex()).toString());
	            int expectedFingersCount = 0;
	            List<Integer> missingFingers = new ArrayList<Integer>();
	
	
	            for(int i = 0; i < cboFingers.size(); i++)
	            {
		            JCheckBox cboFinger = cboFingers.get(i);
	            	
		            if(cboFinger.isVisible())
		            {
			            if(cboFinger.isSelected()) expectedFingersCount++;
			            else missingFingers.add(i + 1);
		            }
	            }
	            
	            onFingerprintStartPreviewAndAutoCapture(position, expectedFingersCount, missingFingers);
            }
        });
		
		btnStopPreview.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onFingerprintStopPreview();
			}
		});
		
		btnClearFields.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onFingerprintFieldsClear();
			}
		});
		
		btnDeinitializeDevice.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int position = Integer.parseInt(cboPosition.getItemAt(cboPosition.getSelectedIndex()).toString());
				onDeinitializeFingerprintDevice(position);
			}
		});
		
		return panMain;
	}
	
	private static JPanel createSegmentedFingerPanel(String title, JLabel lblFinger, JTextField txtNfiq,
	                                                 JTextField txtMinutiaeCount, JTextField txtIntensity)
	{
		txtNfiq.setEditable(false);
		txtMinutiaeCount.setEditable(false);
		txtIntensity.setEditable(false);
		lblFinger.setPreferredSize(new Dimension(0, 100));
		
		JPanel panSegmentedFinger = new JPanel(new BorderLayout());
		panSegmentedFinger.setBorder(BorderFactory.createTitledBorder(title));
		panSegmentedFinger.add(lblFinger, BorderLayout.CENTER);
		JPanel panDetails = new JPanel(new GridBagLayout());
		JPanel panLeftAligned = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panLeftAligned.add(panDetails);
		panSegmentedFinger.add(panLeftAligned, BorderLayout.SOUTH);
		
		JLabel lblNfiq = new JLabel("NFIQ:");
		JLabel lblMinutiaeCount = new JLabel("Minutiae:");
		JLabel lblIntensity = new JLabel("Intensity:");
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.WEST;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		panDetails.add(lblNfiq, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panDetails.add(lblMinutiaeCount, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		panDetails.add(lblIntensity, gbc);
		
		gbc.insets = new Insets(0, 1, 0, 0);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		panDetails.add(txtNfiq, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		panDetails.add(txtMinutiaeCount, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		panDetails.add(txtIntensity, gbc);
		
		return panSegmentedFinger;
	}
	
	private static void onFaceFieldsClear()
	{
		lblFacePreview.setIcon(null);
		lblFaceCaptured.setIcon(null);
		lblFaceCropped.setIcon(null);
		txtIcaoResult.setText("");
	}
	
	private static void onFingerprintFieldsClear()
	{
		segmentedFingerTemplateMap.clear();
		lblFingerprintPreview.setIcon(null);
		lblFingerprintCaptured.setIcon(null);
		txtIsWrongSlap.setText("");
		btnLeftSlapFindDuplicates.setEnabled(false);
		btnThumbsFindDuplicates.setEnabled(false);
		btnRightSlapFindDuplicates.setEnabled(false);
		lblRightThumb.setIcon(null);
		txtNfiqRightThuumb.setText("");
		txtMinutiaeCountRightThuumb.setText("");
		txtIntensityRightThuumb.setText("");
		lblRightIndex.setIcon(null);
		txtNfiqRightIndex.setText("");
		txtMinutiaeCountRightIndex.setText("");
		txtIntensityRightIndex.setText("");
		lblRightMiddle.setIcon(null);
		txtNfiqRightMiddle.setText("");
		txtMinutiaeCountRightMiddle.setText("");
		txtIntensityRightMiddle.setText("");
		lblRightRing.setIcon(null);
		txtNfiqRightRing.setText("");
		txtMinutiaeCountRightRing.setText("");
		txtIntensityRightRing.setText("");
		lblRightLittle.setIcon(null);
		txtNfiqRightLittle.setText("");
		txtMinutiaeCountRightLittle.setText("");
		txtIntensityRightLittle.setText("");
		lblLeftThumb.setIcon(null);
		txtNfiqLeftThuumb.setText("");
		txtMinutiaeCountLeftThuumb.setText("");
		txtIntensityLeftThuumb.setText("");
		lblLeftIndex.setIcon(null);
		txtNfiqLeftIndex.setText("");
		txtMinutiaeCountLeftIndex.setText("");
		txtIntensityLeftIndex.setText("");
		lblLeftMiddle.setIcon(null);
		txtNfiqLeftMiddle.setText("");
		txtMinutiaeCountLeftMiddle.setText("");
		txtIntensityLeftMiddle.setText("");
		lblLeftRing.setIcon(null);
		txtNfiqLeftRing.setText("");
		txtMinutiaeCountLeftRing.setText("");
		txtIntensityLeftRing.setText("");
		lblLeftLittle.setIcon(null);
		txtNfiqLeftLittle.setText("");
		txtMinutiaeCountLeftLittle.setText("");
		txtIntensityLeftLittle.setText("");
	}
	
	private static void onStartBiokit()
	{
		final CancelCommand cancelCommand = new CancelCommand();
		final JLabel lblStatus = new JLabel("Checking if Bio-Kit is running...");
		dialog = createProgressDialog(frame, "Starting Bio-Kit", lblStatus, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
				cancelCommand.cancel();
			}
		});
		
		final SwingWorker<Void, String> swingWorker = new SwingWorker<Void, String>()
		{
			@Override
			protected Void doInBackground() throws Exception
			{
				boolean isListening = BclUtils.isLocalhostPortListening(BIO_KIT_PORT);
				if(cancelCommand.isCanceled()) return null;
				
				if(isListening)
				{
					throw new AlreadyStartedException();
				}
				else
				{
					publish("Bio-Kit is not running! Launching via BCL...");
					BclUtils.launchAppByBCL("10.0.73.80", "biokit", BIO_KIT_PORT, 1000,
					                        cancelCommand);
				}
				
				return null;
			}
			
			@Override
			protected void process(List<String> chunks)
			{
				for(String chunk : chunks)
				{
					lblStatus.setText(chunk);
					dialog.pack();
					dialog.setLocationRelativeTo(frame);
				}
			}
			
			@Override
			protected void done()
			{
				if(cancelCommand.isCanceled())
				{
					logDemo("BiokitStart() is cancelled!");
					return;
				}
				
				try
				{
					get();
					logDemo("Biokit is started!");
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					
					if(cause instanceof AlreadyStartedException)
					{
						logDemo("Bio-Kit is already running!");
					}
					else
					{
						logDemo("BiokitStart() is failed!");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
				finally
				{
					dialog.dispose();
				}
			}
		};
		
		dialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				swingWorker.execute();
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				cancelCommand.cancel();
			}
		});
		dialog.setVisible(true);
	}
	
	private static void onShutdownBiokit()
	{
		final CancelCommand cancelCommand = new CancelCommand();
		final JLabel lblStatus = new JLabel("Checking if Bio-Kit is running...");
		dialog = createProgressDialog(frame, "Shutting down Bio-Kit", lblStatus, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
				cancelCommand.cancel();
			}
		});
		
		final SwingWorker<Future<ServiceResponse<ShutdownResponse>>, String> swingWorker =
												new SwingWorker<Future<ServiceResponse<ShutdownResponse>>, String>()
		{
			@Override
			protected Future<ServiceResponse<ShutdownResponse>> doInBackground()
			{
				boolean isListening = BclUtils.isLocalhostPortListening(BIO_KIT_PORT);
				if(cancelCommand.isCanceled()) return null;
				
				if(isListening) publish("Bio-Kit is running! Sending shutdown command...");
				else
				{
					logDemo("Bio-Kit is not running!");
					return null;
				}
				
				return biokitCommander.shutdown();
			}
			
			@Override
			protected void process(List<String> chunks)
			{
				for(String chunk : chunks)
				{
					lblStatus.setText(chunk);
					dialog.pack();
					dialog.setLocationRelativeTo(frame);
				}
			}
			
			@Override
			protected void done()
			{
				if(cancelCommand.isCanceled())
				{
					logDemo("BiokitShutdown() is cancelled!");
					return;
				}
				
				try
				{
					final Future<ServiceResponse<ShutdownResponse>> future = get();
					
					if(future == null) return;
					
					new SwingWorker<ServiceResponse<ShutdownResponse>, String>()
					{
						@Override
						protected ServiceResponse<ShutdownResponse> doInBackground() throws Exception
						{
							return future.get();
						}
						
						@Override
						protected void done()
						{
							try
							{
								if(future.isCancelled())
								{
									logDemo("BiokitShutdown() is cancelled!");
									return;
								}
								
								ServiceResponse<ShutdownResponse> serviceResponse = get();
								
								if(serviceResponse.isSuccess())
								{
									logDemo("BiokitShutdown() receives a response.");
									
									ShutdownResponse result = serviceResponse.getResult();
									
									if(result.getReturnCode() == ShutdownResponse.SuccessCodes.BIOKIT_IS_SHUTTING_DOWN)
									{
										logDemo("BiokitShutdown()'s response: SUCCESS - biokit is shutting down!");
									}
									else
									{
										StringBuilder sb = new StringBuilder("BiokitShutdown()'s response: FAILURE - ");
										sb.append("code = ");
										sb.append(result.getReturnCode());
										sb.append(" ");
										
										switch(result.getReturnCode())
										{
											default: sb.append("(UNKNOWN)."); break;
										}
										
										logDemo(sb.toString());
									}
								}
								else
								{
									logDemo("BiokitShutdown() failed to receive a response. errorCode = " +
									        serviceResponse.getErrorCode());
									Exception exception = serviceResponse.getException();
									if(exception != null) exception.printStackTrace();
								}
							}
							catch(ExecutionException e)
							{
								Throwable cause = e.getCause();
								if(cause instanceof ExecutionException) cause = cause.getCause();
								
								if(cause instanceof TimeoutException) logDemo("BiokitShutdown() timeout.");
								else if(cause instanceof NotConnectedException) logDemo(
																"BiokitShutdown(): Websocket is not connected.");
								else
								{
									logDemo("BiokitShutdown(): Exception.");
									logDemo(cause);
								}
							}
							catch(InterruptedException e) // should't happen
							{
								e.printStackTrace();
							}
							finally
							{
								dialog.dispose();
							}
						}
					}.execute();
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					
					logDemo("BiokitShutdown() is failed!");
					logDemo(cause);
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
				finally
				{
					dialog.dispose();
				}
			}
		};
		
		dialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				swingWorker.execute();
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				cancelCommand.cancel();
			}
		});
		dialog.setVisible(true);
	}
	
	private static void onUpdateBiokit()
	{
		final Future<ServiceResponse<UpdateResponse>> future = biokitCommander.update();
		final JLabel lblStatus = new JLabel("Sending update command to Bio-Kit...");
		final JDialog dialog = createProgressDialog(frame, "Updating Bio-Kit", lblStatus, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				future.cancel(true);
			}
		});
		
		final SwingWorker<ServiceResponse<UpdateResponse>, String> swingWorker =
															new SwingWorker<ServiceResponse<UpdateResponse>, String>()
		{
			@Override
			protected ServiceResponse<UpdateResponse> doInBackground() throws Exception
			{
				return future.get();
			}
			
			@Override
			protected void done()
			{
				try
				{
					if(future.isCancelled())
					{
						logDemo("BiokitUpdate() is cancelled.");
						return;
					}
					
					ServiceResponse<UpdateResponse> serviceResponse = get();
					
					if(serviceResponse.isSuccess())
					{
						logDemo("BiokitUpdate() receives a response.");
						
						UpdateResponse result = serviceResponse.getResult();
						
						if(result.getReturnCode() == UpdateResponse.SuccessCodes.GOING_TO_UPDATE)
						{
							logDemo("BiokitUpdate()'s response: SUCCESS - Biokit is going to update itself!");
						}
						else if(result.getReturnCode() == UpdateResponse.SuccessCodes.NO_UPDATE_AVAILABLE)
						{
							logDemo("BiokitUpdate()'s response: SUCCESS - No update available for Biokit!");
						}
						else
						{
							StringBuilder sb = new StringBuilder("BiokitUpdate()'s response: FAILURE - ");
							sb.append("code = ");
							sb.append(result.getReturnCode());
							sb.append(" ");
							
							switch(result.getReturnCode())
							{
								default: sb.append("(UNKNOWN)."); break;
							}
							
							logDemo(sb.toString());
						}
					}
					else
					{
						logDemo("BiokitUpdate() failed to receive a response. errorCode = " +
						        serviceResponse.getErrorCode());
						Exception exception = serviceResponse.getException();
						if(exception != null) exception.printStackTrace();
					}
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					if(cause instanceof ExecutionException) cause = cause.getCause();
					
					if(cause instanceof TimeoutException) logDemo("BiokitUpdate() timeout.");
					else
					{
						logDemo("BiokitUpdate(): Exception.");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
				finally
				{
					dialog.dispose();
				}
			}
		};
		
		dialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				swingWorker.execute();
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				future.cancel(true);
			}
		});
		dialog.setVisible(true);
	}
	
	private static void onWebsocketConnect()
	{
		final CancelCommand cancelCommand = new CancelCommand();
		final JLabel lblStatus = new JLabel("Checking if Bio-Kit is running...");
		dialog = createProgressDialog(frame, "Connecting to Bio-Kit", lblStatus, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
				cancelCommand.cancel();
			}
		});
		
		final SwingWorker<Void, String> swingWorker = new SwingWorker<Void, String>()
		{
			@Override
			protected Void doInBackground() throws Exception
			{
				boolean isListening = BclUtils.isLocalhostPortListening(BIO_KIT_PORT);
				if(cancelCommand.isCanceled()) return null;
				
				if(isListening) publish("Bio-Kit is running! Trying to connect...");
				else
				{
					publish("Bio-Kit is not running! Launching via BCL...");
					BclUtils.launchAppByBCL("10.0.73.80", "biokit", BIO_KIT_PORT, 1000,
					                        cancelCommand);
				}
				
				if(cancelCommand.isCanceled()) return null;
				websocketClient.connect();
				
				return null;
			}
			
			@Override
			protected void process(List<String> chunks)
			{
				for(String chunk : chunks)
				{
					lblStatus.setText(chunk);
					dialog.pack();
					dialog.setLocationRelativeTo(frame);
				}
			}
			
			@Override
			protected void done()
			{
				if(cancelCommand.isCanceled())
				{
					logDemo("Websocket connection is cancelled!");
					return;
				}
				
				try
				{
					get();
					logDemo("Websocket is connected!");
					lblDeviceConnectivity.setText("Connected");
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					
					if(cause instanceof AlreadyConnectedException)
					{
						logDemo("Websocket is already connected!");
					}
					else
					{
						logDemo("Websocket connection is failed!");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
				finally
				{
					dialog.dispose();
				}
			}
		};
		
		dialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				swingWorker.execute();
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				cancelCommand.cancel();
			}
		});
		dialog.setVisible(true);
	}
	
	private static void onWebsocketDisconnect()
	{
		new SwingWorker<Void, Void>()
		{
			@Override
			protected Void doInBackground() throws Exception
			{
				websocketClient.disconnect();
				return null;
			}
			
			@Override
			protected void done()
			{
				try
				{
					get();
					lblDeviceConnectivity.setText("Disconnected");
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					
					if(cause instanceof NotConnectedException)
					{
						logDemo("Websocket is not connected!");
					}
					else
					{
						logDemo("Websocket disconnection is failed!");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
			}
		}.execute();
	}
	
	private static void onFaceDeviceInitialize()
	{
		final Future<ServiceResponse<InitializeResponse>> future = faceService.initialize();
		final JLabel lblStatus = new JLabel("Initializing the Face Device...");
		final JDialog dialog = createProgressDialog(frame, "Waiting for Bio-Kit", lblStatus,
		                                            new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				future.cancel(true);
			}
		});
		
		final SwingWorker<ServiceResponse<InitializeResponse>, String> swingWorker =
														new SwingWorker<ServiceResponse<InitializeResponse>, String>()
		{
			@Override
			protected ServiceResponse<InitializeResponse> doInBackground() throws Exception
			{
				return future.get();
			}
			
			@Override
			protected void done()
			{
				try
				{
					if(future.isCancelled())
					{
						logDemo("FaceDeviceInitialize() is cancelled.");
						return;
					}
					
					ServiceResponse<InitializeResponse> serviceResponse = get();
					
					if(serviceResponse.isSuccess())
					{
						logDemo("FaceDeviceInitialize() receives a response.");
						
						InitializeResponse result = serviceResponse.getResult();
						
						if(result.getReturnCode() == InitializeResponse.SuccessCodes.SUCCESS)
						{
							faceDeviceName = result.getCurrentDeviceName();
							logDemo("FaceDeviceInitialize()'s response: SUCCESS - faceDeviceName = " +
							        faceDeviceName);
						}
						else
						{
							StringBuilder sb = new StringBuilder("FaceDeviceInitialize()'s response: FAILURE - ");
							sb.append("code = ");
							sb.append(result.getReturnCode());
							sb.append(" ");
							
							switch(result.getReturnCode())
							{
								case InitializeResponse.FailureCodes.DEVICE_BUSY: sb.append("(DEVICE_BUSY)."); break;
								case InitializeResponse.FailureCodes.DEVICE_NOT_FOUND_OR_UNPLUGGED:
																sb.append("(DEVICE_NOT_FOUND_OR_UNPLUGGED)."); break;
								case InitializeResponse.FailureCodes.EXCEPTION: sb.append("(EXCEPTION)."); break;
								default: sb.append("(UNKNOWN)."); break;
							}
							
							logDemo(sb.toString());
						}
					}
					else
					{
						logDemo("FaceDeviceInitialize() failed to receive a response. errorCode = " +
								                                                    serviceResponse.getErrorCode());
						Exception exception = serviceResponse.getException();
						if(exception != null) exception.printStackTrace();
					}
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					if(cause instanceof ExecutionException) cause = cause.getCause();
					
					if(cause instanceof TimeoutException) logDemo("FaceDeviceInitialize() timeout.");
					else if(cause instanceof NotConnectedException) logDemo(
														"FaceDeviceInitialize(): Websocket is not connected.");
					else
					{
						logDemo("FaceDeviceInitialize(): Exception.");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
				finally
				{
					dialog.dispose();
				}
			}
		};
		
		dialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				swingWorker.execute();
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				future.cancel(true);
			}
		});
		dialog.setVisible(true);
	}
	
	private static void onFaceStartPreview()
	{
		new SwingWorker<ServiceResponse<FaceStartPreviewResponse>, LivePreviewingResponse>()
		{
			@Override
			protected ServiceResponse<FaceStartPreviewResponse> doInBackground() throws Exception
			{
				ResponseProcessor<LivePreviewingResponse> responseProcessor = new ResponseProcessor<LivePreviewingResponse>()
				{
					@Override
					public void processResponse(LivePreviewingResponse response)
					{
						publish(response);
					}
				};
				Future<ServiceResponse<FaceStartPreviewResponse>> future = faceService.startPreview(faceDeviceName,
			                                                                                        responseProcessor);
				return future.get();
			}
			
			@Override
			protected void process(List<LivePreviewingResponse> chunks)
			{
				for(LivePreviewingResponse chunk : chunks) attachImage(lblFacePreview, chunk.getPreviewImage(),
				                                                       null, null);
			}
			
			@Override
			protected void done()
			{
				try
				{
					ServiceResponse<FaceStartPreviewResponse> serviceResponse = get();
					
					if(serviceResponse.isSuccess())
					{
						logDemo("FaceStartPreview() receives a response.");
						
						FaceStartPreviewResponse result = serviceResponse.getResult();
						
						if(result.getReturnCode() == InitializeResponse.SuccessCodes.SUCCESS)
						{
							logDemo("FaceStartPreview() completes successfully.");
						}
						else
						{
							String sb = "FaceStartPreview()'s response: FAILURE - " + "code = " +
										result.getReturnCode() + " (UNKNOWN).";
							
							logDemo(sb);
						}
					}
					else
					{
						logDemo("FaceStartPreview() failed to receive a response. errorCode = " +
						        serviceResponse.getErrorCode());
						Exception exception = serviceResponse.getException();
						if(exception != null) exception.printStackTrace();
					}
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					if(cause instanceof ExecutionException) cause = cause.getCause();
					
					if(cause instanceof TimeoutException) logDemo("FaceStartPreview() timeout.");
					else if(cause instanceof NotConnectedException) logDemo(
															"FaceStartPreview(): Websocket is not connected.");
					else
					{
						logDemo("FaceStartPreview(): Exception.");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
			}
		}.execute();
		
		logDemo("Started the face device previewing...");
	}
	
	private static void onFaceCapture()
	{
		new SwingWorker<ServiceResponse<CaptureFaceResponse>, LivePreviewingResponse>()
		{
			@Override
			protected ServiceResponse<CaptureFaceResponse> doInBackground() throws Exception
			{
				boolean applyIcao = cboApplyIcao.isSelected();
				Future<ServiceResponse<CaptureFaceResponse>> future = faceService.captureFace(faceDeviceName,
				                                                                              applyIcao);
				return future.get();
			}
			
			@Override
			protected void done()
			{
				try
				{
					ServiceResponse<CaptureFaceResponse> serviceResponse = get();
					
					if(serviceResponse.isSuccess())
					{
						logDemo("FaceCapture() receives a response.");
						
						CaptureFaceResponse result = serviceResponse.getResult();
						
						if(result.getReturnCode() == CaptureFaceResponse.SuccessCodes.SUCCESS)
						{
							String icaoCode = result.getIcaoErrorMessage();
							
							if(CaptureFaceResponse.IcaoCodes.SUCCESS.equals(icaoCode))
							{
								logDemo("FaceCapture()'s response: SUCCESS - CaptureFaceResponse = " +
									            (cboTrimBase64Text.isSelected() ? result.toShortString() : result));
								attachImage(lblFaceCropped, result.getCroppedImage(), RUNTIME_ID + "/",
								                        "face_cropped_" + result.getTransactionId() + ".jpg");
								attachImage(lblFaceCaptured, result.getCapturedImage(), RUNTIME_ID + "/",
								                        "face_captured_" + result.getTransactionId() + ".jpg");
							}
							else
							{
								logDemo("FaceCapture()'s response: FAILURE - CaptureFaceResponse = " +
										            (cboTrimBase64Text.isSelected() ? result.toShortString() : result));
								attachImage(lblFaceCropped, result.getCroppedImage(), RUNTIME_ID + "/",
								                        "face_cropped_" + result.getTransactionId() + ".jpg");
								attachImage(lblFaceCaptured, result.getCapturedImage(), RUNTIME_ID + "/",
								                        "face_captured_" + result.getTransactionId() + ".jpg");
							}
						}
						else
						{
							StringBuilder sb = new StringBuilder("FaceCapture()'s response: FAILURE - ");
							sb.append("code = ");
							sb.append(result.getReturnCode());
							sb.append(" ");
							
							switch(result.getReturnCode())
							{
								case CaptureFaceResponse.FailureCodes.DEVICE_NOT_FOUND_OR_UNPLUGGED:
																sb.append("(DEVICE_NOT_FOUND_OR_UNPLUGGED)."); break;
								case CaptureFaceResponse.FailureCodes.EXCEPTION_WHILE_CAPTURING:
																	sb.append("(EXCEPTION_WHILE_CAPTURING)."); break;
								default: sb.append("(UNKNOWN)."); break;
							}
							
							logDemo(sb.toString());
						}
						
						txtIcaoResult.setText(result.getIcaoErrorMessage());
					}
					else
					{
						logDemo("FaceCapture() failed to receive a response. errorCode = " +
								                                                    serviceResponse.getErrorCode());
						Exception exception = serviceResponse.getException();
						if(exception != null) exception.printStackTrace();
					}
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					if(cause instanceof ExecutionException) cause = cause.getCause();
					
					if(cause instanceof TimeoutException) logDemo("FaceCapture() timeout.");
					else if(cause instanceof NotConnectedException) logDemo(
																"FaceCapture(): Websocket is not connected.");
					else
					{
						logDemo("FaceCapture(): Exception.");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
			}
		}.execute();
		
		txtIcaoResult.setText("");
		lblFaceCaptured.setIcon(null);
		lblFaceCropped.setIcon(null);
		logDemo("Fired the face capture command!");
	}
	
	private static void onFaceStopPreview()
	{
		new SwingWorker<ServiceResponse<FaceStopPreviewResponse>, LivePreviewingResponse>()
		{
			@Override
			protected ServiceResponse<FaceStopPreviewResponse> doInBackground() throws Exception
			{
				Future<ServiceResponse<FaceStopPreviewResponse>> future = faceService.stopPreview(faceDeviceName);
				return future.get();
			}
			
			@Override
			protected void done()
			{
				try
				{
					ServiceResponse<FaceStopPreviewResponse> serviceResponse = get();
					
					if(serviceResponse.isSuccess())
					{
						logDemo("FaceStopPreview() receives a response.");
						
						FaceStopPreviewResponse result = serviceResponse.getResult();
						
						if(result.getReturnCode() == FaceStopPreviewResponse.SuccessCodes.SUCCESS)
						{
							logDemo("FaceStopPreview()'s response: SUCCESS.");
						}
						else
						{
							StringBuilder sb = new StringBuilder("FaceStopPreview()'s response: FAILURE - ");
							sb.append("code = ");
							sb.append(result.getReturnCode());
							sb.append(" ");
							
							switch(result.getReturnCode())
							{
								default: sb.append("(UNKNOWN)."); break;
							}
							
							logDemo(sb.toString());
						}
					}
					else
					{
						logDemo("FaceStopPreview() failed to receive a response. errorCode = " +
								                                                    serviceResponse.getErrorCode());
						Exception exception = serviceResponse.getException();
						if(exception != null) exception.printStackTrace();
					}
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					if(cause instanceof ExecutionException) cause = cause.getCause();
					
					if(cause instanceof TimeoutException) logDemo("FaceStopPreview() timeout.");
					else if(cause instanceof NotConnectedException) logDemo(
																"FaceStopPreview(): Websocket is not connected.");
					else
					{
						logDemo("FaceStopPreview(): Exception.");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
			}
		}.execute();
	}
	
	private static void onDeinitializeFaceDevice()
	{
		final Future<ServiceResponse<InitializeResponse>> future = faceService.deinitialize(faceDeviceName);
		final JLabel lblStatus = new JLabel("Deinitializing the Face Device...");
		final JDialog dialog = createProgressDialog(frame, "Waiting for Bio-Kit", lblStatus,
	                                                new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				future.cancel(true);
			}
		});
		
		final SwingWorker<ServiceResponse<InitializeResponse>, String> swingWorker =
														new SwingWorker<ServiceResponse<InitializeResponse>, String>()
		{
			@Override
			protected ServiceResponse<InitializeResponse> doInBackground() throws Exception
			{
				return future.get();
			}
			
			@Override
			protected void done()
			{
				try
				{
					if(future.isCancelled())
					{
						logDemo("FaceDeviceDeinitialize() is cancelled.");
						return;
					}
					
					ServiceResponse<InitializeResponse> serviceResponse = get();
					
					if(serviceResponse.isSuccess())
					{
						logDemo("FaceDeviceDeinitialize() receives a response.");
						
						InitializeResponse result = serviceResponse.getResult();
						
						if(result.getReturnCode() == InitializeResponse.SuccessCodes.SUCCESS)
						{
							fingerprintDeviceName = result.getCurrentDeviceName();
							logDemo("FaceDeviceDeinitialize()'s response: SUCCESS - faceDeviceName = " +
							        faceDeviceName);
						}
						else
						{
							StringBuilder sb = new StringBuilder("FaceDeviceDeinitialize()'s response: FAILURE - ");
							sb.append("code = ");
							sb.append(result.getReturnCode());
							sb.append(" ");
							
							switch(result.getReturnCode())
							{
								case InitializeResponse.FailureCodes.DEVICE_BUSY: sb.append("(DEVICE_BUSY)."); break;
								case InitializeResponse.FailureCodes.DEVICE_NOT_FOUND_OR_UNPLUGGED:
																sb.append("(DEVICE_NOT_FOUND_OR_UNPLUGGED)."); break;
								case InitializeResponse.FailureCodes.EXCEPTION: sb.append("(EXCEPTION)."); break;
								default: sb.append("(UNKNOWN)."); break;
							}
							
							logDemo(sb.toString());
						}
					}
					else
					{
						logDemo("FaceDeviceDeinitialize() failed to receive a response. errorCode = " +
						        serviceResponse.getErrorCode());
						Exception exception = serviceResponse.getException();
						if(exception != null) exception.printStackTrace();
					}
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					if(cause instanceof ExecutionException) cause = cause.getCause();
					
					if(cause instanceof TimeoutException) logDemo("FaceDeviceDeinitialize() timeout.");
					else if(cause instanceof NotConnectedException) logDemo(
							"FaceDeviceDeinitialize(): Websocket is not connected.");
					else
					{
						logDemo("FaceDeviceDeinitialize(): Exception.");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
				finally
				{
					dialog.dispose();
				}
			}
		};
		
		dialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				swingWorker.execute();
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				future.cancel(true);
			}
		});
		dialog.setVisible(true);
	}
	
	private static void onFindDuplicates(int position)
	{
		Map<Integer, String> gallery = new HashMap<Integer, String>();
		Map<Integer, String> probes = new HashMap<Integer, String>();
		
		for(Map.Entry<Integer, String> entry : segmentedFingerTemplateMap.entrySet())
		{
			if(position == 13)
			{
				if(entry.getKey() == 2 || entry.getKey() == 3 || entry.getKey() == 4 || entry.getKey() == 5)
				{
					probes.put(entry.getKey(), entry.getValue());
				}
				else gallery.put(entry.getKey(), entry.getValue());
			}
			else if(position == 14)
			{
				if(entry.getKey() == 7 || entry.getKey() == 8 || entry.getKey() == 9 || entry.getKey() == 10)
				{
					probes.put(entry.getKey(), entry.getValue());
				}
				else gallery.put(entry.getKey(), entry.getValue());
			}
			else if(position == 15)
			{
				if(entry.getKey() == 1 || entry.getKey() == 6)
				{
					probes.put(entry.getKey(), entry.getValue());
				}
				else gallery.put(entry.getKey(), entry.getValue());
			}
		}
		
		final Future<ServiceResponse<DuplicatedFingerprintsResponse>> future =
											fingerprintUtilitiesService.findDuplicatedFingerprints(gallery, probes);
		final JLabel lblStatus = new JLabel("Initializing the Fingerprint Device...");
		final JDialog dialog = createProgressDialog(frame, "Waiting for Bio-Kit", lblStatus,
		                                            new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				future.cancel(true);
			}
		});
		
		final SwingWorker<ServiceResponse<DuplicatedFingerprintsResponse>, String> swingWorker =
											new SwingWorker<ServiceResponse<DuplicatedFingerprintsResponse>, String>()
		{
			@Override
			protected ServiceResponse<DuplicatedFingerprintsResponse> doInBackground() throws Exception
			{
				return future.get();
			}
			
			@Override
			protected void done()
			{
				try
				{
					if(future.isCancelled())
					{
						logDemo("FingerprintFindDuplicates() is cancelled.");
						return;
					}
					
					ServiceResponse<DuplicatedFingerprintsResponse> serviceResponse = get();
					
					if(serviceResponse.isSuccess())
					{
						logDemo("FingerprintFindDuplicates() receives a response.");
						
						DuplicatedFingerprintsResponse result = serviceResponse.getResult();
						
						if(result.getReturnCode() == DuplicatedFingerprintsResponse.SuccessCodes.SUCCESS)
						{
							Map<Integer, Boolean> duplicatedFingers = result.getDuplicatedFingers();
							logDemo("FingerprintFindDuplicates()'s response: SUCCESS - duplicatedFingers = " +
							        duplicatedFingers);
						}
						else
						{
							StringBuilder sb = new StringBuilder("FingerprintFindDuplicates()'s response: FAILURE - ");
							sb.append("code = ");
							sb.append(result.getReturnCode());
							sb.append(" ");
							
							switch(result.getReturnCode())
							{
								case DuplicatedFingerprintsResponse.FailureCodes.FAILED_TO_FIND_DUPLICATES:
																	sb.append("(FAILED_TO_FIND_DUPLICATES)."); break;
								default: sb.append("(UNKNOWN)."); break;
							}
							
							logDemo(sb.toString());
						}
					}
					else
					{
						logDemo("FingerprintFindDuplicates() failed to receive a response. errorCode = " +
						        serviceResponse.getErrorCode());
						Exception exception = serviceResponse.getException();
						if(exception != null) exception.printStackTrace();
					}
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					if(cause instanceof ExecutionException) cause = cause.getCause();
					
					if(cause instanceof TimeoutException) logDemo("FingerprintFindDuplicates() timeout.");
					else if(cause instanceof NotConnectedException) logDemo(
													"FingerprintFindDuplicates(): Websocket is not connected.");
					else
					{
						logDemo("FingerprintFindDuplicates(): Exception.");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
				finally
				{
					dialog.dispose();
				}
			}
		};
		
		dialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				swingWorker.execute();
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				future.cancel(true);
			}
		});
		dialog.setVisible(true);
		
		logDemo("Finding duplicated for gallery = " + gallery.keySet() + " and probes = " +
		        probes.keySet() + "...");
	}
	
	private static void onFingerprintDeviceInitialize(int position)
	{
		final Future<ServiceResponse<InitializeResponse>> future = fingerprintService.initialize(position);
		final JLabel lblStatus = new JLabel("Initializing the Fingerprint Device...");
		final JDialog dialog = createProgressDialog(frame, "Waiting for Bio-Kit", lblStatus,
		                                            new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				future.cancel(true);
			}
		});
		
		final SwingWorker<ServiceResponse<InitializeResponse>, String> swingWorker =
														new SwingWorker<ServiceResponse<InitializeResponse>, String>()
		{
			@Override
			protected ServiceResponse<InitializeResponse> doInBackground() throws Exception
			{
				return future.get();
			}
			
			@Override
			protected void done()
			{
				try
				{
					if(future.isCancelled())
					{
						logDemo("FingerprintDeviceInitialize() is cancelled.");
						return;
					}
					
					ServiceResponse<InitializeResponse> serviceResponse = get();
					
					if(serviceResponse.isSuccess())
					{
						logDemo("FingerprintDeviceInitialize() receives a response.");
						
						InitializeResponse result = serviceResponse.getResult();
						
						if(result.getReturnCode() == InitializeResponse.SuccessCodes.SUCCESS)
						{
							fingerprintDeviceName = result.getCurrentDeviceName();
							logDemo(
								"FingerprintDeviceInitialize()'s response: SUCCESS - fingerprintDeviceName = " +
								fingerprintDeviceName);
						}
						else
						{
							StringBuilder sb = new StringBuilder(
															"FingerprintDeviceInitialize()'s response: FAILURE - ");
							sb.append("code = ");
							sb.append(result.getReturnCode());
							sb.append(" ");
							
							switch(result.getReturnCode())
							{
								case InitializeResponse.FailureCodes.DEVICE_BUSY: sb.append("(DEVICE_BUSY)."); break;
								case InitializeResponse.FailureCodes.DEVICE_NOT_FOUND_OR_UNPLUGGED:
																sb.append("(DEVICE_NOT_FOUND_OR_UNPLUGGED)."); break;
								case InitializeResponse.FailureCodes.EXCEPTION: sb.append("(EXCEPTION)."); break;
								case InitializeResponse.FailureCodes.EXCEPTION2: sb.append("(EXCEPTION2)."); break;
								default: sb.append("(UNKNOWN)."); break;
							}
							
							logDemo(sb.toString());
						}
					}
					else
					{
						logDemo("FingerprintDeviceInitialize() failed to receive a response. errorCode = " +
								                                                    serviceResponse.getErrorCode());
						Exception exception = serviceResponse.getException();
						if(exception != null) exception.printStackTrace();
					}
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					if(cause instanceof ExecutionException) cause = cause.getCause();
					
					if(cause instanceof TimeoutException) logDemo("FingerprintDeviceInitialize() timeout.");
					else if(cause instanceof NotConnectedException) logDemo(
												"FingerprintDeviceInitialize(): Websocket is not connected.");
					else
					{
						logDemo("FingerprintDeviceInitialize(): Exception.");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
				finally
				{
					dialog.dispose();
				}
			}
		};
		
		dialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				swingWorker.execute();
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				future.cancel(true);
			}
		});
		dialog.setVisible(true);
	}
	
	private static void onFingerprintStartPreviewAndAutoCapture(final int position, final int expectedFingersCount,
                                                                final List<Integer> missingFingers)
	{
		new SwingWorker<ServiceResponse<CaptureFingerprintResponse>, LivePreviewingResponse>()
		{
			@Override
			protected ServiceResponse<CaptureFingerprintResponse> doInBackground() throws Exception
			{
				currentFingerprintDeviceCapturingPosition = position;
				ResponseProcessor<LivePreviewingResponse> responseProcessor =
																	new ResponseProcessor<LivePreviewingResponse>()
				{
					@Override
					public void processResponse(LivePreviewingResponse response)
					{
						publish(response);
					}
				};
				Future<ServiceResponse<CaptureFingerprintResponse>> future =
						fingerprintService.startPreviewAndAutoCapture(fingerprintDeviceName, position,
						                                              expectedFingersCount, missingFingers,
						                                              responseProcessor);
				return future.get();
			}
			
			@Override
			protected void process(List<LivePreviewingResponse> chunks)
			{
				for(LivePreviewingResponse chunk : chunks) attachImage(lblFingerprintPreview, chunk.getPreviewImage(),
				                                                       null, null);
			}
			
			@Override
			protected void done()
			{
				try
				{
					ServiceResponse<CaptureFingerprintResponse> serviceResponse = get();
					
					if(serviceResponse.isSuccess())
					{
						logDemo("FingerprintStartPreviewAndAutoCapture() receives a response.");
						
						CaptureFingerprintResponse result = serviceResponse.getResult();
						
						if(result.getReturnCode() == CaptureFingerprintResponse.SuccessCodes.SUCCESS)
						{
							logDemo(
								"FingerprintStartPreviewAndAutoCapture()'s response: SUCCESS - " +
								"CaptureFingerprintResponse = " +
								(cboTrimBase64Text.isSelected() ? result.toShortString() : result));
							
							attachImage(lblFingerprintCaptured, result.getCapturedImage(),
							            RUNTIME_ID + "/", "full_fingers_" +
							            result.getPosition() + "_" + result.getTransactionId() + ".jpg");
							txtIsWrongSlap.setText(String.valueOf(result.isWrongSlap()));
							List<DMFingerData> fingerData = result.getFingerData();
							
							if(fingerData != null)
							{
								for(DMFingerData dmFingerData : fingerData)
								{
									segmentedFingerTemplateMap.put(dmFingerData.getPosition(),
									                               dmFingerData.getTemplate());
									
									switch(dmFingerData.getPosition())
									{
										case 1:
										{
											attachImage(lblRightThumb, dmFingerData.getFinger(),
											            RUNTIME_ID + "/", "segmented_finger_" +
											            dmFingerData.getPosition() + "_" + result.getTransactionId() +
											            ".jpg");
											txtNfiqRightThuumb.setText(String.valueOf(dmFingerData.getNfiqQuality()));
											txtMinutiaeCountRightThuumb.setText(
																	String.valueOf(dmFingerData.getMinutiaeCount()));
											txtIntensityRightThuumb.setText(
																	String.valueOf(dmFingerData.getIntensity()) + "%");
											btnThumbsFindDuplicates.setEnabled(true);
											break;
										}
										case 2:
										{
											attachImage(lblRightIndex, dmFingerData.getFinger(),
											            RUNTIME_ID + "/", "segmented_finger_" +
											            dmFingerData.getPosition() + "_" + result.getTransactionId() +
											            ".jpg");
											txtNfiqRightIndex.setText(String.valueOf(dmFingerData.getNfiqQuality()));
											txtMinutiaeCountRightIndex.setText(
																	String.valueOf(dmFingerData.getMinutiaeCount()));
											txtIntensityRightIndex.setText(
																	String.valueOf(dmFingerData.getIntensity()) + "%");
											btnRightSlapFindDuplicates.setEnabled(true);
											break;
										}
										case 3:
										{
											attachImage(lblRightMiddle, dmFingerData.getFinger(),
											            RUNTIME_ID + "/", "segmented_finger_" +
											            dmFingerData.getPosition() + "_" + result.getTransactionId() +
											            ".jpg");
											txtNfiqRightMiddle.setText(String.valueOf(dmFingerData.getNfiqQuality()));
											txtMinutiaeCountRightMiddle.setText(
																	String.valueOf(dmFingerData.getMinutiaeCount()));
											txtIntensityRightMiddle.setText(
																	String.valueOf(dmFingerData.getIntensity()) + "%");
											btnRightSlapFindDuplicates.setEnabled(true);
											break;
										}
										case 4:
										{
											attachImage(lblRightRing, dmFingerData.getFinger(),
											            RUNTIME_ID + "/", "segmented_finger_" +
											            dmFingerData.getPosition() + "_" + result.getTransactionId() +
											            ".jpg");
											txtNfiqRightRing.setText(String.valueOf(dmFingerData.getNfiqQuality()));
											txtMinutiaeCountRightRing.setText(
																	String.valueOf(dmFingerData.getMinutiaeCount()));
											txtIntensityRightRing.setText(
																	String.valueOf(dmFingerData.getIntensity()) + "%");
											btnRightSlapFindDuplicates.setEnabled(true);
											break;
										}
										case 5:
										{
											attachImage(lblRightLittle, dmFingerData.getFinger(),
											            RUNTIME_ID + "/", "segmented_finger_" +
											            dmFingerData.getPosition() + "_" + result.getTransactionId() +
											            ".jpg");
											txtNfiqRightLittle.setText(String.valueOf(dmFingerData.getNfiqQuality()));
											txtMinutiaeCountRightLittle.setText(
																	String.valueOf(dmFingerData.getMinutiaeCount()));
											txtIntensityRightLittle.setText(
																	String.valueOf(dmFingerData.getIntensity()) + "%");
											btnRightSlapFindDuplicates.setEnabled(true);
											break;
										}
										case 6:
										{
											attachImage(lblLeftThumb, dmFingerData.getFinger(),
											            RUNTIME_ID + "/", "segmented_finger_" +
											            dmFingerData.getPosition() + "_" + result.getTransactionId() +
											            ".jpg");
											txtNfiqLeftThuumb.setText(String.valueOf(dmFingerData.getNfiqQuality()));
											txtMinutiaeCountLeftThuumb.setText(
																	String.valueOf(dmFingerData.getMinutiaeCount()));
											txtIntensityLeftThuumb.setText(
																	String.valueOf(dmFingerData.getIntensity()) + "%");
											btnThumbsFindDuplicates.setEnabled(true);
											break;
										}
										case 7:
										{
											attachImage(lblLeftIndex, dmFingerData.getFinger(),
											            RUNTIME_ID + "/", "segmented_finger_" +
											            dmFingerData.getPosition() + "_" + result.getTransactionId() +
											            ".jpg");
											txtNfiqLeftIndex.setText(String.valueOf(dmFingerData.getNfiqQuality()));
											txtMinutiaeCountLeftIndex.setText(
																	String.valueOf(dmFingerData.getMinutiaeCount()));
											txtIntensityLeftIndex.setText(
																	String.valueOf(dmFingerData.getIntensity()) + "%");
											btnLeftSlapFindDuplicates.setEnabled(true);
											break;
										}
										case 8:
										{
											attachImage(lblLeftMiddle, dmFingerData.getFinger(),
											            RUNTIME_ID + "/", "segmented_finger_" +
											            dmFingerData.getPosition() + "_" + result.getTransactionId() +
											            ".jpg");
											txtNfiqLeftMiddle.setText(String.valueOf(dmFingerData.getNfiqQuality()));
											txtMinutiaeCountLeftMiddle.setText(
																	String.valueOf(dmFingerData.getMinutiaeCount()));
											txtIntensityLeftMiddle.setText(
																	String.valueOf(dmFingerData.getIntensity()) + "%");
											btnLeftSlapFindDuplicates.setEnabled(true);
											break;
										}
										case 9:
										{
											attachImage(lblLeftRing, dmFingerData.getFinger(),
											            RUNTIME_ID + "/", "segmented_finger_" +
											            dmFingerData.getPosition() + "_" + result.getTransactionId() +
											            ".jpg");
											txtNfiqLeftRing.setText(String.valueOf(dmFingerData.getNfiqQuality()));
											txtMinutiaeCountLeftRing.setText(
																	String.valueOf(dmFingerData.getMinutiaeCount()));
											txtIntensityLeftRing.setText(
																	String.valueOf(dmFingerData.getIntensity()) + "%");
											btnLeftSlapFindDuplicates.setEnabled(true);
											break;
										}
										case 10:
										{
											attachImage(lblLeftLittle, dmFingerData.getFinger(),
											            RUNTIME_ID + "/", "segmented_finger_" +
											            dmFingerData.getPosition() + "_" + result.getTransactionId() +
											            ".jpg");
											txtNfiqLeftLittle.setText(String.valueOf(dmFingerData.getNfiqQuality()));
											txtMinutiaeCountLeftLittle.setText(
																	String.valueOf(dmFingerData.getMinutiaeCount()));
											txtIntensityLeftLittle.setText(
																	String.valueOf(dmFingerData.getIntensity()) + "%");
											btnLeftSlapFindDuplicates.setEnabled(true);
											break;
										}
									}
								}
							}
						}
						else
						{
							StringBuilder sb = new StringBuilder(
													"FingerprintStartPreviewAndAutoCapture()'s response: FAILURE - ");
							sb.append("code = ");
							sb.append(result.getReturnCode());
							sb.append(" ");
							
							switch(result.getReturnCode())
							{
								case CaptureFingerprintResponse.FailureCodes.DEVICE_NOT_FOUND_OR_UNPLUGGED:
																sb.append("(DEVICE_NOT_FOUND_OR_UNPLUGGED)."); break;
								case CaptureFingerprintResponse.FailureCodes.DEVICE_BUSY:
																					sb.append("(DEVICE_BUSY)."); break;
								case CaptureFingerprintResponse.FailureCodes.WRONG_NUMBER_OF_EXPECTED_FINGERS:
																sb.append("(WRONG_NUMBER_OF_EXPECTED_FINGERS)."); break;
								case CaptureFingerprintResponse.FailureCodes.SEGMENTATION_FAILED:
																			sb.append("(SEGMENTATION_FAILED)."); break;
								case CaptureFingerprintResponse.FailureCodes.WSQ_CONVERSION_FAILED:
																		sb.append("(WSQ_CONVERSION_FAILED)."); break;
								case CaptureFingerprintResponse.FailureCodes.FAILED_TO_CAPTURE_FINAL_IMAGE:
																sb.append("(FAILED_TO_CAPTURE_FINAL_IMAGE)."); break;
								case CaptureFingerprintResponse.FailureCodes.EXCEPTION_IN_FINGER_HANDLER_CAPTURE:
															sb.append("(EXCEPTION_IN_FINGER_HANDLER_CAPTURE)."); break;
								default: sb.append("(UNKNOWN)."); break;
							}
							
							logDemo(sb.toString());
						}
					}
					else
					{
						logDemo(
							"FingerprintStartPreviewAndAutoCapture() failed to receive a response. errorCode = " +
							serviceResponse.getErrorCode());
						Exception exception = serviceResponse.getException();
						if(exception != null) exception.printStackTrace();
					}
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					if(cause instanceof ExecutionException) cause = cause.getCause();
					
					if(cause instanceof TimeoutException) logDemo(
															"FingerprintStartPreviewAndAutoCapture() timeout.");
					else if(cause instanceof NotConnectedException) logDemo(
										"FingerprintStartPreviewAndAutoCapture(): Websocket is not connected.");
					else
					{
						logDemo("FingerprintStartPreviewAndAutoCapture(): Exception.");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
			}
		}.execute();
		
		logDemo("Started the fingerprint device previewing...");
	}
	
	private static void onFingerprintStopPreview()
	{
		new SwingWorker<ServiceResponse<FingerprintStopPreviewResponse>, LivePreviewingResponse>()
		{
			@Override
			protected ServiceResponse<FingerprintStopPreviewResponse> doInBackground() throws Exception
			{
				Future<ServiceResponse<FingerprintStopPreviewResponse>> future =
					fingerprintService.cancelCapture(fingerprintDeviceName, currentFingerprintDeviceCapturingPosition);
				return future.get();
			}
			
			@Override
			protected void done()
			{
				try
				{
					ServiceResponse<FingerprintStopPreviewResponse> serviceResponse = get();
					
					if(serviceResponse.isSuccess())
					{
						logDemo("FingerprintStopPreview() receives a response.");
						
						FingerprintStopPreviewResponse result = serviceResponse.getResult();
						
						if(result.getReturnCode() == CaptureFingerprintResponse.SuccessCodes.SUCCESS)
						{
							logDemo("FingerprintStopPreview()'s response: SUCCESS.");
						}
						else
						{
							StringBuilder sb = new StringBuilder("FingerprintStopPreview()'s response: FAILURE - ");
							sb.append("code = ");
							sb.append(result.getReturnCode());
							sb.append(" ");
							
							switch(result.getReturnCode())
							{
								case FingerprintStopPreviewResponse.FailureCodes.NOT_CAPTURING_NOW:
																			sb.append("(NOT_CAPTURING_NOW)."); break;
								case FingerprintStopPreviewResponse.FailureCodes.
													EXCEPTION_IN_FINGER_HANDLER_CANCEL_CAPTURE:
													sb.append("(EXCEPTION_IN_FINGER_HANDLER_CANCEL_CAPTURE)."); break;
								case FingerprintStopPreviewResponse.FailureCodes.EXCEPTION:
																					sb.append("(EXCEPTION)."); break;
								default: sb.append("(UNKNOWN)."); break;
							}
							
							logDemo(sb.toString());
						}
					}
					else
					{
						logDemo("FingerprintStopPreview() failed to receive a response. errorCode = " +
		                        serviceResponse.getErrorCode());
						Exception exception = serviceResponse.getException();
						if(exception != null) exception.printStackTrace();
					}
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					if(cause instanceof ExecutionException) cause = cause.getCause();
					
					if(cause instanceof TimeoutException) logDemo("FingerprintStopPreview() timeout.");
					else if(cause instanceof NotConnectedException) logDemo(
													"FingerprintStopPreview(): Websocket is not connected.");
					else
					{
						logDemo("FingerprintStopPreview(): Exception.");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
			}
		}.execute();
	}
	
	private static void onDeinitializeFingerprintDevice(int position)
	{
		final Future<ServiceResponse<InitializeResponse>> future = fingerprintService.deinitialize(position,
	                                                                                           fingerprintDeviceName);
		final JLabel lblStatus = new JLabel("Deinitializing the Fingerprint Device...");
		final JDialog dialog = createProgressDialog(frame, "Waiting for Bio-Kit", lblStatus,
		                                            new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				future.cancel(true);
			}
		});
		
		final SwingWorker<ServiceResponse<InitializeResponse>, String> swingWorker =
														new SwingWorker<ServiceResponse<InitializeResponse>, String>()
		{
			@Override
			protected ServiceResponse<InitializeResponse> doInBackground() throws Exception
			{
				return future.get();
			}
			
			@Override
			protected void done()
			{
				try
				{
					if(future.isCancelled())
					{
						logDemo("FingerprintDeviceDeinitialize() is cancelled.");
						return;
					}
					
					ServiceResponse<InitializeResponse> serviceResponse = get();
					
					if(serviceResponse.isSuccess())
					{
						logDemo("FingerprintDeviceDeinitialize() receives a response.");
						
						InitializeResponse result = serviceResponse.getResult();
						
						if(result.getReturnCode() == InitializeResponse.SuccessCodes.SUCCESS)
						{
							fingerprintDeviceName = result.getCurrentDeviceName();
							logDemo(
								"FingerprintDeviceDeinitialize()'s response: SUCCESS - fingerprintDeviceName = " +
								fingerprintDeviceName);
						}
						else
						{
							StringBuilder sb = new StringBuilder(
															"FingerprintDeviceDeinitialize()'s response: FAILURE - ");
							sb.append("code = ");
							sb.append(result.getReturnCode());
							sb.append(" ");
							
							switch(result.getReturnCode())
							{
								case InitializeResponse.FailureCodes.DEVICE_BUSY: sb.append("(DEVICE_BUSY)."); break;
								case InitializeResponse.FailureCodes.DEVICE_NOT_FOUND_OR_UNPLUGGED:
																sb.append("(DEVICE_NOT_FOUND_OR_UNPLUGGED)."); break;
								case InitializeResponse.FailureCodes.EXCEPTION: sb.append("(EXCEPTION)."); break;
								case InitializeResponse.FailureCodes.EXCEPTION2: sb.append("(EXCEPTION2)."); break;
								default: sb.append("(UNKNOWN)."); break;
							}
							
							logDemo(sb.toString());
						}
					}
					else
					{
						logDemo("FingerprintDeviceDeinitialize() failed to receive a response. errorCode = " +
						        serviceResponse.getErrorCode());
						Exception exception = serviceResponse.getException();
						if(exception != null) exception.printStackTrace();
					}
				}
				catch(ExecutionException e)
				{
					Throwable cause = e.getCause();
					if(cause instanceof ExecutionException) cause = cause.getCause();
					
					if(cause instanceof TimeoutException) logDemo("FingerprintDeviceDeinitialize() timeout.");
					else if(cause instanceof NotConnectedException) logDemo(
											"FingerprintDeviceDeinitialize(): Websocket is not connected.");
					else
					{
						logDemo("FingerprintDeviceDeinitialize(): Exception.");
						logDemo(cause);
					}
				}
				catch(InterruptedException e) // should't happen
				{
					e.printStackTrace();
				}
				finally
				{
					dialog.dispose();
				}
			}
		};
		
		dialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				swingWorker.execute();
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				future.cancel(true);
			}
		});
		dialog.setVisible(true);
	}
	
	private static void attachImage(JLabel container, String imageDataBase64, String directoryName, String fileName)
	{
		if(imageDataBase64 == null)
		{
			logDemo("imageData is null.");
			return;
		}
		
		try
		{
			byte[] imageData = new BASE64Decoder().decodeBuffer(imageDataBase64);
			ByteArrayInputStream stream = new ByteArrayInputStream(imageData);
			BufferedImage bufferedImage = ImageIO.read(stream);
			StretchIcon icon = new StretchIcon(bufferedImage);
			container.setIcon(icon);
			
			if(directoryName != null && fileName != null)
			{
				File directory = new File("C:/bio/user-apps/" + System.getProperty("user.name") +
						                                                    "/biokit-library/demo/" + directoryName);
				directory.mkdirs();
				File file = new File(directory, fileName);
				
				logDemo("Saving the JPG image to " + file.getAbsolutePath());
				ImageIO.write(bufferedImage, "JPG", file);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static JDialog createProgressDialog(Window owner, String dialogTitle, JLabel lblStatus,
                                                ActionListener onCancelButtonClicked)
	{
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		JButton btnCancel = new JButton("Cancel");
		if(onCancelButtonClicked != null) btnCancel.addActionListener(onCancelButtonClicked);
		
		JPanel panButton = new JPanel();
		panButton.add(btnCancel);
		
		JPanel panContainer = new JPanel(new BorderLayout(5, 5));
		panContainer.add(lblStatus, BorderLayout.CENTER);
		panContainer.add(progressBar, BorderLayout.NORTH);
		panContainer.add(panButton, BorderLayout.SOUTH);
		panContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JDialog dialog = new JDialog(owner, dialogTitle);
		dialog.setModal(true);
		dialog.getContentPane().add(panContainer);
		dialog.pack();
		dialog.setLocationRelativeTo(owner);
		return dialog;
	}
}