package presenters.bottomBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import presenters.conflicts.ConflictWithDirection;
import presenters.conflicts.RoadConflictsModel;
import presenters.detector.Detector;
import presenters.detector.RoadDetectorModel;
import presenters.directions.ControlledChanelHBoxCell;
import presenters.directions.GroupControlHBoxCell;
import presenters.directions.RoadDirection;
import presenters.directions.RoadDirectionsModel;
import presenters.directions.RoadDirectionsPresenter;
import presenters.object.RoadObjectModel;
import presenters.object.RoadObjectPresenter;
import presenters.phase.OpenDirectionInCurrentPhaseHBoxCell;
import presenters.phase.RoadPhase;
import presenters.phase.RoadPhaseModel;
import presenters.phase.RoadPhasePresenter;
import presenters.programs.PhaseInProgram;
import presenters.programs.RoadProgram;
import presenters.programs.RoadProgramsModel;
import presenters.programs.RoadProgramsPresenter;
import presenters.programs.ScheduleCalendarDateHBoxCell;
import presenters.programs.ScheduleCalendarWeekDayHBoxCell;
import presenters.programs.ScheduleProgram;
import presenters.programs.SpeedSign;
import presenters.programs.SwitchPhase;
import presenters.promtactu.InterphaseTransitionsHBoxCell;
import presenters.promtactu.PromtactData;
import presenters.promtactu.RoadPromtactuModel;
import presenters.promtactu.RoadPromtactuPresenter;
import roadModel.IRoadModel;

/**
 * Created by Vitaly on 19.12.2016.
 */
public class BottomBarPresenter {

	@FXML
	private Button btnSave;

	@FXML
	//private ResourceBundle bundleGUI, bundleAlert;
	@SuppressWarnings("unused")
	//private Locale localeGUI, localeAlert;
	String langXML = null;

	private IRoadModel iRoadModel;
	
	RoadObjectPresenter roadObjectPresenter;
	RoadDirectionsPresenter roadDirectionsPresenter;
	RoadPhasePresenter roadPhasePresenter;
	RoadProgramsPresenter roadProgramsPresenter;
	RoadPromtactuPresenter roadPromtactuPresenter;

	Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfDirectionSpecificPromtact;
	Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfDirectionSpecificPromtactAfterSort = new LinkedHashMap<>();
	List<InterphaseTransitionsHBoxCell> interphaseTransitionsHBoxCellList = new ArrayList<>();
	List<String> phaseNumberFrom = new ArrayList<>();
	ObservableList<Integer> toIntegerFrom = FXCollections.observableArrayList();
	ObservableList<String> afterSortToStringFrom = FXCollections.observableArrayList();

	String path = null;

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

