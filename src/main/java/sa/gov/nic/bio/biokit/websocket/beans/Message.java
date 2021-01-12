package sa.gov.nic.bio.biokit.websocket.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Message implements Serializable
{
	private String type;
	private int position;
	private String operation;
	private String expectedFingersCount;
	private boolean needIcaoCropping;
	private List<String> initDevicesList;
	private List<DMFingerData> galleryFingers;
	private List<DMFingerData> probeFingers;
	private String transactionId;
	private boolean segmentationRequired;
	private boolean wsqRequired;
	private List<Integer> missingFingersList;
	private String finalImage;
	private String previewImage;
	private String finalWSQImage;
	private String icaoImage;
	private String status;
	private int returnCode;
	private String returnMessage;
	private String icaoErrorMessage;
	private boolean isEnd;
	private List<DMFingerData> dmSegmentedFingers;
	private boolean isWrongSlap;
	private Map<Integer, Boolean> duplicateList;
	private String currentDeviceName;
	private boolean noTimeout;
	private boolean segmentedWsqRequired;
	private Map<String,String> fingerDeviceStatus;
	private Map<String,String> faceDeviceStatus;
	private String slapImageForSegmentation;
	private String imageFormat;
	private MRZData mrzData;
	private String rightIrisImage;
	private String leftIrisImage;
	public String rightIrisCompressed;
	public String leftIrisCompressed;
	
	public String getType(){return type;}
	public void setType(String type){this.type = type;}
	
	public int getPosition(){return position;}
	public void setPosition(int position){this.position = position;}
	
	public String getOperation(){return operation;}
	public void setOperation(String operation){this.operation = operation;}
	
	public String getExpectedFingersCount(){return expectedFingersCount;}
	public void setExpectedFingersCount(String expectedFingersCount){this.expectedFingersCount = expectedFingersCount;}
	
	public boolean isNeedIcaoCropping(){return needIcaoCropping;}
	public void setNeedIcaoCropping(boolean needIcaoCropping){this.needIcaoCropping = needIcaoCropping;}
	
	public List<String> getInitDevicesList(){return initDevicesList;}
	public void setInitDevicesList(List<String> initDevicesList){this.initDevicesList = initDevicesList;}
	
	public List<DMFingerData> getGalleryFingers(){return galleryFingers;}
	public void setGalleryFingers(List<DMFingerData> galleryFingers){this.galleryFingers = galleryFingers;}
	
	public List<DMFingerData> getProbeFingers(){return probeFingers;}
	public void setProbeFingers(List<DMFingerData> probeFingers){this.probeFingers = probeFingers;}
	
	public String getTransactionId(){return transactionId;}
	public void setTransactionId(String transactionId){this.transactionId = transactionId;}
	
	public boolean isSegmentationRequired(){return segmentationRequired;}
	public void setSegmentationRequired(boolean segmentationRequired){this.segmentationRequired = segmentationRequired;}
	
	public boolean isWsqRequired(){return wsqRequired;}
	public void setWsqRequired(boolean wsqRequired){this.wsqRequired = wsqRequired;}
	
	public List<Integer> getMissingFingersList(){return missingFingersList;}
	public void setMissingFingersList(List<Integer> missingFingersList){this.missingFingersList = missingFingersList;}
	
	public String getFinalImage(){return finalImage;}
	public void setFinalImage(String finalImage){this.finalImage = finalImage;}
	
	public String getPreviewImage(){return previewImage;}
	public void setPreviewImage(String previewImage){this.previewImage = previewImage;}
	
	public String getFinalWSQImage(){return finalWSQImage;}
	public void setFinalWSQImage(String finalWSQImage){this.finalWSQImage = finalWSQImage;}
	
	public String getIcaoImage(){return icaoImage;}
	public void setIcaoImage(String icaoImage){this.icaoImage = icaoImage;}
	
	public String getStatus(){return status;}
	public void setStatus(String status){this.status = status;}
	
	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}
	
	public String getReturnMessage(){return returnMessage;}
	public void setReturnMessage(String returnMessage){this.returnMessage = returnMessage;}
	
	public String getIcaoErrorMessage(){return icaoErrorMessage;}
	public void setIcaoErrorMessage(String icaoErrorMessage){this.icaoErrorMessage = icaoErrorMessage;}
	
	public boolean isEnd(){return isEnd;}
	public void setEnd(boolean end){isEnd = end;}
	
	public List<DMFingerData> getDmSegmentedFingers(){return dmSegmentedFingers;}
	public void setDmSegmentedFingers(List<DMFingerData> dmSegmentedFingers)
																	{this.dmSegmentedFingers = dmSegmentedFingers;}
	
	public boolean isWrongSlap(){return isWrongSlap;}
	public void setWrongSlap(boolean wrongSlap){isWrongSlap = wrongSlap;}
	
	public Map<Integer, Boolean> getDuplicateList(){return duplicateList;}
	public void setDuplicateList(Map<Integer, Boolean> duplicateList){this.duplicateList = duplicateList;}
	
	public String getCurrentDeviceName(){return currentDeviceName;}
	public void setCurrentDeviceName(String currentDeviceName){this.currentDeviceName = currentDeviceName;}
	
	public boolean isNoTimeout(){return noTimeout;}
	public void setNoTimeout(boolean noTimeout){this.noTimeout = noTimeout;}
	
	public boolean isSegmentedWsqRequired(){return segmentedWsqRequired;}
	public void setSegmentedWsqRequired(boolean segmentedWsqRequired){this.segmentedWsqRequired = segmentedWsqRequired;}
	
	public Map<String, String> getFingerDeviceStatus(){return fingerDeviceStatus;}
	public void setFingerDeviceStatus(Map<String, String> fingerDeviceStatus)
																		{this.fingerDeviceStatus = fingerDeviceStatus;}
	
	public Map<String, String> getFaceDeviceStatus(){return faceDeviceStatus;}
	public void setFaceDeviceStatus(Map<String, String> faceDeviceStatus){this.faceDeviceStatus = faceDeviceStatus;}
	
	public String getSlapImageForSegmentation(){return slapImageForSegmentation;}
	public void setSlapImageForSegmentation(String slapImageForSegmentation)
															{this.slapImageForSegmentation = slapImageForSegmentation;}
	
	public String getImageFormat(){return imageFormat;}
	public void setImageFormat(String imageFormat){this.imageFormat = imageFormat;}
	
	public MRZData getMrzData(){return mrzData;}
	public void setMrzData(MRZData mrzData){this.mrzData = mrzData;}
	
	public String getRightIrisImage(){return rightIrisImage;}
	public void setRightIrisImage(String rightIrisImage){this.rightIrisImage = rightIrisImage;}
	
	public String getLeftIrisImage(){return leftIrisImage;}
	public void setLeftIrisImage(String leftIrisImage){this.leftIrisImage = leftIrisImage;}
	
	@Override
	public String toString()
	{
		return "Message{" + "type='" + type + '\'' + ", position=" + position + ", operation='" + operation + '\'' +
			   ", expectedFingersCount='" + expectedFingersCount + '\'' + ", needIcaoCropping=" + needIcaoCropping +
			   ", initDevicesList=" + initDevicesList + ", galleryFingers=" + galleryFingers + ", probeFingers=" +
			   probeFingers + ", transactionId='" + transactionId + '\'' + ", segmentationRequired=" +
			   segmentationRequired + ", wsqRequired=" + wsqRequired + ", missingFingersList=" + missingFingersList +
			   ", finalImage='" + finalImage + '\'' + ", previewImage='" + previewImage + '\'' + ", finalWSQImage='" +
			   finalWSQImage + '\'' + ", icaoImage='" + icaoImage + '\'' + ", status='" + status + '\'' +
			   ", returnCode=" + returnCode + ", returnMessage='" + returnMessage + '\'' + ", icaoErrorMessage='" +
			   icaoErrorMessage + '\'' + ", isEnd=" + isEnd + ", dmSegmentedFingers=" + dmSegmentedFingers +
			   ", isWrongSlap=" + isWrongSlap + ", duplicateList=" + duplicateList + ", currentDeviceName='" +
			   currentDeviceName + '\'' + ", noTimeout=" + noTimeout + ", segmentedWsqRequired=" +
			   segmentedWsqRequired + ", fingerDeviceStatus=" + fingerDeviceStatus + ", faceDeviceStatus=" +
			   faceDeviceStatus + ", slapImageForSegmentation='" + slapImageForSegmentation + '\'' +
			   ", imageFormat='" + imageFormat + '\'' + ", mrzData='" + mrzData + '\'' + ", rightIrisImage='" +
				rightIrisImage + '\'' + ", leftIrisImage='" + leftIrisImage + '\'' + '}';
	}
	
	public String toShortString()
	{
		return "Message{" + "type='" + type + '\'' + ", position=" + position + ", operation='" + operation + '\'' +
			   ", expectedFingersCount='" + expectedFingersCount + '\'' + ", needIcaoCropping=" + needIcaoCropping +
			   ", initDevicesList=" + initDevicesList + ", galleryFingers=" +
			   DMFingerData.shortenDMFingerDataList(galleryFingers) + ", probeFingers=" +
			   DMFingerData.shortenDMFingerDataList(probeFingers) + ", transactionId='" + transactionId + '\'' +
			   ", segmentationRequired=" + segmentationRequired + ", wsqRequired=" + wsqRequired +
			   ", missingFingersList=" + missingFingersList + ", finalImage=isNull?'" + (finalImage == null) + '\'' +
			   ", previewImage=isNull?'" + (previewImage == null) + '\'' + ", finalWSQImage=isNull?'" +
			   (finalWSQImage == null) + '\'' + ", icaoImage=isNull?'" + (icaoImage == null) + '\'' + ", status='" +
			   status + '\'' + ", returnCode=" + returnCode + ", returnMessage='" + returnMessage + '\'' +
			   ", icaoErrorMessage='" + icaoErrorMessage + '\'' + ", isEnd=" + isEnd + ", dmSegmentedFingers=" +
			   dmSegmentedFingers + ", isWrongSlap=" + isWrongSlap + ", duplicateList=" + duplicateList +
			   ", currentDeviceName='" + currentDeviceName + '\'' + ", noTimeout=" + noTimeout + '\'' +
			   ", segmentedWsqRequired=" + segmentedWsqRequired + ", fingerDeviceStatus=" + fingerDeviceStatus +
			   ", faceDeviceStatus=" + faceDeviceStatus + ", slapImageForSegmentation=isNull?='" +
			   (slapImageForSegmentation == null) + '\'' + ", imageFormat='" + imageFormat + '\'' + ", mrzData='" +
			   mrzData + '\'' + ", rightIrisImage=isNull?'" + (rightIrisImage == null) + '\'' +
			   ", leftIrisImage=isNull?'" + (leftIrisImage == null) + '\'' + '}';
	}

	public String getRightIrisCompressed() {
		return rightIrisCompressed;
	}

	public void setRightIrisCompressed(String rightIrisCompressed) {
		this.rightIrisCompressed = rightIrisCompressed;
	}

	public String getLeftIrisCompressed() {
		return leftIrisCompressed;
	}

	public void setLeftIrisCompressed(String leftIrisCompressed) {
		this.leftIrisCompressed = leftIrisCompressed;
	}
}