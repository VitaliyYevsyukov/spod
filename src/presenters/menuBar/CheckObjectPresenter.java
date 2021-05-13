package presenters.menuBar;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import presenters.directions.RoadDirection;
import presenters.object.TypeKDK;
import presenters.phase.OpenDirectionInCurrentPhaseHBoxCell;
import presenters.phase.RoadPhase;
import presenters.programs.PhaseInProgram;
import presenters.programs.RoadProgram;
import presenters.programs.ScheduleCalendarDateHBoxCell;
import presenters.programs.ScheduleCalendarWeekDayHBoxCell;
import presenters.programs.ScheduleProgram;
import presenters.programs.SwitchPhase;
import presenters.promtactu.InterphaseTransitionsHBoxCell;
import presenters.promtactu.PromtactData;
import roadModel.IRoadModel;
import roadModel.RoadModel;
import sourceKDAR.SourceKDAR;

public class CheckObjectPresenter {

	@FXML
	ListView<Text> listViewErrors;
	@FXML 
	Button btnCheck, btnSave;
	
	File dcFile;
	
	RoadModel roadModel;
	
	
	public void show(RoadModel roadModel) {
		this.roadModel = roadModel;
	}
			
	public void save() {
		System.out.println("========= Click 'SAVE BUTTON' ===========");
		System.out.println();
		
		DirectoryChooser directoryChooser = new DirectoryChooser();
    	dcFile = directoryChooser.showDialog(null);
    	if(dcFile != null) {
	    	if(dcFile == null || dcFile.getPath().equals("C:\\")) {
	    		System.out.println("Path not exist");
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Ошибка");
				alert.setHeaderText("Укажите другой путь");
				
				Stage mainStage = new Stage();
				mainStage = (Stage)alert.getDialogPane().getScene().getWindow();
				mainStage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
				
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.OK) {
					dcFile = directoryChooser.showDialog(null);
				}
	    	}else {
	    		System.out.println("Path exist");
	    		SourceKDAR kdar = new SourceKDAR(roadModel, "test");
	    		kdar.createCycle(dcFile);
	    		kdar.createConfig(dcFile);
	    		kdar.createConnectSettingFile(dcFile);
	    		kdar.createBinary(dcFile);
	    		
	    		Alert alert = new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Информация");
	    		alert.setHeaderText("Файлы прошивки КДАР успешно сохранены\n " + dcFile.getPath());
	    		
	    		Stage stage = new Stage();
				stage = (Stage)alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("image/other/komkon_logo_title.png"));
				
	    		alert.show();
	    		System.out.println("The files cycle.xml and config.xml was created");
	    	}
    	}
	}

	public void checkEvent() {
		System.out.println("========= Click 'CHECK BUTTON' ===========");
		System.out.println();
		
		Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram = roadModel.getRoadProgramsModel().getMapOfPhasesInProgram();
		Map<RoadProgram, List<SwitchPhase>> mapOfSwichPhase = roadModel.getRoadProgramsModel().getMapOfSwichPhase();
		List<RoadPhase> roadPhasesList = roadModel.getRoadPhaseModel().getRoadPhaseList();
		Map<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> mapOfInterphaseSpecificPromtact = roadModel.getRoadPromtactuModel().getMapOfInterphaseSpecificPromtact();
		Map<String, PromtactData> mapOfBasePromtact = roadModel.getRoadDirectionModel().getMapOfBasePromtact();
		
		Map<String, String> mapOfChannels = new LinkedHashMap<>();
		
		List<String> errorsList = new LinkedList<>();
		
		for(Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {		// remove empty phase in program
			List<PhaseInProgram> phaseInPrograms = entry.getValue();
			
			phaseInPrograms.removeIf(phase -> phase.getPhaseInProgramNumber() == null || phase.getDurationPhaseInProgram() == null);
			
		}
		

		// CHECK IS CONTROLER 
		if(roadModel.getRoadObjectModel().getRoadObjectTypeOfKDK().equals("")) {
			String error = "- Укажите тип используемого контроллера!\nДальнейшая проверка невозможна.";
			errorsList.add(error);
		}else {
			
			// CHECH IS PROTOKOL
			if(roadModel.getRoadObjectModel().getRoadObjectProtocol().equals("")) {
				String error = "- Укажите протокол связи для контроллера";
				errorsList.add(error);
			}
			
			// CHECK NETWORK ADDRESS
			if(roadModel.getRoadObjectModel().getRoadObjectNetworkAddress().equals("0") || roadModel.getRoadObjectModel().getRoadObjectNetworkAddress().equals("1") || 
					roadModel.getRoadObjectModel().getRoadObjectNetworkAddress().equals("63") || roadModel.getRoadObjectModel().getRoadObjectNetworkAddress().equals("255")) {
				String error = "- Укажите другое значение для сетевого адресса\n(0, 1, 63, 255 не могут быть использованны)";
				errorsList.add(error);
			}
			
			
			///////////////////////////////////////////////////////////////////////////////////////////
			
			// CHECK IS DIRECTION
			if(roadModel.getRoadDirectionModel().getRoadDirectionList().isEmpty()) {
				String error = "- Список направлений пуст";
				errorsList.add(error);
			}else {
				List<RoadDirection> roadDirectionsList = roadModel.getRoadDirectionModel().getRoadDirectionList();

				List<RoadDirection> tramDirectionsList = new ArrayList<>();
				for(RoadDirection roadDirection : roadDirectionsList){
					String type = roadDirection.getRoadDirections_typeOfDirection().getTypDirection();
					if(type.equals("Трамвайное налево") || type.equals("Трамвайное прямо") || type.equals("Трамвайное направо")){
						tramDirectionsList.add(roadDirection);
					}
				}

				boolean exist = true;
				String str = "";
				for(RoadDirection tramDirection : tramDirectionsList){
					String greenChannel = tramDirection.getRoadDirections_chanal_3();
					String redChannel = tramDirection.getRoadDirections_chanal_1();
					String yellowChannel = tramDirection.getRoadDirections_chanal_2();
					if(!greenChannel.equals("")){
						if(tramDirection.getRoadDirections_typeOfDirection().getTypDirection().equals("Трамвайное налево")){
							str = greenChannel;
						}else{
							if(!str.equals(greenChannel)){
								exist = false;
							}
						}
					}else{
						String error = "- В таблице направлений, в трамвайной группе, укажите значение\nдля зеленого канала";
						errorsList.add(error);
					}

					if(redChannel.equals("")){
						String error = "- В таблице направлений, в трамвайной группе, укажите значение\nдля красного канала";
						errorsList.add(error);
					}

					if(!yellowChannel.equals("")){
						String error = "- В таблице направлений, в трамвайной группе, удалите значение\nдля желтого канала";
						errorsList.add(error);
					}

				}
				if(exist == false){
					String error = "- Для работы трамвайного светофора должны учавствовать\nвсе три трамвайных направления. Убедитесь " +
							"в правильности\nзаполнения таблицы направлений";
					errorsList.add(error);
				}

				//Map<String, String> mapOfChannels = new LinkedHashMap<>();
				for(RoadDirection roadDirection : roadDirectionsList) {
					if(roadDirection.getRoadDirections_number().equals("")) {
						String error = "- В таблице направлений, присутствует направление\nбез номера";
						errorsList.add(error);
					}else {

						// Check repeating numbers
						List<Integer> numbersOfDirections = new ArrayList<>();
						for(RoadDirection existedRoadDirection : roadDirectionsList){
							String number = existedRoadDirection.getRoadDirections_number();
							numbersOfDirections.add(Integer.parseInt(number));
						}

						Set<Integer> set = new HashSet<>(numbersOfDirections);
						if(set.size() < numbersOfDirections.size()){
							String error = "- В списке направлений имеются повторяющиеся номера";
							errorsList.add(error);
						}
						//////////////////////////////////////////////////////////////////////////

						String typeDirection = roadDirection.getRoadDirections_typeOfDirection().getTypDirection();

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

							if(!typeDirection.equals("Трамвайное налево") && !typeDirection.equals("Трамвайное прямо") && !typeDirection.equals("Трамвайное направо")){
								if(mapOfChannels.containsKey(ch3)) {
									String error = "- В направлении № " + roadDirection.getRoadDirections_number() + " дублируется номер\nзеленого канала";
									errorsList.add(error);
								}else {
									mapOfChannels.put(ch3, roadDirection.getRoadDirections_number());
								}
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
						
						String usedTypeKDK = roadModel.getRoadObjectModel().getRoadObjectTypeOfKDK();		// check max allowed channel size
						List<TypeKDK> typeKDKsList = roadModel.getRoadObjectModel().getKdkTypeList();
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
				
				for(RoadDirection direction : roadDirectionsList) {		// check control
					String number = direction.getRoadDirections_number();
					String control_1 = direction.getRoadDirections_control_1();
					String control_2 = direction.getRoadDirections_control_2();
					String channel_red = direction.getRoadDirections_chanal_1();
					String channel_addit_red = direction.getRoadDirections_chanal_4();
					
					if(control_1.equals(control_2) && !control_1.equals("")) {
						String error = "- В направлении № " + number + " Контроль 1 и Контроль 2 - не могут\nиметь одинаковые значения";
						errorsList.add(error);
					}else {
						if(!channel_addit_red.equals("") && !direction.getRoadDirections_typeOfDirection().getTypDirection().equals("Транспортное два красных")) {
							String error = "- Тип направления № " + number + " не поддерживает установку\nканала № 4";
							errorsList.add(error);
						}else {
							if(!control_1.equals("")) {
								if(!(control_1.equals(channel_red) || control_1.equals(channel_addit_red))) {
									String error = "- В направлении № " + number + " Контроль № 1 не соответствует ни одному\nканалу красного из этого направления";
									errorsList.add(error);
								}
							}
							if(!control_2.equals("")) {
								if(!(control_2.equals(channel_red) || control_2.equals(channel_addit_red))) {
									String error = "- В направлении № " + number + " Контроль № 2 не соответствует ни одному\nканалу красного из этого направления";
									errorsList.add(error);
								}
							}
						}	
					}
				}
				
				for(RoadDirection direction : roadDirectionsList) {		// check base promtact
					String dirNumber = direction.getRoadDirections_number();
					
					for(Map.Entry<String, PromtactData> entryPromtact : mapOfBasePromtact.entrySet()) {
						if(dirNumber.equals(entryPromtact.getKey())) {
							PromtactData promtactData = entryPromtact.getValue();
							
							if(promtactData.getRoadPromtactu_endGreenAddit().equals("")) {
								String error = "- В направлении № " + dirNumber + " не установлен базовый промтакт\nдля конца зеленого дополнительного";
								errorsList.add(error);
							}
							if(promtactData.getRoadPromtactu_durationGreenBlink().equals("")) {
								String error = "- В направлении № " + dirNumber + " не установлен базовый промтакт\nдля конца зеленого мигающего";
								errorsList.add(error);
							}
							if(promtactData.getRoadPromtactu_durationYellow().equals("")) {
								String error = "- В направлении № " + dirNumber + " не установлен базовый промтакт\nдля конца желтого";
								errorsList.add(error);
							}
							if(promtactData.getRoadPromtactu_endRed().equals("")) {
								String error = "- В направлении № " + dirNumber + " не установлен базовый промтакт\nдля конца красного";
								errorsList.add(error);
							}
							if(promtactData.getRoadPromtactu_durationRedYellow().equals("")) {
								String error = "- В направлении № " + dirNumber + " не установлен базовый промтакт\nдля конца красно-желтого";
								errorsList.add(error);
							}
							
						}
					}
					
				}
				
			}
			///////////////////////////////////////////////////////////////////////////////////////////
			
			// CHECK IS PHASE
			if(roadModel.getRoadPhaseModel().getRoadPhaseList().isEmpty()) {
				String error = "- Список фаз пуст";
				errorsList.add(error);
			}else {
				roadPhasesList = roadModel.getRoadPhaseModel().getRoadPhaseList();
				Map<String, String> mapOfTVP = new LinkedHashMap<>();
				
				List<Integer> listOf = new ArrayList<>();
				List<Integer> tempList = new ArrayList<>();
				for(RoadPhase existPhase : roadPhasesList) {
					listOf.add(Integer.parseInt(existPhase.getRoadPhase_number()));
				}
				for(Integer integer : listOf) {
					if(!tempList.contains(integer)){
	                    tempList.add(integer);
	                }else {
	                	String error = "- В таблице фаз присутствуют фазы с\nодинаковым номером - " + integer;
						errorsList.add(error);
	                }
				}
				
				for(RoadPhase roadPhase : roadPhasesList) {
					if(roadPhase.getRoadPhase_number().equals("")) {
						String error = "- В таблице фаз, присутствует фаза\nбез номера";
						errorsList.add(error);
					}
					if(roadPhase.getRoadPhase_Tmin().equals("")) {
						String error = "- В фазе № " + roadPhase.getRoadPhase_number() + "укажите\nзначение Т Min";
						errorsList.add(error);
					}
					
					String type = roadPhase.getRoadPhase_phaseTVP().getTvp();
					
					if(!type.equals("Отсутствует")) {
						if(mapOfTVP.containsKey(type)) {
							String error = "- В фазе № " + roadPhase.getRoadPhase_number() + " используется тип ТВП который ранее\nуже был включен в фазе № " + mapOfTVP.get(type);
							errorsList.add(error);
							
						}else {							
							mapOfTVP.put(type, roadPhase.getRoadPhase_number());
							
							if(type.equals("ТВП - 1") || type.equals("ТВП - 2")) {
								if(mapOfTVP.containsKey("ТВП - 1 и ТВП - 2")) {
									String error = "- В фазе № " + roadPhase.getRoadPhase_number() + " используется тип ТВП который ранее\nуже был включен в фазе № " + mapOfTVP.get("ТВП - 1 и ТВП - 2");
									errorsList.add(error);
								}
								if(mapOfTVP.containsKey("ТВП - 1 или ТВП - 2")) {
									String error = "- В фазе № " + roadPhase.getRoadPhase_number() + " используется тип ТВП который ранее\nуже был включен в фазе № " + mapOfTVP.get("ТВП - 1 или ТВП - 2");
									errorsList.add(error);
								}
								
							}else if(type.equals("ТВП - 1 и ТВП - 2")){
								if(mapOfTVP.containsKey("ТВП - 1")) {
									String error = "- В фазе № " + roadPhase.getRoadPhase_number() + " используется тип ТВП который ранее\nуже был включен в фазе № " + mapOfTVP.get("ТВП - 1");
									errorsList.add(error);
								}
								if(mapOfTVP.containsKey("ТВП - 2")) {
									String error = "- В фазе № " + roadPhase.getRoadPhase_number() + " используется тип ТВП который ранее\nуже был включен в фазе № " + mapOfTVP.get("ТВП - 2");
									errorsList.add(error);
								}
								if(mapOfTVP.containsKey("ТВП - 1 или ТВП - 2")) {
									String error = "- В фазе № " + roadPhase.getRoadPhase_number() + " используется тип ТВП который ранее\nуже был включен в фазе № " + mapOfTVP.get("ТВП - 1 или ТВП - 2");
									errorsList.add(error);
								}
							}else if(type.equals("ТВП - 1 или ТВП - 2")){
								if(mapOfTVP.containsKey("ТВП - 1")) {
									String error = "- В фазе № " + roadPhase.getRoadPhase_number() + " используется тип ТВП который ранее\nуже был включен в фазе № " + mapOfTVP.get("ТВП - 1");
									errorsList.add(error);
								}
								if(mapOfTVP.containsKey("ТВП - 2")) {
									String error = "- В фазе № " + roadPhase.getRoadPhase_number() + " используется тип ТВП который ранее\nуже был включен в фазе № " + mapOfTVP.get("ТВП - 2");
									errorsList.add(error);
								}
								if(mapOfTVP.containsKey("ТВП - 1 и ТВП - 2")) {
									String error = "- В фазе № " + roadPhase.getRoadPhase_number() + " используется тип ТВП который ранее\nуже был включен в фазе № " + mapOfTVP.get("ТВП - 1 и ТВП - 2");
									errorsList.add(error);
								}
							}
						}
					}
					
					if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("Отсутствует")) {
						if(roadPhase.getRoadPhase_panelTVP_1() != null || roadPhase.getRoadPhase_panelTVP_2() != null) {
							if(!roadPhase.getRoadPhase_panelTVP_1().equals("") || !roadPhase.getRoadPhase_panelTVP_2().equals("")) {
								String error = "- Фаза № "+ roadPhase.getRoadPhase_number() + " не поддерживает ТВП, поэтому задать\nканал индикатора табло(ТВП1 ТПВ2) невозможно";
								errorsList.add(error);
							}
						}
					}
					
					if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 1")) {
						String error;
						if(!roadPhase.getRoadPhase_panelTVP_2().equals("")) {
							error = "- В фазе № " + roadPhase.getRoadPhase_number() + "используется ТВП - 1 в которой\nзадать значение канала табло ТВП2 невозможно";
							errorsList.add(error);
						}else {
							String usedTypeKDK = roadModel.getRoadObjectModel().getRoadObjectTypeOfKDK();
							List<TypeKDK> typeKDKsList = roadModel.getRoadObjectModel().getKdkTypeList();
							for(TypeKDK typeKDK : typeKDKsList) {
								if(usedTypeKDK.equals(typeKDK.getName_KDK())) {
									String maxChannels = typeKDK.getChanels();
									int max_channels = Integer.parseInt(maxChannels);
									
									int table_1 = 0;
									int table_2 = 0;
									if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 1")) {
										if(!roadPhase.getRoadPhase_panelTVP_1().equals("")) {
										
											table_1 = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_1());
											if(table_1 > max_channels) {
												error = "- В фазе № " + roadPhase.getRoadPhase_number() + " канал табло ТВП1 превышает поддерживаемое\nданным контроллером количество каналов!"
														+ " " + "(максимум " + max_channels + ")";
												errorsList.add(error);
											}
											
										}
										
										if(mapOfChannels.containsKey(roadPhase.getRoadPhase_panelTVP_1())) {
											error = "- В фазе № " + roadPhase.getRoadPhase_number() + " указан канал табло ТВП1 № " + roadPhase.getRoadPhase_panelTVP_1() +
													", который\nуже задействован в направлениях";
											errorsList.add(error);
										}
										
									}
									if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 2")) {
										if(!roadPhase.getRoadPhase_panelTVP_2().equals("")) {
										
											table_2 = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_2());
											if(table_2 > max_channels) {
												error = "- В фазе № " + roadPhase.getRoadPhase_number() + " канал табло ТВП2 превышает поддерживаемое\nданным контроллером количество каналов!"
														+ " " + "(максимум " + max_channels + ")";
												errorsList.add(error);
											}
										
										}
										
										if(mapOfChannels.containsKey(roadPhase.getRoadPhase_panelTVP_2())) {
											error = "- В фазе № " + roadPhase.getRoadPhase_number() + " указан канал табло ТВП2 № " + roadPhase.getRoadPhase_panelTVP_2() +
													", который\nуже задействован в направлениях";
											errorsList.add(error);
										}
									}
								}
							}
						}
					}
					if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 2")) {
						String error;
						if(!roadPhase.getRoadPhase_panelTVP_1().equals("")) {
							error = "- В фазе № " + roadPhase.getRoadPhase_number() + "используется ТВП - 2 в которой\nзадать значение канала табло ТВП1 невозможно";
							errorsList.add(error);
						}else {
							String usedTypeKDK = roadModel.getRoadObjectModel().getRoadObjectTypeOfKDK();
							List<TypeKDK> typeKDKsList = roadModel.getRoadObjectModel().getKdkTypeList();
							for(TypeKDK typeKDK : typeKDKsList) {
								if(usedTypeKDK.equals(typeKDK.getName_KDK())) {
									String maxChannels = typeKDK.getChanels();
									int max_channels = Integer.parseInt(maxChannels);
									
									int table_1 = 0;
									int table_2 = 0;
									if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 1")) {
										if(!roadPhase.getRoadPhase_panelTVP_1().equals("")) {
											table_1 = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_1());
										}
										if(table_1 > max_channels) {
											error = "- В фазе № " + roadPhase.getRoadPhase_number() + " канал табло ТВП1 превышает поддерживаемое\nданным контроллером количество каналов!"
													+ " " + "(максимум " + max_channels + ")";
											errorsList.add(error);
										}
										
										if(mapOfChannels.containsKey(roadPhase.getRoadPhase_panelTVP_1())) {
											error = "- В фазе № " + roadPhase.getRoadPhase_number() + " указан канал табло ТВП1 № " + roadPhase.getRoadPhase_panelTVP_1() +
													", который\nуже задействован в направлениях";
											errorsList.add(error);
										}
										
									}
									if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 2")) {
										if(!roadPhase.getRoadPhase_panelTVP_2().equals("")) {
											table_2 = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_2());
										}
										if(table_2 > max_channels) {
											error = "- В фазе № " + roadPhase.getRoadPhase_number() + " канал табло ТВП2 превышает поддерживаемое\nданным контроллером количество каналов!"
													+ " " + "(максимум " + max_channels + ")";
											errorsList.add(error);
										}
										
										if(mapOfChannels.containsKey(roadPhase.getRoadPhase_panelTVP_2())) {
											error = "- В фазе № " + roadPhase.getRoadPhase_number() + " указан канал табло ТВП2 № " + roadPhase.getRoadPhase_panelTVP_2() +
													", который\nуже задействован в направлениях";
											errorsList.add(error);
										}
									}
								}
							}
						}
					}
					
					if(roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 1 и ТВП - 2") || roadPhase.getRoadPhase_phaseTVP().getTvp().equals("ТВП - 1 или ТВП - 2")) {
						String usedTypeKDK = roadModel.getRoadObjectModel().getRoadObjectTypeOfKDK();
						List<TypeKDK> typeKDKsList = roadModel.getRoadObjectModel().getKdkTypeList();
						String error;
						for(TypeKDK typeKDK : typeKDKsList) {
							if(usedTypeKDK.equals(typeKDK.getName_KDK())) {
								String maxChannels = typeKDK.getChanels();
								int max_channels = Integer.parseInt(maxChannels);
								
								int table_1 = 0;
								int table_2 = 0;
								
								if(!roadPhase.getRoadPhase_panelTVP_1().equals("")) {
									table_1 = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_1());
									if(table_1 > max_channels) {
										error = "- В фазе № " + roadPhase.getRoadPhase_number() + " канал табло ТВП1 превышает поддерживаемое\nданным контроллером количество каналов!"
												+ " " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
									
									if(mapOfChannels.containsKey(roadPhase.getRoadPhase_panelTVP_1())) {
										error = "- В фазе № " + roadPhase.getRoadPhase_number() + " указан канал табло ТВП1 № " + roadPhase.getRoadPhase_panelTVP_1() +
												", который\nуже задействован в направлениях";
										errorsList.add(error);
									}
								}
								
								if(!roadPhase.getRoadPhase_panelTVP_2().equals("")) {
									table_2 = Integer.parseInt(roadPhase.getRoadPhase_panelTVP_2());
									if(table_2 > max_channels) {
										error = "- В фазе № " + roadPhase.getRoadPhase_number() + " канал табло ТВП2 превышает поддерживаемое\nданным контроллером количество каналов!"
												+ " " + "(максимум " + max_channels + ")";
										errorsList.add(error);
									}
									
									if(mapOfChannels.containsKey(roadPhase.getRoadPhase_panelTVP_2())) {
										error = "- В фазе № " + roadPhase.getRoadPhase_number() + " указан канал табло ТВП2 № " + roadPhase.getRoadPhase_panelTVP_2() +
												", который\nуже задействован в направлениях";
										errorsList.add(error);
									}
								}
							}
						}
					}
				}
			}
			///////////////////////////////////////////////////////////////////////////////////////////
			
			// CHECK IS PROGRAM
			if(roadModel.getRoadProgramsModel().getRoadProgramList().isEmpty()) {
				String error = "- Список программ пуст";
				errorsList.add(error);
			}else {
				List<RoadProgram> roadProgramsList = roadModel.getRoadProgramsModel().getRoadProgramList();
				for(RoadProgram roadProgram : roadProgramsList) {
					if(roadProgram.getRoadProgram_number().equals("")) {
						String error = "- В таблице программ, присутствует программа\nбез номера";
						errorsList.add(error);
					}
				}
				
				for(RoadProgram existedRoadProgram : roadProgramsList) {
					if(!existedRoadProgram.getRoadProgram_programMode().getMode().equals("Замена фаз")) {
						
						// check phases in program(until GM and OS)
						List<PhaseInProgram> phaseInProgramsList = mapOfPhasesInProgram.get(existedRoadProgram);
						
						String type = existedRoadProgram.getRoadProgram_programMode().getMode();
						
						if(!type.equals("Желтое мигание") && !type.equals("Отключение светофора")) {
							
							if(phaseInProgramsList.isEmpty()) {
								String error = "- В программе № " + existedRoadProgram.getRoadProgram_number() + " не задано\nни одной рабочей фазы";
								errorsList.add(error);
							}else {
								for(PhaseInProgram phaseInProgram : phaseInProgramsList) {
									if(phaseInProgram.getDurationPhaseInProgram() == null) {
										String error = "- В программе № " + existedRoadProgram.getRoadProgram_number() + " , в рабочей фазе № " + phaseInProgram.getPhaseInProgramNumber().getPhaseNumber()
												+ " не указана длительность";
										errorsList.add(error);
									}
									
									if(phaseInProgram.getPhaseInProgramNumber() == null) {
										String error = "- В программе № " + existedRoadProgram.getRoadProgram_number() + " присутствует фазы без номера";
										errorsList.add(error);
									}
									
								}
							}
						}
						/*for(Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {
							
							String type = entry.getKey().getRoadProgram_programMode().getMode();
							
							if(!type.equals("Желтое мигание") && !type.equals("Отключение светофора")) {
								
								List<PhaseInProgram> phaseInPrograms = entry.getValue();
								if(phaseInPrograms.isEmpty()) {
									String error = "- В программе № " + entry.getKey().getRoadProgram_number() + " не задано\nни одной рабочей фазы";
									errorsList.add(error);
								}else {
									
									for(PhaseInProgram phaseInProgram : phaseInPrograms) {
										if(phaseInProgram.getDurationPhaseInProgram() == null) {
											String error = "- В программе № " + entry.getKey().getRoadProgram_number() + " , в рабочей фазе № " + phaseInProgram.getPhaseInProgramNumber().getPhaseNumber()
													+ " не указана длительность";
											errorsList.add(error);
										}
										
										if(phaseInProgram.getPhaseInProgramNumber() == null) {
											String error = "- В программе № " + entry.getKey().getRoadProgram_number() + " присутствует фазы без номера";
											errorsList.add(error);
										}
										
									}
									
								}
								
							}
							
						}*/
						
					}else {
						
						List<SwitchPhase> swichPhasesList = mapOfSwichPhase.get(existedRoadProgram);
						if(swichPhasesList.isEmpty()) {
							String error = "- В программе № " + existedRoadProgram.getRoadProgram_number() + " не задано\nни одной конфигурации 'Замена фаз'";
							errorsList.add(error);
						
						}else {
							for(SwitchPhase swichPhase : swichPhasesList) {
								if(swichPhase.getPhase().getPhaseNumber() == null || swichPhase.getToPhase1().getPhaseNumber() == null ||
										swichPhase.getMainTime() == null || swichPhase.getPromtact() == null || swichPhase.getDurationPhase() == null) {
									String error = "- В программе № " + existedRoadProgram.getRoadProgram_number() + " заполните все поля\nконфигурации 'Замена фаз'";
									errorsList.add(error);
								}
							}
						}
					}
				}
				
				
				// check phases in program(until GM and OS)
				/*for(Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {
					
					String type = entry.getKey().getRoadProgram_programMode().getMode();
					
					if(!type.equals("Желтое мигание") && !type.equals("Отключение светофора")) {
						
						List<PhaseInProgram> phaseInPrograms = entry.getValue();
						if(phaseInPrograms.isEmpty()) {
							String error = "- В программе № " + entry.getKey().getRoadProgram_number() + " не задано\nни одной рабочей фазы";
							errorsList.add(error);
						}else {
							
							for(PhaseInProgram phaseInProgram : phaseInPrograms) {
								if(phaseInProgram.getDurationPhaseInProgram() == null) {
									String error = "- В программе № " + entry.getKey().getRoadProgram_number() + " , в рабочей фазе № " + phaseInProgram.getPhaseInProgramNumber().getPhaseNumber()
											+ " не указана длительность";
									errorsList.add(error);
								}
								
								if(phaseInProgram.getPhaseInProgramNumber() == null) {
									String error = "- В программе № " + entry.getKey().getRoadProgram_number() + " присутствует фазы без номера";
									errorsList.add(error);
								}
								
							}
							
						}
						
					}
					
				}*/
				
				
				
				// check schedule of weekday
				Map<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> mapOfWeekCalendar = roadModel.getRoadProgramsModel().getMapOfWeekCalendar();
				for(Map.Entry<ScheduleCalendarWeekDayHBoxCell, List<ScheduleProgram>> entry : mapOfWeekCalendar.entrySet()) {
					
					List<ScheduleProgram> scheduleProgramsList = entry.getValue();
					if(scheduleProgramsList.isEmpty()) {
						String error = "- На " + entry.getKey().getWeekDay().getText() + " не задано расписание программ";
						errorsList.add(error);
					}else {
						List<String> elements = new ArrayList<>();
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
				Map<ScheduleCalendarDateHBoxCell, List<ScheduleProgram>> mapOfDay = roadModel.getRoadProgramsModel().getMapOfDateCalendar();
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
			Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> mapOfOpenDirectionInPhase = roadModel.getRoadPhaseModel().getMapOpenDirectionInPhase();
			if(mapOfInterphaseSpecificPromtact.isEmpty()) {
				String error = "- Не задано ни одного межфазного перехода";
				errorsList.add(error);
			}else {
				
				for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entryInterphase : mapOfInterphaseSpecificPromtact.entrySet()) {
					String fromPhase = entryInterphase.getKey().getComboBoxFromPhase().getValue();
					String toPhase = entryInterphase.getKey().getComboBoxToPhase().getValue();
					
					Map<String, PromtactData> specPromMap = entryInterphase.getValue();
					
					List<String> allDirectionsInInterphse = new LinkedList<>();
					
					if(!specPromMap.isEmpty()) {						
						
						List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromPhase = mapOfOpenDirectionInPhase.get(fromPhase);	// get open directions list from phase
						List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToPhase = mapOfOpenDirectionInPhase.get(toPhase);		// get open directions list to phase
						
						List<String> openDirectionsFrom = new LinkedList<>();
						List<String> openDirectionsTo = new LinkedList<>();
						
						
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
						
						for(String directionFrom : openDirectionsFrom) {
							if(!allDirectionsInInterphse.contains(directionFrom)) {
								allDirectionsInInterphse.add(directionFrom);
							}
						}
						for(String directionTo : openDirectionsTo) {
							if(!allDirectionsInInterphse.contains(directionTo)) {
								allDirectionsInInterphse.add(directionTo);
							}
						}
						
						List<String> directionPromtact = new ArrayList<String>(allDirectionsInInterphse);
						
						for(String dir : directionPromtact) {
							if(openDirectionsFrom.contains(dir) & openDirectionsTo.contains(dir)) {
								allDirectionsInInterphse.remove(dir);
							}
						}
						
						for(Map.Entry<String, PromtactData> entryPromtact : specPromMap.entrySet()) {
							String direction = entryPromtact.getKey();
							PromtactData promtactData = entryPromtact.getValue();
							
							if(promtactData.isFullPromtact()) {
								
								if(promtactData.getRoadPromtactu_endGreenAddit().equals("")) {
									String error = "- В межфазном переходе с фазы № " + fromPhase + " в фазу № " + toPhase + " \nв направлении № " + direction + 
											" не задано значение промтакта\nзелёного дополнительного.";
									errorsList.add(error);
								}
								if(promtactData.getRoadPromtactu_durationGreenBlink().equals("")) {
									String error = "- В межфазном переходе с фазы № " + fromPhase + " в фазу № " + toPhase + " \nв направлении № " + direction + 
											" не задано значение промтакта\nзелёного мигающего.";
									errorsList.add(error);
								}
								if(promtactData.getRoadPromtactu_durationYellow().equals("")) {
									String error = "- В межфазном переходе с фазы № " + fromPhase + " в фазу № " + toPhase + " \nв направлении № " + direction + 
											" не задано значение промтакта\nжелтого.";
									errorsList.add(error);
								}
								if(promtactData.getRoadPromtactu_durationRedYellow().equals("")) {
									String error = "- В межфазном переходе с фазы № " + fromPhase + " в фазу № " + toPhase + " \nв направлении № " + direction + 
											" не задано значение промтакта\nкрасно-желтого.";
									errorsList.add(error);
								}
								if(promtactData.getRoadPromtactu_endRed().equals("")) {
									String error = "- В межфазном переходе с фазы № " + fromPhase + " в фазу № " + toPhase + " \nв направлении № " + direction + 
											" не задано значение промтакта\nкрасного.";
									errorsList.add(error);
								}
								
							}else {
								allDirectionsInInterphse.remove(direction);
								if(openDirectionsFrom.contains(direction)) {
									if(promtactData.getRoadPromtactu_endGreenAddit().equals("")) {
										String error = "- В межфазном переходе с фазы № " + fromPhase + " в фазу № " + toPhase + " \nв направлении № " + direction + 
												" не задано значение промтакта\nзелёного дополнительного.";
										errorsList.add(error);
									}
									if(promtactData.getRoadPromtactu_durationGreenBlink().equals("")) {
										String error = "- В межфазном переходе с фазы № " + fromPhase + " в фазу № " + toPhase + " \nв направлении № " + direction + 
												" не задано значение промтакта\nзелёного мигающего.";
										errorsList.add(error);
									}
									if(promtactData.getRoadPromtactu_durationYellow().equals("")) {
										String error = "- В межфазном переходе с фазы № " + fromPhase + " в фазу № " + toPhase + " \nв направлении № " + direction + 
												" не задано значение промтакта\nжелтого.";
										errorsList.add(error);
									}
									
								}else {
									if(promtactData.getRoadPromtactu_durationRedYellow().equals("")) {
										String error = "- В межфазном переходе с фазы № " + fromPhase + " в фазу № " + toPhase + " \nв направлении № " + direction + 
												" не задано значение промтакта\nкрасно-желтого.";
										errorsList.add(error);
									}
									if(promtactData.getRoadPromtactu_endRed().equals("")) {
										String error = "- В межфазном переходе с фазы № " + fromPhase + " в фазу № " + toPhase + " \nв направлении № " + direction + 
												" не задано значение промтакта\nкрасного.";
										errorsList.add(error);
									}
									
								}
							}
							
						}
						
					}else {
						String error = "- В вкладке 'Промтакты', при межфазном переходе\nс фазы " + fromPhase + " в фазу " + toPhase +
								" не указаны направления";
						errorsList.add(error);
					}
					
					if(!allDirectionsInInterphse.isEmpty()) {						
						String notUsedDirections = String.join(",", allDirectionsInInterphse);
						
						String error = "- В межфазном переходе с фазы № " + fromPhase + " в фазу № " + toPhase + " не указанны\nследующие направления["
								+ notUsedDirections + "]";
						errorsList.add(error);						
					}
					
				}
				
				
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

						if(phaseInProgramFrom.getDurationPhaseInProgram() != null) {
							String phaseDurationFrom = phaseInProgramFrom.getDurationPhaseInProgram(); // duration phase in program
							duration = Integer.parseInt(phaseDurationFrom);
						}

						for (RoadPhase roadPhase : roadPhasesList) {
							if (phaseInProgramNumberFrom.equals(roadPhase.getRoadPhase_number())) {
								String tMinPhase = roadPhase.getRoadPhase_Tmin(); // tMin phase
								tMin = Integer.parseInt(tMinPhase);
							}
						}

						for (Map.Entry<InterphaseTransitionsHBoxCell, Map<String, PromtactData>> entryPromtact : mapOfInterphaseSpecificPromtact.entrySet()) {
							String specPromFrom = entryPromtact.getKey().getComboBoxFromPhase().getValue();
							String specPromTo = entryPromtact.getKey().getComboBoxToPhase().getValue();

							if (phaseInProgramNumberFrom.equals(specPromFrom) && phaseInProgramNumberTo.equals(specPromTo)) {
								isSpecPromtact = true;
								Map<String, PromtactData> specPromMap = entryPromtact.getValue();
								
								if(!specPromMap.isEmpty()) {
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
						}
						if (isSpecPromtact == false) {
							Map<String, List<OpenDirectionInCurrentPhaseHBoxCell>> openDirectionsInPhase = roadModel.getRoadPhaseModel().getMapOpenDirectionInPhase();
							List<OpenDirectionInCurrentPhaseHBoxCell> diferent = new ArrayList<>();
							allPromtactValues = new ArrayList<>();

							List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsFromList = openDirectionsInPhase.get(phaseInProgramNumberFrom);
							List<OpenDirectionInCurrentPhaseHBoxCell> openDirectionsToList = openDirectionsInPhase.get(phaseInProgramNumberTo);

							if(openDirectionsFromList != null) {
							
								for (OpenDirectionInCurrentPhaseHBoxCell existed : openDirectionsFromList) {
									
									if(openDirectionsToList != null) {
									
										if (!openDirectionsToList.contains(existed)) {
											diferent.add(existed);
										}
									
									}
									
								}
							}
							for (OpenDirectionInCurrentPhaseHBoxCell difDirection : diferent) {
								if (mapOfBasePromtact.containsKey(difDirection.getComboBox().getValue())) {
									PromtactData promtactData = mapOfBasePromtact.get(difDirection.getComboBox().getValue());
									String value = promtactData.getRoadPromtactu_endGreenAddit(); //

									allPromtactValues.add(Integer.parseInt(value));
								}
							}
							
							diferent.clear();
							
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
							String error = "- Указанная длительность фазы № " + phaseInProgramNumberFrom + "\nв программе № " + entry.getKey().getRoadProgram_number() + " меньше допустимого.";
							errorsList.add(error);
						}
					}
				}
			}
			//////////////////////////////////////////////////////////////////////////////////////////
			
		}				
		
		if (!errorsList.isEmpty()) {
			
			List<String> notRepeatedErrorsList = errorsList.stream().distinct().collect(Collectors.toList());	// remove repeated string from list
			
			List<Text> errorListText = new ArrayList<>();
			for(String errorString : notRepeatedErrorsList) {		// Convert string errors to text
				Text errorText = new Text(errorString);
				errorListText.add(errorText);
			}
			
			listViewErrors.setItems(FXCollections.observableArrayList(errorListText));		// set errors item in list view
			String finding = "- Проверка данных выявила ошибки!\nЗапись флэш-модуля невозможна,\nпока все найденные ошибки не будут исправлены.";
			Text determination = new Text(finding);
			determination.setFill(Color.RED);
			listViewErrors.getItems().add(determination);
			
		}else {
			
			btnCheck.setVisible(false);
			btnSave.setVisible(true);
			
			String finding = "- Проверка данных завершилась успешно!\nОшибки не были обнаружены, можете переходить\nк прошивке флэш-модуля.";
			Text determination = new Text(finding);
			determination.setFill(Color.GREEN);
			listViewErrors.getItems().add(determination);
			
		}
		
	}
	
}
