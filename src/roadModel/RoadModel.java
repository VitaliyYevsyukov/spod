package roadModel;

import presenters.conflicts.RoadConflictsModel;
import presenters.detector.RoadDetectorModel;
import presenters.directions.RoadDirectionsModel;
import presenters.object.RoadObjectModel;
import presenters.phase.RoadPhaseModel;
import presenters.programs.RoadProgramsModel;
import presenters.promtactu.RoadPromtactuModel;
import presenters.scheme.RoadSchemeModel;

/**
 * Created by Vitaly on 05.12.2016.
 */
public class RoadModel {

    private RoadObjectModel roadObjectModel;
    private RoadDirectionsModel roadDirectionsModel;
    private RoadPhaseModel roadPhaseModel;
    private RoadDetectorModel roadDetectorModel;
	private RoadProgramsModel roadProgramsModel;
    private RoadPromtactuModel roadPromtactuModel;
    private RoadConflictsModel roadConflictsModel;
    private RoadSchemeModel roadSchemeModel;
    private RoadModelSettings roadModelSettings;

    public RoadModelSettings getRoadModelSettings() {
        return roadModelSettings;
    }
    public void setRoadModelSettings(RoadModelSettings roadModelSettings) {
        this.roadModelSettings = roadModelSettings;
    }

    public RoadObjectModel getRoadObjectModel(){
        return roadObjectModel;
    }

    public RoadDirectionsModel getRoadDirectionModel(){
        return roadDirectionsModel;
    }

    public RoadPhaseModel getRoadPhaseModel(){
        return roadPhaseModel;
    }
    
    public RoadDetectorModel getRoadDetectorModel() {
		return roadDetectorModel;
	}

    public RoadProgramsModel getRoadProgramsModel() {
        return roadProgramsModel;
    }

    public RoadPromtactuModel getRoadPromtactuModel() {
        return roadPromtactuModel;
    }

    public RoadConflictsModel getRoadConflictsModel() {
        return roadConflictsModel;
    }

    public RoadSchemeModel getRoadSchemeModel(){
        return roadSchemeModel;
    }

    public RoadModel(){
        roadObjectModel = new RoadObjectModel();
        roadDirectionsModel = new RoadDirectionsModel();
        roadPhaseModel = new RoadPhaseModel();
        roadDetectorModel = new RoadDetectorModel();
        roadProgramsModel = new RoadProgramsModel();
        roadPromtactuModel = new RoadPromtactuModel();
        roadConflictsModel = new RoadConflictsModel();
        roadSchemeModel = new RoadSchemeModel();
        roadModelSettings = new RoadModelSettings();
    }


}
