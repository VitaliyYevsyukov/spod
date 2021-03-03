package presenters.programs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Vitaly on 23.01.2017.
 */
public class PhaseInProgram {

    private SimpleStringProperty duration;
    private SimpleObjectProperty<PhaseNumber> phaseInProgramNumber;
    private String phaseIndex;
    private String phaseTmain;

	public PhaseInProgram() {
        duration = new SimpleStringProperty();
        phaseInProgramNumber = new SimpleObjectProperty<>();
        phaseIndex = new String();
        phaseTmain = new String();
    }
	

    public PhaseNumber getPhaseInProgramNumber() {
        return phaseInProgramNumber.get();
    }
    public void setPhaseInProgramNumber(PhaseNumber phaseInProgramNumber) {
        this.phaseInProgramNumber.set(phaseInProgramNumber);
    }
    public ObjectProperty<PhaseNumber> phaseInProgramProperty() {
        return this.phaseInProgramNumber;
    }

    public String getDurationPhaseInProgram() {
        return duration.get();
    }
    public void setDurationPhaseInProgram(String duration) {
        this.duration.set(duration);
    }
    public StringProperty durationPhaseInProgramProperty() {
        return this.duration;
    }

    public String getPhaseIndex() {
		return phaseIndex;
	}

	public void setPhaseIndex(String phaseIndex) {
		this.phaseIndex = phaseIndex;
	}
	
	public String getPhaseTmain() {
		return phaseTmain;
	}


	public void setPhaseTmain(String phaseTmain) {
		this.phaseTmain = phaseTmain;
	}


}
