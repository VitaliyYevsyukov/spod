package presenters.object;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Vitaly on 05.12.2016.
 */
public class RoadObjectModel {

	private String roadObjectConfigurationId;
	private String roadObjectName;
	private String roadObjectCountry;
	private String roadObjectRegion;
	private String roadObjectCity;
	private String roadObjectObjectNumber;
	private String roadObjectNetworkAddress;
	private String roadObjectMagistral;
	private String roadObjectProtocol;
	private String roadObjectDateOfCreation;
	private String roadObjectLaunchDate;
	private String roadObjectTechnologist;
	private String roadObjectCharge;
	private String roadObjectNote;
	private String roadObjectTypeOfKDK;
	private String roadObjectConnectType;
	private String roadObjectConnectPort;
	private String roadObjectConnectSpeed;
	private String roadObjectKDPPort;
	private String roadObjectKDPSpeed;
	private String roadObjectLEDPort;
	private String roadObjectLEDSpeed;
	private String roadObjectConnModId;
	private String roadObjectIP;
	private String roadObjectDNS;
	private String roadObjectMASK;
	private String roadObjectNTP;
	private String delay;
	private String delayYF;
	private String sleepTime;
	private List<TypeKDK> kdkTypeList = new LinkedList<>();;

	
	public RoadObjectModel() {
		roadObjectConfigurationId = new String();
		roadObjectName = new String();
		roadObjectCountry = new String();
		roadObjectRegion = new String();
		roadObjectCity = new String();
		roadObjectObjectNumber = new String();
		roadObjectNetworkAddress = new String();
		roadObjectMagistral = new String();
		roadObjectProtocol = new String();
		roadObjectDateOfCreation = new String();
		roadObjectLaunchDate = new String();
		roadObjectTechnologist = new String();
		roadObjectCharge = new String();
		roadObjectNote = new String();
		roadObjectTypeOfKDK = new String();
		roadObjectConnectType = new String();
		roadObjectConnectPort = new String();
		roadObjectConnectSpeed = new String();
		roadObjectKDPPort = new String();
		roadObjectKDPSpeed = new String();
		roadObjectLEDPort = new String();
		roadObjectLEDSpeed = new String();
		roadObjectConnModId = new String();
		roadObjectIP = new String();
		roadObjectDNS = new String();
		roadObjectMASK = new String();
		roadObjectNTP = new String();
		delay = new String();
		delayYF = new String();
		sleepTime = new String();
	}

	public String getRoadObjectConfiguratinId() {
		return roadObjectConfigurationId;
	}

	public void setRoadObjectConfiguratinId(String roadObjectId) {
		this.roadObjectConfigurationId = roadObjectId;
	}

	public String getRoadObjectName() {
		return roadObjectName;
	}

	public void setRoadObjectName(String roadObjectName) {
		this.roadObjectName = roadObjectName;
	}

	public String getRoadObjectCountry() {
		return roadObjectCountry;
	}

	public void setRoadObjectCountry(String roadObjectCountry) {
		this.roadObjectCountry = roadObjectCountry;
	}

	public String getRoadObjectRegion() {
		return roadObjectRegion;
	}

	public void setRoadObjectRegion(String roadObjectRegion) {
		this.roadObjectRegion = roadObjectRegion;
	}

	public String getRoadObjectCity() {
		return roadObjectCity;
	}

	public void setRoadObjectCity(String roadObjectCity) {
		this.roadObjectCity = roadObjectCity;
	}

	public String getRoadObjectObjectNumber() {
		return roadObjectObjectNumber;
	}

	public void setRoadObjectObjectNumber(String roadObjectObjectNumber) {
		this.roadObjectObjectNumber = roadObjectObjectNumber;
	}

	public String getRoadObjectNetworkAddress() {
		return roadObjectNetworkAddress;
	}

	public void setRoadObjectNetworkAddress(String roadObjectNetworkAddress) {
		this.roadObjectNetworkAddress = roadObjectNetworkAddress;
	}

	public String getRoadObjectMagistral() {
		return roadObjectMagistral;
	}

	public void setRoadObjectMagistral(String roadObjectMagistral) {
		this.roadObjectMagistral = roadObjectMagistral;
	}

	public String getRoadObjectProtocol() {
		return roadObjectProtocol;
	}

	public void setRoadObjectProtocol(String roadObjectProtocol) {
		this.roadObjectProtocol = roadObjectProtocol;
	}

	public String getRoadObjectDateOfCreation() {
		return roadObjectDateOfCreation;
	}

