package presenters.directions;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import presenters.promtactu.PromtactData;

import java.util.Map;
import java.util.UUID;

public class DirectionBasePromtactPresenter {

    @FXML
    private Button buttonNextDirection, buttonPreviousDirection, buttonOK;
    @FXML
    private Label labelNumberDirection;
    @FXML
    private TextField textFieldEndGreenAddit, textFieldDurationGreenBlink, textFieldDurationYellow, textFieldEndRed, textFieldDurationRedYellow;

    Map<String, PromtactData> promtactDataMap;

    
    public void clickNext(){
        String directionNumber = labelNumberDirection.getText();

        UUID uuid = UUID.randomUUID();
        String basicPromtactId = uuid.toString(); 
        
        PromtactData promtactDataWrite = promtactDataMap.get(directionNumber);
        promtactDataWrite.setPromtactId(basicPromtactId); 
        promtactDataWrite.setRoadPromtactu_endGreenAddit(textFieldEndGreenAddit.getText());
        promtactDataWrite.setRoadPromtactu_durationGreenBlink(textFieldDurationGreenBlink.getText());
        promtactDataWrite.setRoadPromtactu_durationYellow(textFieldDurationYellow.getText());
        promtactDataWrite.setRoadPromtactu_endRed(textFieldEndRed.getText());
        promtactDataWrite.setRoadPromtactu_durationRedYellow(textFieldDurationRedYellow.getText());

        int nextNumber = Integer.parseInt(directionNumber) + 1;
        int finalIndex = promtactDataMap.size();

        if(nextNumber > finalIndex){
            String number = promtactDataMap.keySet().stream().findFirst().get();
            labelNumberDirection.setText(number);
            PromtactData promtactData = promtactDataMap.get(number);
            textFieldEndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
            textFieldDurationGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
            textFieldDurationYellow.setText(promtactData.getRoadPromtactu_durationYellow());
            textFieldEndRed.setText(promtactData.getRoadPromtactu_endRed());
            textFieldDurationRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
        }else{
            String number = Integer.toString(nextNumber);
            labelNumberDirection.setText(number);
            for(Map.Entry<String, PromtactData> entry : promtactDataMap.entrySet()){
                if(number.equals(entry.getKey())){
                    PromtactData promtactData = entry.getValue();
                    textFieldEndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
                    textFieldDurationGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
                    textFieldDurationYellow.setText(promtactData.getRoadPromtactu_durationYellow());
                    textFieldEndRed.setText(promtactData.getRoadPromtactu_endRed());
                    textFieldDurationRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
                }
            }
        }
    }
    public void clickPrevious(){
        String directionNumber = labelNumberDirection.getText();
        
        UUID uuid = UUID.randomUUID();
        String basicPromtactId = uuid.toString();

        PromtactData promtactDataWrite = promtactDataMap.get(directionNumber);
        promtactDataWrite.setPromtactId(basicPromtactId); 
        promtactDataWrite.setRoadPromtactu_endGreenAddit(textFieldEndGreenAddit.getText());
        promtactDataWrite.setRoadPromtactu_durationGreenBlink(textFieldDurationGreenBlink.getText());
        promtactDataWrite.setRoadPromtactu_durationYellow(textFieldDurationYellow.getText());
        promtactDataWrite.setRoadPromtactu_endRed(textFieldEndRed.getText());
        promtactDataWrite.setRoadPromtactu_durationRedYellow(textFieldDurationRedYellow.getText());

        int previosNumber = Integer.parseInt(directionNumber) - 1;
        int finalIndex = promtactDataMap.size();

        if(previosNumber == 0){
            String number = Integer.toString(finalIndex);
            labelNumberDirection.setText(number);
            PromtactData promtactData = promtactDataMap.get(number);
            textFieldEndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
            textFieldDurationGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
            textFieldDurationYellow.setText(promtactData.getRoadPromtactu_durationYellow());
            textFieldEndRed.setText(promtactData.getRoadPromtactu_endRed());
            textFieldDurationRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
        }else{
            String number = Integer.toString(previosNumber);
            labelNumberDirection.setText(number);
            PromtactData promtactData = promtactDataMap.get(number);
            textFieldEndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
            textFieldDurationGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
            textFieldDurationYellow.setText(promtactData.getRoadPromtactu_durationYellow());
            textFieldEndRed.setText(promtactData.getRoadPromtactu_endRed());
            textFieldDurationRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
        }


    }
    public void pressButtonOK(){
        String directionNumber = labelNumberDirection.getText();

        UUID uuid = UUID.randomUUID();
        String basicPromtactId = uuid.toString();
        
        PromtactData promtactDataWrite = promtactDataMap.get(directionNumber);
        promtactDataWrite.setPromtactId(basicPromtactId);
        promtactDataWrite.setRoadPromtactu_directionNumber(directionNumber);
        promtactDataWrite.setRoadPromtactu_endGreenAddit(textFieldEndGreenAddit.getText());
        promtactDataWrite.setRoadPromtactu_durationGreenBlink(textFieldDurationGreenBlink.getText());
        promtactDataWrite.setRoadPromtactu_durationYellow(textFieldDurationYellow.getText());
        promtactDataWrite.setRoadPromtactu_endRed(textFieldEndRed.getText());
        promtactDataWrite.setRoadPromtactu_durationRedYellow(textFieldDurationRedYellow.getText());

        Stage stage = (Stage) buttonOK.getScene().getWindow();
        stage.close();
        ok();
    }

    public Map<String, PromtactData> getPromtactDataMap() {
        return promtactDataMap;
    }
    public boolean ok(){
        return true;
    }
    public void mapOfDirection(Map<String, PromtactData> promtactDataMap){
        this.promtactDataMap = promtactDataMap;

        String numberDirection = promtactDataMap.keySet().stream().findFirst().get();
        labelNumberDirection.setText(numberDirection);

        if(!promtactDataMap.isEmpty()){
            PromtactData promtactData = promtactDataMap.get(numberDirection);
            textFieldEndGreenAddit.setText(promtactData.getRoadPromtactu_endGreenAddit());
            textFieldDurationGreenBlink.setText(promtactData.getRoadPromtactu_durationGreenBlink());
            textFieldDurationYellow.setText(promtactData.getRoadPromtactu_durationYellow());
            textFieldEndRed.setText(promtactData.getRoadPromtactu_endRed());
            textFieldDurationRedYellow.setText(promtactData.getRoadPromtactu_durationRedYellow());
        }

    }


    @FXML
    public void initialize(){
    	
    	textFieldEndGreenAddit.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldEndGreenAddit.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
    	
    	textFieldDurationGreenBlink.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldDurationGreenBlink.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
    	
    	textFieldDurationYellow.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldDurationYellow.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
    	
    	textFieldEndRed.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldEndRed.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });
    	
    	textFieldDurationRedYellow.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textFieldDurationRedYellow.setText(newValue.replaceAll("[^\\d]", ""));
				 }
			}
		 });

    }
}
