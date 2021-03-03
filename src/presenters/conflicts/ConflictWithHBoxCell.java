package presenters.conflicts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Vitaly on 06.07.2017.
 */
public class ConflictWithHBoxCell extends HBox {

    private Label label = new Label();
    private ComboBox<String> comboBox = new ComboBox<>();
    private ObservableList<String> observableListComboBox;

    //private static ResourceBundle bundleGUI;
    //private static Locale localeGUI;
    //static String langXML = null;

    /////////////////////////////////////////////////////////////////////
    ////////// See which language is installed in the file //////////////
    /////////////////////////////////////////////////////////////////////
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

    ConflictWithHBoxCell(){
        super();
        //langXML();
        //loadLang(langXML);
        

        label.setText("Конфликт с направлением");
        label.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);

        comboBox.setPrefWidth(USE_COMPUTED_SIZE);
        comboBox.setStyle("-fx-background-radius: 10; -fx-font-weight: bold");

        this.getChildren().addAll(label, comboBox);
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;

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

}
