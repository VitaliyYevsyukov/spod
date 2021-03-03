package presenters.detector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class DetectorZonesHBoxCell extends HBox{
	
	private Label labelZoneNumber = new Label();
	private ChoiceBox<String> choiceBoxTypeOfZone = new ChoiceBox<>();
	private ObservableList<String> observableListType;
	
	public DetectorZonesHBoxCell(String zoneNumber) {
		super();
		
		labelZoneNumber.setText(zoneNumber);
		labelZoneNumber.setMaxWidth(Double.MAX_VALUE);
		
		HBox.setHgrow(labelZoneNumber, Priority.ALWAYS);
		HBox.setMargin(labelZoneNumber, new Insets(3, 0, 0, 30));
		
		choiceBoxTypeOfZone.setPrefWidth(150);
		choiceBoxTypeOfZone.setDisable(true);
		observableListType = FXCollections.observableArrayList("Присутствие", "Остановка");
		choiceBoxTypeOfZone.setItems(observableListType);
		
		this.getChildren().addAll(labelZoneNumber, choiceBoxTypeOfZone);
		
	}
	
	
	public Label getLabelZoneNumber() {
		return labelZoneNumber;
	}

	public void setLabelZoneNumber(Label labelZoneNumber) {
		this.labelZoneNumber = labelZoneNumber;
	}

	public ChoiceBox<String> getChoiceBoxTypeOfZone() {
		return choiceBoxTypeOfZone;
	}

	public void setChoiceBoxTypeOfZone(ChoiceBox<String> choiceBoxTypeOfZone) {
		this.choiceBoxTypeOfZone = choiceBoxTypeOfZone;
	}

	public ObservableList<String> getObservableListType() {
		return observableListType;
	}

	public void setObservableListType(ObservableList<String> observableListType) {
		this.observableListType = observableListType;
	}
	

}
