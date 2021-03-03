package presenters.promtactu;

import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import presenters.directions.RoadDirection;

public class SpecificPromtactTablePresenter {
	@FXML
    private TableView<SpecificPromtactTableData> tableViewSpecificPromtactTable;
	
	@FXML
    private TableColumn<SpecificPromtactTableData, String> tableColumnNumber, tableColumnTypeDirection, tableColumnEndGreenAddit, tableColumnDurationGreenBlink,
            tableColumnDurationYellow, tableColumnEndOfRed, tableColumnDurationRedYellow;
	
	@FXML
	private Button buttonOK;

	RoadDirection roadDirection;

	List<RoadDirection> roadDirectionList;
	
	ObservableList<SpecificPromtactTableData> promtactTableDataObservableList = FXCollections.observableArrayList();
	
	Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfDirectionSpecificPromtact;
	
	SpecificPromtactTableData specificPromtactTableData;
	
	public void buttonOKEvent(){
        Stage stage = (Stage) buttonOK.getScene().getWindow();
        stage.close();
    }
	
	public void setItemsTableView() {
		
	}
	
	
	public void listRoadDirection(List<RoadDirection> roadDirectionList){
        this.roadDirectionList = roadDirectionList;
    }
    public void mapOfDirection(Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfDirectionSpecificPromtact){
        this.mapOfDirectionSpecificPromtact = mapOfDirectionSpecificPromtact;
    }

    @FXML
    public void initialize(){


    }
	    
	    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
