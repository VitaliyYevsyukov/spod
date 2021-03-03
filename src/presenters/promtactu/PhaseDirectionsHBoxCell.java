package presenters.promtactu;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Created by Vitaly on 01.08.2017.
 */
public class PhaseDirectionsHBoxCell extends HBox {

    Label labelPhaseDirection = new Label();
    ComboBox<String> comboBoxChangeStateDirection = new ComboBox<>();
    ComboBox<String> comboBoxNotChangeStateDirection = new ComboBox<>();
	ObservableList<String> observableListChangeStateDirection;
    ObservableList<String> observableListNotChangeStateDirection;

    
    

	public PhaseDirectionsHBoxCell(){
        super();

        labelPhaseDirection.setText("Направление №:");
        
        HBox.setMargin(labelPhaseDirection, new Insets(3, 0, 0, 0));
        
        comboBoxChangeStateDirection.setPrefWidth(USE_COMPUTED_SIZE);
        comboBoxChangeStateDirection.setStyle("-fx-background-radius: 10; -fx-font-weight: bold");
        HBox.setMargin(comboBoxChangeStateDirection, new Insets(0, 0, 0, 15));
        comboBoxChangeStateDirection.setDisable(true);
        
        comboBoxNotChangeStateDirection.setPrefWidth(USE_COMPUTED_SIZE);
        comboBoxNotChangeStateDirection.setStyle("-fx-background-radius: 10; -fx-font-weight: bold");
        HBox.setMargin(comboBoxNotChangeStateDirection, new Insets(0, 0, 0, 40));
        comboBoxNotChangeStateDirection.setDisable(true);
        
        //labelPhaseDirection.setMaxWidth(Double.MAX_VALUE);
        //HBox.setHgrow(labelPhaseDirection, Priority.ALWAYS);

        

        this.getChildren().addAll(labelPhaseDirection, comboBoxChangeStateDirection, comboBoxNotChangeStateDirection);
    }

    public Label getLabelPhaseDirection() {
        return labelPhaseDirection;
    }

    public void setLabelPhaseDirection(Label labelPhaseDirection) {
        this.labelPhaseDirection = labelPhaseDirection;
    }

    public ComboBox<String> getComboBoxDirNumber() {
        return comboBoxChangeStateDirection;
    }

    public void setComboBoxDirNumber(ComboBox<String> comboBoxDirNumber) {
        this.comboBoxChangeStateDirection = comboBoxDirNumber;
    }

    public ObservableList<String> getObservableList() {
        return observableListChangeStateDirection;
    }

    public void setObservableList(ObservableList<String> observableList) {
        this.observableListChangeStateDirection = observableList;
        comboBoxChangeStateDirection.getItems().addAll(observableList);
    }
    public ComboBox<String> getComboBoxNotChangeStateDirection() {
		return comboBoxNotChangeStateDirection;
	}

	public void setComboBoxNotChangeStateDirection(ComboBox<String> comboBoxNotChangeStateDirection) {
		this.comboBoxNotChangeStateDirection = comboBoxNotChangeStateDirection;
	}
	public ObservableList<String> getObservableListNotChangeStateDirection() {
		return observableListNotChangeStateDirection;
	}

	public void setObservableListNotChangeStateDirection(ObservableList<String> observableListNotChangeStateDirection) {
		this.observableListNotChangeStateDirection = observableListNotChangeStateDirection;
		comboBoxNotChangeStateDirection.getItems().addAll(observableListNotChangeStateDirection);
	}

}
