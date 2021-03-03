package presenters.phase;

import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import presenters.directions.RoadDirectionsModel;
import presenters.programs.PhaseInProgram;
import presenters.programs.RoadProgram;
import roadModel.IRoadModel;

/**
 * Created by Vitaly on 18.12.2016.
 */
class PhaseEditingCell extends TableCell<RoadPhase, String> {

    private TextField textField; 
    
    IRoadModel iRoadModel;
    
    Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram;

    public PhaseEditingCell() {
    }
    

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            textField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                    	textField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
            //setText(null);
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
                    //setGraphic(null);
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
        //textField.setEditable(false);		// set editable text field
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnAction((e) -> commitEdit(textField.getText()));
        
        textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                System.out.println("Commiting phase number " + textField.getText());
                commitEdit(textField.getText());
                
                
                
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }
}
