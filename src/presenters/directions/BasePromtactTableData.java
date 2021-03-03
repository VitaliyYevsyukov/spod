package presenters.directions;

import javafx.beans.property.SimpleStringProperty;

public class BasePromtactTableData {

    private SimpleStringProperty number;
    private SimpleStringProperty type;
    private SimpleStringProperty endGreenAddit;
    private SimpleStringProperty durationGreenBlink;
    private SimpleStringProperty durationYellow;
    private SimpleStringProperty endOfRed;
    private SimpleStringProperty durationRedYellow;

    BasePromtactTableData(){
        number = new SimpleStringProperty();
        type = new SimpleStringProperty();
        endGreenAddit = new SimpleStringProperty();
        durationGreenBlink = new SimpleStringProperty();
        durationYellow = new SimpleStringProperty();
        endOfRed = new SimpleStringProperty();
        durationRedYellow = new SimpleStringProperty();
    }
    

    public String getNumber() {
        return number.get();
    }
    public void setNumber(String number){
        this.number.set(number);
    }

    public String getType() {
        return type.get();
    }
    public void setType(String type) {
        this.type.set(type);
    }

    public String getEndGreenAddit() {
        return endGreenAddit.get();
    }
    public void setEndGreenAddit(String endGreenAddit) {
        this.endGreenAddit.set(endGreenAddit);
    }

    public String getDurationGreenBlink() {
        return durationGreenBlink.get();
    }
    public void setDurationGreenBlink(String durationGreenBlink) {
        this.durationGreenBlink.set(durationGreenBlink);
    }

    public String getDurationYellow() {
        return durationYellow.get();
    }
    public void setDurationYellow(String durationYellow) {
        this.durationYellow.set(durationYellow);
    }

    public String getEndOfRed() {
        return endOfRed.get();
    }
    public void setEndOfRed(String endOfRed) {
        this.endOfRed.set(endOfRed);
    }

    public String getDurationRedYellow() {
        return durationRedYellow.get();
    }
    public void setDurationRedYellow(String durationRedYellow) {
        this.durationRedYellow.set(durationRedYellow);
    }

}
