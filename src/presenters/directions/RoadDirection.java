package presenters.directions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Vitaly on 17.01.2017.
 */
public class RoadDirection {

	private String IdDirection;
	private String numberOfTypeDirection;
	private SimpleStringProperty roadDirections_number;
    private SimpleObjectProperty<TypDirection> roadDirections_typeOfDirection;
    private SimpleStringProperty roadDirections_chanal_1;
    private SimpleStringProperty roadDirections_chanal_2;
    private SimpleStringProperty roadDirections_chanal_3;
    private SimpleStringProperty roadDirections_chanal_4;
    private SimpleStringProperty roadDirections_control_1;
    private SimpleStringProperty roadDirections_control_2;

    
    public RoadDirection(){
    	IdDirection = new String();
    	numberOfTypeDirection = new String();
        roadDirections_number = new SimpleStringProperty();
        roadDirections_typeOfDirection = new SimpleObjectProperty<>();
        roadDirections_chanal_1 = new SimpleStringProperty();
        roadDirections_chanal_2 = new SimpleStringProperty();
        roadDirections_chanal_3 = new SimpleStringProperty();
        roadDirections_chanal_4 = new SimpleStringProperty();
        roadDirections_control_1 = new SimpleStringProperty();
        roadDirections_control_2 = new SimpleStringProperty();
    }

    public String getRoadDirections_number() {
        return roadDirections_number.get();
    }
    public void setRoadDirections_number(String roadDirections_number) {
        this.roadDirections_number.set(roadDirections_number);
    }
    public StringProperty roadDirectionsNumberProperty(){
        return this.roadDirections_number;
    }
    
    
    public String getNumberOfTypeDirection() {
		return numberOfTypeDirection;
	}

	public void setNumberOfTypeDirection(String numberOfTypeDirection) {
		this.numberOfTypeDirection = numberOfTypeDirection;
	}


    public TypDirection getRoadDirections_typeOfDirection() {
        return roadDirections_typeOfDirection.get();
    }
    public void setRoadDirections_typeOfDirection(TypDirection roadDirections_typeOfDirection) {
        this.roadDirections_typeOfDirection.set(roadDirections_typeOfDirection);
    }
    public ObjectProperty<TypDirection> roadDirectionsTypeOfDirectionProperty(){
        return this.roadDirections_typeOfDirection;
    }


    public String getRoadDirections_chanal_1() {
        return roadDirections_chanal_1.get();
    }
    public void setRoadDirections_chanal_1(String roadDirections_chanal_1) {
        this.roadDirections_chanal_1.set(roadDirections_chanal_1);
    }
    public StringProperty roadDirectionsChanal_1_Property(){
        return this.roadDirections_chanal_1;
    }


    public String getRoadDirections_chanal_2() {
        return roadDirections_chanal_2.get();
    }
    public void setRoadDirections_chanal_2(String roadDirections_chanal_2) {
        this.roadDirections_chanal_2.set(roadDirections_chanal_2);
    }
    public StringProperty roadDirectionsChanal_2_Property(){
        return this.roadDirections_chanal_2;
    }


    public String getRoadDirections_chanal_3() {
        return roadDirections_chanal_3.get();
    }
    public void setRoadDirections_chanal_3(String roadDirections_chanal_3) {
        this.roadDirections_chanal_3.set(roadDirections_chanal_3);
    }
    public StringProperty roadDirectionsChanal_3_Property(){
        return this.roadDirections_chanal_3;
    }


    public String getRoadDirections_chanal_4() {
        return roadDirections_chanal_4.get();
    }
    public void setRoadDirections_chanal_4(String roadDirections_chanal_4) {
        this.roadDirections_chanal_4.set(roadDirections_chanal_4);
    }
    public StringProperty roadDirectionsChanal_4_Property(){
        return this.roadDirections_chanal_4;
    }


    public String getRoadDirections_control_1() {
        return roadDirections_control_1.get();
    }
    public void setRoadDirections_control_1(String roadDirections_control_1) {
        this.roadDirections_control_1.set(roadDirections_control_1);
    }
    public StringProperty roadDirectionsControl_1_Property(){
        return this.roadDirections_control_1;
    }


    public String getRoadDirections_control_2() {
        return roadDirections_control_2.get();
    }
    public void setRoadDirections_control_2(String roadDirections_control_2) {
        this.roadDirections_control_2.set(roadDirections_control_2);
    }
    public StringProperty roadDirectionsControl_2_Property(){
        return this.roadDirections_control_2;
    }
    
    public String getIdDirection() {
		return IdDirection;
	}

	public void setIdDirection(String idDirection) {
		IdDirection = idDirection;
	}

}
