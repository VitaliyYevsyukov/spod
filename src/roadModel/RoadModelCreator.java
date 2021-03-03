package roadModel;

import controllers.Employee;

/**
 * Created by Vitaly on 16.02.2017.
 */
public class RoadModelCreator {

    RoadModel roadModel;

    public RoadModelCreator(){

    }
    
    public RoadModel createXMLRoadModel(RoadModel model) {
    	roadModel = new RoadModel();
    	return roadModel;
    }

    public RoadModel createRoadModel(Employee employee){
        roadModel = new RoadModel();
        roadModel.getRoadObjectModel().setRoadObjectName(employee.getObjectName());
        return roadModel;
    }
    
    public RoadModel createFBRoadModel(String objectFB) {
    	roadModel = new RoadModel();
    	roadModel.getRoadObjectModel().setRoadObjectName(objectFB);
    	return roadModel;
    }

}
