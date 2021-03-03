package presenters.directions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vitaly on 13.12.2017.
 */
public class GroupControl {

    private String groupNumber;
    private List<ControlledChannel> controlledChannelList = new ArrayList<>();
    private Map<GroupControl, List<ControlledChannel>> mapOfGroupControl;
    ControlledChannel controlledChannel;

    public GroupControl(){
        groupNumber = new String();
        controlledChannel = new ControlledChannel();
        mapOfGroupControl = new HashMap<>();
    }
    

    public String getGroupNumber() {
        return groupNumber;
    }
    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public List<ControlledChannel> getControlledChannelList() {
        return controlledChannelList;
    }
    public void setControlledChannelList(List<ControlledChannel> controlledChannelList) {
        this.controlledChannelList = controlledChannelList;
    }

    public Map<GroupControl, List<ControlledChannel>> getMapOfGroupControl() {
        return mapOfGroupControl;
    }
    public void setMapOfGroupControl(Map<GroupControl, List<ControlledChannel>> mapOfGroupControl) {
        this.mapOfGroupControl = mapOfGroupControl;
    }

}
