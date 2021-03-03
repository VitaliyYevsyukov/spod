package presenters.conflicts;

/**
 * Created by Vitaly on 16.06.2017.
 */
public class ConflictForDirection {

    private String roadConflict_directionNumber;

    public ConflictForDirection(){
        roadConflict_directionNumber = new String();
    }

    public String getRoadConflict_directionNumber() {
        return roadConflict_directionNumber;
    }
    public void setRoadConflict_directionNumber(String roadConflict_directionNumber) {
        this.roadConflict_directionNumber = roadConflict_directionNumber;
    }
    
}
