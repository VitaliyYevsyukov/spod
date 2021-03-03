package presenters.programs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SwitchPhase {

	private SimpleObjectProperty<PhaseNumber> phase;
	private SimpleObjectProperty<PhaseNumber> toPhase1;
	private SimpleObjectProperty<PhaseNumber> toPhase2;
	private SimpleObjectProperty<SwitchPhaseMode> mode;
	private SimpleStringProperty mainTime;
	private SimpleStringProperty promtact;
	private SimpleStringProperty durationPhase;
	
	public SwitchPhase() {
		phase = new SimpleObjectProperty<>();
		toPhase1 = new SimpleObjectProperty<>();
		toPhase2 = new SimpleObjectProperty<>();
		mode = new SimpleObjectProperty<>();
		mainTime = new SimpleStringProperty();
		promtact = new SimpleStringProperty();
		durationPhase = new SimpleStringProperty();
	}
	
	public SwitchPhase(SimpleObjectProperty<PhaseNumber> phase, SimpleObjectProperty<PhaseNumber> toPhase1,
			SimpleObjectProperty<PhaseNumber> toPhase2, SimpleStringProperty mainTime, SimpleStringProperty promtact, SimpleStringProperty durationPhase) {
		super();
		this.phase = phase;
		this.toPhase1 = toPhase1;
		this.toPhase2 = toPhase2;
		this.mainTime = mainTime;
		this.promtact = promtact;
		this.durationPhase = durationPhase;
	}
	
	public PhaseNumber getPhase() {
		return phase.get();
	}
	public void setPhase(PhaseNumber phase) {
		this.phase.set(phase);;
	}
	public ObjectProperty<PhaseNumber> phaseNumberProperty() {
        return this.phase;
    }
	

	public PhaseNumber getToPhase1() {
		return toPhase1.get();
	}
	public void setToPhase1(PhaseNumber toPhase) {
		this.toPhase1.set(toPhase);
	}
	public ObjectProperty<PhaseNumber> toPhase1NumberProperty() {
        return this.toPhase1;
    }
	
	
	public PhaseNumber getToPhase2() {
		return toPhase2.get();
	}
	public void setToPhase2(PhaseNumber toPhase) {
		this.toPhase2.set(toPhase);
	}
	public ObjectProperty<PhaseNumber> toPhase2NumberProperty() {
        return this.toPhase2;
    }
	

	public String getMainTime() {
		return mainTime.get();
	}
	public void setMainTime(String mainTime) {
		this.mainTime.set(mainTime);
	}
	public StringProperty mainTimeProperty() {
        return this.mainTime;
    }

	
	public String getPromtact() {
		return promtact.get();
	}
	public void setPromtact(String promtact) {
		this.promtact.set(promtact);
	}
	public StringProperty promtactProperty() {
        return this.promtact;
    }
	

	public String getDurationPhase() {
		return durationPhase.get();
	}
	public void setDurationPhase(String durationPhase) {
		this.durationPhase.set(durationPhase);
	}
	public StringProperty durationPhaseProperty() {
        return this.durationPhase;
    }
	
	
	public SwitchPhaseMode getSwitchPhaseMode() {
		return mode.get();
	}
	public void setSwitchPhaseMode(SwitchPhaseMode mode) {
		this.mode.set(mode);
	}
	public ObjectProperty<SwitchPhaseMode> switchPhaseProperty(){
		return this.mode;
	}
	/*public ProgramMode getRoadProgram_programMode() {
        return roadProgram_programMode.get();
    }
    public void setRoadProgram_programMode(ProgramMode roadProgram_programMode) {
        this.roadProgram_programMode.set(roadProgram_programMode);
    }
    public ObjectProperty<ProgramMode> roadProgramProgramModeProperty(){
        return this.roadProgram_programMode;
    }*/
	

}
