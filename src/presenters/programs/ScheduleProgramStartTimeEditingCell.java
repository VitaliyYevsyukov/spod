package presenters.programs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

/**
 * Created by Vitaly on 23.01.2017.
 */
public class ScheduleProgramStartTimeEditingCell extends TableCell<ScheduleProgram, String> {

	private TextField textField;

	public ScheduleProgramStartTimeEditingCell() {
	}
	

	@Override
	public void startEdit() {
		if (!isEmpty()) {
			super.startEdit();
			createTextField();
			textField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
						textField.setText(newValue.replaceAll("[a-zA-Z ]+(:[ a-zA-Z ]+)*", ""));
						System.out.println(newValue);
					}
				}
			});
			setText("00:00");
			setGraphic(textField);
			textField.selectAll();
		}
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();

		setText((String) getItem());
		setGraphic(null);
	}

	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(item);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getString());
					// setGraphic(null);
				}
				setText(null);
				setGraphic(textField);
			} else {
				setText(getString());
				setGraphic(null);
			}
		}
	}

	private void createTextField() {
		textField = new TextField("00:00");
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.setOnAction((e) -> commitEdit(textField.getText()));
		textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (!newValue) {
				System.out.println("Commiting " + textField.getText());
				commitEdit(textField.getText());
				String str = textField.getText();
				if (!str.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Ошибка");
					alert.setHeaderText("Формат должен иметь следующий вид: ЧЧ:ММ");
					
					Stage stage = new Stage();
					stage = (Stage)alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
					
					alert.show();
					setText("00:00");
					// setGraphic(textField);
					textField.setStyle("-fx-control-inner-background: red;");
				}
			}
		});
	}

	private String getString() {
		return getItem() == null ? "" : getItem();
	}

}
