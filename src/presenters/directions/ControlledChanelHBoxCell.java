package presenters.directions;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ControlledChanelHBoxCell extends HBox {

	Label direction = new Label("Направление ");
	Label channal = new Label("канал ");
	ComboBox<String> comboBoxDirection = new ComboBox<>();
	TextField textFieldChanel = new TextField();
	private ObservableList<String> observableListComboBoxDirection;

	
	public ControlledChanelHBoxCell() {
		super();

		direction.setMaxWidth(Double.MAX_VALUE);
		channal.setMaxWidth(Double.MAX_VALUE);

		comboBoxDirection.setPrefWidth(USE_COMPUTED_SIZE);
		comboBoxDirection.setStyle("-fx-background-radius: 10");
		HBox.setMargin(comboBoxDirection, new Insets(0, 5, 0, 5));

		textFieldChanel.setPrefWidth(50);
		textFieldChanel.setAlignment(Pos.CENTER);
		textFieldChanel.setStyle("-fx-text-fill: #f11e17; -fx-font-weight: bold; -fx-background-radius: 10");
		HBox.setMargin(textFieldChanel, new Insets(0, 0, 0, 5));
		textFieldChanel.setEditable(false);

		this.getChildren().addAll(direction, comboBoxDirection, new Text(": "), channal, textFieldChanel);
	}

	public ObservableList<String> getObservableListComboBoxDirection() {
		return observableListComboBoxDirection;
	}

	public void setObservableListComboBoxDirection(ObservableList<String> observableListComboBoxDirection) {
		this.observableListComboBoxDirection = observableListComboBoxDirection;
		comboBoxDirection.getItems().addAll(observableListComboBoxDirection);
	}

	public TextField getTextFieldChanel() {
		return textFieldChanel;
	}

	public void setTextFieldChanel(TextField textFieldChanel) {
		this.textFieldChanel = textFieldChanel;
	}
	
	public ComboBox<String> getComboBoxDirection() {
		return comboBoxDirection;
	}

	public void setComboBoxDirection(ComboBox<String> comboBoxDirection) {
		this.comboBoxDirection = comboBoxDirection;
	}

}
