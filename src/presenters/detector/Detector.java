package presenters.detector;

public class Detector {

	private String typeDetector;
	private String modelDetector;
	private String locationDetector;
	private String faultTimeoutDetector;
	private String connectionType;
	private String response;
	private String IPDetector;
	private String periodInterrogation;
	private String periodSaving;
	private String port;
	private String portXML;
	private String portHTTP;
	private String spi;
	private String typeZone;
	private String idDetector;
	private String numberDetector;
	private String rootID;

	public Detector() {
		typeDetector = new String();
		modelDetector = new String();
		locationDetector = new String();
		faultTimeoutDetector = new String();
		connectionType = new String();
		response = new String();
		IPDetector = new String();
		periodInterrogation = new String();
		periodSaving = new String();
		port = new String();
		portXML = new String();
		portHTTP = new String();
		spi = new String();
		typeZone = new String();
		rootID = new String();
	}
	
	
	public String getTypeDetector() {
		return typeDetector;
	}

	public void setTypeDetector(String typeDetector) {
		this.typeDetector = typeDetector;
	}

	public String getModelDetector() {
		return modelDetector;
	}

	public void setModelDetector(String modelDetector) {
		this.modelDetector = modelDetector;
	}

	public String getLocationDetector() {
		return locationDetector;
	}

	public void setLocationDetector(String locationDetector) {
		this.locationDetector = locationDetector;
	}

	public String getFaultTimeoutDetector() {
		return faultTimeoutDetector;
	}

	public void setFaultTimeoutDetector(String faultTimeoutDetector) {
		this.faultTimeoutDetector = faultTimeoutDetector;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getIPDetector() {
		return IPDetector;
	}

	public void setIPDetector(String iPDetector) {
		IPDetector = iPDetector;
	}

	public String getPeriodInterrogation() {
		return periodInterrogation;
	}

	public void setPeriodInterrogation(String periodInterrogation) {
		this.periodInterrogation = periodInterrogation;
	}

	public String getPeriodSaving() {
		return periodSaving;
	}

	public void setPeriodSaving(String periodSaving) {
		this.periodSaving = periodSaving;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPortXML() {
		return portXML;
	}

	public void setPortXML(String portXML) {
		this.portXML = portXML;
	}

	public String getPortHTTP() {
		return portHTTP;
	}

	public void setPortHTTP(String portHTTP) {
		this.portHTTP = portHTTP;
	}

	public String getSpi() {
		return spi;
	}

	public void setSpi(String spi) {
		this.spi = spi;
	}
	
	public String getTypeZone() {
		return typeZone;
	}

	public void setTypeZone(String typeZone) {
		this.typeZone = typeZone;
	}
	
	public String getIdDetector() {
		return idDetector;
	}

	public void setIdDetector(String idDetector) {
		this.idDetector = idDetector;
	}

	public String getNumberDetector() {
		return numberDetector;
	}

	public void setNumberDetector(String numberDetector) {
		this.numberDetector = numberDetector;
	}

	public String getRootID() {
		return rootID;
	}

	public void setRootID(String rootID) {
		this.rootID = rootID;
	}
	
}