	public void setRoadObjectDateOfCreation(String roadObjectDateOfCreation) {
		this.roadObjectDateOfCreation = roadObjectDateOfCreation;
	}

	public String getRoadObjectLaunchDate() {
		return roadObjectLaunchDate;
	}

	public void setRoadObjectLaunchDate(String roadObjectLaunchDate) {
		this.roadObjectLaunchDate = roadObjectLaunchDate;
	}

	public String getRoadObjectTechnologist() {
		return roadObjectTechnologist;
	}

	public void setRoadObjectTechnologist(String roadObjectTechnologist) {
		this.roadObjectTechnologist = roadObjectTechnologist;
	}

	public String getRoadObjectNote() {
		return roadObjectNote;
	}

	public void setRoadObjectNote(String roadObjectNote) {
		this.roadObjectNote = roadObjectNote;
	}

	public String getRoadObjectTypeOfKDK() {
		return roadObjectTypeOfKDK;
	}

	public void setRoadObjectTypeOfKDK(String roadObjectTypeOfKDK) {
		this.roadObjectTypeOfKDK = roadObjectTypeOfKDK;
	}

	public String getRoadObjectConnectType() {
		return roadObjectConnectType;
	}

	public void setRoadObjectConnectType(String roadObjectConnectType) {
		this.roadObjectConnectType = roadObjectConnectType;
	}

	public String getRoadObjectConnectPort() {
		return roadObjectConnectPort;
	}

	public void setRoadObjectConnectPort(String roadObjectConnectPort) {
		this.roadObjectConnectPort = roadObjectConnectPort;
	}

	public String getRoadObjectConnectSpeed() {
		return roadObjectConnectSpeed;
	}

	public void setRoadObjectConnectSpeed(String roadObjectConnectSpeed) {
		this.roadObjectConnectSpeed = roadObjectConnectSpeed;
	}

	public String getRoadObjectKDPPort() {
		return roadObjectKDPPort;
	}

	public void setRoadObjectKDPPort(String roadObjectKDPPort) {
		this.roadObjectKDPPort = roadObjectKDPPort;
	}

	public String getRoadObjectKDPSpeed() {
		return roadObjectKDPSpeed;
	}

	public void setRoadObjectKDPSpeed(String roadObjectKDPSpeed) {
		this.roadObjectKDPSpeed = roadObjectKDPSpeed;
	}

	public String getRoadObjectLEDPort() {
		return roadObjectLEDPort;
	}

	public void setRoadObjectLEDPort(String roadObjectLEDPort) {
		this.roadObjectLEDPort = roadObjectLEDPort;
	}

	public String getRoadObjectLEDSpeed() {
		return roadObjectLEDSpeed;
	}

	public void setRoadObjectLEDSpeed(String roadObjectLEDSpeed) {
		this.roadObjectLEDSpeed = roadObjectLEDSpeed;
	}

	public String getRoadObjectConnModId() {
		return roadObjectConnModId;
	}

	public void setRoadObjectConnModId(String roadObjectConnModId) {
		this.roadObjectConnModId = roadObjectConnModId;
	}

	public String getRoadObjectIP() {
		return roadObjectIP;
	}

	public void setRoadObjectIP(String roadObjectIP) {
		this.roadObjectIP = roadObjectIP;
	}

	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public String getDelayYF() {
		return delayYF;
	}

	public void setDelayYF(String delayYF) {
		this.delayYF = delayYF;
	}

	public String getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}

	public List<TypeKDK> getKdkTypeList() {
		return kdkTypeList;
	}

	public void setKdkTypeList(List<TypeKDK> kdkTypeList) {
		this.kdkTypeList = kdkTypeList;
	}
	
	public String getRoadObjectDNS() {
		return roadObjectDNS;
	}

	public void setRoadObjectDNS(String roadObjectDNS) {
		this.roadObjectDNS = roadObjectDNS;
	}

	public String getRoadObjectMASK() {
		return roadObjectMASK;
	}

	public void setRoadObjectMASK(String roadObjectMASK) {
		this.roadObjectMASK = roadObjectMASK;
	}
	
	public String getRoadObjectNTP() {
		return roadObjectNTP;
	}

	public void setRoadObjectNTP(String roadObjectNTP) {
		this.roadObjectNTP = roadObjectNTP;
	}
	
	public String getRoadObjectCharge() {
		return roadObjectCharge;
	}

	public void setRoadObjectCharge(String roadObjectCharge) {
		this.roadObjectCharge = roadObjectCharge;
	}

	@Override
	public String toString() {
		return "RoadObjectModel " + this.roadObjectName;
	}

}
