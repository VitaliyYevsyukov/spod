package presenters.phase;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import presenters.directions.RoadDirection;
import presenters.object.TypeKDK;
import presenters.programs.PhaseInProgram;
import presenters.programs.RoadProgram;
import presenters.promtactu.InterphaseTransitionsHBoxCell;
import presenters.promtactu.PromtactData;
import roadModel.IRoadModel;

/**
 * Created by Vitaly on 06.12.2016.
 */
public class RoadPhasePresenter {

	@FXML
	private TableView<RoadPhase> tableViewPhase;
	@FXML
	private TableColumn<RoadPhase, String> tableColumnNumber, tableColumnTmin, tableColumnPanelTVP_1, tableColumnPanelTVP_2;
	@FXML
	private TableColumn<RoadPhase, TVP> tableColumnPhaseTVP;
	@FXML
	private Button btnCreatePhase, btnDeletePhase, btnCreateDirectionInPhase, btnDeleteDirectionInPhase, buttonPrevious, buttonNext;
	@FXML
	private Label labelPhase, labelListOfAllPhasesOfControllerOperation, labelPhaseOpenDirect, labelDirectionInPhase, labelDirInPhaseValue;
	@FXML
	private ListView<OpenDirectionInCurrentPhaseHBoxCell> listViewOpenDirectionInCurrentPhase;
	@FXML
	private ImageView imageViewLight;

	@FXML
	//private static ResourceBundle bundleGUI, bundleAlert;
	//private static Locale localeGUI, localeAlert;
	//static String langXML = null;

	RoadPhase roadPhase;
	private IRoadModel iRoadModel;

	Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> unsortedMapOfOpenDirectionInPhase;
	Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOfOpenDirectionInPhase;
	Map<String, String> mapOfPhaseTmin;
	Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram;
	Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfDirectionSpecificPromtact;

	List<CurrentPhaseHBoxCell> currentPhaseHBoxCellList = new ArrayList<>();
	List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionInCurrentPhaseHBoxCellList = new ArrayList<>();

	CurrentPhaseHBoxCell currentPhaseHBoxCell;
	OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell;

	ObservableList<CurrentPhaseHBoxCell> currentPhaseHBoxCellObservableList;
	ObservableList<OpenDirectionInCurrentPhaseHBoxCell> openDirectionInCurrentPhaseHBoxCellObservableList;
	ObservableList<String> valuesOfComboBox;

	ComboBox<String> comboBox;

	OpenDirectionInCurrentPhaseHBoxCell newOpenDirHBoxCell;
	String createPhaseNumber = null;

	int indexDir;
	int previousIndex;
	
	OpenDirectionInCurrentPhaseHBoxCell previousOpenDirectionInPhaseHBoxCell;
	RoadPhase previousRoadPhase;

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

