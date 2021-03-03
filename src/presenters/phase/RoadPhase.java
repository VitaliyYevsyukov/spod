package presenters.phase;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Vitaly on 17.01.2017.
 */
public class RoadPhase {

	private String IdPhase;
	private SimpleStringProperty roadPhase_number;
    private SimpleStringProperty roadPhase_Tmin;
    private SimpleObjectProperty<TVP> roadPhase_phaseTVP;
    private SimpleStringProperty roadPhase_panelTVP_1;
    private SimpleStringProperty roadPhase_panelTVP_2;

    public RoadPhase(){
    	IdPhase = new String();
        roadPhase_number = new SimpleStringProperty();
        roadPhase_Tmin = new SimpleStringProperty();
        roadPhase_phaseTVP = new SimpleObjectProperty<>();
        roadPhase_panelTVP_1 = new SimpleStringProperty();
        roadPhase_panelTVP_2 = new SimpleStringProperty();
    }
    

    public String getRoadPhase_number() {
        return roadPhase_number.get();
    }
    public void setRoadPhase_number(String roadPhase_number) {
        this.roadPhase_number.set(roadPhase_number);
    }
    public StringProperty roadPhaseNumberProperty(){
        return this.roadPhase_number;
    }


    public String getRoadPhase_Tmin() {
        return roadPhase_Tmin.get();
    }
    public void setRoadPhase_Tmin(String roadPhase_Tmin) {
        this.roadPhase_Tmin.set(roadPhase_Tmin);
    }
    public StringProperty roadPhaseTminProperty(){
        return this.roadPhase_Tmin;
    }


    public TVP getRoadPhase_phaseTVP() {
        return roadPhase_phaseTVP.get();
    }
    public void setRoadPhase_phaseTVP(TVP roadPhase_phaseTVP) {
        this.roadPhase_phaseTVP.set(roadPhase_phaseTVP);
    }
    public ObjectProperty<TVP> roadPhasePhaseTVPProperty(){
        return this.roadPhase_phaseTVP;
    }


    public String getRoadPhase_panelTVP_1() {
        return roadPhase_panelTVP_1.get();
    }
    public void setRoadPhase_panelTVP_1(String roadPhase_panelTVP_1) {
        this.roadPhase_panelTVP_1.set(roadPhase_panelTVP_1);
    }
    public StringProperty roadPhasePanel_1_Property(){
        return this.roadPhase_panelTVP_1;
    }


    public String getRoadPhase_panelTVP_2() {
        return roadPhase_panelTVP_2.get();
    }
    public void setRoadPhase_panelTVP_2(String roadPhase_panelTVP_2) {
        this.roadPhase_panelTVP_2.set(roadPhase_panelTVP_2);
    }
    public StringProperty roadPhasePanel_2_Property(){
        return this.roadPhase_panelTVP_2;
    }
    
    public String getIdPhase() {
		return IdPhase;
	}

	public void setIdPhase(String idPhase) {
		IdPhase = idPhase;
	}

}
