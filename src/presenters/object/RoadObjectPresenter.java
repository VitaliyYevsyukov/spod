package presenters.object;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import controllers.Employee;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import presenters.conflicts.ConflictForDirection;
import presenters.conflicts.ConflictWithDirection;
import presenters.directions.RoadDirection;
import presenters.directions.TypDirection;
import presenters.phase.RoadPhase;
import presenters.programs.BackupProgram;
import presenters.programs.PhaseInProgram;
import presenters.programs.PhaseNumber;
import presenters.programs.ProgramMode;
import presenters.programs.RoadProgram;
import presenters.programs.ScheduleCalendarWeekDayHBoxCell;
import presenters.programs.ScheduleNumber;
import presenters.programs.ScheduleProgram;
import presenters.promtactu.InterphaseTransitionsHBoxCell;
import presenters.promtactu.PromtactData;
import roadModel.IRoadModel;
import roadModel.RoadModelCreator;

/**
 * Created by Vitaly on 06.12.2016.
 */
public class RoadObjectPresenter {

	@FXML
	private TextField txtName, txtObjectNumber, txtNetworkAddress, txtCity, txtMagistral, txtTechnologist;
	@FXML
	private TextArea txtAreaNote;
	@FXML
	private ChoiceBox<String> chBoxCountry, chBoxProtokol, chBoxTypeKDK, chCharge;
	@FXML
	private DatePicker dPdateOfCreation, dPlaunchDate;
	@FXML
	private Button buttonObjects, buttonReferenceBookOfTheKDK, openConnectSettings;
	@FXML
	private Label labelName, labelObject, labelCountry, labelRegion, labelCity, labelObjectNumber, labelNetworkAddress, labelMagistral, labelProtocol, labelDateOfCreation, labelLaunchDate, labelNote, labelTechnologist, labelDirectionLoad1,
			labelDirectionsLoad2, labelTypeOfKDK, labelViewObject, labelMaxChannel, labelMaxProgram, labelMaxDirections, labelMaxTVP, labelMaxPhases, lblMAX_channels, lblMAX_program, lblMAX_directions, lblMAX_tvp, lblMAX_phases;

	ListView<String> listView;

	String dateCreation = "";
	LocalDate localDateCreation;
	DateTimeFormatter formatterCreation;
	String dateLaunchDate = "";
	LocalDate localLaunchDate;
	DateTimeFormatter formatterLaunchDate;

	String objectId;
	String objectName;
	String selectedItem;
	String queryBySelectedObjectId;
	String configurationId;
	String employeeName;
	String roadModelIP;
	String roadModelIndexNumber;
	String roadModelAddress;
	String directionNumber;
	String directionType;
	String endGreenAddit;
	String endGreenBlink;
	String endRed;
	String endRedYellow;
	String endYellow;
	String directionID;
	String directionName;
	String phaseNumber;
	String phaseTmin;
	String programNumber;
	String conflictForDir;
	String conflictWithDir;
	String keyPhaseNumber;
	String openDirValue;
	String fromPhase;
	String toPhase;

	String dataBaseName;
	String port;
	String localHost;
	String userName;
	String password;
	String portFB;
	String pathFB;
	String userFB;
	String passwordFB;

	@FXML
	//private static ResourceBundle bundleGUI, bundleAlert;
	//private static Locale localeGUI, localeAlert;
	//static String langXML = null;

	ObservableList<String> observableListNameObjects = FXCollections.observableArrayList();
	ObservableList<String> observableListObjectId = FXCollections.observableArrayList();
	ObservableList<String> observableListTypeKDK = FXCollections.observableArrayList();

	Map<String, String> mapObjectNameByObjectId = new HashMap<>();
	Map<String, String> mapObjectsFB;
	Map<String, String> mapTypeDirection = new HashMap<>();
	Map<String, String> mapOfPhaseTmin;
	Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram;
	Map<RoadProgram, List<ScheduleProgram>> mapOfScheduleProgram;
	Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> mapOfWeekCalendar;

	Map<String, List<ConflictWithDirection>> map;

	Map<String, String> mapCameraZones;
	Map<String, String> mapOfFBObject;

	ConflictForDirection conflictForDirection;
	ConflictWithDirection conflictWithDirection;

	List<RoadDirection> roadDirectionList = new ArrayList<>();
	List<RoadPhase> roadPhaseList = new ArrayList<>();
	List<RoadProgram> roadProgramList = new ArrayList<>();
	List<PhaseInProgram> phaseInProgramList;
	List<ScheduleProgram> scheduleProgramsList;
	List<String> listOfPhaseTo;
	List<ConflictForDirection> conflictForDirectionList = new ArrayList<>();
	List<ConflictWithDirection> conflictWithDirectionList = new ArrayList<>();
	List<ConflictWithDirection> conflictList;
	List<String> openDirInPhaseValue;

	List<TypeKDK> typeKDKList = new LinkedList<>();

	Map<String, List<String>> interPhaseMap;
	Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> basicMapInterphaseSpecificPromtact;
	Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapInterphaseSpecPromtact = new HashMap<>();
	Map<String, PromtactData> specificPromtactDataMap;
	Map<String, PromtactData> basicPromtactDataMap;

	private IRoadModel iRoadObjectModel;
	RoadModelCreator roadModelCreator;
	Employee employee = null;
	
	ConnectSettingsPresenter connectSettingsPresenter;

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

