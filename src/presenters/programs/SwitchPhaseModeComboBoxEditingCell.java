package presenters.programs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;

public class SwitchPhaseModeComboBoxEditingCell extends TableCell<SwitchPhase, SwitchPhaseMode>{
	
	private ObservableList<SwitchPhaseMode> modes = FXCollections.observableArrayList(
			new SwitchPhaseMode("Стандартный"),
			new SwitchPhaseMode("Вставка"),
			new SwitchPhaseMode("Плавающая"),
			new SwitchPhaseMode("Замена"));
	
	private ComboBox<SwitchPhaseMode> comboBox;

    public SwitchPhaseModeComboBoxEditingCell() {

    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createComboBox();
            setText("Пусто");
            setGraphic(comboBox);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getTyp().getMode());
        setGraphic(null);
    }

    @Override
    public void updateItem(SwitchPhaseMode item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getTyp());
                }
                setText(getTyp().getMode());
                setGraphic(comboBox);
            } else {
                setText(getTyp().getMode());
                setGraphic(null);
            }
        }
    }

    private void createComboBox() {
        //langXML();
        //itemProgramModeLang(langXML);
        comboBox = new ComboBox<>(modes);
        comboBoxConverter(comboBox);
        comboBox.valueProperty().set(getTyp());
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        comboBox.setOnAction((e) -> {
            System.out.println("Committed: " + comboBox.getSelectionModel().getSelectedItem());
            commitEdit(comboBox.getSelectionModel().getSelectedItem());
        });
//            comboBox.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) {
//                    commitEdit(comboBox.getSelectionModel().getSelectedItem());
//                }
//      });
    }

    private void comboBoxConverter(ComboBox<SwitchPhaseMode> comboBox) {
        // Define rendering of the list of values in ComboBox drop down.
        comboBox.setCellFactory((c) -> {
            return new ListCell<SwitchPhaseMode>() {
                @Override
                protected void updateItem(SwitchPhaseMode item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getMode());
                    }
                }
            };
        });
    }

    private SwitchPhaseMode getTyp() {
        return getItem() == null ? new SwitchPhaseMode("") : getItem();
    }

}
