package presenters.programs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import presenters.phase.RoadPhase;
import presenters.phase.RoadPhaseModel;
import roadModel.IRoadModel;

import java.util.Map;

/**
 * Created by Vitaly on 23.01.2017.
 */
public class PhaseInProgramComboBoxEditingCell extends TableCell<PhaseInProgram, PhaseNumber> {

    private ComboBox<PhaseNumber> comboBox;
    private RoadPhaseModel roadPhaseModel;

    public PhaseInProgramComboBoxEditingCell(RoadPhaseModel roadPhaseModel){
        this.roadPhaseModel = roadPhaseModel;
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
        setText(getTyp().getPhaseNumber());
        setGraphic(null);
    }

    @Override
    public void updateItem(PhaseNumber item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getTyp());
                }
                setText(getTyp().getPhaseNumber());
                setGraphic(comboBox);
            } else {
                setText(getTyp().getPhaseNumber());
                setGraphic(null);
            }
        }
    }

    private void createComboBox() {
        ObservableList<PhaseNumber> phaseNumbers = FXCollections.observableArrayList();
        for(RoadPhase roadPhase : roadPhaseModel.getRoadPhaseList()){
            phaseNumbers.add(new PhaseNumber(roadPhase.getRoadPhase_number()));
        }
        comboBox = new ComboBox<>(phaseNumbers);
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

    private void comboBoxConverter(ComboBox<PhaseNumber> comboBox) {
        // Define rendering of the list of values in ComboBox drop down.
        comboBox.setCellFactory((c) -> {
            return new ListCell<PhaseNumber>() {
                @Override
                protected void updateItem(PhaseNumber item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getPhaseNumber());
                    }
                }
            };
        });
    }

    private PhaseNumber getTyp() {
        return getItem() == null ? new PhaseNumber("") : getItem();
    }

}
