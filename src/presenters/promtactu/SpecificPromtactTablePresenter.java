package presenters.promtactu;

import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import presenters.directions.BasePromtactTableData;
import presenters.directions.RoadDirection;

public class SpecificPromtactTablePresenter {
	@FXML
    private TableView<SpecificPromtactTableData> tableViewSpecificPromtactTable;
	@FXML
    private TableColumn<SpecificPromtactTableData, String> tableColumnNumber, tableColumnTypeDirection, tableColumnEndGreenAddit, tableColumnDurationGreenBlink,
            tableColumnDurationYellow, tableColumnEndOfRed, tableColumnDurationRedYellow;
	@FXML
	private Button buttonOK, btnPrevious, btnNext;
	@FXML
    private Label lbFromPhase, lbToPhase;

	int index = 0;
	int finalIndex;

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

		String fromPhaseNumber = mapOfDirectionSpecificPromtact.keySet().stream().findFirst().get().getComboBoxFromPhase().getValue();
		String toPhaseNumber = mapOfDirectionSpecificPromtact.keySet().stream().findFirst().get().getComboBoxToPhase().getValue();

		lbFromPhase.setText(fromPhaseNumber);
		lbToPhase.setText(toPhaseNumber);

		InterphaseTransitionsHBoxCell firstInterPhase = mapOfDirectionSpecificPromtact.keySet().stream().findFirst().get();
		Map<String, PromtactData> promtactDataMap = mapOfDirectionSpecificPromtact.get(firstInterPhase);

