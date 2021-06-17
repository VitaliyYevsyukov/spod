package presenters.detector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import roadModel.IRoadModel;

public class RoadDetectorPresenter {
	
	@FXML
	private Button buttonCreateDetector, buttonDeleteDetector, buttonCreateZone, buttonDeleteZone;
	@FXML
	private TextField textFieldResponse, textFieldSPIAddress, textFieldIPCamera, textFieldPeriodInterrogation, textFieldPeriodSaving, textFieldPortCamera, textFieldPortXML, textFieldPortHTTP, 
						textFieldModelDetector, textFieldLocationDetector, textFieldFaultTimeOut;
	@FXML
	private Label labelResponse, labelSPIAddress, labelConnectionType, labelIPCamera, labelPeriodInterrogation, labelPeriodSaving,
					labelPortCamera, labelPortXML, labelPortHTTP, labelZones, labelListZones,labelZoneSetting;
	@FXML
	private ChoiceBox<String> choiceBoxConnectionType;
	@FXML
	private ListView<RoadDetectorHBoxCell> listViewDetectors;
	@FXML
	private ListView<DetectorZonesHBoxCell> listViewZones;
	@FXML
	private CheckBox checkBoxSave;
	@FXML
	private AnchorPane anchorPaneDetector;
	
	IRoadModel iRoadModel;
	
	RoadDetectorHBoxCell roadDetectorHBoxCell;
	RoadDetectorHBoxCell previousRoadDetectorHBoxCell;
	DetectorZonesHBoxCell detectorZonesHBoxCell;
	DetectorZonesHBoxCell previousDetectorZonesHBoxCell;
	Detector detector;
	
	int indexZone;
	int previousIndex;
	
	List<Detector> allDetectorList;
	List<Detector> createdDetectorList;
	
	Map<RoadDetectorHBoxCell, List<Detector>> mapOfMultiZonesDetector;
	
	List<RoadDetectorHBoxCell> roadDetectorHBoxCellsList = new ArrayList<>();
	ObservableList<RoadDetectorHBoxCell> roadDetectorHBoxCellsObservableList;	
	
	List<DetectorZonesHBoxCell> detectorZonesHBoxCellsList = new ArrayList<>();
	ObservableList<DetectorZonesHBoxCell> detectorZonesHBoxCellsObservableList;
	
	public void show(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;
		textFieldModelDetector.clear();
		textFieldLocationDetector.clear();
		textFieldFaultTimeOut.clear();
		
		mapOfMultiZonesDetector = iRoadModel.getModel().getRoadDetectorModel().getMapOfMultiZonesDetector();
		
		roadDetectorHBoxCellsList.clear();
		
		// set all existed detector in list view
		for(Map.Entry<RoadDetectorHBoxCell, List<Detector>> entry : mapOfMultiZonesDetector.entrySet()) {
			roadDetectorHBoxCell = entry.getKey();
			
			roadDetectorHBoxCellsList.add(roadDetectorHBoxCell);
			roadDetectorHBoxCellsObservableList = FXCollections.observableArrayList(roadDetectorHBoxCellsList);			
			listViewDetectors.setItems(roadDetectorHBoxCellsObservableList);
			listViewDetectors.getSelectionModel().selectFirst();			
		}
		
		List<Detector> detectorList = mapOfMultiZonesDetector.get(listViewDetectors.getSelectionModel().getSelectedItem());
		if(detectorList != null) {
			for(Detector detector : detectorList) {
				if(detector.getTypeDetector().equals("Кнопка ТВП") || detector.getTypeDetector().equals("Ультразвук")) {
					fieldsForTVP();
					ObservableList<String> connType = FXCollections.observableArrayList("Сухой контакт", "RS 485");
					choiceBoxConnectionType.setItems(connType);
					
					textFieldModelDetector.setText(detector.getModelDetector());
					textFieldLocationDetector.setText(detector.getLocationDetector());
					textFieldFaultTimeOut.setText(detector.getFaultTimeoutDetector());
					choiceBoxConnectionType.setValue(detector.getConnectionType());
					textFieldSPIAddress.setText(detector.getSpi());
					textFieldResponse.setText(detector.getResponse());
				}else{
					fieldsForCamera();
					fieldsForZones();
					
					if(detector.getTypeDetector().equals("Камера")) {
						ObservableList<String> connType = FXCollections.observableArrayList("Ethernet");
						choiceBoxConnectionType.setItems(connType);
					}else {
						ObservableList<String> connType = FXCollections.observableArrayList("RS 485", "Сухой контакт");
						choiceBoxConnectionType.setItems(connType);
					}
					
					textFieldModelDetector.setText(detector.getModelDetector());
					textFieldLocationDetector.setText(detector.getLocationDetector());
					textFieldFaultTimeOut.setText(detector.getFaultTimeoutDetector());
					choiceBoxConnectionType.setValue(detector.getConnectionType());
					textFieldIPCamera.setText(detector.getIPDetector());
					textFieldPortCamera.setText(detector.getPort());
					textFieldPortXML.setText(detector.getPortXML());
					textFieldPortHTTP.setText(detector.getPortHTTP());
					textFieldPeriodInterrogation.setText(detector.getPeriodInterrogation());
					if(!detector.getPeriodSaving().equals("")) {
						textFieldPeriodSaving.setText(detector.getPeriodSaving());
						checkBoxSave.setSelected(true);
					}
					break;
				}
			}
			for(Detector detector : detectorList) {
				detectorZonesHBoxCell = new DetectorZonesHBoxCell(Integer.toString(listViewZones.getItems().size() + 1));
				detectorZonesHBoxCell.getChoiceBoxTypeOfZone().setValue(detector.getTypeZone());
				detectorZonesHBoxCellsList.add(detectorZonesHBoxCell);
				detectorZonesHBoxCellsObservableList = FXCollections.observableArrayList(detectorZonesHBoxCellsList);
				listViewZones.setItems(detectorZonesHBoxCellsObservableList);
				listViewZones.getSelectionModel().selectFirst();
			}
		}
	}
	
