package presenters.conflicts;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presenters.directions.RoadDirection;
import presenters.phase.OpenDirectionInCurrentPhaseHBoxCell;
import roadModel.IRoadModel;

/**
 * Created by Vitaly on 06.12.2016.
 */
public class RoadConflictsPresenter {

	@FXML
	private Label labelConflicts, labelConflictDirection, labelConflictForDirection, labelConflictWithDirections;
	@FXML
	private Button buttonAutofillConflicts, buttonCreateConflicts, buttonDeleteConflicts;
	@FXML
	private ListView<ConflictForHBoxCell> listViewConflictFor;
	@FXML
	private ListView<ConflictWithHBoxCell> listViewConflictWith;
	@FXML
	private GridPane gridPane;

	@FXML
	//private static ResourceBundle bundleGUI, bundleAlert;
	//private static Locale localeGUI, localeAlert;
	//String langXML = null;

	int indexConflictWith;

	Map<String, List<ConflictWithDirection>> map;
	Map<String, List<ConflictWithDirection>> autofillConflictMap;

	ConflictWithDirection conflictWithDirection;
	ConflictWithDirection conflictDirection;

	ComboBox<String> comboBox;
	Label conflictDirectionNumber;

	List<ConflictForHBoxCell> conflictForDirectionHBoxCellList = new ArrayList<>();
	List<ConflictWithHBoxCell> conflictWithDirectionHBoxCellList = new ArrayList<>();

	ObservableList<ConflictForHBoxCell> conflictForDirectionObservableList;
	ObservableList<ConflictWithHBoxCell> conflictWithDirectionObservableList;
	ObservableList<String> observableList;
	
	Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> openDirectionsMap;

	private IRoadModel iRoadModel;

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

	/////////////////////////////////////////////////////////////////
	//////////////////// LOCALE /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/*private void loadLang(String lang) {
		localeGUI = new Locale(lang);
		bundleGUI = ResourceBundle.getBundle("bundle.locale_lang", localeGUI);

