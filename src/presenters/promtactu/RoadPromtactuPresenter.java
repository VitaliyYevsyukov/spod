package presenters.promtactu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import presenters.directions.DirectionBasePromtactPresenter;
import presenters.directions.RoadDirection;
import presenters.phase.OpenDirectionInCurrentPhaseHBoxCell;
import presenters.phase.RoadPhase;
import roadModel.IRoadModel;

/**
 * Created by Vitaly on 06.12.2016.
 */
public class RoadPromtactuPresenter {

	IRoadModel iRoadModel;

	@FXML
	private TextField textField_EndGreenAddit, textField_EndGreenBlink, textField_EndYellow, textField_EndRed, textField_EndRedYellow;
	@FXML
	private Label labelPromtactu, labelPromtactControl, labelInterphaseTransitions, labelDirectionsFromThesePhases, labelPromtactForDirectionN, labelDuringTheTransitionFromPhaseN, labelToPhaseN, labelEndGreenAddit, labelEndGreenBlind, labelEndYellow,
			labelEndRed, labelEndRedYellow;
	@FXML
	private Button buttonCreateInterphaseTransitions, buttonDeleteInterphaseTransitions, buttonGenerateInterphaseTransitions, buttonIndustrialTable, buttonEstablishABasic, buttonCreatePhaseDirection;
	@FXML
	private CheckBox checkBoxFullPromtact;
	@FXML
	private ListView<InterphaseTransitionsHBoxCell> listViewInterphase;
	@FXML
	private ListView<PhaseDirectionsHBoxCell> listViewPhaseDirections;
	@FXML
	private Label labelPromtactForDirection, labelInterPhaseFrom, labelInterPhaseTo;

	@FXML
	//private ResourceBundle bundleGUI, bundleAlert;
	//private Locale localeGUI, localeAlert;
	//String langXML = null;

	int previousIndex;
	String previousDirection;

	InterphaseTransitionsHBoxCell interphaseTransitionsHBoxCell;
	InterphaseTransitionsHBoxCell previousInterphaseTransitionsHBoxCell;
	PhaseDirectionsHBoxCell phaseDirectionsHBoxCell;
	PromtactData promtactData;

	List<InterphaseTransitionsHBoxCell> interphaseTransitionsHBoxCellList = new ArrayList<>();
	List<PhaseDirectionsHBoxCell> phaseDirectionsHBoxCellList = new ArrayList<>();
	List<RoadPhase> roadPhaseList;
	List<RoadDirection> roadDirectionList;

	ObservableList<InterphaseTransitionsHBoxCell> interphaseTransitionsHBoxCellObservableList;
	ObservableList<PhaseDirectionsHBoxCell> phaseDirectionsHBoxCellObservableList;
	ObservableList<String> observableListPhaseNumberFrom = FXCollections.observableArrayList();
	ObservableList<String> observableListPhaseNumberTo = FXCollections.observableArrayList();
	
	ObservableList<String> observableListDirectionsNumbers = FXCollections.observableArrayList();	// directions which change state
	ObservableList<String> observableListDirectionNotChangeState = FXCollections.observableArrayList();	// directions which not change state

	Map<String, List<String>> interPhaseMap;
	Map<String, PromtactData> specificPromtactDataMap;
	Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOfOpenDirInPhase;
	Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfDirectionSpecificPromtact;

	List<String> phaseNumberFrom = new ArrayList<>();
	ObservableList<Integer> toIntegerFrom = FXCollections.observableArrayList();
	ObservableList<String> afterSortToStringFrom = FXCollections.observableArrayList();

	/////////////////////////////////////////////////////////////////////
	////////// See which language is installed in the file //////////////
	/////////////////////////////////////////////////////////////////////
	/*private void langXML() {
		String filepath = System.getProperty("user.dir") +"\\" + "configuration.xml";
		File xmlFile = new File(filepath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			document.getDocumentElement().normalize();

			NodeList nodeList = document.getElementsByTagName("lang");
			Node node = nodeList.item(0);
			langXML = node.getTextContent();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}*/

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////// LOCALE /////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*private void loadLang(String lang) {
		localeGUI = new Locale(lang);
		bundleGUI = ResourceBundle.getBundle("bundle.locale_lang", localeGUI);