	public void save(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;
		
		mapOfMultiZonesDetector = iRoadModel.getModel().getRoadDetectorModel().getMapOfMultiZonesDetector();
		
		List<Detector> previousDetectorsList = mapOfMultiZonesDetector.get(listViewDetectors.getSelectionModel().getSelectedItem());
		if(previousDetectorsList != null) {
			if(previousDetectorsList.size() == 1) {
				Detector detector = previousDetectorsList.get(0);
				/*detector.setModelDetector(textFieldModelDetector.getText());
				detector.setLocationDetector(textFieldLocationDetector.getText());
				detector.setFaultTimeoutDetector(textFieldFaultTimeOut.getText());
				detector.setResponse(textFieldResponse.getText());
				detector.setConnectionType(choiceBoxConnectionType.getValue());
				detector.setSpi(textFieldSPIAddress.getText());*/
				detector.setModelDetector(textFieldModelDetector.getText());
				detector.setLocationDetector(textFieldLocationDetector.getText());
				detector.setFaultTimeoutDetector(textFieldFaultTimeOut.getText());
				detector.setConnectionType(choiceBoxConnectionType.getValue());
				detector.setIPDetector(textFieldIPCamera.getText());
				detector.setPort(textFieldPortCamera.getText());
				detector.setPortXML(textFieldPortXML.getText());
				detector.setPortHTTP(textFieldPortHTTP.getText());
				detector.setResponse(textFieldResponse.getText());
				detector.setSpi(textFieldSPIAddress.getText());
				detector.setPeriodInterrogation(textFieldPeriodInterrogation.getText());
				detector.setPeriodSaving(textFieldPeriodSaving.getText());
				detector.setRootID(listViewDetectors.getSelectionModel().getSelectedItem().getLabelNumberOfDetector().getText());
			}
			if(previousDetectorsList.size() > 1) {
				String index = listViewZones.getSelectionModel().getSelectedItem().getLabelZoneNumber().getText();
				Detector detector = previousDetectorsList.get(Integer.parseInt(index) - 1);
				detector.setModelDetector(textFieldModelDetector.getText());
				detector.setLocationDetector(textFieldLocationDetector.getText());
				detector.setFaultTimeoutDetector(textFieldFaultTimeOut.getText());
				detector.setConnectionType(choiceBoxConnectionType.getValue());
				detector.setIPDetector(textFieldIPCamera.getText());
				detector.setPort(textFieldPortCamera.getText());
				detector.setPortXML(textFieldPortXML.getText());
				detector.setPortHTTP(textFieldPortHTTP.getText());
				detector.setPeriodInterrogation(textFieldPeriodInterrogation.getText());
				detector.setPeriodSaving(textFieldPeriodSaving.getText());
			}
		}
		generateListOfDetector();
		
	}
	
	public void generateListOfDetector() {
		List<Detector> detectorsList = iRoadModel.getModel().getRoadDetectorModel().getDetectorsList();
		mapOfMultiZonesDetector = iRoadModel.getModel().getRoadDetectorModel().getMapOfMultiZonesDetector();
		
		for(Map.Entry<RoadDetectorHBoxCell, List<Detector>> entry : mapOfMultiZonesDetector.entrySet()) {
			List<Detector> existedDetectorList = entry.getValue();
			for(Detector detector : existedDetectorList) {
				detectorsList.add(detector);
			}
		}
		
		for(Detector detector : detectorsList) {
			detector.setNumberDetector(Integer.toString(detectorsList.indexOf(detector) + 1));
		}		
	}
	
