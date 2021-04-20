package presenters.directions;

import java.io.File;
import java.io.IOException;
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

import javafx.scene.control.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import presenters.conflicts.ConflictWithDirection;
import presenters.object.TypeKDK;
import presenters.phase.OpenDirectionInCurrentPhaseHBoxCell;
import presenters.promtactu.InterphaseTransitionsHBoxCell;
import presenters.promtactu.PromtactData;
import roadModel.IRoadModel;

/**
 * Created by Vitaly on 06.12.2016.
 */
public class RoadDirectionsPresenter {
	
	@FXML
	private Button btnCreate, btnDelete, btnAutofill, btnBaseTableOfPromtact, btnOutputChannelParameters, btnBasePromtact, btnDirectionReferenceBook, btnGroupControl;
	@FXML
	private TableView<RoadDirection> tableViewDirections;
	@FXML
	private TableColumn<RoadDirection, String> tableColumn_number, tableColumn_channel_1, tableColumn_channel_2, tableColumn_channel_3, tableColumn_channel_4, tableColumn_control1, tableColumn_control2, tableColumn_Channel;
	@FXML
	private TableColumn<RoadDirection, TypDirection> tableColumn_typeOfDirection;
	@FXML
	private Label labelDirection, labelAllDirections;
	@FXML
	private RadioButton radioCheckControl;
	@FXML
	private CheckBox chBoxTram;

	@FXML
	//private static ResourceBundle bundleGUI, bundleAlert;
	//private static Locale localeGUI, localeAlert;
	//static String langXML = null;

	String dataBaseName;
	String port;
	String localHost;
	String userName;
	String password;

	IRoadModel iRoadModel;
	RoadDirection roadDirection;
	ConflictWithDirection conflictWithDirection;
	PromtactData promtactData;

	Map<String, List<ConflictWithDirection>> conflictMap;
	Map<String, PromtactData> basePromtactDataMap;
	Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> groupControlHBoxCellListMap;
	Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOfOpenDirectionInPhase;
	Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfDirectionSpecificPromtact;

	List<ConflictWithDirection> conflictWithDirectionList;
	List<String> directionList = new ArrayList<>();

