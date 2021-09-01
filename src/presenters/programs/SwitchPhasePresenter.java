package presenters.programs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import roadModel.IRoadModel;

public class SwitchPhasePresenter {

	@FXML
	private TableView<SwitchPhase> tableViewSwichPhase;
	@FXML
	private TableColumn<SwitchPhase, PhaseNumber> tableColumnPhase;
	@FXML
	private TableColumn<SwitchPhase, PhaseNumber> tableColumnPhase1, tableColumnPhase2;
	@FXML
	private TableColumn<SwitchPhase, SwitchPhaseMode> tableColumnMode;
	@FXML
	private TableColumn<SwitchPhase, String> tableColumnTmain;
	@FXML
	private TableColumn<SwitchPhase, String> tableColumnTpromtact;
	@FXML
	private TableColumn<SwitchPhase, String> tableColumnTphase;
	@FXML
	private Button btnCreateSwichPhase, btnDeleteSwichPhase;
	
	private IRoadModel iRoadModel;
	RoadProgram roadProgram;
	SwitchPhase swichPhase;
	Map<RoadProgram, List<SwitchPhase>> mapOfSwichPhase;
	List<SwitchPhase> swichPhaseList;
	
	public void showSwichPhase(IRoadModel iRoadModel, RoadProgram roadProgram) {
		this.iRoadModel = iRoadModel;
		this.roadProgram = roadProgram;
		
		mapOfSwichPhase = iRoadModel.getModel().getRoadProgramsModel().getMapOfSwichPhase();
		
		if(!mapOfSwichPhase.isEmpty()) {
			List<SwitchPhase> swichPhaseList = mapOfSwichPhase.get(roadProgram);
			tableViewSwichPhase.setItems(FXCollections.observableArrayList(swichPhaseList));
			tableViewSwichPhase.getSelectionModel().selectFirst();
		}
	}
	
	public void createSwichPhase() {
		System.out.println("Press 'Create switch phase'");
		
		mapOfSwichPhase = iRoadModel.getModel().getRoadProgramsModel().getMapOfSwichPhase();
		
		swichPhase = new SwitchPhase();
		
		if(mapOfSwichPhase.isEmpty()) {
			List<SwitchPhase> swichPhasesList = new ArrayList<SwitchPhase>();
			swichPhasesList.add(swichPhase);
			mapOfSwichPhase.put(roadProgram, swichPhasesList);
		}else {
			mapOfSwichPhase.get(roadProgram).add(swichPhase);
		}
		
		tableViewSwichPhase.setItems(FXCollections.observableArrayList(mapOfSwichPhase.get(roadProgram)));
		
		tableViewSwichPhase.getSelectionModel().selectLast();
		
	}
		
