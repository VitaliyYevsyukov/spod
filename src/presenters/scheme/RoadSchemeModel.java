package presenters.scheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitaly on 05.12.2016.
 */
public class RoadSchemeModel {

    private List<RoadArrow> roadArrowList = new ArrayList<>();
    private String roadSchemeTypeJunction;
    //private String roadSchemeArrowType;
    private String roadScheme_directionNumber;

    RoadArrow roadArrow;

    public RoadSchemeModel(){
        roadSchemeTypeJunction = new String();
        roadArrow = new RoadArrow();
    }

    public List<RoadArrow> getRoadArrowList() {
        return roadArrowList;
    }
    public void setRoadArrowList(List<RoadArrow> roadArrowList) {
        this.roadArrowList = roadArrowList;
    }

    public String getRoadSchemeTypeJunction() {
        return roadSchemeTypeJunction;
    }
    public void setRoadSchemeTypeJunction(String roadSchemeTypeJunction) {
        this.roadSchemeTypeJunction = roadSchemeTypeJunction;
    }

    /*public String getRoadSchemeArrowType() {
        return roadSchemeArrowType;
    }
    public void setRoadSchemeArrowType(String roadSchemeArrowType) {
        this.roadSchemeArrowType = roadSchemeArrowType;
    }*/

    public String getRoadScheme_directionNumber() {
        return roadScheme_directionNumber;
    }
    public void setRoadScheme_directionNumber(String roadScheme_directionNumber) {
        this.roadScheme_directionNumber = roadScheme_directionNumber;
    }
}
