package presenters.programs;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SpeedSignTextFieldEditingCell extends HBox {
	
	private Label labelNumberOfSign = new Label();
	private Label labelRecomendSpeedSign = new Label();
	private TextField textFieldSignValue = new TextField();
	private String textId;

	
	SpeedSignTextFieldEditingCell(){
		super();
		
		labelNumberOfSign.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(labelNumberOfSign, Priority.ALWAYS);
        
        labelRecomendSpeedSign.setText(" рекомендованная скорость ");
        labelRecomendSpeedSign.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(labelRecomendSpeedSign, Priority.ALWAYS);
        
        textFieldSignValue.setPrefWidth(30);
        textFieldSignValue.setDisable(true);
        textFieldSignValue.setMaxWidth(Double.MAX_VALUE);
        textFieldSignValue.setAlignment(Pos.CENTER);
        textFieldSignValue.setStyle("-fx-background-radius: 10");
        HBox.setHgrow(textFieldSignValue, Priority.ALWAYS);
        
        this.getChildren().addAll(labelNumberOfSign, labelRecomendSpeedSign, textFieldSignValue);
        
	}
	
	public String getTextId() {
		return textId;
	}
	public void setTextId(String textId) {
		this.textId = textId;
	}
	
	public Label getLabelNumberOfSign() {
		return labelNumberOfSign;
	}

	public void setLabelNumberOfSign(Label labelNumberOfSign) {
		this.labelNumberOfSign = labelNumberOfSign;
	}

	public TextField getTextFieldSignValue() {
		return textFieldSignValue;
	}

	public void setTextFieldSignValue(TextField textFieldSignValue) {
		this.textFieldSignValue = textFieldSignValue;
	}

}
