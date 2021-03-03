package presenters.conflicts;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Created by Vitaly on 19.06.2017.
 */
public class ConflictForHBoxCell extends HBox {

    private Label labelFirst = new Label();
    private Label labelConflictNumber = new Label();

    ConflictForHBoxCell(String labelText, String conflictForDirection){
        super();

        labelFirst.setText(labelText);
        labelFirst.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(labelFirst, Priority.ALWAYS);

        labelConflictNumber.setText(conflictForDirection);

        this.getChildren().addAll(labelFirst, labelConflictNumber);
    }

    public Label getLabelConflictNumber() {
        return labelConflictNumber;
    }

    public void setLabelConflictNumber(Label labelConflictNumber) {
        this.labelConflictNumber = labelConflictNumber;
    }
}
