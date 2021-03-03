package presenters.menuBar;

import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import presenters.programs.PhaseInProgram;
import presenters.programs.RoadProgram;
import roadModel.RoadModel;

public class DiagramPresenter {
	
	@FXML
	Button btnSaveInFile, btnPrintDiagram;
	@FXML
	ChoiceBox<String> chAllPrograms;

	RoadModel roadModel;
	Map<RoadProgram, List<PhaseInProgram>> mapOfPhasesInProgram; 
	
	public void show(RoadModel roadModel) {
		this.roadModel = roadModel;
		System.out.println("-- Open diagram --");
		
		// add all exist program in choice box
		if(!roadModel.getRoadProgramsModel().getRoadProgramList().isEmpty()) {
			System.out.println("Program list not empty");
			
			mapOfPhasesInProgram = roadModel.getRoadProgramsModel().getMapOfPhasesInProgram();
			
			for(Map.Entry<RoadProgram, List<PhaseInProgram>> entry : mapOfPhasesInProgram.entrySet()) {
				RoadProgram roadProgram = entry.getKey();
				chAllPrograms.getItems().add(roadProgram.getRoadProgram_number());
			}
			
			chAllPrograms.getSelectionModel().selectFirst(); // select first value
		}
		
	}
	
	public void saveInFile() {
		
	}
	
	public void printDiagram() {
		
	}
	
	@FXML
	public void initialize() {
	    btnSaveInFile.setTooltip(new Tooltip("Сохранить в файл")); 
		btnPrintDiagram.setTooltip(new Tooltip("Печать циклограммы")); 
		 
	}
	
}