	public void fieldsForCamera() {
		labelSPIAddress.setVisible(false);
		labelResponse.setVisible(false);
		textFieldSPIAddress.setVisible(false);
		textFieldResponse.setVisible(false);
		
		labelConnectionType.setVisible(true);
		choiceBoxConnectionType.setVisible(true);
		choiceBoxConnectionType.setValue("");
		
		labelIPCamera.setVisible(true);
		labelPortCamera.setVisible(true);
		labelPortXML.setVisible(true);
		labelPortHTTP.setVisible(true);
		textFieldIPCamera.setVisible(true);
		textFieldPortCamera.setVisible(true);
		textFieldPortXML.setVisible(true);
		textFieldPortHTTP.setVisible(true);
		labelListZones.setVisible(true);
		listViewZones.setVisible(true);
		buttonCreateZone.setVisible(true);
		buttonDeleteZone.setVisible(true);
		labelZoneSetting.setVisible(true);
		
	}
	
	public void fieldsForTVP() {
		labelSPIAddress.setVisible(true);
		labelResponse.setVisible(true);
		textFieldSPIAddress.setVisible(true);
		textFieldResponse.setVisible(true);
		
		labelConnectionType.setVisible(true);
		choiceBoxConnectionType.setVisible(true);
		choiceBoxConnectionType.setValue("");
		
		labelIPCamera.setVisible(false);
		labelPeriodInterrogation.setVisible(false);
		labelPeriodSaving.setVisible(false);
		labelPortCamera.setVisible(false);
		labelPortXML.setVisible(false);
		labelPortHTTP.setVisible(false);
		textFieldIPCamera.setVisible(false);
		textFieldPeriodInterrogation.setVisible(false);
		textFieldPeriodSaving.setVisible(false);
		textFieldPortCamera.setVisible(false);
		textFieldPortXML.setVisible(false);
		textFieldPortHTTP.setVisible(false);
		labelListZones.setVisible(false);
		listViewZones.setVisible(false);
		checkBoxSave.setVisible(false);
		buttonCreateZone.setVisible(false);
		buttonDeleteZone.setVisible(false);
		labelZoneSetting.setVisible(false);
	}
	
	public void fieldsForZones() {
		labelPeriodInterrogation.setVisible(true);
		labelPeriodSaving.setVisible(true);
		textFieldPeriodInterrogation.setVisible(true);
		textFieldPeriodSaving.setVisible(true);
		checkBoxSave.setVisible(true);
	}
	
	public void fieldsNotVisible() {
		labelSPIAddress.setVisible(false);
		labelResponse.setVisible(false);
		textFieldSPIAddress.setVisible(false);
		textFieldResponse.setVisible(false);
		
		labelConnectionType.setVisible(false);
		choiceBoxConnectionType.setVisible(false);
		
		labelIPCamera.setVisible(false);
		labelPeriodInterrogation.setVisible(false);
		labelPeriodSaving.setVisible(false);
		labelPortCamera.setVisible(false);
		labelPortXML.setVisible(false);
		labelPortHTTP.setVisible(false);
		textFieldIPCamera.setVisible(false);
		textFieldPeriodInterrogation.setVisible(false);
		textFieldPeriodSaving.setVisible(false);
		textFieldPortCamera.setVisible(false);
		textFieldPortXML.setVisible(false);
		textFieldPortHTTP.setVisible(false);
		labelListZones.setVisible(false);
		listViewZones.setVisible(false);
		checkBoxSave.setVisible(false);
		buttonCreateZone.setVisible(false);
		buttonDeleteZone.setVisible(false);
	}
	
	public void allFieldsClear() {
		textFieldModelDetector.clear();
		textFieldLocationDetector.clear();
		textFieldResponse.clear();
		textFieldSPIAddress.clear();
		textFieldFaultTimeOut.clear();
		textFieldIPCamera.clear();
		textFieldPeriodInterrogation.clear();
		textFieldPeriodSaving.clear();
		textFieldPortCamera.clear();
		textFieldPortXML.clear();
		textFieldPortHTTP.clear();
	}
	