		btnSave.setText(bundleGUI.getString("buttonSave"));
	}*/

	@SuppressWarnings("unused")
	private void sortInterphase() {

		mapOfDirectionSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();

		interphaseTransitionsHBoxCellList.clear();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////// add non-repeating phase numbers to the list
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// //////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()) {
			String entryNumberFrom = entry.getKey().getComboBoxFromPhase().getValue();
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

		/*
		 * if(interphaseTransitionsHBoxCell != null) {
		 * interphaseTransitionsHBoxCellObservableList.clear();
		 * listViewInterphase.getItems().clear(); }
		 */

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////// add item in sorted order
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (String phaseNumberFrom : afterSortToStringFrom) {
			for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()) {
				String numberFrom = entry.getKey().getComboBoxFromPhase().getValue();
				if (phaseNumberFrom.equals(numberFrom)) { // sort by first value from combo box

					if (interphaseTransitionsHBoxCellList.size() == 0) { // add interphaseHBoxCell if list is empty
						interphaseTransitionsHBoxCellList.add(entry.getKey());
					} else {
						for (InterphaseTransitionsHBoxCell existedInterphase : interphaseTransitionsHBoxCellList) { // sort by second value from combo box
							if (numberFrom.equals(existedInterphase.getComboBoxFromPhase().getValue())) {
								int numberToFromList = Integer.parseInt(existedInterphase.getComboBoxToPhase().getValue());
								int numberFromList = Integer.parseInt(entry.getKey().getComboBoxToPhase().getValue());
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
		System.out.println();

		for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfDirectionSpecificPromtact.entrySet()) {
			String numberFrom = entry.getKey().getComboBoxFromPhase().getValue();
			for (InterphaseTransitionsHBoxCell interphaseTransitionsHBoxCell : interphaseTransitionsHBoxCellList) {
				String existerNumberFrom = interphaseTransitionsHBoxCell.getComboBoxFromPhase().getValue();
				if (numberFrom.equals(existerNumberFrom)) {
					mapOfDirectionSpecificPromtactAfterSort.put(interphaseTransitionsHBoxCell, entry.getValue());
				}
			}
		}
		//System.out.println(mapOfDirectionSpecificPromtactAfterSort);
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	public List<RoadDirection> assignNumberOfType(List<RoadDirection> roadDirectionsList) {

		for (RoadDirection roadDirection : roadDirectionsList) {
			if (roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное направление")) {
				roadDirection.setNumberOfTypeDirection("1");
			}
			if (roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Пешеходное")) {
				roadDirection.setNumberOfTypeDirection("2");
			}
			if (roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Поворотная стрелка")) {
				roadDirection.setNumberOfTypeDirection("3");
			}
			if (roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное с одним красным")) {
				roadDirection.setNumberOfTypeDirection("4");
			}
			if (roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное красное и желтое")) {
				roadDirection.setNumberOfTypeDirection("5");
			}
			if (roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное два красных")) {
				roadDirection.setNumberOfTypeDirection("6");
			}
			if (roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное налево")) {
				roadDirection.setNumberOfTypeDirection("7");
			}
			if (roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное прямо")) {
				roadDirection.setNumberOfTypeDirection("8");
			}
			if (roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное направо")) {
				roadDirection.setNumberOfTypeDirection("9");
			}
		}
		return roadDirectionsList;
	}

	public void xmlForAsudd() {

		RoadObjectModel roadObjectModel = iRoadModel.getModel().getRoadObjectModel();
		RoadDirectionsModel roadDirectionsModel = iRoadModel.getModel().getRoadDirectionModel();
		RoadPhaseModel roadPhaseModel = iRoadModel.getModel().getRoadPhaseModel();
		RoadProgramsModel roadProgramsModel = iRoadModel.getModel().getRoadProgramsModel();
		RoadPromtactuModel roadPromtactuModel = iRoadModel.getModel().getRoadPromtactuModel();
		RoadConflictsModel roadConflictsModel = iRoadModel.getModel().getRoadConflictsModel();

		Map<String, List<ConflictWithDirection>> mapOfConflict = roadConflictsModel.getMapOfConflict();
		Map<String, PromtactData> mapOfBasePromtact = roadDirectionsModel.getMapOfBasePromtact();
		Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfSpecificPromtact = roadPromtactuModel.getMapOfInterphaseSpecificPromtact();
		//Map<String, List<String>> mapOpenDirectionInPhase = roadPhaseModel.getMapOpenDirectionInPhase();

		try {
			// root element
			Element elementRoadController = new Element("RoadController");
			Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			Namespace xsd = Namespace.getNamespace("xsd", "http://www.w3.org/2001/XMLSchema-instance");
			elementRoadController.addNamespaceDeclaration(xsi);
			elementRoadController.addNamespaceDeclaration(xsd);
			org.jdom.Document document = new org.jdom.Document();

			Element elementCameras = new Element("Cameras");
			elementCameras.setText("null");
			elementRoadController.addContent(elementCameras);

			Element elementConfiguration = new Element("Configuration");
			elementRoadController.addContent(elementConfiguration);

			Element elementConfigurationID = new Element("ConfigurationID");
			if (roadObjectModel.getRoadObjectConfiguratinId().equals("")) {
				UUID uuid = UUID.randomUUID();
				String configId = uuid.toString();
				roadObjectModel.setRoadObjectConfiguratinId(configId);
				elementConfigurationID.setText(configId);
			} else {
				elementConfigurationID.setText(roadObjectModel.getRoadObjectConfiguratinId());
			}

			elementConfiguration.addContent(elementConfigurationID);

			Element elementDB_ID_Object = new Element("DB_ID_Object");
			// elementDB_ID_Object.setText(roadModelSettings.getDb_id_object());
			elementDB_ID_Object.setText("null");
			elementConfiguration.addContent(elementDB_ID_Object);

			Element elemetDB_Name_Object = new Element("DB_Name_Object");
			String db_id_object = roadObjectModel.getRoadObjectName();
			if (!db_id_object.equals("")) {
				elemetDB_Name_Object.setText(db_id_object);
			} else {
				elemetDB_Name_Object.setText("null");
			}
			elementConfiguration.addContent(elemetDB_Name_Object);

			Element elementDB_Number_Object = new Element("DB_Number_Object");
			elementDB_Number_Object.setText("null");
			// elementDB_Number_Object.setText(roadModelSettings.getDb_number_object());
			elementConfiguration.addContent(elementDB_Number_Object);

			Element elementDB_Path = new Element("DB_Path");
			elementDB_Path.setText("null");
			// elementDB_Path.setText(roadModelSettings.getDb_path());
			elementConfiguration.addContent(elementDB_Path);

			Element elementDB_Path_Options = new Element("DB_Path_Options");
			elementDB_Path_Options.setText("null");
			// elementDB_Path_Options.setText(roadModelSettings.getDb_path_options());
			elementConfiguration.addContent(elementDB_Path_Options);

			Element elementDateTimeLoaded = new Element("DateTimeLoaded");
			elementDateTimeLoaded.setText("null");
			// elementDateTimeLoaded.setText(roadModelSettings.getConfiguration_datetime_loaded());
			elementConfiguration.addContent(elementDateTimeLoaded);

			// all directions
			Element elementDirectionsList = new Element("DirectionsList");
			elementConfiguration.addContent(elementDirectionsList);

			// Element elementDirectionServer = new Element("DirectionServer");
			// elementDirectionsList.addContent(elementDirectionServer);

			Element elementDirectionServer = null;
			Element elementAdditRed = null;
			Element elementConflictWithDirectionsList = null;

			// directions configuration
			List<RoadDirection> roadDirectionsList = roadDirectionsModel.getRoadDirectionList();
			assignNumberOfType(roadDirectionsList);

			for (RoadDirection roadDirection : roadDirectionsList) {

				elementDirectionServer = new Element("DirectionServer");
				elementDirectionsList.addContent(elementDirectionServer);

				elementAdditRed = new Element("AdditRed");
				elementAdditRed.setText("null");
				// fill with data
				elementDirectionServer.addContent(elementAdditRed);

				elementConflictWithDirectionsList = new Element("ConflictWithDirectionsList");
				elementDirectionServer.addContent(elementConflictWithDirectionsList);

				for (Map.Entry<String, List<ConflictWithDirection>> entry : mapOfConflict.entrySet()) {
					if (roadDirection.getRoadDirections_number().equals(entry.getKey())) {
						List<ConflictWithDirection> allConflicts = entry.getValue();

						for (ConflictWithDirection conflictWithDirection : allConflicts) {
							String dirNumber = conflictWithDirection.getConflictWithDirection();

							for (RoadDirection conflictDirection : roadDirectionsList) {
								if (dirNumber.equals(conflictDirection.getRoadDirections_number())) {
									String conflictDirectionId = conflictDirection.getIdDirection();

									Element elementGuid = new Element("guid");
									elementGuid.setText(conflictDirectionId);
									elementConflictWithDirectionsList.addContent(elementGuid);

								}
							}
						}
					}
					// break;
				}

				Element elementControl1 = new Element("Control1");
				elementControl1.setText("null");
				// fill with data
				elementDirectionServer.addContent(elementControl1);

				Element elementControl2 = new Element("Control2");
				elementControl2.setText("null");
				// elementControl2.setAttribute("nil", "true", xsi);
				elementDirectionServer.addContent(elementControl2);

				Element elementDefaultPromtakt = new Element("DefaultPromtakt");
				elementDirectionServer.addContent(elementDefaultPromtakt);

				for (Map.Entry<String, PromtactData> entry : mapOfBasePromtact.entrySet()) {
					String dirNumber = entry.getKey();
					if (dirNumber.equals(roadDirection.getRoadDirections_number())) {
						PromtactData promtactData = entry.getValue();
						String defPromtactId = promtactData.getPromtactId();
						String end_Green_Addit = promtactData.getRoadPromtactu_endGreenAddit();
						String end_Green_Blink = promtactData.getRoadPromtactu_durationGreenBlink();
						String end_Red = promtactData.getRoadPromtactu_endRed();
						String end_Red_Yellow = promtactData.getRoadPromtactu_durationRedYellow();
						String end_Yellow = promtactData.getRoadPromtactu_durationYellow();

						Element elementEnd_Green_Addit = new Element("End_Green_Addit");
						elementEnd_Green_Addit.setText(end_Green_Addit);
						elementDefaultPromtakt.addContent(elementEnd_Green_Addit);

						Element elementEnd_Green_Blink = new Element("End_Green_Blink");
						elementEnd_Green_Blink.setText(end_Green_Blink);
						elementDefaultPromtakt.addContent(elementEnd_Green_Blink);

						Element elementEnd_Red = new Element("End_Red");
						elementEnd_Red.setText(end_Red);
						elementDefaultPromtakt.addContent(elementEnd_Red);

						Element elementEnd_Red_Yellow = new Element("End_Red_Yellow");
						elementEnd_Red_Yellow.setText(end_Red_Yellow);
						elementDefaultPromtakt.addContent(elementEnd_Red_Yellow);

						Element elementEnd_Yellow = new Element("End_Yellow");
						elementEnd_Yellow.setText(end_Yellow);
						elementDefaultPromtakt.addContent(elementEnd_Yellow);

						Element elementPromtaktID = new Element("PromtaktID");
						elementPromtaktID.setText(defPromtactId);
						elementDefaultPromtakt.addContent(elementPromtaktID);

					}
				}

				Element elementDirectInTransitCollection = new Element("DirectInTransitCollection");
				elementDirectionServer.addContent(elementDirectInTransitCollection);

				Element elementDirectInTransitServer = null;

				for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entryInterphase : mapOfSpecificPromtact.entrySet()) {
					Map<String, PromtactData> mapOfPromtactData = entryInterphase.getValue();

					for (Map.Entry<String, PromtactData> entryPromtact : mapOfPromtactData.entrySet()) {
						String dirNumber = entryPromtact.getKey();

						if (dirNumber.equals(roadDirection.getRoadDirections_number())) {
							elementDirectInTransitServer = new Element("DirectInTransitServer");

							PromtactData promtactData = entryPromtact.getValue();
							String specPromtactId = promtactData.getPromtactId();
							String fromPhaseId = promtactData.getRoadPromtactu_fromPhaseId();
							String dirTransitId = promtactData.getRoadPromtactu_directInTransitId();
							String toPhaseId = promtactData.getRoadPromtactu_toPhaseId();
							String end_Green_Addit = promtactData.getRoadPromtactu_endGreenAddit();
							String end_Green_Blink = promtactData.getRoadPromtactu_durationGreenBlink();
							String end_Red = promtactData.getRoadPromtactu_endRed();
							String end_Red_Yellow = promtactData.getRoadPromtactu_durationRedYellow();
							String end_Yellow = promtactData.getRoadPromtactu_durationYellow();

							Element elementDirectionInTransitID = new Element("DirectionInTransitID");
							elementDirectionInTransitID.setText(dirTransitId);
							elementDirectInTransitServer.addContent(elementDirectionInTransitID);

							Element elementPhase_From_ID = new Element("Phase_From_ID");
							elementPhase_From_ID.setText(fromPhaseId);
							elementDirectInTransitServer.addContent(elementPhase_From_ID);

							Element elementPhase_To_ID = new Element("Phase_To_ID");
							elementPhase_To_ID.setText(toPhaseId);
							elementDirectInTransitServer.addContent(elementPhase_To_ID);

							Element elementTransitPromtakt = new Element("TransitPromtakt");

							Element elementEnd_Green_Addit = new Element("End_Green_Addit");
							elementEnd_Green_Addit.setText(end_Green_Addit);
							elementTransitPromtakt.addContent(elementEnd_Green_Addit);

							Element elementEnd_Green_Blink = new Element("End_Green_Blink");
							elementEnd_Green_Blink.setText(end_Green_Blink);
							elementTransitPromtakt.addContent(elementEnd_Green_Blink);

							Element elementEnd_Red = new Element("End_Red");
							elementEnd_Red.setText(end_Red);
							elementTransitPromtakt.addContent(elementEnd_Red);

							Element elementEnd_Red_Yellow = new Element("End_Red_Yellow");
							elementEnd_Red_Yellow.setText(end_Red_Yellow);
							elementTransitPromtakt.addContent(elementEnd_Red_Yellow);

							Element elementEnd_Yellow = new Element("End_Yellow");
							elementEnd_Yellow.setText(end_Yellow);
							elementTransitPromtakt.addContent(elementEnd_Yellow);

							Element elementPromtaktID = new Element("PromtaktID");
							elementPromtaktID.setText(specPromtactId);
							elementTransitPromtakt.addContent(elementPromtaktID);

							elementDirectInTransitServer.addContent(elementTransitPromtakt);
							elementDirectInTransitCollection.addContent(elementDirectInTransitServer);
						}
					}
				}

				Element elementDirectionID = new Element("DirectionID");
				elementDirectionID.setText(roadDirection.getIdDirection());
				elementDirectionServer.addContent(elementDirectionID);

				Element elementDirectionNumber = new Element("DirectionNumber");
				elementDirectionNumber.setText(roadDirection.getRoadDirections_number());
				elementDirectionServer.addContent(elementDirectionNumber);

				Element elementDirectionType = new Element("DirectionType");
				elementDirectionType.setText(roadDirection.getNumberOfTypeDirection());
				elementDirectionServer.addContent(elementDirectionType);

				Element elementGreenChannelColor = new Element("GreenChannelColor");
				String greenChannel = roadDirection.getRoadDirections_chanal_3();
				if (greenChannel != null) {
					elementGreenChannelColor.setText(greenChannel);
				} else {
					elementGreenChannelColor.setText("null");
				}
				elementDirectionServer.addContent(elementGreenChannelColor);

				Element elementRedChannelColor = new Element("RedChannelColor");
				String redChannel = roadDirection.getRoadDirections_chanal_1();
				if (redChannel != null) {
					elementRedChannelColor.setText(redChannel);
				} else {
					elementRedChannelColor.setText("null");
				}
				elementDirectionServer.addContent(elementRedChannelColor);

				Element elementYellowChannelColor = new Element("YellowChannelColor");
				String yellowChannel = roadDirection.getRoadDirections_chanal_2();
				if (yellowChannel != null) {
					elementYellowChannelColor.setText(yellowChannel);
				} else {
					elementYellowChannelColor.setText("null");
				}
				elementDirectionServer.addContent(elementYellowChannelColor);

			}
			///////////////////////////////////////////////////////////////////////////////////

			Element elementPhasesList = new Element("PhasesList");
			elementConfiguration.addContent(elementPhasesList);

			Element elementConfigPhase = null;

			// phases configuration
			List<RoadPhase> allPhasesList = roadPhaseModel.getRoadPhaseList();
			for (RoadPhase roadPhase : allPhasesList) {
				elementConfigPhase = new Element("ConfigPhase");
				elementPhasesList.addContent(elementConfigPhase);

				Element elementDirectionsListOpenedID = new Element("DirectionsListOpenedID");
				elementConfigPhase.addContent(elementDirectionsListOpenedID);
				Element elementPhaseId = new Element("Id");
				elementPhaseId.setText(roadPhase.getIdPhase());
				elementConfigPhase.addContent(elementPhaseId);

				Element elementNumberPhase = new Element("Number");
				elementNumberPhase.setText(roadPhase.getRoadPhase_number());
				elementConfigPhase.addContent(elementNumberPhase);

				Element elementTmin = new Element("Tmin");
				elementTmin.setText(roadPhase.getRoadPhase_Tmin());
				elementConfigPhase.addContent(elementTmin);

			}

			// CONNECTION SETTINGS
			Element elementConnectionSettings = new Element("ConnectionSettings");
			elementRoadController.addContent(elementConnectionSettings);

			Element elementConnModId = new Element("ConnModId");
			elementConnModId.setText("null");
			// elementConnModId.setText(roadModelSettings.getConModId());
			elementConnectionSettings.addContent(elementConnModId);

			Element elementConnTypeName = new Element("ConnTypeName");
			String connectionType = roadObjectModel.getRoadObjectConnectType();
			if (!connectionType.equals("")) {
				elementConnTypeName.setText(connectionType);
			} else {
				elementConnTypeName.setText("null");
			}
			elementConnectionSettings.addContent(elementConnTypeName);

			Element elementDelay = new Element("Delay");
			String delay = iRoadModel.getModel().getRoadObjectModel().getDelay();
			if (!delay.equals("")) {
				elementDelay.setText(delay);
			} else
				elementDelay.setText("null");
			elementConnectionSettings.addContent(elementDelay);

			Element elementDelayDarkYF = new Element("DelayDarkYF");
			String delayDarkYF = iRoadModel.getModel().getRoadObjectModel().getDelayYF();
			if (!delayDarkYF.equals("")) {
				elementDelayDarkYF.setText(delayDarkYF);
			} else
				elementDelayDarkYF.setText("null");
			elementConnectionSettings.addContent(elementDelayDarkYF);

			Element elementIpAddress = new Element("IpAddress");
			String ipAddress = iRoadModel.getModel().getRoadObjectModel().getRoadObjectIP();
			if (!ipAddress.equals("")) {
				elementIpAddress.setText(ipAddress);
			} else
				elementIpAddress.setText("null");
			elementConnectionSettings.addContent(elementIpAddress);

			Element elementKVU_ControllerProtocol = new Element("KVU_ControllerProtocol");
			elementKVU_ControllerProtocol.setText("null");
			// fill with data
			elementConnectionSettings.addContent(elementKVU_ControllerProtocol);

			Element elementKVU_ControllerProtocolDescription = new Element("KVU_ControllerProtocolDescription");
			elementKVU_ControllerProtocolDescription.setText("null");
			// fill with data
			elementConnectionSettings.addContent(elementKVU_ControllerProtocolDescription);

			Element elementKVU_Device = new Element("KVU_Device");
			elementKVU_Device.setText("null");
			// fill with data
			elementConnectionSettings.addContent(elementKVU_Device);

			Element elementKVU_Line = new Element("KVU_Line");
			elementKVU_Line.setText("null");
			// fill with data
			elementConnectionSettings.addContent(elementKVU_Line);

			Element elementKVU_Modem = new Element("KVU_Modem");
			elementKVU_Modem.setText("null");
			// fill with data
			elementConnectionSettings.addContent(elementKVU_Modem);

			Element elementPort = new Element("Port");
			String port = roadObjectModel.getRoadObjectConnectPort();
			if (!port.equals("")) {
				elementPort.setText(port);
			} else {
				elementPort.setText("null");
			}
			// fill with data
			elementConnectionSettings.addContent(elementPort);

			Element elementSleepTime = new Element("SleepTime");
			String sleepTime = iRoadModel.getModel().getRoadObjectModel().getSleepTime();
			if (!sleepTime.equals("")) {
				elementSleepTime.setText(sleepTime);
			} else
				elementSleepTime.setText("null");
			elementConnectionSettings.addContent(elementSleepTime);
			//////////////////////////////////////////////////////////////

			Element elementDataModificationTime = new Element("DataModificationTime");
			elementDataModificationTime.setText("null");
			// elementDataModificationTime.setAttribute("nil", "true", xsi);
			elementRoadController.addContent(elementDataModificationTime);

			Element elementDirectionsLoading = new Element("DirectionsLoading");
			elementDirectionsLoading.setText("null");
			// fill with data
			elementRoadController.addContent(elementDirectionsLoading);

			Element elementGetAddress = new Element("GetAddress");
			elementGetAddress.setText(roadObjectModel.getRoadObjectNetworkAddress());
			elementRoadController.addContent(elementGetAddress);

			Element elementGetId = new Element("GetId");
			elementGetId.setText("null");
			// elementGetId.setText(roadModelSettings.getObjectId());
			elementRoadController.addContent(elementGetId);

			Element elementGetNumber = new Element("GetNumber");
			elementGetNumber.setText("null");
			// elementGetNumber.setText(roadObjectModel.getRoadObjectObjectNumber());
			elementRoadController.addContent(elementGetNumber);

			Element elementHWId = new Element("HWId");
			elementHWId.setText("null");
			// elementHWId.setText(roadModelSettings.getHwId());
			elementRoadController.addContent(elementHWId);

			Element elementHasAddInfo = new Element("HasAddInfo");
			elementHasAddInfo.setText("null");
			// fill with data
			elementRoadController.addContent(elementHasAddInfo);

			Element elementInCC = new Element("InCC");
			elementInCC.setText("null");
			// fill with data
			elementRoadController.addContent(elementInCC);

			Element elementIsActive = new Element("IsActive");
			elementIsActive.setText("null");
			// fill with data
			elementRoadController.addContent(elementIsActive);

			Element elementIsBusy = new Element("IsBusy");
			elementIsBusy.setText("null");
			// fill with data
			elementRoadController.addContent(elementIsBusy);

			Element elementIsDetector = new Element("IsDetector");
			elementIsDetector.setText("null");
			// fill with data
			elementRoadController.addContent(elementIsDetector);

			Element elementIsHasGPS = new Element("IsHasGPS");
			elementIsHasGPS.setText("null");
			// fill with data
			elementRoadController.addContent(elementIsHasGPS);

			Element elementIsInterrogation = new Element("IsInterrogation");
			elementIsInterrogation.setText("null");
			// fill with data
			elementRoadController.addContent(elementIsInterrogation);

			Element elementIsSubPhases = new Element("IsSubPhases");
			elementIsSubPhases.setText("null");
			// fill with data
			elementRoadController.addContent(elementIsSubPhases);

			Element elementLastUpdateTime = new Element("LastUpdateTime");
			elementLastUpdateTime.setText("null");
			// elementLastUpdateTime.setText(roadModelSettings.getLastUpdateTime());
			elementRoadController.addContent(elementLastUpdateTime);

			Element elementName = new Element("Name");
			elementName.setText(roadObjectModel.getRoadObjectName());
			elementRoadController.addContent(elementName);

			Element elementNote = new Element("Note");
			elementNote.setText(roadObjectModel.getRoadObjectNote());
			elementRoadController.addContent(elementNote);

			Element elementPointX = new Element("PointX");
			elementPointX.setText("null");
			// fill with data
			elementRoadController.addContent(elementPointX);

			Element elementPointY = new Element("PointY");
			elementPointY.setText("null");
			// fill with data
			elementRoadController.addContent(elementPointY);

			// <Programs>
			Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram = roadProgramsModel.getMapOfPhasesInProgram();
			Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> mapOfWeekCalendar = roadProgramsModel.getMapOfWeekCalendar();

			Element elementPrograms = new Element("Programs");
			elementRoadController.addContent(elementPrograms);

			Element elementProgramServer = null;

			List<RoadProgram> allRoadProgramsList = roadProgramsModel.getRoadProgramList();
			for (RoadProgram roadProgram : allRoadProgramsList) {
				elementProgramServer = new Element("ProgramServer");
				elementPrograms.addContent(elementProgramServer);

				String idProgram = roadProgram.getIdProgram();

				Element elementID = new Element("ID");
				elementID.setText(idProgram);
				elementProgramServer.addContent(elementID);

				Element elementNumber = new Element("Number");
				elementNumber.setText(roadProgram.getRoadProgram_number());
				elementProgramServer.addContent(elementNumber);

				Element elementOrigin = new Element("Origin");
				elementOrigin.setText("Default");
				elementProgramServer.addContent(elementOrigin);

				Element elementProgramsForSubstitutionPhases = new Element("ProgramsForSubstitutionPhases");
				elementProgramsForSubstitutionPhases.setText("null");
				// fill with data
				elementProgramServer.addContent(elementProgramsForSubstitutionPhases);

				Element elementProgramsPhases = new Element("ProgramsPhases");
				elementProgramServer.addContent(elementProgramsPhases);

				Element elementProgramPhaseServer = null;

				List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(roadProgram);
				if (phaseInProgramsList != null) {
					for (PhaseInProgram phaseInProgram : phaseInProgramsList) {
						elementProgramPhaseServer = new Element("ProgramPhaseServer");
						elementProgramsPhases.addContent(elementProgramPhaseServer);

						Element elementIndex = new Element("Index");
						elementIndex.setText(phaseInProgram.getPhaseIndex());
						elementProgramPhaseServer.addContent(elementIndex);

						Element elementLength = new Element("Length");
						elementLength.setText(phaseInProgram.getDurationPhaseInProgram());
						elementProgramPhaseServer.addContent(elementLength);

						Element elementNumberPhaseInProgram = new Element("Number");
						elementNumberPhaseInProgram.setText(phaseInProgram.getPhaseInProgramNumber().getPhaseNumber());
						elementProgramPhaseServer.addContent(elementNumberPhaseInProgram);
					}
				}

				// <SpodTimeTables>
				Element elementSpodTimeTables = new Element("SpodTimeTables");
				elementProgramServer.addContent(elementSpodTimeTables);

				Element elementSPOD_TimeTableServer = null;

				for (Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekCalendar.entrySet()) {
					List<ScheduleProgram> scheduleProgramsList = entry.getValue();
					for (ScheduleProgram scheduleProgram : scheduleProgramsList) {
						String scheduleNumber = scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber();
						if (scheduleNumber.equals(roadProgram.getRoadProgram_number())) {
							elementSPOD_TimeTableServer = new Element("SPOD_TimeTableServer");
							elementSpodTimeTables.addContent(elementSPOD_TimeTableServer);

							Element elementOffsetTime = new Element("OffsetTime");
							elementOffsetTime.setText(scheduleProgram.getDisplacementTimeOfScheduleProgram());
							elementSPOD_TimeTableServer.addContent(elementOffsetTime);

							Element elementStartTime = new Element("StartTime");
							elementStartTime.setText(scheduleProgram.getTimeONOfScheduleProgram() + ":00");
							elementSPOD_TimeTableServer.addContent(elementStartTime);

							Element elementTimeID = new Element("TimeID");
							elementTimeID.setText(scheduleProgram.getScheduleProgramId());
							elementSPOD_TimeTableServer.addContent(elementTimeID);

							String weekDay = entry.getKey().getWeekDay().getText();

							if (weekDay.equals("Понедельник")) {
								weekDay = "Monday";
							}
							if (weekDay.equals("Вторник")) {
								weekDay = "Tuesday";
							}
							if (weekDay.equals("Среда")) {
								weekDay = "Wednesday";
							}
							if (weekDay.equals("Четверг")) {
								weekDay = "Thursday";
							}
							if (weekDay.equals("Пятница")) {
								weekDay = "Friday";
							}
							if (weekDay.equals("Суббота")) {
								weekDay = "Saturday";
							}
							if (weekDay.equals("Воскресенье")) {
								weekDay = "Sunday";
							}

							Element elementWeekDays = new Element("WeekDays");
							elementWeekDays.setText(weekDay);
							elementSPOD_TimeTableServer.addContent(elementWeekDays);

						}
					}
				}
				String type = roadProgram.getRoadProgram_programMode().getMode();
				if (type.equals("Желтое мигание")) {
					type = "YF";
				} else {
					type = "DP";
				}

				Element elementType = new Element("Type");
				elementType.setText(type);
				elementProgramServer.addContent(elementType);

			}

			Element elementProtocol = new Element("Protocol");
			elementProtocol.setText(roadObjectModel.getRoadObjectProtocol());
			elementRoadController.addContent(elementProtocol);

			Element elementSimNumber = new Element("SimNumber");
			elementSimNumber.setText("null");
			elementRoadController.addContent(elementSimNumber);

			Element elementSpeedSign = new Element("SpeedSign");
			elementSpeedSign.setText("null");
			elementRoadController.addContent(elementSpeedSign);

			Element elementTypeKDK = new Element("TypeKDK");
			String typeKDK = iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK();
			if (!typeKDK.equals("")) {
				elementTypeKDK.setText(typeKDK);
			} else
				elementRoadController.addContent("null");

			Element elementUserDataChanger = new Element("UserDataChanger");
			elementUserDataChanger.setText("null");
			elementRoadController.addContent(elementUserDataChanger);

			document.setRootElement(elementRoadController);

			XMLOutputter outter = new XMLOutputter();
			outter.setFormat(Format.getPrettyFormat());
			outter.output(document, new FileWriter(new File(path + "\\asudd.xml")));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void xmlObject() {
		
		/*try {
			FXMLLoader fxmlLoaderObject = new FXMLLoader(getClass().getResource("/fxml/Road_object.fxml"));
			AnchorPane anchorPaneObject = fxmlLoaderObject.load();
			roadObjectPresenter = fxmlLoaderObject.getController();
			roadObjectPresenter.save(iRoadModel);
			
			FXMLLoader fxmlLoaderPromtact = new FXMLLoader(getClass().getResource("/fxml/Road_promtactu.fxml"));
			AnchorPane anchorPanePromtact = fxmlLoaderPromtact.load();
			roadObjectPresenter = fxmlLoaderPromtact.getController();
			roadObjectPresenter.save(iRoadModel);
			
			
			
		}catch (IOException e) {
			e.printStackTrace();
		}*/

		RoadObjectModel roadObjectModel = iRoadModel.getModel().getRoadObjectModel();
		RoadDirectionsModel roadDirectionsModel = iRoadModel.getModel().getRoadDirectionModel();
		RoadPhaseModel roadPhaseModel = iRoadModel.getModel().getRoadPhaseModel();
		RoadProgramsModel roadProgramsModel = iRoadModel.getModel().getRoadProgramsModel();
		RoadPromtactuModel roadPromtactuModel = iRoadModel.getModel().getRoadPromtactuModel();
		RoadDetectorModel roadDetectorModel = iRoadModel.getModel().getRoadDetectorModel();
		RoadConflictsModel roadConflictsModel = iRoadModel.getModel().getRoadConflictsModel();

		PrintWriter file = null;
		try {
			org.jdom.Document document = new org.jdom.Document();
			org.jdom.Element rootElement = new org.jdom.Element("root");

			// OBJECT
			org.jdom.Element objectElement = new org.jdom.Element("object");

			org.jdom.Element nameElement = new org.jdom.Element("name");
			nameElement.setText(roadObjectModel.getRoadObjectName());
			objectElement.addContent(nameElement);

			org.jdom.Element countryElement = new org.jdom.Element("country");
			countryElement.setText(roadObjectModel.getRoadObjectCountry());
			objectElement.addContent(countryElement);

			org.jdom.Element cityElement = new org.jdom.Element("city");
			cityElement.setText(roadObjectModel.getRoadObjectCity());
			objectElement.addContent(cityElement);

			org.jdom.Element numberElement = new org.jdom.Element("number");
			numberElement.setText(roadObjectModel.getRoadObjectObjectNumber());
			objectElement.addContent(numberElement);

			org.jdom.Element network_addressElement = new org.jdom.Element("network_address");
			network_addressElement.setText(roadObjectModel.getRoadObjectNetworkAddress());
			objectElement.addContent(network_addressElement);

			org.jdom.Element magistralElement = new org.jdom.Element("magistral");
			magistralElement.setText(roadObjectModel.getRoadObjectMagistral());
			objectElement.addContent(magistralElement);

			org.jdom.Element protocolElement = new org.jdom.Element("protocol");
			protocolElement.setText(roadObjectModel.getRoadObjectProtocol());
			objectElement.addContent(protocolElement);
			
			org.jdom.Element chargeElement = new org.jdom.Element("charge");
			chargeElement.setText(roadObjectModel.getRoadObjectCharge());
			objectElement.addContent(chargeElement);

			org.jdom.Element date_creatElement = new org.jdom.Element("date_create");
			date_creatElement.setText(roadObjectModel.getRoadObjectDateOfCreation());
			objectElement.addContent(date_creatElement);

			org.jdom.Element date_launchElement = new org.jdom.Element("date_launch");
			date_launchElement.setText(roadObjectModel.getRoadObjectLaunchDate());
			objectElement.addContent(date_launchElement);

			org.jdom.Element technologistElement = new org.jdom.Element("technologist");
			technologistElement.setText(roadObjectModel.getRoadObjectTechnologist());
			objectElement.addContent(technologistElement);

			org.jdom.Element noteElement = new org.jdom.Element("note");
			noteElement.setText(roadObjectModel.getRoadObjectNote());
			objectElement.addContent(noteElement);

			org.jdom.Element type_kdkElement = new org.jdom.Element("type_kdk");
			type_kdkElement.setText(roadObjectModel.getRoadObjectTypeOfKDK());
			objectElement.addContent(type_kdkElement);

			org.jdom.Element connection_settingElement = new org.jdom.Element("connection_setting");

			org.jdom.Element type_connectionElement = new org.jdom.Element("type_connection");
			type_connectionElement.setText(roadObjectModel.getRoadObjectConnectType());
			connection_settingElement.addContent(type_connectionElement);

			org.jdom.Element connect_portElement = new org.jdom.Element("port");
			connect_portElement.setText(roadObjectModel.getRoadObjectConnectPort());
			connection_settingElement.addContent(connect_portElement);

			org.jdom.Element connect_speedElement = new org.jdom.Element("speed");
			connect_speedElement.setText(roadObjectModel.getRoadObjectConnectSpeed());
			connection_settingElement.addContent(connect_speedElement);

			org.jdom.Element connect_ipElement = new org.jdom.Element("ip");
			connect_ipElement.setText(roadObjectModel.getRoadObjectIP());
			connection_settingElement.addContent(connect_ipElement);
			
			org.jdom.Element connect_dnsElement = new org.jdom.Element("dns");
			connect_dnsElement.setText(roadObjectModel.getRoadObjectDNS());
			connection_settingElement.addContent(connect_dnsElement);

			org.jdom.Element connect_maskElement = new org.jdom.Element("mask");
			connect_maskElement.setText(roadObjectModel.getRoadObjectMASK());
			connection_settingElement.addContent(connect_maskElement);
			
			org.jdom.Element connect_ntp = new org.jdom.Element("ntp");
			connect_ntp.setText(roadObjectModel.getRoadObjectNTP());
			connection_settingElement.addContent(connect_ntp);
			
			org.jdom.Element connect_delayElement = new org.jdom.Element("delay");
			connect_delayElement.setText(roadObjectModel.getDelay());
			connection_settingElement.addContent(connect_delayElement);

			org.jdom.Element connect_delayYFElement = new org.jdom.Element("delayYF");
			connect_delayYFElement.setText(roadObjectModel.getDelayYF());
			connection_settingElement.addContent(connect_delayYFElement);

			org.jdom.Element connect_sleep_timeElement = new org.jdom.Element("sleep_time");
			connect_sleep_timeElement.setText(roadObjectModel.getSleepTime());
			connection_settingElement.addContent(connect_sleep_timeElement);

			org.jdom.Element kdk_portElement = new org.jdom.Element("kdk_port");
			kdk_portElement.setAttribute("port", roadObjectModel.getRoadObjectKDPPort());
			kdk_portElement.setAttribute("speed", roadObjectModel.getRoadObjectKDPSpeed());			

			org.jdom.Element led_portElement = new org.jdom.Element("led_port");
			led_portElement.setAttribute("port", roadObjectModel.getRoadObjectLEDPort());
			led_portElement.setAttribute("speed", roadObjectModel.getRoadObjectLEDSpeed());

			connection_settingElement.addContent(led_portElement);
			connection_settingElement.addContent(kdk_portElement);
			objectElement.addContent(connection_settingElement);
			
			rootElement.addContent(objectElement);
			/////////////////////////////////////////////////////////////////////////////////////////////////

			// DIRECTIONS
			org.jdom.Element directionsElement = new org.jdom.Element("directions");

			List<RoadDirection> roadDirectionList = roadDirectionsModel.getRoadDirectionList();
			org.jdom.Element directionElement = null;
			for (RoadDirection roadDirection : roadDirectionList) {
				directionElement = new org.jdom.Element("direction");

				directionElement.setAttribute("id", roadDirection.getRoadDirections_number());
				directionElement.setAttribute("type", roadDirection.getRoadDirections_typeOfDirection().getTypDirection());
				if (roadDirection.getRoadDirections_chanal_1() != null) {
					directionElement.setAttribute("channel_1", roadDirection.getRoadDirections_chanal_1());
				}
				if (roadDirection.getRoadDirections_chanal_2() != null) {
					directionElement.setAttribute("channel_2", roadDirection.getRoadDirections_chanal_2());
				}
				if (roadDirection.getRoadDirections_chanal_3() != null) {
					directionElement.setAttribute("channel_3", roadDirection.getRoadDirections_chanal_3());
				}
				if (roadDirection.getRoadDirections_chanal_4() != null) {
					directionElement.setAttribute("channel_4", roadDirection.getRoadDirections_chanal_4());
				}
				
				if(roadDirection.getRoadDirections_control_1() != null) {
					directionElement.setAttribute("control1", roadDirection.getRoadDirections_control_1());
				}
				if(roadDirection.getRoadDirections_control_2() != null) {
					directionElement.setAttribute("control2", roadDirection.getRoadDirections_control_2());
				}

				directionsElement.addContent(directionElement);
			}

			Map<String, PromtactData> mapOfBasePromtactMap = roadDirectionsModel.getMapOfBasePromtact();
			org.jdom.Element dasePromtactElement = new org.jdom.Element("base_promtact");
			for (Map.Entry<String, PromtactData> entry : mapOfBasePromtactMap.entrySet()) {
				directionElement = new org.jdom.Element("direction");

				directionElement.setAttribute("number", entry.getKey());
				directionElement.setAttribute("KZD", entry.getValue().getRoadPromtactu_endGreenAddit());
				directionElement.setAttribute("KZM", entry.getValue().getRoadPromtactu_durationGreenBlink());
				directionElement.setAttribute("KG", entry.getValue().getRoadPromtactu_durationYellow());
				directionElement.setAttribute("KK", entry.getValue().getRoadPromtactu_endRed());
				directionElement.setAttribute("KKG", entry.getValue().getRoadPromtactu_durationRedYellow());

				dasePromtactElement.addContent(directionElement);
			}
			directionsElement.addContent(dasePromtactElement);
			
			rootElement.addContent(directionsElement);
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// GROUP CONTROL
			org.jdom.Element groupControlsElement = new org.jdom.Element("groups");
			
			Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> groupControlHBoxCellListMap = roadDirectionsModel.getGroupControlHBoxCellListMap();
			org.jdom.Element groupElement = null;
			for(Map.Entry<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> entry : groupControlHBoxCellListMap.entrySet()) {
				groupElement = new org.jdom.Element("group");
				groupElement.setAttribute("id", entry.getKey().getNumberOfControl().getText());
				
				List<ControlledChanelHBoxCell> controlledChanelHBoxCells = entry.getValue();
				org.jdom.Element controlDirElement = null;
				for(ControlledChanelHBoxCell controlledChanelHBoxCell : controlledChanelHBoxCells) {
					controlDirElement = new org.jdom.Element("control_direction");
					controlDirElement.setAttribute("id", controlledChanelHBoxCell.getComboBoxDirection().getValue());
					controlDirElement.setAttribute("channel", controlledChanelHBoxCell.getTextFieldChanel().getText());
					
					groupElement.addContent(controlDirElement);
				}
				
				groupControlsElement.addContent(groupElement);
			}
			
			rootElement.addContent(groupControlsElement);
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			// PHASES
			org.jdom.Element phasesElement = new org.jdom.Element("phases");

			List<RoadPhase> roadPhaseList = roadPhaseModel.getRoadPhaseList();
			Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOpenDirectionInPhase = roadPhaseModel.getMapOpenDirectionInPhase();
			org.jdom.Element phaseElement = null;
			for (RoadPhase roadPhase : roadPhaseList) {
				phaseElement = new org.jdom.Element("phase");

				phaseElement.setAttribute("id", roadPhase.getRoadPhase_number());
				phaseElement.setAttribute("type", roadPhase.getRoadPhase_phaseTVP().getTvp());
				phaseElement.setAttribute("t_min", roadPhase.getRoadPhase_Tmin());
				if (roadPhase.getRoadPhase_panelTVP_1() != null) {
					phaseElement.setAttribute("panel_1", roadPhase.getRoadPhase_panelTVP_1());
				}
				if (roadPhase.getRoadPhase_panelTVP_2() != null) {
					phaseElement.setAttribute("panel_2", roadPhase.getRoadPhase_panelTVP_2());
				}
				
				for (Map.Entry<String, List<OpenDirectionInCurrentPhaseHBoxCell>> entry : mapOpenDirectionInPhase.entrySet()) {
					if(roadPhase.getRoadPhase_number().equals(entry.getKey())) {
						org.jdom.Element openDirectionElement = null;
						
						if(entry.getValue() != null) {
						
							for (OpenDirectionInCurrentPhaseHBoxCell openDirection : entry.getValue()) {
								if(openDirection != null) {
									openDirectionElement = new org.jdom.Element("open_direction");
									if(openDirection.getCheckBox().isSelected()) {
										openDirectionElement.setAttribute("blinking", "true");
										openDirectionElement.setText(openDirection.getComboBox().getValue());
									}else {
										openDirectionElement.setAttribute("blinking", "false");
										openDirectionElement.setText(openDirection.getComboBox().getValue());
									}
									
									phaseElement.addContent(openDirectionElement);
								}
							}
						
						}
						
						
					}
				}
				
				phasesElement.addContent(phaseElement);
			}
			
			rootElement.addContent(phasesElement);
			
			/*for (Map.Entry<String, List<String>> entry : mapOpenDirectionInPhase.entrySet()) {
				phaseElement = new org.jdom.Element("phase");
				phaseElement.setAttribute("number", entry.getKey());
				phasesElement.addContent(phaseElement);

				org.jdom.Element openDirectionElement = null;
				for (String openDirection : entry.getValue()) {
					openDirectionElement = new org.jdom.Element("open_direction");
					openDirectionElement.setText(openDirection);
					phaseElement.addContent(openDirectionElement);
				}
			}*/

			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// DETECTOR
			List<Detector> detectorsList = roadDetectorModel.getDetectorsList();
			org.jdom.Element detectorsElement = new org.jdom.Element("detectors");
			org.jdom.Element detectorElement = null;
			for (Detector detector : detectorsList) {
				detectorElement = new org.jdom.Element("detector");
				String type = detector.getTypeDetector();
				if (type.equals("Камера") || type.equals("Радар")) {
					detectorElement.setAttribute("id", detector.getNumberDetector());
					detectorElement.setAttribute("type", detector.getTypeDetector());

					org.jdom.Element rootIDElement = new org.jdom.Element("root_id");
					rootIDElement.setText(detector.getRootID());
					detectorElement.addContent(rootIDElement);
					
					org.jdom.Element modelElement = new org.jdom.Element("model");
					modelElement.setText(detector.getModelDetector());
					detectorElement.addContent(modelElement);
					
					org.jdom.Element locationElement = new org.jdom.Element("location");
					locationElement.setText(detector.getLocationDetector());
					detectorElement.addContent(locationElement);

					org.jdom.Element faultTimeOutElement = new org.jdom.Element("fault_timeout");
					faultTimeOutElement.setText(detector.getFaultTimeoutDetector());
					detectorElement.addContent(faultTimeOutElement);

					org.jdom.Element connectionTypeElement = new org.jdom.Element("connection_type");
					connectionTypeElement.setText(detector.getConnectionType());
					detectorElement.addContent(connectionTypeElement);

					org.jdom.Element ipDetectorElement = new org.jdom.Element("ip");
					ipDetectorElement.setText(detector.getIPDetector());
					detectorElement.addContent(ipDetectorElement);

					org.jdom.Element portDetectorElement = new org.jdom.Element("port");
					portDetectorElement.setText(detector.getPort());
					detectorElement.addContent(portDetectorElement);

					org.jdom.Element portXMLDetectorElement = new org.jdom.Element("port_xml");
					portXMLDetectorElement.setText(detector.getPortXML());
					detectorElement.addContent(portXMLDetectorElement);

					org.jdom.Element portHHTPDetectorElement = new org.jdom.Element("port_HTTP");
					portHHTPDetectorElement.setText(detector.getPortHTTP());
					detectorElement.addContent(portHHTPDetectorElement);

					org.jdom.Element periodInterrogationDetectorElement = new org.jdom.Element("period_interrogation");
					periodInterrogationDetectorElement.setText(detector.getPeriodInterrogation());
					detectorElement.addContent(periodInterrogationDetectorElement);

					org.jdom.Element zoneElement = new org.jdom.Element("zone");
					zoneElement.setAttribute("type", detector.getTypeZone());
					if (!detector.getPeriodSaving().equals("")) {
						zoneElement.setAttribute("state", "true");
						zoneElement.setText(detector.getPeriodSaving());
						detectorElement.addContent(zoneElement);
					} else {
						zoneElement.setAttribute("state", "false");
						detectorElement.addContent(zoneElement);
					}
					detectorsElement.addContent(detectorElement);
				}else {
					detectorElement.setAttribute("id", detector.getNumberDetector());
					detectorElement.setAttribute("type", detector.getTypeDetector());

					org.jdom.Element rootIDElement = new org.jdom.Element("root_id");
					rootIDElement.setText(detector.getRootID());
					detectorElement.addContent(rootIDElement);
					
					org.jdom.Element modelElement = new org.jdom.Element("model");
					modelElement.setText(detector.getModelDetector());
					detectorElement.addContent(modelElement);
					
					org.jdom.Element locationElement = new org.jdom.Element("location");
					locationElement.setText(detector.getLocationDetector());
					detectorElement.addContent(locationElement);

					org.jdom.Element faultTimeOutElement = new org.jdom.Element("fault_timeout");
					faultTimeOutElement.setText(detector.getFaultTimeoutDetector());
					detectorElement.addContent(faultTimeOutElement);

					org.jdom.Element connectionTypeElement = new org.jdom.Element("connection_type");
					connectionTypeElement.setText(detector.getConnectionType());
					detectorElement.addContent(connectionTypeElement);
					
					org.jdom.Element spiElement = new org.jdom.Element("spi_channel");
					spiElement.setText(detector.getSpi());
					detectorElement.addContent(spiElement);
					
					org.jdom.Element delayElement = new org.jdom.Element("delay");
					delayElement.setText(detector.getResponse());
					detectorElement.addContent(delayElement);
					
					detectorsElement.addContent(detectorElement);
				}

			}

			rootElement.addContent(detectorsElement);
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// PROGRAM
			Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram = roadProgramsModel.getMapOfPhasesInProgram();
			Map<String, List<SpeedSign>> mapOfProgramSpeedSign = roadProgramsModel.getMapOfProgramSpeedSign();
			Map<RoadProgram, List<SwitchPhase>> mapOfSwichPhase = roadProgramsModel.getMapOfSwichPhase();
			List<RoadProgram> roadProgramsList = roadProgramsModel.getRoadProgramList();
			
			for(Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {		// remove empty phase in program
				List<PhaseInProgram> phaseInPrograms = entry.getValue();
				
				phaseInPrograms.removeIf(phase -> phase.getPhaseInProgramNumber() == null || phase.getDurationPhaseInProgram() == null);
				
			}

			org.jdom.Element programsElement = new org.jdom.Element("programs");

			org.jdom.Element programElement = null;
			
			for(RoadProgram existedRoadProgram : roadProgramsList) {
				if(!existedRoadProgram.getRoadProgram_programMode().getMode().equals("Замена фаз")) {
					
					programElement = new org.jdom.Element("program");
					programElement.setAttribute("id", existedRoadProgram.getRoadProgram_number());
					programElement.setAttribute("type", existedRoadProgram.getRoadProgram_programMode().getMode());
					programElement.setAttribute("backup_program", existedRoadProgram.getRoadProgram_backupProgram().getBackupProgram());
					
					org.jdom.Element phaseInProgramElement = null;
					List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(existedRoadProgram);
					for (PhaseInProgram phaseInProgram : phaseInProgramsList) {
						phaseInProgramElement = new org.jdom.Element("phase");
						phaseInProgramElement.setAttribute("id", phaseInProgram.getPhaseInProgramNumber().getPhaseNumber());
						phaseInProgramElement.setText(phaseInProgram.getDurationPhaseInProgram());
						programElement.addContent(phaseInProgramElement);
					}
					List<SpeedSign> speedSignsList = mapOfProgramSpeedSign.get(existedRoadProgram.getRoadProgram_number());
					if (speedSignsList.size() >= 0) {
						org.jdom.Element speedSignElement = null;
						for (SpeedSign speedSign : speedSignsList) {
							speedSignElement = new org.jdom.Element("speed_sign");
							speedSignElement.setAttribute("id", speedSign.getNumberSpeedSign());
							speedSignElement.setAttribute("value", speedSign.getRecomendSpeed());
							programElement.addContent(speedSignElement);
						}
					}
					
					programsElement.addContent(programElement);
					
				}else {
					if(!mapOfSwichPhase.isEmpty()) {
						
						programElement = new org.jdom.Element("program");
						programElement.setAttribute("id", existedRoadProgram.getRoadProgram_number());
						programElement.setAttribute("type", existedRoadProgram.getRoadProgram_programMode().getMode());
						programElement.setAttribute("backup_program", existedRoadProgram.getRoadProgram_backupProgram().getBackupProgram());
						
						org.jdom.Element swichPhaseElement = null;
						List<SwitchPhase> swichPhasesList = mapOfSwichPhase.get(existedRoadProgram);
						
						for(SwitchPhase existedSwichPhase : swichPhasesList) {
							swichPhaseElement = new org.jdom.Element("switch_phase");
							
							if(existedSwichPhase.getPhase().getPhaseNumber() != null) {
								swichPhaseElement.setAttribute("id", existedSwichPhase.getPhase().getPhaseNumber());
							}else {
								swichPhaseElement.setAttribute("id", "");
							}
							
							if(existedSwichPhase.getToPhase1().getPhaseNumber() != null) {
								swichPhaseElement.setAttribute("to_phase1", existedSwichPhase.getToPhase1().getPhaseNumber());
							}else {
								swichPhaseElement.setAttribute("to_phase1", "");
							}
							
							if(existedSwichPhase.getToPhase2().getPhaseNumber() != null) {
								swichPhaseElement.setAttribute("to_phase2", existedSwichPhase.getToPhase2().getPhaseNumber());
							}else {
								swichPhaseElement.setAttribute("to_phase2", "");
							}
							
							if(existedSwichPhase.getMainTime() != null) {
								swichPhaseElement.setAttribute("main_time", existedSwichPhase.getMainTime());
							}else {
								swichPhaseElement.setAttribute("main_time", "");
							}
							
							if(existedSwichPhase.getPromtact() != null) {
								swichPhaseElement.setAttribute("promtact", existedSwichPhase.getPromtact());
							}else {
								swichPhaseElement.setAttribute("promtact", "");
							}
							
							if(existedSwichPhase.getDurationPhase() != null) {
								swichPhaseElement.setAttribute("duration", existedSwichPhase.getDurationPhase());
							}else {
								swichPhaseElement.setAttribute("duration", "");
							}
							
							if(existedSwichPhase.getSwitchPhaseMode().getMode() != null) {
								swichPhaseElement.setAttribute("mode", existedSwichPhase.getSwitchPhaseMode().getMode());
							}else {
								swichPhaseElement.setAttribute("mode", "");
							}
														
							programElement.addContent(swichPhaseElement);
						}
						
						programsElement.addContent(programElement);
						
					}
				}
			}

			rootElement.addContent(programsElement);
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// CALENDAR
			org.jdom.Element calendarElement = new org.jdom.Element("calendar");

			// by_weekday element
			org.jdom.Element by_weekdayElement = new org.jdom.Element("by_weekday");

			// day by weekday element
			org.jdom.Element dayByWeekDayElement = null;

			org.jdom.Element programByWeekDayElement = null;

			Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> mapOfWeekDayCalendar = roadProgramsModel.getMapOfWeekCalendar();
			for (Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekDayCalendar.entrySet()) {
				ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = entry.getKey();

				dayByWeekDayElement = new org.jdom.Element("day");
				dayByWeekDayElement.setAttribute("id", scheduleCalendarWeekDayHBoxCell.getWeekDay().getText());
				List<ScheduleProgram> scheduleProgramsList = entry.getValue();
				for (ScheduleProgram scheduleProgram : scheduleProgramsList) {
					String progNumber = scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber();
					String startTime = scheduleProgram.getTimeONOfScheduleProgram();
					String offset = scheduleProgram.getDisplacementTimeOfScheduleProgram();

					programByWeekDayElement = new org.jdom.Element("program");
					programByWeekDayElement.setAttribute("id", progNumber);
					programByWeekDayElement.setAttribute("startTime", startTime);
					programByWeekDayElement.setAttribute("offset", offset);
					// programByWeekDayElement.setAttribute("tvp_group", "");

					dayByWeekDayElement.addContent(programByWeekDayElement);
				}

				by_weekdayElement.addContent(dayByWeekDayElement);

			}

			// by_date element
			org.jdom.Element by_dateElement = new org.jdom.Element("by_date");
			org.jdom.Element programByDateElement = null;
			// day by date element
			org.jdom.Element dayElement = null;
			Map<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> mapOfDateCalendar = roadProgramsModel.getMapOfDateCalendar();
			for(Map.Entry<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> entry : mapOfDateCalendar.entrySet()) {
				dayElement = new org.jdom.Element("day");
				String date = entry.getKey().getDatePicker().getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				dayElement.setAttribute("date", date);
				
				String day = entry.getKey().getChoiceBox().getValue();
				dayElement.setAttribute("fromDay", day);
				
				List<ScheduleProgram> scheduleProgramsList = entry.getValue();
				for(ScheduleProgram scheduleProgram : scheduleProgramsList) {
					programByDateElement = new org.jdom.Element("program");
					programByDateElement.setAttribute("id", scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber());
					programByDateElement.setAttribute("startTime", scheduleProgram.getTimeONOfScheduleProgram());
					programByDateElement.setAttribute("offset", scheduleProgram.getDisplacementTimeOfScheduleProgram());
					dayElement.addContent(programByDateElement);
				}
				by_dateElement.addContent(dayElement);
			}

			rootElement.addContent(calendarElement);
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// PROMTACT
			Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfInterphaseSpecificPromtact = roadPromtactuModel.getMapOfInterphaseSpecificPromtact();

			org.jdom.Element promtactsElement = new org.jdom.Element("promtacts");

			org.jdom.Element promtactElement = null;
			for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : mapOfInterphaseSpecificPromtact.entrySet()) {
				promtactElement = new org.jdom.Element("promtact");

				org.jdom.Element fromElement = new org.jdom.Element("from_phase");
				fromElement.setText(entry.getKey().getComboBoxFromPhase().getValue());
				promtactElement.addContent(fromElement);

				org.jdom.Element toElement = new org.jdom.Element("to_phase");
				toElement.setText(entry.getKey().getComboBoxToPhase().getValue());
				promtactElement.addContent(toElement);

				org.jdom.Element promtactDirectionsElement = new org.jdom.Element("directions");

				org.jdom.Element promDirectionElement = null;
				for (Map.Entry<String, PromtactData> promEntry : entry.getValue().entrySet()) {
					promDirectionElement = new org.jdom.Element("direction");
					promDirectionElement.setAttribute("id", promEntry.getKey());
					
					if(promEntry.getValue().isFullPromtact() == true) {
						promDirectionElement.setAttribute("isFull", "true");
					}else {
						promDirectionElement.setAttribute("isFull", "false");
					}
					
					
					if (promEntry.getValue().getRoadPromtactu_endGreenAddit() != null) {
						promDirectionElement.setAttribute("KZD", promEntry.getValue().getRoadPromtactu_endGreenAddit());
					}
					if (promEntry.getValue().getRoadPromtactu_durationGreenBlink() != null) {
						promDirectionElement.setAttribute("KZM", promEntry.getValue().getRoadPromtactu_durationGreenBlink());
					}
					if (promEntry.getValue().getRoadPromtactu_durationYellow() != null) {
						promDirectionElement.setAttribute("KG", promEntry.getValue().getRoadPromtactu_durationYellow());
					}
					if (promEntry.getValue().getRoadPromtactu_endRed() != null) {
						promDirectionElement.setAttribute("KK", promEntry.getValue().getRoadPromtactu_endRed());
					}
					if (promEntry.getValue().getRoadPromtactu_durationRedYellow() != null) {
						promDirectionElement.setAttribute("KKG", promEntry.getValue().getRoadPromtactu_durationRedYellow());
					}

					promtactDirectionsElement.addContent(promDirectionElement);
				}
				promtactElement.addContent(promtactDirectionsElement);
				promtactsElement.addContent(promtactElement);
			}
			
			rootElement.addContent(promtactsElement);
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// CONFLICT
			Map<String, List<ConflictWithDirection>> mapOfConflict = roadConflictsModel.getMapOfConflict();
			
			org.jdom.Element conflictsElement = new org.jdom.Element("conflicts");
			
			org.jdom.Element conflict_forElement = null;
			for(Map.Entry<String, List<ConflictWithDirection>> entryConflict : mapOfConflict.entrySet()) {
				
				conflict_forElement = new org.jdom.Element("conflict_for");
				conflict_forElement.setAttribute("id", entryConflict.getKey());
				
				List<ConflictWithDirection> conflictWithList = entryConflict.getValue();
				
				org.jdom.Element conflict_withElement = null;
				for(ConflictWithDirection conflictDirection : conflictWithList) {
					
					conflict_withElement = new org.jdom.Element("conflict_with");
					conflict_withElement.setText(conflictDirection.getConflictWithDirection());
					
					conflict_forElement.addContent(conflict_withElement);
				
				}
				
				conflictsElement.addContent(conflict_forElement);
			}
			rootElement.addContent(conflictsElement);
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			/*rootElement.addContent(objectElement);
			rootElement.addContent(directionsElement);
			rootElement.addContent(groupControlsElement);
			rootElement.addContent(phasesElement);
			rootElement.addContent(detectorsElement);
			rootElement.addContent(programsElement);
			rootElement.addContent(calendarElement);
			rootElement.addContent(promtactsElement);*/

			calendarElement.addContent(by_weekdayElement);
			calendarElement.addContent(by_dateElement);

			document.setRootElement(rootElement);

			XMLOutputter outter = new XMLOutputter();
			
			CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
			encoder.onMalformedInput(CodingErrorAction.REPORT);
			encoder.onUnmappableCharacter(CodingErrorAction.REPORT);
			
			String objectName = roadObjectModel.getRoadObjectName() +  ".xml";
			String fileName = objectName.trim();
			
			File folder = new File("Objects");
			if(!folder.exists()) {
				folder.mkdir();
			}		
			
			file = new PrintWriter(new OutputStreamWriter(new FileOutputStream(folder + File.separator + fileName),encoder));
			
			outter.output(document, file);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/*finally {

			file.close();
		}*/
	}

	/*public void checkEvent() {
		//System.out.println(iRoadModel);
		Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
		List<RoadPhase> roadPhasesList = iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList();
		Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfInterphaseSpecificPromtact = iRoadModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		Map<String, PromtactData> mapOfBasePromtact = iRoadModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();

		List<String> errorsList = new LinkedList<>();

		// CHECK IS CONTROLER 
		if(iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK().equals("")) {
			String error = "- Укажите тип используемого контроллера!\nДальнейшая проверка невозможна.";
			errorsList.add(error);
		}else {
			
			// CHECH IS PROTOKOL
			if(iRoadModel.getModel().getRoadObjectModel().getRoadObjectProtocol().equals("")) {
				String error = "- Укажите протокол связи для контроллера";
				errorsList.add(error);
			}
			///////////////////////////////////////////////////////////////////////////////////////////
			
			// CHECK IS DIRECTION
			if(iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().isEmpty()) {
				String error = "- Список направлений пуст";
				errorsList.add(error);
			}else {
				List<RoadDirection> roadDirectionsList = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();
				Map<String, String> mapOfChannels = new LinkedHashMap<>();
				for(RoadDirection roadDirection : roadDirectionsList) {
					if(roadDirection.getRoadDirections_number().equals("")) {
						String error = "- В таблице направлений, присутствует направление\nбез номера";
						errorsList.add(error);
					}else {
						String ch1 = roadDirection.getRoadDirections_chanal_1();	// check channel repeat
						String ch2 = roadDirection.getRoadDirections_chanal_2();
						String ch3 = roadDirection.getRoadDirections_chanal_3();
						String ch4 = roadDirection.getRoadDirections_chanal_4();
						if(!ch1.equals("")) {
							if(mapOfChannels.containsKey(ch1)) {
								String error = "- В направлении № " + roadDirection.getRoadDirections_number() + " дублируется номер\nкрасного канала";
								errorsList.add(error);
							}else {
								mapOfChannels.put(ch1, roadDirection.getRoadDirections_number());
							}
						}
						if(!ch2.equals("")) {
							if(mapOfChannels.containsKey(ch2)) {
								String error = "- В направлении № " + roadDirection.getRoadDirections_number() + " дублируется номер\nжелтого канала";
								errorsList.add(error);
							}else {
								mapOfChannels.put(ch2, roadDirection.getRoadDirections_number());
							}
						}
						if(!ch3.equals("")) {
							if(mapOfChannels.containsKey(ch3)) {
								String error = "- В направлении № " + roadDirection.getRoadDirections_number() + " дублируется номер\nзеленого канала";
								errorsList.add(error);
							}else {
								mapOfChannels.put(ch3, roadDirection.getRoadDirections_number());
							}
						}
						if(!ch4.equals("")) {
							if(mapOfChannels.containsKey(ch4)) {
								String error = "- В направлении № " + roadDirection.getRoadDirections_number() + " дублируется номер\nдоп. красного канала";
								errorsList.add(error);
							}else {
								mapOfChannels.put(ch4, roadDirection.getRoadDirections_number());
							}
						}
						
						String usedTypeKDK = iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK();		// check max allowed channel size
						List<TypeKDK> typeKDKsList = iRoadModel.getModel().getRoadObjectModel().getKdkTypeList();
						for(TypeKDK typeKDK : typeKDKsList) {
							if(usedTypeKDK.equals(typeKDK.getName_KDK())) {
								String maxChannels = typeKDK.getChanels();
								int max_channels = Integer.parseInt(maxChannels);
								
								String number = roadDirection.getRoadDirections_number();
								if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное направление")) {	
									int channel_1 = 0;
									int channel_2 = 0;
									int channel_3 = 0;
									
									if(!roadDirection.getRoadDirections_chanal_1().equals("")) {
										channel_1 = Integer.parseInt(roadDirection.getRoadDirections_chanal_1());
									}
									if(!roadDirection.getRoadDirections_chanal_2().equals("")) {
										channel_2 = Integer.parseInt(roadDirection.getRoadDirections_chanal_2());
									}
									if(!roadDirection.getRoadDirections_chanal_3().equals("")) {
										channel_3 = Integer.parseInt(roadDirection.getRoadDirections_chanal_3());
									}
									
									if(max_channels < channel_1) {
										String error = "- В направлении № " + number + " красный канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
									if(max_channels < channel_2) {
										String error = "- В направлении № " + number + " жёлтый канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
									if(max_channels < channel_3) {
										String error = "- В направлении № " + number + " зелёный канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
								}
								if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Пешеходное")) {
									int channel_1 = 0;
									int channel_3 = 0;
									
									if(!roadDirection.getRoadDirections_chanal_1().equals("")) {
										channel_1 = Integer.parseInt(roadDirection.getRoadDirections_chanal_1());
									}
									if(!roadDirection.getRoadDirections_chanal_3().equals("")) {
										channel_3 = Integer.parseInt(roadDirection.getRoadDirections_chanal_3());
									}
															
									if(max_channels < channel_1) {
										String error = "- В направлении № " + number + " красный канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
									if(max_channels < channel_3) {
										String error = "- В направлении № " + number + " зелёный канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
								}
								if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Поворотная стрелка")) {
									int channel_3 = 0;
									
									if(!roadDirection.getRoadDirections_chanal_3().equals("")) {
										channel_3 = Integer.parseInt(roadDirection.getRoadDirections_chanal_3());
									}
									
									if(max_channels < channel_3) {
										String error = "- В направлении № " + number + " зелёный канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
								}
								if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное с одним красным")) {
									int channel_1 = 0;
									
									if(!roadDirection.getRoadDirections_chanal_1().equals("")) {
										channel_1 = Integer.parseInt(roadDirection.getRoadDirections_chanal_1());
									}
									
									if(max_channels < channel_1) {
										String error = "- В направлении № " + number + " красный канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
								}
								if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное красное и желтое")) {
									int channel_1 = 0;
									int channel_2 = 0;
									
									if(!roadDirection.getRoadDirections_chanal_1().equals("")) {
										channel_1 = Integer.parseInt(roadDirection.getRoadDirections_chanal_1());
									}
									if(!roadDirection.getRoadDirections_chanal_2().equals("")) {
										channel_2 = Integer.parseInt(roadDirection.getRoadDirections_chanal_2());
									}
									
									if(max_channels < channel_1) {
										String error = "- В направлении № " + number + " красный канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
									if(max_channels < channel_2) {
										String error = "- В направлении № " + number + " жёлтый канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
								}					
								if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное два красных")) {
									int channel_1 = 0;
									int channel_2 = 0;
									int channel_3 = 0;
									int channel_4 = 0;
									
									if(!roadDirection.getRoadDirections_chanal_1().equals("")) {
										channel_1 = Integer.parseInt(roadDirection.getRoadDirections_chanal_1());
									}
									if(!roadDirection.getRoadDirections_chanal_2().equals("")) {
										channel_2 = Integer.parseInt(roadDirection.getRoadDirections_chanal_2());
									}
									if(!roadDirection.getRoadDirections_chanal_3().equals("")) {
										channel_3 = Integer.parseInt(roadDirection.getRoadDirections_chanal_3());
									}
									if(!roadDirection.getRoadDirections_chanal_4().equals("")) {
										channel_4 = Integer.parseInt(roadDirection.getRoadDirections_chanal_4());
									}
									
									if(max_channels < channel_1) {
										String error = "- В направлении № " + number + " красный канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
									if(max_channels < channel_2) {
										String error = "- В направлении № " + number + " жёлтый канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
									if(max_channels < channel_3) {
										String error = "- В направлении № " + number + " зелёный канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
									if(max_channels < channel_4) {
										String error = "- В направлении № " + number + " красный-доп. канал превышает поддерживаемое\nданным контроллером количество каналов! " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
								}
							}
						}
					}
				}
			}
			///////////////////////////////////////////////////////////////////////////////////////////
			
			// CHECK IS PHASE
			if(iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList().isEmpty()) {
				String error = "- Список фаз пуст";
				errorsList.add(error);
			}else {
				roadPhasesList = iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList();
				for(RoadPhase roadPhase : roadPhasesList) {
					if(roadPhase.getRoadPhase_number().equals("")) {
						String error = "- В таблице фаз, присутствует фаза\nбез номера";
						errorsList.add(error);
					}
					if(roadPhase.getRoadPhase_Tmin().equals("")) {
						String error = "- В фазе № " + roadPhase.getRoadPhase_number() + "укажите\nзначение Т Min";
						errorsList.add(error);
					}
				}
			}
			///////////////////////////////////////////////////////////////////////////////////////////
			
			// CHECK IS PROGRAM
			if(iRoadModel.getModel().getRoadProgramsModel().getRoadProgramList().isEmpty()) {
				String error = "- Список программ пуст";
				errorsList.add(error);
			}else {
				List<RoadProgram> roadProgramsList = iRoadModel.getModel().getRoadProgramsModel().getRoadProgramList();
				for(RoadProgram roadProgram : roadProgramsList) {
					if(roadProgram.getRoadProgram_number().equals("")) {
						String error = "- В таблице программ, присутствует программа\nбез номера";
						errorsList.add(error);
					}
				}
				
				// check schedule of weekday
				Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> mapOfWeekCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();
				for(Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekCalendar.entrySet()) {
					
					List<ScheduleProgram> scheduleProgramsList = entry.getValue();
					if(scheduleProgramsList.isEmpty()) {
						String error = "- На " + entry.getKey().getWeekDay().getText() + " не задано расписание программ";
						errorsList.add(error);
					}else {
						List<String> elements=new ArrayList<>();
						for(ScheduleProgram scheduleProgram : scheduleProgramsList) {
							elements.add(scheduleProgram.getTimeONOfScheduleProgram());
						}
						
						//System.out.println("Время включения программ " + elements);
						
						Set<String> set=new HashSet<>();
						Set<String> duplicateElements=new HashSet<>();
						
						for (String element : elements) {
							if(!set.add(element)){
								duplicateElements.add(element);
							}
						}
						//System.out.println("Duplicate Elements : " + duplicateElements);
						
						if(!duplicateElements.isEmpty()) {
							String error = "- На " + entry.getKey().getWeekDay().getText() + " заданно несколько программ с\n идентичным временем включения";
							errorsList.add(error);
						}
					}
				}
				
				// check schedule of day
				Map<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> mapOfDay = iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar();
				for(Map.Entry<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> entry : mapOfDay.entrySet()) {
					List<ScheduleProgram> scheduleProgramsList = entry.getValue();
					
					if(scheduleProgramsList.isEmpty()) {
						String date = entry.getKey().getDatePicker().getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
						String error = "- На " + date + " не задано расписание программ";
						errorsList.add(error);
					}else {
						List<String> elements=new ArrayList<>();
						for(ScheduleProgram scheduleProgram : scheduleProgramsList) {
							elements.add(scheduleProgram.getTimeONOfScheduleProgram());
						}
						
						Set<String> set=new HashSet<>();
						Set<String> duplicateElements=new HashSet<>();
						
						for (String element : elements) {
							if(!set.add(element)){
								duplicateElements.add(element);
							}
						}
						
						if(!duplicateElements.isEmpty()) {
							String error = "- На " + entry.getKey().getDatePicker().getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " заданно несколько программ с\n идентичным временем включения";
							errorsList.add(error);
						}
					}
				}
			}
			///////////////////////////////////////////////////////////////////////////////////////////
			
			// CHECK IS INTERPHASE
			if(mapOfInterphaseSpecificPromtact.isEmpty()) {
				String error = "Не задано ни одного межфазного перехода";
				errorsList.add(error);
			}else {
				for (Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {	// CHECK DURATION PHASE VALUE
					List<PhaseInProgram> phaseInProgramsList = entry.getValue();

					List<Integer> allPromtactValues;

					for (int i = 0; i < phaseInProgramsList.size(); i++) {
						PhaseInProgram phaseInProgramFrom = phaseInProgramsList.get(i);
						PhaseInProgram phaseInProgramTo;
						if (i == phaseInProgramsList.size() - 1) {
							phaseInProgramTo = phaseInProgramsList.get(0);
						} else {
							phaseInProgramTo = phaseInProgramsList.get(i + 1);
						}

						int duration = 0;
						int tMin = 0;
						int promtact = 0;
						boolean isSpecPromtact = false;

						String phaseInProgramNumberFrom = phaseInProgramFrom.getPhaseInProgramNumber().getPhaseNumber();
						String phaseInProgramNumberTo = phaseInProgramTo.getPhaseInProgramNumber().getPhaseNumber();

						String phaseDurationFrom = phaseInProgramFrom.getDurationPhaseInProgram(); // duration phase in program
						duration = Integer.parseInt(phaseDurationFrom);

						for (RoadPhase roadPhase : roadPhasesList) {
							if (phaseInProgramNumberFrom.equals(roadPhase.getRoadPhase_number())) {
								String tMinPhase = roadPhase.getRoadPhase_Tmin(); // tMin phase
								tMin = Integer.parseInt(tMinPhase);
							}
						}

						for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entryPromtact : mapOfInterphaseSpecificPromtact.entrySet()) {
							String specPromFrom = entryPromtact.getKey().getComboBoxFromPhase().getValue();
							String specPromTo = entryPromtact.getKey().getComboBoxToPhase().getValue();

							if (phaseInProgramNumberFrom.equals(specPromFrom) || phaseInProgramNumberTo.equals(specPromTo)) {
								isSpecPromtact = true;
								Map<String, PromtactData> specPromMap = entryPromtact.getValue();
								allPromtactValues = new ArrayList<>();

								for (Map.Entry<String, PromtactData> entrySpecPromtact : specPromMap.entrySet()) {
									PromtactData promtactData = entrySpecPromtact.getValue();
									String greenBlink = promtactData.getRoadPromtactu_durationGreenBlink();
									String greenAddit = promtactData.getRoadPromtactu_endGreenAddit();
									String yellow = promtactData.getRoadPromtactu_durationYellow();
									String red = promtactData.getRoadPromtactu_endRed();
									String redYellow = promtactData.getRoadPromtactu_durationRedYellow();

									if (!greenBlink.equals("")) {
										allPromtactValues.add(Integer.parseInt(greenBlink));
									}
									if (!greenAddit.equals("")) {
										allPromtactValues.add(Integer.parseInt(greenAddit));
									}
									if (!yellow.equals("")) {
										allPromtactValues.add(Integer.parseInt(yellow));
									}
									if (!red.equals("")) {
										allPromtactValues.add(Integer.parseInt(red));
									}
									if (!redYellow.equals("")) {
										allPromtactValues.add(Integer.parseInt(redYellow));
									}
								}
								promtact = Collections.max(allPromtactValues);
								break;
							}
						}
						if (isSpecPromtact == false) {
							Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> openDirectionsInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
							List<OpenDirectionInCurrentPhaseHBoxCell> diferent = new ArrayList<>();
							allPromtactValues = new ArrayList<>();

							List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromList = openDirectionsInPhase.get(phaseInProgramNumberFrom);
							List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToList = openDirectionsInPhase.get(phaseInProgramNumberTo);

							for (OpenDirectionInCurrentPhaseHBoxCell existed : openDirectionsFromList) {
								if (!openDirectionsToList.contains(existed)) {
									diferent.add(existed);
								}
							}
							for (OpenDirectionInCurrentPhaseHBoxCell difDirection : diferent) {
								if (mapOfBasePromtact.containsKey(difDirection.getComboBox().getValue())) {
									PromtactData promtactData = mapOfBasePromtact.get(difDirection.getComboBox().getValue());
									String value = promtactData.getRoadPromtactu_endGreenAddit(); //

									allPromtactValues.add(Integer.parseInt(value));
								}
							}
							for (OpenDirectionInCurrentPhaseHBoxCell existed : openDirectionsToList) {
								if (!openDirectionsFromList.contains(existed)) {
									diferent.add(existed);
								}
							}
							for (OpenDirectionInCurrentPhaseHBoxCell difDirection : diferent) {
								if (mapOfBasePromtact.containsKey(difDirection.getComboBox().getValue())) {
									PromtactData promtactData = mapOfBasePromtact.get(difDirection.getComboBox().getValue());
									String value = promtactData.getRoadPromtactu_endRed();

									allPromtactValues.add(Integer.parseInt(value));
								}
							}
							promtact = Collections.max(allPromtactValues);
						}
						if (duration >= tMin + promtact) {
							System.out.println("IT'S OK: " + "duration = " + duration + " Tmin = " + tMin + " promtact = " + promtact);
						} else {
							System.out.println("ERROR");
							String error = "- Указанная длительность фазы № " + phaseInProgramNumberFrom + " в программе № " + entry.getKey().getRoadProgram_number() + " меньше допустимого.";
							errorsList.add(error);
						}
					}
				}
			}
			//////////////////////////////////////////////////////////////////////////////////////////
			
		}
		
		
		
				
		
		if (!errorsList.isEmpty()) {
			String allErrors = "";
			for (String error : errorsList) {
				allErrors += error + "\n";
			}
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText(allErrors);
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
		}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Информация");
			alert.setHeaderText("Проверка данных завершилась успешно.\nОшибок небыло обнаружено");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
		}
	}*/
	
	public void showBottomBar(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;
	}

	public void saveObject() {
		System.out.println("Button 'Save' was clicked");
		System.out.println("=====================================");
		
		RoadObjectModel roadObjectModel = iRoadModel.getModel().getRoadObjectModel();
		String objectName = roadObjectModel.getRoadObjectName();
		String fileName = objectName.trim();
		
		List<String> allFilesList = new ArrayList<>();
		
		// get all files from folder 'Object'
		File folder = new File(System.getProperty("user.dir") + File.separator + "Objects");
		if(folder.exists()) {
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				String existFileName = file.getName();
				if (existFileName.indexOf(".") > 0) {
					existFileName = existFileName.substring(0, existFileName.lastIndexOf("."));
					allFilesList.add(existFileName);		// add files name
				}
			}
		}
		
		//System.out.println("Files: " + allFilesList);
		//System.out.println();
		
		// check if contains and create or update
		if(allFilesList.contains(fileName)) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Информация");
			alert.setHeaderText("Переписать данные объекта?");
			
			Stage mainStage = new Stage();
			mainStage = (Stage)alert.getDialogPane().getScene().getWindow();
			mainStage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
		    okButton.setText("Да, перепесать существующий");
		    okButton.setTooltip(new Tooltip("Да, переписать существующий"));
		    
		    Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
		    cancelButton.setText("Нет, создать новый");
		    cancelButton.setTooltip(new Tooltip("Нет, создать новый"));
		    
		    Optional<ButtonType> result = alert.showAndWait();
		    if(result.get() == ButtonType.OK) {
		    	xmlObject();
		    	Alert wasEditalert = new Alert(AlertType.INFORMATION);
		    	wasEditalert.setTitle("Информация");
		    	wasEditalert.setHeaderText("Объект успешно переписан");
		    	
		    	Stage stage = new Stage();
				stage = (Stage)wasEditalert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
		    	
		    	wasEditalert.show();
		    }
		    if(result.get() == ButtonType.CANCEL) {
		    	Alert needRename = new Alert(AlertType.WARNING);
		    	needRename.setTitle("Внимание");
		    	needRename.setHeaderText("Измените название объекта");
		    	
		    	Stage stage = new Stage();
				stage = (Stage)needRename.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
		    	
		    	needRename.show();
		    }
		}else {
			if(roadObjectModel.getRoadObjectName().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Внимание");
				alert.setHeaderText("Задайте имя объекту");
				
				Stage stage = new Stage();
				stage = (Stage)alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
				
				alert.show();
			}else {
				xmlObject();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Информация");
				alert.setHeaderText("Объект сохранен");
				
				Stage stage = new Stage();
				stage = (Stage)alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
				
				alert.show();
				
			}
		}
	}

	@FXML
	public void initialize() {

		//langXML();
		//loadLang(langXML);
	}

}
