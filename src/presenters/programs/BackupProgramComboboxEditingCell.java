package presenters.programs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Vitaly on 10.01.2017.
 */
public class BackupProgramComboboxEditingCell extends TableCell<RoadProgram, BackupProgram> {

    //private ResourceBundle bundleGUI, bundleAlert;
    //private Locale localeGUI, localeAlert;
   //String langXML = null;

    private ObservableList<BackupProgram> programModes = FXCollections.observableArrayList(new BackupProgram("Отсутствует"),
            new BackupProgram("Неисправность таймера"),
            new BackupProgram("Неисправность ТВП - 1"),
            new BackupProgram("Неисправность ТВП - 2"),
            new BackupProgram("Неисправность ТВП - 1 и ТВП - 2"),
            new BackupProgram("Неисправность детектора"));

    private void itemBackupProgramLang(String lang){
        //localeGUI = new Locale(lang);
        //bundleGUI = ResourceBundle.getBundle("bundle.locale_lang", localeGUI);

        
    }

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

    private ComboBox<BackupProgram> comboBox;

    public BackupProgramComboboxEditingCell() {

    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createComboBox();
            setText(null);
            setGraphic(comboBox);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getTyp().getBackupProgram());
        setGraphic(null);
    }

    @Override
    public void updateItem(BackupProgram item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getTyp());
                }
                setText(getTyp().getBackupProgram());
                setGraphic(comboBox);
            } else {
                setText(getTyp().getBackupProgram());
                setGraphic(null);
            }
        }
    }

    private void createComboBox() {
        //langXML();
        //itemBackupProgramLang(langXML);
        comboBox = new ComboBox<>(programModes);
        comboBoxConverter(comboBox);
        comboBox.valueProperty().set(getTyp());
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        comboBox.setOnAction((e) -> {
            System.out.println("Резервная программа: " + comboBox.getSelectionModel().getSelectedItem());
            commitEdit(comboBox.getSelectionModel().getSelectedItem());
        });
//            comboBox.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) {
//                    commitEdit(comboBox.getSelectionModel().getSelectedItem());
//                }
//      });
    }

    private void comboBoxConverter(ComboBox<BackupProgram> comboBox) {
        // Define rendering of the list of values in ComboBox drop down.
        comboBox.setCellFactory((c) -> {
            return new ListCell<BackupProgram>() {
                @Override
                protected void updateItem(BackupProgram item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getBackupProgram());
                    }
                }
            };
        });
    }

    private BackupProgram getTyp() {
        return getItem() == null ? new BackupProgram("") : getItem();
    }

}