	public void createDetector() {
		System.out.println();
		System.out.println("============= Create new detector =============");
		System.out.println();
		
		mapOfMultiZonesDetector = iRoadModel.getModel().getRoadDetectorModel().getMapOfMultiZonesDetector();
		
		if(previousIndex != -1) {
			List<Detector> detectorsList = mapOfMultiZonesDetector.get(listViewDetectors.getSelectionModel().getSelectedItem());
			if(detectorsList != null) {
				if(detectorsList.size() == 1) {
					Detector detector = detectorsList.get(0);
					/*detector.setModelDetector(textFieldModelDetector.getText());
					detector.setLocationDetector(textFieldLocationDetector.getText());
					detector.setFaultTimeoutDetector(textFieldFaultTimeOut.getText());
					detector.setResponse(textFieldResponse.getText());
					detector.setConnectionType(choiceBoxConnectionType.getValue());
					detector.setSpi(textFieldSPIAddress.getText());
					detector.setRootID(listViewDetectors.getSelectionModel().getSelectedItem().getLabelNumberOfDetector().getText());*/
					detector.setModelDetector(textFieldModelDetector.getText());
					detector.setLocationDetector(textFieldLocationDetector.getText());
					detector.setFaultTimeoutDetector(textFieldFaultTimeOut.getText());
					detector.setConnectionType(choiceBoxConnectionType.getValue());
					detector.setIPDetector(textFieldIPCamera.getText());
					detector.setPort(textFieldPortCamera.getText());
					detector.setPortXML(textFieldPortXML.getText());
					detector.setPortHTTP(textFieldPortHTTP.getText());
					detector.setResponse(textFieldResponse.getText());
					detector.setSpi(textFieldSPIAddress.getText());
					detector.setPeriodInterrogation(textFieldPeriodInterrogation.getText());
					detector.setPeriodSaving(textFieldPeriodSaving.getText());
					detector.setRootID(listViewDetectors.getSelectionModel().getSelectedItem().getLabelNumberOfDetector().getText());
				}
				if(detectorsList.size() > 1) {
					previousIndex = listViewZones.getSelectionModel().getSelectedIndex();
					Detector detector = detectorsList.get(previousIndex);
					detector.setModelDetector(textFieldModelDetector.getText());
					detector.setLocationDetector(textFieldLocationDetector.getText());
					detector.setFaultTimeoutDetector(textFieldFaultTimeOut.getText());
					detector.setConnectionType(choiceBoxConnectionType.getValue());
					detector.setIPDetector(textFieldIPCamera.getText());
					detector.setPort(textFieldPortCamera.getText());
					detector.setPortXML(textFieldPortXML.getText());
					detector.setPortHTTP(textFieldPortHTTP.getText());
					detector.setResponse(textFieldResponse.getText());
					detector.setSpi(textFieldSPIAddress.getText());
					detector.setPeriodInterrogation(textFieldPeriodInterrogation.getText());
					detector.setPeriodSaving(textFieldPeriodSaving.getText());
					detector.setRootID(listViewDetectors.getSelectionModel().getSelectedItem().getLabelNumberOfDetector().getText());
				}
			}
		}
		
		for(RoadDetectorHBoxCell existedRoadDetectorHBoxCel : roadDetectorHBoxCellsList) {
			existedRoadDetectorHBoxCel.getChoiceBoxTypeOfDetector().setDisable(true);
		}
		
		allFieldsClear();
		fieldsNotVisible();
		listViewZones.getItems().clear();
		detectorZonesHBoxCellsList.clear();
		
		roadDetectorHBoxCell = new RoadDetectorHBoxCell(String.valueOf(listViewDetectors.getItems().size() + 1));
		roadDetectorHBoxCellsList.add(roadDetectorHBoxCell);
		roadDetectorHBoxCellsObservableList = FXCollections.observableArrayList(roadDetectorHBoxCellsList);
		listViewDetectors.setItems(roadDetectorHBoxCellsObservableList);
		listViewDetectors.getSelectionModel().select(listViewDetectors.getItems().size() -1);
		roadDetectorHBoxCell.getChoiceBoxTypeOfDetector().setDisable(false);
		
		roadDetectorHBoxCell.getChoiceBoxTypeOfDetector().getSelectionModel().selectedItemProperty().addListener((observableType, oldValueType, newValueType) -> {
			if(newValueType.equals("Кнопка ТВП") || newValueType.equals("Ультразвук")) {
				fieldsForTVP();
				
				ObservableList<String> connType = FXCollections.observableArrayList("Сухой контакт", "RS 485");
				choiceBoxConnectionType.setItems(connType);
				
				detector = new Detector();
				detector.setTypeDetector(newValueType);
				
				createdDetectorList = new ArrayList<>();
				
				createdDetectorList.add(detector);
				mapOfMultiZonesDetector.put(roadDetectorHBoxCell, createdDetectorList);
			}else{
				fieldsForCamera();
				
				if(newValueType.equals("Камера")) {
					ObservableList<String> connType = FXCollections.observableArrayList("Ethernet");
					choiceBoxConnectionType.setItems(connType);
				}else {
					ObservableList<String> connType = FXCollections.observableArrayList("RS 485", "Сухой контакт");
					choiceBoxConnectionType.setItems(connType);
				}
				
				createdDetectorList = new ArrayList<>();
				
				mapOfMultiZonesDetector.put(roadDetectorHBoxCell, createdDetectorList);
				
			}
		});
	}
	
