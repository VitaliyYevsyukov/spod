package presenters.programs;

import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ScheduleCalendarDateHBoxCell extends Pane{
	
	DatePicker datePicker = new DatePicker();
	ChoiceBox<String> choiceBox = new ChoiceBox<String>();
	TextField textFild = new TextField();

	ObservableList<String> observableListCopyScheduleOfCurrentDay = FXCollections.observableArrayList("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье");

	public ScheduleCalendarDateHBoxCell() {
		super();
		
		datePicker.setPrefWidth(0);
		datePicker.setEditable(false);
		datePicker.setDisable(true);
		datePicker.setLayoutX(50);
		
		textFild.setPrefWidth(75);
		textFild.setStyle("-fx-background-radius: 10");
		textFild.setLayoutX(0);
		textFild.setEditable(false);
		
		choiceBox.setPrefWidth(110);
		choiceBox.setLayoutX(110);
		choiceBox.setStyle("-fx-background-radius: 10");
		choiceBox.setItems(observableListCopyScheduleOfCurrentDay);
				
		/*textFild.setPrefWidth(145);
		HBox.setMargin(textFild, new Insets(0, 0, 0, 0));
		
		HBox.setMargin(datePicker, new Insets(0, 50, 0, 0));
		
		choiceBox.setPrefWidth(145);
		HBox.setMargin(choiceBox, new Insets(0, 0, 0, 30));
		choiceBox.setItems(observableListCopyScheduleOfCurrentDay);*/
		
		this.getChildren().addAll(datePicker,textFild, choiceBox);
	}

	public DatePicker getDatePicker() {
		return datePicker;
	}

	public void setDatePicker(DatePicker datePicker) {
		this.datePicker = datePicker;
	}
	public ChoiceBox<String> getChoiceBox() {
		return choiceBox;
	}

	public void setChoiceBox(ChoiceBox<String> choiceBox) {
		this.choiceBox = choiceBox;
	}
	public TextField getTextFild() {
		return textFild;
	}

	public void setTextFild(TextField textFild) {
		this.textFild = textFild;
	}
}
