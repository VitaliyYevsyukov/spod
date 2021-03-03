package presenters.promtactu;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Created by Vitaly on 01.08.2017.
 */
public class InterphaseTransitionsHBoxCell extends HBox {

    private Label labelFromPhase = new Label();
    private Label labelToPhase = new Label();
    private ComboBox<String> comboBoxFromPhase = new ComboBox<>();
    private ComboBox<String> comboBoxToPhase = new ComboBox<>();
    private ObservableList<String> observableListComboBoxFromPhase;
    private ObservableList<String> observableListComboBoxToPhase;

    public InterphaseTransitionsHBoxCell(){
        super();

        labelFromPhase.setText("Переход из фазы №:");
        labelToPhase.setText("в фазу №:");
        
        HBox.setMargin(labelFromPhase, new Insets(3, 15, 0, 0));
        HBox.setMargin(labelToPhase, new Insets(3, 15, 0, 0));

        comboBoxFromPhase.setPrefWidth(USE_COMPUTED_SIZE);
        comboBoxFromPhase.setStyle("-fx-background-radius: 10; -fx-font-weight: bold");
        HBox.setMargin(comboBoxFromPhase, new Insets(0, 15, 0, 15));

        comboBoxToPhase.setPrefWidth(USE_COMPUTED_SIZE);
        comboBoxToPhase.setStyle("-fx-background-radius: 10; -fx-font-weight: bold");
        HBox.setMargin(comboBoxToPhase, new Insets(0, 0, 0, 15));

        this.getChildren().addAll(labelFromPhase, comboBoxFromPhase, labelToPhase, comboBoxToPhase);
    }

    public Label getLabelFromPhase() {
        return labelFromPhase;
    }

    public void setLabelFromPhase(Label labelFromPhase) {
        this.labelFromPhase = labelFromPhase;
    }

    public Label getLabelToPhase() {
        return labelToPhase;
    }

    public void setLabelToPhase(Label labelToPhase) {
        this.labelToPhase = labelToPhase;
    }

    public ComboBox<String> getComboBoxFromPhase() {
        return comboBoxFromPhase;
    }

    public void setComboBoxFromPhase(ComboBox<String> comboBoxFromPhase) {
        this.comboBoxFromPhase = comboBoxFromPhase;
    }

    public ComboBox<String> getComboBoxToPhase() {
        return comboBoxToPhase;
    }

    public void setComboBoxToPhase(ComboBox<String> comboBoxToPhase) {
        this.comboBoxToPhase = comboBoxToPhase;
    }

    public ObservableList<String> getObservableListComboBoxFromPhase() {
        return observableListComboBoxFromPhase;
    }

    public void setObservableListComboBoxFromPhase(ObservableList<String> observableListComboBoxFromPhase) {
        this.observableListComboBoxFromPhase = observableListComboBoxFromPhase;
        comboBoxFromPhase.getItems().addAll(observableListComboBoxFromPhase);
    }

    public ObservableList<String> getObservableListComboBoxToPhase() {
        return observableListComboBoxToPhase;
    }

    public void setObservableListComboBoxToPhase(ObservableList<String> observableListComboBoxToPhase) {
        this.observableListComboBoxToPhase = observableListComboBoxToPhase;
        comboBoxToPhase.getItems().addAll(observableListComboBoxToPhase);
    }

}
