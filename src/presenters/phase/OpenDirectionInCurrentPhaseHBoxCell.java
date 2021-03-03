package presenters.phase;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Vitaly on 05.09.2017.
 */
public class OpenDirectionInCurrentPhaseHBoxCell extends HBox {

    Label labelOpenDirection = new Label();
    ComboBox<String> comboBox = new ComboBox<>();
    CheckBox checkBox = new CheckBox();
    

	//Label blinkingTOSN = new Label();
    ObservableList<String> observableListComboBox;

    //private static ResourceBundle bundleGUI;
    //private static Locale localeGUI;
    //static String langXML = null;

    /*private void langXML(){
        String filepath = System.getProperty("user.dir") +"\\" + "configuration.xml";
        File xmlFile = new File(filepath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;


        try{
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("lang");
            Node node = nodeList.item(0);
            langXML = node.getTextContent();

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }*/

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////                                LOCALE                   /////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*private void loadLang(String lang){
        localeGUI = new Locale(lang);
        bundleGUI = ResourceBundle.getBundle("bundle.locale_lang", localeGUI);
    }*/

    public OpenDirectionInCurrentPhaseHBoxCell(){
        super();

        //langXML();
        //loadLang(langXML);

        labelOpenDirection.setText("Открыто направление");
        HBox.setMargin(labelOpenDirection, new Insets(0, 25, 0, 0));
        //labelOpenDirection.setMaxWidth(Double.MAX_VALUE);
        //HBox.setHgrow(labelOpenDirection, Priority.ALWAYS);

        comboBox.setPrefWidth(USE_COMPUTED_SIZE);
        comboBox.setStyle("-fx-background-radius: 10; -fx-font-weight: bold");
        HBox.setMargin(comboBox, new Insets(0,25,0,0));

        checkBox.setText("мигание Тосн");
        HBox.setMargin(checkBox, new Insets(2,0,0,0));

        this.getChildren().addAll(labelOpenDirection, comboBox, checkBox);
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    public void setComboBox(ComboBox<String> comboBox) {
        this.comboBox = comboBox;
    }

    public ObservableList<String> getObservableListComboBox() {
        return observableListComboBox;
    }

    public void setObservableListComboBox(ObservableList<String> observableListComboBox) {
        this.observableListComboBox = observableListComboBox;
        comboBox.getItems().addAll(observableListComboBox);
    }
    
    public CheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}

}
