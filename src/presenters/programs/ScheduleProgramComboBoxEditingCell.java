package presenters.programs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import roadModel.IRoadModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitaly on 23.01.2017.
 */
public class ScheduleProgramComboBoxEditingCell extends TableCell<ScheduleProgram, ScheduleNumber> {

    private ComboBox<ScheduleNumber> comboBox;
    private RoadProgramsModel roadProgramsModel;

    public ScheduleProgramComboBoxEditingCell(RoadProgramsModel roadProgramsModel) {
        this.roadProgramsModel = roadProgramsModel;
    }
    

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createComboBox();
            setText(null);
            setGraphic(comboBox);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getTyp().getScheduleNumber());
        setGraphic(null);
    }

    @Override
    public void updateItem(ScheduleNumber item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getTyp());
                }
                setText(getTyp().getScheduleNumber());
                setGraphic(comboBox);
            } else {
                setText(getTyp().getScheduleNumber());
                setGraphic(null);
            }
        }
    }

    private void createComboBox() {
        ObservableList<ScheduleNumber> scheduleNumbers = FXCollections.observableArrayList();
        for(RoadProgram roadProgram : roadProgramsModel.getRoadProgramList()){
            scheduleNumbers.add(new ScheduleNumber(roadProgram.getRoadProgram_number()));
        }

        comboBox = new ComboBox<>(scheduleNumbers);
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

    private void comboBoxConverter(ComboBox<ScheduleNumber> comboBox) {
        // Define rendering of the list of values in ComboBox drop down.
        comboBox.setCellFactory((c) -> {
            return new ListCell<ScheduleNumber>() {
                @Override
                protected void updateItem(ScheduleNumber item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getScheduleNumber());
                    }
                }
            };
        });
    }

    private ScheduleNumber getTyp() {
        return getItem() == null ? new ScheduleNumber("") : getItem();
    }

}
