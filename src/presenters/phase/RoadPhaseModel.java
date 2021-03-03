package presenters.phase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vitaly on 05.12.2016.
 */
public class RoadPhaseModel {

    private List<RoadPhase> roadPhaseList = new ArrayList<>();
    private Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOpenDirectionInPhase;
    private Map<String, String> mapPhaseTmin;
    RoadPhase roadPhase;

    public RoadPhaseModel(){
        roadPhase = new RoadPhase();
        mapOpenDirectionInPhase = new LinkedHashMap<>();
        mapPhaseTmin = new LinkedHashMap<>();
    }
    

    public List<RoadPhase> getRoadPhaseList() {
        return roadPhaseList;
    }
    public void setRoadPhaseList(List<RoadPhase> roadPhaseList){
        this.roadPhaseList = roadPhaseList;
    }


    public Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> getMapOpenDirectionInPhase() {
        return mapOpenDirectionInPhase;
    }

    public void setMapOpenDirectionInPhase(Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOpenDirectionInPhase) {
        this.mapOpenDirectionInPhase = mapOpenDirectionInPhase;
    }

    public Map<String, String> getMapPhaseTmin() {
        return mapPhaseTmin;
    }

    public void setMapPhaseTmin(Map<String, String> mapPhaseTmin) {
        this.mapPhaseTmin = mapPhaseTmin;
    }

}
