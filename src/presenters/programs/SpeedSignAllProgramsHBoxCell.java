package presenters.programs;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SpeedSignAllProgramsHBoxCell extends HBox {
	
	private Label program = new Label();
	private Label number = new Label();

	SpeedSignAllProgramsHBoxCell(String program, String number){
		super();
		
		this.program.setText(program);
		this.program.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(this.program, Priority.ALWAYS);
		
		this.number.setText(number);
		
		this.getChildren().addAll(this.program, this.number);
		
	}
	
	
	
	public Label getNumber() {
		return number;
	}

	public void setNumber(Label number) {
		this.number = number;
	}
}
