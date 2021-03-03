package presenters.programs;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Vitaly on 23.01.2017.
 */
public class PhaseNumber {

    private final SimpleStringProperty phaseNumber;

    public PhaseNumber(String phaseNumber) {
        this.phaseNumber = new SimpleStringProperty(phaseNumber);
    }
    public String getPhaseNumber() {
        return this.phaseNumber.get();
    }
    public StringProperty phaseNumberProperty() {
        return this.phaseNumber;
    }
    public void setPhaseNumber(String phaseNumber) {
        this.phaseNumber.set(phaseNumber);
    }
    @Override
    public String toString(){
        return phaseNumber.get();
    }
    
}
