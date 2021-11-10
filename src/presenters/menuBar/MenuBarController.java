package presenters.menuBar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import controllers.MainController;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import presenters.conflicts.ConflictWithDirection;
import presenters.conflicts.RoadConflictsModel;
import presenters.detector.Detector;
import presenters.detector.RoadDetectorModel;
import presenters.directions.ControlledChanelHBoxCell;
import presenters.directions.GroupControlHBoxCell;
import presenters.directions.RoadDirection;
import presenters.directions.RoadDirectionsModel;
import presenters.object.RoadObjectModel;
import presenters.phase.OpenDirectionInCurrentPhaseHBoxCell;
import presenters.phase.RoadPhase;
import presenters.phase.RoadPhaseModel;
import presenters.programs.PhaseInProgram;
import presenters.programs.RoadProgram;
import presenters.programs.RoadProgramsModel;
import presenters.programs.ScheduleCalendarDateHBoxCell;
import presenters.programs.ScheduleCalendarWeekDayHBoxCell;
import presenters.programs.ScheduleProgram;
import presenters.programs.SpeedSign;
import presenters.programs.SwitchPhase;
import presenters.promtactu.InterphaseTransitionsHBoxCell;
import presenters.promtactu.PromtactData;
import presenters.promtactu.RoadPromtactuModel;
import presenters.promtactu.RoadPromtactuPresenter;
import sourceKDAR.SourceKDAR;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jdom.output.XMLOutputter;

/**
 * Created by Vitaly on 03.12.2016.
 */
public class MenuBarController {

    @FXML
    private Menu menuObject, menuTools, menuService, menuCOMCON_ACADEM, menuAWP_technologist, menuDirectories, menuReference;

    @FXML
    private  MenuItem menuItemObjects, menuItemRefresh1, menuItemExportObject, menuItemImportObject, menuItemSaveKDAR, menuItemServiseDB, menuItemExit,
             menuItemIntensity, menuItemSettings, menuItemRefresh2, menuItemDiagram, menuItemCheck, menuItemEntry,
            menuItemReport, menuItemWorkSimulator, menuItemProtectionWhenDownloadingTheProgram, menuItemDefaultSettings, menuItemExportingDataToACSDD,
            menuItemActivateWorkWithACSDD, menuItemImportingDataFromTheWorkstation, menuItemDesignMode, menuItemReferenceBookOfControllers,
            menuItemDirectionOfReferrals, menuItemHelp, menuItemAbout;

    

	@FXML
    //private static ResourceBundle bundleGUI, bundleAlert;
    //private static Locale localeGUI, localeAlert;
    //static String langXML = null;

    MainController mainController;
	
	CheckObjectPresenter checkObjectPresenter;
	QuickCheckPresenter quickCheckPresenter;
	DiagramPresenter diagramPresenter;

    String dataBaseName = "";
    String port = "";
    String userName = "";
    String password = "";
    String localHost = "";
    
    File dcFile;

