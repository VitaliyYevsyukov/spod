package presenters.directions;

import presenters.promtactu.PromtactData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vitaly on 05.12.2016.
 */
public class RoadDirectionsModel {

    private List<RoadDirection> roadDirectionList = new ArrayList<>();
    private Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> groupControlHBoxCellListMap;    
	private Map<String, PromtactData> mapOfBasePromtact;
    private Map<String, PromtactData> mapOfSpecificPromtact;
    RoadDirection roadDirection;
    GroupControl groupControl;
    

    public RoadDirectionsModel(){
        roadDirection = new RoadDirection();
        groupControl = new GroupControl();
        mapOfBasePromtact = new LinkedHashMap<>();
        mapOfSpecificPromtact = new LinkedHashMap<>();
        groupControlHBoxCellListMap = new LinkedHashMap<>();
    }

    public List<RoadDirection> getRoadDirectionList() {
        return roadDirectionList;
    }
    public void setRoadDirectionList(List<RoadDirection> roadDirectionList) {
        this.roadDirectionList = roadDirectionList;
    }

    public Map<String, PromtactData> getMapOfBasePromtact() {
        return mapOfBasePromtact;
    }

    public void setMapOfBasePromtact(Map<String, PromtactData> mapOfBasePromtact) {
        this.mapOfBasePromtact = mapOfBasePromtact;
    }

    public Map<String, PromtactData> getMapOfSpecificPromtact() {
        return mapOfSpecificPromtact;
    }

    public void setMapOfSpecificPromtact(Map<String, PromtactData> mapOfSpecificPromtact) {
        this.mapOfSpecificPromtact = mapOfSpecificPromtact;
    }
    
    public Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> getGroupControlHBoxCellListMap() {
		return groupControlHBoxCellListMap;
	}

	public void setGroupControlHBoxCellListMap(
			Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> groupControlHBoxCellListMap) {
		this.groupControlHBoxCellListMap = groupControlHBoxCellListMap;
	}

}
