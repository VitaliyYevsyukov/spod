package presenters.programs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import roadModel.IRoadModel;

public class SpeedSignPresenter {
	
	@FXML
	private ListView<SpeedSignAllProgramsHBoxCell> listViewAllPrograms;
	@FXML
	private ListView<SpeedSignTextFieldEditingCell> listViewSIgnList;
	@FXML
	Button btnOK;
	
	
	Map<String, List<SpeedSign>> mapOfSpeedSign;
	
	ObservableList<SpeedSignAllProgramsHBoxCell> observableListSpeedSignAllProgramsHBoxCell;
	ObservableList<SpeedSignTextFieldEditingCell> observableListSign;
	
	List<SpeedSignAllProgramsHBoxCell> speedSignAllProgramsHBoxCellsList = new ArrayList<>();
	List<SpeedSignTextFieldEditingCell> speedSignTextFieldEditingCellsList = new ArrayList<>();
	
	List<SpeedSign> speedSignsList = new ArrayList<>();
	
	SpeedSignTextFieldEditingCell signTextFieldEditingCell;
	
	boolean OKwasPressed = false;
	int indexSign;
	
	private IRoadModel iRoadModel;
	
	public void showSpeedSign(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;
		mapOfSpeedSign = iRoadModel.getModel().getRoadProgramsModel().getMapOfProgramSpeedSign();
		
		boolean isShow = false;
		
		for(Map.Entry<String, List<SpeedSign>> entry : mapOfSpeedSign.entrySet()) {
			speedSignAllProgramsHBoxCellsList.add(new SpeedSignAllProgramsHBoxCell("Программа № ", entry.getKey()));
			observableListSpeedSignAllProgramsHBoxCell = FXCollections.observableArrayList(speedSignAllProgramsHBoxCellsList);
			listViewAllPrograms.setItems(observableListSpeedSignAllProgramsHBoxCell);
			listViewAllPrograms.getSelectionModel().selectFirst();
			
			if(!isShow) {
				speedSignTextFieldEditingCellsList.clear();
				listViewSIgnList.getItems().clear();
				for(SpeedSign speedSign : entry.getValue()) {
					signTextFieldEditingCell = new SpeedSignTextFieldEditingCell();	
					signTextFieldEditingCell.getLabelNumberOfSign().setText(Integer.toString(listViewSIgnList.getItems().size() + 1));
					signTextFieldEditingCell.getTextFieldSignValue().setText(speedSign.getRecomendSpeed());
					speedSignTextFieldEditingCellsList.add(signTextFieldEditingCell);
					observableListSign = FXCollections.observableArrayList(speedSignTextFieldEditingCellsList);
					listViewSIgnList.setItems(observableListSign);
					listViewSIgnList.getSelectionModel().selectFirst();
				}
				isShow = true;
			}
		}
	}
	
	public void createSign() {
		mapOfSpeedSign = iRoadModel.getModel().getRoadProgramsModel().getMapOfProgramSpeedSign();
		String programNumber = listViewAllPrograms.getSelectionModel().getSelectedItem().getNumber().getText();
		List<RoadProgram> roadProgramsList = iRoadModel.getModel().getRoadProgramsModel().getRoadProgramList();
		
		for(SpeedSignTextFieldEditingCell signTextFieldEditingCell : speedSignTextFieldEditingCellsList) {
			signTextFieldEditingCell.getTextFieldSignValue().setDisable(true);
		}
		
		
		signTextFieldEditingCell = new SpeedSignTextFieldEditingCell();	
		signTextFieldEditingCell.getLabelNumberOfSign().setText(Integer.toString(listViewSIgnList.getItems().size() + 1));
		speedSignTextFieldEditingCellsList.add(signTextFieldEditingCell);
		observableListSign = FXCollections.observableArrayList(speedSignTextFieldEditingCellsList);
		listViewSIgnList.setItems(observableListSign);
		listViewSIgnList.getSelectionModel().select(listViewSIgnList.getItems().size() - 1);
		signTextFieldEditingCell.getTextFieldSignValue().setDisable(false);
		
		SpeedSign speedSign = new SpeedSign();
		
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		
		speedSign.setIdSpeedSign(id);
		speedSign.setNumberSpeedSign(signTextFieldEditingCell.getLabelNumberOfSign().getText());
		
		for(RoadProgram roadProgram : roadProgramsList) {
			if(programNumber.equals(roadProgram.getRoadProgram_number())) {
				mapOfSpeedSign.get(programNumber).add(speedSign);
			}
		}
		
		signTextFieldEditingCell.getTextFieldSignValue().textProperty().addListener((observable, oldValue, newValue) -> {
			indexSign = listViewSIgnList.getSelectionModel().getSelectedIndex();
			
			for(RoadProgram roadProgram : roadProgramsList) {
				if(programNumber.equals(roadProgram.getRoadProgram_number())) {
					mapOfSpeedSign.get(programNumber).get(indexSign).setRecomendSpeed(newValue);
				}
			}
		});
		
	}
	