	public void show(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;
		
		List<RoadDirection> roadDirectionsList = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();
		
		tableViewDirections.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList()));
		tableViewDirections.getSelectionModel().selectFirst();
		if (tableViewDirections.getItems().size() > 0) {
			btnBaseTableOfPromtact.setDisable(false);
			btnBasePromtact.setDisable(false);
		}
		
		if (iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK().equals("")) {
			btnCreate.setDisable(true);
		}
		
		for(RoadDirection direction : roadDirectionsList) {
			if(direction.getRoadDirections_control_1() != null) {
				if(!direction.getRoadDirections_control_1().equals("")) {
					radioCheckControl.setSelected(true);
				}
			}
		}
		
	}

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

		labelDirection.setText(bundleGUI.getString("labelDirection"));
		labelAllDirections.setText(bundleGUI.getString("labelAllDirections"));
		smbCreate.setText(bundleGUI.getString("buttonCreate"));
		btnDelete.setText(bundleGUI.getString("buttonDelete"));
		btnAutofill.setText(bundleGUI.getString("buttonAutofill"));
		btnBasePromtact.setText(bundleGUI.getString("buttonBasePromtact"));
		btnBaseTableOfPromtact.setText(bundleGUI.getString("buttonBaseTableOfPromtact"));
		btnOutputChannelParameters.setText(bundleGUI.getString("buttonOutputChannelParameters"));
		btnDirectionReferenceBook.setText(bundleGUI.getString("buttonDirectionReferenceBook"));
		btnGroupControl.setText(bundleGUI.getString("buttonGroupControl"));
		tableColumn_number.setText(bundleGUI.getString("tableColumnNumber"));
		tableColumn_Channel.setText(bundleGUI.getString("tableColumnDirectionChennals"));
		tableColumn_typeOfDirection.setText(bundleGUI.getString("tableColumnDirectionTypeOfDirection"));
		// tableColumn_control.setText(bundleGUI.getString("tableColumnDirectionControl"));
		// tableColumn_channel_parameters.setText(bundleGUI.getString("tableColumnDirectionChannelParameters"));
		// tableColumn_promtact.setText(bundleGUI.getString("tableColumnDirectionPromtact"));
	}*/

	/*private static void alertLang(String lang) {
		localeAlert = new Locale(lang);
		bundleAlert = ResourceBundle.getBundle("localeAlert_lang", localeAlert);
	}*/

	public void createTramGroup(){



	}

	///////////// Event on button 'Create direction' ////////////////
	public void createNewDirection() {
		System.out.println("============= Press create direction =============");
		System.out.println();
		
		List<RoadDirection> roadDirectionsList = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();

		if(!chBoxTram.isSelected()){

			if(roadDirectionsList.size() == 0) {
				String number = Integer.toString(roadDirectionsList.size() + 1);
				String directionName = "Транспортное направление";

				conflictMap = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();
				basePromtactDataMap = iRoadModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();

				TypDirection type = new TypDirection(directionName);
				roadDirection = new RoadDirection();
				promtactData = new PromtactData();

				UUID uuid = UUID.randomUUID();
				String id = uuid.toString();
				roadDirection.setIdDirection(id);

				conflictWithDirectionList = new ArrayList<>();

				directionList.add(number);

				conflictMap.put(number, conflictWithDirectionList);
				basePromtactDataMap.put(number, promtactData);

				roadDirection.setRoadDirections_number(number);
				roadDirection.setRoadDirections_typeOfDirection(type);
				roadDirection.setRoadDirections_chanal_1("");
				roadDirection.setRoadDirections_chanal_2("");
				roadDirection.setRoadDirections_chanal_3("");
				roadDirection.setRoadDirections_chanal_4("");
				roadDirection.setRoadDirections_control_1("");
				roadDirection.setRoadDirections_control_2("");
				iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().add(roadDirection);
				tableViewDirections.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList()));

				tableViewDirections.getSelectionModel().select(tableViewDirections.getItems().size() - 1);
				btnBaseTableOfPromtact.setDisable(false);
				btnBasePromtact.setDisable(false);

			}else {
				String typeKDK = iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK();
				List<TypeKDK> typeKDKsList = iRoadModel.getModel().getRoadObjectModel().getKdkTypeList();
				List<Integer> allNumbersOfDirList = new ArrayList<>();

				for(RoadDirection roadDirection : roadDirectionsList) {
					allNumbersOfDirList.add(Integer.parseInt(roadDirection.getRoadDirections_number()));
				}

				String number = Integer.toString(Collections.max(allNumbersOfDirList) + 1);		//number for large direction number + 1
				for (TypeKDK existedKDKType : typeKDKsList) {
					if (existedKDKType.getName_KDK().equals(typeKDK)) {
						int max_value = Integer.parseInt(existedKDKType.getDirections());

						if (Integer.parseInt(number) <= max_value) {

							String directionName = tableViewDirections.getSelectionModel().getSelectedItem().getRoadDirections_typeOfDirection().getTypDirection();

							conflictMap = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();
							basePromtactDataMap = iRoadModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();

							TypDirection type = new TypDirection(directionName);
							roadDirection = new RoadDirection();
							promtactData = new PromtactData();

							UUID uuid = UUID.randomUUID();
							String id = uuid.toString();
							roadDirection.setIdDirection(id);

							conflictWithDirectionList = new ArrayList<>();;

							directionList.add(number);

							conflictMap.put(number, conflictWithDirectionList);
							basePromtactDataMap.put(number, promtactData);

							roadDirection.setRoadDirections_number(number);
							roadDirection.setRoadDirections_typeOfDirection(type);
							roadDirection.setRoadDirections_chanal_1("");
							roadDirection.setRoadDirections_chanal_2("");
							roadDirection.setRoadDirections_chanal_3("");
							roadDirection.setRoadDirections_chanal_4("");
							roadDirection.setRoadDirections_control_1("");
							roadDirection.setRoadDirections_control_2("");
							iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().add(roadDirection);
							show(iRoadModel);

							tableViewDirections.getSelectionModel().select(tableViewDirections.getItems().size() - 1);
							btnBaseTableOfPromtact.setDisable(false);
							btnBasePromtact.setDisable(false);

						}else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Ошибка");
							alert.setHeaderText("Тип контроллера не позволяет создать больше " + max_value + " направлений");

							Stage stage = new Stage();
							stage = (Stage)alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));

							alert.show();
						}
					}
				}
			}
		}else{
			if(roadDirectionsList.size() == 0){

				int number = roadDirectionsList.size() + 1;
				String strNumber = Integer.toString(number);
				String tramLeft = "Трамвайное налево";
				String tramStraight = "Трамвайное прямо";
				String tramRight = "Трамвайное направо";

				conflictMap = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();
				basePromtactDataMap = iRoadModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();

				TypDirection typeLeft = new TypDirection(tramLeft);
				TypDirection typeStraight = new TypDirection(tramStraight);
				TypDirection typeRight = new TypDirection(tramRight);

				RoadDirection directionLeft = new RoadDirection();
				promtactData = new PromtactData();
				directionLeft.setRoadDirections_number(Integer.toString(number));
				directionLeft.setRoadDirections_typeOfDirection(typeLeft);
				directionLeft.setRoadDirections_chanal_1("");
				directionLeft.setRoadDirections_chanal_2("");
				directionLeft.setRoadDirections_chanal_3("");
				directionLeft.setRoadDirections_chanal_4("");
				directionLeft.setRoadDirections_control_1("");
				directionLeft.setRoadDirections_control_2("");

				conflictWithDirectionList = new ArrayList<>();;

				directionList.add(strNumber);

				conflictMap.put(strNumber, conflictWithDirectionList);
				basePromtactDataMap.put(strNumber, promtactData);

				iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().add(directionLeft);

				number++;
				strNumber = Integer.toString(number);

				RoadDirection directionStraight = new RoadDirection();
				promtactData = new PromtactData();
				directionStraight.setRoadDirections_number(Integer.toString(number));
				directionStraight.setRoadDirections_typeOfDirection(typeStraight);
				directionStraight.setRoadDirections_chanal_1("");
				directionStraight.setRoadDirections_chanal_2("");
				directionStraight.setRoadDirections_chanal_3("");
				directionStraight.setRoadDirections_chanal_4("");
				directionStraight.setRoadDirections_control_1("");
				directionStraight.setRoadDirections_control_2("");

				conflictWithDirectionList = new ArrayList<>();;

				directionList.add(strNumber);

				conflictMap.put(strNumber, conflictWithDirectionList);
				basePromtactDataMap.put(strNumber, promtactData);

				iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().add(directionStraight);

				number++;
				strNumber = Integer.toString(number);

				RoadDirection directionRight = new RoadDirection();
				promtactData = new PromtactData();
				directionRight.setRoadDirections_number(Integer.toString(number));
				directionRight.setRoadDirections_typeOfDirection(typeRight);
				directionRight.setRoadDirections_chanal_1("");
				directionRight.setRoadDirections_chanal_2("");
				directionRight.setRoadDirections_chanal_3("");
				directionRight.setRoadDirections_chanal_4("");
				directionRight.setRoadDirections_control_1("");
				directionRight.setRoadDirections_control_2("");

				conflictWithDirectionList = new ArrayList<>();;

				directionList.add(strNumber);

				conflictMap.put(strNumber, conflictWithDirectionList);
				basePromtactDataMap.put(strNumber, promtactData);

				iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().add(directionRight);

				show(iRoadModel);


			}else{
				int number = roadDirectionsList.size() + 1;
				String strNumber = Integer.toString(number);
				String tramLeft = "Трамвайное налево";
				String tramStraight = "Трамвайное прямо";
				String tramRight = "Трамвайное направо";

				conflictMap = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();
				basePromtactDataMap = iRoadModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();

				TypDirection typeLeft = new TypDirection(tramLeft);
				TypDirection typeStraight = new TypDirection(tramStraight);
				TypDirection typeRight = new TypDirection(tramRight);

				RoadDirection directionLeft = new RoadDirection();
				promtactData = new PromtactData();
				directionLeft.setRoadDirections_number(Integer.toString(number));
				directionLeft.setRoadDirections_typeOfDirection(typeLeft);
				directionLeft.setRoadDirections_chanal_1("");
				directionLeft.setRoadDirections_chanal_2("");
				directionLeft.setRoadDirections_chanal_3("");
				directionLeft.setRoadDirections_chanal_4("");
				directionLeft.setRoadDirections_control_1("");
				directionLeft.setRoadDirections_control_2("");

				conflictWithDirectionList = new ArrayList<>();;

				directionList.add(strNumber);

				conflictMap.put(strNumber, conflictWithDirectionList);
				basePromtactDataMap.put(strNumber, promtactData);

				iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().add(directionLeft);

				number++;
				strNumber = Integer.toString(number);

				RoadDirection directionStraight = new RoadDirection();
				promtactData = new PromtactData();
				directionStraight.setRoadDirections_number(Integer.toString(number));
				directionStraight.setRoadDirections_typeOfDirection(typeStraight);
				directionStraight.setRoadDirections_chanal_1("");
				directionStraight.setRoadDirections_chanal_2("");
				directionStraight.setRoadDirections_chanal_3("");
				directionStraight.setRoadDirections_chanal_4("");
				directionStraight.setRoadDirections_control_1("");
				directionStraight.setRoadDirections_control_2("");

				conflictWithDirectionList = new ArrayList<>();;

				directionList.add(strNumber);

				conflictMap.put(strNumber, conflictWithDirectionList);
				basePromtactDataMap.put(strNumber, promtactData);

				iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().add(directionStraight);

				number++;
				strNumber = Integer.toString(number);

				RoadDirection directionRight = new RoadDirection();
				promtactData = new PromtactData();
				directionRight.setRoadDirections_number(Integer.toString(number));
				directionRight.setRoadDirections_typeOfDirection(typeRight);
				directionRight.setRoadDirections_chanal_1("");
				directionRight.setRoadDirections_chanal_2("");
				directionRight.setRoadDirections_chanal_3("");
				directionRight.setRoadDirections_chanal_4("");
				directionRight.setRoadDirections_control_1("");
				directionRight.setRoadDirections_control_2("");

				conflictWithDirectionList = new ArrayList<>();;

				directionList.add(strNumber);

				conflictMap.put(strNumber, conflictWithDirectionList);
				basePromtactDataMap.put(strNumber, promtactData);

				iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().add(directionRight);

				show(iRoadModel);

			}
		}

		/*if(roadDirectionsList.size() == 0) {
			String number = Integer.toString(roadDirectionsList.size() + 1);
			String directionName = "Транспортное направление";

			conflictMap = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();
			basePromtactDataMap = iRoadModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();

			TypDirection type = new TypDirection(directionName);
			roadDirection = new RoadDirection();
			promtactData = new PromtactData();

			UUID uuid = UUID.randomUUID();
			String id = uuid.toString();
			roadDirection.setIdDirection(id);

			conflictWithDirectionList = new ArrayList<>();

			directionList.add(number);

			conflictMap.put(number, conflictWithDirectionList);
			basePromtactDataMap.put(number, promtactData);

			roadDirection.setRoadDirections_number(number);
			roadDirection.setRoadDirections_typeOfDirection(type);
			roadDirection.setRoadDirections_chanal_1("");
			roadDirection.setRoadDirections_chanal_2("");
			roadDirection.setRoadDirections_chanal_3("");
			roadDirection.setRoadDirections_chanal_4("");
			roadDirection.setRoadDirections_control_1("");
			roadDirection.setRoadDirections_control_2("");
			iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().add(roadDirection);
			tableViewDirections.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList()));

			tableViewDirections.getSelectionModel().select(tableViewDirections.getItems().size() - 1);
			btnBaseTableOfPromtact.setDisable(false);
			btnBasePromtact.setDisable(false);
			
		}else {
			String typeKDK = iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK();
			List<TypeKDK> typeKDKsList = iRoadModel.getModel().getRoadObjectModel().getKdkTypeList();
			List<Integer> allNumbersOfDirList = new ArrayList<>();
			
			for(RoadDirection roadDirection : roadDirectionsList) {
				allNumbersOfDirList.add(Integer.parseInt(roadDirection.getRoadDirections_number()));
			}
			
			String number = Integer.toString(Collections.max(allNumbersOfDirList) + 1);		//number for large direction number + 1
			for (TypeKDK existedKDKType : typeKDKsList) {
				if (existedKDKType.getName_KDK().equals(typeKDK)) {
					int max_value = Integer.parseInt(existedKDKType.getDirections());
					
					if (Integer.parseInt(number) <= max_value) {
												
						String directionName = tableViewDirections.getSelectionModel().getSelectedItem().getRoadDirections_typeOfDirection().getTypDirection();

						conflictMap = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();
						basePromtactDataMap = iRoadModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();

						TypDirection type = new TypDirection(directionName);
						roadDirection = new RoadDirection();
						promtactData = new PromtactData();

						UUID uuid = UUID.randomUUID();
						String id = uuid.toString();
						roadDirection.setIdDirection(id);

						conflictWithDirectionList = new ArrayList<>();;

						directionList.add(number);

						conflictMap.put(number, conflictWithDirectionList);
						basePromtactDataMap.put(number, promtactData);

						roadDirection.setRoadDirections_number(number);
						roadDirection.setRoadDirections_typeOfDirection(type);
						roadDirection.setRoadDirections_chanal_1("");
						roadDirection.setRoadDirections_chanal_2("");
						roadDirection.setRoadDirections_chanal_3("");
						roadDirection.setRoadDirections_chanal_4("");
						roadDirection.setRoadDirections_control_1("");
						roadDirection.setRoadDirections_control_2("");
						iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().add(roadDirection);
						show(iRoadModel);

						tableViewDirections.getSelectionModel().select(tableViewDirections.getItems().size() - 1);
						btnBaseTableOfPromtact.setDisable(false);
						btnBasePromtact.setDisable(false);
						
					}else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Ошибка");
						alert.setHeaderText("Тип контроллера не позволяет создать больше " + max_value + " направлений");
						
						Stage stage = new Stage();
						stage = (Stage)alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
						
						alert.show();
					}
				}
			}
		}*/
	}

	public void deleteDirection() {
		//alertLang(langXML);
		basePromtactDataMap = iRoadModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();
		groupControlHBoxCellListMap = iRoadModel.getModel().getRoadDirectionModel().getGroupControlHBoxCellListMap();
		mapOfOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		conflictMap = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();

		/*String tramLeft = "Трамвайное налево";
		String tramStraight = "Трамвайное прямо";
		String tramRight = "Трамвайное направо";*/

		RoadDirection selectedRoadDirection = tableViewDirections.getSelectionModel().getSelectedItem();
		String directionNumber = tableViewDirections.getSelectionModel().getSelectedItem().getRoadDirections_number();
		int selectedDirectionIndex = tableViewDirections.getSelectionModel().getSelectedIndex();
		if(selectedRoadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное налево")){
			tableViewDirections.getItems().remove(selectedDirectionIndex);
			tableViewDirections.getItems().remove(selectedDirectionIndex);
			tableViewDirections.getItems().remove(selectedDirectionIndex);
		}else if (selectedRoadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное прямо")){
			tableViewDirections.getItems().remove(selectedDirectionIndex);
			tableViewDirections.getItems().remove(selectedDirectionIndex);
			tableViewDirections.getItems().remove(selectedDirectionIndex - 1);
		}else if (selectedRoadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное направо")){
			tableViewDirections.getItems().remove(selectedDirectionIndex);
			tableViewDirections.getItems().remove(selectedDirectionIndex - 1);
			tableViewDirections.getItems().remove(selectedDirectionIndex - 2);


		}

		/*int selectDirection = tableViewDirections.getSelectionModel().getSelectedIndex();
		String dirNumber = tableViewDirections.getSelectionModel().getSelectedItem().getRoadDirections_number();
		if (selectDirection >= 0) {
			tableViewDirections.getItems().remove(selectDirection);
			
			// delete direction from group control
			for(Map.Entry<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> entry : groupControlHBoxCellListMap.entrySet()) {
				List<ControlledChanelHBoxCell> controlledChanelList = entry.getValue();
				for(ControlledChanelHBoxCell controlledChanelHBoxCell : controlledChanelList) {
					String dirNumberControl = controlledChanelHBoxCell.getComboBoxDirection().getValue();
					if(dirNumber.equals(dirNumberControl)) {
						controlledChanelList.remove(controlledChanelHBoxCell);
						break;
					}
				}
			}
			Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> mapGroupControllAfterDelete = new LinkedHashMap<>(groupControlHBoxCellListMap);
			for(Map.Entry<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> entry : groupControlHBoxCellListMap.entrySet()) {
				List<ControlledChanelHBoxCell> controlledChanelList = entry.getValue();
				if(controlledChanelList.isEmpty()) {
					mapGroupControllAfterDelete.remove(entry.getKey());
				}
			}
			iRoadModel.getModel().getRoadDirectionModel().setGroupControlHBoxCellListMap(mapGroupControllAfterDelete);
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			if(!mapOfOpenDirectionInPhase.isEmpty()) {		// remove direction from open direction in phase
				for(Map.Entry<String, List<OpenDirectionInCurrentPhaseHBoxCell>> entry : mapOfOpenDirectionInPhase.entrySet()) {
					List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsList = entry.getValue();
					if(openDirectionsList != null) {
						List<OpenDirectionInCurrentPhaseHBoxCell> listAfterRemove = new ArrayList<>(); 
						for(OpenDirectionInCurrentPhaseHBoxCell openDirection : openDirectionsList) {
							String existDirection = openDirection.getComboBox().getValue();
							if(!dirNumber.equals(existDirection)) {
								listAfterRemove.add(openDirection);
							}
						}
						mapOfOpenDirectionInPhase.put(entry.getKey(), listAfterRemove);
					}
				}
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			if(!mapOfDirectionSpecificPromtact.isEmpty()) {		// remove direction from promtact interphase
				for(Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()) {
					Map<String, PromtactData> mapOfDirectionInInterphase = entry.getValue();
					if(mapOfDirectionInInterphase.containsKey(dirNumber)) {
						mapOfDirectionInInterphase.remove(dirNumber);
					}
				}
			}
			////////////////////////////////////////////////////////////////////////////////////////////////
			
			if(!conflictMap.isEmpty()) {
				if(conflictMap.containsKey(dirNumber)) {
					conflictMap.remove(dirNumber);
				}
			}
			
			iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().remove(selectDirection);
			basePromtactDataMap.remove(dirNumber);
			tableViewDirections.getSelectionModel().select(tableViewDirections.getItems().size() - 1);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Выберите стоку для удаления");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.showAndWait();
		}*/
	}

	public void openGroupControl() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GroupControl.fxml"));
			Pane object = fxmlLoader.load();

			GroupControlPresenter groupConrtolPresenter = fxmlLoader.getController();

			groupConrtolPresenter.show(iRoadModel, iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList(), iRoadModel.getModel().getRoadDirectionModel().getGroupControlHBoxCellListMap());

			Scene scene = new Scene(object);

			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			stage.setTitle("Групповой контроль");
			stage.setScene(scene);
			stage.showAndWait();
			if (groupConrtolPresenter.buttonOKWasPressed() == true) {
				stage.close();
			}
			
		} catch (IOException e) {
			System.out.println(e.getStackTrace());
		}
	}

	public void openBasePromtact() {
		basePromtactDataMap = iRoadModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Base_promtacts.fxml"));
			Pane object = fxmlLoader.load();

			DirectionBasePromtactPresenter directionBasePromtactPresenter = fxmlLoader.getController();

			directionBasePromtactPresenter.mapOfDirection(basePromtactDataMap);

			Scene scene = new Scene(object);

			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			stage.setTitle("Базовые промтакты(для направлений)");
			stage.setScene(scene);
			stage.showAndWait();
			if (directionBasePromtactPresenter.ok()) {
				basePromtactDataMap = directionBasePromtactPresenter.getPromtactDataMap();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void openBasePromtactTable() {
		basePromtactDataMap = iRoadModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Base_promtact_table.fxml"));
			Pane object = fxmlLoader.load();

			BasePromtactTablePresenter basePromtactTablePresenter = fxmlLoader.getController();
			basePromtactTablePresenter.mapOfDirection(basePromtactDataMap);
			basePromtactTablePresenter.listRoadDirection(iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList());
			basePromtactTablePresenter.setItemsTableView();

			Scene scene = new Scene(object);

			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			stage.setTitle("Общая таблица базовых промтактов");
			stage.setScene(scene);
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mouseClick() {
		System.out.println();
	}

	public void autofillChannels() {
		List<RoadDirection> roadDirections = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();
		
		if(!roadDirections.isEmpty()) {
			
			if(radioCheckControl.isSelected()) {
				int channel = 1;
				for(RoadDirection roadDirection : roadDirections) {
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное направление")) {
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						roadDirection.setRoadDirections_control_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_2(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Пешеходное")) {
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						roadDirection.setRoadDirections_control_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Поворотная стрелка")) {
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное с одним красным")) {
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						roadDirection.setRoadDirections_control_1(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное красное и желтое")) {
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						roadDirection.setRoadDirections_control_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_2(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное два красных")) {
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						roadDirection.setRoadDirections_control_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_2(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_4(Integer.toString(channel));
						roadDirection.setRoadDirections_control_2(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное прямо")){
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						roadDirection.setRoadDirections_control_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное налево")){
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						roadDirection.setRoadDirections_control_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное направо")){
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						roadDirection.setRoadDirections_control_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
				}
			}else {
				int channel = 1;
				for(RoadDirection roadDirection : roadDirections) {
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное направление")) {
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_2(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Пешеходное")) {
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Поворотная стрелка")) {
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное с одним красным")) {
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное красное и желтое")) {
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_2(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное два красных")) {
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_2(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_4(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное прямо")){
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное налево")){
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное направо")){
						roadDirection.setRoadDirections_chanal_1(Integer.toString(channel));
						channel++;
						roadDirection.setRoadDirections_chanal_3(Integer.toString(channel));
						channel++;
					}
				}
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
	
	public void setControlDirections() {
		List<RoadDirection> roadDirections = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();
		
		if(!roadDirections.isEmpty()) {
			if(radioCheckControl.isSelected()) {
				for(RoadDirection roadDirection : roadDirections) {
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное направление")) {
						roadDirection.setRoadDirections_control_1(roadDirection.getRoadDirections_chanal_1());
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Пешеходное")) {
						roadDirection.setRoadDirections_control_1(roadDirection.getRoadDirections_chanal_1());
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Поворотная стрелка")) {
						
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное с одним красным")) {
						roadDirection.setRoadDirections_control_1(roadDirection.getRoadDirections_chanal_1());
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное красное и желтое")) {
						roadDirection.setRoadDirections_control_1(roadDirection.getRoadDirections_chanal_1());
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное два красных")) {
						roadDirection.setRoadDirections_control_1(roadDirection.getRoadDirections_chanal_1());
						roadDirection.setRoadDirections_control_2(roadDirection.getRoadDirections_chanal_4());
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное прямо")){
						roadDirection.setRoadDirections_control_1(roadDirection.getRoadDirections_chanal_1());
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное направо")){
						roadDirection.setRoadDirections_control_1(roadDirection.getRoadDirections_chanal_1());
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное налево")){
						roadDirection.setRoadDirections_control_1(roadDirection.getRoadDirections_chanal_1());
					}
				}
			}else {
				for(RoadDirection roadDirection : roadDirections) {
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное направление")) {
						roadDirection.setRoadDirections_control_1("");
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Пешеходное")) {
						roadDirection.setRoadDirections_control_1("");
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Поворотная стрелка")) {
						
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное с одним красным")) {
						roadDirection.setRoadDirections_control_1("");
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное красное и желтое")) {
						roadDirection.setRoadDirections_control_1("");
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное два красных")) {
						roadDirection.setRoadDirections_control_1("");
						roadDirection.setRoadDirections_control_2("");
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное прямо")){
						roadDirection.setRoadDirections_control_1("");
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное направо")){
						roadDirection.setRoadDirections_control_1("");
					}
					if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное налево")){
						roadDirection.setRoadDirections_control_1("");
					}
				}
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
	
	@FXML
	public void initialize() {

		//langXML();
		//loadLang(langXML);

		tableViewDirections.setEditable(true);
		btnBaseTableOfPromtact.setDisable(true);
		btnBasePromtact.setDisable(true);
		
		tableViewDirections.setPlaceholder(new Label("Нет данных для отображения"));

		Callback<TableColumn<RoadDirection, String>, TableCell<RoadDirection, String>> cellFactory = (TableColumn<RoadDirection, String> param) -> new DirectionsEditingCell();
		Callback<TableColumn<RoadDirection, TypDirection>, TableCell<RoadDirection, TypDirection>> comboBoxCellFactory = (TableColumn<RoadDirection, TypDirection> param) -> new DirectionsComboBoxEditingCell();
		// Callback<TableColumn<RoadDirectionsModel, String>,
		// TableCell<RoadDirectionsModel, String>> dateCellFactory
		// = (TableColumn<RoadDirectionsModel, String> param) -> new DateEditingCell();

		tableColumn_number.setCellValueFactory(cellData -> cellData.getValue().roadDirectionsNumberProperty());
		tableColumn_number.setCellFactory(cellFactory);
		tableColumn_number.setStyle("-fx-alignment: CENTER;");
		tableColumn_number.setOnEditCommit((TableColumn.CellEditEvent<RoadDirection, String> t) -> {
			((RoadDirection) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadDirections_number(t.getNewValue());
		});

		tableColumn_typeOfDirection.setCellValueFactory(cellData -> cellData.getValue().roadDirectionsTypeOfDirectionProperty());
		tableColumn_typeOfDirection.setCellFactory(comboBoxCellFactory);
		tableColumn_typeOfDirection.setStyle("-fx-alignment: CENTER;");
		tableColumn_typeOfDirection.setOnEditCommit((TableColumn.CellEditEvent<RoadDirection, TypDirection> t) -> {
			((RoadDirection) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadDirections_typeOfDirection(t.getNewValue());
		});

		tableColumn_channel_1.setCellValueFactory(cellData -> cellData.getValue().roadDirectionsChanal_1_Property());
		tableColumn_channel_1.setCellFactory(cellFactory);
		tableColumn_channel_1.setStyle("-fx-alignment: CENTER;");
		tableColumn_channel_1.setOnEditCommit((TableColumn.CellEditEvent<RoadDirection, String> t) -> {
			((RoadDirection) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadDirections_chanal_1(t.getNewValue());
		});
		tableColumn_channel_1.setStyle("-fx-text-fill: #f11e17; -fx-font-weight: bold; -fx-alignment:CENTER");

		tableColumn_channel_2.setCellValueFactory(cellData -> cellData.getValue().roadDirectionsChanal_2_Property());
		tableColumn_channel_2.setCellFactory(cellFactory);
		tableColumn_channel_2.setStyle("-fx-alignment: CENTER;");
		tableColumn_channel_2.setOnEditCommit((TableColumn.CellEditEvent<RoadDirection, String> t) -> {
			((RoadDirection) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadDirections_chanal_2(t.getNewValue());
		});
		tableColumn_channel_2.setStyle("-fx-text-fill: #c2ba38; -fx-font-weight: bold; -fx-alignment:CENTER");

		tableColumn_channel_3.setCellValueFactory(cellData -> cellData.getValue().roadDirectionsChanal_3_Property());
		tableColumn_channel_3.setCellFactory(cellFactory);
		tableColumn_channel_3.setStyle("-fx-alignment: CENTER;");
		tableColumn_channel_3.setOnEditCommit((TableColumn.CellEditEvent<RoadDirection, String> t) -> {
			((RoadDirection) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadDirections_chanal_3(t.getNewValue());
		});
		tableColumn_channel_3.setStyle("-fx-text-fill: #03cc0a; -fx-font-weight: bold; -fx-alignment:CENTER");
		
		tableColumn_channel_4.setCellValueFactory(cellData -> cellData.getValue().roadDirectionsChanal_4_Property());
		tableColumn_channel_4.setCellFactory(cellFactory);
		tableColumn_channel_4.setStyle("-fx-alignment: CENTER;");
		tableColumn_channel_4.setOnEditCommit((TableColumn.CellEditEvent<RoadDirection, String> t) -> {
			((RoadDirection) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadDirections_chanal_4(t.getNewValue());
		});
		tableColumn_channel_4.setStyle("-fx-text-fill: #f11e17; -fx-font-weight: bold; -fx-alignment:CENTER");

		
		tableColumn_control1.setCellValueFactory(cellData -> cellData.getValue().roadDirectionsControl_1_Property());
		tableColumn_control1.setCellFactory(cellFactory);
		tableColumn_control1.setStyle("-fx-alignment: CENTER;");
		tableColumn_control1.setOnEditCommit((TableColumn.CellEditEvent<RoadDirection, String> t) -> {
			((RoadDirection) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadDirections_control_1(t.getNewValue());
		});
		tableColumn_control1.setStyle("-fx-text-fill: #1e439a; -fx-font-weight: bold; -fx-alignment:CENTER");
		
		tableColumn_control2.setCellValueFactory(cellData -> cellData.getValue().roadDirectionsControl_2_Property());
		tableColumn_control2.setCellFactory(cellFactory);
		tableColumn_control2.setStyle("-fx-alignment: CENTER;");
		tableColumn_control2.setOnEditCommit((TableColumn.CellEditEvent<RoadDirection, String> t) -> {
			((RoadDirection) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadDirections_control_2(t.getNewValue());
		});
		tableColumn_control2.setStyle("-fx-text-fill: #1e439a; -fx-font-weight: bold; -fx-alignment:CENTER");

	}

	public void selectRoadDirection() {

		/*
		 * String typeOfDirection =
		 * tableViewDirections.getSelectionModel().getSelectedItem().
		 * getRoadDirections_typeOfDirection().getTypDirection();
		 * 
		 * if (typeOfDirection.equals("Транспортное направление")) { Image image = new
		 * Image("image/other/trafficLights.jpg"); imageViewLight.setImage(image); } if
		 * (typeOfDirection.equals("Пешеходное")) { Image image = new
		 * Image("image/other/pedesrtianLights.jpg"); imageViewLight.setImage(image); }
		 * if (typeOfDirection.equals("Поворотная стрелка")) { Image image = new
		 * Image("image/other/turnArrowLights.jpg"); imageViewLight.setImage(image); }
		 */

	}

	/*
	 * public bool Validate(String errorMsg){ bool isValidated = false;
	 * 
	 * return isValidated; }
	 */

	/*
	 * public RoadDirectionsModel getModel(){ return tableViewDirections.getItems();
	 * }
	 */

}
