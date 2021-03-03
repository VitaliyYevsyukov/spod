package presenters.object;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presenters.conflicts.ConflictWithDirection;
import presenters.detector.Detector;
import presenters.detector.RoadDetectorHBoxCell;
import presenters.directions.ControlledChanelHBoxCell;
import presenters.directions.GroupControlHBoxCell;
import presenters.directions.RoadDirection;
import presenters.directions.TypDirection;
import presenters.phase.OpenDirectionInCurrentPhaseHBoxCell;
import presenters.phase.RoadPhase;
import presenters.phase.TVP;
import presenters.programs.BackupProgram;
import presenters.programs.PhaseInProgram;
import presenters.programs.PhaseNumber;
import presenters.programs.ProgramMode;
import presenters.programs.RoadProgram;
import presenters.programs.ScheduleCalendarDateHBoxCell;
import presenters.programs.ScheduleCalendarWeekDayHBoxCell;
import presenters.programs.ScheduleNumber;
import presenters.programs.ScheduleProgram;
import presenters.programs.SpeedSign;
import presenters.programs.SwitchPhase;
import presenters.programs.SwitchPhaseMode;
import presenters.promtactu.InterphaseTransitionsHBoxCell;
import presenters.promtactu.PromtactData;
import roadModel.IRoadModel;
import roadModel.RoadModel;

public class ListViewAllObjectPresenter {
	
	@FXML
	private ListView<String> listViewAllObjects;
	
	@FXML
    //private static ResourceBundle bundleGUI, bundleAlert;
    @SuppressWarnings("unused")
	//private static Locale localeGUI, localeAlert;
    //static String langXML = null;
    
    ObservableList<String> itemFiles = FXCollections.observableArrayList(); 
    
    public boolean selectObject = false;
    public boolean isDelete = false;
   
