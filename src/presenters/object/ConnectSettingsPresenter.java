package presenters.object;

import java.util.function.UnaryOperator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.stage.Stage;
import roadModel.IRoadModel;

public class ConnectSettingsPresenter {

	@FXML
	private ComboBox<String> comboBoxConnectType, comboBoxKDKPort, comboBoxLEDPort, comboBoxConnectSpeed, comboBoxKDPSpeed, comboBoxLEDSpeed;
	@FXML
	private TextField txtConnectPort, txtIP, txtDelay, txtDelayDarkYF, txtSleepTime, txtDNS, txtMASK, txtNTP;
	@FXML
	private Label tSpeed, tDelay, tDelayYF, tSleepTime;
	@FXML
	private Button btnOK;

	
	boolean OKwasPressed = false;

	private IRoadModel iRoadObjectModel;

	public void showSettings(IRoadModel iRoadModel) {
		this.iRoadObjectModel = iRoadModel;
		
		txtConnectPort.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectConnectPort());
		txtIP.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectIP());
		txtDNS.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectDNS());
		txtMASK.setText(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectMASK());
		txtNTP.setText(iRoadModel.getModel().getRoadObjectModel().getRoadObjectNTP());
		
		txtDelay.setText("1000");
        txtDelayDarkYF.setText("3000");
        txtSleepTime.setText("10000");
		
		/*txtDelay.setText(iRoadObjectModel.getModel().getRoadObjectModel().getDelay());
		txtDelayDarkYF.setText(iRoadObjectModel.getModel().getRoadObjectModel().getDelayYF());
		txtSleepTime.setText(iRoadObjectModel.getModel().getRoadObjectModel().getSleepTime());*/
		
        if(!iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectProtocol().equals("")) {
        	
        	if(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectProtocol().equals("Ethernet 'КОМКОН' - 115200")){
        		comboBoxConnectType.setValue("Ethernet");
        		comboBoxConnectSpeed.setValue("115200");
        		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectConnectType(comboBoxConnectType.getValue());
        	}
        }
		//comboBoxConnectType.setValue(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectConnectType());
		
		
		//comboBoxConnectSpeed.setValue(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectConnectSpeed());
		
		
		
		comboBoxKDKPort.setValue(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectKDPPort());
		comboBoxKDPSpeed.setValue(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectKDPSpeed());
		comboBoxLEDPort.setValue(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectLEDPort());
		comboBoxLEDSpeed.setValue(iRoadObjectModel.getModel().getRoadObjectModel().getRoadObjectLEDSpeed());

	}

	public boolean OKevent() {
		return true;
	}

	public void saveSettings() {
		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectConnectType(comboBoxConnectType.getValue());
		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectConnectPort(txtConnectPort.getText());
		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectIP(txtIP.getText());
		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectDNS(txtDNS.getText());
		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectMASK(txtMASK.getText());
		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectNTP(txtNTP.getText());
		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectConnectSpeed(comboBoxConnectSpeed.getValue());
		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectKDPPort(comboBoxKDKPort.getValue());
		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectKDPSpeed(comboBoxKDPSpeed.getValue());
		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectLEDPort(comboBoxLEDPort.getValue());
		iRoadObjectModel.getModel().getRoadObjectModel().setRoadObjectLEDSpeed(comboBoxLEDSpeed.getValue());
		iRoadObjectModel.getModel().getRoadObjectModel().setDelay(txtDelay.getText());
		iRoadObjectModel.getModel().getRoadObjectModel().setDelayYF(txtDelayDarkYF.getText());
		iRoadObjectModel.getModel().getRoadObjectModel().setSleepTime(txtSleepTime.getText());

		Stage stage = (Stage) btnOK.getScene().getWindow();
		stage.close();
		OKevent();

	}

	private String makePartialIPRegex() {
        String partialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))" ;
        String subsequentPartialBlock = "(\\."+partialBlock+")" ;
        String ipAddress = partialBlock+"?"+subsequentPartialBlock+"{0,3}";
        return "^"+ipAddress ;
    }
	
	@FXML
	public void initialize() {
		
		String regex = makePartialIPRegex();
        final UnaryOperator<Change> ipAddressFilter = c -> {
            String text = c.getControlNewText();
            if  (text.matches(regex)) {
                return c ;
            } else {
                return null ;
            }
        };
        txtIP.setTextFormatter(new TextFormatter<>(ipAddressFilter));
        txtDNS.setTextFormatter(new TextFormatter<>(ipAddressFilter));
        txtMASK.setTextFormatter(new TextFormatter<>(ipAddressFilter));

		comboBoxConnectType.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals("GPRS")) {
				tSleepTime.setDisable(true);
				txtSleepTime.setDisable(true);
				tSpeed.setDisable(false);
				comboBoxConnectSpeed.setDisable(false);
				txtConnectPort.setText("");
				txtConnectPort.setText("ttyUSB0");
				comboBoxKDKPort.getSelectionModel().selectFirst();
				comboBoxLEDPort.getSelectionModel().selectFirst();
			} else {
				txtConnectPort.setText("4002");
				tSleepTime.setDisable(false);
				txtSleepTime.setDisable(false);
				comboBoxKDKPort.getSelectionModel().selectFirst();
				comboBoxLEDPort.getSelectionModel().selectFirst();
			}
		});

		comboBoxConnectType.setItems(FXCollections.observableArrayList("Ethernet", "GPRS"));
		comboBoxConnectSpeed.setItems(FXCollections.observableArrayList("115200", "1200"));
		comboBoxKDKPort.setItems(FXCollections.observableArrayList("ttyS3"));
		comboBoxLEDPort.setItems(FXCollections.observableArrayList("ttyS1"));
		comboBoxKDPSpeed.setItems(FXCollections.observableArrayList("115200", "1200"));
		comboBoxLEDSpeed.setItems(FXCollections.observableArrayList("115200", "1200"));
		
		txtDelay.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txtDelay.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}
		});
		
		txtSleepTime.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txtSleepTime.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}
		});
		
		txtDelayDarkYF.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txtDelayDarkYF.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}
		});
		
		txtConnectPort.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txtConnectPort.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}
		});
		
		txtDelay.setText("");
		txtSleepTime.setText("");
		txtDelayDarkYF.setText("");
	}
	
	

}
