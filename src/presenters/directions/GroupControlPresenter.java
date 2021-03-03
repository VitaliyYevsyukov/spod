package presenters.directions;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import presenters.object.TypeKDK;
import roadModel.IRoadModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Vitaly on 13.12.2017.
 */
public class GroupControlPresenter {

    @FXML
    private Button buttonOK;
    @FXML
    private ListView<GroupControlHBoxCell> listViewGroupControl;
    @FXML
    private ListView<ControlledChanelHBoxCell> listViewControlledChannel;

    GroupControl groupControl;
    ControlledChannel controlledChannel;

    GroupControlHBoxCell groupControlHBoxCell;
    ControlledChanelHBoxCell controlledChanelHBoxCell;

    List<ControlledChanelHBoxCell> controlledChannelList;
    List<GroupControlHBoxCell> groupControlHBoxCellList = new ArrayList<>();
    
    List<RoadDirection> roadDirectionList;
    IRoadModel iRoadModel;

    ObservableList<GroupControlHBoxCell> groupControlHBoxCellObservableList;
    ObservableList<ControlledChanelHBoxCell> controlledChanelHBoxCellObservableList;

    Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> groupControlHBoxCellListMap;
    
    public boolean buttonOKWasPressed(){
        if(groupControlHBoxCellListMap.isEmpty()){
            return true;
        }else
            return false;
    }
    public void pressOKButton(){
        System.out.println("-----Group control is ready-----");
        
        Stage stage = (Stage) buttonOK.getScene().getWindow();
        stage.close();
        buttonOKWasPressed();
    }
    
    public void createGroupControl(){
        System.out.println("================ 'Create Group Control' ================");
        System.out.println();
        
        if(!roadDirectionList.isEmpty()) {
        	if(listViewGroupControl.getItems().size() < 16) {
	        	listViewControlledChannel.getItems().clear();
		        int sizeListViewGroupControl = listViewGroupControl.getItems().size() + 1;
		        String numberOfGroupControl = Integer.toString(sizeListViewGroupControl);
		        
		        groupControlHBoxCell = new GroupControlHBoxCell();
		        groupControlHBoxCell.getNumberOfControl().setText(numberOfGroupControl);
		        
		        groupControlHBoxCellList.add(groupControlHBoxCell);
		        groupControlHBoxCellObservableList = FXCollections.observableArrayList(groupControlHBoxCellList);
		        listViewGroupControl.setItems(groupControlHBoxCellObservableList);
		        
		        listViewGroupControl.getSelectionModel().select(listViewGroupControl.getItems().size() - 1);
		        
		        controlledChannelList = new ArrayList<>();
		        
		        groupControlHBoxCellListMap.put(groupControlHBoxCell, controlledChannelList);
        	}else {
        		Alert alert = new Alert(AlertType.INFORMATION);
            	alert.setTitle("Внимание");
            	alert.setHeaderText("Вы не можете создать больше 16 групп");
            	
            	Stage stage = new Stage();
    			stage = (Stage)alert.getDialogPane().getScene().getWindow();
    			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
            	
            	alert.show();
        	}
        }else {
        	Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("Внимание");
        	alert.setHeaderText("Создайте направления");
        	
        	Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
        	
        	alert.show();
        }
    }
    public void deleteGroupControl(){
        System.out.println("============ Delete 'Group Control' ============");

        groupControlHBoxCell = listViewGroupControl.getSelectionModel().getSelectedItem();
        groupControlHBoxCellListMap.remove(groupControlHBoxCell);
        
        listViewControlledChannel.getItems().clear();
        listViewGroupControl.getItems().clear();
        groupControlHBoxCellList.clear();
        if(!groupControlHBoxCellListMap.isEmpty()) {
	        for(Map.Entry<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> entry : groupControlHBoxCellListMap.entrySet()) {
	        	groupControlHBoxCellList.add(entry.getKey());
	        	groupControlHBoxCellObservableList = FXCollections.observableArrayList(groupControlHBoxCellList);
	        	listViewGroupControl.setItems(groupControlHBoxCellObservableList);
		        listViewGroupControl.getSelectionModel().selectFirst();
	        }
	        List<ControlledChanelHBoxCell> controlledChanelHBoxCellsList = groupControlHBoxCellListMap.get(listViewGroupControl.getSelectionModel().getSelectedItem());
	        if(!controlledChanelHBoxCellsList.isEmpty()) {
	        	controlledChannelList = new ArrayList<>();
	        	for(ControlledChanelHBoxCell controlledChanelHBoxCell : controlledChanelHBoxCellsList) {
	        		controlledChannelList.add(controlledChanelHBoxCell);
	        		controlledChanelHBoxCellObservableList = FXCollections.observableList(controlledChannelList);
	        		listViewControlledChannel.setItems(controlledChanelHBoxCellObservableList);
	        		listViewControlledChannel.getSelectionModel().selectFirst();
	        	}
	        }
        }
       
    }
    
