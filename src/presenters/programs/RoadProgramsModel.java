package presenters.programs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vitaly on 05.12.2016.
 */
public class RoadProgramsModel {

    private List<RoadProgram> roadProgramList = new ArrayList<>();
    private List<ScheduleProgram> scheduleProgramList = new ArrayList<>();
    private List<PhaseInProgram> phaseInProgramList = new ArrayList<>();
    private Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram;
    private Map<RoadProgram, List<ScheduleProgram>> mapOfScheduleProgram;
    private Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> mapOfWeekCalendar;
    private Map<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> mapOfDateCalendar;
	private Map<String, List<SpeedSign>> mapOfProgramSpeedSign = new LinkedHashMap<>();
	private Map<RoadProgram, List<SwitchPhase>> mapOfSwichPhase = new LinkedHashMap<>();

	RoadProgram roadProgram;
    ScheduleProgram scheduleProgram;
    PhaseInProgram phaseInProgram;
    
    public RoadProgramsModel() {
        mapOfPhasesInProgram = new LinkedHashMap<>();
        mapOfScheduleProgram = new LinkedHashMap<>();
        roadProgram = new RoadProgram();
        scheduleProgram = new ScheduleProgram();
        phaseInProgram = new PhaseInProgram();
        mapOfWeekCalendar = new LinkedHashMap<>();
        mapOfDateCalendar = new LinkedHashMap<>();
    }

	public Map<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> getMapOfDateCalendar() {
		return mapOfDateCalendar;
	}

	public void setMapOfDateCalendar(Map<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> mapOfDateCalendar) {
		this.mapOfDateCalendar = mapOfDateCalendar;
	}
    public Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> getMapOfWeekCalendar() {
		return mapOfWeekCalendar;
	}

	public void setMapOfWeekCalendar(Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> mapOfWeekCalendar) {
		this.mapOfWeekCalendar = mapOfWeekCalendar;
	}

    public List<RoadProgram> getRoadProgramList() {
        return roadProgramList;
    }
    public void setRoadProgramList(List<RoadProgram> roadProgramList) {
        this.roadProgramList = roadProgramList;
    }

    public List<ScheduleProgram> getScheduleProgramList() {
        return scheduleProgramList;
    }
    public void setScheduleProgramList(List<ScheduleProgram> scheduleProgramList) {
        this.scheduleProgramList = scheduleProgramList;
    }

    public List<PhaseInProgram> getPhaseInProgramList() {return phaseInProgramList;}
    public void setPhaseInProgramList(List<PhaseInProgram> phaseInProgramList) {
        this.phaseInProgramList = phaseInProgramList;
    }

    public Map<RoadProgram, List<PhaseInProgram>> getMapOfPhasesInProgram() {
        return mapOfPhasesInProgram;
    }

    public void setMapOfPhasesInProgram(Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram) {
        this.mapOfPhasesInProgram = mapOfPhasesInProgram;
    }
    
    public Map<RoadProgram, List<ScheduleProgram>> getMapOfScheduleProgram() {
		return mapOfScheduleProgram;
	}
	public void setMapOfScheduleProgram(Map<RoadProgram, List<ScheduleProgram>> mapOfScheduleProgram) {
		this.mapOfScheduleProgram = mapOfScheduleProgram;
	}

	public Map<String, List<SpeedSign>> getMapOfProgramSpeedSign() {
		return mapOfProgramSpeedSign;
	}

	public void setMapOfProgramSpeedSign(Map<String, List<SpeedSign>> mapOfProgramSpeedSign) {
		this.mapOfProgramSpeedSign = mapOfProgramSpeedSign;
	}
	
	public Map<RoadProgram, List<SwitchPhase>> getMapOfSwichPhase() {
		return mapOfSwichPhase;
	}

	public void setMapOfSwichPhase(Map<RoadProgram, List<SwitchPhase>> mapOfSwichPhase) {
		this.mapOfSwichPhase = mapOfSwichPhase;
	}

}