		for(RoadDirection roadDirection : roadDirectionList){
			String directionNumber = roadDirection.getRoadDirections_number();
			String typeDirection = roadDirection.getRoadDirections_typeOfDirection().getTypDirection();

			for(Map.Entry<String, PromtactData> entry :  promtactDataMap.entrySet()){
				String dirNumberKey = entry.getKey();
				PromtactData promtactData = entry.getValue();

				if(directionNumber.equals(dirNumberKey)){
					specificPromtactTableData = new SpecificPromtactTableData();

					specificPromtactTableData.setNumber(directionNumber);
					specificPromtactTableData.setType(typeDirection);
					specificPromtactTableData.setEndGreenAddit(promtactData.getRoadPromtactu_endGreenAddit());
					specificPromtactTableData.setDurationGreenBlink(promtactData.getRoadPromtactu_durationGreenBlink());
					specificPromtactTableData.setDurationYellow(promtactData.getRoadPromtactu_durationYellow());
					specificPromtactTableData.setEndOfRed(promtactData.getRoadPromtactu_endRed());
					specificPromtactTableData.setDurationRedYellow(promtactData.getRoadPromtactu_durationRedYellow());

					promtactTableDataObservableList.add(specificPromtactTableData);

					tableColumnNumber.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("number"));
					tableColumnTypeDirection.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("type"));
					tableColumnEndGreenAddit.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("endGreenAddit"));
					tableColumnDurationGreenBlink.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("durationGreenBlink"));
					tableColumnDurationYellow.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("durationYellow"));
					tableColumnEndOfRed.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("endOfRed"));
					tableColumnDurationRedYellow.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("durationRedYellow"));
				}
			}
		}
		tableViewSpecificPromtactTable.setItems(promtactTableDataObservableList);

		index++;

    }

    public InterphaseTransitionsHBoxCell getNextInterphase (InterphaseTransitionsHBoxCell currentInterPhase){
		InterphaseTransitionsHBoxCell nextInterPhase = null;

		Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> map = mapOfDirectionSpecificPromtact;
		Iterator<Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()){
			Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry = iterator.next();
			if(entry.getKey().equals(currentInterPhase)){
				InterphaseTransitionsHBoxCell i = iterator.next().getKey();
				nextInterPhase = i;
				break;
			}
		}

		return nextInterPhase;
	}

    public void eventNextInterphase(){
		System.out.println("Event next interphase");

		tableViewSpecificPromtactTable.getItems().clear();

		finalIndex = mapOfDirectionSpecificPromtact.size();

		if(index != finalIndex){

			InterphaseTransitionsHBoxCell currentInterPhase = null;

			String currentFrom = lbFromPhase.getText();
			String currentTo = lbToPhase.getText();

			for(Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()){
				InterphaseTransitionsHBoxCell interPhase = entry.getKey();
				if(interPhase.getComboBoxFromPhase().getValue().equals(currentFrom) & interPhase.getComboBoxToPhase().getValue().equals(currentTo)){
					currentInterPhase = interPhase;
				}
			}

			InterphaseTransitionsHBoxCell nextInterPhase = getNextInterphase(currentInterPhase);

			String nextFrom = nextInterPhase.getComboBoxFromPhase().getValue();
			String nextTo = nextInterPhase.getComboBoxToPhase().getValue();

			System.out.println("From " + nextFrom + " to " + nextTo);

			lbFromPhase.setText(nextFrom);
			lbToPhase.setText(nextTo);

			Map<String, PromtactData> promtactDataMap = mapOfDirectionSpecificPromtact.get(nextInterPhase);

			for(RoadDirection roadDirection : roadDirectionList){
				String directionNumber = roadDirection.getRoadDirections_number();
				String typeDirection = roadDirection.getRoadDirections_typeOfDirection().getTypDirection();

				for(Map.Entry<String, PromtactData> entry :  promtactDataMap.entrySet()){
					String dirNumberKey = entry.getKey();
					PromtactData promtactData = entry.getValue();

					if(directionNumber.equals(dirNumberKey)){
						specificPromtactTableData = new SpecificPromtactTableData();

						specificPromtactTableData.setNumber(directionNumber);
						specificPromtactTableData.setType(typeDirection);
						specificPromtactTableData.setEndGreenAddit(promtactData.getRoadPromtactu_endGreenAddit());
						specificPromtactTableData.setDurationGreenBlink(promtactData.getRoadPromtactu_durationGreenBlink());
						specificPromtactTableData.setDurationYellow(promtactData.getRoadPromtactu_durationYellow());
						specificPromtactTableData.setEndOfRed(promtactData.getRoadPromtactu_endRed());
						specificPromtactTableData.setDurationRedYellow(promtactData.getRoadPromtactu_durationRedYellow());

						promtactTableDataObservableList.add(specificPromtactTableData);

						tableColumnNumber.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("number"));
						tableColumnTypeDirection.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("type"));
						tableColumnEndGreenAddit.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("endGreenAddit"));
						tableColumnDurationGreenBlink.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("durationGreenBlink"));
						tableColumnDurationYellow.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("durationYellow"));
						tableColumnEndOfRed.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("endOfRed"));
						tableColumnDurationRedYellow.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("durationRedYellow"));
					}
				}
			}
			tableViewSpecificPromtactTable.setItems(promtactTableDataObservableList);

			index++;
		}

		if(index == finalIndex){
			btnNext.setDisable(true);
		}

		if(index > 0){
			btnPrevious.setDisable(false);
		}


	}

	public void eventPreviousInterphase(){
		System.out.println("Event previous interphase");

		index--;

		tableViewSpecificPromtactTable.getItems().clear();

		List<InterphaseTransitionsHBoxCell> interPhaseList = new ArrayList<>();

		if(index >= 1){

			String currentFrom = lbFromPhase.getText();
			String currentTo = lbToPhase.getText();

			InterphaseTransitionsHBoxCell previousInterPhase = null;

			for(Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()){
				InterphaseTransitionsHBoxCell interPhase = entry.getKey();
				interPhaseList.add(interPhase);
			}

			InterphaseTransitionsHBoxCell interphaseTransitionsHBoxCell = interPhaseList.get(index - 1);

			lbFromPhase.setText(interphaseTransitionsHBoxCell.getComboBoxFromPhase().getValue());
			lbToPhase.setText(interphaseTransitionsHBoxCell.getComboBoxToPhase().getValue());

			Map<String, PromtactData> promtactDataMap = mapOfDirectionSpecificPromtact.get(interphaseTransitionsHBoxCell);
			for(RoadDirection roadDirection : roadDirectionList){
				String directionNumber = roadDirection.getRoadDirections_number();
				String typeDirection = roadDirection.getRoadDirections_typeOfDirection().getTypDirection();

				for(Map.Entry<String, PromtactData> entry :  promtactDataMap.entrySet()){
					String dirNumberKey = entry.getKey();
					PromtactData promtactData = entry.getValue();

					if(directionNumber.equals(dirNumberKey)){
						specificPromtactTableData = new SpecificPromtactTableData();

						specificPromtactTableData.setNumber(directionNumber);
						specificPromtactTableData.setType(typeDirection);
						specificPromtactTableData.setEndGreenAddit(promtactData.getRoadPromtactu_endGreenAddit());
						specificPromtactTableData.setDurationGreenBlink(promtactData.getRoadPromtactu_durationGreenBlink());
						specificPromtactTableData.setDurationYellow(promtactData.getRoadPromtactu_durationYellow());
						specificPromtactTableData.setEndOfRed(promtactData.getRoadPromtactu_endRed());
						specificPromtactTableData.setDurationRedYellow(promtactData.getRoadPromtactu_durationRedYellow());

						promtactTableDataObservableList.add(specificPromtactTableData);

						tableColumnNumber.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("number"));
						tableColumnTypeDirection.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("type"));
						tableColumnEndGreenAddit.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("endGreenAddit"));
						tableColumnDurationGreenBlink.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("durationGreenBlink"));
						tableColumnDurationYellow.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("durationYellow"));
						tableColumnEndOfRed.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("endOfRed"));
						tableColumnDurationRedYellow.setCellValueFactory(new PropertyValueFactory<SpecificPromtactTableData, String>("durationRedYellow"));
					}
				}
			}

			tableViewSpecificPromtactTable.setItems(promtactTableDataObservableList);

		}

		if(index != finalIndex){
			btnNext.setDisable(false);
		}

		if(index == 1){
			btnPrevious.setDisable(true);
		}

	}

    @FXML
    public void initialize(){

		tableViewSpecificPromtactTable.setPlaceholder(new Label("Нет данных для отображения"));

		tableColumnNumber.setStyle("-fx-alignment:CENTER");
		tableColumnTypeDirection.setStyle("-fx-alignment:CENTER");
		tableColumnEndGreenAddit.setStyle("-fx-alignment:CENTER");
		tableColumnDurationGreenBlink.setStyle("-fx-alignment:CENTER");
		tableColumnDurationYellow.setStyle("-fx-alignment:CENTER");
		tableColumnEndOfRed.setStyle("-fx-alignment:CENTER");
		tableColumnDurationRedYellow.setStyle("-fx-alignment:CENTER");

    }
	
	
	
}
