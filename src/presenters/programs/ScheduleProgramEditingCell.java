package presenters.programs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

/**
 * Created by Vitaly on 23.01.2017.
 */
public class ScheduleProgramEditingCell extends TableCell<ScheduleProgram, String> {

	private TextField textField;

	public ScheduleProgramEditingCell() {
	}
	

	@Override
	public void startEdit() {
		if (!isEmpty()) {
			super.startEdit();
			createTextField();
			textField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
						textField.setText(newValue.replaceAll("[a-z]+(:[a-z]+)*", ""));
						System.out.println(newValue);
					}
				}
			});
			setText(null);
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
		textField = new TextField(getString());
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.setOnAction((e) -> commitEdit(textField.getText()));
		textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (!newValue) {
				System.out.println("Commiting " + textField.getText());
				commitEdit(textField.getText());
				String str = textField.getText();
				if (!str.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText(str + " ���������������� �����");
					alert.show();
					setText(str);
					textField.clear();
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
