package presenters.programs;

import java.awt.Label;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ScheduleCalendarHBoxCell extends HBox{
	
	CheckBox checkBox = new CheckBox();
	Text weekDay = new Text();

	ScheduleCalendarHBoxCell(CheckBox checkBoxDay, String day){
		super();
		
		weekDay.setText(day);
		this.checkBox = checkBoxDay;
		
		checkBoxDay.setDisable(true);
		
		this.getChildren().addAll(checkBoxDay, weekDay);
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}
	
	public Text getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(Text weekDay) {
		this.weekDay = weekDay;
	}
}
