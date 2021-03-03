package presenters.programs;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Vitaly on 23.12.2016.
 */
public class ProgramMode {

    private final SimpleStringProperty mode;

    public ProgramMode(String mode) {
        this.mode = new SimpleStringProperty(mode);
    }

    public String getMode(){
        return this.mode.get();
    }

    public StringProperty modeProperty(){
        return this.mode;
    }

    public void setMode(String mode){
        this.mode.set(mode);
    }

    @Override
    public String toString() {
        return mode.get();
    }
}
