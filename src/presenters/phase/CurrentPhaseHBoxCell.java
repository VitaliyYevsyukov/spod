package presenters.phase;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Created by Vitaly on 05.09.2017.
 */
public class CurrentPhaseHBoxCell extends HBox {

    private Label currentPhase = new Label();
    private Label numberOfCurrentPhase = new Label();
    

    CurrentPhaseHBoxCell(String labelPhase, String number){
        super();

        currentPhase.setText(labelPhase);
        currentPhase.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(currentPhase, Priority.ALWAYS);

        numberOfCurrentPhase.setText(number);

        this.getChildren().addAll(currentPhase, numberOfCurrentPhase);
    }

    public Label getNumberOfCurrentPhase() {
        return numberOfCurrentPhase;
    }

    public void setNumberOfCurrentPhase(Label numberOfCurrentPhase) {
        this.numberOfCurrentPhase = numberOfCurrentPhase;
    }

}