		labelObject.setText(bundleGUI.getString("labelObject"));
		labelName.setText(bundleGUI.getString("labelName"));
		labelCountry.setText(bundleGUI.getString("labelCountry"));
		labelCity.setText(bundleGUI.getString("labelCity"));
		labelDateOfCreation.setText(bundleGUI.getString("labelDateOfCreation"));
		labelDirectionLoad1.setText(bundleGUI.getString("labelDirectionLoad1"));
		labelDirectionsLoad2.setText(bundleGUI.getString("labelDirectionLoad2"));
		// labelFirmware.setText(bundleGUI.getString("labelFirmware"));
		labelLaunchDate.setText(bundleGUI.getString("labelLaunchDate"));
		labelMagistral.setText(bundleGUI.getString("labelMagistral"));
		labelMaxChannel.setText(bundleGUI.getString("labelMaxChannel"));
		labelMaxDirections.setText(bundleGUI.getString("labelMaxDirections"));
		labelMaxPhases.setText(bundleGUI.getString("labelMaxPhases"));
		labelMaxProgram.setText(bundleGUI.getString("labelMaxProgram"));
		labelMaxTVP.setText(bundleGUI.getString("labelMaxTVP"));
		labelNetworkAddress.setText(bundleGUI.getString("labelNetworkAddress"));
		labelNote.setText(bundleGUI.getString("labelNote"));
		labelObjectNumber.setText(bundleGUI.getString("labelObjectNumber"));
		labelProtocol.setText(bundleGUI.getString("labelProtocol"));
		labelTypeOfKDK.setText(bundleGUI.getString("labelTypeOfKDK"));
		labelTechnologist.setText(bundleGUI.getString("labelTechnologist"));
		labelViewObject.setText(bundleGUI.getString("labelViewObject"));
		buttonObjects.setText(bundleGUI.getString("buttonObjects"));
		buttonReferenceBookOfTheKDK.setText(bundleGUI.getString("buttonReferenceBookOfTheKDK"));
	}

	private static void alertLang(String lang) {
		localeAlert = new Locale(lang);
		bundleAlert = ResourceBundle.getBundle("localeAlert_lang", localeAlert);
	}*/

	public void show(IRoadModel roadObjectModel) {
		this.iRoadObjectModel = roadObjectModel;
		iRoadObjectModel.getModel().getRoadObjectModel().setKdkTypeList(typeKDKList);

		System.out.println(iRoadObjectModel);

		if(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectCountry().equals("")) {
			chBoxCountry.setItems(FXCollections.observableArrayList("Украина", "Россия", "Белорусь", "Молдова", "Другая страна"));
		}else {
			chBoxCountry.setValue(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectCountry());
		}
		if(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectProtocol().equals("")) {
			chBoxProtokol.setItems(FXCollections.observableArrayList("Радио 'КОМКОН' - 1200", "Ethernet 'КОМКОН' - 115200", "Радио 'Росток'", "Харьков(1 линия)",
					"Харьков(2 линия)", "Житомир(1 линия)", "Житомир(2 линия)", "Луганск(1 линия)", "Луганск(2 линия)"));
		}else {
			chBoxProtokol.setValue(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectProtocol());
		}
		if(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK().equals("")) {
			chBoxTypeKDK.setItems(FXCollections.observableArrayList(observableListTypeKDK));
		}else {
			chBoxTypeKDK.setValue(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK());
		}
		if(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectCharge().equals("")) {
			chCharge.setItems(FXCollections.observableArrayList("Красно-желтый", "Желтый"));
		}else {
			chCharge.setValue(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectCharge());
		}
		
		txtName.setText(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectName());
		txtObjectNumber.setText(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectObjectNumber());
		txtNetworkAddress.setText(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectNetworkAddress());
		txtCity.setText(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectCity());
		txtMagistral.setText(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectMagistral());
		txtTechnologist.setText(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectTechnologist());
		txtAreaNote.setText(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectNote());
		chBoxCountry.setValue(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectCountry());
		chBoxProtokol.setValue(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectProtocol());
		chBoxTypeKDK.setValue(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK());
		chCharge.setValue(roadObjectModel.getModel().getRoadObjectModel().getRoadObjectCharge());

		if (roadObjectModel.getModel().getRoadObjectModel().getRoadObjectDateOfCreation().equals("")) {
			dPdateOfCreation.setValue(null);
		} else {
			String date = roadObjectModel.getModel().getRoadObjectModel().getRoadObjectDateOfCreation();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate localDate = LocalDate.parse(date, formatter);
			dPdateOfCreation.setValue(localDate);
		}
		if (roadObjectModel.getModel().getRoadObjectModel().getRoadObjectLaunchDate().equals("")) {
			dPlaunchDate.setValue(null);
		} else {
			String date = roadObjectModel.getModel().getRoadObjectModel().getRoadObjectLaunchDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate localDate = LocalDate.parse(date, formatter);
			dPlaunchDate.setValue(localDate);
		}
		
		if(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectProtocol() != null) {
			if(!iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectProtocol().equals("")) {
				chBoxProtokol.setTooltip(new Tooltip(chBoxProtokol.getValue()));
			}
		}
		
		
	}

	public void save(IRoadModel roadObjectModel) {
		this.iRoadObjectModel = roadObjectModel;
		
		if (dPdateOfCreation.getValue() != null) {
			localDateCreation = dPdateOfCreation.getValue();
			formatterCreation = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			dateCreation = localDateCreation.format(formatterCreation);
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectDateOfCreation(dateCreation);
		}else {
			roadObjectModel.getModel().getRoadObjectModel().setRoadObjectDateOfCreation("");
		}
		if (dPlaunchDate.getValue() != null) {
			localLaunchDate = dPlaunchDate.getValue();
			formatterLaunchDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			dateLaunchDate = localLaunchDate.format(formatterLaunchDate);
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectLaunchDate(dateLaunchDate);
		}else {
			roadObjectModel.getModel().getRoadObjectModel().setRoadObjectLaunchDate("");
		}

		roadObjectModel.getModel().getRoadObjectModel().setRoadObjectName(txtName.getText());
		roadObjectModel.getModel().getRoadObjectModel().setRoadObjectObjectNumber(txtObjectNumber.getText());
		roadObjectModel.getModel().getRoadObjectModel().setRoadObjectNetworkAddress(txtNetworkAddress.getText());
		roadObjectModel.getModel().getRoadObjectModel().setRoadObjectCity(txtCity.getText());
		roadObjectModel.getModel().getRoadObjectModel().setRoadObjectMagistral(txtMagistral.getText());
		roadObjectModel.getModel().getRoadObjectModel().setRoadObjectTechnologist(txtTechnologist.getText());
		roadObjectModel.getModel().getRoadObjectModel().setRoadObjectNote(txtAreaNote.getText());
		
		if(chBoxCountry.getValue() != null) {
			roadObjectModel.getModel().getRoadObjectModel().setRoadObjectCountry(chBoxCountry.getValue().toString());
		}else {
			roadObjectModel.getModel().getRoadObjectModel().setRoadObjectCountry("");
		}
		
		if(chBoxProtokol.getValue() != null) {
			roadObjectModel.getModel().getRoadObjectModel().setRoadObjectProtocol(chBoxProtokol.getValue().toString());
		}else {
			roadObjectModel.getModel().getRoadObjectModel().setRoadObjectProtocol("");
		}
		if(chCharge.getValue() != null) {
			roadObjectModel.getModel().getRoadObjectModel().setRoadObjectCharge(chCharge.getValue().toString());
		}else {
			roadObjectModel.getModel().getRoadObjectModel().setRoadObjectCharge("");
		}
		if(chBoxTypeKDK.getValue() != null) {
			roadObjectModel.getModel().getRoadObjectModel().setRoadObjectTypeOfKDK(chBoxTypeKDK.getValue().toString());
		}
		else {
			roadObjectModel.getModel().getRoadObjectModel().setRoadObjectTypeOfKDK("");
		}
		
	}
	

	public void choiseConnectType() {
		/*
		 * comboBoxConnectType.valueProperty().addListener((observable, oldValue,
		 * newValue) -> { if(newValue.equals("GPRS")) {
		 * txtConnectSpeed.setDisable(false); } });
		 */
	}

	public static SessionFactory getSessionFactory() {
		//alertLang(langXML);
		Configuration configuration;
		SessionFactory sessionFactory = null;
		try {
			configuration = new Configuration().configure("hibernate.cfg.xml");
			sessionFactory = configuration.buildSessionFactory();
		} catch (ServiceException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Нет соединения с базой данных");
			alert.setContentText("");
			alert.showAndWait();
			e.printStackTrace();
		}
		/*
		 * Configuration configuration = new
		 * Configuration().configure("hibernate.cfg.xml"); SessionFactory sessionFactory
		 * = configuration.buildSessionFactory();
		 */
		return sessionFactory;
	}

	public static List<Employee> read() {
		Session session = getSessionFactory().openSession();
		@SuppressWarnings({ "unchecked", "deprecation" })
		List<Employee> roadModelList = session.createQuery("FROM Employee").list();
		session.close();
		System.out.println("Found " + roadModelList.size() + " Employees");
		return roadModelList;
	}

	public void validateObjectNumber() {
		// txtObjectNumber.add
		/*
		 * txtObjectNumber.textProperty().addListener((observable, oldValue, newValue)
		 * -> { if(!newValue.matches("\\d*")) { txtObjectNumber.setText(newValue); }else
		 * { Alert alert = new Alert(AlertType.ERROR); alert.show(); } });
		 */
	}

	////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////// PRESS BUTTON 'ConnectSettings'
	//////////////////////////////////////////////////////////////////////////////////// ///////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	public void openConnectSettings() {
		System.out.println("============== Click 'open connect_settings' ======================");
		System.out.println();
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Settings.fxml"));
			AnchorPane anchorPane = fxmlLoader.load();

			connectSettingsPresenter = fxmlLoader.<ConnectSettingsPresenter>getController();
			//System.out.println(connectSettingsPresenter);
			connectSettingsPresenter.showSettings(this.iRoadObjectModel);

			//System.out.println(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectName());

			Scene scene = new Scene(anchorPane);

			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			stage.setTitle("Настройки соединения");
			stage.setScene(scene);
			stage.showAndWait();
			
			if (connectSettingsPresenter.OKwasPressed == true) {
				//System.out.println(iRoadObjectModel);
				//stage.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////// PRESS BUTTON 'OBJECTS'
	//////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	public void showExistObjects() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ListViewAllObjects.fxml"));
			AnchorPane pane = fxmlLoader.load();

			ListViewAllObjectPresenter listViewAllObjectPresenter = fxmlLoader.getController();
			
			listViewAllObjectPresenter.show(iRoadObjectModel);
			
			Scene scene = new Scene(pane);

			Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			stage.setTitle("Объекты");
			stage.setScene(scene);
			stage.showAndWait();
			
			if(listViewAllObjectPresenter.selectObject == true) {
				show(iRoadObjectModel);
			}
			
			if(listViewAllObjectPresenter.isDelete == true) {
				show(iRoadObjectModel);
			}
			
			/*if (listViewAllObjectPresenter.selectObjectMS() == true) {
				queryBySelectedObjectId = listViewAllObjectPresenter.getSelectedObjectId();
				System.out.println("Id of selected object MS " + queryBySelectedObjectId);
				dbQueryMS();

				roadModelCreator = new RoadModelCreator();
				this.iRoadObjectModel.setModel(roadModelCreator.createRoadModel(employee));
				modelIPMS();
				modelAtributsMS();
				typeDirectionMS();
				modelDirectionsMS();
				modelPhaseMS();
				openDirectionInPhaseMS();
				modelProgramsMS();
				programCalendar();
				modelConflictsMS();
				modelPromtactMS();
				modelDetectorMS();

				iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectConfiguratinId(configurationId);
				txtName.setText(this.iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectName());
				txtObjectNumber.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectObjectNumber());
				txtTechnologist.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectTechnologist());
				txtNetworkAddress.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectNetworkAddress());
				txtObjectNumber.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectObjectNumber());
				txtObjectNumber.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectObjectNumber());
				txtAreaNote.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectNote());
				if (iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectDateOfCreation().equals("")) {
					System.out.println("Date of creation is null");
					dPdateOfCreation.setValue(null);
				} else {
					String date = iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectDateOfCreation();
					LocalDate localDate = LocalDate.parse(date);
					dPdateOfCreation.setValue(localDate);
				}
				if (iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectLaunchDate().equals("")) {
					System.out.println("Launch date is null");
					dPlaunchDate.setValue(null);
				} else {
					String date = iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectLaunchDate();
					LocalDate localDate = LocalDate.parse(date);
					dPlaunchDate.setValue(localDate);
				}
				System.out.println("CONFIGURATION ID " + iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectConfiguratinId());
			}*/

			/*if (listViewAllObjectPresenter.isSelectItemFB == true) {
				queryBySelectedObjectId = listViewAllObjectPresenter.getSelectedObjectId();
				System.out.println("Id of selected object FB " + queryBySelectedObjectId);
				mapObjectsFB = listViewAllObjectPresenter.getFBMap();
				for (Entry<String, String> entry : mapObjectsFB.entrySet()) {
					if (entry.getValue().equals(queryBySelectedObjectId)) {
						String object = entry.getKey();
						System.out.println("OBJECT NAME " + object);

						roadModelCreator = new RoadModelCreator();

						this.iRoadObjectModel.setModel(roadModelCreator.createFBRoadModel(object));
						txtName.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectName());

						modelObject();
						modelProgramsFB();
						modelCalendarFB();
						typeDirectionFB();
						modelDirectionFB();
						modelPhaseFB();
						openDirectionInPhaseFB();
						modelPromtactFB();

					}
				}
			}*/

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//////////////////////////////////////////////////////////////
	//////////////// atributs MS object //////////////////////////
	//////////////////////////////////////////////////////////////
	public void typeDirectionMS() {
		iRoadObjectModel.getModel().getRoadDirectionModel().getRoadDirectionList().clear();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("port").item(0);
			port = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
			localHost = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userName").item(0);
			userName = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("password").item(0);
			password = nodePassword.getTextContent();

			Node nodeDBName = document.getElementsByTagName("dbName").item(0);
			dataBaseName = nodeDBName.getTextContent();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://" + localHost + "\\sqlexpress:" + port + ";user=" + userName + ";" + "password=" + password + ";databaseName=" + dataBaseName);
			statement = connection.createStatement();

			String url = "SELECT DirectionID, Name FROM DirectionDict";

			resultSet = statement.executeQuery(url);
			while (resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					row.add(resultSet.getString(i));
				}
				System.out.println(row);
				directionID = row.get(0);
				directionName = row.get(1);
				mapTypeDirection.put(directionID, directionName);
			}
			System.out.println("Map type direction: " + mapTypeDirection);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public void modelDirectionsMS() {
		roadDirectionList.clear();

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("port").item(0);
			port = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
			localHost = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userName").item(0);
			userName = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("password").item(0);
			password = nodePassword.getTextContent();

			Node nodeDBName = document.getElementsByTagName("dbName").item(0);
			dataBaseName = nodeDBName.getTextContent();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://" + localHost + "\\sqlexpress:" + port + ";user=" + userName + ";" + "password=" + password + ";databaseName=" + dataBaseName);
			statement = connection.createStatement();

			String url = "SELECT\n" +

					"dir.DirectionNumber,\n" + "dir.DirectionType,\n" + "dir.End_green_addit,\n" + "dir.End_green_blink,\n" + "dir.End_red,\n" + "dir.End_red_yellow,\n" + "dir.End_yellow,\n" + "dir.DirectionID\n" + "FROM\n" + "[Directions] dir\n"
					+ "INNER JOIN [ConfigurationsDB] configDB ON configDB.ConfigurationID = dir.ConfigurationID\n" + "INNER JOIN [TrafficObjects] obj ON obj.ObjectId = configDB.ControllerID\n" + "WHERE \n" + "obj.ObjectId = '"
					+ queryBySelectedObjectId + "'\n" + "ORDER BY\n" + "dir.DirectionNumber";

			resultSet = statement.executeQuery(url);
			while (resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					row.add(resultSet.getString(i));
				}
				System.out.println(row);
				RoadDirection roadDirection = new RoadDirection();
				directionNumber = row.get(0);
				directionType = mapTypeDirection.get(row.get(1));
				String numberOfType = row.get(1);
				endGreenAddit = row.get(2);
				endGreenBlink = row.get(3);
				endRed = row.get(4);
				endRedYellow = row.get(5);
				endYellow = row.get(6);
				String idDirection = row.get(7);
				TypDirection typDirection = new TypDirection(directionType);
				roadDirection.setRoadDirections_number(directionNumber);
				roadDirection.setRoadDirections_typeOfDirection(typDirection);
				roadDirection.setNumberOfTypeDirection(numberOfType);
				roadDirection.setIdDirection(idDirection);
				roadDirectionList.add(roadDirection);
				iRoadObjectModel.getModel().getRoadDirectionModel().setRoadDirectionList(roadDirectionList);
				// iRoadObjectModel.getModel().getRoadPromtactuModel().setRoadPromtactu_endGreenAddit(endGreenAddit);
				// iRoadObjectModel.getModel().getRoadPromtactuModel().setRoadPromtactu_endGreenBlink(endGreenBlink);
				// iRoadObjectModel.getModel().getRoadPromtactuModel().setRoadPromtactu_endRed(endRed);
				// iRoadObjectModel.getModel().getRoadPromtactuModel().setRoadPromtactu_endRedYellow(endRedYellow);
				// iRoadObjectModel.getModel().getRoadPromtactuModel().setRoadPromtactu_endYellow(endYellow);

			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public void modelPhaseMS() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("port").item(0);
			port = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
			localHost = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userName").item(0);
			userName = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("password").item(0);
			password = nodePassword.getTextContent();

			Node nodeDBName = document.getElementsByTagName("dbName").item(0);
			dataBaseName = nodeDBName.getTextContent();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://" + localHost + "\\sqlexpress:" + port + ";user=" + userName + ";" + "password=" + password + ";databaseName=" + dataBaseName);
			statement = connection.createStatement();

			String url = "SELECT\n" + "phConfig.Number,\n" + "phConfig.Tmin,\n" + "phConfig.PhaseId\n" + "\n" + "FROM\n" + "[Phases_Configuration] phConfig\n"
					+ "INNER JOIN [ConfigurationsDB] configDB ON configDB.ConfigurationID = phConfig.ConfigurationId\n" + "INNER JOIN [TrafficObjects] obj ON obj.ObjectId = configDB.ControllerID\n" + "WHERE \n" + "configDB.ControllerID = '"
					+ queryBySelectedObjectId + "'\n" + "ORDER BY\n" + "phConfig.Number";
			resultSet = statement.executeQuery(url);
			while (resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					row.add(resultSet.getString(i));
				}
				System.out.println(row);

				mapOfPhaseTmin = iRoadObjectModel.getModel().getRoadPhaseModel().getMapPhaseTmin();

				RoadPhase roadPhase = new RoadPhase();
				phaseNumber = row.get(0);
				phaseTmin = row.get(1);
				String idPhase = row.get(2);
				roadPhase.setIdPhase(idPhase);
				roadPhase.setRoadPhase_number(phaseNumber);
				roadPhase.setRoadPhase_Tmin(phaseTmin);
				roadPhaseList.add(roadPhase);
				mapOfPhaseTmin.put(phaseNumber, phaseTmin);
				iRoadObjectModel.getModel().getRoadPhaseModel().setRoadPhaseList(roadPhaseList);
				iRoadObjectModel.getModel().getRoadPhaseModel().setMapPhaseTmin(mapOfPhaseTmin);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public void openDirectionInPhaseMS() {

		/*Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Map<String, List<String>> mapOpenDirectionInPhase = iRoadObjectModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();*/

		/*try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("port").item(0);
			port = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
			localHost = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userName").item(0);
			userName = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("password").item(0);
			password = nodePassword.getTextContent();

			Node nodeDBName = document.getElementsByTagName("dbName").item(0);
			dataBaseName = nodeDBName.getTextContent();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://" + localHost + "\\sqlexpress:" + port + ";user=" + userName + ";" + "password=" + password + ";databaseName=" + dataBaseName);
			statement = connection.createStatement();*/

			/*
			 * String url = "SELECT phaseConfig.Number," + "directions.DirectionNumber," +
			 * "obj.Name" +
			 * 
			 * "FROM Phases_Configuration phaseConfig" +
			 * 
			 * "INNER JOIN ConfigurationsDB configDB on (configDB.ConfigurationID = phaseConfig.ConfigurationId)"
			 * + "INNER JOIN TrafficObjects obj on (obj.ObjectId = configDB.ControllerID)" +
			 * "INNER JOIN Directions directions on (directions.ConfigurationID = phaseConfig.ConfigurationId)"
			 * +
			 * "INNER JOIN DirectInPhase dirInPhase on (dirInPhase.OpenDirectoryID = directions.DirectionID and dirInPhase.PhaseID = phaseConfig.PhaseId)"
			 * +
			 * 
			 * "WHERE obj.ObjectId = '" + queryBySelectedObjectId +"'" +
			 * 
			 * "ORDER BY phaseConfig.Number, directions.DirectionNumber";
			 */

			/*String url = "select phaseConfig.Number, \n" + "\t\tdirections.DirectionNumber, \n" + "\t\tobj.Name \n" + "\n" + "\n" + "from Phases_Configuration phaseConfig\n" + "\n"
					+ "inner join ConfigurationsDB configDB on (configDB.ConfigurationID = phaseConfig.ConfigurationId)\n" + "inner join TrafficObjects obj on (obj.ObjectId = configDB.ControllerID)\n"
					+ "inner join Directions directions on (directions.ConfigurationID = phaseConfig.ConfigurationId)\n"
					//+ "inner join DirectInPhase dirInPhase on (dirInPhase.OpenDirectoryID = directions.DirectionID and dirInPhase.PhaseID = phaseConfig.PhaseId) \n" + "\n" + "/*where phaseConfig.PhaseId = '20615DF2-4786-4BDD-AC29-AA93A4CB2CB3'*///\n"
					//+ "\n" + "where obj.ObjectId = '" + queryBySelectedObjectId + "'\n" + "\n" + "order by phaseConfig.Number, directions.DirectionNumber";

			/*resultSet = statement.executeQuery(url);
			while (resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					row.add(resultSet.getString(i));
				}

				keyPhaseNumber = row.get(0);
				openDirValue = row.get(1);

				if (mapOpenDirectionInPhase.containsKey(keyPhaseNumber)) {
					mapOpenDirectionInPhase.get(keyPhaseNumber).add(openDirValue);
				} else {
					openDirInPhaseValue = new ArrayList<>();
					openDirInPhaseValue.add(openDirValue);
					mapOpenDirectionInPhase.put(keyPhaseNumber, openDirInPhaseValue);
				}

			}*/

			/*iRoadObjectModel.getModel().getRoadPhaseModel().setMapOpenDirectionInPhase(mapOpenDirectionInPhase);
			System.out.println("MAP" + mapOpenDirectionInPhase);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}*/

	}

	public void modelProgramsMS() {
		roadProgramList.clear();

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSetAllProgram = null;
		ResultSet resultSetPhaseInProgram = null;

		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("port").item(0);
			port = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
			localHost = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userName").item(0);
			userName = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("password").item(0);
			password = nodePassword.getTextContent();

			Node nodeDBName = document.getElementsByTagName("dbName").item(0);
			dataBaseName = nodeDBName.getTextContent();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://" + localHost + "\\sqlexpress:" + port + ";user=" + userName + ";" + "password=" + password + ";databaseName=" + dataBaseName);
			statement = connection.createStatement();

			String urlAllProgram = "SELECT prog.ProgNumber,\n" + "prog.ProgType,\n " + "prog.ProgramID,\n" + "prog.ProgOrigin\n" +

					"FROM [ProgramsDB] prog " +

					"WHERE prog.ControllerID = '" + queryBySelectedObjectId + "'" +

					"ORDER BY prog.ProgNumber";

			String urlPhaseInProgram = "SELECT \n" + "prog.ProgNumber,\n" + "prog.ProgType,\n" + "progPhase.PhaseNumber,\n" + "progPhase.PhaseLength,\n" + "progPhase.PhaseIndex\n" + "\n" + "FROM ProgramsDB prog\n" + "\n"
					+ "INNER JOIN TrafficObjects obj on (obj.ObjectId = prog.ControllerId)\n" + "INNER JOIN ProgPhasesDB progPhase on (progPhase.ProgramID = prog.ProgramID)\n" + "\n" + "WHERE obj.ObjectId = '" + queryBySelectedObjectId + "'" + "\n"
					+ "ORDER BY prog.ProgNumber";

			resultSetAllProgram = statement.executeQuery(urlAllProgram);
			mapOfScheduleProgram = iRoadObjectModel.getModel().getRoadProgramsModel().getMapOfScheduleProgram();
			while (resultSetAllProgram.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSetAllProgram.getMetaData().getColumnCount(); i++) {
					row.add(resultSetAllProgram.getString(i));
				}
				System.out.println("---Program row--- " + row);

				scheduleProgramsList = new LinkedList<>();

				programNumber = row.get(0);
				String type = row.get(1);
				String programId = row.get(2);
				type = type.trim();
				String progOrigin = row.get(3);

				if (type.equals("YF")) {
					type = "Желтое мигание";
				}
				if (type.equals("DP")) {
					type = "�����������";
				}

				RoadProgram roadProgram = new RoadProgram();
				ProgramMode programMode = new ProgramMode(type);

				roadProgram.setRoadProgram_number(programNumber);
				roadProgram.setRoadProgram_programMode(programMode);
				roadProgram.setIdProgram(programId);
				roadProgram.setProgOrigin(progOrigin);

				mapOfScheduleProgram.put(roadProgram, scheduleProgramsList);

				roadProgramList.add(roadProgram);
				iRoadObjectModel.getModel().getRoadProgramsModel().setRoadProgramList(roadProgramList);
			}

			///////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////// add all road programs keys in map<RoadProgram,
			/////////////////////////////////////////////////////////////////////////////////////////////////// List<PhaseInProgram>>
			/////////////////////////////////////////////////////////////////////////////////////////////////// ///////////
			////////////////// with null values ///////////
			///////////////////////////////////////////////////////////////////////////////////////////////////
			/*
			 * mapOfPhasesInProgram =
			 * iRoadObjectModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
			 * if(!roadProgramList.isEmpty()){ for(RoadProgram roadProgram :
			 * roadProgramList){ phaseInProgramList = new ArrayList<>();
			 * 
			 * mapOfPhasesInProgram.put(roadProgram, phaseInProgramList); } }
			 */
			/////////////////////////////////////////////////////////////////////////////////////////////////////

			resultSetPhaseInProgram = statement.executeQuery(urlPhaseInProgram);
			mapOfPhasesInProgram = iRoadObjectModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
			while (resultSetPhaseInProgram.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSetPhaseInProgram.getMetaData().getColumnCount(); i++) {
					row.add(resultSetPhaseInProgram.getString(i));
				}
				String programNumber = row.get(0);
				String phaseInProgramNumber = row.get(2);
				String durationPhase = row.get(3);
				String phaseIndex = row.get(4);

				if (!roadProgramList.isEmpty()) {
					for (RoadProgram roadProgram : roadProgramList) {
						if (roadProgram.getRoadProgram_number().equals(programNumber)) {
							if (mapOfPhasesInProgram.containsKey(roadProgram)) {
								System.out.println("conteins");
								PhaseInProgram phaseInProgram = new PhaseInProgram();
								PhaseNumber phaseNumber = new PhaseNumber(phaseInProgramNumber);

								phaseInProgram.setPhaseInProgramNumber(phaseNumber);
								phaseInProgram.setDurationPhaseInProgram(durationPhase);
								phaseInProgram.setPhaseIndex(phaseIndex);

								mapOfPhasesInProgram.get(roadProgram).add(phaseInProgram);

							} else {
								System.out.println("not conteins");
								phaseInProgramList = new ArrayList<>();
								PhaseInProgram phaseInProgram = new PhaseInProgram();
								PhaseNumber phaseNumber = new PhaseNumber(phaseInProgramNumber);

								phaseInProgram.setPhaseInProgramNumber(phaseNumber);
								phaseInProgram.setDurationPhaseInProgram(durationPhase);
								phaseInProgram.setPhaseIndex(phaseIndex);

								phaseInProgramList.add(phaseInProgram);

								mapOfPhasesInProgram.put(roadProgram, phaseInProgramList);
							}
						}
					}
				}
				/////////////////////////////////////////////////////////////////////////////////////////////////
				////////////////////////// add all values in map by keys
				///////////////////////////////////////////////////////////////////////////////////////////////// ////////////////////////////////////////
				/////////////////////////////////////////////////////////////////////////////////////////////////
				/*
				 * for(Map.Entry<RoadProgram, List<PhaseInProgram>> entry :
				 * mapOfPhasesInProgram.entrySet()){ RoadProgram roadProgram = entry.getKey();
				 * if(roadProgram.getRoadProgram_number().equals(programNumber)){ PhaseInProgram
				 * phaseInProgram = new PhaseInProgram(); PhaseNumber phaseNumber = new
				 * PhaseNumber(phaseInProgramNumber);
				 * 
				 * phaseInProgram.setPhaseInProgramNumber(phaseNumber);
				 * phaseInProgram.setDurationPhaseInProgram(durationPhase);
				 * 
				 * mapOfPhasesInProgram.get(roadProgram).add(phaseInProgram); } }
				 */
				///////////////////////////////////////////////////////////////////////////////////////////////////
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public void programCalendar() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {

			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("port").item(0);
			port = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
			localHost = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userName").item(0);
			userName = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("password").item(0);
			password = nodePassword.getTextContent();

			Node nodeDBName = document.getElementsByTagName("dbName").item(0);
			dataBaseName = nodeDBName.getTextContent();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://" + localHost + "\\sqlexpress:" + port + ";user=" + userName + ";" + "password=" + password + ";databaseName=" + dataBaseName);
			statement = connection.createStatement();

			String calendar = "SELECT \n" + "prog.ProgNumber,\n" + "spod.TimeID,\n" + "spod.WeekDays,\n" + "spod.StartTime,\n" + "spod.OffsetTime\n" + "\n" + "FROM TrafficObjects obj\n" + "\n"
					+ "INNER JOIN ProgramsDB prog ON (prog.ControllerId = obj.ObjectId)\n" + "INNER JOIN SPOD_TimeTable spod ON (spod.ProgramID = prog.ProgramID)\n" + "\n" + "WHERE obj.ObjectId = '" + queryBySelectedObjectId + "'" + "\n"
					+ "ORDER BY prog.ProgNumber";

			resultSet = statement.executeQuery(calendar);
			mapOfWeekCalendar = iRoadObjectModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();
			for (int i = 1; i <= 7; i++) {
				CheckBox checkBox;
				if (i == 1) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "�����������");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 2) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "�������");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 3) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "�����");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 4) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "�������");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 5) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "�������");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 6) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "�������");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 7) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "�����������");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
			}

			while (resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					row.add(resultSet.getString(i));
				}

				String programNumber = row.get(0);
				String weekDay = row.get(2);
				String startTime = row.get(3);
				String offsetTime = row.get(4);
				String timeId = row.get(1);

				if (weekDay.equals("Monday")) {
					weekDay = "�����������";
				}
				if (weekDay.equals("Tuesday")) {
					weekDay = "�������";
				}
				if (weekDay.equals("Wednesday")) {
					weekDay = "�����";
				}
				if (weekDay.equals("Thursday")) {
					weekDay = "�������";
				}
				if (weekDay.equals("Friday")) {
					weekDay = "�������";
				}
				if (weekDay.equals("Saturday")) {
					weekDay = "�������";
				}
				if (weekDay.equals("Sunday")) {
					weekDay = "�����������";
				}

				if (startTime.contains(".")) {
					startTime = startTime.substring(0, startTime.indexOf("."));
				}

				for (Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekCalendar.entrySet()) {
					String day = entry.getKey().getWeekDay().getText();
					if (day.equals(weekDay)) {
						ScheduleProgram scheduleProgram = new ScheduleProgram();
						ScheduleNumber scheduleNumber = new ScheduleNumber(programNumber);
						scheduleProgram.setNumberOfScheduleProgram(scheduleNumber);
						scheduleProgram.setTimeONOfScheduleProgram(startTime);
						scheduleProgram.setDisplacementTimeOfScheduleProgram(offsetTime);
						scheduleProgram.setScheduleProgramId(timeId);
						entry.getValue().add(scheduleProgram);
					}
				}

			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}

	public void modelConflictsMS() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("port").item(0);
			port = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
			localHost = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userName").item(0);
			userName = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("password").item(0);
			password = nodePassword.getTextContent();

			Node nodeDBName = document.getElementsByTagName("dbName").item(0);
			dataBaseName = nodeDBName.getTextContent();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://" + localHost + "\\sqlexpress:" + port + ";user=" + userName + ";" + "password=" + password + ";databaseName=" + dataBaseName);
			statement = connection.createStatement();

			String url = "SELECT conflict.ConflictID,\n" + "conflict.DirectionID,\n" + "conflict.Conflict_withID,\n" + "directions2.DirectionNumber AS CurentDirectionNumber,\n" + "directions1.DirectionNumber AS Conflict_DirectionNumber\n"
					+ "FROM CONFLICTS conflict\n" + "INNER JOIN Directions directions1 on (conflict.Conflict_withID = directions1.DirectionID)\n" + "LEFT JOIN Directions directions2 on (conflict.DirectionID = directions2.DirectionID)\n"
					+ "INNER JOIN ConfigurationsDB config on (config.ConfigurationID = directions1.ConfigurationID)\n" + "INNER JOIN TrafficObjects object on (object.ObjectId = config.ControllerID)\n" + "WHERE object.ObjectId = '"
					+ queryBySelectedObjectId + "'\n" + "ORDER BY directions2.DirectionNumber";

			resultSet = statement.executeQuery(url);
			while (resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					row.add(resultSet.getString(i));
				}

				conflictForDir = row.get(3);
				conflictWithDir = row.get(4);

				map = iRoadObjectModel.getModel().getRoadConflictsModel().getMapOfConflict();

				if (map.containsKey(conflictForDir)) {
					conflictWithDirection = new ConflictWithDirection();
					conflictWithDirection.setConflictWithDirection(conflictWithDir);
					map.get(conflictForDir).add(conflictWithDirection);
				} else {
					conflictForDirection = new ConflictForDirection();
					conflictWithDirection = new ConflictWithDirection();

					conflictForDirection.setRoadConflict_directionNumber(conflictForDir);
					conflictWithDirection.setConflictWithDirection(conflictWithDir);

					conflictForDirectionList.add(conflictForDirection);
					conflictWithDirectionList.add(conflictWithDirection);

					conflictList = new ArrayList<>();
					conflictList.add(conflictWithDirection);

					map.put(conflictForDir, conflictList);
				}
			}
			iRoadObjectModel.getModel().getRoadConflictsModel().setConflictForDirectionList(conflictForDirectionList);
			iRoadObjectModel.getModel().getRoadConflictsModel().setConflictWithDirectionList(conflictWithDirectionList);
			// iRoadObjectModel.getModel().getRoadConflictsModel().setMapOfConflict(map);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public void modelPromtactMS() {
		interPhaseMap = iRoadObjectModel.getModel().getRoadPromtactuModel().getInterPhaseMap();
		basicMapInterphaseSpecificPromtact = iRoadObjectModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		basicPromtactDataMap = iRoadObjectModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();
		InterphaseTransitionsHBoxCell interphaseTransitionsHBoxCell = null;

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("port").item(0);
			port = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
			localHost = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userName").item(0);
			userName = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("password").item(0);
			password = nodePassword.getTextContent();

			Node nodeDBName = document.getElementsByTagName("dbName").item(0);
			dataBaseName = nodeDBName.getTextContent();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://" + localHost + "\\sqlexpress:" + port + ";user=" + userName + ";" + "password=" + password + ";databaseName=" + dataBaseName);
			statement = connection.createStatement();

			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////// QUERY SPECIFIC PROMTACT THE BASE
			//////////////////////////////////////////////////////////////////////////////////////////////////////////// /////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			String specificPromtactQueryTheDatabase = "SELECT \n" + "transit.From_phase_ID,\n" + "transit.To_phase_ID,\n" + "phase1.Number AS FromPhase,\n" + "phase2.Number AS ToPhase,\n" + "dir.DirectionNumber,\n" + "specProm.End_Green_Addit,\n"
					+ "specProm.End_Green_Blink,\n" + "specProm.End_Red,\n" + "specProm.End_Red_Yellow,\n" + "specProm.End_Yellow,\n" + "specProm.PromtaktID,\n" + "transit.DirectInTransitID\n" + "\n" + "FROM DirectInTransit transit\n" + "\n"
					+ "INNER JOIN Phases_Configuration phase1 on (phase1.PhaseId = transit.From_phase_ID)\n" + "LEFT JOIN Phases_Configuration phase2 on (phase2.PhaseId = transit.To_phase_ID)\n"
					+ "INNER JOIN ConfigurationsDB config on (config.ConfigurationID = phase1.ConfigurationId)\n" + "INNER JOIN TrafficObjects obj on (obj.ObjectId = config.ControllerID)\n"
					+ "INNER JOIN Promtakt specProm on (specProm.DirectInTransitID = transit.DirectInTransitID)\n" + "INNER JOIN Directions dir on (dir.DirectionID = transit.DirectionID)\n" + "\n" + "WHERE obj.ObjectId = '" + queryBySelectedObjectId
					+ "'\n" + "\n" + "GROUP BY transit.From_phase_ID, transit.To_phase_ID, phase1.Number, phase2.Number,\n" + "dir.DirectionNumber, specProm.End_Green_Addit,\n"
					+ "specProm.End_Green_Blink, specProm.End_Red, specProm.End_Red_Yellow, specProm.End_Yellow, specProm.PromtaktID, transit.DirectInTransitID\n" + "ORDER BY phase1.Number";

			resultSet = statement.executeQuery(specificPromtactQueryTheDatabase);
			while (resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					row.add(resultSet.getString(i));
				}

				String fromPhaseId = row.get(0);
				String toPhaseId = row.get(1);
				fromPhase = row.get(2);
				toPhase = row.get(3);
				String numberDirectionInterphase = row.get(4);
				String roadPromtactu_endGreenAddit = row.get(5);
				String roadPromtactu_durationGreenBlink = row.get(6);
				String roadPromtactu_durationYellow = row.get(7);
				String roadPromtactu_endRed = row.get(8);
				String roadPromtactu_durationRedYellow = row.get(9);
				String promtactId = row.get(10);
				String dirInTransitId = row.get(11);

				//Map<String, List<String>> mapOfOpenDirInPhase = iRoadObjectModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
				PromtactData promtactData = null;

				/*for (Map.Entry<String, List<String>> entryOpenDir : mapOfOpenDirInPhase.entrySet()) {
					String keyPhaseFrom = entryOpenDir.getKey();
					if (keyPhaseFrom.equals(fromPhase)) {
						List<String> openDirectionsList = mapOfOpenDirInPhase.get(keyPhaseFrom);
						for (String direction : openDirectionsList) {
							if (direction.equals(numberDirectionInterphase)) {
								promtactData = new PromtactData();
								promtactData.setPromtactId(promtactId);
								promtactData.setRoadPromtactu_endGreenAddit(roadPromtactu_endGreenAddit);
								promtactData.setRoadPromtactu_durationGreenBlink(roadPromtactu_durationGreenBlink);
								promtactData.setRoadPromtactu_durationYellow(roadPromtactu_durationYellow);
								promtactData.setRoadPromtactu_endRed(new Integer(0).toString());
								promtactData.setRoadPromtactu_durationRedYellow(new Integer(0).toString());
								promtactData.setRoadPromtactu_fromPhaseId(fromPhaseId);
								promtactData.setRoadPromtactu_toPhaseId(toPhaseId);
								promtactData.setRoadPromtactu_directInTransitId(dirInTransitId);

							}
						}
					} else {
						String keyPhaseTo = entryOpenDir.getKey();
						if (keyPhaseTo.equals(toPhase)) {
							List<String> openDirectionsList = mapOfOpenDirInPhase.get(keyPhaseTo);
							for (String direction : openDirectionsList) {
								if (direction.equals(numberDirectionInterphase)) {
									promtactData = new PromtactData();
									promtactData.setPromtactId(promtactId);
									promtactData.setRoadPromtactu_endGreenAddit(new Integer(0).toString());
									promtactData.setRoadPromtactu_durationGreenBlink(new Integer(0).toString());
									promtactData.setRoadPromtactu_durationYellow(new Integer(0).toString());
									promtactData.setRoadPromtactu_endRed(roadPromtactu_endRed);
									promtactData.setRoadPromtactu_durationRedYellow(roadPromtactu_durationRedYellow);
									promtactData.setRoadPromtactu_fromPhaseId(fromPhaseId);
									promtactData.setRoadPromtactu_toPhaseId(toPhaseId);
									promtactData.setRoadPromtactu_directInTransitId(dirInTransitId);
								}
							}
						}
					}
				}*/

				if (!basicMapInterphaseSpecificPromtact.isEmpty()) {
					boolean needNewInter = true;

					for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : basicMapInterphaseSpecificPromtact.entrySet()) {
						InterphaseTransitionsHBoxCell existedInterphase = entry.getKey();
						String from = existedInterphase.getComboBoxFromPhase().getValue();
						String to = existedInterphase.getComboBoxToPhase().getValue();

						if (from.equals(fromPhase) & to.equals(toPhase)) {
							Map<String, PromtactData> promDataMap = basicMapInterphaseSpecificPromtact.get(existedInterphase);
							promDataMap.put(numberDirectionInterphase, promtactData);
							needNewInter = false;
							break;
						}
					}
					if (needNewInter) {
						specificPromtactDataMap = new LinkedHashMap<>();

						interphaseTransitionsHBoxCell = new InterphaseTransitionsHBoxCell();
						interphaseTransitionsHBoxCell.getComboBoxFromPhase().setValue(fromPhase);
						interphaseTransitionsHBoxCell.getComboBoxToPhase().setValue(toPhase);

						specificPromtactDataMap.put(numberDirectionInterphase, promtactData);
						basicMapInterphaseSpecificPromtact.put(interphaseTransitionsHBoxCell, specificPromtactDataMap);
					}
				} else {
					specificPromtactDataMap = new LinkedHashMap<>();

					interphaseTransitionsHBoxCell = new InterphaseTransitionsHBoxCell();
					interphaseTransitionsHBoxCell.getComboBoxFromPhase().setValue(fromPhase);
					interphaseTransitionsHBoxCell.getComboBoxToPhase().setValue(toPhase);

					specificPromtactDataMap.put(numberDirectionInterphase, promtactData);
					basicMapInterphaseSpecificPromtact.put(interphaseTransitionsHBoxCell, specificPromtactDataMap);
				}
			}
			resultSet.close();
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////

			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////// QUERY THE BASE PROMTACT THE DATDBASE
			//////////////////////////////////////////////////////////////////////////////////////////////////////////// /////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			String basePromtactQueryTheBase = "SELECT\n" + "\n" + "dir.DirectionNumber,\n" + "dir.End_green_addit,\n" + "dir.End_green_blink,\n" + "dir.End_red,\n" + "dir.End_red_yellow,\n" + "dir.End_yellow\n" + "\n" + "FROM Directions dir\n" + "\n"
					+ "INNER JOIN ConfigurationsDB config on (config.ConfigurationID = dir.ConfigurationID)\n" + "INNER JOIN TrafficObjects obj on (obj.ObjectId = config.ControllerID)\n" + "\n" + "WHERE obj.ObjectId = '" + queryBySelectedObjectId
					+ "'\n" + "\n" + "ORDER BY dir.DirectionNumber";

			resultSet = statement.executeQuery(basePromtactQueryTheBase);
			while (resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					row.add(resultSet.getString(i));
				}

				String numberDirection = row.get(0);
				String roadPromtactu_endGreenAddit = row.get(1);
				String roadPromtactu_durationGreenBlink = row.get(2);
				String roadPromtactu_endRed = row.get(3);
				String roadPromtactu_durationRedYellow = row.get(4);
				String roadPromtactu_durationYellow = row.get(5);

				UUID uuid = UUID.randomUUID();
				String basicPromtactId = uuid.toString();

				PromtactData promtactData = new PromtactData();
				promtactData.setPromtactId(basicPromtactId);
				promtactData.setRoadPromtactu_directionNumber(numberDirection);
				promtactData.setRoadPromtactu_endGreenAddit(roadPromtactu_endGreenAddit);
				promtactData.setRoadPromtactu_durationGreenBlink(roadPromtactu_durationGreenBlink);
				promtactData.setRoadPromtactu_durationYellow(roadPromtactu_durationYellow);
				promtactData.setRoadPromtactu_endRed(roadPromtactu_endRed);
				promtactData.setRoadPromtactu_durationRedYellow(roadPromtactu_durationRedYellow);

				basicPromtactDataMap.put(numberDirection, promtactData);
			}
			// System.out.println("--- Base promtact data map " + basicPromtactDataMap);

			////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// System.out.println(basicMapInterphaseSpecificPromtact);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	

	public void modelIPMS() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("port").item(0);
			port = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
			localHost = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userName").item(0);
			userName = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("password").item(0);
			password = nodePassword.getTextContent();

			Node nodeDBName = document.getElementsByTagName("dbName").item(0);
			dataBaseName = nodeDBName.getTextContent();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://" + localHost + "\\sqlexpress:" + port + ";user=" + userName + ";" + "password=" + password + ";databaseName=" + dataBaseName);
			statement = connection.createStatement();
			/*
			 * String url = "SELECT\n" + "ip.IpAddress\n" + "\n" +
			 * "FROM [TrafficObjects] obj INNER JOIN\n" +
			 * "[EthernetConnections] ip ON obj.ObjectId = ip.EthernetConnectionId \n" +
			 * "WHERE obj.Name = '" + employeeName + "'";
			 */
			String url = "SELECT\n" + "                    ip.IpAddress,\n" + "					obj.ConModId,\n" + "					conect.ConnectionTypeName,\n" + "					config.db_id_object,\n"
					+ "					config.db_name_object,\n" + "					config.db_number_object,\n" + "					config.db_path,\n" + "					config.db_path_options,\n"
					+ "					config.Configuration_datetime_loaded,\n" + "					obj.ObjectId,\n" + "					obj.HighWayId,\n" + "					obj.LastUpdateTime\n" + "                    \n"
					+ "                    FROM [TrafficObjects] obj \n" + "\n" + "					INNER JOIN[EthernetConnections] ip ON obj.ObjectId = ip.EthernetConnectionId\n"
					+ "					INNER JOIN ConnectionTypesDict conect ON conect.ConnectionTypeId = obj.ConnectionType\n" + "					INNER JOIN ConfigurationsDB config ON config.ControllerID = obj.ObjectId \n" + "\n"
					+ "                    WHERE obj.Name = '" + employeeName + "'";

			resultSet = statement.executeQuery(url);
			while (resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					row.add(resultSet.getString(i));
				}
				System.out.println(row);
				roadModelIP = row.get(0);
				String conModId = row.get(1);
				String connectionTypeName = row.get(2);
				String db_id_object = row.get(3);
				String db_name_object = row.get(4);
				String db_number_object = row.get(5);
				String db_path = row.get(6);
				String db_path_options = row.get(7);
				String configuration_datetime_loaded = row.get(8);
				String objectId = row.get(9);
				String hwId = row.get(10);
				String lastUpdateTime = row.get(11);

				this.iRoadObjectModel.getModel().getRoadModelSettings().setModelIP(roadModelIP);
				this.iRoadObjectModel.getModel().getRoadModelSettings().setConModId(conModId);
				this.iRoadObjectModel.getModel().getRoadModelSettings().setConnectionTypeName(connectionTypeName);
				this.iRoadObjectModel.getModel().getRoadModelSettings().setDb_id_object(db_id_object);
				this.iRoadObjectModel.getModel().getRoadModelSettings().setDb_name_object(db_name_object);
				this.iRoadObjectModel.getModel().getRoadModelSettings().setDb_number_object(db_number_object);
				this.iRoadObjectModel.getModel().getRoadModelSettings().setDb_path(db_path);
				this.iRoadObjectModel.getModel().getRoadModelSettings().setDb_path_options(db_path_options);
				this.iRoadObjectModel.getModel().getRoadModelSettings().setConfiguration_datetime_loaded(configuration_datetime_loaded);
				this.iRoadObjectModel.getModel().getRoadModelSettings().setObjectId(objectId);
				this.iRoadObjectModel.getModel().getRoadModelSettings().setHwId(hwId);
				this.iRoadObjectModel.getModel().getRoadModelSettings().setLastUpdateTime(lastUpdateTime);

				System.out.println("RoadModel IP - " + roadModelIP);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public void modelAtributsMS() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("port").item(0);
			port = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
			localHost = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userName").item(0);
			userName = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("password").item(0);
			password = nodePassword.getTextContent();

			Node nodeDBName = document.getElementsByTagName("dbName").item(0);
			dataBaseName = nodeDBName.getTextContent();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://" + localHost + "\\sqlexpress:" + port + ";user=" + userName + ";" + "password=" + password + ";databaseName=" + dataBaseName);
			statement = connection.createStatement();

			String url = "SELECT obj.Number, obj.Address FROM [TrafficObjects] obj WHERE obj.Name = '" + employeeName + "'";
			resultSet = statement.executeQuery(url);
			while (resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					row.add(resultSet.getString(i));
				}
				System.out.println(row);
				roadModelIndexNumber = row.get(0);
				roadModelAddress = row.get(1);
				this.iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectObjectNumber(roadModelIndexNumber);
				this.iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectNetworkAddress(roadModelAddress);
				System.out.println("RoadModel Index number " + roadModelIndexNumber);
				System.out.println("RoadModel Address " + roadModelAddress);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}

	public void dbQueryMS() {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {

			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("port").item(0);
			port = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
			localHost = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userName").item(0);
			userName = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("password").item(0);
			password = nodePassword.getTextContent();

			Node nodeDBName = document.getElementsByTagName("dbName").item(0);
			dataBaseName = nodeDBName.getTextContent();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection("jdbc:sqlserver://" + localHost + "\\sqlexpress:" + port + ";user=" + userName + ";" + "password=" + password + ";databaseName=" + dataBaseName);
			statement = connection.createStatement();
			/*
			 * String url =
			 * "SELECT TOP 1000 obj.ObjectId, obj.Name, pr.ProgNumber, cont.Number AS Phase, "
			 * + "Tmin, PhaseLength, pr.ProgType, cont.PhaseId, cont.ControllerId FROM " +
			 * "Phases_Configuration cont INNER JOIN TrafficObjects obj ON (ControllerId = ObjectId) "
			 * + "INNER JOIN dbo.ProgramsDB pr ON (pr.ControllerId = cont.ControllerId) " +
			 * "INNER JOIN dbo.ProgPhasesDB prPhase ON (prPhase.ProgramID = pr.ProgramID) "
			 * + "WHERE obj.ObjectId='" + queryBySelectedObjectId + "'" +
			 * "ORDER BY obj.ObjectId, pr.ProgNumber, cont.Number";
			 */
			String url = "SELECT TOP 1000 obj.ObjectId," + "obj.Name, pr.ProgNumber, phase_cont.Number AS Phase," + "Tmin, pr.ProgType, phase_cont.PhaseId, phase_cont.ConfigurationId " + "FROM ConfigurationsDB config "
					+ "INNER JOIN TrafficObjects obj ON config.ControllerID = obj.ObjectId " + "INNER JOIN Phases_Configuration phase_cont ON phase_cont.ConfigurationId = config.ConfigurationID "
					+ "INNER JOIN ProgramsDB pr ON pr.ControllerId = config.ControllerID " + "WHERE obj.ObjectId='" + queryBySelectedObjectId + "'" + "ORDER BY obj.ObjectId, pr.ProgNumber";
			resultSet = statement.executeQuery(url);
			while (resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					row.add(resultSet.getString(i));
				}
				System.out.println("Employee row: " + row);
				employeeName = row.get(1);
				configurationId = row.get(7);
				employee = new Employee();
				employee.setObjectName(employeeName);
				System.out.println("Employee Name - " + employeeName);

			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	//////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////
	//////////////// atribute FB object //////////////////////////
	//////////////////////////////////////////////////////////////
	public void modelObject() {
		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("portToFBDB").item(0);
			portFB = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("pathToFBDB").item(0);
			pathFB = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userFBDB").item(0);
			userFB = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("passwordFBDB").item(0);
			passwordFB = nodePassword.getTextContent();

			Class.forName("org.firebirdsql.jdbc.FBDriver");
			Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/" + portFB + ":" + pathFB, userFB, passwordFB);
			Statement stm = con.createStatement();
			ResultSet res = stm.executeQuery("select\n" + "obj.number,\n" + "obj.d_create,\n" + "obj.d_start,\n" + "obj.fio_tech,\n" + "obj.trough,\n" + "obj.net_adr,\n" + "obj.note\n" + "\n" + "\n" + "from object obj\n" + "\n"
					+ "where obj.id_object = '" + queryBySelectedObjectId + "'");
			while (res.next()) {
				String objNumber = res.getString(1);
				String objDateCreate = res.getString(2);
				String objD_start = res.getString(3);
				String objFio_tech = res.getString(4);
				if (objFio_tech == null) {
					objFio_tech = "";
				}
				String objTrough = res.getString(5);
				String objNet_adr = res.getString(6);
				String objNote = res.getString(7);

				this.iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectObjectNumber(objNumber);
				this.iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectDateOfCreation(objDateCreate);
				this.iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectLaunchDate(objD_start);
				this.iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectTechnologist(objFio_tech);
				this.iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectRegion(objTrough);
				this.iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectNetworkAddress(objNet_adr);
				this.iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectNote(objNote);
				txtObjectNumber.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectObjectNumber());
				txtTechnologist.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectTechnologist());
				txtNetworkAddress.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectNetworkAddress());
				txtObjectNumber.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectObjectNumber());
				txtObjectNumber.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectObjectNumber());
				txtAreaNote.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectNote());
				if (iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectDateOfCreation().equals("")) {
					System.out.println("Date of creation is null");
				} else {
					String date = iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectDateOfCreation();
					LocalDate localDate = LocalDate.parse(date);
					dPdateOfCreation.setValue(localDate);
				}
				if (iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectLaunchDate().equals("")) {
					System.out.println("Launch date is null");
				} else {
					String date = iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectLaunchDate();
					LocalDate localDate = LocalDate.parse(date);
					dPlaunchDate.setValue(localDate);
				}

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void typeDirectionFB() {
		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("portToFBDB").item(0);
			portFB = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("pathToFBDB").item(0);
			pathFB = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userFBDB").item(0);
			userFB = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("passwordFBDB").item(0);
			passwordFB = nodePassword.getTextContent();

			Class.forName("org.firebirdsql.jdbc.FBDriver");
			Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/" + portFB + ":" + pathFB, userFB, passwordFB);
			Statement stm = con.createStatement();
			ResultSet res = stm.executeQuery("SELECT\n" + "dir.number,\n" + "dirType.name\n" + "\n" + "FROM directions dir\n" + "\n" + "INNER JOIN object obj ON (obj.id_object = dir.fk_object)\n"
					+ "INNER JOIN direction_dict dirType ON (dirType.id_type_direction = dir.type_direction)\n" + "\n" + "WHERE obj.id_object = '" + queryBySelectedObjectId + "'");
			while (res.next()) {
				directionID = res.getString(1);
				directionName = res.getString(2);
				mapTypeDirection.put(directionID, directionName);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void modelDirectionFB() {
		roadDirectionList.clear();

		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("portToFBDB").item(0);
			portFB = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("pathToFBDB").item(0);
			pathFB = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userFBDB").item(0);
			userFB = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("passwordFBDB").item(0);
			passwordFB = nodePassword.getTextContent();

			Class.forName("org.firebirdsql.jdbc.FBDriver");
			Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/" + portFB + ":" + pathFB, userFB, passwordFB);
			Statement stm = con.createStatement();
			ResultSet res = stm.executeQuery("SELECT\n" + "dir.number,\n" + "dirType.name\n" + "\n" + "FROM directions dir\n" + "\n" + "INNER JOIN object obj ON (obj.id_object = dir.fk_object)\n"
					+ "INNER JOIN direction_dict dirType ON (dirType.id_type_direction = dir.type_direction)\n" + "\n" + "WHERE obj.id_object = '" + queryBySelectedObjectId + "'");
			while (res.next()) {
				RoadDirection roadDirection = new RoadDirection();
				directionNumber = res.getString(1);
				directionType = mapTypeDirection.get(res.getString(1));

				TypDirection typDirection = new TypDirection(directionType);

				roadDirection.setRoadDirections_number(directionNumber);
				roadDirection.setRoadDirections_typeOfDirection(typDirection);

				roadDirectionList.add(roadDirection);
				iRoadObjectModel.getModel().getRoadDirectionModel().setRoadDirectionList(roadDirectionList);

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void modelPhaseFB() {
		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("portToFBDB").item(0);
			portFB = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("pathToFBDB").item(0);
			pathFB = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userFBDB").item(0);
			userFB = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("passwordFBDB").item(0);
			passwordFB = nodePassword.getTextContent();

			Class.forName("org.firebirdsql.jdbc.FBDriver");
			Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/" + portFB + ":" + pathFB, userFB, passwordFB);
			Statement stm = con.createStatement();
			ResultSet res = stm.executeQuery(
					"SELECT\n" + "phase.number,\n" + "phase.tmin\n" + "\n" + "FROM phas_list phase\n" + "\n" + "INNER JOIN object obj ON (obj.id_object = phase.fk_object)\n" + "\n" + "WHERE obj.id_object = '" + queryBySelectedObjectId + "'");
			while (res.next()) {
				mapOfPhaseTmin = iRoadObjectModel.getModel().getRoadPhaseModel().getMapPhaseTmin();

				RoadPhase roadPhase = new RoadPhase();

				phaseNumber = res.getString(1);
				phaseTmin = res.getString(2);

				roadPhase.setRoadPhase_number(phaseNumber);
				roadPhase.setRoadPhase_Tmin(phaseTmin);
				roadPhaseList.add(roadPhase);
				mapOfPhaseTmin.put(phaseNumber, phaseTmin);
				iRoadObjectModel.getModel().getRoadPhaseModel().setRoadPhaseList(roadPhaseList);
				iRoadObjectModel.getModel().getRoadPhaseModel().setMapPhaseTmin(mapOfPhaseTmin);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void openDirectionInPhaseFB() {
		/*Map<String, List<String>> mapOpenDirectionInPhase = iRoadObjectModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("portToFBDB").item(0);
			portFB = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("pathToFBDB").item(0);
			pathFB = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userFBDB").item(0);
			userFB = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("passwordFBDB").item(0);
			passwordFB = nodePassword.getTextContent();

			Class.forName("org.firebirdsql.jdbc.FBDriver");
			Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/" + portFB + ":" + pathFB, userFB, passwordFB);
			Statement stm = con.createStatement();
			ResultSet res = stm.executeQuery("SELECT\n" + "phase.number AS Phase_Number,\n" + "phase.tmin,\n" + "dir.number AS openDirection\n" + "\n" + "FROM phas_list phase\n" + "\n" + "INNER JOIN object obj ON (obj.id_object = phase.fk_object)\n"
					+ "INNER JOIN directions dir ON (dir.fk_object = obj.id_object)\n" + "INNER JOIN direct_in_phas dirInPhase ON (dirInPhase.open_direct = dir.id_direction\n" + "                            AND dirInPhase.fk_phas = phase.id_phas)\n"
					+ "\n" + "WHERE obj.id_object = '" + queryBySelectedObjectId + "'" + "\n" + "ORDER BY phase.number");
			while (res.next()) {
				keyPhaseNumber = res.getString(1);
				openDirValue = res.getString(3);

				if (mapOpenDirectionInPhase.containsKey(keyPhaseNumber)) {
					mapOpenDirectionInPhase.get(keyPhaseNumber).add(openDirValue);
				} else {
					openDirInPhaseValue = new ArrayList<>();
					openDirInPhaseValue.add(openDirValue);
					mapOpenDirectionInPhase.put(keyPhaseNumber, openDirInPhaseValue);
				}
			}

			iRoadObjectModel.getModel().getRoadPhaseModel().setMapOpenDirectionInPhase(mapOpenDirectionInPhase);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}

	public void modelProgramsFB() {
		roadProgramList.clear();

		String mode = null;
		String reserve = null;
		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("portToFBDB").item(0);
			portFB = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("pathToFBDB").item(0);
			pathFB = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userFBDB").item(0);
			userFB = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("passwordFBDB").item(0);
			passwordFB = nodePassword.getTextContent();

			Class.forName("org.firebirdsql.jdbc.FBDriver");
			Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/" + portFB + ":" + pathFB, userFB, passwordFB);
			Statement stm = con.createStatement();
			ResultSet resProgram = stm.executeQuery("SELECT\n" + "obj.number AS obj_Number,\n" + "obj.name,\n" + "prog.number AS prog_Number,\n" + "progMode.val,\n" + "progReserve.val\n" + "\n" + "FROM object obj\n"
					+ "INNER JOIN programs prog ON (obj.id_object = prog.fk_object)\n" + "INNER JOIN lookup_mode progMode ON (progMode.id_lookup_mode = prog.mode)\n"
					+ "INNER JOIN lookup_type_reserve progReserve ON (progReserve.id_lookup_type_reserve = prog.type_reserve)\n" + "\n" + "WHERE obj.id_object = '" + queryBySelectedObjectId + "'");

			while (resProgram.next()) {
				RoadProgram roadProgram = new RoadProgram();
				mode = resProgram.getString(4);
				reserve = resProgram.getString(5);
				ProgramMode programMode = new ProgramMode(mode);
				BackupProgram backupProgram = new BackupProgram(reserve);
				programNumber = resProgram.getString(3);
				roadProgram.setRoadProgram_number(programNumber);
				roadProgram.setRoadProgram_programMode(programMode);
				roadProgram.setRoadProgram_backupProgram(backupProgram);
				roadProgramList.add(roadProgram);
				iRoadObjectModel.getModel().getRoadProgramsModel().setRoadProgramList(roadProgramList);

			}

			///////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////// add all road programs keys in map<RoadProgram,
			/////////////////////////////////////////////////////////////////////////////////////////////////// List<PhaseInProgram>>
			/////////////////////////////////////////////////////////////////////////////////////////////////// ///////////
			////////////////// with null values ///////////
			///////////////////////////////////////////////////////////////////////////////////////////////////
			mapOfPhasesInProgram = iRoadObjectModel.getModel().getRoadProgramsModel().getMapOfPhasesInProgram();
			if (!roadProgramList.isEmpty()) {
				for (RoadProgram roadProgram : roadProgramList) {
					phaseInProgramList = new ArrayList<>();

					mapOfPhasesInProgram.put(roadProgram, phaseInProgramList);
				}
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////

			ResultSet resPhaseInProgram = stm.executeQuery("SELECT\n" + "obj.number AS obj_Number,\n" + "obj.name,\n" + "prog.number AS prog_Number,\n" + "phase.number AS phase_Number,\n" + "phaseInProg.delay\n" + "\n" + "FROM object obj\n"
					+ "INNER JOIN programs prog ON (obj.id_object = prog.fk_object)\n" + "INNER JOIN phas_in_program phaseInProg ON (phaseInProg.fk_program = prog.id_program)\n"
					+ "INNER JOIN phas_list phase ON (phase.id_phas = phaseInProg.work_phas)\n" + "\n" + "WHERE obj.id_object = '" + queryBySelectedObjectId + "'");

			while (resPhaseInProgram.next()) {
				String programNumber = resPhaseInProgram.getString(3);
				String phaseInProgramNumber = resPhaseInProgram.getString(4);
				String durationPhase = resPhaseInProgram.getString(5);

				/////////////////////////////////////////////////////////////////////////////////////////////////
				////////////////////////// add all values in map by keys
				///////////////////////////////////////////////////////////////////////////////////////////////// ////////////////////////////////////////
				/////////////////////////////////////////////////////////////////////////////////////////////////
				for (Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {
					RoadProgram roadProgram = entry.getKey();
					if (roadProgram.getRoadProgram_number().equals(programNumber)) {
						PhaseInProgram phaseInProgram = new PhaseInProgram();
						PhaseNumber phaseNumber = new PhaseNumber(phaseInProgramNumber);

						phaseInProgram.setPhaseInProgramNumber(phaseNumber);
						phaseInProgram.setDurationPhaseInProgram(durationPhase);

						mapOfPhasesInProgram.get(roadProgram).add(phaseInProgram);
					}
				}
				///////////////////////////////////////////////////////////////////////////////////////////////////
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void modelCalendarFB() {
		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("portToFBDB").item(0);
			portFB = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("pathToFBDB").item(0);
			pathFB = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userFBDB").item(0);
			userFB = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("passwordFBDB").item(0);
			passwordFB = nodePassword.getTextContent();

			Class.forName("org.firebirdsql.jdbc.FBDriver");
			Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/" + portFB + ":" + pathFB, userFB, passwordFB);
			Statement stm = con.createStatement();
			ResultSet res = stm.executeQuery("SELECT\n" + "obj.name,\n" + "prog.number,\n" + "calendar.day_of_week,\n" + "calendar.time_start,\n" + "calendar.time_offset\n" + "\n" + "FROM object obj\n" + "\n"
					+ "INNER JOIN programs prog ON (prog.fk_object = obj.id_object)\n" + "INNER JOIN time_table calendar ON (calendar.work_program = prog.id_program)\n" + "\n" + "WHERE obj.id_object = '" + queryBySelectedObjectId + "'");

			mapOfWeekCalendar = iRoadObjectModel.getModel().getRoadProgramsModel().getMapOfWeekCalendar();
			for (int i = 1; i <= 7; i++) {
				CheckBox checkBox;
				if (i == 1) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Понедельник");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 2) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Вторник");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 3) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Среда");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 4) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Четверг");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 5) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Пятница");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 6) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Суббота");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
				if (i == 7) {
					checkBox = new CheckBox();
					scheduleProgramsList = new LinkedList<>();
					ScheduleCalendarWeekDayHBoxCell scheduleCalendarWeekDayHBoxCell = new ScheduleCalendarWeekDayHBoxCell(checkBox, "Воскресенье");
					mapOfWeekCalendar.put(scheduleCalendarWeekDayHBoxCell, scheduleProgramsList);
				}
			}

			while (res.next()) {
				String programNumber = res.getString(2);
				String weekDay = res.getString(3);
				String startTime = res.getString(4);
				String offsetTime = res.getString(5);

				if (weekDay.equals("0")) {
					weekDay = "Понедельник";
				}
				if (weekDay.equals("1")) {
					weekDay = "Вторник";
				}
				if (weekDay.equals("2")) {
					weekDay = "Среда";
				}
				if (weekDay.equals("3")) {
					weekDay = "Четверг";
				}
				if (weekDay.equals("4")) {
					weekDay = "Пятница";
				}
				if (weekDay.equals("5")) {
					weekDay = "Суббота";
				}
				if (weekDay.equals("6")) {
					weekDay = "Воскресенье";
				}

				if (startTime.contains(".")) {
					startTime = startTime.substring(0, startTime.indexOf("."));
				}

				for (Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekCalendar.entrySet()) {
					String day = entry.getKey().getWeekDay().getText();
					if (day.equals(weekDay)) {
						ScheduleProgram scheduleProgram = new ScheduleProgram();
						ScheduleNumber scheduleNumber = new ScheduleNumber(programNumber);
						scheduleProgram.setNumberOfScheduleProgram(scheduleNumber);
						scheduleProgram.setTimeONOfScheduleProgram(startTime);
						scheduleProgram.setDisplacementTimeOfScheduleProgram(offsetTime);
						entry.getValue().add(scheduleProgram);
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void modelConflictsFB() {
		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("portToFBDB").item(0);
			portFB = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("pathToFBDB").item(0);
			pathFB = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userFBDB").item(0);
			userFB = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("passwordFBDB").item(0);
			passwordFB = nodePassword.getTextContent();

			Class.forName("org.firebirdsql.jdbc.FBDriver");
			Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/" + portFB + ":" + pathFB, userFB, passwordFB);
			Statement stm = con.createStatement();
			ResultSet res = stm.executeQuery("");
			while (res.next()) {

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void modelPromtactFB() {
		interPhaseMap = iRoadObjectModel.getModel().getRoadPromtactuModel().getInterPhaseMap();
		basicMapInterphaseSpecificPromtact = iRoadObjectModel.getModel().getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		basicPromtactDataMap = iRoadObjectModel.getModel().getRoadDirectionModel().getMapOfBasePromtact();
		InterphaseTransitionsHBoxCell interphaseTransitionsHBoxCell = null;
		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("portToFBDB").item(0);
			portFB = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("pathToFBDB").item(0);
			pathFB = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userFBDB").item(0);
			userFB = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("passwordFBDB").item(0);
			passwordFB = nodePassword.getTextContent();

			Class.forName("org.firebirdsql.jdbc.FBDriver");
			Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/" + portFB + ":" + pathFB, userFB, passwordFB);
			Statement stm = con.createStatement();
			ResultSet specificPromtact = stm.executeQuery("SELECT\n" + "obj.name,\n" + "transDir.id_direct_in_transit,\n" + "phase1.number AS Phase_From,\n" + "phase2.number AS Phase_To,\n" + "dir.number AS DIR_NUMBER,\n" + "prom.end_green_addit,\n"
					+ "prom.end_green_blink,\n" + "prom.end_yellow,\n" + "prom.end_red,\n" + "prom.end_red_yellow\n" + "\n" + "FROM transit tran\n" + "\n" + "INNER JOIN object obj ON (obj.id_object = tran.fk_object)\n"
					+ "INNER JOIN direct_in_transit transDir ON (transDir.fk_transit =\n" + "                    tran.id_transit)\n" + "INNER JOIN directions dir ON (dir.id_direction = transDir.direction)\n"
					+ "INNER JOIN phas_list phase1 ON (phase1.id_phas = tran.from_phas)\n" + "LEFT JOIN phas_list phase2 ON (phase2.id_phas = tran.to_phas)\n" + "INNER JOIN promtakt prom ON (prom.fk_direct_in_transit =\n"
					+ "                    transDir.id_direct_in_transit)\n" + "\n" + "WHERE obj.id_object = '" + queryBySelectedObjectId + "'" + "\n" + "ORDER BY phase1.number, phase2.number, dir.number");
			while (specificPromtact.next()) {
				fromPhase = specificPromtact.getString(3);
				toPhase = specificPromtact.getString(4);

				String numberDirectionInterphase = specificPromtact.getString(5);
				String roadPromtactu_endGreenAddit = specificPromtact.getString(6);
				String roadPromtactu_durationGreenBlink = specificPromtact.getString(7);
				String roadPromtactu_durationYellow = specificPromtact.getString(8);
				String roadPromtactu_endRed = specificPromtact.getString(9);
				String roadPromtactu_durationRedYellow = specificPromtact.getString(10);

				/*
				 * PromtactData promtactData = new PromtactData();
				 * promtactData.setRoadPromtactu_transitionFromThePhaseNumber(fromPhase);
				 * promtactData.setRoadPromtactu_toPhaseNumber(toPhase);
				 * promtactData.setRoadPromtactu_directionNumber(numberDirectionInterphase);
				 * promtactData.setRoadPromtactu_endGreenAddit(roadPromtactu_endGreenAddit);
				 * promtactData.setRoadPromtactu_durationGreenBlink(
				 * roadPromtactu_durationGreenBlink);
				 * promtactData.setRoadPromtactu_durationYellow(roadPromtactu_durationYellow);
				 * promtactData.setRoadPromtactu_endRed(roadPromtactu_endRed);
				 * promtactData.setRoadPromtactu_durationRedYellow(
				 * roadPromtactu_durationRedYellow);
				 */

				/*Map<String, List<String>> mapOfOpenDirInPhase = iRoadObjectModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
				PromtactData promtactData = null;

				for (Map.Entry<String, List<String>> entryOpenDir : mapOfOpenDirInPhase.entrySet()) {
					String keyPhaseFrom = entryOpenDir.getKey();
					if (keyPhaseFrom.equals(fromPhase)) {
						List<String> openDirectionsList = mapOfOpenDirInPhase.get(keyPhaseFrom);
						for (String direction : openDirectionsList) {
							if (direction.equals(numberDirectionInterphase)) {
								promtactData = new PromtactData();
								promtactData.setRoadPromtactu_endGreenAddit(roadPromtactu_endGreenAddit);
								promtactData.setRoadPromtactu_durationGreenBlink(roadPromtactu_durationGreenBlink);
								promtactData.setRoadPromtactu_durationYellow(roadPromtactu_durationYellow);
								promtactData.setRoadPromtactu_endRed(new Integer(0).toString());
								promtactData.setRoadPromtactu_durationRedYellow(new Integer(0).toString());

							}
						}
					} else {
						String keyPhaseTo = entryOpenDir.getKey();
						if (keyPhaseTo.equals(toPhase)) {
							List<String> openDirectionsList = mapOfOpenDirInPhase.get(keyPhaseTo);
							for (String direction : openDirectionsList) {
								if (direction.equals(numberDirectionInterphase)) {
									promtactData = new PromtactData();
									promtactData.setRoadPromtactu_endGreenAddit(new Integer(0).toString());
									promtactData.setRoadPromtactu_durationGreenBlink(new Integer(0).toString());
									promtactData.setRoadPromtactu_durationYellow(new Integer(0).toString());
									promtactData.setRoadPromtactu_endRed(roadPromtactu_endRed);
									promtactData.setRoadPromtactu_durationRedYellow(roadPromtactu_durationRedYellow);
								}
							}
						}
					}
				}*/

				/*if (!basicMapInterphaseSpecificPromtact.isEmpty()) {
					boolean needNewInter = true;

					for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entry : basicMapInterphaseSpecificPromtact.entrySet()) {
						InterphaseTransitionsHBoxCell existedInterphase = entry.getKey();
						String from = existedInterphase.getComboBoxFromPhase().getValue();
						String to = existedInterphase.getComboBoxToPhase().getValue();

						if (from.equals(fromPhase) & to.equals(toPhase)) {
							Map<String, PromtactData> promDataMap = basicMapInterphaseSpecificPromtact.get(existedInterphase);
							promDataMap.put(numberDirectionInterphase, promtactData);
							needNewInter = false;
							break;
						}
					}
					if (needNewInter) {
						specificPromtactDataMap = new HashMap<>();

						interphaseTransitionsHBoxCell = new InterphaseTransitionsHBoxCell();
						interphaseTransitionsHBoxCell.getComboBoxFromPhase().setValue(fromPhase);
						interphaseTransitionsHBoxCell.getComboBoxToPhase().setValue(toPhase);

						specificPromtactDataMap.put(numberDirectionInterphase, promtactData);
						basicMapInterphaseSpecificPromtact.put(interphaseTransitionsHBoxCell, specificPromtactDataMap);
					}
				} else {
					specificPromtactDataMap = new HashMap<>();

					interphaseTransitionsHBoxCell = new InterphaseTransitionsHBoxCell();
					interphaseTransitionsHBoxCell.getComboBoxFromPhase().setValue(fromPhase);
					interphaseTransitionsHBoxCell.getComboBoxToPhase().setValue(toPhase);

					specificPromtactDataMap.put(numberDirectionInterphase, promtactData);
					basicMapInterphaseSpecificPromtact.put(interphaseTransitionsHBoxCell, specificPromtactDataMap);
				}*/

			}

			ResultSet basicPromtact = stm.executeQuery("SELECT\n" + "dir.number,\n" + "dir.end_green_addit,\n" + "dir.end_green_blink,\n" + "dir.end_yellow,\n" + "dir.end_red,\n" + "dir.end_red_yellow\n" + "\n" + "FROM directions dir\n" + "\n"
					+ "INNER JOIN object obj ON (obj.id_object = dir.fk_object)\n" + "\n" + "WHERE obj.id_object = '" + queryBySelectedObjectId + "'");

			while (basicPromtact.next()) {
				String numberDirection = basicPromtact.getString(1);
				String roadPromtactu_endGreenAddit = basicPromtact.getString(2);
				String roadPromtactu_durationGreenBlink = basicPromtact.getString(3);
				String roadPromtactu_endRed = basicPromtact.getString(5);
				String roadPromtactu_durationRedYellow = basicPromtact.getString(6);
				String roadPromtactu_durationYellow = basicPromtact.getString(4);

				PromtactData promtactData = new PromtactData();
				promtactData.setRoadPromtactu_directionNumber(numberDirection);
				promtactData.setRoadPromtactu_endGreenAddit(roadPromtactu_endGreenAddit);
				promtactData.setRoadPromtactu_durationGreenBlink(roadPromtactu_durationGreenBlink);
				promtactData.setRoadPromtactu_durationYellow(roadPromtactu_durationYellow);
				promtactData.setRoadPromtactu_endRed(roadPromtactu_endRed);
				promtactData.setRoadPromtactu_durationRedYellow(roadPromtactu_durationRedYellow);

				basicPromtactDataMap.put(numberDirection, promtactData);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void modelCalendar() {
		try {
			String fileName = "configuration.xml";
			String workingDirectory = System.getProperty("user.dir");
			String filePath = "";

			filePath = workingDirectory + File.separator + fileName;

			File xmlFile = new File(filePath);

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);

			Node nodePort = document.getElementsByTagName("portToFBDB").item(0);
			portFB = nodePort.getTextContent();

			Node nodeLocalHost = document.getElementsByTagName("pathToFBDB").item(0);
			pathFB = nodeLocalHost.getTextContent();

			Node nodeUserName = document.getElementsByTagName("userFBDB").item(0);
			userFB = nodeUserName.getTextContent();

			Node nodePassword = document.getElementsByTagName("passwordFBDB").item(0);
			passwordFB = nodePassword.getTextContent();

			Class.forName("org.firebirdsql.jdbc.FBDriver");
			Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/" + portFB + ":" + pathFB, userFB, passwordFB);
			Statement stm = con.createStatement();
			ResultSet res = stm.executeQuery("SELECT\n" + "prog.number AS prog_number,\n" + "calendar.day_of_week,\n" + "calendar.time_start,\n" + "calendar.time_offset\n" + "\n" + "FROM time_table calendar\n" + "\n"
					+ "INNER JOIN object obj ON (obj.id_object = calendar.fk_object)\n" + "INNER JOIN programs prog ON (prog.id_program = calendar.work_program)\n" + "\n" + "WHERE obj.id_object = '" + queryBySelectedObjectId + "'");
			while (res.next()) {

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void modelIPFB() {

	}

	public void modelAtributsFB() {

	}
	//////////////////////////////////////////////////////////////

	public void createKDKcatalog() {

		//////////////////////////////////////////////////////////////////////////////////////////////
		String id0801np;
		UUID uuid0801np = UUID.randomUUID();
		id0801np = uuid0801np.toString();
		TypeKDK kdk0801np = new TypeKDK(id0801np, "КДК-08-01 НП (Mega128)", "8", "16", "32", "16", "2");

		String id1601np;
		UUID uuid1601np = UUID.randomUUID();
		id1601np = uuid1601np.toString();
		TypeKDK kdk1601np = new TypeKDK(id1601np, "КДК-16-01 НП (Mega128)", "16", "16", "32", "16", "2");

		String id2401np;
		UUID uuid2401np = UUID.randomUUID();
		id2401np = uuid2401np.toString();
		TypeKDK kdk2401np = new TypeKDK(id2401np, "КДК-24-01 НП (Mega128)", "24", "16", "32", "16", "2");

		String id3201np;
		UUID uuid3201np = UUID.randomUUID();
		id3201np = uuid3201np.toString();
		TypeKDK kdk3201np = new TypeKDK(id3201np, "КДК-32-01 НП (Mega128)", "32", "16", "32", "16", "2");

		String id4001np;
		UUID uuid4001np = UUID.randomUUID();
		id4001np = uuid4001np.toString();
		TypeKDK kdk4001np = new TypeKDK(id4001np, "КДК-40-01 НП (Mega128)", "40", "16", "32", "16", "2");

		String id4801np;
		UUID uuid4801np = UUID.randomUUID();
		id4801np = uuid4801np.toString();
		TypeKDK kdk4801np = new TypeKDK(id4801np, "КДК-48-01 НП (Mega128)", "48", "16", "32", "16", "2");
		
		typeKDKList.add(kdk0801np);
		typeKDKList.add(kdk1601np);
		typeKDKList.add(kdk2401np);
		typeKDKList.add(kdk3201np);
		typeKDKList.add(kdk4001np);
		typeKDKList.add(kdk4801np);

	}

	public void setItemsKDKType() {
		for (TypeKDK typeKDK : typeKDKList) {
			observableListTypeKDK.add(typeKDK.name_KDK);
		}
	}

	@FXML
	public void initialize() {

		//langXML();
		//loadLang(langXML);
		buttonReferenceBookOfTheKDK.setVisible(false);
		labelViewObject.setVisible(false);

		ObservableList<String> observableList = FXCollections.observableArrayList("Красно-желтый", "Желтый");
		chCharge.setItems(observableList);

		// listeners to fields for update
		txtName.textProperty().addListener((observable, oldValue, newValue) -> {
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectName(newValue);
		});
		txtNetworkAddress.textProperty().addListener((observable, oldValue, newValue) -> {
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectNetworkAddress(newValue);
		});
		txtObjectNumber.textProperty().addListener((observable, oldValue, newValue) -> {
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectObjectNumber(newValue);
		});
		txtCity.textProperty().addListener((observable, oldValue, newValue) -> {
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectCity(newValue);
		});
		txtTechnologist.textProperty().addListener((observable, oldValue, newValue) -> {
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectTechnologist(newValue);
		});
		txtAreaNote.textProperty().addListener((observable, oldValue, newValue) -> {
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectNote(newValue);
		});
		txtMagistral.textProperty().addListener((observable, oldValue, newValue) -> {
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectMagistral(newValue);
		});
		chBoxTypeKDK.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectTypeOfKDK(newValue);
		});
		chBoxProtokol.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectProtocol(newValue);
		});
		chBoxCountry.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectCountry(newValue);
		});
		chCharge.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectCharge(newValue);
		});
		
		
		
		/*if(iRoadObjectModel != null) {
			if(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectProtocol() != null) {
				System.out.println("!!!!!!!!");
			}
		}*/
		
		
		
		dPdateOfCreation.valueProperty().addListener((observable, oldDate, newDate) -> {
			if(newDate != null) {
				localDateCreation = newDate;
				formatterCreation = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				dateCreation = localDateCreation.format(formatterCreation);
				iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectDateOfCreation(dateCreation);	
			}
		});
		dPlaunchDate.valueProperty().addListener((observable, oldDate, newDate) -> {
			if(newDate != null) {
				localLaunchDate = newDate;
				formatterLaunchDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				dateLaunchDate = localLaunchDate.format(formatterLaunchDate);
				iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectLaunchDate(dateLaunchDate);
			}
		});	
				
		createKDKcatalog();
		setItemsKDKType();

		labelDirectionsLoad2.setLayoutX(labelDirectionLoad1.getLayoutX());

		chBoxCountry.setItems(FXCollections.observableArrayList("Украина", "Россия", "Белорусь", "Молдова", "Другая страна"));
		chBoxProtokol.setItems(FXCollections.observableArrayList("Радио 'КОМКОН' - 1200", "Ethernet 'КОМКОН' - 115200", "Радио 'Росток'", "Харьков(1 линия)",
				"Харьков(2 линия)", "Житомир(1 линия)", "Житомир(2 линия)", "Луганск(1 линия)", "Луганск(2 линия)"));
		chBoxTypeKDK.setItems(observableListTypeKDK);

		chBoxTypeKDK.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				for (TypeKDK typeKDK : typeKDKList) {
					if (newValue.equals(typeKDK.getName_KDK())) {
						lblMAX_channels.setText(typeKDK.getChanels());
						lblMAX_program.setText(typeKDK.getPrograms());
						lblMAX_directions.setText(typeKDK.getDirections());
						lblMAX_tvp.setText(typeKDK.getMax_TVP());
						lblMAX_phases.setText(typeKDK.getPhases());
	
						iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectTypeOfKDK(newValue);
					}
				}
			}
		});
				
		txtObjectNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txtObjectNumber.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
				 
		txtNetworkAddress.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txtNetworkAddress.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}
		});
				 
		txtMagistral.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txtMagistral.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
				 
		txtCity.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("[a-zA-Z]")) {
							 txtCity.setText(newValue.replaceAll("[^\\D]", ""));
				}
			}
		});
				 
		txtTechnologist.textProperty().addListener(new ChangeListener<String>() {
			 @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("[a-zA-Z]")) {
					 txtTechnologist.setText(newValue.replaceAll("[^\\D]", ""));
				}
			}
		});
		
		txtName.textProperty().addListener(new ChangeListener<String>() {
			 @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("[a-zA-Z]")) {
					txtName.setText(newValue.replaceAll("", ""));
				}
			}
		});
	}
}