		labelPromtactu.setText(bundleGUI.getString("labelPromtactu"));
		labelPromtactControl.setText(bundleGUI.getString("labelPromtactControl"));
		labelInterphaseTransitions.setText(bundleGUI.getString("labelInterphaseTransitions"));
		labelDirectionsFromThesePhases.setText(bundleGUI.getString("labelDirectionsFromThesePhases"));
		labelPromtactForDirectionN.setText(bundleGUI.getString("labelPromtactForDirectionN"));
		labelDuringTheTransitionFromPhaseN.setText(bundleGUI.getString("labelDuringTheTransitionFromPhaseN"));
		labelToPhaseN.setText(bundleGUI.getString("labelToPhaseN"));
		labelEndGreenAddit.setText(bundleGUI.getString("labelEndGreenAddit"));
		labelEndGreenBlind.setText(bundleGUI.getString("labelEndGreenBlind"));
		labelEndYellow.setText(bundleGUI.getString("labelEndYellow"));
		labelEndRed.setText(bundleGUI.getString("labelEndRed"));
		labelEndRedYellow.setText(bundleGUI.getString("labelEndRedYellow"));
		checkBoxFullPromtact.setText(bundleGUI.getString("checkBoxFullPromtact"));
		buttonCreateInterphaseTransitions.setText(bundleGUI.getString("buttonCreate"));
		buttonDeleteInterphaseTransitions.setText(bundleGUI.getString("buttonDelete"));
		buttonGenerateInterphaseTransitions.setText(bundleGUI.getString("buttonGenerate"));
		buttonIndustrialTable.setText(bundleGUI.getString("buttonIndustrialTable"));
		buttonEstablishABasic.setText(bundleGUI.getString("buttonEstablishABasic"));
	}*/

	public void show(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;
		interPhaseMap = iRoadModel.getModel().getRoadPromtactuModel().getInterPhaseMap();
		
		visibleField();
		
		roadPhaseList = iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList();
		roadDirectionList = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();
		
		Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> fullMap = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
				
		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		mapOfOpenDirInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();

		interphaseTransitionsHBoxCellList.clear();
		
		for(Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : fullMap.entrySet()) {
			InterphaseTransitionsHBoxCell interphaseTransitionsHBoxCell = entry.getKey();
			
			if(interphaseTransitionsHBoxCell.getComboBoxFromPhase().getValue() == null || 
					interphaseTransitionsHBoxCell.getComboBoxToPhase().getValue() == null) {
				mapOfDirectionSpecificPromtact.remove(interphaseTransitionsHBoxCell);
			}
			
		}
		
		System.out.println(mapOfDirectionSpecificPromtact);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////// add non-repeating phase numbers to the list //////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()) {
			String entryNumberFrom = entry.getKey().getComboBoxFromPhase().getValue();
			if(entryNumberFrom != null) {
			
				if (!phaseNumberFrom.isEmpty()) {
					boolean write = true;
					for (String existedNumber : phaseNumberFrom) {
						if (entryNumberFrom.equals(existedNumber)) {
							write = false;
						}
					}
					if (write) {
						phaseNumberFrom.add(entryNumberFrom);
					}
				} else {
					phaseNumberFrom.add(entryNumberFrom);
				}
			
			}
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//////////////////////////////////////////////////////////
		/////////////////////// SORT /////////////////////////////
		//////////////////////////////////////////////////////////
		for (String sortNumber : phaseNumberFrom) {
			toIntegerFrom.add(Integer.parseInt(sortNumber));
		}
		Collections.sort(toIntegerFrom);
		for (Integer number : toIntegerFrom) {
			afterSortToStringFrom.add(number.toString());
		}
		//////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////

		if (interphaseTransitionsHBoxCell != null) {
			interphaseTransitionsHBoxCellObservableList.clear();
			listViewInterphase.getItems().clear();
		}
		
		for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()) {
			String from = entry.getKey().getComboBoxFromPhase().getValue();
			String to = entry.getKey().getComboBoxToPhase().getValue();
			
			if(!interPhaseMap.containsKey(from)) {
				List<String> listTo = new ArrayList<>();
				listTo.add(to);
				interPhaseMap.put(from, listTo);
			}else {
				interPhaseMap.get(from).add(to);
			}
			
		}
		

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////// add item in sorted order ///////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (String phaseNumberFrom : afterSortToStringFrom) {
			for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()) {
				
				//String from = entry.getKey().getComboBoxFromPhase().getValue();
				//String to = entry.getKey().getComboBoxToPhase().getValue();
				
				/*if(!interPhaseMap.containsKey(from)) {
					List<String> listTo = new ArrayList<>();
					listTo.add(to);
					interPhaseMap.put(from, listTo);
				}else {
					interPhaseMap.get(from).add(to);
				}*/
				
				
				String numberFrom = entry.getKey().getComboBoxFromPhase().getValue();
				if (phaseNumberFrom.equals(numberFrom)) { // sort by first value from combo box

					if (interphaseTransitionsHBoxCellList.size() == 0) { // add interphaseHBoxCell if list is empty
						interphaseTransitionsHBoxCellList.add(entry.getKey());
					} else {
						for (InterphaseTransitionsHBoxCell existedInterphase : interphaseTransitionsHBoxCellList) { // sort by second value from combo box
							if (numberFrom.equals(existedInterphase.getComboBoxFromPhase().getValue())) {
								int numberToFromList = Integer.parseInt(existedInterphase.getComboBoxToPhase().getValue());
								int numberFromList = 0;
								if(entry.getKey().getComboBoxToPhase().getValue() != null) {
									numberFromList = Integer.parseInt(entry.getKey().getComboBoxToPhase().getValue());
								}
								
								//int numberFromList = Integer.parseInt(entry.getKey().getComboBoxToPhase().getValue());
								if (numberToFromList > numberFromList) { // add after existed interphase
									int index = interphaseTransitionsHBoxCellList.indexOf(existedInterphase);
									interphaseTransitionsHBoxCellList.add(index, entry.getKey());
									break;
								}
							} else if (Integer.parseInt(entry.getKey().getComboBoxFromPhase().getValue()) < Integer.parseInt(existedInterphase.getComboBoxFromPhase().getValue())) { // add before existed interphase
								int index = interphaseTransitionsHBoxCellList.indexOf(existedInterphase);
								interphaseTransitionsHBoxCellList.add(index, entry.getKey());
								break;
							}
							if (interphaseTransitionsHBoxCellList.indexOf(existedInterphase) == interphaseTransitionsHBoxCellList.size() - 1) { // add to last index
								interphaseTransitionsHBoxCellList.add(entry.getKey());
								break;
							}
						}
					}
				}
			}
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		interphaseTransitionsHBoxCellObservableList = FXCollections.observableArrayList(interphaseTransitionsHBoxCellList);
		listViewInterphase.setItems(interphaseTransitionsHBoxCellObservableList);
		listViewInterphase.getSelectionModel().selectFirst();

		
		////////////////////////////////////////////////////////////////////////////
		///////////////// add all existed direction from interphase ////////////////
		////////////////////////////////////////////////////////////////////////////
		
		
		// sort all promtactdata map in interphase map 
		Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> unsortedInterPhaseMap = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		Map<InterphaseTransitionsHBoxCell, Map<Integer, PromtactData>> sort = new LinkedHashMap<>();
		Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> sortedInterPhaseMap = new LinkedHashMap<>();
		
		for(Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> existEntry : unsortedInterPhaseMap.entrySet()) {
			
			InterphaseTransitionsHBoxCell interPhase = existEntry.getKey();
			Map<String, PromtactData> promtactDataMap = existEntry.getValue();
			
			Map<Integer, PromtactData> sortedMap = new TreeMap<Integer, PromtactData>();	// tree map for sort order	
			
			for(Map.Entry<String, PromtactData> entry : promtactDataMap.entrySet()) {
				
				Integer direction = Integer.parseInt(entry.getKey());	// convert direction number from string to int
				PromtactData promtactData = entry.getValue();
				
				sortedMap.put(direction, promtactData);				
			}
			sort.put(interPhase, sortedMap);
		}
		
		for(Map.Entry<InterphaseTransitionsHBoxCell, Map<Integer, PromtactData>> entry : sort.entrySet()) { 	// convert all direction from int to string for interphase 
			
			InterphaseTransitionsHBoxCell interPhase = entry.getKey();
			Map<Integer, PromtactData> promtactDataMap = entry.getValue();
			
			Map<String, PromtactData> sortInterPhase = new LinkedHashMap<>();
			
			for(Map.Entry<Integer, PromtactData> entryData : promtactDataMap.entrySet()) {
				
				String direction = entryData.getKey().toString();
				PromtactData promtactData = entryData.getValue();
				
				sortInterPhase.put(direction, promtactData);
				
			}
			sortedInterPhaseMap.put(interPhase, sortInterPhase);
		}
		
		iRoadModel.getModel().getRoadPromtactuModel().setMapOfInterphaseSpecificPromtact(sortedInterPhaseMap);	// set sort interphase map to iRoadModel
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
				
		
		
		
		if (listViewInterphase.getItems().size() != 0) {
			specificPromtactDataMap = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact().get(listViewInterphase.getSelectionModel().getSelectedItem());

			for(Map.Entry<String, PromtactData> entry : specificPromtactDataMap.entrySet()) {
				
				String dirNumber = entry.getKey();
				PromtactData promtactData = entry.getValue();
				
				if(promtactData.isFullPromtact() == true) {
					observableListDirectionNotChangeState.add(dirNumber);
					
					phaseDirectionsHBoxCell = new PhaseDirectionsHBoxCell();
					phaseDirectionsHBoxCell.setObservableListNotChangeStateDirection(observableListDirectionNotChangeState);
					phaseDirectionsHBoxCell.getComboBoxNotChangeStateDirection().setValue(dirNumber);
					phaseDirectionsHBoxCellList.add(phaseDirectionsHBoxCell);
					
				}else {
					
					observableListDirectionsNumbers.add(dirNumber);

					phaseDirectionsHBoxCell = new PhaseDirectionsHBoxCell();
					phaseDirectionsHBoxCell.setObservableList(observableListDirectionsNumbers);
					phaseDirectionsHBoxCell.comboBoxChangeStateDirection.setValue(dirNumber);
					phaseDirectionsHBoxCellList.add(phaseDirectionsHBoxCell);
					
				}
				
			}			
			
			phaseDirectionsHBoxCellObservableList = FXCollections.observableArrayList(phaseDirectionsHBoxCellList);
			listViewPhaseDirections.setItems(phaseDirectionsHBoxCellObservableList);
			listViewPhaseDirections.getSelectionModel().selectFirst();
			
			
			PhaseDirectionsHBoxCell selectedDirection = listViewPhaseDirections.getSelectionModel().getSelectedItem();
			
			if(selectedDirection != null) {
				if(selectedDirection.getComboBoxDirNumber().getValue() != null) {
					selectedDirection.getComboBoxDirNumber().setDisable(false);
				}
				if(selectedDirection.getComboBoxNotChangeStateDirection().getValue() != null) {
					selectedDirection.getComboBoxNotChangeStateDirection().setDisable(false);
				}
			}

		}
		/////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////

		/////////////////////////////////////////////////////////////////////////////
		////////////////// add promtact data for the first direction ////////////////
		/////////////////////////////////////////////////////////////////////////////
		if (listViewPhaseDirections.getItems().size() != 0) {
						
			String fromPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue();
			String toPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxToPhase().getValue();
			
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromPhase = mapOfOpenDirInPhase.get(fromPhase);
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToPhase = mapOfOpenDirInPhase.get(toPhase);
			
			List<String> openDirectionsFrom = new ArrayList<>();
			List<String> openDirectionsTo = new ArrayList<>();
			
			if(openDirectionsFromPhase != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsFromPhase) {
					openDirectionsFrom.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
				}
			}
			
			if(openDirectionsToPhase != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsToPhase) {
					openDirectionsTo.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
				}
			}
			
			PhaseDirectionsHBoxCell selectedDirection = listViewPhaseDirections.getSelectionModel().getSelectedItem();
			
			if(selectedDirection.getComboBoxDirNumber().getValue() == null) {
				//System.out.println("full");
				
				promtactData = specificPromtactDataMap.get(selectedDirection.getComboBoxNotChangeStateDirection().getValue());
				
				textField_EndGreenBlink.setDisable(false);
				textField_EndGreenAddit.setDisable(false);
				textField_EndYellow.setDisable(false);
				textField_EndRed.setDisable(false);
				textField_EndRedYellow.setDisable(false);

				textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
				textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
				textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
				textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
				textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
				
				checkBoxFullPromtact.setSelected(true);
				
				previousDirection = selectedDirection.getComboBoxNotChangeStateDirection().getValue();
				
				
				for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
					directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
					directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
				}
				
				
				selectedDirection.getComboBoxDirNumber().setDisable(true);
				selectedDirection.getComboBoxNotChangeStateDirection().setDisable(false);
				
				labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxNotChangeStateDirection().getValue());
				
			}else {
				//System.out.println("not full");
				
				promtactData = specificPromtactDataMap.get(selectedDirection.getComboBoxDirNumber().getValue());
				
				previousDirection = selectedDirection.getComboBoxDirNumber().getValue();
				
				for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
					directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
					directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
				}
				
				selectedDirection.getComboBoxDirNumber().setDisable(false);
				selectedDirection.getComboBoxNotChangeStateDirection().setDisable(true);
				
				if (openDirectionsFrom.contains(selectedDirection.getComboBoxDirNumber().getValue())) {				// direction will be closing 
					textField_EndGreenBlink.setDisable(false);
					textField_EndGreenAddit.setDisable(false);
					textField_EndYellow.setDisable(false);
					textField_EndRed.setDisable(true);
					textField_EndRedYellow.setDisable(true);
					
					textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
					textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
					textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
					checkBoxFullPromtact.setSelected(false);					
					
				} else {
					textField_EndGreenBlink.setDisable(true);		// direction will be opening
					textField_EndGreenAddit.setDisable(true);
					textField_EndYellow.setDisable(true);
					textField_EndRed.setDisable(false);
					textField_EndRedYellow.setDisable(false);
					
					textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
					textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
					checkBoxFullPromtact.setSelected(false);
					
				}
				
				labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue());
				
			}
			
			labelInterPhaseFrom.setText(listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue());
			labelInterPhaseTo.setText(listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxToPhase().getValue());
			
			
		}
		//////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////
	}

	public void save(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;

		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		mapOfOpenDirInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();		
		
		specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(listViewInterphase.getSelectionModel().getSelectedItem());
		
		if (listViewPhaseDirections.getItems().size() != 0) {			

			String fromPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue();
			String toPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxToPhase().getValue();
			
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromPhase = mapOfOpenDirInPhase.get(fromPhase);
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToPhase = mapOfOpenDirInPhase.get(toPhase);
			
			List<String> openDirectionsFrom = new ArrayList<>();
			List<String> openDirectionsTo = new ArrayList<>();
			
			if(openDirectionsFromPhase != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsFromPhase) {
					openDirectionsFrom.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
				}
			}
			
			if(openDirectionsToPhase != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsToPhase) {
					openDirectionsTo.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
				}
			}
			
			PhaseDirectionsHBoxCell direction = listViewPhaseDirections.getSelectionModel().getSelectedItem();
			List<String> fromPhaseList1 = null;
			
			if(direction != null) {
				List<OpenDirectionInCurrentPhaseHBoxCell> fromPhaseList = mapOfOpenDirInPhase.get(fromPhase);
				fromPhaseList1 = new ArrayList<String>();
				
				if(fromPhaseList != null) {
					for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : fromPhaseList) {
						fromPhaseList1.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
					}
				}
			}
			
			if(direction.getComboBoxNotChangeStateDirection().getValue() != null) {
				promtactData = specificPromtactDataMap.get(direction.getComboBoxNotChangeStateDirection().getValue());
				
				if(promtactData.isFullPromtact() == true) {
					promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());					// NEED EDIT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
					promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
					promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
					promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
				}
				
			}else {
				if (fromPhaseList1.contains(direction.getComboBoxDirNumber().getValue())) {
					promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());
					promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
					promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
				} else {
					promtactData = specificPromtactDataMap.get(direction.getComboBoxDirNumber().getValue());
					promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
					promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
				}
			}
			
			/*if (previousDirection != null) {
				List<OpenDirectionInCurrentPhaseHBoxCell> fromPhaseList = mapOfOpenDirInPhase.get(fromPhase);
				List<String> fromPhaseList1 = new ArrayList<String>();
				
				if(fromPhaseList != null) {
					for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : fromPhaseList) {
						fromPhaseList1.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
					}
				}
				promtactData = specificPromtactDataMap.get(previousDirection);
				
				if(promtactData.isFullPromtact() == true) {
					promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());					// NEED EDIT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
					promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
					promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
					promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
				}else {
					if (fromPhaseList1.contains(previousDirection)) {
						promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());
						promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
						promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
					} else {
						promtactData = specificPromtactDataMap.get(previousDirection);
						promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
						promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
					}
				}
			}*/
			
		}

	}

	public void clearTextFields() {
		textField_EndGreenAddit.clear();
		textField_EndGreenBlink.clear();
		textField_EndRed.clear();
		textField_EndRedYellow.clear();
		textField_EndYellow.clear();
	}

	public void visibleField() {
		textField_EndGreenAddit.setDisable(true);
		textField_EndGreenBlink.setDisable(true);
		textField_EndRed.setDisable(true);
		textField_EndRedYellow.setDisable(true);
		textField_EndYellow.setDisable(true);
	}
		
	public void createInterphaseTransitions() {
		roadPhaseList = iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList();
		roadDirectionList = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();
		interPhaseMap = iRoadModel.getModel().getRoadPromtactuModel().getInterPhaseMap();
		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		mapOfOpenDirInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();

		checkBoxFullPromtact.setSelected(false);
		visibleField();
		
		if(listViewInterphase.getItems().size() > 0) {
			specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(listViewInterphase.getSelectionModel().getSelectedItem());
			if (listViewPhaseDirections.getItems().size() != 0) {

				String fromPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue();
				String toPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxToPhase().getValue();
				
				List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromPhase = mapOfOpenDirInPhase.get(fromPhase);
				List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToPhase = mapOfOpenDirInPhase.get(toPhase);
				
				List<String> openDirectionsFrom = new ArrayList<>();
				List<String> openDirectionsTo = new ArrayList<>();
				
				if(openDirectionsFromPhase != null) {	// was edit
				
					for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsFromPhase) {
						openDirectionsFrom.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
					}
				}
				
				if(openDirectionsToPhase != null) {		// was edit
					for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsToPhase) {
						openDirectionsTo.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
					}
				}
				
				if (previousDirection != null) {
					List<OpenDirectionInCurrentPhaseHBoxCell> fromPhaseList = mapOfOpenDirInPhase.get(fromPhase);
					List<String> fromPhaseList1 = new ArrayList<String>();
					
					if(fromPhaseList != null) {
						for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : fromPhaseList) {
							fromPhaseList1.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
						}
					}
					promtactData = specificPromtactDataMap.get(previousDirection);
					
					if(promtactData != null) {
						if(promtactData.isFullPromtact() == true) {
							promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());					
							promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
							promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
							promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
							promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
						}else {
							if (fromPhaseList1.contains(previousDirection)) {
								promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());
								promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
								promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
							} else {
								promtactData = specificPromtactDataMap.get(previousDirection);
								promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
								promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
							}
						}
					
					}
					
				}
				
			}
		}

		specificPromtactDataMap = new LinkedHashMap<>();

		clearTextFields();
		previousDirection = null;

		observableListPhaseNumberFrom.clear();
		observableListPhaseNumberTo.clear();
		if(roadPhaseList.size() > 1) {
			for (RoadPhase roadPhase : roadPhaseList) {
				if (!observableListPhaseNumberFrom.contains(roadPhase.getRoadPhase_number())) {
					observableListPhaseNumberFrom.add(roadPhase.getRoadPhase_number());
				}
			}
			
			
			if(listViewInterphase.getItems().size() != 0) {				
				
				InterphaseTransitionsHBoxCell selectedInterphase = listViewInterphase.getSelectionModel().getSelectedItem();
				
				if(selectedInterphase.getComboBoxFromPhase().getValue() != null && selectedInterphase.getComboBoxToPhase().getValue() != null) {
				
		
					interphaseTransitionsHBoxCell = new InterphaseTransitionsHBoxCell();
					interphaseTransitionsHBoxCell.getComboBoxToPhase().setDisable(true);
			
					interphaseTransitionsHBoxCell.setObservableListComboBoxFromPhase(observableListPhaseNumberFrom);
			
					interphaseTransitionsHBoxCellList.add(interphaseTransitionsHBoxCell);
					interphaseTransitionsHBoxCellObservableList = FXCollections.observableArrayList(interphaseTransitionsHBoxCellList);
			
					listViewInterphase.setItems(interphaseTransitionsHBoxCellObservableList);
					listViewInterphase.getSelectionModel().select(listViewInterphase.getItems().size() - 1);
			
					mapOfDirectionSpecificPromtact.put(interphaseTransitionsHBoxCell, specificPromtactDataMap);
			
					interphaseTransitionsHBoxCell.getComboBoxFromPhase().getSelectionModel().selectedItemProperty().addListener((observableKey, oldKey, newKey) -> {
						if (newKey != null) {
							
							if (!interPhaseMap.containsKey(newKey)) {
								List<String> listOfPhaseTo = new ArrayList<>();
								interPhaseMap.put(newKey, listOfPhaseTo);
								labelInterPhaseFrom.setText(newKey);
							}
							for (RoadPhase roadPhase : roadPhaseList) {
								boolean write = true;
								if (roadPhase.getRoadPhase_number().equals(newKey)) {
									write = false;
								}
								if (interPhaseMap.get(newKey) != null) {
									for (String existedValue : interPhaseMap.get(newKey)) {
										if (roadPhase.getRoadPhase_number().equals(existedValue)) {
											write = false;
										}
									}
								}
								if (write == true) {
									observableListPhaseNumberTo.add(roadPhase.getRoadPhase_number());
								}
							}
							if(observableListPhaseNumberTo.size() != 0) {
								interphaseTransitionsHBoxCell.getComboBoxToPhase().setDisable(false);
								interphaseTransitionsHBoxCell.getComboBoxToPhase().getItems().clear();
								interphaseTransitionsHBoxCell.setObservableListComboBoxToPhase(observableListPhaseNumberTo);
								observableListPhaseNumberTo.clear();
							}else {
								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("Внимание");
								alert.setHeaderText("Все возможные межфазные переходы для\nфазы № " + interphaseTransitionsHBoxCell.getComboBoxFromPhase().getValue() + " были созданны");
								
								Stage stage = new Stage();
								stage = (Stage)alert.getDialogPane().getScene().getWindow();
								stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
								
								alert.show();
								mapOfDirectionSpecificPromtact.remove(interphaseTransitionsHBoxCell);
								interphaseTransitionsHBoxCellList.remove(interphaseTransitionsHBoxCell);
								listViewInterphase.getItems().remove(listViewInterphase.getItems().size() - 1);
							}
						}
						interphaseTransitionsHBoxCell.getComboBoxToPhase().getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
							if (newValue != null) {
								interPhaseMap.get(interphaseTransitionsHBoxCell.getComboBoxFromPhase().getValue()).add(newValue);
								labelInterPhaseTo.setText(newValue);
							}
						});
					});
					
					
				}else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Внимание");
					alert.setHeaderText("Заполните межфазный переход данными");
					
					Stage stage = new Stage();
					stage = (Stage)alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
					
					alert.show();
				}
			
			
			}else {
				interphaseTransitionsHBoxCell = new InterphaseTransitionsHBoxCell();
				interphaseTransitionsHBoxCell.getComboBoxToPhase().setDisable(true);
		
				interphaseTransitionsHBoxCell.setObservableListComboBoxFromPhase(observableListPhaseNumberFrom);
		
				interphaseTransitionsHBoxCellList.add(interphaseTransitionsHBoxCell);
				interphaseTransitionsHBoxCellObservableList = FXCollections.observableArrayList(interphaseTransitionsHBoxCellList);
		
				listViewInterphase.setItems(interphaseTransitionsHBoxCellObservableList);
				listViewInterphase.getSelectionModel().select(listViewInterphase.getItems().size() - 1);
		
				mapOfDirectionSpecificPromtact.put(interphaseTransitionsHBoxCell, specificPromtactDataMap);
		
				interphaseTransitionsHBoxCell.getComboBoxFromPhase().getSelectionModel().selectedItemProperty().addListener((observableKey, oldKey, newKey) -> {
					if (newKey != null) {
						
						if (!interPhaseMap.containsKey(newKey)) {
							List<String> listOfPhaseTo = new ArrayList<>();
							interPhaseMap.put(newKey, listOfPhaseTo);
							labelInterPhaseFrom.setText(newKey);
						}
						for (RoadPhase roadPhase : roadPhaseList) {
							boolean write = true;
							if (roadPhase.getRoadPhase_number().equals(newKey)) {
								write = false;
							}
							if (interPhaseMap.get(newKey) != null) {
								for (String existedValue : interPhaseMap.get(newKey)) {
									if (roadPhase.getRoadPhase_number().equals(existedValue)) {
										write = false;
									}
								}
							}
							if (write == true) {
								observableListPhaseNumberTo.add(roadPhase.getRoadPhase_number());
							}
						}
						if(observableListPhaseNumberTo.size() != 0) {
							interphaseTransitionsHBoxCell.getComboBoxToPhase().setDisable(false);
							interphaseTransitionsHBoxCell.getComboBoxToPhase().getItems().clear();
							interphaseTransitionsHBoxCell.setObservableListComboBoxToPhase(observableListPhaseNumberTo);
							observableListPhaseNumberTo.clear();
						}else {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Внимание");
							alert.setHeaderText("Все возможные межфазные переходы для\nфазы № " + interphaseTransitionsHBoxCell.getComboBoxFromPhase().getValue() + " были созданны");
							
							Stage stage = new Stage();
							stage = (Stage)alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
							
							alert.show();
							mapOfDirectionSpecificPromtact.remove(interphaseTransitionsHBoxCell);
							interphaseTransitionsHBoxCellList.remove(interphaseTransitionsHBoxCell);
							listViewInterphase.getItems().remove(listViewInterphase.getItems().size() - 1);
						}
					}
					interphaseTransitionsHBoxCell.getComboBoxToPhase().getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
						if (newValue != null) {
							interPhaseMap.get(interphaseTransitionsHBoxCell.getComboBoxFromPhase().getValue()).add(newValue);
							labelInterPhaseTo.setText(newValue);
						}
					});
				});
			}
			
			
		}else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Внимание");
			alert.setHeaderText("Недостаточное количество фаз\nдля создание межфазного перехода");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
		}

		listViewPhaseDirections.getItems().clear();
		if (phaseDirectionsHBoxCellList != null) {
			phaseDirectionsHBoxCellList.clear();
		}
	}

	public void createDirectionOfInterphaseTransitions() {
		mapOfOpenDirInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		roadDirectionList = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();
		
		checkBoxFullPromtact.setSelected(false);
		visibleField();
		
		List<RoadPhase> roadPhasesList = iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList();
		ObservableList<String> comboBoxValuesWhichChangeState = FXCollections.observableArrayList();
		ObservableList<String> comboBoxValuesWhichNotChangeState = FXCollections.observableArrayList();
		
		for (PhaseDirectionsHBoxCell existedPhaseDirectionsHBoxCell : phaseDirectionsHBoxCellList) {
			existedPhaseDirectionsHBoxCell.getComboBoxDirNumber().setDisable(true);
			existedPhaseDirectionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
		}
		
		if (listViewInterphase.getItems().size() != 0) {
			
			String fromPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue();
			String toPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxToPhase().getValue();
			ObservableList<Integer> integersChange = FXCollections.observableArrayList();
			ObservableList<Integer> integersNotChange = FXCollections.observableArrayList();
			
			// SAVE PROMTACTDATA TO PREVIOUS DIRECTION
			if (previousDirection != null) {
				List<OpenDirectionInCurrentPhaseHBoxCell> fromPhaseList = mapOfOpenDirInPhase.get(fromPhase);
				List<String> fromPhaseList1 = new ArrayList<String>();
				
				if(fromPhaseList != null) {	// was edit
					for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : fromPhaseList) {
						fromPhaseList1.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
					}
				}
				
				promtactData = specificPromtactDataMap.get(previousDirection);
				
				if(promtactData != null) {
					if(promtactData.isFullPromtact() == true) {
						promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());					
						promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
						promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
						promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
						promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
					}else {
						if (fromPhaseList1.contains(previousDirection)) {
							promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());
							promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
							promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
						} else {
							promtactData = specificPromtactDataMap.get(previousDirection);
							promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
							promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
						}
					}
				}
				clearTextFields();
			}
			////////////////////////////////////////////////////
			
			if(listViewPhaseDirections.getItems().isEmpty()) {
				if(fromPhase != null && toPhase != null) {			// if interphase have correct values
					
					if(!roadDirectionList.isEmpty()) {
						
						List<OpenDirectionInCurrentPhaseHBoxCell> fromPhaseOpenDirList = mapOfOpenDirInPhase.get(fromPhase);	// get open directions list from phase
						List<OpenDirectionInCurrentPhaseHBoxCell> toPhaseOpenDirList = mapOfOpenDirInPhase.get(toPhase);		// get open directions list to phase
						
						if(fromPhaseOpenDirList != null || toPhaseOpenDirList != null) {	// if phases have open direction
							
							List<String> fromPhaseList = new ArrayList<String>();
							List<String> toPhaseList = new ArrayList<String>();
							
							if(fromPhaseOpenDirList != null) {	// was edit
							
								for(OpenDirectionInCurrentPhaseHBoxCell openDirectionFrom : fromPhaseOpenDirList) {	// add open direction from
									fromPhaseList.add(openDirectionFrom.getComboBox().getValue());
								}
							}
							
							if(toPhaseOpenDirList != null) {	// was edit
							
								for(OpenDirectionInCurrentPhaseHBoxCell openDirectionTo : toPhaseOpenDirList) {		// add open direction to
									toPhaseList.add(openDirectionTo.getComboBox().getValue());
								}
							}
							
							observableListDirectionsNumbers.clear();
							observableListDirectionNotChangeState.clear();
							
							for(String directionFrom : fromPhaseList) {		// go to the list direction from	
								if(toPhaseList.contains(directionFrom)) {	
									observableListDirectionNotChangeState.add(directionFrom);	// add if not change state
								}else {
									observableListDirectionsNumbers.add(directionFrom);			// add if change state
								}
							}
							
							
							for(String directionTo : toPhaseList) {			// go to the list direction to
								if(fromPhaseList.contains(directionTo)) {
									observableListDirectionNotChangeState.add(directionTo);		// add if not change state
								}else {
									observableListDirectionsNumbers.add(directionTo);			// add if change state
								}
							}
							
							
							for(RoadDirection direction : roadDirectionList) {			// do to all directions
								String dirNumber = direction.getRoadDirections_number();
								
								if(!observableListDirectionsNumbers.contains(dirNumber)) {	// if direction exist in the list of changes direction - skip 
									observableListDirectionNotChangeState.add(dirNumber); // add in the list of not changes direction
								}
								
							}
							
							
							Set<String> set = new LinkedHashSet<>();					// remove repeating numbers from changes list
							set.addAll(observableListDirectionNotChangeState);
							observableListDirectionNotChangeState.clear();
							observableListDirectionNotChangeState.addAll(set);
							
							
							for(PhaseDirectionsHBoxCell existDirection : listViewPhaseDirections.getItems()) {			// remove directions number that was created
								String existNumberWhichChange = existDirection.getComboBoxDirNumber().getValue();
								String existNumberWhichNotChange = existDirection.getComboBoxNotChangeStateDirection().getValue();
								observableListDirectionsNumbers.removeIf(direction -> direction.equals(existNumberWhichChange));
								observableListDirectionNotChangeState.removeIf(direction -> direction.equals(existNumberWhichNotChange));
							}
							
							// sort direction which change
							for (String number : observableListDirectionsNumbers) {
								integersChange.add(Integer.parseInt(number));
							}
							
							Collections.sort(integersChange);
	
							for (Integer i : integersChange) {
								comboBoxValuesWhichChangeState.add(i.toString());
							}
							/////////////////////////////////////////////////////
							
							// sort direction which not change
							for (String number : observableListDirectionNotChangeState) {
								integersNotChange.add(Integer.parseInt(number));
							}
							
							Collections.sort(integersNotChange);
	
							for (Integer i : integersNotChange) {
								comboBoxValuesWhichNotChangeState.add(i.toString());
							}
							/////////////////////////////////////////////////////
							
							//if(!comboBoxValuesWhichChangeState.isEmpty() && !comboBoxValuesWhichNotChangeState.isEmpty()) {
								
								clearTextFields();
								visibleField();
								phaseDirectionsHBoxCell = new PhaseDirectionsHBoxCell();
								phaseDirectionsHBoxCell.setObservableList(comboBoxValuesWhichChangeState);
								phaseDirectionsHBoxCell.setObservableListNotChangeStateDirection(comboBoxValuesWhichNotChangeState);
								phaseDirectionsHBoxCellList.add(phaseDirectionsHBoxCell);
								phaseDirectionsHBoxCellObservableList = FXCollections.observableArrayList(phaseDirectionsHBoxCellList);
								listViewPhaseDirections.setItems(phaseDirectionsHBoxCellObservableList);
								phaseDirectionsHBoxCell.getComboBoxDirNumber().setDisable(false);
								listViewPhaseDirections.getSelectionModel().select(listViewPhaseDirections.getItems().size() - 1);
								
								promtactData = new PromtactData();
								
								phaseDirectionsHBoxCell.comboBoxChangeStateDirection.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
									if (newValue != null) {
										
										
										if (fromPhaseList.contains(newValue)) {				// direction will be closing 
												textField_EndGreenBlink.setDisable(false);
												textField_EndGreenAddit.setDisable(false);
												textField_EndYellow.setDisable(false);
												textField_EndRed.setDisable(true);
												textField_EndRedYellow.setDisable(true);
												
											} else {
												textField_EndGreenBlink.setDisable(true);		// direction will be opening
												textField_EndGreenAddit.setDisable(true);
												textField_EndYellow.setDisable(true);
												textField_EndRed.setDisable(false);
												textField_EndRedYellow.setDisable(false);
												
											}
										}
										
										if(newValue != null) {
											previousDirection = newValue;
											specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(listViewInterphase.getSelectionModel().getSelectedItem());
											specificPromtactDataMap.put(newValue, promtactData);
											
											promtactData = specificPromtactDataMap.get(newValue);
											
											if(promtactData != null) {
												promtactData.setFullPromtact(false);
											}else {
												promtactData = new PromtactData();
												promtactData.setFullPromtact(false);
												specificPromtactDataMap.put(newValue, promtactData);
											}
											
											labelPromtactForDirection.setText(newValue);
										}
										
										
									
								});
								
								
								phaseDirectionsHBoxCell.comboBoxNotChangeStateDirection.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
									if(newValue != null) {
										
										
										textField_EndGreenBlink.setDisable(false);
										textField_EndGreenAddit.setDisable(false);
										textField_EndYellow.setDisable(false);
										textField_EndRed.setDisable(false);
										textField_EndRedYellow.setDisable(false);
										
										if(newValue != null) {
											previousDirection = newValue;
											specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(listViewInterphase.getSelectionModel().getSelectedItem());
											specificPromtactDataMap.put(newValue, promtactData);
											
											promtactData = specificPromtactDataMap.get(newValue);
											if(promtactData != null) {
												promtactData.setFullPromtact(true);
											}else {
												promtactData = new PromtactData();
												promtactData.setFullPromtact(true);
												specificPromtactDataMap.put(newValue, promtactData);
											}
											
											labelPromtactForDirection.setText(newValue);
										}
										
									}
								});
							
							
						}else {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("Ошибка");
							alert.setHeaderText("Создайте открытые направления в фазах");
							
							Stage stage = new Stage();
							stage = (Stage)alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
							
							alert.show();
						}
						
						
					}else {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Ошибка");
						alert.setHeaderText("Создайте направления");
						
						Stage stage = new Stage();
						stage = (Stage)alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
						
						alert.show();
					}
					
					
				}else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Ошибка");
					alert.setHeaderText("Укажите фазу перехода");
					
					Stage stage = new Stage();
					stage = (Stage)alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
					
					alert.show();
				}
			}else {	// if list view phase direction not empty
				
				PhaseDirectionsHBoxCell selectDirection = listViewPhaseDirections.getSelectionModel().getSelectedItem();
				
				if(selectDirection.getComboBoxDirNumber().getValue() != null || selectDirection.getComboBoxNotChangeStateDirection().getValue() != null) {	// check if combobox value is null
				
					if(fromPhase != null && toPhase != null) {			// if interphase have correct values
						
						if(!roadDirectionList.isEmpty()) {
							
							List<OpenDirectionInCurrentPhaseHBoxCell> fromPhaseOpenDirList = mapOfOpenDirInPhase.get(fromPhase);	// get open directions list from phase
							List<OpenDirectionInCurrentPhaseHBoxCell> toPhaseOpenDirList = mapOfOpenDirInPhase.get(toPhase);		// get open directions list to phase
							
							if(fromPhaseOpenDirList != null || toPhaseOpenDirList != null) {	// if phases have open direction
								
								List<String> fromPhaseList = new ArrayList<String>();
								List<String> toPhaseList = new ArrayList<String>();
								
								if(fromPhaseOpenDirList != null) {	// was edit
									
									for(OpenDirectionInCurrentPhaseHBoxCell openDirectionFrom : fromPhaseOpenDirList) {	// add open direction from
										fromPhaseList.add(openDirectionFrom.getComboBox().getValue());
									}
								}
								
								if(toPhaseOpenDirList != null) {	// was edit
								
									for(OpenDirectionInCurrentPhaseHBoxCell openDirectionTo : toPhaseOpenDirList) {		// add open direction to
										toPhaseList.add(openDirectionTo.getComboBox().getValue());
									}
								}
								
								observableListDirectionsNumbers.clear();
								observableListDirectionNotChangeState.clear();
								
								for(String directionFrom : fromPhaseList) {		// go to the list direction from	
									if(toPhaseList.contains(directionFrom)) {	
										observableListDirectionNotChangeState.add(directionFrom);	// add if not change state
									}else {
										observableListDirectionsNumbers.add(directionFrom);			// add if change state
									}
								}
								
								
								for(String directionTo : toPhaseList) {			// go to the list direction to
									if(fromPhaseList.contains(directionTo)) {
										observableListDirectionNotChangeState.add(directionTo);		// add if not change state
									}else {
										observableListDirectionsNumbers.add(directionTo);			// add if change state
									}
								}
								
								
								for(RoadDirection direction : roadDirectionList) {			// do to all directions
									String dirNumber = direction.getRoadDirections_number();
									
									if(!observableListDirectionsNumbers.contains(dirNumber)) {	// if direction exist in the list of changes direction - skip 
										observableListDirectionNotChangeState.add(dirNumber); // add in the list of not changes direction
									}
									
								}
								
								
								Set<String> set = new LinkedHashSet<>();					// remove repeating numbers from changes list
								set.addAll(observableListDirectionNotChangeState);
								observableListDirectionNotChangeState.clear();
								observableListDirectionNotChangeState.addAll(set);
								
								
								for(PhaseDirectionsHBoxCell existDirection : listViewPhaseDirections.getItems()) {			// remove directions number that was created
									String existNumberWhichChange = existDirection.getComboBoxDirNumber().getValue();
									String existNumberWhichNotChange = existDirection.getComboBoxNotChangeStateDirection().getValue();
									observableListDirectionsNumbers.removeIf(direction -> direction.equals(existNumberWhichChange));
									observableListDirectionNotChangeState.removeIf(direction -> direction.equals(existNumberWhichNotChange));
								}
								
								// sort direction which change
								for (String number : observableListDirectionsNumbers) {
									integersChange.add(Integer.parseInt(number));
								}
								
								Collections.sort(integersChange);
		
								for (Integer i : integersChange) {
									comboBoxValuesWhichChangeState.add(i.toString());
								}
								/////////////////////////////////////////////////////
								
								// sort direction which not change
								for (String number : observableListDirectionNotChangeState) {
									integersNotChange.add(Integer.parseInt(number));
								}
								
								Collections.sort(integersNotChange);
		
								for (Integer i : integersNotChange) {
									comboBoxValuesWhichNotChangeState.add(i.toString());
								}
								/////////////////////////////////////////////////////
								
								if(!comboBoxValuesWhichChangeState.isEmpty() | !comboBoxValuesWhichNotChangeState.isEmpty()) {
									
									clearTextFields();
									visibleField();
									phaseDirectionsHBoxCell = new PhaseDirectionsHBoxCell();
									phaseDirectionsHBoxCell.setObservableList(comboBoxValuesWhichChangeState);
									phaseDirectionsHBoxCell.setObservableListNotChangeStateDirection(comboBoxValuesWhichNotChangeState);
									phaseDirectionsHBoxCellList.add(phaseDirectionsHBoxCell);
									phaseDirectionsHBoxCellObservableList = FXCollections.observableArrayList(phaseDirectionsHBoxCellList);
									listViewPhaseDirections.setItems(phaseDirectionsHBoxCellObservableList);
									phaseDirectionsHBoxCell.getComboBoxDirNumber().setDisable(false);
									listViewPhaseDirections.getSelectionModel().select(listViewPhaseDirections.getItems().size() - 1);
									
									promtactData = new PromtactData();
									
									phaseDirectionsHBoxCell.comboBoxChangeStateDirection.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
										if (newValue != null) {
											
											
											if (fromPhaseList.contains(newValue)) {				// direction will be closing 
													textField_EndGreenBlink.setDisable(false);
													textField_EndGreenAddit.setDisable(false);
													textField_EndYellow.setDisable(false);
													textField_EndRed.setDisable(true);
													textField_EndRedYellow.setDisable(true);
													
												} else {
													textField_EndGreenBlink.setDisable(true);		// direction will be opening
													textField_EndGreenAddit.setDisable(true);
													textField_EndYellow.setDisable(true);
													textField_EndRed.setDisable(false);
													textField_EndRedYellow.setDisable(false);
													
												}
											}
											
											if(newValue != null) {
												previousDirection = newValue;
												specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(listViewInterphase.getSelectionModel().getSelectedItem());
												specificPromtactDataMap.put(newValue, promtactData);
												
												promtactData = specificPromtactDataMap.get(newValue);
												if(promtactData != null) {
													promtactData.setFullPromtact(false);
												}else {
													promtactData = new PromtactData();
													promtactData.setFullPromtact(false);
													specificPromtactDataMap.put(newValue, promtactData);
												}
												
												labelPromtactForDirection.setText(newValue);
											}
										
									});
									
									
									phaseDirectionsHBoxCell.comboBoxNotChangeStateDirection.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
										if(newValue != null) {
											
											
											textField_EndGreenBlink.setDisable(false);
											textField_EndGreenAddit.setDisable(false);
											textField_EndYellow.setDisable(false);
											textField_EndRed.setDisable(false);
											textField_EndRedYellow.setDisable(false);
											
											if(newValue != null) {
												previousDirection = newValue;
												specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(listViewInterphase.getSelectionModel().getSelectedItem());
												specificPromtactDataMap.put(newValue, promtactData);
												
												promtactData = specificPromtactDataMap.get(newValue);
												if(promtactData != null) {
													promtactData.setFullPromtact(true);
												}else {
													promtactData = new PromtactData();
													promtactData.setFullPromtact(true);
													specificPromtactDataMap.put(newValue, promtactData);
												}
												
												labelPromtactForDirection.setText(newValue);
											}
											
										}
									});
									
									
									
									
								}else {
									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("Внимание");
									alert.setHeaderText("Все возможные направления были созданны");
									
									Stage stage = new Stage();
									stage = (Stage)alert.getDialogPane().getScene().getWindow();
									stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
									
									alert.show();
									
									// set promtact data from selected direction
									String fromPhase_1 = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue();
									String toPhase_1 = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxToPhase().getValue();
									
									List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromPhase = mapOfOpenDirInPhase.get(fromPhase_1);
									List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToPhase = mapOfOpenDirInPhase.get(toPhase_1);
									
									List<String> openDirectionsFrom = new ArrayList<>();
									List<String> openDirectionsTo = new ArrayList<>();
									
									if(openDirectionsFromPhase != null) {
										for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsFromPhase) {
											openDirectionsFrom.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
										}
									}
									
									if(openDirectionsToPhase != null) {
										for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsToPhase) {
											openDirectionsTo.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
										}
									}
									
									PhaseDirectionsHBoxCell selectedDirection = listViewPhaseDirections.getSelectionModel().getSelectedItem();
									
									if(selectedDirection.getComboBoxDirNumber().getValue() == null) {
										//System.out.println("full");
										
										promtactData = specificPromtactDataMap.get(selectedDirection.getComboBoxNotChangeStateDirection().getValue());
										
										textField_EndGreenBlink.setDisable(false);
										textField_EndGreenAddit.setDisable(false);
										textField_EndYellow.setDisable(false);
										textField_EndRed.setDisable(false);
										textField_EndRedYellow.setDisable(false);

										textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
										textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
										textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
										textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
										textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
										
										checkBoxFullPromtact.setSelected(true);
										
										previousDirection = selectedDirection.getComboBoxNotChangeStateDirection().getValue();
										
										
										for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
											directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
											directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
										}
										
										
										selectedDirection.getComboBoxDirNumber().setDisable(true);
										selectedDirection.getComboBoxNotChangeStateDirection().setDisable(false);
										
										labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxNotChangeStateDirection().getValue());
										
									}else {
										//System.out.println("not full");
										
										promtactData = specificPromtactDataMap.get(selectedDirection.getComboBoxDirNumber().getValue());
										
										previousDirection = selectedDirection.getComboBoxDirNumber().getValue();
										
										for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
											directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
											directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
										}
										
										selectedDirection.getComboBoxDirNumber().setDisable(false);
										selectedDirection.getComboBoxNotChangeStateDirection().setDisable(true);
										
										if (openDirectionsFrom.contains(selectedDirection.getComboBoxDirNumber().getValue())) {				// direction will be closing 
											textField_EndGreenBlink.setDisable(false);
											textField_EndGreenAddit.setDisable(false);
											textField_EndYellow.setDisable(false);
											textField_EndRed.setDisable(true);
											textField_EndRedYellow.setDisable(true);
											
											textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
											textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
											textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
											checkBoxFullPromtact.setSelected(false);
											
											
											
										} else {
											textField_EndGreenBlink.setDisable(true);		// direction will be opening
											textField_EndGreenAddit.setDisable(true);
											textField_EndYellow.setDisable(true);
											textField_EndRed.setDisable(false);
											textField_EndRedYellow.setDisable(false);
											
											textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
											textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
											checkBoxFullPromtact.setSelected(false);
											
											
										}
										
										labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue());
										
									}
									
									////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
									
								}
								
								
							}else {
								Alert alert = new Alert(Alert.AlertType.ERROR);
								alert.setTitle("Ошибка");
								alert.setHeaderText("Создайте открытые направления в фазах");
								
								Stage stage = new Stage();
								stage = (Stage)alert.getDialogPane().getScene().getWindow();
								stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
								
								alert.show();
							}
							
							
						}else {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("Ошибка");
							alert.setHeaderText("Создайте направления");
							
							Stage stage = new Stage();
							stage = (Stage)alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
							
							alert.show();
						}
						
						
					}else {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Ошибка");
						alert.setHeaderText("Укажите фазу перехода");
						
						Stage stage = new Stage();
						stage = (Stage)alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
						
						alert.show();
					}
					
				}else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Ошибка");
					alert.setHeaderText("Укажите номер направления и\nзаполните промтакт значениями");
					
					Stage stage = new Stage();
					stage = (Stage)alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
					
					alert.show();
					
					selectDirection.getComboBoxDirNumber().setDisable(false);
					
				}
				
			}
			
			
		}else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Внимание");
			alert.setHeaderText("Создайте межфазный переход");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
		}
		
		
	}

	public void selectInterphaseTransitions() {
		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		mapOfOpenDirInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		
		
		// save data to previous interphase
		specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(previousInterphaseTransitionsHBoxCell);
		String selectedDirectionNumber;
		if(listViewPhaseDirections.getItems().size() != 0){
			if(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue() != null){
				selectedDirectionNumber = listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue();

				String fromPhase = previousInterphaseTransitionsHBoxCell.getComboBoxFromPhase().getValue();
				List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromPhase = mapOfOpenDirInPhase.get(fromPhase);
				List<String> openDirectionsFrom = new ArrayList<>();

				if(openDirectionsFromPhase != null) {	// was edit
					for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsFromPhase) {
						openDirectionsFrom.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
					}
				}

				promtactData = specificPromtactDataMap.get(selectedDirectionNumber);
				if(openDirectionsFrom.contains(selectedDirectionNumber)){
					promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());
					promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
					promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
				}else{
					promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
					promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
				}


			}else{
				selectedDirectionNumber = listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxNotChangeStateDirection().getValue();
				promtactData = specificPromtactDataMap.get(selectedDirectionNumber);

				promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());
				promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
				promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
				promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
				promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
			}
		}



		/*if (previousInterphaseTransitionsHBoxCell != null) {
			specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(previousInterphaseTransitionsHBoxCell);
			if (listViewPhaseDirections.getItems().size() != 0) {

				
				String fromPhase = previousInterphaseTransitionsHBoxCell.getComboBoxFromPhase().getValue();
				String toPhase = previousInterphaseTransitionsHBoxCell.getComboBoxToPhase().getValue();
				
				List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromPhase = mapOfOpenDirInPhase.get(fromPhase);
				List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToPhase = mapOfOpenDirInPhase.get(toPhase);
				
				List<String> openDirectionsFrom = new ArrayList<>();
				List<String> openDirectionsTo = new ArrayList<>();
				
				if(openDirectionsFromPhase != null) {	// was edit
					for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsFromPhase) {
						openDirectionsFrom.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
					}
				}
				
				if(openDirectionsToPhase != null) {		// was edit
					for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsToPhase) {
						openDirectionsTo.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
					}
				}
				
				PhaseDirectionsHBoxCell direction = listViewPhaseDirections.getSelectionModel().getSelectedItem();
				List<String> fromPhaseList1 = null;
				
				if(direction != null) {
					List<OpenDirectionInCurrentPhaseHBoxCell> fromPhaseList = mapOfOpenDirInPhase.get(fromPhase);
					fromPhaseList1 = new ArrayList<String>();
					
					if(fromPhaseList != null) {
						for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : fromPhaseList) {
							fromPhaseList1.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
						}
					}
				}
				
				if(direction.getComboBoxNotChangeStateDirection().getValue() != null) {
					promtactData = specificPromtactDataMap.get(direction.getComboBoxNotChangeStateDirection().getValue());
					
					if(promtactData.isFullPromtact() == true) {
						promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());					// NEED EDIT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
						promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
						promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
						promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
					}
					
				}else {
					if (fromPhaseList1.contains(direction.getComboBoxDirNumber().getValue())) {
						promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());
						promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
						promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
					} else {
						promtactData = specificPromtactDataMap.get(direction.getComboBoxDirNumber().getValue());
						promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
						promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
					}
				}
				
				
				if (previousDirection != null) {
					List<OpenDirectionInCurrentPhaseHBoxCell> fromPhaseList = mapOfOpenDirInPhase.get(fromPhase);
					List<String> fromPhaseList1 = new ArrayList<String>();
					
					if(fromPhaseList != null) {
						for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : fromPhaseList) {
							fromPhaseList1.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
						}
					}
					
					promtactData = specificPromtactDataMap.get(previousDirection);
					
					if(promtactData != null) {
					
						if(promtactData.isFullPromtact() == true) {
							promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());					// NEED EDIT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
							promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
							promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
							promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
							promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
						}else {
							if (fromPhaseList1.contains(previousDirection)) {
								promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());
								promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
								promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
							} else {
								promtactData = specificPromtactDataMap.get(previousDirection);
								promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
								promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
							}
						}
					
					}
				}
				
				
				
			}
		}*/
		//////////////////////////////////////////////////////////////////////////////////////

		listViewPhaseDirections.getItems().clear();
		phaseDirectionsHBoxCellList.clear();

		specificPromtactDataMap = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact().get(listViewInterphase.getSelectionModel().getSelectedItem());

		for(Map.Entry<String, PromtactData> entry : specificPromtactDataMap.entrySet()) {
			
			String dirNumber = entry.getKey();
			PromtactData promtactData = entry.getValue();
			
			if(promtactData.isFullPromtact() == true) {
				observableListDirectionNotChangeState.add(dirNumber);
				
				phaseDirectionsHBoxCell = new PhaseDirectionsHBoxCell();
				phaseDirectionsHBoxCell.setObservableListNotChangeStateDirection(observableListDirectionNotChangeState);
				phaseDirectionsHBoxCell.getComboBoxNotChangeStateDirection().setValue(dirNumber);
				phaseDirectionsHBoxCellList.add(phaseDirectionsHBoxCell);
				
			}else {
				
				observableListDirectionsNumbers.add(dirNumber);

				phaseDirectionsHBoxCell = new PhaseDirectionsHBoxCell();
				phaseDirectionsHBoxCell.setObservableList(observableListDirectionsNumbers);
				phaseDirectionsHBoxCell.comboBoxChangeStateDirection.setValue(dirNumber);
				phaseDirectionsHBoxCellList.add(phaseDirectionsHBoxCell);
				
			}
			
		}
		
		phaseDirectionsHBoxCellObservableList = FXCollections.observableArrayList(phaseDirectionsHBoxCellList);
		listViewPhaseDirections.setItems(phaseDirectionsHBoxCellObservableList);
		listViewPhaseDirections.getSelectionModel().selectFirst();
		
		clearTextFields();
		
		if (listViewPhaseDirections.getItems().size() != 0) {
			
			String fromPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue();
			String toPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxToPhase().getValue();
			
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromPhase = mapOfOpenDirInPhase.get(fromPhase);
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToPhase = mapOfOpenDirInPhase.get(toPhase);
			
			List<String> openDirectionsFrom = new ArrayList<>();
			List<String> openDirectionsTo = new ArrayList<>();
			
			if(openDirectionsFromPhase != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsFromPhase) {
					openDirectionsFrom.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
				}
			}
			
			if(openDirectionsToPhase != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsToPhase) {
					openDirectionsTo.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
				}
			}
			
			PhaseDirectionsHBoxCell selectedDirection = listViewPhaseDirections.getSelectionModel().getSelectedItem();
			
			if(selectedDirection.getComboBoxDirNumber().getValue() == null) {
				//System.out.println("full");
				
				promtactData = specificPromtactDataMap.get(selectedDirection.getComboBoxNotChangeStateDirection().getValue());
				
				textField_EndGreenBlink.setDisable(false);
				textField_EndGreenAddit.setDisable(false);
				textField_EndYellow.setDisable(false);
				textField_EndRed.setDisable(false);
				textField_EndRedYellow.setDisable(false);

				textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
				textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
				textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
				textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
				textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
				
				checkBoxFullPromtact.setSelected(true);
				
				previousDirection = selectedDirection.getComboBoxNotChangeStateDirection().getValue();
				
				
				for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
					directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
					directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
				}
				
				
				selectedDirection.getComboBoxDirNumber().setDisable(true);
				selectedDirection.getComboBoxNotChangeStateDirection().setDisable(false);
				
				labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxNotChangeStateDirection().getValue());
				
			}else {
				//System.out.println("not full");
				
				promtactData = specificPromtactDataMap.get(selectedDirection.getComboBoxDirNumber().getValue());
				
				previousDirection = selectedDirection.getComboBoxDirNumber().getValue();
				
				for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
					directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
					directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
				}
				
				selectedDirection.getComboBoxDirNumber().setDisable(false);
				selectedDirection.getComboBoxNotChangeStateDirection().setDisable(true);
				
				if (openDirectionsFrom.contains(selectedDirection.getComboBoxDirNumber().getValue())) {				// direction will be closing 
					textField_EndGreenBlink.setDisable(false);
					textField_EndGreenAddit.setDisable(false);
					textField_EndYellow.setDisable(false);
					textField_EndRed.setDisable(true);
					textField_EndRedYellow.setDisable(true);
					
					textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
					textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
					textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
					checkBoxFullPromtact.setSelected(false);
					
					
					
				} else {
					textField_EndGreenBlink.setDisable(true);		// direction will be opening
					textField_EndGreenAddit.setDisable(true);
					textField_EndYellow.setDisable(true);
					textField_EndRed.setDisable(false);
					textField_EndRedYellow.setDisable(false);
					
					textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
					textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
					checkBoxFullPromtact.setSelected(false);
					
					
				}
				
				labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue());
				
			}
			
			labelInterPhaseFrom.setText(listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue());
			labelInterPhaseTo.setText(listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxToPhase().getValue());
			
			
			//System.out.println();
			
		}
		
		
	}

	public void selectDirectionInPhase() {
		InterphaseTransitionsHBoxCell selectedInterPhase = listViewInterphase.getSelectionModel().getSelectedItem();
		specificPromtactDataMap = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact().get(selectedInterPhase);
		mapOfOpenDirInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		
		/*String fromPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue();
		String toPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxToPhase().getValue();
		
		List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromPhase = mapOfOpenDirInPhase.get(fromPhase);
		List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToPhase = mapOfOpenDirInPhase.get(toPhase);
		
		List<String> openDirectionsFrom = new ArrayList<>();
		List<String> openDirectionsTo = new ArrayList<>();
		
		if(openDirectionsFromPhase != null) {	// was edit
		
			for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsFromPhase) {
				openDirectionsFrom.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
			}
		}
		
		if(openDirectionsToPhase != null) {		// was edit
		
			for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsToPhase) {
				openDirectionsTo.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
			}	
		}
		
		
		// SAVE PROMTACTDATA TO PREVIOUS DIRECTION			
		if (previousDirection != null) {
			List<OpenDirectionInCurrentPhaseHBoxCell> fromPhaseList = mapOfOpenDirInPhase.get(fromPhase);
			List<String> fromPhaseList1 = new ArrayList<String>();
			
			if(fromPhaseList != null) {
			
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : fromPhaseList) {
					fromPhaseList1.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
				}
			}
			System.out.println("Previous # " + previousDirection);
			
			promtactData = specificPromtactDataMap.get(previousDirection);
			
			if(promtactData != null) {
			
				if(promtactData.isFullPromtact() == true) {
					promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());					// NEED EDIT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
					promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
					promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
					promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
				}else {
					if (fromPhaseList1.contains(previousDirection)) {
						promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());
						promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
						promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
					} else {
						promtactData = specificPromtactDataMap.get(previousDirection);
						promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
						promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
					}
				}
			
			}
			//clearTextFields();
			//System.out.println(specificPromtactDataMap);
		}

		//////////////////////////////////////////////////////////////////////////////////////////

		clearTextFields();
		
		PhaseDirectionsHBoxCell selectedDirection = listViewPhaseDirections.getSelectionModel().getSelectedItem();		
		
		if(selectedDirection != null) {
		
			if(selectedDirection.getComboBoxDirNumber().getValue() == null) {
				//System.out.println("full");
				
				promtactData = specificPromtactDataMap.get(selectedDirection.getComboBoxNotChangeStateDirection().getValue());
				
				if(promtactData != null) {
				
					textField_EndGreenBlink.setDisable(false);
					textField_EndGreenAddit.setDisable(false);
					textField_EndYellow.setDisable(false);
					textField_EndRed.setDisable(false);
					textField_EndRedYellow.setDisable(false);
		
					textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
					textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
					textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
					textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
					textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
					
					checkBoxFullPromtact.setSelected(true);
					
					//previousDirection = selectedDirection.getComboBoxNotChangeStateDirection().getValue();
					
					
					for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
						directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
						directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
					}
					
					
					selectedDirection.getComboBoxDirNumber().setDisable(true);
					selectedDirection.getComboBoxNotChangeStateDirection().setDisable(false);
					
					labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxNotChangeStateDirection().getValue());		
				
				}
			}else {
				//System.out.println("not full");
				
				promtactData = specificPromtactDataMap.get(selectedDirection.getComboBoxDirNumber().getValue());
				
				//previousDirection = selectedDirection.getComboBoxDirNumber().getValue();
				
				for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
					directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
					directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
				}
				
				selectedDirection.getComboBoxDirNumber().setDisable(false);
				selectedDirection.getComboBoxNotChangeStateDirection().setDisable(true);
				if(promtactData != null) {
					if (openDirectionsFrom.contains(selectedDirection.getComboBoxDirNumber().getValue())) {				// direction will be closing 
						textField_EndGreenBlink.setDisable(false);
						textField_EndGreenAddit.setDisable(false);
						textField_EndYellow.setDisable(false);
						textField_EndRed.setDisable(true);
						textField_EndRedYellow.setDisable(true);
						
						textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
						textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
						textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
						checkBoxFullPromtact.setSelected(false);
						
						
						
					} else {
						textField_EndGreenBlink.setDisable(true);		// direction will be opening
						textField_EndGreenAddit.setDisable(true);
						textField_EndYellow.setDisable(true);
						textField_EndRed.setDisable(false);
						textField_EndRedYellow.setDisable(false);
						
						textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
						textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
						checkBoxFullPromtact.setSelected(false);
						
						
					}
				}
				
				
				labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue());
				
			}
		
		}*/
		
		

	}

	public void setBasePromtact() {
		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		mapOfOpenDirInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		Map<String, PromtactData> mapOfBasePromtact = iRoadModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();

		
		if(!checkBoxFullPromtact.isSelected()) {
			
			if (listViewPhaseDirections.getItems().size() != 0) {
				
				String selectedDirection = listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue();
				
				PromtactData basePromtactData = null;
				for (Map.Entry<String, PromtactData> entry : mapOfBasePromtact.entrySet()) {
					if (entry.getKey().equals(selectedDirection)) {
						basePromtactData = entry.getValue();
					}
				}
				
				if(!basePromtactData.getRoadPromtactu_endGreenAddit().equals("") && !basePromtactData.getRoadPromtactu_durationGreenBlink().equals("") && 
						!basePromtactData.getRoadPromtactu_durationYellow().equals("") && !basePromtactData.getRoadPromtactu_endRed().equals("") &&
						!basePromtactData.getRoadPromtactu_durationRedYellow().equals("")) {
					
					List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionInCurrentPhaseHBoxCells = mapOfOpenDirInPhase.get(listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue());
					List<String> openDirectionsList = new ArrayList<>();
					for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionInCurrentPhaseHBoxCells) {
						openDirectionsList.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
					}
					
					if (openDirectionsList.contains(selectedDirection)) {
						
						specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(listViewInterphase.getSelectionModel().getSelectedItem());
						promtactData = specificPromtactDataMap.get(selectedDirection);
		
						textField_EndGreenAddit.setText(basePromtactData.getRoadPromtactu_endGreenAddit());
						textField_EndGreenBlink.setText(basePromtactData.getRoadPromtactu_durationGreenBlink());
						textField_EndYellow.setText(basePromtactData.getRoadPromtactu_durationYellow());
						
						promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());
						promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
						promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
						
						
					} else {
						specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(listViewInterphase.getSelectionModel().getSelectedItem());
						promtactData = specificPromtactDataMap.get(selectedDirection);
		
						textField_EndRed.setText(basePromtactData.getRoadPromtactu_endRed());
						textField_EndRedYellow.setText(basePromtactData.getRoadPromtactu_durationRedYellow());
						
						promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
						promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
						
					}
					
				}else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Внимание");
					alert.setHeaderText("Вам нужно задать значения базовым промтактам");
					alert.setContentText("Перейдите в 'Направления' -> кнопка 'Базовые промтакты'");
					
					Stage stage = new Stage();
					stage = (Stage)alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
					
					alert.show();
				}
				
			}else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Внимание");
				alert.setHeaderText("Создайте направление в межфазном переходе");
				
				Stage stage = new Stage();
				stage = (Stage)alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
				
				alert.show();
			}
			
			
		}else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Внимание");
			alert.setHeaderText("Установить значения базового промтакта\nдля полного промтакта - невозможно");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
		}
		
		
		
	}

	public void checkFullPromtact() {
		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(listViewInterphase.getSelectionModel().getSelectedItem());
		mapOfOpenDirInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		
		if(!checkBoxFullPromtact.isSelected()) {
			
			PhaseDirectionsHBoxCell selectedDirection = listViewPhaseDirections.getSelectionModel().getSelectedItem();
			if(selectedDirection != null) {
				
				String number = selectedDirection.getComboBoxNotChangeStateDirection().getValue();
				if(number != null) {
					specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(listViewInterphase.getSelectionModel().getSelectedItem());
					
					specificPromtactDataMap.remove(number);
					
				}
				
				selectedDirection.getComboBoxDirNumber().setDisable(false);
				selectedDirection.getComboBoxNotChangeStateDirection().setDisable(true);
				selectedDirection.getComboBoxNotChangeStateDirection().setValue(null);
				
				clearTextFields();
				
				
			}
			
			
		}else {
			
			PhaseDirectionsHBoxCell selectedDirection = listViewPhaseDirections.getSelectionModel().getSelectedItem();
			if(selectedDirection != null) {
				
				String number = selectedDirection.getComboBoxDirNumber().getValue();
				if(number != null) {
					specificPromtactDataMap = mapOfDirectionSpecificPromtact.get(listViewInterphase.getSelectionModel().getSelectedItem());
					
					specificPromtactDataMap.remove(number);
				}
				
				selectedDirection.getComboBoxNotChangeStateDirection().setDisable(false);
				selectedDirection.getComboBoxDirNumber().setDisable(true);
				selectedDirection.getComboBoxDirNumber().setValue(null);
				
				clearTextFields();
				
			}
			
		}

	}

	public void deleteInterphase() {
		System.out.println("======== Click remove interphase =========");
		System.out.println();
		
		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		interPhaseMap = iRoadModel.getModel().getRoadPromtactuModel().getInterPhaseMap();
		InterphaseTransitionsHBoxCell removingInterphase = listViewInterphase.getSelectionModel().getSelectedItem();
		//interPhaseMap.get(removingInterphase.getComboBoxFromPhase().getValue()).add(removingInterphase.getComboBoxToPhase().getValue());
		int selectInterphase = listViewInterphase.getSelectionModel().getSelectedIndex();
		if (selectInterphase >= 0) {
			
			List<String> interPhaseToList = interPhaseMap.get(removingInterphase.getComboBoxFromPhase().getValue());
			if(interPhaseToList != null) {
				if(interPhaseToList.size() > 1) {
					interPhaseMap.get(removingInterphase.getComboBoxFromPhase().getValue()).remove(removingInterphase.getComboBoxToPhase().getValue());
				}else {
					interPhaseMap.remove(removingInterphase.getComboBoxFromPhase().getValue());
				}
			}
			
			
			
			mapOfDirectionSpecificPromtact.remove(removingInterphase);
			listViewInterphase.getItems().remove(selectInterphase);
			
			clearTextFields();			
			listViewPhaseDirections.getItems().clear();
			listViewInterphase.getItems().clear();
			interphaseTransitionsHBoxCellList.clear();
			phaseDirectionsHBoxCellList.clear();
			
			for(Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()) {
				interphaseTransitionsHBoxCellList.add(entry.getKey());
				interphaseTransitionsHBoxCellObservableList = FXCollections.observableArrayList(interphaseTransitionsHBoxCellList);
				listViewInterphase.setItems(interphaseTransitionsHBoxCellObservableList);
				listViewInterphase.getSelectionModel().selectFirst();
				
			}
			
			if(listViewInterphase.getItems().size() != 0) {
				specificPromtactDataMap = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact().get(listViewInterphase.getItems().get(0));
	
				for (Map.Entry<String, PromtactData> entry : specificPromtactDataMap.entrySet()) {
					String dirNumber = entry.getKey();
					observableListDirectionsNumbers.add(dirNumber);
	
					phaseDirectionsHBoxCell = new PhaseDirectionsHBoxCell();
					phaseDirectionsHBoxCell.setObservableList(observableListDirectionsNumbers);
					phaseDirectionsHBoxCell.comboBoxChangeStateDirection.setValue(dirNumber);
					phaseDirectionsHBoxCellList.add(phaseDirectionsHBoxCell);
					
					phaseDirectionsHBoxCellObservableList = FXCollections.observableArrayList(phaseDirectionsHBoxCellList);
					listViewPhaseDirections.setItems(phaseDirectionsHBoxCellObservableList);
					listViewPhaseDirections.getSelectionModel().selectFirst();
	
				}
			
				promtactData = specificPromtactDataMap.get(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue());
				String dir = listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue();
	
				List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionInCurrentPhaseHBoxCells = mapOfOpenDirInPhase.get(listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue());
				List<String> openDirections = new ArrayList<>();
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionInCurrentPhaseHBoxCells) {
					openDirections.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
				}
				
				if (openDirections.contains(dir)) {
					textField_EndGreenAddit.setDisable(false);
					textField_EndGreenBlink.setDisable(false);
					textField_EndYellow.setDisable(false);
					textField_EndRed.setDisable(true);
					textField_EndRedYellow.setDisable(true);
	
					textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
					textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
					textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
				} else {
					textField_EndGreenAddit.setDisable(true);
					textField_EndGreenBlink.setDisable(true);
					textField_EndYellow.setDisable(true);
					textField_EndRed.setDisable(false);
					textField_EndRedYellow.setDisable(false);
	
					textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
					textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
				}
	
				labelPromtactForDirection.setText(listViewPhaseDirections.getItems().get(0).comboBoxChangeStateDirection.getValue());
			}
		}
		
	}
	
	public void deleteDirectionOfInterphase() {
		System.out.println("======== Click remove direction from interphase =========");
		System.out.println();
		int selectDirectionInterphase = listViewPhaseDirections.getSelectionModel().getSelectedIndex();
		
		previousDirection = null;
		
		if(selectDirectionInterphase >= 0) {
			String removeDirection = listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue();
			specificPromtactDataMap = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact().get(listViewInterphase.getSelectionModel().getSelectedItem());
			specificPromtactDataMap.remove(removeDirection);
			listViewPhaseDirections.getItems().remove(selectDirectionInterphase);
			phaseDirectionsHBoxCellList.remove(selectDirectionInterphase);
			clearTextFields();
		}
		listViewPhaseDirections.getSelectionModel().select(listViewPhaseDirections.getItems().size() - 1);
		
		if(listViewPhaseDirections.getItems().size() > 0) {
			
			String fromPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue();
			String toPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxToPhase().getValue();
			
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromPhase = mapOfOpenDirInPhase.get(fromPhase);
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToPhase = mapOfOpenDirInPhase.get(toPhase);
			
			List<String> openDirectionsFrom = new ArrayList<>();
			List<String> openDirectionsTo = new ArrayList<>();
			
			if(openDirectionsFromPhase != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsFromPhase) {
					openDirectionsFrom.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
				}
			}
			
			if(openDirectionsToPhase != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsToPhase) {
					openDirectionsTo.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
				}
			}
			
			PhaseDirectionsHBoxCell selectedDirection = listViewPhaseDirections.getSelectionModel().getSelectedItem();
			
			if(selectedDirection.getComboBoxDirNumber().getValue() == null) {
				//System.out.println("full");
				
				promtactData = specificPromtactDataMap.get(selectedDirection.getComboBoxNotChangeStateDirection().getValue());
				
				textField_EndGreenBlink.setDisable(false);
				textField_EndGreenAddit.setDisable(false);
				textField_EndYellow.setDisable(false);
				textField_EndRed.setDisable(false);
				textField_EndRedYellow.setDisable(false);

				textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
				textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
				textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
				textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
				textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
				
				checkBoxFullPromtact.setSelected(true);
				
				previousDirection = selectedDirection.getComboBoxNotChangeStateDirection().getValue();
				
				
				for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
					directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
					directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
				}
				
				
				selectedDirection.getComboBoxDirNumber().setDisable(true);
				selectedDirection.getComboBoxNotChangeStateDirection().setDisable(false);
				
				labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxNotChangeStateDirection().getValue());
				
			}else {
				//System.out.println("not full");
				
				promtactData = specificPromtactDataMap.get(selectedDirection.getComboBoxDirNumber().getValue());
				
				previousDirection = selectedDirection.getComboBoxDirNumber().getValue();
				
				for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
					directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
					directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
				}
				
				selectedDirection.getComboBoxDirNumber().setDisable(false);
				selectedDirection.getComboBoxNotChangeStateDirection().setDisable(true);
				
				if (openDirectionsFrom.contains(selectedDirection.getComboBoxDirNumber().getValue())) {				// direction will be closing 
					textField_EndGreenBlink.setDisable(false);
					textField_EndGreenAddit.setDisable(false);
					textField_EndYellow.setDisable(false);
					textField_EndRed.setDisable(true);
					textField_EndRedYellow.setDisable(true);
					
					textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
					textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
					textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
					checkBoxFullPromtact.setSelected(false);
					
					
					
				} else {
					textField_EndGreenBlink.setDisable(true);		// direction will be opening
					textField_EndGreenAddit.setDisable(true);
					textField_EndYellow.setDisable(true);
					textField_EndRed.setDisable(false);
					textField_EndRedYellow.setDisable(false);
					
					textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
					textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
					checkBoxFullPromtact.setSelected(false);
					
					
				}
				
				labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue());
				
			}
			
			
		}else {
			visibleField();
		}
				
		
		
	}
	
	public void openPromtactTable() {
		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/componentTreeView/Specific_promtact_table.fxml"));
			Pane object = fxmlLoader.load();
			
			SpecificPromtactTablePresenter specificPromtactTablePresenter = fxmlLoader.getController();
			
			specificPromtactTablePresenter.mapOfDirection(mapOfDirectionSpecificPromtact);
			specificPromtactTablePresenter.listRoadDirection(iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList());
			specificPromtactTablePresenter.setItemsTableView();
			
			Scene scene = new Scene(object);

			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Общая таблица базовых промтактов");
			stage.setScene(scene);
			stage.showAndWait();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void initialize() {

		//langXML();
		//loadLang(langXML);
		
		listViewInterphase.setPlaceholder(new Label("Нет данных для отображения"));
		listViewPhaseDirections.setPlaceholder(new Label("Нет данных для отображения"));
		
		listViewPhaseDirections.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {		// set text on label direction
			if(newValue != null) {
				labelPromtactForDirection.setText(newValue.getComboBoxDirNumber().getValue());
			}else {
				labelPromtactForDirection.setText("");
			}
		});
		
		listViewInterphase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {				// set text on label interphase 
			if(newValue != null) {
				labelInterPhaseFrom.setText(newValue.getComboBoxFromPhase().getValue());
				labelInterPhaseTo.setText(newValue.getComboBoxToPhase().getValue());
			}else {
				labelInterPhaseFrom.setText("");
				labelInterPhaseTo.setText("");
			}
		});
		
		
		listViewPhaseDirections.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				previousDirection = oldValue.getComboBoxDirNumber().getValue();
				
				if(previousDirection != null) {
					promtactData = specificPromtactDataMap.get(previousDirection);
					promtactData.setRoadPromtactu_endGreenAddit(textField_EndGreenAddit.getText());
					promtactData.setRoadPromtactu_durationGreenBlink(textField_EndGreenBlink.getText());
					promtactData.setRoadPromtactu_durationYellow(textField_EndYellow.getText());
					promtactData.setRoadPromtactu_durationRedYellow(textField_EndRedYellow.getText());
					promtactData.setRoadPromtactu_endRed(textField_EndRed.getText());
					
				}
				
			}
			
			if(newValue != null) {
				
				String fromPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxFromPhase().getValue();
				String toPhase = listViewInterphase.getSelectionModel().getSelectedItem().getComboBoxToPhase().getValue();
				
				List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromPhase = mapOfOpenDirInPhase.get(fromPhase);
				List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToPhase = mapOfOpenDirInPhase.get(toPhase);
				
				List<String> openDirectionsFrom = new ArrayList<>();
				List<String> openDirectionsTo = new ArrayList<>();
				
				if(openDirectionsFromPhase != null) {	// was edit
				
					for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsFromPhase) {
						openDirectionsFrom.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
					}
				}
				
				if(openDirectionsToPhase != null) {		// was edit
				
					for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsToPhase) {
						openDirectionsTo.add(openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue());
					}	
				}
				
				clearTextFields();
				
				
				if(newValue.getComboBoxDirNumber().getValue() == null) {
					//System.out.println("full");
					
					promtactData = specificPromtactDataMap.get(newValue.getComboBoxNotChangeStateDirection().getValue());
					
					if(promtactData != null) {
					
						textField_EndGreenBlink.setDisable(false);
						textField_EndGreenAddit.setDisable(false);
						textField_EndYellow.setDisable(false);
						textField_EndRed.setDisable(false);
						textField_EndRedYellow.setDisable(false);
			
						textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
						textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
						textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
						textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
						textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
						
						checkBoxFullPromtact.setSelected(true);
						
						//previousDirection = selectedDirection.getComboBoxNotChangeStateDirection().getValue();
						
						
						for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
							directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
							directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
						}
						
						
						newValue.getComboBoxDirNumber().setDisable(true);
						newValue.getComboBoxNotChangeStateDirection().setDisable(false);
						
						labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxNotChangeStateDirection().getValue());		
					
					}
				}else {
					//System.out.println("not full");
					
					promtactData = specificPromtactDataMap.get(newValue.getComboBoxDirNumber().getValue());
					
					//previousDirection = selectedDirection.getComboBoxDirNumber().getValue();
					
					for(PhaseDirectionsHBoxCell directionsHBoxCell : listViewPhaseDirections.getItems()) {
						directionsHBoxCell.getComboBoxDirNumber().setDisable(true);
						directionsHBoxCell.getComboBoxNotChangeStateDirection().setDisable(true);
					}
					
					newValue.getComboBoxDirNumber().setDisable(false);
					newValue.getComboBoxNotChangeStateDirection().setDisable(true);
					if(promtactData != null) {
						if (openDirectionsFrom.contains(newValue.getComboBoxDirNumber().getValue())) {				// direction will be closing 
							textField_EndGreenBlink.setDisable(false);
							textField_EndGreenAddit.setDisable(false);
							textField_EndYellow.setDisable(false);
							textField_EndRed.setDisable(true);
							textField_EndRedYellow.setDisable(true);
							
							textField_EndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
							textField_EndGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
							textField_EndYellow.setText(promtactData.getRoadPromtactu_durationYellow());
							checkBoxFullPromtact.setSelected(false);
							
							
							
						} else {
							textField_EndGreenBlink.setDisable(true);		// direction will be opening
							textField_EndGreenAddit.setDisable(true);
							textField_EndYellow.setDisable(true);
							textField_EndRed.setDisable(false);
							textField_EndRedYellow.setDisable(false);
							
							textField_EndRed.setText(promtactData.getRoadPromtactu_endRed());
							textField_EndRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
							checkBoxFullPromtact.setSelected(false);							
							
						}
					}
					
					labelPromtactForDirection.setText(listViewPhaseDirections.getSelectionModel().getSelectedItem().getComboBoxDirNumber().getValue());
					
				}
				
			}
			
		});
		
		
		listViewInterphase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				for(InterphaseTransitionsHBoxCell interphaseTransitionsHBoxCell : interphaseTransitionsHBoxCellObservableList) {
					interphaseTransitionsHBoxCell.getComboBoxFromPhase().setDisable(true);
					interphaseTransitionsHBoxCell.getComboBoxToPhase().setDisable(true);
				}
				newValue.getComboBoxFromPhase().setDisable(false);
				newValue.getComboBoxToPhase().setDisable(false);
			}
		});

		listViewPhaseDirections.getSelectionModel().selectedIndexProperty().addListener((observable, oldIndex, newIndex) -> {
			previousIndex = oldIndex.intValue();
		});

		listViewInterphase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			previousInterphaseTransitionsHBoxCell = oldValue;
			//System.out.println("Old value interphase " + oldValue);
		});
		
		textField_EndGreenAddit.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textField_EndGreenAddit.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
    	
		textField_EndGreenBlink.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textField_EndGreenBlink.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
    	
		textField_EndYellow.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textField_EndYellow.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
    	
		textField_EndRed.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textField_EndRed.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
    	
		textField_EndRedYellow.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textField_EndRedYellow.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });

	}

}
