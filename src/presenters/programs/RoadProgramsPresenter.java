package presenters.programs;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import presenters.object.TypeKDK;
import roadModel.IRoadModel;

/**
 * Created by Vitaly on 06.12.2016.
 */
public class RoadProgramsPresenter {

	@FXML
	private TableView<RoadProgram> tableViewAllProgram; // Table View of all program
	@FXML
	private TableView<ScheduleProgram> tableViewSchedulePrograms; // Table View timetable specified controller programs
	@FXML
	private TableView<PhaseInProgram> tableViewPhaseInProgram; // Table View phase in program
	@FXML
	private TableColumn<RoadProgram, ProgramMode> tableColumnProgramMode;
	@FXML
	private TableColumn<RoadProgram, BackupProgram> tableColumnBackupProgram;
	@FXML
	private TableColumn<RoadProgram, String> tableColumnNumber;
	@FXML
	private TableColumn<ScheduleProgram, ScheduleNumber> tableColumnScheduleProgramsNumber, tableColumnScheduleProgramsNumber1;
	@FXML
	private TableColumn<ScheduleProgram, String> tableColumnScheduleProgramsTimeON, tableColumnScheduleProgramsDisplacement, tableColumnScheduleProgramsTimeON1,
			tableColumnScheduleProgramsDisplacement1;
	@FXML
	private TableColumn<PhaseInProgram, PhaseNumber> tableColumnPhase;
	@FXML
	private TableColumn<PhaseInProgram, String> tableColumnPhaseDuration;
	@FXML
	private ChoiceBox<String> chCopyDay, cbScheduleDate;
	@FXML
	private Label labelProgram, labelListAllProgram, labelProgramN, labelSchedulingControllerPrograms, labelCopySchedule1, labelCopySchedule2, labelProgramNumber;
	@FXML
	private Button buttonCreateProgram, buttonDeleteProgram, buttonSpeedSign, buttonCreatePhaseInProgram, buttonDeletePhaseInProgram,
			buttonCreateScheduleProgram, buttonDeleteScheduleProgram, buttonCopyDay, buttonCopyAllDay,
			buttonCreateScheduleProgramByDate, buttonCreateDate, buttonSwichPhase;
	@FXML
	private ListView<ScheduleCalendarWeekDayHBoxCell> listViewDayOfWeek;
	@FXML
	private ListView<ScheduleCalendarDateHBoxCell> listViewDate;

	@FXML
	//private static ResourceBundle bundleGUI, bundleAlert;
	//private static Locale localeGUI, localeAlert;
	//static String langXML = null;

	private IRoadModel iRoadModel;

	RoadProgram roadProgram;
	ScheduleProgram scheduleProgram;
	PhaseInProgram phaseInProgram;

	ObservableList<String> observableListCopyScheduleOfCurrentDay = FXCollections.observableArrayList();

	List<PhaseInProgram> phaseInProgramList;
	List<ScheduleProgram> scheduleProgramsList;
	List<Integer> sortedList = new ArrayList<>();
	List<PhaseInProgram> sortedPhaseInProgramList;
	List<String> dayOfWeeksList;
	List<SpeedSign> speedSignsList;
	List<SwitchPhase> swichPhasesList;

	List<ScheduleCalendarWeekDayHBoxCell> scheduleCalendarWeekDayHBoxCellList = new ArrayList<>();
	ObservableList<ScheduleCalendarWeekDayHBoxCell> scheduleCalendarWeekDayHBoxCellsObservableList;
	Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> mapOfWeekCalendar;

	List<ScheduleCalendarDateHBoxCell> scheduleCalendarDateHBoxCellsList = new ArrayList<>();
	ObservableList<ScheduleCalendarDateHBoxCell> scheduleCalendarDateHBoxCellsObservableList;
	Map<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> mapOfDateCalendar;

	Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram;
	Map<RoadProgram, List<ScheduleProgram>> mapOfScheduleProgram;
	Map<RoadProgram, List<PhaseInProgram>> sortMapOfPhasesInProgram = new HashMap<>();
	Map<String, List<SpeedSign>> mapOfSpeedSign;
	Map<RoadProgram, List<SwitchPhase>> mapOfSwichPhase;

