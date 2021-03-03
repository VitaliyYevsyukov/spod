package presenters.programs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import presenters.phase.TVP;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Vitaly on 23.12.2016.
 */
public class ProgramsComboBoxEditingCell extends TableCell<RoadProgram, ProgramMode> {

    //private ResourceBundle bundleGUI, bundleAlert;
    //private Locale localeGUI, localeAlert;
    //String langXML = null;

    private ObservableList<ProgramMode> programModes = FXCollections.observableArrayList(new ProgramMode("Циклическая"),
            new ProgramMode("Циклическая с ТВП - 1"),
            new ProgramMode("Циклическая с ТВП - 2"),
            new ProgramMode("Циклическая с ТВП - 1 и ТВП - 2"),
            new ProgramMode("Пешеходная с ТВП - 1"),
            new ProgramMode("Пешеходная с ТВП - 2"),
            new ProgramMode("Пешеходная с ТВП - 1 и ТВП - 2"),
            new ProgramMode("Замена фаз"),
            new ProgramMode("Желтое мигание"),
            new ProgramMode("Отключение светофора"));

    /*private void itemProgramModeLang(String lang){
        localeGUI = new Locale(lang);
        bundleGUI = ResourceBundle.getBundle("bundle.locale_lang", localeGUI);

        programModes = FXCollections.observableArrayList(new ProgramMode("Циклическая"),
                            new ProgramMode("Циклическая с ТВП - 1"),
                            new ProgramMode("Циклическая с ТВП - 2"),
                            new ProgramMode("Циклическая с ТВП - 1 и ТВП - 2"),
                            new ProgramMode("Пешеходная с ТВП - 1"),
                            new ProgramMode("Пешеходная с ТВП - 2"),
                            new ProgramMode("Пешеходная с ТВП - 1 и ТВП - 2"),
                            new ProgramMode("Желтое мигание"),
                            new ProgramMode("Всем красный"),
                            new ProgramMode("Отключение светофора"));
    }*/

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

    private ComboBox<ProgramMode> comboBox;

    public ProgramsComboBoxEditingCell() {

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
        setText(getTyp().getMode());
        setGraphic(null);
    }

    @Override
    public void updateItem(ProgramMode item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getTyp());
                }
                setText(getTyp().getMode());
                setGraphic(comboBox);
            } else {
                setText(getTyp().getMode());
                setGraphic(null);
            }
        }
    }

    private void createComboBox() {
        //langXML();
        //itemProgramModeLang(langXML);
        comboBox = new ComboBox<>(programModes);
        comboBoxConverter(comboBox);
        comboBox.valueProperty().set(getTyp());
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        comboBox.setOnAction((e) -> {
            System.out.println("Committed: " + comboBox.getSelectionModel().getSelectedItem());
            commitEdit(comboBox.getSelectionModel().getSelectedItem());
        });
//            comboBox.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) {
//                    commitEdit(comboBox.getSelectionModel().getSelectedItem());
//                }
//      });
    }

    private void comboBoxConverter(ComboBox<ProgramMode> comboBox) {
        // Define rendering of the list of values in ComboBox drop down.
        comboBox.setCellFactory((c) -> {
            return new ListCell<ProgramMode>() {
                @Override
                protected void updateItem(ProgramMode item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getMode());
                    }
                }
            };
        });
    }

    private ProgramMode getTyp() {
        return getItem() == null ? new ProgramMode("") : getItem();
    }

}