	public void deleteSwichPhase() {
		System.out.println("Press 'Delete swich phase'");
		
		if(!tableViewSwichPhase.getItems().isEmpty()) {
			mapOfSwichPhase = iRoadModel.getModel().getRoadProgramsModel().getMapOfSwichPhase();
			
			SwitchPhase swichPhaseToDelete = tableViewSwichPhase.getSelectionModel().getSelectedItem();
			mapOfSwichPhase.get(roadProgram).remove(swichPhaseToDelete);
			
			tableViewSwichPhase.getItems().remove(swichPhaseToDelete);
			tableViewSwichPhase.getSelectionModel().selectLast();
		}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Внимание");
			alert.setHeaderText("Вы не выбрали элемент для удаления");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
		}
		
	}
	
	
	@FXML
	public void initialize() {
		
		tableViewSwichPhase.setEditable(true);
				
		Callback<TableColumn<SwitchPhase, PhaseNumber>, TableCell<SwitchPhase, PhaseNumber>> swichPhaseComboBoxCellFactory = (TableColumn<SwitchPhase, PhaseNumber> param) -> new SwitchPhaseNumberComboBoxEditingCell(iRoadModel.getModel().getRoadPhaseModel());
		Callback<TableColumn<SwitchPhase, String>, TableCell<SwitchPhase, String>> swichPhaseCellFactory = (TableColumn<SwitchPhase, String> param) -> new SwitchPhaseEditingCell(iRoadModel);
		Callback<TableColumn<SwitchPhase, SwitchPhaseMode>, TableCell<SwitchPhase, SwitchPhaseMode>> switchPhaseModeComboBoxCellFactory = (TableColumn<SwitchPhase, SwitchPhaseMode> param) -> new SwitchPhaseModeComboBoxEditingCell();
		
		tableColumnPhase.setCellValueFactory(cellData -> cellData.getValue().phaseNumberProperty());
		tableColumnPhase.setCellFactory(swichPhaseComboBoxCellFactory);
		tableColumnPhase.setStyle("-fx-alignment: CENTER;");
		tableColumnPhase.setOnEditCommit((TableColumn.CellEditEvent<SwitchPhase, PhaseNumber> t) -> {
			((SwitchPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhase(t.getNewValue());
		});
		
		tableColumnTmain.setCellValueFactory(cellData -> cellData.getValue().mainTimeProperty());
		tableColumnTmain.setCellFactory(swichPhaseCellFactory);
		tableColumnTmain.setStyle("-fx-alignment: CENTER;");
		tableColumnTmain.setOnEditCommit((TableColumn.CellEditEvent<SwitchPhase, String> t) -> {
			((SwitchPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMainTime(t.getNewValue());
		});
		
		tableColumnTpromtact.setCellValueFactory(cellData -> cellData.getValue().promtactProperty());
		tableColumnTpromtact.setCellFactory(swichPhaseCellFactory);
		tableColumnTpromtact.setStyle("-fx-alignment: CENTER;");
		tableColumnTpromtact.setOnEditCommit((TableColumn.CellEditEvent<SwitchPhase, String> t) -> {
			((SwitchPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPromtact(t.getNewValue());
		});
		
		tableColumnTphase.setCellValueFactory(cellData -> cellData.getValue().durationPhaseProperty());
		tableColumnTphase.setCellFactory(swichPhaseCellFactory);
		tableColumnTphase.setStyle("-fx-alignment: CENTER;");
		tableColumnTphase.setOnEditCommit((TableColumn.CellEditEvent<SwitchPhase, String> t) -> {
			((SwitchPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDurationPhase(t.getNewValue());
		});
		
		tableColumnPhase1.setCellValueFactory(cellData -> cellData.getValue().toPhase1NumberProperty());
		tableColumnPhase1.setCellFactory(swichPhaseComboBoxCellFactory);
		tableColumnPhase1.setStyle("-fx-alignment: CENTER;");
		tableColumnPhase1.setOnEditCommit((TableColumn.CellEditEvent<SwitchPhase, PhaseNumber> t) -> {
			((SwitchPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setToPhase1(t.getNewValue());
		});
		
		tableColumnPhase2.setCellValueFactory(cellData -> cellData.getValue().toPhase2NumberProperty());
		tableColumnPhase2.setCellFactory(swichPhaseComboBoxCellFactory);
		tableColumnPhase2.setStyle("-fx-alignment: CENTER;");
		tableColumnPhase2.setOnEditCommit((TableColumn.CellEditEvent<SwitchPhase, PhaseNumber> t) -> {
			((SwitchPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setToPhase2(t.getNewValue());
		});
		
		tableColumnMode.setCellValueFactory(cellData -> cellData.getValue().switchPhaseProperty());
		tableColumnMode.setCellFactory(switchPhaseModeComboBoxCellFactory);
		tableColumnMode.setStyle("-fx-alignment: CENTER;");
		tableColumnMode.setOnEditCommit((TableColumn.CellEditEvent<SwitchPhase, SwitchPhaseMode> t) -> {
			((SwitchPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSwitchPhaseMode(t.getNewValue());
		});
		
		/*tableColumnProgramMode.setCellValueFactory(cellData -> cellData.getValue().roadProgramProgramModeProperty());
		tableColumnProgramMode.setCellFactory(programComboBoxCellFactory);
		tableColumnProgramMode.setStyle("-fx-alignment: CENTER;");
		tableColumnProgramMode.setOnEditCommit((TableColumn.CellEditEvent<RoadProgram, ProgramMode> t) -> {
			((RoadProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadProgram_programMode(t.getNewValue());
			
			if(t.getNewValue().getMode().equals("Желтое мигание") || t.getNewValue().getMode().equals("Отключение светофора")) {		// if type don't support phase
				List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(tableViewAllProgram.getSelectionModel().getSelectedItem());
				phaseInProgramsList.clear();
				tableViewPhaseInProgram.getItems().clear();
			}
		});*/
		
		/*tableColumnPhase.setCellValueFactory(cellData -> cellData.getValue().phaseNumberProperty());
		tableColumnPhase.setCellFactory(swichPhaseComboBoxCellFactory);
		tableColumnPhase.setStyle("-fx-alignment: CENTER;");
		tableColumnPhase.setOnEditCommit((TableColumn.CellEditEvent<SwichPhase, PhaseNumber> t) -> {
			((SwichPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhase(t.getNewValue());
		});
		
		tableColumnSwichPhase.setCellValueFactory(cellData -> cellData.getValue().toPhaseNumberProperty());
		tableColumnSwichPhase.setCellFactory(swichPhaseComboBoxCellFactory);
		tableColumnSwichPhase.setStyle("-fx-alignment: CENTER;");
		tableColumnSwichPhase.setOnEditCommit((TableColumn.CellEditEvent<SwichPhase, PhaseNumber> t) -> {
			((SwichPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setToPhase(t.getNewValue());
		});
		
		tableColumnMainTime.setCellValueFactory(cellData -> cellData.getValue().mainTimeProperty());
		tableColumnMainTime.setCellFactory(swichPhaseCellFactory);
		tableColumnMainTime.setStyle("-fx-alignment: CENTER;");
		tableColumnMainTime.setOnEditCommit((TableColumn.CellEditEvent<SwichPhase, String> t) -> {
			((SwichPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMainTime(t.getNewValue());
		});
		
		tableColumnPromtact.setCellValueFactory(cellData -> cellData.getValue().promtactProperty());
		tableColumnPromtact.setCellFactory(swichPhaseCellFactory);
		tableColumnPromtact.setStyle("-fx-alignment: CENTER;");
		tableColumnPromtact.setOnEditCommit((TableColumn.CellEditEvent<SwichPhase, String> t) -> {
			((SwichPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPromtact(t.getNewValue());
		});
		
		tableColumnDuration.setCellValueFactory(cellData -> cellData.getValue().durationPhaseProperty());
		tableColumnDuration.setCellFactory(swichPhaseCellFactory);
		tableColumnDuration.setStyle("-fx-alignment: CENTER;");
		tableColumnDuration.setOnEditCommit((TableColumn.CellEditEvent<SwichPhase, String> t) -> {
			((SwichPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDurationPhase(t.getNewValue());
		});*/
		
	}
	
}