    public void createControlledChannel(){
        System.out.println("============== Create 'Controlled Channel' ============== ");
        
        groupControlHBoxCell = listViewGroupControl.getSelectionModel().getSelectedItem();
        if(groupControlHBoxCell != null) {
        	ObservableList<String> directionNumberObservableList = FXCollections.observableArrayList();
        	for(RoadDirection roadDirection : roadDirectionList) {
        		if(!roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Поворотная стрелка")) {
        			directionNumberObservableList.add(roadDirection.getRoadDirections_number());
        		}
			}
        	
        	if(listViewControlledChannel.getItems().size() > 0) {
        		for(ControlledChanelHBoxCell controlledChanelHBoxCell : listViewControlledChannel.getItems()) {
        			String existDirection = controlledChanelHBoxCell.getComboBoxDirection().getValue();
        			directionNumberObservableList.removeIf(direction -> direction.equals(existDirection));
        		}
        	}
        	List<ControlledChanelHBoxCell> controlledChanelHBoxCellsList = groupControlHBoxCellListMap.get(groupControlHBoxCell);
        	ControlledChanelHBoxCell previousControllChannel = listViewControlledChannel.getSelectionModel().getSelectedItem();
        	if(previousControllChannel != null) {
        		if(previousControllChannel.getComboBoxDirection().getValue() != null) {
		        	if(!directionNumberObservableList.isEmpty()) {
		        		if(controlledChanelHBoxCellsList.isEmpty()) {
		        			controlledChanelHBoxCell = new ControlledChanelHBoxCell();
					        controlledChanelHBoxCell.setObservableListComboBoxDirection(directionNumberObservableList);
					        controlledChanelHBoxCellsList.add(controlledChanelHBoxCell);
					        controlledChanelHBoxCellObservableList = FXCollections.observableArrayList(controlledChanelHBoxCellsList);
					        listViewControlledChannel.setItems(controlledChanelHBoxCellObservableList);
					        listViewControlledChannel.getSelectionModel().select(listViewControlledChannel.getItems().size() - 1);
					        
					        controlledChanelHBoxCell.getComboBoxDirection().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
					        	if(newValue != null) {
					        		for(RoadDirection roadDirection : roadDirectionList) {
					        			if(roadDirection.getRoadDirections_chanal_1() != null) {
					        				if(newValue.equals(roadDirection.getRoadDirections_number())) {
					        					if(!roadDirection.getRoadDirections_chanal_1().equals("")) {
					        						controlledChanelHBoxCell.getTextFieldChanel().setText(roadDirection.getRoadDirections_chanal_1());
					        						break;
					        					}else {
						        					Alert alert = new Alert(AlertType.WARNING);
							        				alert.setTitle("Внимание");
							        				alert.setHeaderText("Заполните каналы для направлений, которые\nхотите использовать для контроля");
							        				
							        				Stage stage = new Stage();
							        				stage = (Stage)alert.getDialogPane().getScene().getWindow();
							        				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
							        				
							        				alert.show();
							        				groupControlHBoxCellListMap.get(groupControlHBoxCell).remove(controlledChanelHBoxCell);
							        				listViewControlledChannel.getItems().remove(controlledChanelHBoxCell);
							        				break;
												}
					        				}
					        			}
					        		}
					        	}
					        });
		        		}else {
		        			if(controlledChanelHBoxCellsList.size() < 4) {
			        			controlledChanelHBoxCell = new ControlledChanelHBoxCell();
						        controlledChanelHBoxCell.setObservableListComboBoxDirection(directionNumberObservableList);
						        controlledChanelHBoxCellsList.add(controlledChanelHBoxCell);
						        controlledChanelHBoxCellObservableList = FXCollections.observableArrayList(controlledChanelHBoxCellsList);
						        listViewControlledChannel.setItems(controlledChanelHBoxCellObservableList);
						        listViewControlledChannel.getSelectionModel().select(listViewControlledChannel.getItems().size() - 1);
						        
						        controlledChanelHBoxCell.getComboBoxDirection().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
						        	if(newValue != null) {
						        		for(RoadDirection roadDirection : roadDirectionList) {
						        			if(roadDirection.getRoadDirections_chanal_1() != null) {
						        				if(newValue.equals(roadDirection.getRoadDirections_number())) {
						        					if(!roadDirection.getRoadDirections_chanal_1().equals("")) {
						        						controlledChanelHBoxCell.getTextFieldChanel().setText(roadDirection.getRoadDirections_chanal_1());
						        						break;
						        					}else {
							        					Alert alert = new Alert(AlertType.WARNING);
								        				alert.setTitle("Внимание");
								        				alert.setHeaderText("Заполните каналы для направлений, которые\nхотите использовать для контроля");
								        				
								        				Stage stage = new Stage();
								        				stage = (Stage)alert.getDialogPane().getScene().getWindow();
								        				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
								        				
								        				alert.show();
								        				groupControlHBoxCellListMap.get(groupControlHBoxCell).remove(controlledChanelHBoxCell);
								        				listViewControlledChannel.getItems().remove(controlledChanelHBoxCell);
								        				break;
													}
						        				}
						        			}
						        		}
						        	}
						        });
		        			}else {
		        				Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("Внимание");
								alert.setHeaderText("Вы не можете создать больше 4 каналов\nв одной группе");
								
								Stage stage = new Stage();
								stage = (Stage)alert.getDialogPane().getScene().getWindow();
								stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
								
								alert.show();
		        			}
		        		}
		        	}else {
		        		Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Внимание");
						alert.setHeaderText("Все возможные контролируемые направления\nв группе созданы");
						
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
        		controlledChanelHBoxCell = new ControlledChanelHBoxCell();
		        controlledChanelHBoxCell.setObservableListComboBoxDirection(directionNumberObservableList);
		        controlledChanelHBoxCellsList.add(controlledChanelHBoxCell);
		        controlledChanelHBoxCellObservableList = FXCollections.observableArrayList(controlledChanelHBoxCellsList);
		        listViewControlledChannel.setItems(controlledChanelHBoxCellObservableList);
		        listViewControlledChannel.getSelectionModel().select(listViewControlledChannel.getItems().size() - 1);
		        
		        controlledChanelHBoxCell.getComboBoxDirection().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
		        	if(newValue != null) {
		        		for(RoadDirection roadDirection : roadDirectionList) {
		        			if(roadDirection.getRoadDirections_chanal_1() != null) {
		        				if(newValue.equals(roadDirection.getRoadDirections_number())) {
		        					if(!roadDirection.getRoadDirections_chanal_1().equals("")) {
		        						controlledChanelHBoxCell.getTextFieldChanel().setText(roadDirection.getRoadDirections_chanal_1());
		        						break;
		        					}else {
			        					Alert alert = new Alert(AlertType.WARNING);
				        				alert.setTitle("Внимание");
				        				alert.setHeaderText("Заполните каналы для направлений, которые\nхотите использовать для контроля");
				        				
				        				Stage stage = new Stage();
				        				stage = (Stage)alert.getDialogPane().getScene().getWindow();
				        				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
				        				
				        				alert.show();
				        				groupControlHBoxCellListMap.get(groupControlHBoxCell).remove(controlledChanelHBoxCell);
				        				listViewControlledChannel.getItems().remove(controlledChanelHBoxCell);
				        				break;
									}
		        				}
		        			}
		        		}
		        	}
		        });
        	}
        }else {
        	Alert alert = new Alert(AlertType.WARNING);
        	alert.setTitle("Внимание");
        	alert.setHeaderText("Создайте группу контроля");
        	
        	Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
        	
        	alert.show();
        }
        
        
        
        /*if(groupControlHBoxCell != null) {
        	ObservableList<String> directionNumberObservableList = FXCollections.observableArrayList();
        	for(RoadDirection roadDirection : roadDirectionList) {
        		if(!roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Поворотная стрелка")) {
        			directionNumberObservableList.add(roadDirection.getRoadDirections_number());
        		}
			}
        	
        	for(Map.Entry<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> entry : groupControlHBoxCellListMap.entrySet()) {
        		List<ControlledChanelHBoxCell> controlledChanelList = entry.getValue();
        		for(ControlledChanelHBoxCell controlledChanelHBoxCell : controlledChanelList) {
        			String existDirection = controlledChanelHBoxCell.getComboBoxDirection().getValue();
        			directionNumberObservableList.removeIf(direction -> direction.equals(existDirection));
        		}
        	}
        	
        	if(!directionNumberObservableList.isEmpty()) {
        		List<ControlledChanelHBoxCell> controlledChanelHBoxCellsList = groupControlHBoxCellListMap.get(groupControlHBoxCell);
        		if(controlledChanelHBoxCellsList.isEmpty()) {
	        		controlledChanelHBoxCell = new ControlledChanelHBoxCell();
			        controlledChanelHBoxCell.setObservableListComboBoxDirection(directionNumberObservableList);
			        controlledChanelHBoxCellsList.add(controlledChanelHBoxCell);
			        controlledChanelHBoxCellObservableList = FXCollections.observableArrayList(controlledChanelHBoxCellsList);
			        listViewControlledChannel.setItems(controlledChanelHBoxCellObservableList);
			        listViewControlledChannel.getSelectionModel().select(listViewControlledChannel.getItems().size() - 1);
			        
			        controlledChanelHBoxCell.getComboBoxDirection().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			        	if(newValue != null) {
			        		for(RoadDirection roadDirection : roadDirectionList) {
			        			if(roadDirection.getRoadDirections_chanal_1() != null) {
				        			if(newValue.equals(roadDirection.getRoadDirections_number())) {
				        				if(!roadDirection.getRoadDirections_chanal_1().equals("")) {
				        					controlledChanelHBoxCell.getTextFieldChanel().setText(roadDirection.getRoadDirections_chanal_1());
				        					break;
				        				}else {
				        					Alert alert = new Alert(AlertType.WARNING);
					        				alert.setTitle("Внимание");
					        				alert.setHeaderText("Заполните каналы для направлений, которые\nхотите использовать для контроля");
					        				
					        				Stage stage = new Stage();
					        				stage = (Stage)alert.getDialogPane().getScene().getWindow();
					        				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
					        				
					        				alert.show();
					        				groupControlHBoxCellListMap.get(groupControlHBoxCell).remove(controlledChanelHBoxCell);
					        				listViewControlledChannel.getItems().remove(controlledChanelHBoxCell);
					        				break;
										}
				        			}
			        			}else {
			        				Alert alert = new Alert(AlertType.WARNING);
			        				alert.setTitle("Внимание");
			        				alert.setHeaderText("Заполните каналы для направлений, которые\nхотите использовать для контроля");
			        				
			        				Stage stage = new Stage();
			        				stage = (Stage)alert.getDialogPane().getScene().getWindow();
			        				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
			        				
			        				alert.show();
			        				groupControlHBoxCellListMap.get(groupControlHBoxCell).remove(controlledChanelHBoxCell);
			        				listViewControlledChannel.getItems().remove(controlledChanelHBoxCell);
			        				break;
			        			}
			        		}
			        	}
			        });
        		}
        		if(controlledChanelHBoxCellsList.size() == 1) {
        			String number = listViewControlledChannel.getSelectionModel().getSelectedItem().getComboBoxDirection().getValue();
        			for(RoadDirection direction : roadDirectionList) {
        				if(direction.getRoadDirections_number().equals(number)) {
        					String type = direction.getRoadDirections_typeOfDirection().getTypDirection();
        					if(type.equals("Транспортное два красных")) {
        						controlledChanelHBoxCell = new ControlledChanelHBoxCell();
	        			        controlledChanelHBoxCell.getComboBoxDirection().setValue(number);
	        			        controlledChanelHBoxCell.getTextFieldChanel().setText(direction.getRoadDirections_chanal_4());
	        			        controlledChanelHBoxCellsList.add(controlledChanelHBoxCell);
	        			        controlledChanelHBoxCellObservableList = FXCollections.observableArrayList(controlledChanelHBoxCellsList);
	        			        listViewControlledChannel.setItems(controlledChanelHBoxCellObservableList);
	        			        listViewControlledChannel.getSelectionModel().select(listViewControlledChannel.getItems().size() - 1);
        					}else {
        						Alert alert = new Alert(AlertType.ERROR);
	        					alert.setTitle("Ошибка");
	        					alert.setHeaderText("Для этого направления создать второй\nконтролируемый канал невозможно");
	        					
	        					Stage stage = new Stage();
	        					stage = (Stage)alert.getDialogPane().getScene().getWindow();
	        					stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
	        					
	        					alert.show();
        					}
        				}
        			}
        		}else {
        			Alert alert = new Alert(AlertType.WARNING);
        			alert.setTitle("Внимание");
        			alert.setHeaderText("Вы не можете создать больше двух\nконтролируемых направлений в данной группе");
        			
        			Stage stage = new Stage();
        			stage = (Stage)alert.getDialogPane().getScene().getWindow();
        			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
        			
        			alert.show();
        		}
        		
        	}else {
        		
        		List<ControlledChanelHBoxCell> controlledChanelList = groupControlHBoxCellListMap.get(listViewGroupControl.getSelectionModel().getSelectedItem());
        		if(controlledChanelList.size() == 1) {
        			String number = listViewControlledChannel.getSelectionModel().getSelectedItem().getComboBoxDirection().getValue();        			
        			for(RoadDirection roadDirection : roadDirectionList) {
            			if(number.equals(roadDirection.getRoadDirections_number())) {
            				if(roadDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное два красных")){
            					controlledChanelHBoxCell = new ControlledChanelHBoxCell();
	        			        controlledChanelHBoxCell.getComboBoxDirection().setValue(number);
	        			        controlledChanelHBoxCell.getTextFieldChanel().setText(roadDirection.getRoadDirections_chanal_4());
	        			        controlledChanelList.add(controlledChanelHBoxCell);
	        			        controlledChanelHBoxCellObservableList = FXCollections.observableArrayList(controlledChanelList);
	        			        listViewControlledChannel.setItems(controlledChanelHBoxCellObservableList);
	        			        listViewControlledChannel.getSelectionModel().select(listViewControlledChannel.getItems().size() - 1);
            				}else {
            					Alert alert = new Alert(AlertType.WARNING);
            					alert.setTitle("Внимание");
            					alert.setHeaderText("Для этого направления создать второй\nконтролируемый канал невозможно");
            					
            					Stage stage = new Stage();
            					stage = (Stage)alert.getDialogPane().getScene().getWindow();
            					stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
            					
            					alert.show();
            				}
            			}
            		}
        		}else {
        			Alert alert = new Alert(AlertType.WARNING);
    				alert.setTitle("Внимание");
    				alert.setHeaderText("Все возможные группы\nконтроля созданы");
    				
    				Stage stage = new Stage();
    				stage = (Stage)alert.getDialogPane().getScene().getWindow();
    				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
    				
    				alert.show();
        		}				
			}
        	
        }else {
        	Alert alert = new Alert(AlertType.WARNING);
        	alert.setTitle("Внимание");
        	alert.setHeaderText("Создайте группу контроля");
        	
        	Stage stage = new Stage();
			stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
        	
        	alert.show();
        }*/
    }
    public void deleteControlledChannel(){

        System.out.println("============== Delete 'Controlled Channel' ==============");
        
        groupControlHBoxCell = listViewGroupControl.getSelectionModel().getSelectedItem();
        int indexToDelete = listViewControlledChannel.getSelectionModel().getSelectedIndex();
        
        if(indexToDelete >= 0) {
        	groupControlHBoxCellListMap.get(groupControlHBoxCell).remove(listViewControlledChannel.getSelectionModel().getSelectedItem());
        	listViewControlledChannel.getItems().remove(indexToDelete);
        }
        

    }
    
    public void selectGroupControl(){
        System.out.println("========= Select 'Group Control' =========");
        listViewControlledChannel.getItems().clear();
        List<ControlledChanelHBoxCell> controlledChanelHBoxCellsList = groupControlHBoxCellListMap.get(listViewGroupControl.getSelectionModel().getSelectedItem());
    	controlledChannelList = new ArrayList<>();
    	for(ControlledChanelHBoxCell controlledChanelHBoxCell : controlledChanelHBoxCellsList) {
    		controlledChannelList.add(controlledChanelHBoxCell);
    		controlledChanelHBoxCellObservableList = FXCollections.observableList(controlledChannelList);
    		listViewControlledChannel.setItems(controlledChanelHBoxCellObservableList);
    		listViewControlledChannel.getSelectionModel().selectFirst();
    	}
    }
    public void selectControlledChannel(){

        System.out.println("-----Select 'Controlled Channel'----- ");

    }
    

    public void autoFillEvent() {
    	System.out.println();
    	System.out.println("=============== Autofill clicked ===============");
    	
    	groupControlHBoxCellList.clear();
    	listViewGroupControl.getItems().clear();
    	List<TypeKDK> typeKDKsList = iRoadModel.getModel().getRoadObjectModel().getKdkTypeList();
    	for(TypeKDK existedKDKType : typeKDKsList) {
    		if(existedKDKType.getName_KDK().equals(iRoadModel.getModel().getRoadObjectModel().getRoadObjectTypeOfKDK())) {
    			
    			int channels = Integer.parseInt(existedKDKType.getChanels());
    			System.out.println("Number of kdk channels " + channels);
    			
    			if(!roadDirectionList.isEmpty()) {
    				
    				ObservableList<String> directionNumberObservableList = FXCollections.observableArrayList();
    		        for(RoadDirection roadDirection : roadDirectionList){
    		            directionNumberObservableList.add(roadDirection.getRoadDirections_number());
    		        }
    		        
    		        for(RoadDirection roadDirection : roadDirectionList) {
    		        	String typeDirection = roadDirection.getRoadDirections_typeOfDirection().getTypDirection();
    		        	if(!typeDirection.equals("Поворотная стрелка")) {
    		        		if(!typeDirection.equals("Транспортное два красных")) {
    		        			groupControlHBoxCell = new GroupControlHBoxCell();
    		        			controlledChannelList = new ArrayList<>();
    			    	        groupControlHBoxCell.getNumberOfControl().setText(Integer.toString(listViewGroupControl.getItems().size() + 1));
    			    	        groupControlHBoxCellList.add(groupControlHBoxCell);
    			    	        groupControlHBoxCellObservableList = FXCollections.observableArrayList(groupControlHBoxCellList);
    			    	        listViewGroupControl.setItems(groupControlHBoxCellObservableList);
    			    	        listViewGroupControl.getSelectionModel().selectFirst();
    			    	        
    			    	        controlledChanelHBoxCell = new ControlledChanelHBoxCell();
    			
    			    	        controlledChanelHBoxCell.setObservableListComboBoxDirection(directionNumberObservableList);
    			    	        controlledChanelHBoxCell.getComboBoxDirection().setValue(roadDirection.getRoadDirections_number());
    			    	        controlledChanelHBoxCell.getTextFieldChanel().setText(roadDirection.getRoadDirections_chanal_1());
    			    	        controlledChannelList.add(controlledChanelHBoxCell);
    			    	        
    			    	        groupControlHBoxCellListMap.put(groupControlHBoxCell, controlledChannelList);
    			    	        
    		        		}else {
    		        			groupControlHBoxCell = new GroupControlHBoxCell();
    		        			controlledChannelList = new ArrayList<>();
    			    	        groupControlHBoxCell.getNumberOfControl().setText(Integer.toString(listViewGroupControl.getItems().size() + 1));
    			    	        groupControlHBoxCellList.add(groupControlHBoxCell);
    			    	        groupControlHBoxCellObservableList = FXCollections.observableArrayList(groupControlHBoxCellList);
    			    	        listViewGroupControl.setItems(groupControlHBoxCellObservableList);
    			    	        listViewGroupControl.getSelectionModel().selectFirst();
    		        			
    			    	        controlledChanelHBoxCell = new ControlledChanelHBoxCell();
    			    			
    			    	        controlledChanelHBoxCell.setObservableListComboBoxDirection(directionNumberObservableList);
    			    	        controlledChanelHBoxCell.getComboBoxDirection().setValue(roadDirection.getRoadDirections_number());
    			    	        controlledChanelHBoxCell.getTextFieldChanel().setText(roadDirection.getRoadDirections_chanal_1());
    			    	        controlledChannelList.add(controlledChanelHBoxCell);
    			    	        
    			    	        groupControlHBoxCellListMap.put(groupControlHBoxCell, controlledChannelList);
    		        			
    		        		}
    		        	}
    		        }
    		        
    		        List<ControlledChanelHBoxCell> controlledChanelHBoxCellsList = groupControlHBoxCellListMap.get(listViewGroupControl.getSelectionModel().getSelectedItem());
    		    	controlledChannelList = new ArrayList<>();
    		    	for(ControlledChanelHBoxCell controlledChanelHBoxCell : controlledChanelHBoxCellsList) {
    		    		controlledChannelList.add(controlledChanelHBoxCell);
    		    		controlledChanelHBoxCellObservableList = FXCollections.observableList(controlledChannelList);
    		    		listViewControlledChannel.setItems(controlledChanelHBoxCellObservableList);
    		    		listViewControlledChannel.getSelectionModel().selectFirst();
    		    	}
    		        
    			}else {
    	        	Alert alert = new Alert(AlertType.INFORMATION);
    	        	alert.setTitle("Внимание");
    	        	alert.setHeaderText("Создайте направления");
    	        	
    	        	Stage stage = new Stage();
    				stage = (Stage)alert.getDialogPane().getScene().getWindow();
    				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
    	        	
    	        	alert.show();
    	        }
    		}
    	}    	
    }

    public void show(IRoadModel iRoadModel, List<RoadDirection> roadDirectionList, Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> groupControlHBoxCellListMap){
    	this.iRoadModel = iRoadModel;
        this.roadDirectionList = roadDirectionList;
        this.groupControlHBoxCellListMap = groupControlHBoxCellListMap;
        
        groupControlHBoxCellList.clear();
        ObservableList<String> directionNumberObservableList = FXCollections.observableArrayList();
        for(RoadDirection roadDirection : roadDirectionList){
            directionNumberObservableList.add(roadDirection.getRoadDirections_number());
        }
        
        if(!groupControlHBoxCellListMap.isEmpty()) {
        	for(Map.Entry<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> entry : groupControlHBoxCellListMap.entrySet()) {
        		groupControlHBoxCellList.add(entry.getKey());
        		groupControlHBoxCellObservableList = FXCollections.observableArrayList(groupControlHBoxCellList);
        		listViewGroupControl.setItems(groupControlHBoxCellObservableList);
    	        listViewGroupControl.getSelectionModel().selectFirst();
        	}
        	
        	List<ControlledChanelHBoxCell> controlledChanelHBoxCellsList = groupControlHBoxCellListMap.get(listViewGroupControl.getSelectionModel().getSelectedItem());
        	controlledChannelList = new ArrayList<>();
        	for(ControlledChanelHBoxCell controlledChanelHBoxCell : controlledChanelHBoxCellsList) {
        		controlledChannelList.add(controlledChanelHBoxCell);
        		controlledChanelHBoxCellObservableList = FXCollections.observableList(controlledChannelList);
        		listViewControlledChannel.setItems(controlledChanelHBoxCellObservableList);
        		listViewControlledChannel.getSelectionModel().selectFirst();
        	}
        }
    }

    public Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> getGroupControlHBoxCellListMap() {


        return groupControlHBoxCellListMap;
    }

    public void setGroupControlHBoxCellListMap(Map<GroupControlHBoxCell, List<ControlledChanelHBoxCell>> groupControlHBoxCellListMap) {

        this.groupControlHBoxCellListMap = groupControlHBoxCellListMap;
    }



    @FXML
    public void initialize(){
    	
    	listViewControlledChannel.setPlaceholder(new Label("Нет данных для отображения"));
    	listViewGroupControl.setPlaceholder(new Label("Нет данных для отображения"));
    	
    	listViewControlledChannel.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    		if(newValue != null) {
    			for(ControlledChanelHBoxCell controlledChanelHBoxCell : controlledChanelHBoxCellObservableList) {
    				controlledChanelHBoxCell.getComboBoxDirection().setDisable(true);
    				controlledChanelHBoxCell.getTextFieldChanel().setDisable(true);
    			}
    			newValue.getComboBoxDirection().setDisable(false);
    			newValue.getTextFieldChanel().setDisable(false);
    		}
    	});
    	
    }
}
