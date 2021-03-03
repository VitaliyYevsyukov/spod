package presenters.directions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import presenters.promtactu.PromtactData;

import java.util.List;
import java.util.Map;

public class BasePromtactTablePresenter {

    @FXML
    private TableView<BasePromtactTableData> tableViewBasePromtactTable;
    @FXML
    private TableColumn<BasePromtactTableData, String> tableColumnNumber, tableColumnTypeDirection, tableColumnEndGreenAddit, tableColumnDurationGreenBlink,
            tableColumnDurationYellow, tableColumnEndOfRed, tableColumnDurationRedYellow;
    @FXML
    private Button buttonOK;

    RoadDirection roadDirection;

    List<RoadDirection> roadDirectionList;

    ObservableList<BasePromtactTableData> promtactTableDataObservableList = FXCollections.observableArrayList();;

    Map<String, PromtactData> promtactDataMap;

    BasePromtactTableData basePromtactTableData;

    public void buttonOKEvent(){
        Stage stage = (Stage) buttonOK.getScene().getWindow();
        stage.close();
    }
    

    public void setItemsTableView(){
        for(RoadDirection roadDirection : roadDirectionList){
            String directionNumber = roadDirection.getRoadDirections_number();
            String typeDirection = roadDirection.getRoadDirections_typeOfDirection().getTypDirection();

            for(Map.Entry<String, PromtactData> entry : promtactDataMap.entrySet()){
                String dirNumberKey = entry.getKey();
                PromtactData promtactData = entry.getValue();

                if(directionNumber.equals(dirNumberKey)){
                    basePromtactTableData = new BasePromtactTableData();

                    basePromtactTableData.setNumber(directionNumber);
                    basePromtactTableData.setType(typeDirection);
                    basePromtactTableData.setEndGreenAddit(promtactData.getRoadPromtactu_endGreenAddit());
                    basePromtactTableData.setDurationGreenBlink(promtactData.getRoadPromtactu_durationGreenBlink());
                    basePromtactTableData.setDurationYellow(promtactData.getRoadPromtactu_durationYellow());
                    basePromtactTableData.setEndOfRed(promtactData.getRoadPromtactu_endRed());
                    basePromtactTableData.setDurationRedYellow(promtactData.getRoadPromtactu_durationRedYellow());

                    promtactTableDataObservableList.add(basePromtactTableData);

                    tableColumnNumber.setCellValueFactory(new PropertyValueFactory<BasePromtactTableData, String>("number"));
                    tableColumnTypeDirection.setCellValueFactory(new PropertyValueFactory<BasePromtactTableData, String>("type"));
                    tableColumnEndGreenAddit.setCellValueFactory(new PropertyValueFactory<BasePromtactTableData, String>("endGreenAddit"));
                    tableColumnDurationGreenBlink.setCellValueFactory(new PropertyValueFactory<BasePromtactTableData, String>("durationGreenBlink"));
                    tableColumnDurationYellow.setCellValueFactory(new PropertyValueFactory<BasePromtactTableData, String>("durationYellow"));
                    tableColumnEndOfRed.setCellValueFactory(new PropertyValueFactory<BasePromtactTableData, String>("endOfRed"));
                    tableColumnDurationRedYellow.setCellValueFactory(new PropertyValueFactory<BasePromtactTableData, String>("durationRedYellow"));

                }
            }
        }
        tableViewBasePromtactTable.setItems(promtactTableDataObservableList);
    }

    public void listRoadDirection(List<RoadDirection> roadDirectionList){
        this.roadDirectionList = roadDirectionList;
    }
    public void mapOfDirection(Map<String, PromtactData> promtactDataMap){
        this.promtactDataMap = promtactDataMap;
    }

    @FXML
    public void initialize(){

    	tableViewBasePromtactTable.setPlaceholder(new Label("Нет данных для отображения"));
    	
    	tableColumnNumber.setStyle("-fx-alignment:CENTER");
    	tableColumnTypeDirection.setStyle("-fx-alignment:CENTER");
    	tableColumnEndGreenAddit.setStyle("-fx-alignment:CENTER");
    	tableColumnDurationGreenBlink.setStyle("-fx-alignment:CENTER");
    	tableColumnDurationYellow.setStyle("-fx-alignment:CENTER");
    	tableColumnEndOfRed.setStyle("-fx-alignment:CENTER");
    	tableColumnDurationRedYellow.setStyle("-fx-alignment:CENTER");
    	

    }
}
