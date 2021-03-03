package presenters.phase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Vitaly on 15.12.2016.
 */
public class TVP {

    private final SimpleStringProperty tvp;

    public TVP(String tvp) {
        this.tvp = new SimpleStringProperty(tvp);
    }
    

    public String getTvp() {
        return this.tvp.get();
    }

    public StringProperty tvpProperty() {
        return this.tvp;
    }

    public void setTvp(String tvp) {
        this.tvp.set(tvp);
    }

    @Override
    public String toString() {
        return tvp.get();
    }

}
