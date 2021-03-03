package presenters.detector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class RoadDetectorHBoxCell extends HBox {
	
	private Label labelNumberOfDetector = new Label();
	private ChoiceBox<String> choiceBoxTypeOfDetector = new ChoiceBox<>();
	private ObservableList<String> observableListTypeOfDetector;

	public RoadDetectorHBoxCell(String number) {
		super();
		
		labelNumberOfDetector.setText(number);
		labelNumberOfDetector.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(labelNumberOfDetector, Priority.ALWAYS);
		HBox.setMargin(labelNumberOfDetector, new Insets(3, 0, 0, 30));
		
		choiceBoxTypeOfDetector.setPrefWidth(150);
		choiceBoxTypeOfDetector.setDisable(true);
		
		observableListTypeOfDetector = FXCollections.observableArrayList("Кнопка ТВП", "Ультразвук", "Камера", "Радар");
		choiceBoxTypeOfDetector.getItems().addAll(observableListTypeOfDetector);
		
		this.getChildren().addAll(labelNumberOfDetector, choiceBoxTypeOfDetector);
	}
	
	public Label getLabelNumberOfDetector() {
		return labelNumberOfDetector;
	}

	public void setLabelNumberOfDetector(Label labelNumberOfDetector) {
		this.labelNumberOfDetector = labelNumberOfDetector;
	}

	public ChoiceBox<String> getChoiceBoxTypeOfDetector() {
		return choiceBoxTypeOfDetector;
	}

	public void setChoiceBoxTypeOfDetector(ChoiceBox<String> choiceBoxTypeOfDetector) {
		this.choiceBoxTypeOfDetector = choiceBoxTypeOfDetector;
	}

	public ObservableList<String> getObservableListTypeOfDetector() {
		return observableListTypeOfDetector;
	}

	public void setObservableListTypeOfDetector(ObservableList<String> observableListTypeOfDetector) {
		this.observableListTypeOfDetector = observableListTypeOfDetector;
		choiceBoxTypeOfDetector.getItems().addAll(observableListTypeOfDetector);
	}

}
