package presenters.promtactu;

/**
 * Created by Vitaly on 23.11.2017.
 */
public class PromtactData {

	private String promtactId;
	private String roadPromtactu_fromPhaseId;
	private String roadPromtactu_toPhaseId;
    private String roadPromtactu_directInTransitId;
    private String roadPromtactu_directionNumber;
    private String roadPromtactu_endGreenAddit;
    private String roadPromtactu_durationGreenBlink;
    private String roadPromtactu_durationYellow;
    private String roadPromtactu_endRed;
    private String roadPromtactu_durationRedYellow;
    private boolean isFullPromtact;

    
	public PromtactData(){
    	promtactId = new String();
        roadPromtactu_fromPhaseId = new String();
        roadPromtactu_toPhaseId = new String();
        roadPromtactu_directInTransitId = new String();
        roadPromtactu_directionNumber = new String();
        roadPromtactu_endGreenAddit = new String();
        roadPromtactu_durationGreenBlink = new String();
        roadPromtactu_durationYellow = new String();
        roadPromtactu_endRed = new String();
        roadPromtactu_durationRedYellow = new String();
        isFullPromtact = false;
    }
	
	
	public boolean isFullPromtact() {
		return isFullPromtact;
	}

	public void setFullPromtact(boolean isFullPromtact) {
		this.isFullPromtact = isFullPromtact;
	}
    
    public String getPromtactId() {
		return promtactId;
	}

	public void setPromtactId(String promtactId) {
		this.promtactId = promtactId;
	}
	
	public String getRoadPromtactu_fromPhaseId() {
		return roadPromtactu_fromPhaseId;
	}

	public void setRoadPromtactu_fromPhaseId(String roadPromtactu_fromPhaseId) {
		this.roadPromtactu_fromPhaseId = roadPromtactu_fromPhaseId;
	}

	public String getRoadPromtactu_toPhaseId() {
		return roadPromtactu_toPhaseId;
	}

	public void setRoadPromtactu_toPhaseId(String roadPromtactu_toPhaseId) {
		this.roadPromtactu_toPhaseId = roadPromtactu_toPhaseId;
	}

	public String getRoadPromtactu_directInTransitId() {
		return roadPromtactu_directInTransitId;
	}

	public void setRoadPromtactu_directInTransitId(String roadPromtactu_directInTransitId) {
		this.roadPromtactu_directInTransitId = roadPromtactu_directInTransitId;
	}

    /*public String getRoadPromtactu_transitionFromThePhaseNumber() {
        return roadPromtactu_fromPhaseId;
    }

    public void setRoadPromtactu_transitionFromThePhaseNumber(String roadPromtactu_transitionFromThePhaseNumber) {
        this.roadPromtactu_fromPhaseId = roadPromtactu_transitionFromThePhaseNumber;
    }

    public String getRoadPromtactu_toPhaseNumber() {
        return roadPromtactu_toPhaseId;
    }

    public void setRoadPromtactu_toPhaseNumber(String roadPromtactu_toPhaseNumber) {
        this.roadPromtactu_toPhaseId = roadPromtactu_toPhaseNumber;
    }*/

    public String getRoadPromtactu_directionNumber() {
        return roadPromtactu_directionNumber;
    }

    public void setRoadPromtactu_directionNumber(String roadPromtactu_directionNumber) {
        this.roadPromtactu_directionNumber = roadPromtactu_directionNumber;
    }

    public String getRoadPromtactu_endGreenAddit() {
        return roadPromtactu_endGreenAddit;
    }

    public void setRoadPromtactu_endGreenAddit(String roadPromtactu_endGreenAddit) {
        this.roadPromtactu_endGreenAddit = roadPromtactu_endGreenAddit;
    }

    public String getRoadPromtactu_durationGreenBlink() {
        return roadPromtactu_durationGreenBlink;
    }

    public void setRoadPromtactu_durationGreenBlink(String roadPromtactu_durationGreenBlink) {
        this.roadPromtactu_durationGreenBlink = roadPromtactu_durationGreenBlink;
    }

    public String getRoadPromtactu_durationYellow() {
        return roadPromtactu_durationYellow;
    }

    public void setRoadPromtactu_durationYellow(String roadPromtactu_durationYellow) {
        this.roadPromtactu_durationYellow = roadPromtactu_durationYellow;
    }

    public String getRoadPromtactu_endRed() {
        return roadPromtactu_endRed;
    }

    public void setRoadPromtactu_endRed(String roadPromtactu_endRed) {
        this.roadPromtactu_endRed = roadPromtactu_endRed;
    }

    public String getRoadPromtactu_durationRedYellow() {
        return roadPromtactu_durationRedYellow;
    }

    public void setRoadPromtactu_durationRedYellow(String roadPromtactu_durationRedYellow) {
        this.roadPromtactu_durationRedYellow = roadPromtactu_durationRedYellow;
    }

}