		btnCreatePhase.setText(bundleGUI.getString("buttonCreate"));
		btnCreateDirectionInPhase.setText(bundleGUI.getString("buttonCreate"));
		btnDeletePhase.setText(bundleGUI.getString("buttonDelete"));
		btnDeleteDirectionInPhase.setText(bundleGUI.getString("buttonDelete"));
		labelPhase.setText(bundleGUI.getString("labelPhase"));
		labelListOfAllPhasesOfControllerOperation.setText(bundleGUI.getString("labelListOfAllPhasesOfControllerOperation"));
		labelPhaseOpenDirect.setText(bundleGUI.getString("labelPhaseOpenDirect"));
		labelDirectionInPhase.setText(bundleGUI.getString("labelPhaseInDirection"));
		tableColumnNumber.setText(bundleGUI.getString("tableColumnNumber"));
		tableColumnTmin.setText(bundleGUI.getString("tableColumnPhaseTmin"));
		tableColumnPanelTVP_1.setText(bundleGUI.getString("tableColumnPhasePanelTVP_1"));
		tableColumnPanelTVP_2.setText(bundleGUI.getString("tableColumnPhasePanelTVP_2"));
		tableColumnPhaseTVP.setText(bundleGUI.getString("tableColumnPhaseTVP"));
	}*/

	/*private static void alertLang(String lang) {
		localeAlert = new Locale(lang);
		bundleAlert = ResourceBundle.getBundle("localeAlert_lang", localeAlert);
	}*/
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void show(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;
		ObservableList<String> existedOpenDirectionsList = FXCollections.observableArrayList();
		existedOpenDirectionsList.clear(); 
		
		if (iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK().equals("")) {
			btnCreatePhase.setDisable(true);
		}		
		
		if(iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList().size() > 0) {
			tableViewPhase.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList()));
			tableViewPhase.getSelectionModel().selectFirst();
		}
		
		unsortedMapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		
		//////////////////////////////////////////////
		//////// SORT OPEN DIRECTION IN PHASE ////////
		//////////////////////////////////////////////		
		mapOfOpenDirectionInPhase = new LinkedHashMap<>();
		Map<String, List<Integer>> sortMap = new LinkedHashMap<>();
		for(Map.Entry<String, List<OpenDirectionInCurrentPhaseHBoxCell>> entry : unsortedMapOfOpenDirectionInPhase.entrySet()) {
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsInPhase = entry.getValue();
			String phaseNumber = entry.getKey();
			List<Integer> sortedList = new ArrayList<Integer>();
			for(OpenDirectionInCurrentPhaseHBoxCell openDirection : openDirectionsInPhase) {
				sortedList.add(Integer.parseInt(openDirection.getComboBox().getValue()));
			}
			
			Collections.sort(sortedList);	// sort integers
			
			sortMap.put(phaseNumber, sortedList);
			
		}
		
		for(Map.Entry<String, List<Integer>> entry : sortMap.entrySet()) {
			String phaseNumber = entry.getKey();
			List<Integer> openDirectionsList = entry.getValue();
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionAfterSort = new ArrayList<>();
			for(Integer value : openDirectionsList) {
				OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell = new OpenDirectionInCurrentPhaseHBoxCell();
				openDirectionInCurrentPhaseHBoxCell.getComboBox().setValue(value.toString());
				openDirectionAfterSort.add(openDirectionInCurrentPhaseHBoxCell);
			}			
			mapOfOpenDirectionInPhase.put(phaseNumber, openDirectionAfterSort);			
		}
		///////////////////////////////////////////////////////
		
		iRoadModel.getModel().getRoadPhaseModel().setMapOpenDirectionInPhase(mapOfOpenDirectionInPhase);
			
		mapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		
		if(!mapOfOpenDirectionInPhase.isEmpty()) {
			
			// all existed directions
			valuesOfComboBox = FXCollections.observableArrayList();
			for (RoadDirection roadDirection : iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList()) {
				String dirNumber = roadDirection.getRoadDirections_number();
				valuesOfComboBox.add(dirNumber);
			}
			////////////////////////////
			
			listViewOpenDirectionInCurrentPhase.getItems().clear();
			
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsList = mapOfOpenDirectionInPhase.get(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());
			
			for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsList) {
				String openDir = openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue();
				existedOpenDirectionsList.add(openDir);
			}
			
			valuesOfComboBox.removeAll(existedOpenDirectionsList);
			
			for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsList) {
				if(valuesOfComboBox.size() != openDirectionInCurrentPhaseHBoxCell.getComboBox().getItems().size()) {
					openDirectionInCurrentPhaseHBoxCell.getComboBox().getItems().clear();
					openDirectionInCurrentPhaseHBoxCell.setObservableListComboBox(valuesOfComboBox);
				}
				openDirectionInCurrentPhaseHBoxCellList.add(openDirectionInCurrentPhaseHBoxCell);
				openDirectionInCurrentPhaseHBoxCellObservableList = FXCollections.observableList(openDirectionInCurrentPhaseHBoxCellList);
				listViewOpenDirectionInCurrentPhase.setItems(openDirectionInCurrentPhaseHBoxCellObservableList);
				listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
			}
			
			labelDirInPhaseValue.setText(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());
			
			if(valuesOfComboBox.size() != 0) {
				btnCreateDirectionInPhase.setDisable(false);
			}
			
		}else {
			valuesOfComboBox = FXCollections.observableArrayList();
			for (RoadDirection roadDirection : iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList()) {
				String dirNumber = roadDirection.getRoadDirections_number();
				valuesOfComboBox.add(dirNumber);
			}
			
		}
		
		/*List<RoadPhase> roadPhaseList = iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList(); 
		
		if (iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK().equals("")) {
			btnCreatePhase.setDisable(true);
		}

		boolean isDirShow = false;
		mapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();

		tableViewPhase.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList()));
		tableViewPhase.getSelectionModel().selectFirst();
		
		valuesOfComboBox = FXCollections.observableArrayList();
		
		for (RoadDirection roadDirection : iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList()) {
			String keys = roadDirection.getRoadDirections_number();
			valuesOfComboBox.add(keys);
		}
		
		listViewOpenDirectionInCurrentPhase.getItems().clear();
		
		if(!tableViewPhase.getItems().isEmpty()) {
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsList = mapOfOpenDirectionInPhase.get(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());
			if(openDirectionsList != null) {
				
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsList) {
					openDirectionInCurrentPhaseHBoxCell.setObservableListComboBox(valuesOfComboBox);
					openDirectionInCurrentPhaseHBoxCellList.add(openDirectionInCurrentPhaseHBoxCell);
					openDirectionInCurrentPhaseHBoxCellObservableList = FXCollections.observableList(openDirectionInCurrentPhaseHBoxCellList);
					listViewOpenDirectionInCurrentPhase.setItems(openDirectionInCurrentPhaseHBoxCellObservableList);
					listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
				}
				labelDirInPhaseValue.setText(listViewOpenDirectionInCurrentPhase.getSelectionModel().getSelectedItem().getComboBox().getValue());
				btnCreateDirectionInPhase.setDisable(false);
			}else {
				listViewOpenDirectionInCurrentPhase.getItems().clear();
			}
		}

		if (tableViewPhase.getItems().size() > 0) {
			String selectedPhase = tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number();
			labelDirInPhaseValue.setText(selectedPhase);
			btnCreateDirectionInPhase.setDisable(false);
		}*/
	}

	public void save(IRoadModel iRoadModel) {
		mapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		
		if(tableViewPhase.getItems().size() > 0) {
			String previousNumber = tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number();
			int index = listViewOpenDirectionInCurrentPhase.getSelectionModel().getSelectedIndex();
			if(index >= 0) {
				mapOfOpenDirectionInPhase.get(previousNumber).set(index, listViewOpenDirectionInCurrentPhase.getSelectionModel().getSelectedItem());
			}
		}
	}

	public void selectTableViewItem() {	
		ObservableList<String> existedOpenDirectionsList = FXCollections.observableArrayList();
		
		mapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		
		if(previousRoadPhase != null) {
			String previousNumber = previousRoadPhase.getRoadPhase_number();
			int index = listViewOpenDirectionInCurrentPhase.getSelectionModel().getSelectedIndex();
			if(index >= 0) {
				mapOfOpenDirectionInPhase.get(previousNumber).set(index, listViewOpenDirectionInCurrentPhase.getSelectionModel().getSelectedItem());
			}
		}		
		labelDirInPhaseValue.setText(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());
		listViewOpenDirectionInCurrentPhase.getItems().clear();		
		
		// all existed directions
		valuesOfComboBox = FXCollections.observableArrayList();
		for (RoadDirection roadDirection : iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList()) {
			String dirNumber = roadDirection.getRoadDirections_number();
			valuesOfComboBox.add(dirNumber);
		}
		////////////////////////////		
		
		List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsList = mapOfOpenDirectionInPhase.get(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());		
		
		for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsList) {
			String openDir = openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue();
			existedOpenDirectionsList.add(openDir);
		}
		
		valuesOfComboBox.removeAll(existedOpenDirectionsList);
		
		if(openDirectionsList != null) {
			for(OpenDirectionInCurrentPhaseHBoxCell numberDirection : openDirectionsList) {				
				//numberDirection.getComboBox().getItems().clear();
				//numberDirection.setObservableListComboBox(valuesOfComboBox);
				openDirectionInCurrentPhaseHBoxCellList.add(numberDirection);				
				openDirectionInCurrentPhaseHBoxCellObservableList = FXCollections.observableList(openDirectionInCurrentPhaseHBoxCellList);
				listViewOpenDirectionInCurrentPhase.setItems(openDirectionInCurrentPhaseHBoxCellObservableList);
				listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
			}
			labelDirInPhaseValue.setText(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());
		}else {
			listViewOpenDirectionInCurrentPhase.getItems().clear();
		}
	}

	/*public void selectPhaseItem() {
		/int indexOfSelectedRow = listViewCurrentPhase.getSelectionModel().getSelectedIndex();
		tableViewPhase.getSelectionModel().select(indexOfSelectedRow);
		listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
		
		String currentPhaseNumber = tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number();
		
		if(mapOfOpenDirectionInPhase.get(currentPhaseNumber).isEmpty()){
			  mapOfOpenDirectionInPhase.get(currentPhaseNumber).add(newOpenDirHBoxCell);
		  }
		
		for (Map.Entry<String, List<OpenDirectionInCurrentPhaseHBoxCell>> entry : mapOfOpenDirectionInPhase.entrySet()) {
			String number = entry.getKey();

			if (currentPhaseNumber.equals(number)) {

				listViewOpenDirectionInCurrentPhase.getItems().clear();

				for (OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : entry.getValue()) {

					openDirectionInCurrentPhaseHBoxCellList.add(openDirectionInCurrentPhaseHBoxCell);
					openDirectionInCurrentPhaseHBoxCellObservableList = FXCollections.observableList(openDirectionInCurrentPhaseHBoxCellList);
					listViewOpenDirectionInCurrentPhase.setItems(openDirectionInCurrentPhaseHBoxCellObservableList);
					listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
				}
			}
		}
		labelDirInPhaseValue.setText(currentPhaseNumber);
	}*/

	public void selectOpenDirItem() {
		mapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();	
		
		if(!mapOfOpenDirectionInPhase.isEmpty()) {
			
			OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell = listViewOpenDirectionInCurrentPhase.getSelectionModel().getSelectedItem();
			
			
			
			/*openDirectionInCurrentPhaseHBoxCell.getComboBox().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue.equals(oldValue)) {
					openDirectionInCurrentPhaseHBoxCell.getComboBox().getItems().add(oldValue);
				}
			});*/
			
			
		}
		
		//OpenDirectionInCurrentPhaseHBoxCell newValue = previousOpenDirectionInPhaseHBoxCell;
		//List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsList = mapOfOpenDirectionInPhase.get(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());
		//openDirectionsList.set(previousIndex, newValue);
		
	}

	public void createNewPhase() {
		System.out.println("============= Press create phase =============");
		System.out.println();
		
		List<RoadPhase> roadPhasesList = iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList();
		mapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		
		if(roadPhasesList.size() == 0) {
			String phaseNumber = Integer.toString(roadPhasesList.size() + 1);
			
			String tMinDefault = "10";
			
			roadPhase = new RoadPhase();
			roadPhase.setRoadPhase_number(phaseNumber);
			roadPhase.setRoadPhase_Tmin(tMinDefault);
			roadPhase.setRoadPhase_panelTVP_1("");
			roadPhase.setRoadPhase_panelTVP_2("");
			roadPhase.setRoadPhase_phaseTVP(new TVP("Отсутствует"));
			
			iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList().add(roadPhase);
			
			tableViewPhase.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList()));
			tableViewPhase.getSelectionModel().select(tableViewPhase.getItems().size() - 1);
			
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionInPhaseList = new ArrayList<>();
			
			mapOfOpenDirectionInPhase.put(phaseNumber, openDirectionInPhaseList);
			
			listViewOpenDirectionInCurrentPhase.getItems().clear();
			btnCreateDirectionInPhase.setDisable(false);
			labelDirInPhaseValue.setText(phaseNumber);
		
		}else {
			
			String typeKDK = iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK();
			List<TypeKDK> typeKDKsList = iRoadModel.getModel().getRoadObjectModel().getKdkTypeList();
			List<Integer> allNumbersOfPhaseList = new ArrayList<>();
			
			for(RoadPhase roadPhase : roadPhasesList) {
				allNumbersOfPhaseList.add(Integer.parseInt(roadPhase.getRoadPhase_number()));
			}
			
			String phaseNumber = Integer.toString(Collections.max(allNumbersOfPhaseList) + 1);		//number for large phase number + 1
			for (TypeKDK existedKDKType : typeKDKsList) {
				if (existedKDKType.getName_KDK().equals(typeKDK)) {
					int max_value = Integer.parseInt(existedKDKType.getPhases()) - 1;
					
						if (Integer.parseInt(phaseNumber) <= max_value) {
						
						String tMinDefault = "10";
						
						roadPhase = new RoadPhase();
						roadPhase.setRoadPhase_number(phaseNumber);
						roadPhase.setRoadPhase_Tmin(tMinDefault);
						roadPhase.setRoadPhase_phaseTVP(new TVP("Отсутствует"));
						roadPhase.setRoadPhase_panelTVP_1("");
						roadPhase.setRoadPhase_panelTVP_2("");
						
						iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList().add(roadPhase);
						
						List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionInPhaseList = new ArrayList<>();
						
						tableViewPhase.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList()));
						tableViewPhase.getSelectionModel().select(tableViewPhase.getItems().size() - 1);
						
						mapOfOpenDirectionInPhase.put(phaseNumber, openDirectionInPhaseList);
						
						listViewOpenDirectionInCurrentPhase.getItems().clear();
						
						btnCreateDirectionInPhase.setDisable(false);
						labelDirInPhaseValue.setText(phaseNumber);
					}else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Ошибка");
						alert.setHeaderText("Тип контроллера не позволяет создать больше " + max_value + " фаз");
						//alert.setContentText("Тип контроллера не позволяет создать больше " + max_value + " фаз");
						
						Stage stage = new Stage();
						stage = (Stage)alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
						
						alert.show();
					}
				}
			}			
		}
	}

	public void deleteSelectPhase() {
		//alertLang(langXML);
		System.out.println("Press delete phase");

		int selectPhase = tableViewPhase.getSelectionModel().getSelectedIndex();
		String phaseNumber = tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number();
		mapOfPhasesInProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		mapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		
		if (selectPhase >= 0) {
			tableViewPhase.getItems().remove(selectPhase);
			tableViewPhase.getSelectionModel().selectFirst();
			iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList().remove(selectPhase);
			mapOfOpenDirectionInPhase.remove(phaseNumber);
			ObservableList<String> existedOpenDirectionsList = FXCollections.observableArrayList();	
			
			listViewOpenDirectionInCurrentPhase.getItems().clear();

			listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
			String phaseNumberAfterDelete = tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number();
			labelDirInPhaseValue.setText(phaseNumberAfterDelete);
			
			// all existed directions
			valuesOfComboBox = FXCollections.observableArrayList();
			for (RoadDirection roadDirection : iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList()) {
				String dirNumber = roadDirection.getRoadDirections_number();
				valuesOfComboBox.add(dirNumber);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////			
						
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsList = mapOfOpenDirectionInPhase.get(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());			
						
			for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsList) {
				String openDir = openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue();
				existedOpenDirectionsList.add(openDir);
			}
						
			valuesOfComboBox.removeAll(existedOpenDirectionsList);			
						
			if(openDirectionsList != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell numberDirection : openDirectionsList) {
								
					numberDirection.getComboBox().getItems().clear();
					numberDirection.setObservableListComboBox(valuesOfComboBox);
					openDirectionInCurrentPhaseHBoxCellList.add(numberDirection);				
					openDirectionInCurrentPhaseHBoxCellObservableList = FXCollections.observableList(openDirectionInCurrentPhaseHBoxCellList);
					listViewOpenDirectionInCurrentPhase.setItems(openDirectionInCurrentPhaseHBoxCellObservableList);
					listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
				}
				labelDirInPhaseValue.setText(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());
			}else {
				listViewOpenDirectionInCurrentPhase.getItems().clear();
			}
			
			if(!mapOfPhasesInProgram.isEmpty()) { 	// remove phase from 'phase in program' 
				for(Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {
					List<PhaseInProgram> phaseInProgramList = entry.getValue();
					if(!phaseInProgramList.isEmpty()) {
						List<PhaseInProgram> listAfterRemove = new ArrayList<>();
						for(PhaseInProgram phaseInProgram : phaseInProgramList) {
							String number = phaseInProgram.getPhaseInProgramNumber().getPhaseNumber();
							if(!number.equals(phaseNumber)) {
								listAfterRemove.add(phaseInProgram);
							}
						}
						mapOfPhasesInProgram.put(entry.getKey(), listAfterRemove);
					}
				}
			}
			
			if(!mapOfDirectionSpecificPromtact.isEmpty()) {		// remove phase from promtact interphase
				Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> afterDelete = new LinkedHashMap<>(mapOfDirectionSpecificPromtact);
				for(Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()) {
					InterphaseTransitionsHBoxCell interPhase = entry.getKey();
					if(interPhase.getComboBoxFromPhase().getValue().equals(phaseNumber) || interPhase.getComboBoxToPhase().getValue().equals(phaseNumber)) {
						afterDelete.remove(interPhase);
					}
				}
				iRoadModel.getModel().getRoadPromtactuModel().setMapOfInterphaseSpecificPromtact(afterDelete);
			}
			
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Выберите фазу для удаления");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.showAndWait();
		}

	}

	public void createNewDirectionInPhase() {
		if(!valuesOfComboBox.isEmpty()) {
									
			ObservableList<String> existedDirection = FXCollections.observableArrayList(valuesOfComboBox);
	
			mapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
			
			//add unused directions in ComboBox
			for (OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : listViewOpenDirectionInCurrentPhase.getItems()) {
				String existedDir = openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue();
				existedDirection.removeIf(direction -> direction.equals(existedDir));
			}
			
			String numberPhase = tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number();
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsList = mapOfOpenDirectionInPhase.get(numberPhase);
			
			OpenDirectionInCurrentPhaseHBoxCell previousOpenDirectionInCurrentPhaseHBoxCell = listViewOpenDirectionInCurrentPhase.getSelectionModel().getSelectedItem();
			
			if(previousOpenDirectionInCurrentPhaseHBoxCell != null) {
				if(previousOpenDirectionInCurrentPhaseHBoxCell.getComboBox().getValue() != null) {
					if(!existedDirection.isEmpty()) {
						if(openDirectionsList == null) {
							//System.out.println("must create new list");
							//openDirectionsList = new ArrayList<>();
							//mapOfOpenDirectionInPhase.put(numberPhase, openDirectionsList);
							
							openDirectionInCurrentPhaseHBoxCell = new OpenDirectionInCurrentPhaseHBoxCell();
							openDirectionInCurrentPhaseHBoxCell.setObservableListComboBox(existedDirection);
							openDirectionInCurrentPhaseHBoxCellList.add(openDirectionInCurrentPhaseHBoxCell);
							openDirectionInCurrentPhaseHBoxCellObservableList = FXCollections.observableList(openDirectionInCurrentPhaseHBoxCellList);
							listViewOpenDirectionInCurrentPhase.setItems(openDirectionInCurrentPhaseHBoxCellObservableList);
							openDirectionInCurrentPhaseHBoxCell.getComboBox().setDisable(false);
							listViewOpenDirectionInCurrentPhase.getSelectionModel().select(listViewOpenDirectionInCurrentPhase.getItems().size() - 1);
							mapOfOpenDirectionInPhase.get(numberPhase).add(openDirectionInCurrentPhaseHBoxCell);
							
						}else {
							openDirectionInCurrentPhaseHBoxCell = new OpenDirectionInCurrentPhaseHBoxCell();
							openDirectionInCurrentPhaseHBoxCell.setObservableListComboBox(existedDirection);
							openDirectionInCurrentPhaseHBoxCellList.add(openDirectionInCurrentPhaseHBoxCell);
							openDirectionInCurrentPhaseHBoxCellObservableList = FXCollections.observableList(openDirectionInCurrentPhaseHBoxCellList);
							listViewOpenDirectionInCurrentPhase.setItems(openDirectionInCurrentPhaseHBoxCellObservableList);
							openDirectionInCurrentPhaseHBoxCell.getComboBox().setDisable(false);
							listViewOpenDirectionInCurrentPhase.getSelectionModel().select(listViewOpenDirectionInCurrentPhase.getItems().size() - 1);
							mapOfOpenDirectionInPhase.get(numberPhase).add(openDirectionInCurrentPhaseHBoxCell);
						}
					}else {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Внимание");
						alert.setHeaderText("Все доступные направления использованы");
						
						Stage stage = new Stage();
						stage = (Stage)alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
						
						alert.show();
					}
				}else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Внимание");
					alert.setHeaderText("Укажите номер направления");
					
					Stage stage = new Stage();
					stage = (Stage)alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
					
					alert.show();
				}
			}else {
				openDirectionInCurrentPhaseHBoxCell = new OpenDirectionInCurrentPhaseHBoxCell();
				openDirectionInCurrentPhaseHBoxCell.setObservableListComboBox(existedDirection);
				openDirectionInCurrentPhaseHBoxCellList.add(openDirectionInCurrentPhaseHBoxCell);
				openDirectionInCurrentPhaseHBoxCellObservableList = FXCollections.observableList(openDirectionInCurrentPhaseHBoxCellList);
				listViewOpenDirectionInCurrentPhase.setItems(openDirectionInCurrentPhaseHBoxCellObservableList);
				openDirectionInCurrentPhaseHBoxCell.getComboBox().setDisable(false);
				listViewOpenDirectionInCurrentPhase.getSelectionModel().select(listViewOpenDirectionInCurrentPhase.getItems().size() - 1);
				
				//openDirectionsList = new ArrayList<>();
				//openDirectionsList.add(openDirectionInCurrentPhaseHBoxCell);
				//mapOfOpenDirectionInPhase.put(numberPhase, openDirectionsList);
				mapOfOpenDirectionInPhase.get(numberPhase).add(openDirectionInCurrentPhaseHBoxCell);
							
			}
		}else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Внимание");
			alert.setHeaderText("Создайте направления");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
		}
	}

	public void deleteDirectionInPhase() {
		//System.out.println("Press delete open direction\n" + " Map before delete direction\n" + mapOfOpenDirectionInPhase);

		String phaseKey = tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number();
		int dirToDelete = listViewOpenDirectionInCurrentPhase.getSelectionModel().getSelectedIndex();

		if (dirToDelete >= 0) {
			listViewOpenDirectionInCurrentPhase.getItems().remove(dirToDelete);
			mapOfOpenDirectionInPhase.get(phaseKey).remove(dirToDelete);
		}
		//System.out.println("Map after delete direction\n" + mapOfOpenDirectionInPhase);
	}

	public void buttonNextEvent() {
		listViewOpenDirectionInCurrentPhase.getItems().clear();
		
		if(!tableViewPhase.getItems().isEmpty()) {
			mapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
			int index = tableViewPhase.getSelectionModel().getSelectedIndex();
			int finalIndex = tableViewPhase.getItems().size() - 1;
			ObservableList<String> existedOpenDirectionsList = FXCollections.observableArrayList();
			
			if(index == finalIndex) {
				tableViewPhase.getSelectionModel().select(0);
				listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
				labelDirInPhaseValue.setText(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());				
			}else {
				
				tableViewPhase.getSelectionModel().select(tableViewPhase.getSelectionModel().getSelectedIndex() + 1);
				//listViewCurrentPhase.getSelectionModel().select(listViewCurrentPhase.getSelectionModel().getSelectedIndex() + 1);
				listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
	
				/////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////// SET CURRENT NUMBER TO LABEL ///////////////////////////////////////
				/////////////////////////////////////////////////////////////////////////////////////////////
				String phaseNumber = tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number();
				labelDirInPhaseValue.setText(phaseNumber);				
			}
			
			// all existed directions
			valuesOfComboBox = FXCollections.observableArrayList();
			for(RoadDirection roadDirection : iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList()) {
				String dirNumber = roadDirection.getRoadDirections_number();
				valuesOfComboBox.add(dirNumber);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////			
						
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsList = mapOfOpenDirectionInPhase.get(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());			
						
			for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsList) {
				String openDir = openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue();
				existedOpenDirectionsList.add(openDir);
			}
						
			valuesOfComboBox.removeAll(existedOpenDirectionsList);			
						
			if(openDirectionsList != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell numberDirection : openDirectionsList) {								
					//numberDirection.getComboBox().getItems().clear();
					//numberDirection.setObservableListComboBox(valuesOfComboBox);
					openDirectionInCurrentPhaseHBoxCellList.add(numberDirection);				
					openDirectionInCurrentPhaseHBoxCellObservableList = FXCollections.observableList(openDirectionInCurrentPhaseHBoxCellList);
					listViewOpenDirectionInCurrentPhase.setItems(openDirectionInCurrentPhaseHBoxCellObservableList);
					listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
				}
				labelDirInPhaseValue.setText(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());
			}else {
				listViewOpenDirectionInCurrentPhase.getItems().clear();
			}			
		}
	}

	public void buttonPreviousEvent() {
		listViewOpenDirectionInCurrentPhase.getItems().clear();
		if(!tableViewPhase.getItems().isEmpty()) {
			
			int index = tableViewPhase.getSelectionModel().getSelectedIndex();
			int finalIndex = tableViewPhase.getItems().size() - 1;
			mapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
			ObservableList<String> existedOpenDirectionsList = FXCollections.observableArrayList();			
			
			if(index == 0) {
				tableViewPhase.getSelectionModel().select(finalIndex);
				String phaseNumber = tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number();
				labelDirInPhaseValue.setText(phaseNumber);
			}else {
				tableViewPhase.getSelectionModel().select(tableViewPhase.getSelectionModel().getSelectedIndex() - 1);
				listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
				String phaseNumber = tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number();
				labelDirInPhaseValue.setText(phaseNumber);
			}
			
			// all existed directions
			valuesOfComboBox = FXCollections.observableArrayList();
			for (RoadDirection roadDirection : iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList()) {
				String dirNumber = roadDirection.getRoadDirections_number();
				valuesOfComboBox.add(dirNumber);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////			
			
			List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsList = mapOfOpenDirectionInPhase.get(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());			
			
			for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionsList) {
				String openDir = openDirectionInCurrentPhaseHBoxCell.getComboBox().getValue();
				existedOpenDirectionsList.add(openDir);
			}
			
			valuesOfComboBox.removeAll(existedOpenDirectionsList);			
			
			if(openDirectionsList != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell numberDirection : openDirectionsList) {
					
					//numberDirection.getComboBox().getItems().clear();
					//numberDirection.setObservableListComboBox(valuesOfComboBox);
					openDirectionInCurrentPhaseHBoxCellList.add(numberDirection);				
					openDirectionInCurrentPhaseHBoxCellObservableList = FXCollections.observableList(openDirectionInCurrentPhaseHBoxCellList);
					listViewOpenDirectionInCurrentPhase.setItems(openDirectionInCurrentPhaseHBoxCellObservableList);
					listViewOpenDirectionInCurrentPhase.getSelectionModel().selectFirst();
				}
				labelDirInPhaseValue.setText(tableViewPhase.getSelectionModel().getSelectedItem().getRoadPhase_number());
			}else {
				listViewOpenDirectionInCurrentPhase.getItems().clear();
			}			
		}
	}

	@FXML
	public void initialize() {

		//langXML();
		//loadLang(langXML);

		tableViewPhase.setEditable(true);
		tableViewPhase.setPlaceholder(new Label("Нет данных для отображения"));
		listViewOpenDirectionInCurrentPhase.setPlaceholder(new Label("Нет данных для отображения"));
		//btnCreateDirectionInPhase.setDisable(true);
		
		listViewOpenDirectionInCurrentPhase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				for(OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell : openDirectionInCurrentPhaseHBoxCellList) {
					openDirectionInCurrentPhaseHBoxCell.getComboBox().setDisable(true);
					openDirectionInCurrentPhaseHBoxCell.getCheckBox().setDisable(true);
				}
				newValue.getComboBox().setDisable(false);
				newValue.getCheckBox().setDisable(false);
				previousOpenDirectionInPhaseHBoxCell = oldValue;
			}
		});
		listViewOpenDirectionInCurrentPhase.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			previousIndex = oldValue.intValue();
		});
		
		tableViewPhase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				previousRoadPhase = oldValue;
			}
		});		

		Callback<TableColumn<RoadPhase, String>, TableCell<RoadPhase, String>> cellFactory = (TableColumn<RoadPhase, String> param) -> new PhaseEditingCell();
		Callback<TableColumn<RoadPhase, TVP>, TableCell<RoadPhase, TVP>> comboBoxCellFactory = (TableColumn<RoadPhase, TVP> param) -> new PhaseComboBoxEditingCell();
		// Callback<TableColumn<RoadDirectionsModel, String>,
		// TableCell<RoadDirectionsModel, String>> dateCellFactory
		// = (TableColumn<RoadDirectionsModel, String> param) -> new DateEditingCell();

		tableColumnNumber.setCellValueFactory(cellData -> cellData.getValue().roadPhaseNumberProperty());
		tableColumnNumber.setCellFactory(cellFactory);
		tableColumnNumber.setStyle("-fx-alignment: CENTER;");
		tableColumnNumber.setOnEditCommit((TableColumn.CellEditEvent<RoadPhase, String> t) -> {
			((RoadPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadPhase_number(t.getNewValue());
		});

		tableColumnTmin.setCellValueFactory(cellData -> cellData.getValue().roadPhaseTminProperty());
		tableColumnTmin.setCellFactory(cellFactory);
		tableColumnTmin.setStyle("-fx-alignment: CENTER;");
		tableColumnTmin.setOnEditCommit((TableColumn.CellEditEvent<RoadPhase, String> t) -> {
			((RoadPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadPhase_Tmin(t.getNewValue());
		});

		tableColumnPanelTVP_1.setCellValueFactory(cellData -> cellData.getValue().roadPhasePanel_1_Property());
		tableColumnPanelTVP_1.setCellFactory(cellFactory);
		tableColumnPanelTVP_1.setStyle("-fx-alignment: CENTER;");
		tableColumnPanelTVP_1.setOnEditCommit((TableColumn.CellEditEvent<RoadPhase, String> t) -> {
			((RoadPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadPhase_panelTVP_1(t.getNewValue());
		});

		tableColumnPanelTVP_2.setCellValueFactory(cellData -> cellData.getValue().roadPhasePanel_2_Property());
		tableColumnPanelTVP_2.setCellFactory(cellFactory);
		tableColumnPanelTVP_2.setStyle("-fx-alignment: CENTER;");
		tableColumnPanelTVP_2.setOnEditCommit((TableColumn.CellEditEvent<RoadPhase, String> t) -> {
			((RoadPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadPhase_panelTVP_2(t.getNewValue());
		});

		tableColumnPhaseTVP.setCellValueFactory(cellData -> cellData.getValue().roadPhasePhaseTVPProperty());
		tableColumnPhaseTVP.setCellFactory(comboBoxCellFactory);
		tableColumnPhaseTVP.setStyle("-fx-alignment: CENTER");
		tableColumnPhaseTVP.setOnEditCommit((TableColumn.CellEditEvent<RoadPhase, TVP> t) -> {
			((RoadPhase) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadPhase_phaseTVP(t.getNewValue());
		});

		listViewOpenDirectionInCurrentPhase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				for (OpenDirectionInCurrentPhaseHBoxCell existedOpenDirectionInCurrentPhaseHBoxCell : openDirectionInCurrentPhaseHBoxCellList) {
					existedOpenDirectionInCurrentPhaseHBoxCell.getComboBox().setDisable(true);
				}
				newValue.getComboBox().setDisable(false);
			}
		});

	}

}
