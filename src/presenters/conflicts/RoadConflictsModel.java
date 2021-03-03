package presenters.conflicts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vitaly on 05.12.2016.
 */
public class RoadConflictsModel {

    private List<ConflictForDirection> conflictForDirectionList = new ArrayList<>();
    private List<ConflictWithDirection> conflictWithDirectionList = new ArrayList<>();
    private Map<String, List<ConflictWithDirection>> mapOfConflict;

    ConflictForDirection conflictForDirection;
    ConflictWithDirection conflictWithDirection;

    public RoadConflictsModel(){
        conflictForDirection = new ConflictForDirection();
        conflictWithDirection = new ConflictWithDirection();
        mapOfConflict = new HashMap<>();
    }

    public List<ConflictForDirection> getConflictForDirectionList() {
        return conflictForDirectionList;
    }
    public void setConflictForDirectionList(List<ConflictForDirection> conflictForDirectionList) {
        this.conflictForDirectionList = conflictForDirectionList;
    }

    public List<ConflictWithDirection> getConflictWithDirectionList() {
        return conflictWithDirectionList;
    }
    public void setConflictWithDirectionList(List<ConflictWithDirection> conflictWithDirectionList) {
        this.conflictWithDirectionList = conflictWithDirectionList;
    }

    public Map<String, List<ConflictWithDirection>> getMapOfConflict() {
        return mapOfConflict;
    }
    public void setMapOfConflict(Map<String, List<ConflictWithDirection>> mapOfConflict) {
        this.mapOfConflict = mapOfConflict;
    }
    
}