	public void deleteSign() {
		mapOfSpeedSign = iRoadModel.getModel().getRoadProgramsModel().getMapOfProgramSpeedSign();
		
		String key = listViewAllPrograms.getSelectionModel().getSelectedItem().getNumber().getText();
		int index = listViewSIgnList.getSelectionModel().getSelectedIndex();
		
		if(index >= 0) {
			listViewSIgnList.getItems().remove(index);
			mapOfSpeedSign.get(key).remove(index);
			
			for(int i = 1; i <= listViewSIgnList.getItems().size(); i++) {
				listViewSIgnList.getItems().get(i - 1).getLabelNumberOfSign().setText(Integer.toString(i));
				mapOfSpeedSign.get(key).get(i - 1).setNumberSpeedSign(Integer.toString(i));
			}
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
	
	public void selectProgram() {
		mapOfSpeedSign = iRoadModel.getModel().getRoadProgramsModel().getMapOfProgramSpeedSign();
		listViewAllPrograms.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			listViewSIgnList.getItems().clear();
			speedSignTextFieldEditingCellsList.clear();
			
			String programNumber = newValue.getNumber().getText();
			List<SpeedSign> speedSignsList = mapOfSpeedSign.get(programNumber);
			if(speedSignsList.size() != 0) {
				for(SpeedSign speedSign : speedSignsList) {
					signTextFieldEditingCell = new SpeedSignTextFieldEditingCell();		
					signTextFieldEditingCell.getLabelNumberOfSign().setText(Integer.toString(listViewSIgnList.getItems().size() + 1));
					signTextFieldEditingCell.getTextFieldSignValue().setText(speedSign.getRecomendSpeed());
					speedSignTextFieldEditingCellsList.add(signTextFieldEditingCell);
					observableListSign = FXCollections.observableArrayList(speedSignTextFieldEditingCellsList);
					listViewSIgnList.setItems(observableListSign);
					listViewSIgnList.getSelectionModel().selectFirst();
				}
			}
		});
	}
	
	public void selectSign() {
		mapOfSpeedSign = iRoadModel.getModel().getRoadProgramsModel().getMapOfProgramSpeedSign();
		String programNumber = listViewAllPrograms.getSelectionModel().getSelectedItem().getNumber().getText();
		indexSign = listViewSIgnList.getSelectionModel().getSelectedIndex();
		
		System.out.println(indexSign);
		
		SpeedSignTextFieldEditingCell speedSignTextFieldEditingCell = listViewSIgnList.getSelectionModel().getSelectedItem();
		speedSignTextFieldEditingCell.getTextFieldSignValue().requestFocus();
		//speedSignTextFieldEditingCell.getTextFieldSignValue().appendText(""); 		// cursor on end
		speedSignTextFieldEditingCell.getTextFieldSignValue().textProperty().addListener((observable, oldValue, newValue) -> {
			mapOfSpeedSign.get(programNumber).get(indexSign).setRecomendSpeed(newValue);
		});
	}
	
	public void pressOK() {
		Stage stage = (Stage) btnOK.getScene().getWindow();
        stage.close();
        OKevent();
	}
	
	public boolean OKevent() {
		return true;
	}
	
	
	@FXML
	public void initialize() {
		
		listViewAllPrograms.setPlaceholder(new Label("Нет данных для отображения"));
		listViewSIgnList.setPlaceholder(new Label("Нет данных для отображения"));
		
		listViewSIgnList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValueItem) -> {
			if(newValueItem != null) {
				newValueItem.getTextFieldSignValue().textProperty().addListener(new ChangeListener<String>() {
					@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
						if (!newValue.matches("\\d*")) {
							newValueItem.getTextFieldSignValue().setText(newValue.replaceAll("[^\\d]", ""));
						 }
					}
				 });
				for(SpeedSignTextFieldEditingCell signTextFieldEditingCell : speedSignTextFieldEditingCellsList) {
					signTextFieldEditingCell.getTextFieldSignValue().setDisable(true);
				}
				newValueItem.getTextFieldSignValue().setDisable(false);
				newValueItem.getTextFieldSignValue().requestFocus();
				//newValue.getTextFieldSignValue().appendText("");		// cursor on end
			}
		});
		
	}
}
