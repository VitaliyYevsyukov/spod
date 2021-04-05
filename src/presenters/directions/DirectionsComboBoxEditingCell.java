package presenters.directions;

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
 * Created by Vitaly on 14.12.2016.
 */
class DirectionsComboBoxEditingCell extends TableCell<RoadDirection, TypDirection> {

    private ResourceBundle bundleGUI, bundleAlert;
    private Locale localeGUI, localeAlert;
    String langXML = null;

    private ObservableList<TypDirection> typDirectionData = FXCollections.observableArrayList(
    		new TypDirection("Транспортное направление"),
            new TypDirection("Пешеходное"),
            new TypDirection("Поворотная стрелка"),
            new TypDirection("Транспортное с одним красным"),
            new TypDirection("Транспортное красное и желтое"),
            new TypDirection("Транспортное два красных"),
            new TypDirection("Трамвайное прямо"),
            new TypDirection("Трамвайное налево"),
            new TypDirection("Трамвайное направо"));

    /*private void itemTypeDirectionLang(String lang){
        localeGUI = new Locale(lang);
        bundleGUI = ResourceBundle.getBundle("bundle.locale_lang", localeGUI);

        
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

    public ObservableList<TypDirection> getTypDirectionData() {
        return typDirectionData;
    }

    private ComboBox<TypDirection> comboBox;

    public DirectionsComboBoxEditingCell() {
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

        setText(getTyp().getTypDirection());
        setGraphic(null);
    }

    @Override
    public void updateItem(TypDirection item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getTyp());
                }
                setText(getTyp().getTypDirection());
                setGraphic(comboBox);
            } else {
                setText(getTyp().getTypDirection());
                setGraphic(null);
            }
        }
    }

    private void createComboBox() {
        //langXML();
        //itemTypeDirectionLang(langXML);
        comboBox = new ComboBox<>(typDirectionData);
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
//            });
    }

    private void comboBoxConverter(ComboBox<TypDirection> comboBox) {
        // Define rendering of the list of values in ComboBox drop down.
        comboBox.setCellFactory((c) -> {
            return new ListCell<TypDirection>() {
                @Override
                protected void updateItem(TypDirection item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getTypDirection());
                    }
                }
            };
        });
    }

    private TypDirection getTyp() {
        return getItem() == null ? new TypDirection("") : getItem();
    }
}
