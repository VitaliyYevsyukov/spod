package presenters.programs;

public class SpeedSign {

	private String idSpeedSign;
	private String numberSpeedSign;
	private String recomendSpeed;
	
	public SpeedSign() {
		idSpeedSign = new String();
		numberSpeedSign = new String();
		recomendSpeed = new String();
	}
	
	
	public SpeedSign(String id, String number, String recomendSpeed) {
		this.idSpeedSign = id;
		this.numberSpeedSign = number;
		this.recomendSpeed = recomendSpeed;
	}
	
	public String getIdSpeedSign() {
		return idSpeedSign;
	}

	public void setIdSpeedSign(String idSpeedSign) {
		this.idSpeedSign = idSpeedSign;
	}

	public String getNumberSpeedSign() {
		return numberSpeedSign;
	}

	public void setNumberSpeedSign(String numberSpeedSign) {
		this.numberSpeedSign = numberSpeedSign;
	}

	public String getRecomendSpeed() {
		return recomendSpeed;
	}

	public void setRecomendSpeed(String recomendSpeed) {
		this.recomendSpeed = recomendSpeed;
	}
	
}
