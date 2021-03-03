package presenters.directions;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Vitaly on 14.12.2016.
 */
public class TypDirection {

    private final SimpleStringProperty typDirection;

    public TypDirection(String typDirection) {
        this.typDirection = new SimpleStringProperty(typDirection);
    }

    
    public String getTypDirection() {
        return this.typDirection.get();
    }

    public StringProperty typDirectionProperty() {
        return this.typDirection;
    }

    public void setTypDirection(String typDirection) {
        this.typDirection.set(typDirection);
    }

    @Override
    public String toString() {
        return typDirection.get();
    }

}
