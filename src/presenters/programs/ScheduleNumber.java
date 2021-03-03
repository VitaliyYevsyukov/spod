package presenters.programs;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitaly on 23.01.2017.
 */
public class ScheduleNumber {

    private final SimpleStringProperty scheduleNumber;

    public ScheduleNumber(){
        this.scheduleNumber = new SimpleStringProperty();
    }

    public ScheduleNumber (String scheduleNumber){
        this.scheduleNumber = new SimpleStringProperty(scheduleNumber);
    }

    public String getScheduleNumber(){
        return this.scheduleNumber.get();
    }

    public StringProperty scheduleNumberProperty(){
        return this.scheduleNumber;
    }

    public void setScheduleNumber(String scheduleNumber) {
        this.scheduleNumber.set(scheduleNumber);
    }

    @Override
    public String toString(){
        return scheduleNumber.get();
    }
}