    private IRoadModel iRoadModel;
    RoadModel roadModel;
    List<RoadDirection> roadDirectionsList;
    
	
	public void show(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;
		
		File folder = new File(System.getProperty("user.dir") + File.separator + "Objects");
		if(folder.exists()) {
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
			    if (file.isFile()) {
			        String fileName = file.getName();
			        if (fileName.indexOf(".") > 0)
			            fileName = fileName.substring(0, fileName.lastIndexOf("."));
			        itemFiles.add(fileName);
			        listViewAllObjects.setItems(itemFiles);
			    }
			}
		}else {
			folder.mkdir();
		}
	}
    
    public void deleteFile() {
    	System.out.println();
    	System.out.println("=============== Delete file =================");
    	System.out.println();
    	
    	int fileToDelete = listViewAllObjects.getSelectionModel().getSelectedIndex();
    	
    	if(fileToDelete >= 0) {
    		String fileName = listViewAllObjects.getSelectionModel().getSelectedItem();
    		File file = new File(System.getProperty("user.dir") + File.separator + "Objects" + File.separator + fileName + ".xml");
    		
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Внимание");
    		alert.setHeaderText("Вы действительно хотите удалить объект\n" + fileName + " ?");
    		
    		Stage mainStage = new Stage();
			mainStage = (Stage)alert.getDialogPane().getScene().getWindow();
			mainStage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		
    		if(result.get() == ButtonType.OK) {
    			file.delete();
    			Alert alertOk = new Alert(AlertType.INFORMATION);
    			alertOk.setTitle("Информация");
    			alertOk.setHeaderText("Файл успешно удален");
    			
    			Stage stage = new Stage();
				stage = (Stage)alertOk.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
    			
    			alertOk.show();
    			
    			listViewAllObjects.getItems().remove(fileToDelete);
    		
    			isDelete = true;
    			
    		}
    		roadModel = new RoadModel();
    		iRoadModel.setModel(roadModel);
    		
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Ошибка");
    		alert.setHeaderText("Выбирите объект который хотите удалить");
    		
    		Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
    		
    		alert.show();
    	}
    }
	
	public void selectObject() {
		listViewAllObjects.setOnMouseClicked(event -> {
			if(event.getClickCount() == 2) {
				String fileName = listViewAllObjects.getSelectionModel().getSelectedItem();
				
				SAXBuilder builder = new SAXBuilder();
				File file = new File(System.getProperty("user.dir") + File.separator + "Objects" + File.separator + fileName + ".xml");
				
				//String currentDirectory = System.getProperty ("user.dir");
			    //System.out.println ("user.dir:" + currentDirectory);
				
				try {
					Document document = (Document) builder.build(file);
					Element rootNode = document.getRootElement();
					
					// object
					Element objectElement = (Element)rootNode.getChildren().get(0);
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectName(objectElement.getChildText("name"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectCountry(objectElement.getChildText("country"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectCity(objectElement.getChildText("city"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectObjectNumber(objectElement.getChildText("number"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectNetworkAddress(objectElement.getChildText("network_address"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectMagistral(objectElement.getChildText("magistral"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectProtocol(objectElement.getChildText("protocol"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectCharge(objectElement.getChildText("charge"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectDateOfCreation(objectElement.getChildText("date_create"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectLaunchDate(objectElement.getChildText("date_launch"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectTechnologist(objectElement.getChildText("technologist"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectNote(objectElement.getChildText("note"));
					iRoadModel.getModel().getRoadObjectModel().setRoadObjectTypeOfKDK(objectElement.getChildText("type_kdk"));
					
					Element objectElementConSettings = (Element)objectElement.getChild("connection_setting");
					if(objectElementConSettings != null) {
						iRoadModel.getModel().getRoadObjectModel().setRoadObjectConnectType(objectElementConSettings.getChildText("type_connection"));
						iRoadModel.getModel().getRoadObjectModel().setRoadObjectConnectPort(objectElementConSettings.getChildText("port"));
						iRoadModel.getModel().getRoadObjectModel().setRoadObjectConnectSpeed(objectElementConSettings.getChildText("speed"));
						iRoadModel.getModel().getRoadObjectModel().setRoadObjectIP(objectElementConSettings.getChildText("ip"));
						iRoadModel.getModel().getRoadObjectModel().setRoadObjectDNS(objectElementConSettings.getChildText("dns"));
						iRoadModel.getModel().getRoadObjectModel().setRoadObjectMASK(objectElementConSettings.getChildText("mask"));
						iRoadModel.getModel().getRoadObjectModel().setRoadObjectNTP(objectElementConSettings.getChildText("ntp"));
						iRoadModel.getModel().getRoadObjectModel().setDelay(objectElementConSettings.getChildText("delay"));
						iRoadModel.getModel().getRoadObjectModel().setDelayYF(objectElementConSettings.getChildText("delayYF"));
						iRoadModel.getModel().getRoadObjectModel().setSleepTime(objectElementConSettings.getChildText("sleep_time"));
						
						
						Element ledPortElement = (Element)objectElementConSettings.getChild("led_port");
						iRoadModel.getModel().getRoadObjectModel().setRoadObjectLEDPort(ledPortElement.getAttributeValue("port"));
						iRoadModel.getModel().getRoadObjectModel().setRoadObjectLEDSpeed(ledPortElement.getAttributeValue("speed"));
						
						Element kdkPortElement = (Element)objectElementConSettings.getChild("kdk_port");
						iRoadModel.getModel().getRoadObjectModel().setRoadObjectKDPPort(kdkPortElement.getAttributeValue("port"));
						iRoadModel.getModel().getRoadObjectModel().setRoadObjectKDPSpeed(kdkPortElement.getAttributeValue("speed"));
					}
					
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					// directions
					Element directionsElement = (Element)rootNode.getChildren().get(1);
					iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().clear();
					for(int i = 0; i < directionsElement.getChildren().size(); i++) {
						Element child = (Element)directionsElement.getChildren().get(i);
						if(child.getName().equals("direction")) {
							RoadDirection roadDirection = new RoadDirection();
							roadDirection.setRoadDirections_number(child.getAttributeValue("id"));
							roadDirection.setRoadDirections_typeOfDirection(new TypDirection(child.getAttributeValue("type")));
							if(child.getAttributeValue("channel_1") == null) {
								roadDirection.setRoadDirections_chanal_1("");
							}else {
								roadDirection.setRoadDirections_chanal_1(child.getAttributeValue("channel_1"));
							}
							if(child.getAttributeValue("channel_2") == null) {
								roadDirection.setRoadDirections_chanal_2("");
							}else {
								roadDirection.setRoadDirections_chanal_2(child.getAttributeValue("channel_2"));
							}
							if(child.getAttributeValue("channel_3") == null) {
								roadDirection.setRoadDirections_chanal_3("");
							}else {
								roadDirection.setRoadDirections_chanal_3(child.getAttributeValue("channel_3"));
							}
							if(child.getAttributeValue("channel_4") == null) {
								roadDirection.setRoadDirections_chanal_4("");
							}else {
								roadDirection.setRoadDirections_chanal_4(child.getAttributeValue("channel_4"));
							}
							
							if(child.getAttributeValue("control1") == null) {
								roadDirection.setRoadDirections_control_1("");
							}else {
								roadDirection.setRoadDirections_control_1(child.getAttributeValue("control1"));
							}
							if(child.getAttributeValue("control2") == null) {
								roadDirection.setRoadDirections_control_2("");
							}else {
								roadDirection.setRoadDirections_control_2(child.getAttributeValue("control2"));
							}
							
							iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList().add(roadDirection);
						}else {
							Map<String, PromtactData> mapOfBasePromtact = new LinkedHashMap<>();
							for(int j = 0; j < child.getChildren().size(); j++) {
								Element basePromtact = (Element)child.getChildren().get(j);
								PromtactData promtactData = new PromtactData();
								promtactData.setRoadPromtactu_endGreenAddit(basePromtact.getAttributeValue("KZD"));
								promtactData.setRoadPromtactu_durationGreenBlink(basePromtact.getAttributeValue("KZM"));
								promtactData.setRoadPromtactu_durationYellow(basePromtact.getAttributeValue("KG"));
								promtactData.setRoadPromtactu_endRed(basePromtact.getAttributeValue("KK"));
								promtactData.setRoadPromtactu_durationRedYellow(basePromtact.getAttributeValue("KKG"));
								mapOfBasePromtact.put(basePromtact.getAttributeValue("number"), promtactData);
							}
							iRoadModel.getModel().getRoadDirectionModel().setMapOfBasePromtact(mapOfBasePromtact);
						}
					}
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
										
					// group control
					Element groupControlElement = (Element)rootNode.getChildren().get(2);
					Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> groupControlMap = new LinkedHashMap<>();
					
					for(int i = 0; i < groupControlElement.getChildren().size(); i++) {
						Element groupElement = (Element)groupControlElement.getChildren().get(i);
						GroupControlHBoxCell groupControlHBoxCell = new GroupControlHBoxCell();
						groupControlHBoxCell.getNumberOfControl().setText(groupElement.getAttributeValue("id"));
						
						List<ControlledChanelHBoxCell> chanelHBoxCellsList = new ArrayList<>();
						for(int j = 0; j < groupElement.getChildren().size(); j++) {
							Element controlDir = (Element)groupElement.getChildren().get(j);
							ControlledChanelHBoxCell controlledChanelHBoxCell = new ControlledChanelHBoxCell();
							controlledChanelHBoxCell.getComboBoxDirection().setValue(controlDir.getAttributeValue("id"));
							controlledChanelHBoxCell.getTextFieldChanel().setText(controlDir.getAttributeValue("channel"));
							chanelHBoxCellsList.add(controlledChanelHBoxCell);
						}
						groupControlMap.put(groupControlHBoxCell, chanelHBoxCellsList);
					}
					iRoadModel.getModel().getRoadDirectionModel().setGroupControlHBoxCellListMap(groupControlMap);
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					// phases
					Element phasesElement = (Element)rootNode.getChildren().get(3);
					iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList().clear();
					Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOpenDirectionInPhase = new LinkedHashMap<>(); 
					for(int i = 0; i < phasesElement.getChildren().size(); i++) {
						Element phase = (Element)phasesElement.getChildren().get(i);
						RoadPhase roadPhase = new RoadPhase();
						roadPhase.setRoadPhase_number(phase.getAttributeValue("id"));
						roadPhase.setRoadPhase_phaseTVP(new TVP(phase.getAttributeValue("type")));
						roadPhase.setRoadPhase_Tmin(phase.getAttributeValue("t_min"));
						if(phase.getAttributeValue("panel_1") == null) {
							roadPhase.setRoadPhase_panelTVP_1("");
						}else {
							roadPhase.setRoadPhase_panelTVP_1(phase.getAttributeValue("panel_1"));
						}
						if(phase.getAttributeValue("panel_2") == null) {
							roadPhase.setRoadPhase_panelTVP_2("");
						}else {
							roadPhase.setRoadPhase_panelTVP_2(phase.getAttributeValue("panel_2"));
						}
						
						List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsList = new ArrayList<>();
						for(int j = 0; j < phase.getChildren().size(); j++) {
							Element openDir = (Element)phase.getChildren().get(j);
							OpenDirectionInCurrentPhaseHBoxCell openDirectionInCurrentPhaseHBoxCell = new OpenDirectionInCurrentPhaseHBoxCell();
							if(openDir.getAttributeValue("blinking").equals("true")) {
								openDirectionInCurrentPhaseHBoxCell.getCheckBox().setSelected(true);
							}else
								openDirectionInCurrentPhaseHBoxCell.getCheckBox().setSelected(false);
							openDirectionInCurrentPhaseHBoxCell.getComboBox().setValue(openDir.getText());
							openDirectionsList.add(openDirectionInCurrentPhaseHBoxCell);
						}
						iRoadModel.getModel().getRoadPhaseModel().getRoadPhaseList().add(roadPhase);
						mapOpenDirectionInPhase.put(phase.getAttributeValue("id"), openDirectionsList);
					}
					iRoadModel.getModel().getRoadPhaseModel().setMapOpenDirectionInPhase(mapOpenDirectionInPhase);
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					// detector
					Element detectorsElement = (Element)rootNode.getChildren().get(4);
					List<Detector> detectorsList = new ArrayList<>();
					System.out.println("Detectors children tag size: " + detectorsElement.getChildren().size());
					for(int i = 0; i < detectorsElement.getChildren().size(); i++) {
						Element detectorElement = (Element)detectorsElement.getChildren().get(i);
						String type = detectorElement.getAttributeValue("type");
						if(type.equals("Камера") || type.equals("Радар")) {
							Detector detector = new Detector();
							detector.setNumberDetector(detectorElement.getAttributeValue("id"));
							detector.setTypeDetector(detectorElement.getAttributeValue("type"));
							detector.setRootID(detectorElement.getChildText("root_id"));
							detector.setModelDetector(detectorElement.getChildText("model"));
							detector.setLocationDetector(detectorElement.getChildText("location"));
							detector.setFaultTimeoutDetector(detectorElement.getChildText("fault_timeout"));
							detector.setConnectionType(detectorElement.getChildText("connection_type"));
							detector.setIPDetector(detectorElement.getChildText("ip"));
							detector.setPort(detectorElement.getChildText("port"));
							detector.setPortXML(detectorElement.getChildText("port_xml"));
							detector.setPortHTTP(detectorElement.getChildText("port_HTTP"));
							detector.setPeriodInterrogation(detectorElement.getChildText("period_interrogation"));
							Element zoneElement = (Element)detectorElement.getChild("zone");
							if(zoneElement.getAttributeValue("state").equals("true")) {
								detector.setTypeZone(zoneElement.getAttributeValue("type"));
								detector.setPeriodSaving(zoneElement.getText());
							}else {
								detector.setTypeZone(zoneElement.getAttributeValue("type"));
							}
							detectorsList.add(detector);
						}else {
							Detector detector = new Detector();
							detector.setNumberDetector(detectorElement.getAttributeValue("id"));
							detector.setTypeDetector(detectorElement.getAttributeValue("type"));
							detector.setRootID(detectorElement.getChildText("root_id"));
							detector.setModelDetector(detectorElement.getChildText("model"));
							detector.setLocationDetector(detectorElement.getChildText("location"));
							detector.setFaultTimeoutDetector(detectorElement.getChildText("fault_timeout"));
							detector.setConnectionType(detectorElement.getChildText("connection_type"));
							detector.setSpi(detectorElement.getChildText("spi_channel"));
							detector.setResponse(detectorElement.getChildText("delay"));
							detectorsList.add(detector);
						}
					}
					
					// collect map of detectors
					Map<RoadDetectorHBoxCell, List<Detector>> mapOfMultiZonesDetector = new LinkedHashMap<>();
					Map<String, List<Detector>> mapOfID = new LinkedHashMap<>();
					for(Detector detector : detectorsList) {
						String rootID = detector.getRootID();
						if(mapOfID.containsKey(rootID)) {
							mapOfID.get(rootID).add(detector);
						}else {
							List<Detector> newList = new ArrayList<>();
							newList.add(detector);
							mapOfID.put(rootID, newList);
						}
					}
					for(Map.Entry<String, List<Detector>> entry : mapOfID.entrySet()) {
						RoadDetectorHBoxCell roadDetectorHBoxCell = new RoadDetectorHBoxCell(entry.getKey());
						roadDetectorHBoxCell.getChoiceBoxTypeOfDetector().setValue(entry.getValue().get(0).getTypeDetector());
						mapOfMultiZonesDetector.put(roadDetectorHBoxCell, entry.getValue());
					}
					iRoadModel.getModel().getRoadDetectorModel().setMapOfMultiZonesDetector(mapOfMultiZonesDetector);
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					// programs
					Element programsElement = (Element)rootNode.getChildren().get(5);
					Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram = new LinkedHashMap<>();
					Map<String, List<SpeedSign>> mapOfProgramSpeedSign = new LinkedHashMap<>();
					Map<RoadProgram, List<SwitchPhase>> mapOfSwichPhase = new LinkedHashMap<>();
					List<RoadProgram> roadProgramList = new ArrayList<>();
					for(int i = 0; i < programsElement.getChildren().size(); i++) {
						Element programElement = (Element)programsElement.getChildren().get(i);
						RoadProgram roadProgram = new RoadProgram();
						roadProgram.setRoadProgram_number(programElement.getAttributeValue("id"));
						roadProgram.setRoadProgram_programMode(new ProgramMode(programElement.getAttributeValue("type")));
						roadProgram.setRoadProgram_backupProgram(new BackupProgram(programElement.getAttributeValue("backup_program")));
						roadProgramList.add(roadProgram);
						
						List<PhaseInProgram> phaseInProgramsList = new ArrayList<>();
						List<SpeedSign> speedSignsList = new ArrayList<>();
						List<SwitchPhase> swichPhasesList = new ArrayList<>();
						for(int j = 0; j < programElement.getChildren().size(); j++) {
							Element childProgramElement = (Element)programElement.getChildren().get(j);
							if(childProgramElement.getName().equals("phase")) {
								PhaseInProgram phaseInProgram = new PhaseInProgram();
								phaseInProgram.setPhaseInProgramNumber(new PhaseNumber(childProgramElement.getAttributeValue("id")));
								phaseInProgram.setDurationPhaseInProgram(childProgramElement.getText());
								phaseInProgramsList.add(phaseInProgram);
							}
							if(childProgramElement.getName().equals("speed_sign")) {
								SpeedSign speedSign = new SpeedSign();
								speedSign.setNumberSpeedSign(childProgramElement.getAttributeValue("id"));
								speedSign.setRecomendSpeed(childProgramElement.getAttributeValue("value"));
								speedSignsList.add(speedSign);
							}
							if(childProgramElement.getName().equals("switch_phase")) {
								SwitchPhase swichPhase = new SwitchPhase();
								swichPhase.setPhase(new PhaseNumber(childProgramElement.getAttributeValue("id")));
								swichPhase.setToPhase1(new PhaseNumber(childProgramElement.getAttributeValue("to_phase1")));
								swichPhase.setToPhase2(new PhaseNumber(childProgramElement.getAttributeValue("to_phase2")));
								swichPhase.setMainTime(childProgramElement.getAttributeValue("main_time"));
								swichPhase.setPromtact(childProgramElement.getAttributeValue("promtact"));
								swichPhase.setDurationPhase(childProgramElement.getAttributeValue("duration"));
								swichPhase.setSwitchPhaseMode(new SwitchPhaseMode(childProgramElement.getAttributeValue("mode")));
								swichPhasesList.add(swichPhase);
							}
						}
						mapOfPhasesInProgram.put(roadProgram, phaseInProgramsList);
						mapOfProgramSpeedSign.put(roadProgram.getRoadProgram_number(), speedSignsList);
						mapOfSwichPhase.put(roadProgram, swichPhasesList);
					}
					iRoadModel.getModel().getRoadProgramsModel().setRoadProgramList(roadProgramList);
					iRoadModel.getModel().getRoadProgramsModel().setMapOfPhasesInProgram(mapOfPhasesInProgram);
					iRoadModel.getModel().getRoadProgramsModel().setMapOfProgramSpeedSign(mapOfProgramSpeedSign);
					iRoadModel.getModel().getRoadProgramsModel().setMapOfSwichPhase(mapOfSwichPhase);
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					// calendar
					Element calendarElement = (Element)rootNode.getChildren().get(6);
					Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> mapOfWeekCalendar = new LinkedHashMap<>();
					Map<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> mapOfDateCalendar = new LinkedHashMap<>();
					
					Element byWeekDayElement = (Element)calendarElement.getChild("by_weekday");
					for(int i = 0; i < byWeekDayElement.getChildren().size(); i++) {
						Element dayElement = (Element)byWeekDayElement.getChildren().get(i);
						ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(new CheckBox(), dayElement.getAttributeValue("id"));
						
						List<ScheduleProgram> scheduleProgramsList = new ArrayList<>();
						for(int j = 0; j < dayElement.getChildren().size(); j++) {
							Element programInDayElement = (Element)dayElement.getChildren().get(j);
							ScheduleProgram scheduleProgram = new ScheduleProgram();
							scheduleProgram.setNumberOfScheduleProgram(new ScheduleNumber(programInDayElement.getAttributeValue("id")));
							scheduleProgram.setTimeONOfScheduleProgram(programInDayElement.getAttributeValue("startTime"));
							scheduleProgram.setDisplacementTimeOfScheduleProgram(programInDayElement.getAttributeValue("offset"));
							scheduleProgramsList.add(scheduleProgram);
						}
						mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
					}
					
					Element byDateElement = (Element)calendarElement.getChild("by_date");
					for(int i = 0; i < byDateElement.getChildren().size(); i++) {
						Element dayElement = (Element)byDateElement.getChildren().get(i);
						
						String date = dayElement.getAttributeValue("date");
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDate localDate = LocalDate.parse(date, formatter);
						
						String day = dayElement.getAttributeValue("fromDay");
						
						ScheduleCalendarDateHBoxCell shScheduleCalendarDateHBoxCell = new ScheduleCalendarDateHBoxCell();
						shScheduleCalendarDateHBoxCell.getDatePicker().setValue(localDate);
						shScheduleCalendarDateHBoxCell.getChoiceBox().setValue(day);
						
						List<ScheduleProgram> scheduleProgramsList = new ArrayList<>();
						for(int j = 0; j < dayElement.getChildren().size(); j++) {
							Element programInDateElement = (Element)dayElement.getChildren().get(j);
							ScheduleProgram scheduleProgram = new ScheduleProgram();
							scheduleProgram.setNumberOfScheduleProgram(new ScheduleNumber(programInDateElement.getAttributeValue("id")));
							scheduleProgram.setTimeONOfScheduleProgram(programInDateElement.getAttributeValue("startTime"));
							scheduleProgram.setDisplacementTimeOfScheduleProgram(programInDateElement.getAttributeValue("offset"));
							scheduleProgramsList.add(scheduleProgram);
						}
						mapOfDateCalendar.put(shScheduleCalendarDateHBoxCell, scheduleProgramsList);
					}
					iRoadModel.getModel().getRoadProgramsModel().setMapOfWeekCalendar(mapOfWeekCalendar);
					iRoadModel.getModel().getRoadProgramsModel().setMapOfDateCalendar(mapOfDateCalendar);
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					// promtacts
					Element promtactsElement = (Element)rootNode.getChildren().get(7);
					Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfInterphaseSpecificPromtact = new LinkedHashMap<>();
					
					for(int i = 0; i < promtactsElement.getChildren().size(); i++) {
						Element promtactElement = (Element)promtactsElement.getChildren().get(i);
						InterphaseTransitionsHBoxCell interphaseTransitionsHBoxCell = new InterphaseTransitionsHBoxCell();
						interphaseTransitionsHBoxCell.getComboBoxFromPhase().setValue(promtactElement.getChildText("from_phase"));
						interphaseTransitionsHBoxCell.getComboBoxToPhase().setValue(promtactElement.getChildText("to_phase"));
						
						Element promtactDataElement = (Element)promtactElement.getChild("directions");
						Map<String, PromtactData> promDataMap = new LinkedHashMap<>();
						for(int j = 0; j < promtactDataElement.getChildren().size(); j++) {
							Element dataElement = (Element)promtactDataElement.getChildren().get(j);
							PromtactData promtactData = new PromtactData();
							
							if(dataElement.getAttributeValue("isFull").equals("true")) {
								promtactData.setFullPromtact(true);
							}else {
								promtactData.setFullPromtact(false);
							}
							
							promtactData.setRoadPromtactu_endGreenAddit(dataElement.getAttributeValue("KZD"));
							promtactData.setRoadPromtactu_durationGreenBlink(dataElement.getAttributeValue("KZM"));
							promtactData.setRoadPromtactu_durationYellow(dataElement.getAttributeValue("KG"));
							promtactData.setRoadPromtactu_endRed(dataElement.getAttributeValue("KK"));
							promtactData.setRoadPromtactu_durationRedYellow(dataElement.getAttributeValue("KKG"));
							promDataMap.put(dataElement.getAttributeValue("id"), promtactData);
						}
						mapOfInterphaseSpecificPromtact.put(interphaseTransitionsHBoxCell, promDataMap);
					}
					iRoadModel.getModel().getRoadPromtactuModel().setMapOfInterphaseSpecificPromtact(mapOfInterphaseSpecificPromtact);
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					// conflict
					Element conflictsElement = (Element)rootNode.getChildren().get(8);
					Map<String, List<ConflictWithDirection>> mapOfConflict = new LinkedHashMap<>();
					
					for(int i = 0; i < conflictsElement.getChildren().size(); i++) {
						Element conflict_forElement = (Element)conflictsElement.getChildren().get(i);
						
						String dirNumber = conflict_forElement.getAttributeValue("id");
						
						List<ConflictWithDirection> conflictWithList = new ArrayList<>();
						for(int j = 0; j < conflict_forElement.getChildren().size(); j++) {
							Element conflictWith = (Element)conflict_forElement.getChildren().get(j);
							
							ConflictWithDirection conflictWithDirection = new ConflictWithDirection();
							conflictWithDirection.setConflictWithDirection(conflictWith.getValue());
							
							conflictWithList.add(conflictWithDirection);
							
						}
						
						mapOfConflict.put(dirNumber, conflictWithList);
						
					}
					
					iRoadModel.getModel().getRoadConflictsModel().setMapOfConflict(mapOfConflict);
					//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					
				}catch (IOException io) {
					System.out.println(io.getMessage());
				} catch (JDOMException jdomex) {
					System.out.println(jdomex.getMessage());
				}
				selectObject = true;
				Stage stage = (Stage) listViewAllObjects.getScene().getWindow();
		        stage.close();
			}
		});
	}
	
}