	public void deleteDetector() {
		System.out.println();
		System.out.println("============== Delete detector =================");
		System.out.println();
		
		mapOfMultiZonesDetector = iRoadModel.getModel().getRoadDetectorModel().getMapOfMultiZonesDetector();
		RoadDetectorHBoxCell roadDetectorToDelete = listViewDetectors.getSelectionModel().getSelectedItem();
		int indexToDelete = listViewDetectors.getSelectionModel().getSelectedIndex();
		
		if(indexToDelete >= 0) {
			listViewDetectors.getItems().remove(indexToDelete);
			mapOfMultiZonesDetector.remove(roadDetectorToDelete);
			roadDetectorHBoxCellsList.remove(roadDetectorToDelete);
			
			for(int i = 1; i <= listViewDetectors.getItems().size(); i++) {
				listViewDetectors.getItems().get(i - 1).getLabelNumberOfDetector().setText(Integer.toString(i));
			}
			textFieldModelDetector.clear();
			textFieldLocationDetector.clear();
			textFieldFaultTimeOut.clear();
			detectorZonesHBoxCellsList.clear();
			
			List<Detector> detectorList = mapOfMultiZonesDetector.get(listViewDetectors.getSelectionModel().getSelectedItem());
			if(detectorList != null) {
				for(Detector detector : detectorList) {
					if(detector.getTypeDetector().equals("Кнопка ТВП") || detector.getTypeDetector().equals("Ультразвук")) {
						fieldsForTVP();
						ObservableList<String> connType = FXCollections.observableArrayList("Сухой контакт", "RS 485");
						choiceBoxConnectionType.setItems(connType);
						
						textFieldModelDetector.setText(detector.getModelDetector());
						textFieldLocationDetector.setText(detector.getLocationDetector());
						textFieldFaultTimeOut.setText(detector.getFaultTimeoutDetector());
						choiceBoxConnectionType.setValue(detector.getConnectionType());
						textFieldSPIAddress.setText(detector.getSpi());
						textFieldResponse.setText(detector.getResponse());
					}else{
						fieldsForCamera();
						fieldsForZones();
						
						if(detector.getTypeDetector().equals("Камера")) {
							ObservableList<String> connType = FXCollections.observableArrayList("Ethernet");
							choiceBoxConnectionType.setItems(connType);
						}else {
							ObservableList<String> connType = FXCollections.observableArrayList("RS 485", "Сухой контакт");
							choiceBoxConnectionType.setItems(connType);
						}
						
						textFieldModelDetector.setText(detector.getModelDetector());
						textFieldLocationDetector.setText(detector.getLocationDetector());
						textFieldFaultTimeOut.setText(detector.getFaultTimeoutDetector());
						choiceBoxConnectionType.setValue(detector.getConnectionType());
						textFieldIPCamera.setText(detector.getIPDetector());
						textFieldPortCamera.setText(detector.getPort());
						textFieldPortXML.setText(detector.getPortXML());
						textFieldPortHTTP.setText(detector.getPortHTTP());
						textFieldPeriodInterrogation.setText(detector.getPeriodInterrogation());
						if(!detector.getPeriodSaving().equals("")) {
							textFieldPeriodSaving.setText(detector.getPeriodSaving());
							checkBoxSave.setSelected(true);
						}
						break;
					}
					
				}
				for(Detector detector : detectorList) {
					detectorZonesHBoxCell = new DetectorZonesHBoxCell(Integer.toString(listViewZones.getItems().size() + 1));
					detectorZonesHBoxCell.getChoiceBoxTypeOfZone().setValue(detector.getTypeZone());
					detectorZonesHBoxCellsList.add(detectorZonesHBoxCell);
					detectorZonesHBoxCellsObservableList = FXCollections.observableArrayList(detectorZonesHBoxCellsList);
					listViewZones.setItems(detectorZonesHBoxCellsObservableList);
					listViewZones.getSelectionModel().selectFirst();
				}
			}
			
		}else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Выбирите элемент для удаления");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.showAndWait();
		}
	}
	
	public void createZone() {
		mapOfMultiZonesDetector = iRoadModel.getModel().getRoadDetectorModel().getMapOfMultiZonesDetector();
		
		List<Detector> detectorsList = mapOfMultiZonesDetector.get(listViewDetectors.getSelectionModel().getSelectedItem());
		if(detectorsList.size() != 0) {
			Detector detector = detectorsList.get(listViewZones.getSelectionModel().getSelectedIndex());
			detector.setPeriodInterrogation(textFieldPeriodInterrogation.getText());
			detector.setPeriodSaving(textFieldPeriodSaving.getText());
		}
		
		textFieldPeriodInterrogation.clear();
		textFieldPeriodSaving.clear();
		textFieldPeriodSaving.setDisable(true);
		checkBoxSave.setSelected(false);
		
		for(DetectorZonesHBoxCell existedDetectorZonesHBoxCell : detectorZonesHBoxCellsList) {
			existedDetectorZonesHBoxCell.getChoiceBoxTypeOfZone().setDisable(true);
		}
		
		detectorZonesHBoxCell = new DetectorZonesHBoxCell(Integer.toString(listViewZones.getItems().size() + 1));
		detectorZonesHBoxCellsList.add(detectorZonesHBoxCell);
		detectorZonesHBoxCellsObservableList = FXCollections.observableArrayList(detectorZonesHBoxCellsList);
		listViewZones.setItems(detectorZonesHBoxCellsObservableList);
		detectorZonesHBoxCell.getChoiceBoxTypeOfZone().setDisable(false);
		listViewZones.getSelectionModel().selectLast();
		
		detector = new Detector();
		
		detectorZonesHBoxCell.getChoiceBoxTypeOfZone().getSelectionModel().selectedItemProperty().addListener((observableTypeZone, oldValueTypeZone, newValueTypeZone) -> {
			if(!newValueTypeZone.equals("")) {
				detector.setTypeZone(newValueTypeZone);
			}
		});
																
		detector.setTypeDetector(listViewDetectors.getSelectionModel().getSelectedItem().getChoiceBoxTypeOfDetector().getValue());							
		detector.setModelDetector(textFieldModelDetector.getText());
		detector.setLocationDetector(textFieldLocationDetector.getText());
		detector.setFaultTimeoutDetector(textFieldFaultTimeOut.getText());
		detector.setConnectionType(choiceBoxConnectionType.getValue());
		detector.setIPDetector(textFieldIPCamera.getText());
		detector.setPort(textFieldPortCamera.getText());
		detector.setPortXML(textFieldPortXML.getText());
		detector.setPortHTTP(textFieldPortHTTP.getText());
		detector.setRootID(listViewDetectors.getSelectionModel().getSelectedItem().getLabelNumberOfDetector().getText());
		
		mapOfMultiZonesDetector.get(listViewDetectors.getSelectionModel().getSelectedItem()).add(detector);
		
		fieldsForZones();
	}
	
	public void deleteZone() {
		System.out.println();
		System.out.println("================ Delete zone ================");
		System.out.println();
		
		mapOfMultiZonesDetector = iRoadModel.getModel().getRoadDetectorModel().getMapOfMultiZonesDetector();
		
		int indexForDelete = listViewZones.getSelectionModel().getSelectedIndex();
		DetectorZonesHBoxCell detectorZonesToDelete = listViewZones.getSelectionModel().getSelectedItem();
		if(indexForDelete >= 0) {
			listViewZones.getItems().remove(indexForDelete);
			mapOfMultiZonesDetector.get(listViewDetectors.getSelectionModel().getSelectedItem()).remove(indexForDelete);
			detectorZonesHBoxCellsList.remove(detectorZonesToDelete);
			listViewZones.getSelectionModel().selectFirst();
			
			List<Detector> detectorList = mapOfMultiZonesDetector.get(listViewDetectors.getSelectionModel().getSelectedItem());
			if(detectorList != null) {
				for(Detector detector : detectorList) {
					if(detector.getTypeDetector().equals("Кнопка ТВП") || detector.getTypeDetector().equals("Ультразвук")) {
						fieldsForTVP();
						ObservableList<String> connType = FXCollections.observableArrayList("Сухой контакт", "RS 485");
						choiceBoxConnectionType.setItems(connType);
						
						textFieldModelDetector.setText(detector.getModelDetector());
						textFieldLocationDetector.setText(detector.getLocationDetector());
						textFieldFaultTimeOut.setText(detector.getFaultTimeoutDetector());
						choiceBoxConnectionType.setValue(detector.getConnectionType());
						textFieldSPIAddress.setText(detector.getSpi());
						textFieldResponse.setText(detector.getResponse());
					}else{
						fieldsForCamera();
						fieldsForZones();
						
						if(detector.getTypeDetector().equals("Камера")) {
							ObservableList<String> connType = FXCollections.observableArrayList("Ethernet");
							choiceBoxConnectionType.setItems(connType);
						}else {
							ObservableList<String> connType = FXCollections.observableArrayList("RS 485", "Сухой контакт");
							choiceBoxConnectionType.setItems(connType);
						}
						
						textFieldModelDetector.setText(detector.getModelDetector());
						textFieldLocationDetector.setText(detector.getLocationDetector());
						textFieldFaultTimeOut.setText(detector.getFaultTimeoutDetector());
						choiceBoxConnectionType.setValue(detector.getConnectionType());
						textFieldIPCamera.setText(detector.getIPDetector());
						textFieldPortCamera.setText(detector.getPort());
						textFieldPortXML.setText(detector.getPortXML());
						textFieldPortHTTP.setText(detector.getPortHTTP());
						textFieldPeriodInterrogation.setText(detector.getPeriodInterrogation());
						if(!detector.getPeriodSaving().equals("")) {
							textFieldPeriodSaving.setText(detector.getPeriodSaving());
							checkBoxSave.setSelected(true);
						}
						break;
					}					
				}			
			}
			for(int i = 1; i <= listViewZones.getItems().size(); i++) {
				listViewZones.getItems().get(i - 1).getLabelZoneNumber().setText(Integer.toString(i));
			}
		}else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Выбирите элемент для удаления");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.showAndWait();
		}
	}
	
	public void selectDetector() {
		mapOfMultiZonesDetector = iRoadModel.getModel().getRoadDetectorModel().getMapOfMultiZonesDetector();
		
		List<Detector> previousDetectorsList = mapOfMultiZonesDetector.get(previousRoadDetectorHBoxCell);
		if(previousDetectorsList != null) {
			if(previousDetectorsList.size() == 1) {
				Detector detector = previousDetectorsList.get(0);
				/*detector.setModelDetector(textFieldModelDetector.getText());
				detector.setLocationDetector(textFieldLocationDetector.getText());
				detector.setFaultTimeoutDetector(textFieldFaultTimeOut.getText());
				detector.setResponse(textFieldResponse.getText());
				detector.setConnectionType(choiceBoxConnectionType.getValue());
				detector.setSpi(textFieldSPIAddress.getText());*/
				detector.setModelDetector(textFieldModelDetector.getText());
				detector.setLocationDetector(textFieldLocationDetector.getText());
				detector.setFaultTimeoutDetector(textFieldFaultTimeOut.getText());
				detector.setConnectionType(choiceBoxConnectionType.getValue());
				detector.setIPDetector(textFieldIPCamera.getText());
				detector.setPort(textFieldPortCamera.getText());
				detector.setPortXML(textFieldPortXML.getText());
				detector.setPortHTTP(textFieldPortHTTP.getText());
				detector.setResponse(textFieldResponse.getText());
				detector.setSpi(textFieldSPIAddress.getText());
				detector.setPeriodInterrogation(textFieldPeriodInterrogation.getText());
				detector.setPeriodSaving(textFieldPeriodSaving.getText());
				detector.setRootID(listViewDetectors.getSelectionModel().getSelectedItem().getLabelNumberOfDetector().getText());
			}
			if(previousDetectorsList.size() > 1) {
				String index = listViewZones.getSelectionModel().getSelectedItem().getLabelZoneNumber().getText();
				Detector detector = previousDetectorsList.get(Integer.parseInt(index) - 1);
				detector.setModelDetector(textFieldModelDetector.getText());
				detector.setLocationDetector(textFieldLocationDetector.getText());
				detector.setFaultTimeoutDetector(textFieldFaultTimeOut.getText());
				detector.setConnectionType(choiceBoxConnectionType.getValue());
				detector.setIPDetector(textFieldIPCamera.getText());
				detector.setPort(textFieldPortCamera.getText());
				detector.setPortXML(textFieldPortXML.getText());
				detector.setPortHTTP(textFieldPortHTTP.getText());
				detector.setPeriodInterrogation(textFieldPeriodInterrogation.getText());
				detector.setPeriodSaving(textFieldPeriodSaving.getText());
			}
		}
		checkBoxSave.setSelected(false);
		textFieldPeriodSaving.setDisable(true);
		
		allFieldsClear();
		RoadDetectorHBoxCell roadDetectorHBoxCell = listViewDetectors.getSelectionModel().getSelectedItem();
		
		List<Detector> detectorsList = mapOfMultiZonesDetector.get(roadDetectorHBoxCell);
		if(detectorsList != null) {
			for(Detector detector : detectorsList) {
				String typeDetector = detector.getTypeDetector();
				if(typeDetector.equals("Кнопка ТВП") || typeDetector.equals("Ультразвук")) {
					fieldsForTVP();
					ObservableList<String> connType = FXCollections.observableArrayList("Сухой контакт", "RS 485");
					choiceBoxConnectionType.setItems(connType);
					
					textFieldModelDetector.setText(detector.getModelDetector());
					textFieldLocationDetector.setText(detector.getLocationDetector());
					textFieldFaultTimeOut.setText(detector.getFaultTimeoutDetector());
					choiceBoxConnectionType.setValue(detector.getConnectionType());
					textFieldSPIAddress.setText(detector.getSpi());
					textFieldResponse.setText(detector.getResponse());
				}else{
					fieldsForCamera();
					fieldsForZones();
					
					if(typeDetector.equals("Камера")) {
						ObservableList<String> connType = FXCollections.observableArrayList("Ethernet");
						choiceBoxConnectionType.setItems(connType);
					}else {
						ObservableList<String> connType = FXCollections.observableArrayList("RS 485", "Сухой контакт");
						choiceBoxConnectionType.setItems(connType);
					}
					
					textFieldModelDetector.setText(detector.getModelDetector());
					textFieldLocationDetector.setText(detector.getLocationDetector());
					textFieldFaultTimeOut.setText(detector.getFaultTimeoutDetector());
					choiceBoxConnectionType.setValue(detector.getConnectionType());
					textFieldIPCamera.setText(detector.getIPDetector());
					textFieldPortCamera.setText(detector.getPort());
					textFieldPortXML.setText(detector.getPortXML());
					textFieldPortHTTP.setText(detector.getPortHTTP());
					textFieldPeriodInterrogation.setText(detector.getPeriodInterrogation());
					if(!detector.getPeriodSaving().equals("")) {
						textFieldPeriodSaving.setText(detector.getPeriodSaving());
						checkBoxSave.setSelected(true);
					}
					break;
				}
			}
		}
		if(detectorsList.isEmpty()) {
			if(listViewDetectors.getSelectionModel().getSelectedItem().getChoiceBoxTypeOfDetector().getValue().equals("Камера")) {
				ObservableList<String> connType = FXCollections.observableArrayList("Ethernet");
				choiceBoxConnectionType.setItems(connType);
			}else {
				ObservableList<String> connType = FXCollections.observableArrayList("RS 485", "Сухой контакт");
				choiceBoxConnectionType.setItems(connType);
			}
			fieldsForCamera();
			fieldsForZones();
			allFieldsClear();
			
		}
		
		listViewZones.getItems().clear();
		detectorZonesHBoxCellsList.clear();
		
		for(DetectorZonesHBoxCell detectorZonesHBoxCell : detectorZonesHBoxCellsList) {
			detectorZonesHBoxCell.getChoiceBoxTypeOfZone().setDisable(true);
		}
		
		if(detectorsList != null) {
			for(Detector detector : detectorsList) {
				detectorZonesHBoxCell = new DetectorZonesHBoxCell(Integer.toString(listViewZones.getItems().size() + 1));
				detectorZonesHBoxCell.getChoiceBoxTypeOfZone().setValue(detector.getTypeZone());
				detectorZonesHBoxCellsList.add(detectorZonesHBoxCell);
				detectorZonesHBoxCellsObservableList = FXCollections.observableArrayList(detectorZonesHBoxCellsList);
				listViewZones.setItems(detectorZonesHBoxCellsObservableList);
				listViewZones.getSelectionModel().selectFirst();
			}
		}
	}
	
	public void selectZone() {
		mapOfMultiZonesDetector = iRoadModel.getModel().getRoadDetectorModel().getMapOfMultiZonesDetector();		
		if(previousIndex != -1) {
			List<Detector> detectorsList = mapOfMultiZonesDetector.get(listViewDetectors.getSelectionModel().getSelectedItem());
			detectorsList.get(previousIndex).setPeriodInterrogation(textFieldPeriodInterrogation.getText());
			detectorsList.get(previousIndex).setPeriodSaving(textFieldPeriodSaving.getText());
			
			Detector detector = mapOfMultiZonesDetector.get(listViewDetectors.getSelectionModel().getSelectedItem()).get(listViewZones.getSelectionModel().getSelectedIndex());
			if(detector.getPeriodSaving().equals("")) {
				checkBoxSave.setSelected(false);
				textFieldPeriodSaving.clear();
				textFieldPeriodSaving.setDisable(true);
			}else {
				checkBoxSave.setSelected(true);
				textFieldPeriodSaving.setDisable(false);
				textFieldPeriodSaving.setText(detector.getPeriodSaving());
			}
			if(!detector.getPeriodInterrogation().equals("")) {
				textFieldPeriodInterrogation.setText(detector.getPeriodInterrogation());
			}else {
				textFieldPeriodInterrogation.clear();
			}
		}
	}
	
	private String makePartialIPRegex() {
        String partialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))" ;
        String subsequentPartialBlock = "(\\."+partialBlock+")" ;
        String ipAddress = partialBlock+"?"+subsequentPartialBlock+"{0,3}";
        return "^"+ipAddress ;
    }
	
	@FXML
    public void initialize() {
		
		listViewDetectors.setPlaceholder(new Label("Нет данных для отображения"));
		listViewZones.setPlaceholder(new Label("Нет данных для отображения"));
		
		String regex = makePartialIPRegex();
        final UnaryOperator<Change> ipAddressFilter = c -> {
            String text = c.getControlNewText();
            if  (text.matches(regex)) {
                return c ;
            } else {
                return null ;
            }
        };
        textFieldIPCamera.setTextFormatter(new TextFormatter<>(ipAddressFilter));
		
		listViewDetectors.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				for(RoadDetectorHBoxCell existedRoadDetectorHBoxCel : roadDetectorHBoxCellsList) {
					existedRoadDetectorHBoxCel.getChoiceBoxTypeOfDetector().setDisable(true);
				}
				newValue.getChoiceBoxTypeOfDetector().setDisable(false);
				previousRoadDetectorHBoxCell = oldValue;
			}
		});
		
		checkBoxSave.selectedProperty().addListener((observableSelect, oldValueSelect, newValueSelect) -> {
			if(newValueSelect == true) {
				textFieldPeriodSaving.setDisable(false);
			}else {
				textFieldPeriodSaving.clear();
				textFieldPeriodSaving.setDisable(true);
			}
		});	
		
		listViewZones.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			previousIndex = oldValue.intValue();
		});
		listViewZones.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				for(DetectorZonesHBoxCell detectorZonesHBoxCell : detectorZonesHBoxCellsList) {
					detectorZonesHBoxCell.getChoiceBoxTypeOfZone().setDisable(true);
				}
				newValue.getChoiceBoxTypeOfZone().setDisable(false);
				previousDetectorZonesHBoxCell = oldValue;
			}
		});
		
		textFieldPeriodInterrogation.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldPeriodInterrogation.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
		
		/*textFieldPeriodSaving.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldPeriodSaving.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });*/
		
		textFieldPortCamera.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldPortCamera.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
		
		textFieldPortXML.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldPortXML.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
		
		textFieldPortHTTP.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldPortHTTP.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
		
		textFieldFaultTimeOut.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldFaultTimeOut.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
		
		textFieldResponse.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldResponse.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
		
		textFieldSPIAddress.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldSPIAddress.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
		
		
		
		/*textFieldIPCamera.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldIPCamera.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });*/
				
	}

}
