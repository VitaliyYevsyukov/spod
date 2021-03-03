package presenters.directions;

/**
 * Created by Vitaly on 13.12.2017.
 */
public class ControlledChannel {

    private String controlledDirection;
    private String controlledDirectionChannel;

    public ControlledChannel(){
        controlledDirection = new String();
        controlledDirectionChannel = new String();
    }

    public String getControlledDirection() {
        return controlledDirection;
    }

    public void setControlledDirection(String controlledDirection) {
        this.controlledDirection = controlledDirection;
    }

    public String getControlledDirectionChannel() {
        return controlledDirectionChannel;
    }

    public void setControlledDirectionChannel(String controlledDirectionChannel) {
        this.controlledDirectionChannel = controlledDirectionChannel;
    }

}
