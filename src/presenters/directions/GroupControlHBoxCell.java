package presenters.directions;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class GroupControlHBoxCell extends HBox {

	Label groupNumber = new Label();
	Text numberOfControl = new Text();

	
	public GroupControlHBoxCell() {
		super();

		groupNumber.setText("Группа № ");
		groupNumber.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(groupNumber, Priority.ALWAYS);

		this.getChildren().addAll(groupNumber, numberOfControl);

	}

	public Text getNumberOfControl() {
		return numberOfControl;
	}

	public void setNumberOfControl(Text numberOfControl) {
		this.numberOfControl = numberOfControl;
	}

}
