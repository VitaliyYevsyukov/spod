package presenters.programs;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Vitaly on 23.01.2017.
 */
public class ScheduleProgram {

    private SimpleObjectProperty<ScheduleNumber> numberOfScheduleProgram;
    private SimpleStringProperty timeONOfScheduleProgram;
    private SimpleStringProperty displacementTimeOfScheduleProgram;
    private String scheduleProgramId;

	public ScheduleProgram() {
        this.numberOfScheduleProgram = new SimpleObjectProperty<>();
        this.timeONOfScheduleProgram = new SimpleStringProperty();
        this.displacementTimeOfScheduleProgram = new SimpleStringProperty();
        scheduleProgramId = new String();
    }
	

    public ScheduleNumber getNumberOfScheduleProgram() {
        return numberOfScheduleProgram.get();
    }
    public void setNumberOfScheduleProgram(ScheduleNumber numberOfScheduleProgram) {
        this.numberOfScheduleProgram.set(numberOfScheduleProgram);
    }
    public ObjectProperty<ScheduleNumber> numberOfScheduleProgramProperty() {
        return this.numberOfScheduleProgram;
    }

    public String getTimeONOfScheduleProgram () {
        return timeONOfScheduleProgram.get();
    }
    public void setTimeONOfScheduleProgram (String timeONOfScheduleProgram) {
        this.timeONOfScheduleProgram.set(timeONOfScheduleProgram);
    }
    public StringProperty timeONOfScheduleProgramProperty() {
        return this.timeONOfScheduleProgram;
    }

    public String getDisplacementTimeOfScheduleProgram () {
        return displacementTimeOfScheduleProgram.get();
    }
    public void setDisplacementTimeOfScheduleProgram (String displacementTimeOfScheduleProgram) {
        this.displacementTimeOfScheduleProgram.set(displacementTimeOfScheduleProgram);
    }
    public StringProperty displacementTimeOfScheduleProgramProperty() {
        return this.displacementTimeOfScheduleProgram;
    }
    

    public String getScheduleProgramId() {
		return scheduleProgramId;
	}

	public void setScheduleProgramId(String sheduleProgramId) {
		this.scheduleProgramId = sheduleProgramId;
	}

}
