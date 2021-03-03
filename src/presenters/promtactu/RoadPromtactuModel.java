package presenters.promtactu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vitaly on 05.12.2016.
 */
public class RoadPromtactuModel {

    private List<PromtactData> promtactDataList = new ArrayList<>();
    private Map<String, List<String>> interPhaseMap;
    private Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfInterphaseSpecificPromtact;
    PromtactData promtactData;

    
    public RoadPromtactuModel(){
        promtactData = new PromtactData();
        interPhaseMap = new LinkedHashMap<>();
        mapOfInterphaseSpecificPromtact = new LinkedHashMap<>();
    }
    

    public List<PromtactData> getPromtactDataList() {
        return promtactDataList;
    }
    public void setPromtactDataList(List<PromtactData> promtactDataList) {
        this.promtactDataList = promtactDataList;
    }

    public Map<String, List<String>> getInterPhaseMap() {
        return interPhaseMap;
    }
    public void setInterPhaseMap(Map<String, List<String>> interPhaseMap) {
        this.interPhaseMap = interPhaseMap;
    }

    public Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> getMapOfInterphaseSpecificPromtact() {
        return mapOfInterphaseSpecificPromtact;
    }

    public void setMapOfInterphaseSpecificPromtact(Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfInterphaseSpecificPromtact) {
        this.mapOfInterphaseSpecificPromtact = mapOfInterphaseSpecificPromtact;
    }

}