		labelConflicts.setText(bundleGUI.getString("labelConflicts"));
		labelConflictDirection.setText(bundleGUI.getString("labelConflictDirection"));
		labelConflictForDirection.setText(bundleGUI.getString("labelConflictForDirection"));
		labelConflictWithDirections.setText(bundleGUI.getString("labelConflictWithDirections"));
		buttonAutofillConflicts.setText(bundleGUI.getString("buttonAutofill"));
		buttonCreateConflicts.setText(bundleGUI.getString("buttonCreate"));
		buttonDeleteConflicts.setText(bundleGUI.getString("buttonDelete"));
	}*/

	/*private static void alertLang(String lang) {
		localeAlert = new Locale(lang);
		bundleAlert = ResourceBundle.getBundle("localeAlert_lang", localeAlert);
	}*/
	public void createGridPane() {
		while (gridPane.getRowConstraints().size() > 0) {
			gridPane.getRowConstraints().remove(0);
		}
		while (gridPane.getColumnConstraints().size() > 0) {
			gridPane.getColumnConstraints().remove(0);
		}

		Text conflict = new Text("Конфликт с направлением");
		StackPane conflictPane = new StackPane(conflict);
		conflictPane.getStyleClass().add("grid-title");
		gridPane.add(conflictPane, 1, 0, 33, 1);

		// add first column
		Text direction = new Text("Направления");
		StackPane directiontPane = new StackPane(direction);
		directiontPane.getStyleClass().add("grid-title");
		direction.setRotate(270);
		gridPane.add(directiontPane, 0, 1, 1, 33);

		// set cell [0,0] style
		StackPane cell_00 = new StackPane();
		cell_00.getStyleClass().add("grid-cell-no-border");
		gridPane.add(cell_00, 0, 0);

		// set cell [1,1] style
		StackPane cell_11 = new StackPane();
		cell_11.getStyleClass().add("grid-direction-cell");
		gridPane.add(cell_11, 1, 1);

		for (int index = 2; index <= 32 + 1; index++) {
			StackPane columnPane = new StackPane(new Text(String.valueOf(index - 1)));
			columnPane.setPrefHeight(33);
			columnPane.setPrefWidth(33);
			columnPane.getStyleClass().add("grid-direction-cell");
			gridPane.addColumn(index, columnPane);

			StackPane rowPane = new StackPane(new Text(String.valueOf(index - 1)));
			rowPane.setPrefHeight(33);
			rowPane.setPrefWidth(33);
			rowPane.getStyleClass().add("grid-direction-cell");
			gridPane.addRow(index, rowPane);
		}

		int i = gridPane.getChildren().size();
		int j = gridPane.getChildren().size();

		for (int row = 2; row <= i; row++) {
			for (int col = 2; col <= j; col++) {
				StackPane sp = new StackPane();
				// sp.setStyle("-fx-background-color: red");
				gridPane.add(sp, col, row);
			}
		}
	}
	
	public void show(IRoadModel iRoadModel) {
		this.iRoadModel = iRoadModel;
		
		
		/*
		 for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : autofillConflictMap.entrySet()) {
				String key = mapKeys.getKey();
	
				for (Map.Entry<String, List<OpenDirectionInCurrentPhaseHBoxCell>> phaseMapKeys : phaseMap.entrySet()) {
					boolean needToDelete = false;
					for (OpenDirectionInCurrentPhaseHBoxCell openDirection : phaseMapKeys.getValue()) {
						if (key.equals(openDirection.getComboBox().getValue())) {
							needToDelete = true;
						}
					}
					if (needToDelete == true) {
						for (OpenDirectionInCurrentPhaseHBoxCell openDirection : phaseMapKeys.getValue()) {
							for (ConflictWithDirection conflictWithDirectionToDelete : mapKeys.getValue()) {
								String valueToDelete = conflictWithDirectionToDelete.getConflictWithDirection();
								if (openDirection.getComboBox().getValue().equals(valueToDelete)) {
									autofillConflictMap.get(key).remove(conflictWithDirectionToDelete);
									break;
								}
							}
						}
					}
				}
			} 
		 */
		
		
		
		
		openDirectionsMap = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		map = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();

		////////////////////////////////////////////////////////////
		/////// set the size of list by the number of keys /////////
		////////////////////////////////////////////////////////////
		for (Map.Entry<String, List<ConflictWithDirection>> entry : map.entrySet()) {
			conflictForDirectionHBoxCellList.add(null);
		}
		////////////////////////////////////////////////////////////

		boolean isDirShow = false;
		ObservableList<String> observableList = FXCollections.observableArrayList();

		/////////////////////////////////////////
		//// add all keys in combobox ///////////
		/////////////////////////////////////////
		for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : map.entrySet()) {
			String keys = entryKeys.getKey();
			observableList.add(keys);
		}
		/////////////////////////////////////////		
		
		

		List<RoadDirection> allDirection = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();

		//////////////////////////////////////////////////////
		///////// ADD CONFLICT DIRECTION IN COMBOBOX /////////
		//////////////////////////////////////////////////////
		for (Map.Entry<String, List<ConflictWithDirection>> entry : map.entrySet()) {
			String conflictFor = entry.getKey();
			Integer sort = Integer.parseInt(conflictFor) - 1;
			conflictForDirectionHBoxCellList.set(sort, new ConflictForHBoxCell("Направление номер ", conflictFor));
			conflictForDirectionObservableList = FXCollections.observableList(conflictForDirectionHBoxCellList);
			listViewConflictFor.setItems(conflictForDirectionObservableList);
			listViewConflictFor.getSelectionModel().selectFirst();
		}

		if (listViewConflictFor.getItems().size() == 0) {
			buttonCreateConflicts.setDisable(true);
		}
		
		
		// set items in lvConflictWith for the first conflictFor 
		listViewConflictWith.getItems().clear();
		map = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();
		observableList = FXCollections.observableArrayList();
		
		if(listViewConflictFor.getItems().size() != 0) {
			String selectedItem = listViewConflictFor.getSelectionModel().getSelectedItem().getLabelConflictNumber().getText();
	
			List<ConflictWithDirection> conflictWithDirectionsList = map.get(selectedItem);
	
			for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : map.entrySet()) {
				String keys = entryKeys.getKey();
				observableList.add(keys);
			}
			
			//sort
			List<Integer> list = new ArrayList<Integer>();
			for(String sort : observableList) {
				list.add(Integer.parseInt(sort));
			}
			
			Collections.sort(list);
			
			observableList.clear();
			
			for(Integer after : list) {
				observableList.add(String.valueOf(after));
			}
			//
	
			for (ConflictWithDirection conflictWithDirection : conflictWithDirectionsList) {
				ConflictWithHBoxCell conflictWithHBoxCell = new ConflictWithHBoxCell();
				comboBox = conflictWithHBoxCell.getComboBox();
				conflictDirectionNumber = new Label("Конфликт с направлением");
	
				comboBox.getSelectionModel().select(conflictWithDirection.getConflictWithDirection());
	
				conflictWithHBoxCell.setObservableListComboBox(observableList);
				conflictWithHBoxCell.setLabel(conflictDirectionNumber);
	
				conflictWithDirectionHBoxCellList.add(conflictWithHBoxCell);
				conflictWithDirectionObservableList = FXCollections.observableList(conflictWithDirectionHBoxCellList);
				listViewConflictWith.setItems(conflictWithDirectionObservableList);
				listViewConflictWith.getSelectionModel().selectFirst();
			}
		}
		
		///////////////////////////////////////////////////

		//////////////////////////////////////////
		/////////////// GRIDPANE /////////////////
		//////////////////////////////////////////
		while (gridPane.getRowConstraints().size() > 0) {
			gridPane.getRowConstraints().remove(0);
		}
		while (gridPane.getColumnConstraints().size() > 0) {
			gridPane.getColumnConstraints().remove(0);
		}

		Text conflict = new Text("Конфликт с направлением");
		StackPane conflictPane = new StackPane(conflict);
		conflictPane.getStyleClass().add("grid-title");
		gridPane.add(conflictPane, 1, 0, 33, 1);

		// add first column
		Text direction = new Text("Направления");
		StackPane directiontPane = new StackPane(direction);
		directiontPane.getStyleClass().add("grid-title");
		direction.setRotate(270);
		gridPane.add(directiontPane, 0, 1, 1, 33);

		// set cell [0,0] style
		StackPane cell_00 = new StackPane();
		cell_00.getStyleClass().add("grid-cell-no-border");
		gridPane.add(cell_00, 0, 0);

		// set cell [1,1] style
		StackPane cell_11 = new StackPane();
		cell_11.getStyleClass().add("grid-direction-cell");
		gridPane.add(cell_11, 1, 1);

		for (int index = 2; index <= 32 + 1; index++) {
			StackPane columnPane = new StackPane(new Text(String.valueOf(index - 1)));
			columnPane.setPrefHeight(33);
			columnPane.setPrefWidth(33);
			columnPane.getStyleClass().add("grid-direction-cell");
			gridPane.addColumn(index, columnPane);

			StackPane rowPane = new StackPane(new Text(String.valueOf(index - 1)));
			rowPane.setPrefHeight(33);
			rowPane.setPrefWidth(33);
			rowPane.getStyleClass().add("grid-direction-cell");
			gridPane.addRow(index, rowPane);
		}

		int i = gridPane.getChildren().size();
		int j = gridPane.getChildren().size();

		for (int row = 2; row <= i; row++) {
			for (int col = 2; col <= j; col++) {
				StackPane sp = new StackPane();
				// sp.setStyle("-fx-background-color: red");
				gridPane.add(sp, col, row);
			}
		}

		for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : map.entrySet()) {
			int key = Integer.parseInt(mapKeys.getKey());

			for (ConflictWithDirection conflictWithDirection : mapKeys.getValue()) {
				if (conflictWithDirection.getConflictWithDirection().equals("")) {
					System.out.println("Values in map is empty");
				} else {
					int value = Integer.parseInt(conflictWithDirection.getConflictWithDirection());
					// System.out.println("Keys: " + key + " Value: " + value);
					gridPane.add(new ImageView(new Image("image/other/Warning.png")), value + 1, key + 1);
				}
			}
		}
		/////////////////////////////////////////////////////
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// create GRIDPANE
		/*while (gridPane.getRowConstraints().size() > 0) {
			gridPane.getRowConstraints().remove(0);
		}
		while (gridPane.getColumnConstraints().size() > 0) {
			gridPane.getColumnConstraints().remove(0);
		}

		Text conflict = new Text("Конфликт с направлением");
		StackPane conflictPane = new StackPane(conflict);
		conflictPane.getStyleClass().add("grid-title");
		gridPane.add(conflictPane, 1, 0, 33, 1);

		// add first column
		Text direction = new Text("Направления");
		StackPane directiontPane = new StackPane(direction);
		directiontPane.getStyleClass().add("grid-title");
		direction.setRotate(270);
		gridPane.add(directiontPane, 0, 1, 1, 33);

		// set cell [0,0] style
		StackPane cell_00 = new StackPane();
		cell_00.getStyleClass().add("grid-cell-no-border");
		gridPane.add(cell_00, 0, 0);

		// set cell [1,1] style
		StackPane cell_11 = new StackPane();
		cell_11.getStyleClass().add("grid-direction-cell");
		gridPane.add(cell_11, 1, 1);

		for (int index = 2; index <= 32 + 1; index++) {
			StackPane columnPane = new StackPane(new Text(String.valueOf(index - 1)));
			columnPane.setPrefHeight(33);
			columnPane.setPrefWidth(33);
			columnPane.getStyleClass().add("grid-direction-cell");
			gridPane.addColumn(index, columnPane);

			StackPane rowPane = new StackPane(new Text(String.valueOf(index - 1)));
			rowPane.setPrefHeight(33);
			rowPane.setPrefWidth(33);
			rowPane.getStyleClass().add("grid-direction-cell");
			gridPane.addRow(index, rowPane);
		}

		int i = gridPane.getChildren().size();
		int j = gridPane.getChildren().size();

		for (int row = 2; row <= i; row++) {
			for (int col = 2; col <= j; col++) {
				StackPane sp = new StackPane();
				// sp.setStyle("-fx-background-color: red");
				gridPane.add(sp, col, row);
			}
		}*/
		/////////////////////////////////////////////////////////////////////////////////
		
		/*map = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();
		
		for (Map.Entry<String, List<ConflictWithDirection>> entry : map.entrySet()) {
			conflictForDirectionHBoxCellList.add(null);
		}
		
		boolean isDirShow = false;
		ObservableList<String> observableList = FXCollections.observableArrayList();
		
		for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : map.entrySet()) {
			String keys = entryKeys.getKey();
			observableList.add(keys);
		}
		
		
		for (Map.Entry<String, List<ConflictWithDirection>> entry : map.entrySet()) {
			String conflictFor = entry.getKey();
			Integer sort = Integer.parseInt(conflictFor) - 1;
			conflictForDirectionHBoxCellList.set(sort, new ConflictForHBoxCell("Направление номер ", conflictFor));
			conflictForDirectionObservableList = FXCollections.observableList(conflictForDirectionHBoxCellList);
			listViewConflictFor.setItems(conflictForDirectionObservableList);
			listViewConflictFor.getSelectionModel().selectFirst();

			if (!isDirShow) {

				for (ConflictWithDirection conflictWithDirection : entry.getValue()) {

					ConflictWithHBoxCell conflictWithHBoxCell = new ConflictWithHBoxCell();
					comboBox = conflictWithHBoxCell.getComboBox();
					conflictDirectionNumber = new Label("Конфликт с направлением");

					System.out.println(entry.getKey() + "    " + conflictWithDirection.getConflictWithDirection());
					comboBox.getSelectionModel().select(conflictWithDirection.getConflictWithDirection());

					conflictWithHBoxCell.setObservableListComboBox(observableList);
					conflictWithHBoxCell.setLabel(conflictDirectionNumber);

					conflictWithDirectionHBoxCellList.add(conflictWithHBoxCell);
					conflictWithDirectionObservableList = FXCollections.observableList(conflictWithDirectionHBoxCellList);
					listViewConflictWith.setItems(conflictWithDirectionObservableList);
				}
				isDirShow = true;
			}
		}
		
		if (listViewConflictFor.getItems().size() == 0) {
			buttonCreateConflicts.setDisable(true);
		}
		
		for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : map.entrySet()) {
			int key = Integer.parseInt(mapKeys.getKey());

			for (ConflictWithDirection conflictWithDirection : mapKeys.getValue()) {
				if (conflictWithDirection.getConflictWithDirection().equals("")) {
					System.out.println("Values in map is empty");
				} else {
					int value = Integer.parseInt(conflictWithDirection.getConflictWithDirection());
					// System.out.println("Keys: " + key + " Value: " + value);
					gridPane.add(new ImageView(new Image("image/other/Warning.png")), value + 1, key + 1);
				}
			}
		}*/
		
		
		/*if(!map.isEmpty()) {
			for (Map.Entry<String, List<ConflictWithDirection>> entry : map.entrySet()) {
				conflictForDirectionHBoxCellList.add(null);
			}
			
			boolean isDirShow = false;
			ObservableList<String> observableList = FXCollections.observableArrayList();
			
			for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : map.entrySet()) {
				String keys = entryKeys.getKey();
				observableList.add(keys);
			}
			
			List<RoadDirection> allDirection = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();
			
			for (Map.Entry<String, List<ConflictWithDirection>> entry : map.entrySet()) {
				String conflictFor = entry.getKey();
				Integer sort = Integer.parseInt(conflictFor) - 1;
				conflictForDirectionHBoxCellList.set(sort, new ConflictForHBoxCell("Направление номер ", conflictFor));
				conflictForDirectionObservableList = FXCollections.observableList(conflictForDirectionHBoxCellList);
				listViewConflictFor.setItems(conflictForDirectionObservableList);
				listViewConflictFor.getSelectionModel().selectFirst();

				if (!isDirShow) {

					for (ConflictWithDirection conflictWithDirection : entry.getValue()) {

						ConflictWithHBoxCell conflictWithHBoxCell = new ConflictWithHBoxCell();
						comboBox = conflictWithHBoxCell.getComboBox();
						conflictDirectionNumber = new Label("Конфликт с направлением");

						System.out.println(entry.getKey() + "    " + conflictWithDirection.getConflictWithDirection());
						comboBox.getSelectionModel().select(conflictWithDirection.getConflictWithDirection());

						conflictWithHBoxCell.setObservableListComboBox(observableList);
						conflictWithHBoxCell.setLabel(conflictDirectionNumber);

						conflictWithDirectionHBoxCellList.add(conflictWithHBoxCell);
						conflictWithDirectionObservableList = FXCollections.observableList(conflictWithDirectionHBoxCellList);
						listViewConflictWith.setItems(conflictWithDirectionObservableList);
					}
					isDirShow = true;
				}
			}

			if (listViewConflictFor.getItems().size() == 0) {
				buttonCreateConflicts.setDisable(true);
			}
			
			for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : map.entrySet()) {
				int key = Integer.parseInt(mapKeys.getKey());

				for (ConflictWithDirection conflictWithDirection : mapKeys.getValue()) {
					if (conflictWithDirection.getConflictWithDirection().equals("")) {
						System.out.println("Values in map is empty");
					} else {
						int value = Integer.parseInt(conflictWithDirection.getConflictWithDirection());
						// System.out.println("Keys: " + key + " Value: " + value);
						gridPane.add(new ImageView(new Image("image/other/Warning.png")), value + 1, key + 1);
					}
				}
			}
		}else {
			listViewConflictFor.getItems().clear();
			listViewConflictWith.getItems().clear();

			Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> phaseMap = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
			List<RoadDirection> allDirection = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();

			autofillConflictMap = new HashMap<>();

			for (RoadDirection directionFromList : allDirection) { //
				String numberDirection = directionFromList.getRoadDirections_number();

				List<ConflictWithDirection> conflictWithDirectionList = new ArrayList<>();

				for (RoadDirection directionValue : allDirection) {
					String value = directionValue.getRoadDirections_number();
					if (!value.equals(numberDirection)) {
						ConflictWithDirection conflictWithDirection = new ConflictWithDirection();
						conflictWithDirection.setConflictWithDirection(value);
						conflictWithDirectionList.add(conflictWithDirection);
					}
				}
				autofillConflictMap.put(numberDirection, conflictWithDirectionList);
				//System.out.println("Map autofillConflictMap " + autofillConflictMap);
			}

			for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : autofillConflictMap.entrySet()) {
				String key = mapKeys.getKey();

				for (Map.Entry<String, List<OpenDirectionInCurrentPhaseHBoxCell>> phaseMapKeys : phaseMap.entrySet()) {
					boolean needToDelete = false;
					for (OpenDirectionInCurrentPhaseHBoxCell openDirection : phaseMapKeys.getValue()) {
						if (key.equals(openDirection.getComboBox().getValue())) {
							needToDelete = true;
						}
					}
					if (needToDelete == true) {
						for (OpenDirectionInCurrentPhaseHBoxCell openDirection : phaseMapKeys.getValue()) {
							for (ConflictWithDirection conflictWithDirectionToDelete : mapKeys.getValue()) {
								String valueToDelete = conflictWithDirectionToDelete.getConflictWithDirection();
								if (openDirection.getComboBox().getValue().equals(valueToDelete)) {
									autofillConflictMap.get(key).remove(conflictWithDirectionToDelete);
									break;
								}
							}
						}
					}
				}
			}
			for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : autofillConflictMap.entrySet()) {
				for (ConflictWithDirection conflictWithDirection : entryKeys.getValue()) {

					//System.out.println("Map conflict after delete " + entryKeys.getKey() + ": " + conflictWithDirection.getConflictWithDirection());
				}
			}

			boolean isDirShow = false;
			ObservableList<String> observableList = FXCollections.observableArrayList();
			
			for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : autofillConflictMap.entrySet()) {
				String keys = entryKeys.getKey();
				observableList.add(keys);
			}
			
			for (Map.Entry<String, List<ConflictWithDirection>> entry : autofillConflictMap.entrySet()) {
				conflictForDirectionHBoxCellList.add(null);
			}
			//System.out.println("Map autofillConflictMap " + autofillConflictMap);
			for (Map.Entry<String, List<ConflictWithDirection>> entry : autofillConflictMap.entrySet()) {
				String conflictFor = entry.getKey();
				Integer sort = Integer.parseInt(conflictFor) - 1;
				conflictForDirectionHBoxCellList.set(sort, new ConflictForHBoxCell("Направление номер", conflictFor));
				conflictForDirectionObservableList = FXCollections.observableList(conflictForDirectionHBoxCellList);
				listViewConflictFor.setItems(conflictForDirectionObservableList);
				listViewConflictFor.getSelectionModel().selectFirst();

				if (!isDirShow) {

					for (ConflictWithDirection conflictWithDirection : entry.getValue()) {
						ConflictWithHBoxCell conflictWithHBoxCell = new ConflictWithHBoxCell();
						comboBox = conflictWithHBoxCell.getComboBox();
						conflictDirectionNumber = new Label("Конфликт с направлением");

						//System.out.println(entry.getKey() + "    " + conflictWithDirection.getConflictWithDirection());
						comboBox.getSelectionModel().select(conflictWithDirection.getConflictWithDirection());

						conflictWithHBoxCell.setObservableListComboBox(observableList);
						conflictWithHBoxCell.setLabel(conflictDirectionNumber);

						conflictWithDirectionHBoxCellList.add(conflictWithHBoxCell);
						conflictWithDirectionObservableList = FXCollections.observableList(conflictWithDirectionHBoxCellList);
						listViewConflictWith.setItems(conflictWithDirectionObservableList);
					}
					isDirShow = true;
				}
			}

			for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : autofillConflictMap.entrySet()) {
				int key = Integer.parseInt(mapKeys.getKey());

				for (ConflictWithDirection conflictWithDirection : mapKeys.getValue()) {
					int value = Integer.parseInt(conflictWithDirection.getConflictWithDirection());
					System.out.println("Keys: " + key + " Value: " + value);
					gridPane.add(new ImageView(new Image("image/other/Warning.png")), value + 1, key + 1);
				}

			}
			iRoadModel.getModel().getRoadConflictsModel().setMapOfConflict(autofillConflictMap);
		}*/
		
		
		
		////////////////////////////////////////////////////////////
		/////// set the size of list by the number of keys /////////
		////////////////////////////////////////////////////////////
		/*for (Map.Entry<String, List<ConflictWithDirection>> entry : map.entrySet()) {
			conflictForDirectionHBoxCellList.add(null);
		}
		////////////////////////////////////////////////////////////

		boolean isDirShow = false;
		ObservableList<String> observableList = FXCollections.observableArrayList();

		/////////////////////////////////////////
		//// add all keys in combobox ///////////
		/////////////////////////////////////////
		for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : map.entrySet()) {
			String keys = entryKeys.getKey();
			observableList.add(keys);
		}
		/////////////////////////////////////////

		List<RoadDirection> allDirection = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();

		//////////////////////////////////////////////////////
		///////// ADD CONFLICT DIRECTION IN COMBOBOX /////////
		//////////////////////////////////////////////////////
		for (Map.Entry<String, List<ConflictWithDirection>> entry : map.entrySet()) {
			String conflictFor = entry.getKey();
			Integer sort = Integer.parseInt(conflictFor) - 1;
			conflictForDirectionHBoxCellList.set(sort, new ConflictForHBoxCell("Направление номер ", conflictFor));
			conflictForDirectionObservableList = FXCollections.observableList(conflictForDirectionHBoxCellList);
			listViewConflictFor.setItems(conflictForDirectionObservableList);
			listViewConflictFor.getSelectionModel().selectFirst();

			if (!isDirShow) {

				for (ConflictWithDirection conflictWithDirection : entry.getValue()) {

					ConflictWithHBoxCell conflictWithHBoxCell = new ConflictWithHBoxCell();
					comboBox = conflictWithHBoxCell.getComboBox();
					conflictDirectionNumber = new Label("Конфликт с направлением");

					System.out.println(entry.getKey() + "    " + conflictWithDirection.getConflictWithDirection());
					comboBox.getSelectionModel().select(conflictWithDirection.getConflictWithDirection());

					conflictWithHBoxCell.setObservableListComboBox(observableList);
					conflictWithHBoxCell.setLabel(conflictDirectionNumber);

					conflictWithDirectionHBoxCellList.add(conflictWithHBoxCell);
					conflictWithDirectionObservableList = FXCollections.observableList(conflictWithDirectionHBoxCellList);
					listViewConflictWith.setItems(conflictWithDirectionObservableList);
				}
				isDirShow = true;
			}
		}

		if (listViewConflictFor.getItems().size() == 0) {
			buttonCreateConflicts.setDisable(true);
		}*/
		///////////////////////////////////////////////////

		//////////////////////////////////////////
		/////////////// GRIDPANE /////////////////
		//////////////////////////////////////////
		/*while (gridPane.getRowConstraints().size() > 0) {
			gridPane.getRowConstraints().remove(0);
		}
		while (gridPane.getColumnConstraints().size() > 0) {
			gridPane.getColumnConstraints().remove(0);
		}

		Text conflict = new Text("Conflict with");
		StackPane conflictPane = new StackPane(conflict);
		conflictPane.getStyleClass().add("grid-title");
		gridPane.add(conflictPane, 1, 0, 33, 1);

		// add first column
		Text direction = new Text("Direction");
		StackPane directiontPane = new StackPane(direction);
		directiontPane.getStyleClass().add("grid-title");
		direction.setRotate(270);
		gridPane.add(directiontPane, 0, 1, 1, 33);

		// set cell [0,0] style
		StackPane cell_00 = new StackPane();
		cell_00.getStyleClass().add("grid-cell-no-border");
		gridPane.add(cell_00, 0, 0);

		// set cell [1,1] style
		StackPane cell_11 = new StackPane();
		cell_11.getStyleClass().add("grid-direction-cell");
		gridPane.add(cell_11, 1, 1);

		for (int index = 2; index <= 32 + 1; index++) {
			StackPane columnPane = new StackPane(new Text(String.valueOf(index - 1)));
			columnPane.setPrefHeight(33);
			columnPane.setPrefWidth(33);
			columnPane.getStyleClass().add("grid-direction-cell");
			gridPane.addColumn(index, columnPane);

			StackPane rowPane = new StackPane(new Text(String.valueOf(index - 1)));
			rowPane.setPrefHeight(33);
			rowPane.setPrefWidth(33);
			rowPane.getStyleClass().add("grid-direction-cell");
			gridPane.addRow(index, rowPane);
		}

		int i = gridPane.getChildren().size();
		int j = gridPane.getChildren().size();

		for (int row = 2; row <= i; row++) {
			for (int col = 2; col <= j; col++) {
				StackPane sp = new StackPane();
				// sp.setStyle("-fx-background-color: red");
				gridPane.add(sp, col, row);
			}
		}*/

		/*for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : map.entrySet()) {
			int key = Integer.parseInt(mapKeys.getKey());

			for (ConflictWithDirection conflictWithDirection : mapKeys.getValue()) {
				if (conflictWithDirection.getConflictWithDirection().equals("")) {
					System.out.println("Values in map is empty");
				} else {
					int value = Integer.parseInt(conflictWithDirection.getConflictWithDirection());
					// System.out.println("Keys: " + key + " Value: " + value);
					gridPane.add(new ImageView(new Image("image/other/Warning.png")), value + 1, key + 1);
				}
			}
		}*/
		/////////////////////////////////////////////////////
	}

	public void selectConflictForItem() {
		listViewConflictWith.getItems().clear();
		map = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();
		observableList = FXCollections.observableArrayList();
		String selectedItem = listViewConflictFor.getSelectionModel().getSelectedItem().getLabelConflictNumber().getText();

		List<ConflictWithDirection> conflictWithDirectionsList = map.get(selectedItem);

		for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : map.entrySet()) {
			String keys = entryKeys.getKey();
			observableList.add(keys);
		}

		for (ConflictWithDirection conflictWithDirection : conflictWithDirectionsList) {
			ConflictWithHBoxCell conflictWithHBoxCell = new ConflictWithHBoxCell();
			comboBox = conflictWithHBoxCell.getComboBox();
			conflictDirectionNumber = new Label("Конфликт с направлением");

			comboBox.getSelectionModel().select(conflictWithDirection.getConflictWithDirection());

			conflictWithHBoxCell.setObservableListComboBox(observableList);
			conflictWithHBoxCell.setLabel(conflictDirectionNumber);

			conflictWithDirectionHBoxCellList.add(conflictWithHBoxCell);
			conflictWithDirectionObservableList = FXCollections.observableList(conflictWithDirectionHBoxCellList);
			listViewConflictWith.setItems(conflictWithDirectionObservableList);
			listViewConflictWith.getSelectionModel().selectFirst();
		}
	}

	public void createConflictWith() {
		//alertLang(langXML);
		
		Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		
			if(!mapOpenDirectionInPhase.isEmpty()) {
			
				map = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();
		
				ConflictWithHBoxCell previosConflictWith = listViewConflictWith.getSelectionModel().getSelectedItem();
				if(previosConflictWith != null) {
					
					ObservableList<String> observableList = FXCollections.observableArrayList();
					String selectConflictFor;
			
					selectConflictFor = listViewConflictFor.getSelectionModel().selectedItemProperty().getValue().getLabelConflictNumber().getText();
			
					///////////////////////////////////////////////////////////////////////
					////////////// COMBOBOX CONTAIN ONLY ALLOYED DIRECTION ////////////////
					///////////////////////////////////////////////////////////////////////
					for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : map.entrySet()) {
						String keys = entryKeys.getKey();
						if (!selectConflictFor.equals(keys)) {
							observableList.add(keys);
						}
					}
			
					for (ConflictWithDirection deleteConflict : map.get(selectConflictFor)) {
						String delete = deleteConflict.getConflictWithDirection();
						observableList.removeIf(item -> item.equals(delete) || item.equals(selectConflictFor));
					}
					////////////////////////////////////////////////////////////////////////
					
					List<Integer> list = new ArrayList<Integer>();
					for(String sort : observableList) {
						list.add(Integer.parseInt(sort));
					}
					
					Collections.sort(list);
					
					observableList.clear();
					
					for(Integer after : list) {
						observableList.add(String.valueOf(after));
					}
					
					
					if(previosConflictWith.getComboBox().getValue() != null) {
						if(!observableList.isEmpty()) {
						
							ConflictWithHBoxCell conflictWithHBoxCell = new ConflictWithHBoxCell();
							conflictWithDirection = new ConflictWithDirection();
							
					
							conflictWithHBoxCell.setObservableListComboBox(observableList);
							conflictWithDirectionHBoxCellList.add(conflictWithHBoxCell);
							conflictWithDirectionObservableList = FXCollections.observableList(conflictWithDirectionHBoxCellList);
							listViewConflictWith.setItems(conflictWithDirectionObservableList);
							listViewConflictWith.getSelectionModel().select(listViewConflictWith.getItems().size() - 1);
					
							conflictWithHBoxCell.getComboBox().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
								int indexOfConflictWith = listViewConflictWith.getSelectionModel().getSelectedIndex();
					
								if (newValue.equals(selectConflictFor)) {
									Alert alert = new Alert(Alert.AlertType.INFORMATION);
									alert.setTitle("Информация");
									alert.setHeaderText("Направление не может конфликтовать само с собой");
									
									Stage stage = new Stage();
									stage = (Stage)alert.getDialogPane().getScene().getWindow();
									stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
									
									alert.showAndWait();
									listViewConflictWith.getItems().remove(listViewConflictWith.getItems().size() - 1);
								} else {
									conflictWithDirection.setConflictWithDirection(newValue);
									map.get(selectConflictFor).add(conflictWithDirection);
					
									ConflictWithDirection createdConflictWithDirection = new ConflictWithDirection();
									createdConflictWithDirection.setConflictWithDirection(selectConflictFor);
									map.get(newValue).add(createdConflictWithDirection);
					
									for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : map.entrySet()) {
										int key = Integer.parseInt(mapKeys.getKey());
					
										for (ConflictWithDirection conflictWithDirection : mapKeys.getValue()) {
											int value = Integer.parseInt(conflictWithDirection.getConflictWithDirection());
											System.out.println("Keys: " + key + " Value: " + value);
											gridPane.add(new ImageView(new Image("image/other/Warning.png")), value + 1, key + 1);
										}
									}
								}
							});
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
						alert.setHeaderText("Укажите конфликтующее направление\nпрежде чем создать новое");
						
						Stage stage = new Stage();
						stage = (Stage)alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
						
						alert.show();
					}
					
				}else {
					ConflictWithHBoxCell conflictWithHBoxCell = new ConflictWithHBoxCell();
					conflictWithDirection = new ConflictWithDirection();
					ObservableList<String> observableList = FXCollections.observableArrayList();
					String selectConflictFor;
			
					selectConflictFor = listViewConflictFor.getSelectionModel().selectedItemProperty().getValue().getLabelConflictNumber().getText();				
					
					
					///////////////////////////////////////////////////////////////////////
					////////////// COMBOBOX CONTAIN ONLY ALLOYED DIRECTION ////////////////
					///////////////////////////////////////////////////////////////////////
					for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : map.entrySet()) {
						String keys = entryKeys.getKey();
						if (!selectConflictFor.equals(keys)) {
							observableList.add(keys);
						}
					}
			
					for (ConflictWithDirection deleteConflict : map.get(selectConflictFor)) {
						String delete = deleteConflict.getConflictWithDirection();
						observableList.removeIf(item -> item.equals(delete) || item.equals(selectConflictFor));
					}
					////////////////////////////////////////////////////////////////////////
			
					conflictWithHBoxCell.setObservableListComboBox(observableList);
					conflictWithDirectionHBoxCellList.add(conflictWithHBoxCell);
					conflictWithDirectionObservableList = FXCollections.observableList(conflictWithDirectionHBoxCellList);
					listViewConflictWith.setItems(conflictWithDirectionObservableList);
					listViewConflictWith.getSelectionModel().select(listViewConflictWith.getItems().size() - 1);
			
					conflictWithHBoxCell.getComboBox().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
						int indexOfConflictWith = listViewConflictWith.getSelectionModel().getSelectedIndex();
			
						if (newValue.equals(selectConflictFor)) {
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("Информация");
							alert.setHeaderText("Направление не может конфликтовать само с собой");
							
							Stage stage = new Stage();
							stage = (Stage)alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
							
							alert.showAndWait();
							listViewConflictWith.getItems().remove(listViewConflictWith.getItems().size() - 1);
						} else {
							conflictWithDirection.setConflictWithDirection(newValue);
							map.get(selectConflictFor).add(conflictWithDirection);
			
							ConflictWithDirection createdConflictWithDirection = new ConflictWithDirection();
							createdConflictWithDirection.setConflictWithDirection(selectConflictFor);
							map.get(newValue).add(createdConflictWithDirection);
			
							for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : map.entrySet()) {
								int key = Integer.parseInt(mapKeys.getKey());
			
								for (ConflictWithDirection conflictWithDirection : mapKeys.getValue()) {
									int value = Integer.parseInt(conflictWithDirection.getConflictWithDirection());
									//System.out.println("Keys: " + key + " Value: " + value);
									gridPane.add(new ImageView(new Image("image/other/Warning.png")), value + 1, key + 1);
								}
							}
						}
					});
				}
		}else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Внимание");
			alert.setHeaderText("Недостаточно данных для создания конфликтов");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
		}
	}

	public void deleteConflictWith() {
		map = iRoadModel.getModel().getRoadConflictsModel().getMapOfConflict();

		int itemToDelete = listViewConflictWith.getSelectionModel().getSelectedIndex();
		if (itemToDelete >= 0) {
			String selectedDirection;
			String selectedConflictNumber = null;
	
			for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : map.entrySet()) {
				String keys = entryKeys.getKey();
				String value = entryKeys.getValue().toString();
				//System.out.println("Keys: " + keys + " Value: " + value + " after added");
			}
	
			int selectedConflictIndex = listViewConflictWith.getSelectionModel().getSelectedIndex();
			if (selectedConflictIndex >= 0) {
				selectedConflictNumber = listViewConflictWith.getSelectionModel().selectedItemProperty().get().getComboBox().getValue();
				//System.out.println(selectedConflictNumber);
				listViewConflictWith.getItems().remove(selectedConflictIndex);
			}
	
			selectedDirection = listViewConflictFor.getSelectionModel().selectedItemProperty().getValue().getLabelConflictNumber().getText();
	
			map.get(selectedDirection).remove(selectedConflictIndex);
			map.get(selectedConflictNumber).removeIf(item -> item.getConflictWithDirection() == selectedDirection);
	
			for (javafx.scene.Node node : gridPane.getChildren()) {
				if (node instanceof ImageView) {
					((ImageView) node).setImage(null);
	
				}
			}
	
			for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : map.entrySet()) {
				int key = Integer.parseInt(mapKeys.getKey());
	
				for (ConflictWithDirection conflictWithDirection : mapKeys.getValue()) {
					if (conflictWithDirection.getConflictWithDirection().equals("")) {
	
					} else {
						int value = Integer.parseInt(conflictWithDirection.getConflictWithDirection());
						//System.out.println("Keys: " + key + " Value: " + value);
						gridPane.add(new ImageView(new Image("image/other/Warning.png")), value + 1, key + 1);
					}
				}
	
			}
		}else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Выберите конфликт для удаления");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.showAndWait();
		}

	}

	public void autofillButtonEvent() {

		Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOpenDirectionInPhase = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
		
		if(!mapOpenDirectionInPhase.isEmpty()) {
		
			listViewConflictFor.getItems().clear();
			listViewConflictWith.getItems().clear();
	
			Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> phaseMap = iRoadModel.getModel().getRoadPhaseModel().getMapOpenDirectionInPhase();
			List<RoadDirection> allDirection = iRoadModel.getModel().getRoadDirectionModel().getRoadDirectionList();
	
			autofillConflictMap = new HashMap<>();
	
			for (RoadDirection direction : allDirection) { //
				String numberDirection = direction.getRoadDirections_number();
	
				List<ConflictWithDirection> conflictWithDirectionList = new ArrayList<>();
	
				for (RoadDirection directionValue : allDirection) {
					String value = directionValue.getRoadDirections_number();
					if (!value.equals(numberDirection)) {
						ConflictWithDirection conflictWithDirection = new ConflictWithDirection();
						conflictWithDirection.setConflictWithDirection(value);
						conflictWithDirectionList.add(conflictWithDirection);
					}
				}
				autofillConflictMap.put(numberDirection, conflictWithDirectionList);
				//System.out.println("Map autofillConflictMap " + autofillConflictMap);
			}
	
			for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : autofillConflictMap.entrySet()) {
				String key = mapKeys.getKey();
	
				for (Map.Entry<String, List<OpenDirectionInCurrentPhaseHBoxCell>> phaseMapKeys : phaseMap.entrySet()) {
					boolean needToDelete = false;
					for (OpenDirectionInCurrentPhaseHBoxCell openDirection : phaseMapKeys.getValue()) {
						if (key.equals(openDirection.getComboBox().getValue())) {
							needToDelete = true;
						}
					}
					if (needToDelete == true) {
						for (OpenDirectionInCurrentPhaseHBoxCell openDirection : phaseMapKeys.getValue()) {
							for (ConflictWithDirection conflictWithDirectionToDelete : mapKeys.getValue()) {
								String valueToDelete = conflictWithDirectionToDelete.getConflictWithDirection();
								if (openDirection.getComboBox().getValue().equals(valueToDelete)) {
									autofillConflictMap.get(key).remove(conflictWithDirectionToDelete);
									break;
								}
							}
						}
					}
				}
			}
			for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : autofillConflictMap.entrySet()) {
				for (ConflictWithDirection conflictWithDirection : entryKeys.getValue()) {
	
					//System.out.println("Map conflict after delete " + entryKeys.getKey() + ": " + conflictWithDirection.getConflictWithDirection());
				}
			}
	
			boolean isDirShow = false;
			ObservableList<String> observableList = FXCollections.observableArrayList();
	
			/////////////////////////////////////////
			//// add all keys in combobox ///////////
			/////////////////////////////////////////
			for (Map.Entry<String, List<ConflictWithDirection>> entryKeys : autofillConflictMap.entrySet()) {
				String keys = entryKeys.getKey();
				observableList.add(keys);
			}
			/////////////////////////////////////////
	
			//////////////////////////////////////////////////////
			///////// ADD CONFLICT DIRECTION IN COMBOBOX /////////
			//////////////////////////////////////////////////////
			for (Map.Entry<String, List<ConflictWithDirection>> entry : autofillConflictMap.entrySet()) {
				conflictForDirectionHBoxCellList.add(null);
			}
			System.out.println("Map autofillConflictMap " + autofillConflictMap);
			for (Map.Entry<String, List<ConflictWithDirection>> entry : autofillConflictMap.entrySet()) {
				String conflictFor = entry.getKey();
				Integer sort = Integer.parseInt(conflictFor) - 1;
				conflictForDirectionHBoxCellList.set(sort, new ConflictForHBoxCell("Направление номер", conflictFor));
				conflictForDirectionObservableList = FXCollections.observableList(conflictForDirectionHBoxCellList);
				listViewConflictFor.setItems(conflictForDirectionObservableList);
				listViewConflictFor.getSelectionModel().selectFirst();
	
				if (!isDirShow) {
	
					for (ConflictWithDirection conflictWithDirection : entry.getValue()) {
						ConflictWithHBoxCell conflictWithHBoxCell = new ConflictWithHBoxCell();
						comboBox = conflictWithHBoxCell.getComboBox();
						conflictDirectionNumber = new Label("Конфликт с направлением");
	
						//System.out.println(entry.getKey() + "    " + conflictWithDirection.getConflictWithDirection());
						comboBox.getSelectionModel().select(conflictWithDirection.getConflictWithDirection());
	
						conflictWithHBoxCell.setObservableListComboBox(observableList);
						conflictWithHBoxCell.setLabel(conflictDirectionNumber);
	
						conflictWithDirectionHBoxCellList.add(conflictWithHBoxCell);
						conflictWithDirectionObservableList = FXCollections.observableList(conflictWithDirectionHBoxCellList);
						listViewConflictWith.setItems(conflictWithDirectionObservableList);
					}
					isDirShow = true;
				}
			}
	
			for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : autofillConflictMap.entrySet()) {
				int key = Integer.parseInt(mapKeys.getKey());
	
				for (ConflictWithDirection conflictWithDirection : mapKeys.getValue()) {
					int value = Integer.parseInt(conflictWithDirection.getConflictWithDirection());
					//System.out.println("Keys: " + key + " Value: " + value);
					gridPane.add(new ImageView(new Image("image/other/Warning.png")), value + 1, key + 1);
				}
	
			}
			iRoadModel.getModel().getRoadConflictsModel().setMapOfConflict(autofillConflictMap);
		}else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Внимание");
			alert.setHeaderText("Недостаточно данных для создания конфликтов");
			
			Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			
			alert.show();
		}
	}

	public void selectConflictWith() {
		String selectedConflictWithKey = listViewConflictFor.getSelectionModel().getSelectedItem().getLabelConflictNumber().getText();
		indexConflictWith = listViewConflictWith.getSelectionModel().getSelectedIndex();

		listViewConflictWith.getSelectionModel().getSelectedItem().getComboBox().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				ConflictWithDirection mirrowConflictWithDirection = new ConflictWithDirection();
				mirrowConflictWithDirection.setConflictWithDirection(selectedConflictWithKey);
			} else {
				map.get(selectedConflictWithKey).get(indexConflictWith).setConflictWithDirection(newValue);
			}
			//System.out.println("Main conflict map " + map);

			for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : map.entrySet()) {
				int key = Integer.parseInt(mapKeys.getKey());

				for (ConflictWithDirection conflictWithDirection : mapKeys.getValue()) {
					if (conflictWithDirection.getConflictWithDirection().equals("")) {
						//System.out.println("Values in map is empty");
					} else {
						int value = Integer.parseInt(conflictWithDirection.getConflictWithDirection());
						//System.out.println("Keys: " + key + " Value: " + value);
						gridPane.add(new ImageView(new Image("image/other/Warning.png")), value + 1, key + 1);
					}
				}
			}
		});
	}

	@FXML
	public void initialize() {

		//langXML();
		//loadLang(langXML);
		//createGridPane();
		
		listViewConflictFor.setPlaceholder(new Label("Нет данных для отображения"));
		listViewConflictWith.setPlaceholder(new Label("Нет данных для отображения"));
		
		gridPane.setGridLinesVisible(true);

		gridPane.setDisable(true);

		gridPane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {

				int rowIndex = 0;
				int colIndex = 0;
				int nodeIndex = 0;

				String newKey = null;
				String newValue = null;

				ConflictWithDirection conflictWithDirection = null;

				for (javafx.scene.Node node : gridPane.getChildren()) {

					if (node instanceof StackPane) {
						if (node.localToScene(node.getBoundsInLocal()).contains(e.getSceneX(), e.getSceneY())) {
							rowIndex = GridPane.getRowIndex(node);
							colIndex = GridPane.getColumnIndex(node);
							nodeIndex = gridPane.getChildren().indexOf(node);
							//System.out.println("Node: " + node + " at " + rowIndex + "/" + colIndex + " , index " + nodeIndex);

							newKey = Integer.toString(rowIndex - 1);
							newValue = Integer.toString(colIndex - 1);

							if (((StackPane) node).getChildren().size() == 0) {
								((Pane) node).getChildren().clear();
								((StackPane) node).getChildren().add(new ImageView(new Image("image/other/Warning.png")));

								conflictWithDirection = new ConflictWithDirection();
								ConflictWithDirection mirrorConflictWithDirection = new ConflictWithDirection();

								conflictWithDirection.setConflictWithDirection(newValue);
								mirrorConflictWithDirection.setConflictWithDirection(newKey);

								map.get(newKey).add(conflictWithDirection);
								map.get(newValue).add(mirrorConflictWithDirection);
								//System.out.println(map.get(newKey));
								//System.out.println(map.get(newValue));

								ObservableList<String> observableList = FXCollections.observableArrayList();
								observableList.add(newKey);

								ConflictWithHBoxCell newConflictWithHBoxCell = new ConflictWithHBoxCell();
								newConflictWithHBoxCell.getComboBox().setValue(newValue);
								newConflictWithHBoxCell.setObservableListComboBox(observableList);
								conflictWithDirectionHBoxCellList.add(newConflictWithHBoxCell);
								conflictWithDirectionObservableList = FXCollections.observableList(conflictWithDirectionHBoxCellList);
								listViewConflictWith.setItems(conflictWithDirectionObservableList);

							} else {
								((StackPane) node).getChildren().clear();

								for (Map.Entry<String, List<ConflictWithDirection>> mapKeys : map.entrySet()) {
									for (ConflictWithDirection conflictWithDirectionDelete : mapKeys.getValue()) {
										if (conflictWithDirectionDelete.getConflictWithDirection().equals(newKey)) {
											map.get(newValue).remove(conflictWithDirectionDelete);
											//System.out.println(map.get(newValue) + " AFTER DELETE");
											listViewConflictWith.getItems().remove(listViewConflictWith.getItems().size() - 1);

										}
									}
								}
							}
						}
					}
				}
				StackPane stackPane = new StackPane(new ImageView(new Image("image/other/Warning.png")));
				gridPane.add(stackPane, rowIndex, colIndex);
			}
		});
	}
}