    public void appExit() {
    	System.out.println("Exit event");
    	
    	if(mainController.getModel() != null) {
    	
	    	RoadObjectModel roadObjectModel = mainController.getModel().getRoadObjectModel();
			RoadDirectionsModel roadDirectionsModel = mainController.getModel().getRoadDirectionModel();
			RoadPhaseModel roadPhaseModel = mainController.getModel().getRoadPhaseModel();
			RoadProgramsModel roadProgramsModel = mainController.getModel().getRoadProgramsModel();
			RoadPromtactuModel roadPromtactuModel = mainController.getModel().getRoadPromtactuModel();
			RoadDetectorModel roadDetectorModel = mainController.getModel().getRoadDetectorModel();
			RoadConflictsModel roadConflictsModel = mainController.getModel().getRoadConflictsModel();
	    	
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Информация");
			alert.setHeaderText("Вы хотите сохранить текущий объект?");
			
			Stage mainStage = new Stage();
			mainStage = (Stage)alert.getDialogPane().getScene().getWindow();
			mainStage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
		    okButton.setText("Сохранить");
		    
		    Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
		    cancelButton.setText("Не сохранять");
		    
		    Optional<ButtonType> result = alert.showAndWait();
		    if(result.get() == ButtonType.OK) {
				if(!roadObjectModel.getRoadObjectName().equals("")) {
				
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
						org.jdom.Element dasePromtactElement = new org.jdom.Element("dase_promtact");
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
									for (OpenDirectionInCurrentPhaseHBoxCell openDirection : entry.getValue()) {
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
							
							phasesElement.addContent(phaseElement);
						}
						
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
						
						
						rootElement.addContent(objectElement);
						rootElement.addContent(directionsElement);
						rootElement.addContent(groupControlsElement);
						rootElement.addContent(phasesElement);
						rootElement.addContent(detectorsElement);
						rootElement.addContent(programsElement);
						rootElement.addContent(calendarElement);
						rootElement.addContent(promtactsElement);
			
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
					}finally {
						file.close();
					}
					
					System.exit(0);
				}else {
					Alert alertName = new Alert(AlertType.WARNING);
					alertName.setTitle("Внимание");
					alertName.setHeaderText("Задайте имя объекта");
					
					Stage mainName = new Stage();
					mainName = (Stage)alertName.getDialogPane().getScene().getWindow();
					mainName.getIcons().add(new Image("image/other/komkon_logo_title.png"));
					
					alertName.show();
				}
		    }else {
		    	System.exit(0);
		    }
	    
    	}else {
    		System.exit(0);
    	}
	    
    }
    
    public String getDataBaseName() {
        return dataBaseName;
    }
    public String getPort() {
        return port;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    public String getLocalHost() {
        return localHost;
    }

    public void setMainController(MainController mController){
        this.mainController = mController;
    }
    public Menu getMenuObject() {
        return menuObject;
    }
    public Menu getMenuTools() {
        return menuTools;
    }
    public Menu getMenuService() {
        return menuService;
    }
    public Menu getMenuCOMCON_ACADEM() {
        return menuCOMCON_ACADEM;
    }
    public Menu getMenuAWP_technologist() {
        return menuAWP_technologist;
    }
    public Menu getMenuDirectories() {
        return menuDirectories;
    }
    public Menu getMenuReference() {
        return menuReference;
    }
    public MenuItem getMenuItemObjects() {
        return menuItemObjects;
    }
    public MenuItem getMenuItemRefresh1() {
        return menuItemRefresh1;
    }
    public MenuItem getMenuItemExportObject() {
        return menuItemExportObject;
    }
    public MenuItem getMenuItemImportObject() {
        return menuItemImportObject;
    }
    public MenuItem getMenuItemSaveKDAR() {
		return menuItemSaveKDAR;
	}
    public MenuItem getMenuItemServiseDB() {
        return menuItemServiseDB;
    }
    public MenuItem getMenuItemExit() {
        return menuItemExit;
    }
    
    public MenuItem getMenuItemIntensity() {
        return menuItemIntensity;
    }
    public MenuItem getMenuItemSettings() {
        return menuItemSettings;
    }
    public MenuItem getMenuItemRefresh2() {
        return menuItemRefresh2;
    }
    public MenuItem getMenuItemDiagram() {
        return menuItemDiagram;
    }
    public MenuItem getMenuItemCheck() {
        return menuItemCheck;
    }
    public MenuItem getMenuItemEntry() {
        return menuItemEntry;
    }
    public MenuItem getMenuItemReport() {
        return menuItemReport;
    }
    public MenuItem getMenuItemWorkSimulator() {
        return menuItemWorkSimulator;
    }
    public MenuItem getMenuItemProtectionWhenDownloadingTheProgram() {
        return menuItemProtectionWhenDownloadingTheProgram;
    }
    public MenuItem getMenuItemDefaultSettings() {
        return menuItemDefaultSettings;
    }
    public MenuItem getMenuItemExportingDataToACSDD() {
        return menuItemExportingDataToACSDD;
    }
    public MenuItem getMenuItemActivateWorkWithACSDD() {
        return menuItemActivateWorkWithACSDD;
    }
    public MenuItem getMenuItemImportingDataFromTheWorkstation() {
        return menuItemImportingDataFromTheWorkstation;
    }
    public MenuItem getMenuItemDesignMode() {
        return menuItemDesignMode;
    }
    public MenuItem getMenuItemReferenceBookOfControllers() {
        return menuItemReferenceBookOfControllers;
    }
    public MenuItem getMenuItemDirectionOfReferrals() {
        return menuItemDirectionOfReferrals;
    }
    public MenuItem getMenuItemHelp() {
        return menuItemHelp;
    }
    public MenuItem getMenuItemAbout() {
        return menuItemAbout;
    }

    public boolean joinToBase(){
        if(!dataBaseName.equals("") || !port.equals("") ||!userName.equals("") ||!localHost.equals("") ||!password.equals("")){
            return true;
        }else
            return false;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////                                LOCALE                   /////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*public void loadLangMenuBar(String lang){
        localeGUI = new Locale(lang);
        bundleGUI = ResourceBundle.getBundle("bundle.locale_lang", localeGUI);
    }

    /////////////////////////////////////////////////////////////////////
    ////////// See which language is installed in the file //////////////
    /////////////////////////////////////////////////////////////////////
    private void langXML(){
        String filepath = System.getProperty("user.dir") +"\\" + "configuration.xml";
        File xmlFile = new File(filepath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;


        try{
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("lang");
            Node node = nodeList.item(0);
            langXML = node.getTextContent();

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }*/

    @FXML
    public void openTreeView(){
        mainController.showTreeView();
    }
    
    @FXML
    public void openDiagram() {
    	if(mainController.getModel() != null) {
    		
    		try {
    			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Diagram.fxml"));
	    		AnchorPane anchorPane = fxmlLoader.load();
	    		
	    		diagramPresenter = fxmlLoader.getController();	    		
	    		diagramPresenter.show(mainController.getModel());
	    		
	    		Scene scene = new Scene(anchorPane);
	    		
	    		Stage stage = new Stage();
	    		stage.initStyle(StageStyle.DECORATED);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
				stage.setTitle("Временная диаграмма работы контроллера");
				stage.setScene(scene);
				stage.showAndWait();
	    		
    		}catch (IOException e) {
				e.printStackTrace();
			}
    		
    		
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Создайте новый или откройте существующий объект");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
    	}
    }
    
    @FXML
    public void createKDARFiles() {
    	
    	if(mainController.getModel() != null) {

	    	try {
	    		
	    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CheckObject.fxml"));
	    		AnchorPane anchorPane = fxmlLoader.load();
	    		
	    		checkObjectPresenter = fxmlLoader.getController();
	    		
	    		checkObjectPresenter.show(mainController.getModel());
	    		
	    		Scene scene = new Scene(anchorPane);
	    		
	    		Stage stage = new Stage();
				stage.initStyle(StageStyle.DECORATED);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
				stage.setTitle("Запись КДАР");
				stage.setScene(scene);
				stage.show();
				
	    	}catch (IOException e) {
				e.printStackTrace();
			}
    	
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Создайте новый или откройте существующий объект");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
    	}
    	
    }

    @FXML
    public void JoinToDB(){
        //langXML();
        //loadLangMenuBar(langXML);

        /*try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/JoinDB.fxml"));
            AnchorPane pane = fxmlLoader.load();

            JoinDBPresenter joinDBPresenter = fxmlLoader.getController();

            Scene scene = new Scene(pane);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Подключение к БД");
            stage.setScene(scene);
            stage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @FXML
    public void openIntensity(){
        mainController.showIntensity();
    }
    
    @FXML
    public void openHelp() {
    	System.out.println("Open help");
    	
    	try {
			Runtime.getRuntime().exec("hh.exe Help.chm");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    public void saveKDAR() {
    	
    	if(mainController.getModel() != null) {
        	
	    	try {
	    		
	    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CheckObject.fxml"));
	    		AnchorPane anchorPane = fxmlLoader.load();
	    		
	    		checkObjectPresenter = fxmlLoader.getController();
	    		
	    		checkObjectPresenter.show(mainController.getModel());
	    		
	    		Scene scene = new Scene(anchorPane);
	    		
	    		Stage stage = new Stage();
				stage.initStyle(StageStyle.DECORATED);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
				stage.setTitle("Запись КДАР");
				stage.setScene(scene);
				stage.showAndWait();
	    		
	    	}catch (IOException e) {
				e.printStackTrace();
			}
    	
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Создайте новый или откройте существующий объект");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
    	}
    	
    }
    
    @FXML
    public void refresh() {
    	System.out.println("'Refresh' event");  	
    	
    	if(mainController.getModel() != null) {
    	
	    	if(!mainController.getModel().getRoadObjectModel().getRoadObjectName().equals("")) {
	    	
	    		RoadObjectModel roadObjectModel = mainController.getModel().getRoadObjectModel();
	    		RoadDirectionsModel roadDirectionsModel = mainController.getModel().getRoadDirectionModel();
	    		RoadPhaseModel roadPhaseModel = mainController.getModel().getRoadPhaseModel();
	    		RoadProgramsModel roadProgramsModel = mainController.getModel().getRoadProgramsModel();
	    		RoadPromtactuModel roadPromtactuModel = mainController.getModel().getRoadPromtactuModel();
	    		RoadDetectorModel roadDetectorModel = mainController.getModel().getRoadDetectorModel();
	    		RoadConflictsModel roadConflictsModel = mainController.getModel().getRoadConflictsModel();
				
				
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
				}finally {
					file.close();
				}
	    	}else {
	    		Alert alert = new Alert(AlertType.WARNING);
	    		alert.setTitle("Внимание");
	    		alert.setHeaderText("Укажите имя объекта");
	    		
	    		Stage stage = new Stage();
				stage = (Stage)alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
	    		
	    		alert.show();
	    	}
    	
    	}
    	
    }
    
    public void quickCheck() {
    	System.out.println("Quick check event");
    	
    	if(mainController.getModel() != null) {
        	
	    	try {
	    		
	    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/QuickCheck.fxml"));
	    		AnchorPane anchorPane = fxmlLoader.load();
	    		
	    		quickCheckPresenter = fxmlLoader.getController();
	    		
	    		quickCheckPresenter.show(mainController.getModel());
	    		
	    		Scene scene = new Scene(anchorPane);
	    		
	    		Stage stage = new Stage();
				stage.initStyle(StageStyle.DECORATED);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
				stage.setTitle("Запись КДАР");
				stage.setScene(scene);
				stage.showAndWait();
	    		
	    	}catch (IOException e) {
				e.printStackTrace();
			}
    	
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Создайте новый или откройте существующий объект");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
    	}
    	
    }
    
    @FXML
    public void initialize() {
        //langXML();
    }

}