	public void show(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;

		if (iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK().equals("")) {
			buttonCreateProgram.setDisable(true);
		}

		tableViewAllProgram.getItems().clear();

		tableViewAllProgram.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getRoadProgramList()));
		tableViewAllProgram.getSelectionModel().selectFirst();
		
		if(!tableViewAllProgram.getItems().isEmpty()) {
			buttonCreatePhaseInProgram.setDisable(false);
		}

		mapOfPhasesInProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
		mapOfScheduleProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfScheduleProgram();
		
		for(Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {		// remove empty phase in program
			List<PhaseInProgram> phaseInPrograms = entry.getValue();
			
			phaseInPrograms.removeIf(phase -> phase.getPhaseInProgramNumber() == null || phase.getDurationPhaseInProgram() == null);
			
		}

		RoadProgram roadProgram = tableViewAllProgram.getSelectionModel().getSelectedItem();
		if(roadProgram != null) {
			if(!roadProgram.getRoadProgram_programMode().getMode().equals("Замена фаз")) {
				tableViewPhaseInProgram.setVisible(true);
				buttonCreatePhaseInProgram.setVisible(true);
				buttonDeletePhaseInProgram.setVisible(true);
				buttonSwichPhase.setVisible(false);
				if (!mapOfPhasesInProgram.isEmpty()) {
					List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(roadProgram);
					if(phaseInProgramsList.size() != 0) {
						tableViewPhaseInProgram.setItems(FXCollections.observableArrayList(phaseInProgramsList));
					}else {
						tableViewPhaseInProgram.getItems().clear();
					}
				}
			}else {
				tableViewPhaseInProgram.setVisible(false);
				buttonCreatePhaseInProgram.setVisible(false);
				buttonDeletePhaseInProgram.setVisible(false);
				buttonSwichPhase.setVisible(true);
			}
		}
			

		mapOfWeekCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();

		if (!mapOfWeekCalendar.isEmpty()) {
			buttonCreateScheduleProgram.setDisable(false);
			for (Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekCalendar.entrySet()) {
				ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = entry.getKey();
				List<ScheduleProgram> scheduleProgramsList = entry.getValue();
				if (!scheduleProgramsList.isEmpty()) {
					scheduleCalendarHBoxCell.getCheckBox().setSelected(true);
				}
				scheduleCalendarWeekDayHBoxCellList.add(scheduleCalendarHBoxCell);
				scheduleCalendarWeekDayHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarWeekDayHBoxCellList);
				listViewDayOfWeek.setItems(scheduleCalendarWeekDayHBoxCellsObservableList);
			}
			ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = mapOfWeekCalendar.keySet().stream().findFirst().get();
			tableViewSchedulePrograms.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar().get(scheduleCalendarHBoxCell)));
			listViewDayOfWeek.getSelectionModel().selectFirst();
		} else {
			createScheduleCalendar();
		}
		tableViewSchedulePrograms.getSelectionModel().selectFirst();

		mapOfDateCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar();
		if (!mapOfDateCalendar.isEmpty()) {
			buttonCreateScheduleProgramByDate.setDisable(false);
			for (Map.Entry<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> entry : mapOfDateCalendar.entrySet()) {
				ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell = entry.getKey();
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
				
				String formattedValue = (scheduleCalendarDateHBoxCell.getDatePicker().getValue()).format(formatter);
				
				scheduleCalendarDateHBoxCell.getTextFild().setText(formattedValue);
				scheduleCalendarDateHBoxCell.getDatePicker().valueProperty().addListener((observable, oldValue, newValue) -> {
					if(newValue != null) {
						System.out.println(newValue);
						
						DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM");
						
						String formattedValue1 = (scheduleCalendarDateHBoxCell.getDatePicker().getValue()).format(formatter1);
						
						scheduleCalendarDateHBoxCell.getTextFild().setText(formattedValue1);
						
					}
				});
				
				scheduleCalendarDateHBoxCellsList.add(scheduleCalendarDateHBoxCell);
				scheduleCalendarDateHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarDateHBoxCellsList);
				listViewDate.setItems(scheduleCalendarDateHBoxCellsObservableList);
			}
			ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell = mapOfDateCalendar.keySet().stream().findFirst().get();
			//tableViewScheduleProgramsByDate.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar().get(scheduleCalendarDateHBoxCell)));
			listViewDate.getSelectionModel().selectFirst();
		}
		//tableViewScheduleProgramsByDate.getSelectionModel().selectFirst();
		if(roadProgram != null) {
			labelProgramNumber.setText(roadProgram.getRoadProgram_number());
		}
		
		
	}

	public void save(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;
		
		mapOfPhasesInProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
		
		for(Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {		// remove empty phase in program
			List<PhaseInProgram> phaseInPrograms = entry.getValue();
			
			phaseInPrograms.removeIf(phase -> phase.getPhaseInProgramNumber() == null || phase.getDurationPhaseInProgram() == null);
			
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

		labelProgram.setText(bundleGUI.getString("labelProgramm"));
		labelListAllProgram.setText(bundleGUI.getString("labelListAllProgramm"));
		labelProgramN.setText(bundleGUI.getString("labelProgrammN"));
		labelSchedulingControllerPrograms.setText(bundleGUI.getString("labelSchedulingControllerPrograms"));
		labelCopySchedule1.setText(bundleGUI.getString("labelCopySchedule"));
		labelCopySchedule2.setText(bundleGUI.getString("labelCopySchedule2"));
		buttonCreateProgram.setText(bundleGUI.getString("buttonCreate"));
		buttonDeleteProgram.setText(bundleGUI.getString("buttonDelete"));
		buttonSpeedSign.setText(bundleGUI.getString("buttonSpeedSign"));
		buttonCreatePhaseInProgram.setText(bundleGUI.getString("buttonCreate"));
		buttonDeletePhaseInProgram.setText(bundleGUI.getString("buttonDelete"));
		buttonCreateScheduleProgram.setText(bundleGUI.getString("buttonCreate"));
		buttonDeleteScheduleProgram.setText(bundleGUI.getString("buttonDelete"));
		buttonCopyDay.setText(bundleGUI.getString("buttonCopy"));
		buttonCopyAllDay.setText(bundleGUI.getString("buttonCopyAllDay"));
		tableColumnNumber.setText(bundleGUI.getString("tableColumnNumber"));
		tableColumnProgramMode.setText(bundleGUI.getString("tableColumnProgramMode"));
		tableColumnBackupProgram.setText(bundleGUI.getString("tableColumnBackupProgram"));
		tableColumnPhase.setText(bundleGUI.getString("tableColumnPhase"));
		tableColumnPhaseDuration.setText(bundleGUI.getString("tableColumnPhaseDuration"));
		tableColumnScheduleProgramsNumber.setText(bundleGUI.getString("tableColumnScheduleProgramsNumber"));
		tableColumnScheduleProgramsTimeON.setText(bundleGUI.getString("tableColumnScheduleProgramsTimeON"));
		tableColumnScheduleProgramsDisplacement.setText(bundleGUI.getString("tableColumnScheduleProgramsDisplacement"));
		observableListCopyScheduleOfCurrentDay.addAll(bundleGUI.getString("monday"), bundleGUI.getString("tuesday"), bundleGUI.getString("wednesday"), bundleGUI.getString("thursday"), bundleGUI.getString("friday"), bundleGUI.getString("saturday"),
				bundleGUI.getString("sunday"));
	}

	private static void alertLang(String lang) {
		localeAlert = new Locale(lang);
		bundleAlert = ResourceBundle.getBundle("localeAlert_lang", localeAlert);
	}*/
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void createScheduleCalendar() {
		mapOfWeekCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				CheckBox checkBox = new CheckBox();
				ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Понедельник");
				scheduleCalendarWeekDayHBoxCellList.add(scheduleCalendarHBoxCell);
				scheduleProgramsList = new ArrayList<>();
				mapOfWeekCalendar.put(scheduleCalendarHBoxCell, scheduleProgramsList);
			}
			scheduleCalendarWeekDayHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarWeekDayHBoxCellList);
			listViewDayOfWeek.setItems(scheduleCalendarWeekDayHBoxCellsObservableList);
			if (i == 1) {
				CheckBox checkBox = new CheckBox();
				ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Вторник");
				scheduleCalendarWeekDayHBoxCellList.add(scheduleCalendarHBoxCell);
				scheduleProgramsList = new ArrayList<>();
				mapOfWeekCalendar.put(scheduleCalendarHBoxCell, scheduleProgramsList);
			}
			scheduleCalendarWeekDayHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarWeekDayHBoxCellList);
			listViewDayOfWeek.setItems(scheduleCalendarWeekDayHBoxCellsObservableList);
			if (i == 2) {
				CheckBox checkBox = new CheckBox();
				ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Среда");
				scheduleCalendarWeekDayHBoxCellList.add(scheduleCalendarHBoxCell);
				scheduleProgramsList = new ArrayList<>();
				mapOfWeekCalendar.put(scheduleCalendarHBoxCell, scheduleProgramsList);
			}
			scheduleCalendarWeekDayHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarWeekDayHBoxCellList);
			listViewDayOfWeek.setItems(scheduleCalendarWeekDayHBoxCellsObservableList);
			if (i == 3) {
				CheckBox checkBox = new CheckBox();
				ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Четверг");
				scheduleCalendarWeekDayHBoxCellList.add(scheduleCalendarHBoxCell);
				scheduleProgramsList = new ArrayList<>();
				mapOfWeekCalendar.put(scheduleCalendarHBoxCell, scheduleProgramsList);
			}
			scheduleCalendarWeekDayHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarWeekDayHBoxCellList);
			listViewDayOfWeek.setItems(scheduleCalendarWeekDayHBoxCellsObservableList);
			if (i == 4) {
				CheckBox checkBox = new CheckBox();
				ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Пятница");
				scheduleCalendarWeekDayHBoxCellList.add(scheduleCalendarHBoxCell);
				scheduleProgramsList = new ArrayList<>();
				mapOfWeekCalendar.put(scheduleCalendarHBoxCell, scheduleProgramsList);
			}
			scheduleCalendarWeekDayHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarWeekDayHBoxCellList);
			listViewDayOfWeek.setItems(scheduleCalendarWeekDayHBoxCellsObservableList);
			if (i == 5) {
				CheckBox checkBox = new CheckBox();
				ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Суббота");
				scheduleCalendarWeekDayHBoxCellList.add(scheduleCalendarHBoxCell);
				scheduleProgramsList = new ArrayList<>();
				mapOfWeekCalendar.put(scheduleCalendarHBoxCell, scheduleProgramsList);
			}
			scheduleCalendarWeekDayHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarWeekDayHBoxCellList);
			listViewDayOfWeek.setItems(scheduleCalendarWeekDayHBoxCellsObservableList);
			if (i == 6) {
				CheckBox checkBox = new CheckBox();
				ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Воскресенье");
				scheduleCalendarWeekDayHBoxCellList.add(scheduleCalendarHBoxCell);
				scheduleProgramsList = new ArrayList<>();
				mapOfWeekCalendar.put(scheduleCalendarHBoxCell, scheduleProgramsList);
			}
			scheduleCalendarWeekDayHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarWeekDayHBoxCellList);
			listViewDayOfWeek.setItems(scheduleCalendarWeekDayHBoxCellsObservableList);
		}

		listViewDayOfWeek.getSelectionModel().selectFirst();

	}

	public void createNewProgram() {
		System.out.println("Press create program");
		tableViewPhaseInProgram.getItems().clear();

		mapOfPhasesInProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
		phaseInProgramList = new ArrayList<>();

		mapOfSpeedSign = iRoadModel.getModel().getRoadProgramsModel().getMapOfProgramSpeedSign();
		speedSignsList = new ArrayList<>();
		
		mapOfSwichPhase = iRoadModel.getModel().getRoadProgramsModel().getMapOfSwichPhase();
		swichPhasesList = new ArrayList<SwitchPhase>();

		mapOfScheduleProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfScheduleProgram();

		int number;
		number = tableViewAllProgram.getItems().size() + 1;

		String typeKDK = iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK();

		List<TypeKDK> typeKDKsList = iRoadModel.getModel().getRoadObjectModel().getKdkTypeList();
		for (TypeKDK existedKDKType : typeKDKsList) {
			if (existedKDKType.getName_KDK().equals(typeKDK)) {
				String max_programs = existedKDKType.getPrograms();

				int max_value = Integer.parseInt(max_programs);

				if (number <= max_value) {
					String createProgramNumber = Integer.toString(number);

					UUID uuid = UUID.randomUUID();
					String id = uuid.toString();

					roadProgram = new RoadProgram();
					roadProgram.setRoadProgram_number(createProgramNumber);
					roadProgram.setIdProgram(id);
					roadProgram.setRoadProgram_programMode(new ProgramMode("Циклическая"));
					roadProgram.setRoadProgram_backupProgram(new BackupProgram("Отсутствует"));

					iRoadModel.getModel().getRoadProgramsModel().getRoadProgramList().add(roadProgram);

					mapOfPhasesInProgram.put(roadProgram, phaseInProgramList);
					mapOfSpeedSign.put(createProgramNumber, speedSignsList);

					System.out.println("Map of phases in program " + mapOfPhasesInProgram);

					tableViewAllProgram.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getRoadProgramList()));
					tableViewAllProgram.getSelectionModel().select(tableViewAllProgram.getItems().size() - 1);

					labelProgramNumber.setText(createProgramNumber);

					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);
					
					buttonCreateScheduleProgram.setDisable(false);
					buttonCreatePhaseInProgram.setDisable(false);
					buttonCreateDate.setDisable(false);
					
					tableViewAllProgram.getSelectionModel().getSelectedItem().roadProgramProgramModeProperty().addListener((observable, oldValue, newValue) -> {
						if(newValue.getMode().equals("Замена фаз")) {
							System.out.println("Выбрана замена фаз");
							
							buttonSwichPhase.setVisible(true);
							tableViewPhaseInProgram.setVisible(false);
							buttonCreatePhaseInProgram.setVisible(false);
							buttonDeletePhaseInProgram.setVisible(false);							
						
							mapOfSwichPhase.put(roadProgram, swichPhasesList);
							mapOfPhasesInProgram.remove(roadProgram);
							
						}else {
							
							buttonSwichPhase.setVisible(false);
							tableViewPhaseInProgram.setVisible(true);
							buttonCreatePhaseInProgram.setVisible(true);
							buttonDeletePhaseInProgram.setVisible(true);
							
						}
					});
					
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Ошибка");
					alert.setContentText("Тип контроллера не позволяет создать больше " + max_programs + " программ");
					
					Stage stage = new Stage();
					stage = (Stage)alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
					
					alert.show();
				}
			}
		}
	}

	public void deleteProgram() {
		//alertLang(langXML);
		System.out.println("============== Press delete program ==============");
		mapOfWeekCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();
		mapOfDateCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar();
		mapOfPhasesInProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
		mapOfSwichPhase = iRoadModel.getModel().getRoadProgramsModel().getMapOfSwichPhase();
		
		RoadProgram roadProgram = tableViewAllProgram.getSelectionModel().getSelectedItem();
		int selectedProgram = tableViewAllProgram.getSelectionModel().getSelectedIndex();
		String programNumber = tableViewAllProgram.getSelectionModel().getSelectedItem().getRoadProgram_number();
		if (selectedProgram >= 0) {
			tableViewAllProgram.getItems().remove(selectedProgram);
			iRoadModel.getModel().getRoadProgramsModel().getRoadProgramList().remove(selectedProgram);
			tableViewAllProgram.getSelectionModel().selectFirst();
			labelProgramNumber.setText(tableViewAllProgram.getSelectionModel().getSelectedItem().getRoadProgram_number());
			
			mapOfPhasesInProgram.remove(roadProgram);	// remove phase in program 
			if (!mapOfPhasesInProgram.isEmpty()) {
				RoadProgram selectedRoadProgram = tableViewAllProgram.getSelectionModel().getSelectedItem();
				List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(selectedRoadProgram);
				if(phaseInProgramsList.size() != 0) {
					tableViewPhaseInProgram.setItems(FXCollections.observableArrayList(phaseInProgramsList));
				}else {
					tableViewPhaseInProgram.getItems().clear();
				}
			}
			
			
			if(!mapOfWeekCalendar.isEmpty()) {		// remove program from schedule calendar
				for(Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekCalendar.entrySet()) {
					List<ScheduleProgram> scheduleProgramList = entry.getValue();
					if(!scheduleProgramList.isEmpty()) {
						List<ScheduleProgram> listAfterRemove = new ArrayList<>();
						for(ScheduleProgram scheduleProgram : scheduleProgramList) {
							String number = scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber();
							if(!number.equals(programNumber)) {
								listAfterRemove.add(scheduleProgram);
							}
						}
						mapOfWeekCalendar.put(entry.getKey(), listAfterRemove);						
					}
				}
			}
			listViewDayOfWeek.getItems().clear();
			scheduleCalendarWeekDayHBoxCellList.clear();
			if (!mapOfWeekCalendar.isEmpty()) {
				buttonCreateScheduleProgram.setDisable(false);
				for (Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekCalendar.entrySet()) {
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = entry.getKey();
					List<ScheduleProgram> scheduleProgramsList = entry.getValue();
					if (!scheduleProgramsList.isEmpty()) {
						scheduleCalendarHBoxCell.getCheckBox().setSelected(true);
					}
					scheduleCalendarWeekDayHBoxCellList.add(scheduleCalendarHBoxCell);
					scheduleCalendarWeekDayHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarWeekDayHBoxCellList);
					listViewDayOfWeek.setItems(scheduleCalendarWeekDayHBoxCellsObservableList);
				}
				ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = mapOfWeekCalendar.keySet().stream().findFirst().get();
				tableViewSchedulePrograms.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar().get(scheduleCalendarHBoxCell)));
				listViewDayOfWeek.getSelectionModel().selectFirst();
			}
			
			if(!mapOfDateCalendar.isEmpty()) {		// remove program from schedule calendar(by date)
				for(Map.Entry<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> entry : mapOfDateCalendar.entrySet()) {
					List<ScheduleProgram> scheduleProgramList = entry.getValue();
					if(!scheduleProgramList.isEmpty()) {
						List<ScheduleProgram> listAfterRemove = new ArrayList<>();
						for(ScheduleProgram scheduleProgram : scheduleProgramList) {
							String number = scheduleProgram.getNumberOfScheduleProgram().getScheduleNumber();
							if(!number.equals(programNumber)) {
								listAfterRemove.add(scheduleProgram);
							}
						}
						mapOfDateCalendar.put(entry.getKey(), listAfterRemove);						
					}
				}
			}
			listViewDate.getItems().clear();
			scheduleCalendarDateHBoxCellsList.clear();
			mapOfDateCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar();
			if (!mapOfDateCalendar.isEmpty()) {
				buttonCreateScheduleProgramByDate.setDisable(false);
				for (Map.Entry<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> entry : mapOfDateCalendar.entrySet()) {
					ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell = entry.getKey();
					scheduleCalendarDateHBoxCellsList.add(scheduleCalendarDateHBoxCell);
					scheduleCalendarDateHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarDateHBoxCellsList);
					listViewDate.setItems(scheduleCalendarDateHBoxCellsObservableList);
				}
				ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell = mapOfDateCalendar.keySet().stream().findFirst().get();
				//tableViewScheduleProgramsByDate.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar().get(scheduleCalendarDateHBoxCell)));
				listViewDate.getSelectionModel().selectFirst();
			}
			
			
		}
		if (tableViewAllProgram.getItems().isEmpty()) {
			buttonCreateDate.setDisable(true);
			buttonCreatePhaseInProgram.setDisable(true);
			buttonCreateScheduleProgram.setDisable(true);
		}

	}

	public void createScheduleProgram() {
		System.out.println("Press create schedule program");
		mapOfWeekCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();
		ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = listViewDayOfWeek.getSelectionModel().getSelectedItem();

		scheduleProgramsList = mapOfWeekCalendar.get(scheduleCalendarHBoxCell);

		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();

		if (!scheduleProgramsList.isEmpty()) {
			scheduleProgram = new ScheduleProgram();
			scheduleProgram.setScheduleProgramId(id);
			scheduleProgram.setDisplacementTimeOfScheduleProgram("0");
			scheduleProgram.setTimeONOfScheduleProgram("00:00");
			scheduleProgramsList.add(scheduleProgram);
			tableViewSchedulePrograms.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar().get(scheduleCalendarHBoxCell)));
			tableViewSchedulePrograms.getSelectionModel().select(tableViewSchedulePrograms.getItems().size() - 1);
		} else {
			scheduleProgram = new ScheduleProgram();
			scheduleProgram.setScheduleProgramId(id);
			scheduleProgram.setDisplacementTimeOfScheduleProgram("0");
			scheduleProgram.setTimeONOfScheduleProgram("00:00");
			scheduleProgramsList.add(scheduleProgram);
			mapOfWeekCalendar.put(scheduleCalendarHBoxCell, scheduleProgramsList);
			scheduleCalendarHBoxCell.getCheckBox().setSelected(true);
			tableViewSchedulePrograms.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar().get(scheduleCalendarHBoxCell)));
			tableViewSchedulePrograms.getSelectionModel().select(tableViewSchedulePrograms.getItems().size() - 1);
		}

	}

	public void deleteScheduleProgram() {
		//alertLang(langXML);
		System.out.println("Press delete schedule program");

		mapOfWeekCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();
		ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = listViewDayOfWeek.getSelectionModel().getSelectedItem();
		List<ScheduleProgram> scheduleProgramsList = mapOfWeekCalendar.get(scheduleCalendarHBoxCell);

		int selectScheduleProgram = tableViewSchedulePrograms.getSelectionModel().getSelectedIndex();
		if (selectScheduleProgram >= 0) {
			scheduleProgramsList.remove(selectScheduleProgram);
			tableViewSchedulePrograms.getItems().remove(selectScheduleProgram);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Выберите стоку для удаления");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.showAndWait();
		}
		
		if(scheduleProgramsList.isEmpty()) {
			scheduleCalendarHBoxCell.getCheckBox().setSelected(false);
		}
		
	}

	public void createPhaseInProgram() {
		System.out.println("Press create phase in program");
		
		mapOfPhasesInProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();

		RoadProgram roadProgram = tableViewAllProgram.getSelectionModel().getSelectedItem();
		String type = roadProgram.getRoadProgram_programMode().getMode();
		
		if(!type.equals("Желтое мигание") && !type.equals("Отключение светофора")) {
		
			phaseInProgramList = mapOfPhasesInProgram.get(tableViewAllProgram.getSelectionModel().getSelectedItem());
	
			int number = tableViewPhaseInProgram.getItems().size();
			String indexPhase = Integer.toString(number);
	
			phaseInProgram = new PhaseInProgram();
			phaseInProgram.setPhaseIndex(indexPhase);
			phaseInProgramList.add(phaseInProgram);
	
			tableViewPhaseInProgram.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram().get(tableViewAllProgram.getSelectionModel().getSelectedItem())));
			tableViewPhaseInProgram.getSelectionModel().select(tableViewPhaseInProgram.getItems().size() - 1);
		
		}else if(type.equals("Замена фаз")) {
			System.out.println("Тип программы - 'замена фаз'");
		}
		
		
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Информация");
			alert.setHeaderText("Этот тип программы не позволяет создать фазы");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
		}
		
		

	}

	public void deletePhaseInProgram() {
		//alertLang(langXML);
		System.out.println("Press delete phase in program");

		mapOfPhasesInProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
		RoadProgram roadProgram = tableViewAllProgram.getSelectionModel().getSelectedItem();
		List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(roadProgram);

		int selectPhaseInProgram = tableViewPhaseInProgram.getSelectionModel().getSelectedIndex();
		if (selectPhaseInProgram >= 0) {
			phaseInProgramsList.remove(selectPhaseInProgram);
			tableViewPhaseInProgram.getItems().remove(selectPhaseInProgram);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Выберите стоку для удаления");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.showAndWait();
		}
	}

	public void selectPhaseInProgram() {

		/*
		 * PhaseInProgram phaseInProgram =
		 * tableViewPhaseInProgram.getSelectionModel().getSelectedItem();
		 * if(phaseInProgram.getDurationPhaseInProgram() != null) { int durationPhase =
		 * Integer.parseInt(phaseInProgram.getDurationPhaseInProgram());
		 * System.out.println("Duration phase " + durationPhase); }
		 */

	}

	public void createDate() {
		System.out.println("=========== Create schedule by date ============");
		
		mapOfWeekCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();
		mapOfDateCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar();
		
		ScheduleCalendarDateHBoxCell selectedCalendarDateHBoxCell = listViewDate.getSelectionModel().getSelectedItem();
		
		if(listViewDate.getItems().isEmpty()) {
			ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell = new ScheduleCalendarDateHBoxCell();
			
			scheduleCalendarDateHBoxCell.getChoiceBox().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				if(newValue != null) {
					
					for(Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> weekEntry : mapOfWeekCalendar.entrySet()) {
						String existDay = weekEntry.getKey().getWeekDay().getText();
						
						if(newValue.equals(existDay)) {
							
							List<ScheduleProgram> schedulePrograms = weekEntry.getValue();
							
							if(!schedulePrograms.isEmpty()) {
								
								mapOfDateCalendar.put(scheduleCalendarDateHBoxCell, schedulePrograms);
								
							}else {
								Alert alert = new Alert(Alert.AlertType.WARNING);
								alert.setTitle("Внимание");
								alert.setHeaderText("Для данного дня не создано расписание программ");
								
								Stage stage = new Stage();
								stage = (Stage)alert.getDialogPane().getScene().getWindow();
								stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
								
								alert.showAndWait();
							}
							
						}
						
					}
				}
			});
			
			scheduleCalendarDateHBoxCell.getDatePicker().valueProperty().addListener((observable, oldValue, newValue) -> {
				if(newValue != null) {
					System.out.println(newValue);
					
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
					
					String formattedValue = (scheduleCalendarDateHBoxCell.getDatePicker().getValue()).format(formatter);
					
					scheduleCalendarDateHBoxCell.getTextFild().setText(formattedValue);
					
				}
			});
			
			scheduleCalendarDateHBoxCellsList.add(scheduleCalendarDateHBoxCell);
			scheduleCalendarDateHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarDateHBoxCellsList);
			listViewDate.setItems(scheduleCalendarDateHBoxCellsObservableList);
			listViewDate.getSelectionModel().select(listViewDate.getItems().size() - 1);
			
			for (ScheduleCalendarDateHBoxCell existed : listViewDate.getItems()) {
				if (existed.equals(scheduleCalendarDateHBoxCell)) {
					scheduleCalendarDateHBoxCell.getDatePicker().setDisable(false);
				} else {
					existed.getDatePicker().setDisable(true);
				}
			}
		}else {
		
			if(selectedCalendarDateHBoxCell.getDatePicker().getValue() != null && selectedCalendarDateHBoxCell.getChoiceBox().getValue() != null) {
				
				ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell = new ScheduleCalendarDateHBoxCell();
				
				scheduleCalendarDateHBoxCell.getChoiceBox().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
					if(newValue != null) {
						
						for(Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> weekEntry : mapOfWeekCalendar.entrySet()) {
							String existDay = weekEntry.getKey().getWeekDay().getText();
							
							if(newValue.equals(existDay)) {
								
								List<ScheduleProgram> schedulePrograms = weekEntry.getValue();
								
								if(!schedulePrograms.isEmpty()) {
									
									mapOfDateCalendar.put(scheduleCalendarDateHBoxCell, schedulePrograms);
									
								}else {
									Alert alert = new Alert(Alert.AlertType.WARNING);
									alert.setTitle("Внимание");
									alert.setHeaderText("Для данного дня не создано расписание программ");
									
									Stage stage = new Stage();
									stage = (Stage)alert.getDialogPane().getScene().getWindow();
									stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
									
									alert.showAndWait();
								}
								
							}
							
						}
					}
				});
				
				scheduleCalendarDateHBoxCell.getDatePicker().valueProperty().addListener((observable, oldValue, newValue) -> {
					if(newValue != null) {
						System.out.println(newValue);
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
						
						String formattedValue = (scheduleCalendarDateHBoxCell.getDatePicker().getValue()).format(formatter);
						
						scheduleCalendarDateHBoxCell.getTextFild().setText(formattedValue);
						
					}
				});
				
				scheduleCalendarDateHBoxCellsList.add(scheduleCalendarDateHBoxCell);
				scheduleCalendarDateHBoxCellsObservableList = FXCollections.observableArrayList(scheduleCalendarDateHBoxCellsList);
				listViewDate.setItems(scheduleCalendarDateHBoxCellsObservableList);
				listViewDate.getSelectionModel().select(listViewDate.getItems().size() - 1);
				
				for (ScheduleCalendarDateHBoxCell existed : listViewDate.getItems()) {
					if (existed.equals(scheduleCalendarDateHBoxCell)) {
						scheduleCalendarDateHBoxCell.getDatePicker().setDisable(false);
					} else {
						existed.getDatePicker().setDisable(true);
					}
				}
			}else {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Внимание");
				alert.setHeaderText("Заполните расписание полностью");
				
				Stage stage = new Stage();
				stage = (Stage)alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
				
				alert.showAndWait();
			}
		}

	}

	public void deleteDate() {
		mapOfDateCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar();

		int selectedDate = listViewDate.getSelectionModel().getSelectedIndex();
		if (selectedDate >= 0) {
			ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell = listViewDate.getSelectionModel().getSelectedItem();
			mapOfDateCalendar.remove(scheduleCalendarDateHBoxCell);
			scheduleCalendarDateHBoxCellsList.remove(scheduleCalendarDateHBoxCell);
			listViewDate.getItems().remove(selectedDate);
			
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Выберите стоку для удаления");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.showAndWait();
		}
	}

	/*public void createScheduleProgramDate() {
		mapOfDateCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar();

		ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell = listViewDate.getSelectionModel().getSelectedItem();
		if(scheduleCalendarDateHBoxCell != null) {
			scheduleProgramsList = mapOfDateCalendar.get(scheduleCalendarDateHBoxCell);
	
			ScheduleProgram scheduleProgram = new ScheduleProgram();
			scheduleProgram.setDisplacementTimeOfScheduleProgram("0");
			scheduleProgram.setTimeONOfScheduleProgram("00:00");
	
			scheduleProgramsList.add(scheduleProgram);
			//tableViewScheduleProgramsByDate.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar().get(scheduleCalendarDateHBoxCell)));
			//tableViewScheduleProgramsByDate.getSelectionModel().select(tableViewScheduleProgramsByDate.getItems().size() - 1);
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Создайте дату");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
		}
	}*/

	public void deleteScheduleProgramDate() {
		mapOfDateCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar();
		ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell = listViewDate.getSelectionModel().getSelectedItem();
		List<ScheduleProgram> scheduleProgramsList = mapOfDateCalendar.get(scheduleCalendarDateHBoxCell);
		//int selectedScheduleProgram = tableViewScheduleProgramsByDate.getSelectionModel().getSelectedIndex();

		/*if (selectedScheduleProgram >= 0) {
			scheduleProgramsList.remove(selectedScheduleProgram);
			//tableViewScheduleProgramsByDate.getItems().remove(selectedScheduleProgram);
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

	public void selectRoadProgram() {
		
		RoadProgram selectedRoadProgram = tableViewAllProgram.getSelectionModel().getSelectedItem();
		
		if(!selectedRoadProgram.getRoadProgram_programMode().getMode().equals("Замена фаз") || !selectedRoadProgram.getRoadProgram_programMode().getMode().equals("Желтое мигание") || 
				!selectedRoadProgram.getRoadProgram_programMode().getMode().equals("Отключение светофора")) {
		
			List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(selectedRoadProgram);
			if(phaseInProgramsList.size() != 0) {
				
				buttonSwichPhase.setVisible(false);
				tableViewPhaseInProgram.setVisible(true);
				buttonCreatePhaseInProgram.setVisible(true);
				buttonDeletePhaseInProgram.setVisible(true);
				
				tableViewPhaseInProgram.setItems(FXCollections.observableArrayList(phaseInProgramsList));
			}else {
				tableViewPhaseInProgram.getItems().clear();
			}
		
		}
		
		if(selectedRoadProgram.getRoadProgram_programMode().getMode().equals("Замена фаз")) {			
			buttonSwichPhase.setVisible(true);
			tableViewPhaseInProgram.setVisible(false);
			buttonCreatePhaseInProgram.setVisible(false);
			buttonDeletePhaseInProgram.setVisible(false);
		}
		
		labelProgramNumber.setText(selectedRoadProgram.getRoadProgram_number());
	}

	public void clickNextProgram() {
		System.out.println("----- Klick button 'Next' -----");
		
		if(!tableViewAllProgram.getItems().isEmpty()) {
			mapOfPhasesInProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
			int index = tableViewAllProgram.getSelectionModel().getSelectedIndex();
			int finalIndex = tableViewAllProgram.getItems().size() - 1;
						
			if (index == finalIndex) {
				tableViewAllProgram.getSelectionModel().select(0);
				labelProgramNumber.setText(tableViewAllProgram.getSelectionModel().getSelectedItem().getRoadProgram_number());
				
				String type = tableViewAllProgram.getSelectionModel().getSelectedItem().getRoadProgram_programMode().getMode();
				
				RoadProgram selectedRoadProgram = tableViewAllProgram.getSelectionModel().getSelectedItem();
				
				if(type.equals("Замена фаз")) {
					System.out.println("Замена фаз");
					buttonSwichPhase.setVisible(true);
					tableViewPhaseInProgram.setVisible(false);
					buttonCreatePhaseInProgram.setVisible(false);
					buttonDeletePhaseInProgram.setVisible(false);
					
				}else if(type.equals("Желтое мигание")) {
					System.out.println("Желтое мигание");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);
					
				}else if(type.equals("Отключение светофора")) {
					System.out.println("Отключение светофора");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);
					
				}else {
					System.out.println("Программа с фазами");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);					
					
					List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(selectedRoadProgram);
					if(phaseInProgramsList.size() != 0) {
						tableViewPhaseInProgram.setItems(FXCollections.observableArrayList(phaseInProgramsList));
					}else {
						tableViewPhaseInProgram.getItems().clear();
					}
					
				}				
				
			}else {
				tableViewAllProgram.getSelectionModel().select(index + 1);
				labelProgramNumber.setText(tableViewAllProgram.getSelectionModel().getSelectedItem().getRoadProgram_number());
				String type = tableViewAllProgram.getSelectionModel().getSelectedItem().getRoadProgram_programMode().getMode();
				
				RoadProgram selectedRoadProgram = tableViewAllProgram.getSelectionModel().getSelectedItem();
				
				if(type.equals("Замена фаз")) {
					System.out.println("Замена фаз");
					buttonSwichPhase.setVisible(true);
					tableViewPhaseInProgram.setVisible(false);
					buttonCreatePhaseInProgram.setVisible(false);
					buttonDeletePhaseInProgram.setVisible(false);
					
				}else if(type.equals("Желтое мигание")) {
					System.out.println("Желтое мигание");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);
					
				}else if(type.equals("Отключение светофора")) {
					System.out.println("Отключение светофора");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);
					
				}else {
					System.out.println("Программа с фазами");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);					
					
					List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(selectedRoadProgram);
					if(phaseInProgramsList.size() != 0) {
						tableViewPhaseInProgram.setItems(FXCollections.observableArrayList(phaseInProgramsList));
					}else {
						tableViewPhaseInProgram.getItems().clear();
					}
					
				}
				
			}
			
		}
	}

	public void clickPreviousProgram() {
		System.out.println("----- Klick button 'Previous' -----");
		
		if(!tableViewAllProgram.getItems().isEmpty()) {
			mapOfPhasesInProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
			int index = tableViewAllProgram.getSelectionModel().getSelectedIndex();
			int finalIndex = tableViewAllProgram.getItems().size() - 1;
			
			if(index == 0) {
				tableViewAllProgram.getSelectionModel().select(finalIndex);
				labelProgramNumber.setText(tableViewAllProgram.getSelectionModel().getSelectedItem().getRoadProgram_number());
				
				String type = tableViewAllProgram.getSelectionModel().getSelectedItem().getRoadProgram_programMode().getMode();
				
				RoadProgram selectedRoadProgram = tableViewAllProgram.getSelectionModel().getSelectedItem();
				
				if(type.equals("Замена фаз")) {
					System.out.println("Замена фаз");
					buttonSwichPhase.setVisible(true);
					tableViewPhaseInProgram.setVisible(false);
					buttonCreatePhaseInProgram.setVisible(false);
					buttonDeletePhaseInProgram.setVisible(false);
					
				}else if(type.equals("Желтое мигание")) {
					System.out.println("Желтое мигание");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);
					
				}else if(type.equals("Отключение светофора")) {
					System.out.println("Отключение светофора");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);
					
				}else {
					System.out.println("Программа с фазами");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);					
					
					List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(selectedRoadProgram);
					if(phaseInProgramsList.size() != 0) {
						tableViewPhaseInProgram.setItems(FXCollections.observableArrayList(phaseInProgramsList));
					}else {
						tableViewPhaseInProgram.getItems().clear();
					}
					
				}
				
			}else {
				
				tableViewAllProgram.getSelectionModel().select(tableViewAllProgram.getSelectionModel().getSelectedIndex() - 1);
				labelProgramNumber.setText(tableViewAllProgram.getSelectionModel().getSelectedItem().getRoadProgram_number());
				
				String type = tableViewAllProgram.getSelectionModel().getSelectedItem().getRoadProgram_programMode().getMode();
				
				RoadProgram selectedRoadProgram = tableViewAllProgram.getSelectionModel().getSelectedItem();
				
				if(type.equals("Замена фаз")) {
					System.out.println("Замена фаз");
					buttonSwichPhase.setVisible(true);
					tableViewPhaseInProgram.setVisible(false);
					buttonCreatePhaseInProgram.setVisible(false);
					buttonDeletePhaseInProgram.setVisible(false);
					
				}else if(type.equals("Желтое мигание")) {
					System.out.println("Желтое мигание");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);
					
				}else if(type.equals("Отключение светофора")) {
					System.out.println("Отключение светофора");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);
					
				}else {
					System.out.println("Программа с фазами");
					tableViewPhaseInProgram.getItems().clear();
					buttonSwichPhase.setVisible(false);
					tableViewPhaseInProgram.setVisible(true);
					buttonCreatePhaseInProgram.setVisible(true);
					buttonDeletePhaseInProgram.setVisible(true);					
					
					List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(selectedRoadProgram);
					if(phaseInProgramsList.size() != 0) {
						tableViewPhaseInProgram.setItems(FXCollections.observableArrayList(phaseInProgramsList));
					}else {
						tableViewPhaseInProgram.getItems().clear();
					}
					
				}
				
			}
			
		}
		
		
		
		/*if(!tableViewAllProgram.getItems().isEmpty()) {
			mapOfPhasesInProgram = iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
			int index = tableViewAllProgram.getSelectionModel().getSelectedIndex();
			int finalIndex = tableViewAllProgram.getItems().size() - 1;
			String number = Integer.toString(index);
			labelProgramNumber.setText(number);
	
			if (index == 0) {
				tableViewAllProgram.getSelectionModel().select(finalIndex);
				number = Integer.toString(tableViewAllProgram.getItems().size());
				labelProgramNumber.setText(number);
			} else {
				labelProgramNumber.textProperty().addListener((observable, oldValue, newValue) -> {
					for (Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {
						RoadProgram roadProgram = entry.getKey();
						if (newValue.equals(roadProgram.getRoadProgram_number())) {
							tableViewPhaseInProgram.getItems().clear();
	
							tableViewPhaseInProgram.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram().get(roadProgram)));
						}
					}
				});
				tableViewAllProgram.getSelectionModel().select(tableViewAllProgram.getSelectionModel().getSelectedIndex() - 1);
				tableViewPhaseInProgram.getSelectionModel().selectFirst();
			}
		}*/
	}

	public void copyScheduleOfCurrentDay() {
		chCopyDay.setItems(observableListCopyScheduleOfCurrentDay);
		chCopyDay.getSelectionModel().selectFirst();
	}

	public void selectWeekDay() {
		tableViewSchedulePrograms.getItems().clear();
		mapOfWeekCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();

		ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = listViewDayOfWeek.getSelectionModel().getSelectedItem();

		List<ScheduleProgram> scheduleProgramsList = mapOfWeekCalendar.get(scheduleCalendarHBoxCell);

		if (!scheduleProgramsList.isEmpty()) {
			tableViewSchedulePrograms.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar().get(scheduleCalendarHBoxCell)));
		}

	}

	public void selectDate() {
		
		mapOfDateCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar();
		mapOfWeekCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();

		ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell = listViewDate.getSelectionModel().getSelectedItem();
		
		scheduleCalendarDateHBoxCell.getChoiceBox().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				
				for(Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> weekEntry : mapOfWeekCalendar.entrySet()) {
					String existDay = weekEntry.getKey().getWeekDay().getText();
					
					if(newValue.equals(existDay)) {
						
						List<ScheduleProgram> schedulePrograms = weekEntry.getValue();
						
						if(!schedulePrograms.isEmpty()) {
							
							mapOfDateCalendar.put(scheduleCalendarDateHBoxCell, schedulePrograms);
							
						}else {
							Alert alert = new Alert(Alert.AlertType.WARNING);
							alert.setTitle("Внимание");
							alert.setHeaderText("Для данного дня не создано расписание программ");
							
							Stage stage = new Stage();
							stage = (Stage)alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
							
							alert.showAndWait();
						}
						
					}
					
				}
			}
		});
		
		scheduleCalendarDateHBoxCell.getDatePicker().valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				System.out.println(newValue);
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
				
				String formattedValue = (scheduleCalendarDateHBoxCell.getDatePicker().getValue()).format(formatter);
				
				scheduleCalendarDateHBoxCell.getTextFild().setText(formattedValue);
				
			}
		});
		
		
		/*ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell = listViewDate.getSelectionModel().getSelectedItem();
		for (ScheduleCalendarDateHBoxCell existed : listViewDate.getItems()) {
			if (existed.equals(scheduleCalendarDateHBoxCell)) {
				scheduleCalendarDateHBoxCell.getDatePicker().setDisable(false);
				List<ScheduleProgram> scheduleProgramsList = mapOfDateCalendar.get(scheduleCalendarDateHBoxCell);
				if (!scheduleProgramsList.isEmpty()) {
					tableViewScheduleProgramsByDate.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfDateCalendar().get(scheduleCalendarDateHBoxCell)));
				}
			} else {
				existed.getDatePicker().setDisable(true);
			}
		}*/
	}

	public void copyByDay() {
		mapOfWeekCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();

		ScheduleCalendarWeekDayHBoxCell selectedScheduleCalendarHBoxCell = listViewDayOfWeek.getSelectionModel().getSelectedItem();
		String selectedDay = chCopyDay.getSelectionModel().getSelectedItem();

		for (Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekCalendar.entrySet()) {
			ScheduleCalendarWeekDayHBoxCell existedScheduleCalendarHBoxCell = entry.getKey();
			if (selectedDay.equals(existedScheduleCalendarHBoxCell.getWeekDay().getText())) {
				List<ScheduleProgram> scheduleProgramsList = entry.getValue();
				if (!scheduleProgramsList.isEmpty()) {
					List<ScheduleProgram> copyList = scheduleProgramsList;
					for (ScheduleProgram scheduleProgram : copyList) {
						ScheduleProgram copyScheduleProgram = new ScheduleProgram();

						UUID uuid = UUID.randomUUID();
						String id = uuid.toString();

						copyScheduleProgram.setScheduleProgramId(id);
						copyScheduleProgram.setTimeONOfScheduleProgram(scheduleProgram.getTimeONOfScheduleProgram());
						copyScheduleProgram.setNumberOfScheduleProgram(scheduleProgram.getNumberOfScheduleProgram());
						copyScheduleProgram.setDisplacementTimeOfScheduleProgram(scheduleProgram.getDisplacementTimeOfScheduleProgram());

						List<ScheduleProgram> copyToList = mapOfWeekCalendar.get(selectedScheduleCalendarHBoxCell);
						copyToList.add(copyScheduleProgram);

					}
					selectedScheduleCalendarHBoxCell.getCheckBox().setSelected(true);
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Ошибка");
					alert.setHeaderText("Создайте расписание");
					
					Stage stage = new Stage();
					stage = (Stage)alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
					
					alert.showAndWait();
					selectedScheduleCalendarHBoxCell.getCheckBox().setSelected(false);
				}
			}
		}
		tableViewSchedulePrograms.setItems(FXCollections.observableArrayList(iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar().get(selectedScheduleCalendarHBoxCell)));
	}

	public void copyForAllDays() {
		System.out.println("Copy for all days");
		mapOfWeekCalendar = iRoadModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();

		String selectedDay = chCopyDay.getSelectionModel().getSelectedItem();
		List<ScheduleProgram> scheduleProgramsList = null;
		for (Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekCalendar.entrySet()) {
			ScheduleCalendarWeekDayHBoxCell scheduleCalendarHBoxCell = entry.getKey();
			if (selectedDay.equals(scheduleCalendarHBoxCell.getWeekDay().getText())) {
				scheduleProgramsList = entry.getValue();
				break;
			}
		}

		if(!scheduleProgramsList.isEmpty()){
			Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> copyScheduleProgramMap = new LinkedHashMap<>();

			for(Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekCalendar.entrySet()){
				copyScheduleProgramMap.put(entry.getKey(), scheduleProgramsList);
				entry.getKey().getCheckBox().setSelected(true);
			}

			iRoadModel.getModel().getRoadProgramsModel().setMapOfWeekCalendar(copyScheduleProgramMap);

		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Создайте расписание");

			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));

			alert.showAndWait();
		}
	}

	public void openSpeedSign() {
		System.out.println("Open speed sign");

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SpeedSign.fxml"));
			AnchorPane anchorPane = fxmlLoader.load();

			SpeedSignPresenter speedSignPresenter = fxmlLoader.getController();
			//System.out.println(speedSignPresenter);
			speedSignPresenter.showSpeedSign(iRoadModel);

			Scene scene = new Scene(anchorPane);

			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			stage.setTitle("Знаки рекомендуемой скорости");
			stage.setScene(scene);
			stage.showAndWait();

			if (speedSignPresenter.OKwasPressed == true) {
				speedSignPresenter.pressOK();
				System.out.println(iRoadModel.getModel().getRoadProgramsModel().getMapOfProgramSpeedSign());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void openSwichPhase() {
		System.out.println("Open swich phase pane");
		
		RoadProgram roadProgram = tableViewAllProgram.getSelectionModel().getSelectedItem();
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SwichPhase.fxml"));
			AnchorPane anchorPane = fxmlLoader.load();
			
			SwitchPhasePresenter swichPhasePresenter = fxmlLoader.getController();
			swichPhasePresenter.showSwichPhase(iRoadModel, roadProgram);
			
			
			Scene scene = new Scene(anchorPane);

			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			stage.setTitle("Конфигурация");
			stage.setScene(scene);
			stage.showAndWait();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@FXML
	public void initialize() {

		//langXML();
		//loadLang(langXML);

		copyScheduleOfCurrentDay();

		tableViewAllProgram.setEditable(true);
		tableViewSchedulePrograms.setEditable(true);
		//tableViewScheduleProgramsByDate.setEditable(true);
		tableViewPhaseInProgram.setEditable(true);
		
		tableViewAllProgram.setPlaceholder(new Label("Нет данных для отображения"));
		tableViewPhaseInProgram.setPlaceholder(new Label("Нет данных для отображения"));
		tableViewSchedulePrograms.setPlaceholder(new Label("Нет данных для отображения"));
		listViewDate.setPlaceholder(new Label("Нет данных для отображения"));
		
		buttonCreateScheduleProgram.setDisable(true);
		
		observableListCopyScheduleOfCurrentDay.addAll("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье");
		chCopyDay.setItems(observableListCopyScheduleOfCurrentDay);
		chCopyDay.setValue("Понедельник");
		
		//cbScheduleDate.setItems(observableListCopyScheduleOfCurrentDay);
		
		listViewDate.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				for(ScheduleCalendarDateHBoxCell scheduleCalendarDateHBoxCell : listViewDate.getItems()) {
					scheduleCalendarDateHBoxCell.getDatePicker().setVisible(false);
					scheduleCalendarDateHBoxCell.getDatePicker().setDisable(true);
					scheduleCalendarDateHBoxCell.getChoiceBox().setDisable(true);
					scheduleCalendarDateHBoxCell.getTextFild().setDisable(true);
				}
				newValue.getDatePicker().setVisible(true);
				newValue.getDatePicker().setDisable(false);
				newValue.getChoiceBox().setDisable(false);
				newValue.getTextFild().setDisable(false);
			}
		});
		
				
		// Cell Factory for Table View "All programs"
		Callback<TableColumn<RoadProgram, String>, TableCell<RoadProgram, String>> programCellFactory = (TableColumn<RoadProgram, String> param) -> new ProgramEditingCell();
		Callback<TableColumn<RoadProgram, ProgramMode>, TableCell<RoadProgram, ProgramMode>> programComboBoxCellFactory = (TableColumn<RoadProgram, ProgramMode> param) -> new ProgramsComboBoxEditingCell();
		Callback<TableColumn<RoadProgram, BackupProgram>, TableCell<RoadProgram, BackupProgram>> backupProgramComboBoxCellFactory = (TableColumn<RoadProgram, BackupProgram> param) -> new BackupProgramComboboxEditingCell();
		// Callback<TableColumn<RoadDirectionsModel, String>,
		// TableCell<RoadDirectionsModel, String>> dateCellFactory
		// = (TableColumn<RoadDirectionsModel, String> param) -> new DateEditingCell();

		// Cell Factory for timetable specified controller programs
		Callback<TableColumn<ScheduleProgram, String>, TableCell<ScheduleProgram, String>> scheduleProgramCellFactory = (TableColumn<ScheduleProgram, String> param) -> new ScheduleProgramStartTimeEditingCell();
		Callback<TableColumn<ScheduleProgram, ScheduleNumber>, TableCell<ScheduleProgram, ScheduleNumber>> scheduleProgramComboBoxCellFactory = (TableColumn<ScheduleProgram, ScheduleNumber> param) -> new ScheduleProgramComboBoxEditingCell(
				iRoadModel.getModel().getRoadProgramsModel());
		Callback<TableColumn<ScheduleProgram, String>, TableCell<ScheduleProgram, String>> scheduleProgramOffsetTimeCellFactory = (TableColumn<ScheduleProgram, String> param) -> new ScheduleCalendarOffsetTimeEditingCell();

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// Cell Factory for Table View "Phase in program"
		Callback<TableColumn<PhaseInProgram, String>, TableCell<PhaseInProgram, String>> phaseInProgramCellFactory = (TableColumn<PhaseInProgram, String> param) -> new PhaseInProgramEditingCell(iRoadModel.getModel().getRoadPhaseModel());
		Callback<TableColumn<PhaseInProgram, PhaseNumber>, TableCell<PhaseInProgram, PhaseNumber>> phaseInProgramComboBoxCellFactory = (TableColumn<PhaseInProgram, PhaseNumber> param) -> new PhaseInProgramComboBoxEditingCell(
				iRoadModel.getModel().getRoadPhaseModel());

		/////////////////////////////////////////////////////////////////////////////////////////////////////////

		// Table View "All program"
		tableColumnNumber.setCellValueFactory(cellData -> cellData.getValue().roadProgramNumberProperty());
		tableColumnNumber.setCellFactory(programCellFactory);
		tableColumnNumber.setStyle("-fx-alignment: CENTER;");
		tableColumnNumber.setOnEditCommit((TableColumn.CellEditEvent<RoadProgram, String> t) -> {
			((RoadProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadProgram_number(t.getNewValue());
		});

		tableColumnProgramMode.setCellValueFactory(cellData -> cellData.getValue().roadProgramProgramModeProperty());
		tableColumnProgramMode.setCellFactory(programComboBoxCellFactory);
		tableColumnProgramMode.setStyle("-fx-alignment: CENTER;");
		tableColumnProgramMode.setOnEditCommit((TableColumn.CellEditEvent<RoadProgram, ProgramMode> t) -> {
			((RoadProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadProgram_programMode(t.getNewValue());
			
			if(t.getNewValue().getMode().equals("Желтое мигание") || t.getNewValue().getMode().equals("Отключение светофора")) {		// if type don't support phase
				List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(tableViewAllProgram.getSelectionModel().getSelectedItem());
				phaseInProgramsList.clear();
				tableViewPhaseInProgram.getItems().clear();
			}
		});

		tableColumnBackupProgram.setCellValueFactory(cellData -> cellData.getValue().roadProgramBackupProgramProperty());
		tableColumnBackupProgram.setCellFactory(backupProgramComboBoxCellFactory);
		tableColumnBackupProgram.setStyle("-fx-alignment: CENTER;");
		tableColumnBackupProgram.setOnEditCommit((TableColumn.CellEditEvent<RoadProgram, BackupProgram> t) -> {
			((RoadProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRoadProgram_backupProgram(t.getNewValue());
		});
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// Table View "Schedule program"
		tableColumnScheduleProgramsNumber.setCellValueFactory(cellData -> cellData.getValue().numberOfScheduleProgramProperty());
		tableColumnScheduleProgramsNumber.setCellFactory(scheduleProgramComboBoxCellFactory);
		tableColumnScheduleProgramsNumber.setStyle("-fx-alignment: CENTER;");
		tableColumnScheduleProgramsNumber.setOnEditCommit((TableColumn.CellEditEvent<ScheduleProgram, ScheduleNumber> t) -> {
			((ScheduleProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNumberOfScheduleProgram(t.getNewValue());
		});

		tableColumnScheduleProgramsTimeON.setCellValueFactory(cellData -> cellData.getValue().timeONOfScheduleProgramProperty());
		tableColumnScheduleProgramsTimeON.setCellFactory(scheduleProgramCellFactory);
		tableColumnScheduleProgramsTimeON.setStyle("-fx-alignment: CENTER;");
		tableColumnScheduleProgramsTimeON.setOnEditCommit((TableColumn.CellEditEvent<ScheduleProgram, String> t) -> {
			((ScheduleProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTimeONOfScheduleProgram(t.getNewValue());
		});

		tableColumnScheduleProgramsDisplacement.setCellValueFactory(cellData -> cellData.getValue().displacementTimeOfScheduleProgramProperty());
		tableColumnScheduleProgramsDisplacement.setCellFactory(scheduleProgramOffsetTimeCellFactory);
		tableColumnScheduleProgramsDisplacement.setStyle("-fx-alignment: CENTER;");
		tableColumnScheduleProgramsDisplacement.setOnEditCommit((TableColumn.CellEditEvent<ScheduleProgram, String> t) -> {
			((ScheduleProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDisplacementTimeOfScheduleProgram(t.getNewValue());
		});

		/*tableColumnScheduleProgramsNumber1.setCellValueFactory(cellData -> cellData.getValue().numberOfScheduleProgramProperty());
		tableColumnScheduleProgramsNumber1.setCellFactory(scheduleProgramComboBoxCellFactory);
		tableColumnScheduleProgramsNumber1.setStyle("-fx-alignment: CENTER;");
		tableColumnScheduleProgramsNumber1.setOnEditCommit((TableColumn.CellEditEvent<ScheduleProgram, ScheduleNumber> t) -> {
			((ScheduleProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNumberOfScheduleProgram(t.getNewValue());
		});

		tableColumnScheduleProgramsTimeON1.setCellValueFactory(cellData -> cellData.getValue().timeONOfScheduleProgramProperty());
		tableColumnScheduleProgramsTimeON1.setCellFactory(scheduleProgramCellFactory);
		tableColumnScheduleProgramsTimeON1.setStyle("-fx-alignment: CENTER;");
		tableColumnScheduleProgramsTimeON1.setOnEditCommit((TableColumn.CellEditEvent<ScheduleProgram, String> t) -> {
			((ScheduleProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTimeONOfScheduleProgram(t.getNewValue());
		});

		tableColumnScheduleProgramsDisplacement1.setCellValueFactory(cellData -> cellData.getValue().displacementTimeOfScheduleProgramProperty());
		tableColumnScheduleProgramsDisplacement1.setCellFactory(scheduleProgramOffsetTimeCellFactory);
		tableColumnScheduleProgramsDisplacement1.setStyle("-fx-alignment: CENTER;");
		tableColumnScheduleProgramsDisplacement1.setOnEditCommit((TableColumn.CellEditEvent<ScheduleProgram, String> t) -> {
			((ScheduleProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDisplacementTimeOfScheduleProgram(t.getNewValue());
		});*/
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// Table View "Phase in program"
		tableColumnPhase.setCellValueFactory(cellData -> cellData.getValue().phaseInProgramProperty());
		tableColumnPhase.setCellFactory(phaseInProgramComboBoxCellFactory);
		tableColumnPhase.setStyle("-fx-alignment: CENTER;");
		tableColumnPhase.setOnEditCommit((TableColumn.CellEditEvent<PhaseInProgram, PhaseNumber> t) -> {
			((PhaseInProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhaseInProgramNumber(t.getNewValue());
		});

		tableColumnPhaseDuration.setCellValueFactory(cellData -> cellData.getValue().durationPhaseInProgramProperty());
		tableColumnPhaseDuration.setCellFactory(phaseInProgramCellFactory);
		tableColumnPhaseDuration.setStyle("-fx-alignment: CENTER;");
		tableColumnPhaseDuration.setOnEditCommit((TableColumn.CellEditEvent<PhaseInProgram, String> t) -> {
			((PhaseInProgram) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDurationPhaseInProgram(t.getNewValue());
		});
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

}
