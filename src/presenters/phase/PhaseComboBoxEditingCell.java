package presenters.phase;

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
 * Created by Vitaly on 18.12.2016.
 */
public class PhaseComboBoxEditingCell extends TableCell<RoadPhase, TVP> {

    //private ResourceBundle bundleGUI, bundleAlert;
    //private Locale localeGUI, localeAlert;
    //String langXML = null;

    private ObservableList<TVP> tvpPhaseData = FXCollections.observableArrayList(new TVP("Отсутствует"),
            new TVP("ТВП - 1"),
            new TVP("ТВП - 2"),
            new TVP("ТВП - 1 и ТВП - 2"),
            new TVP("ТВП - 1 или ТВП - 2"));

    public ObservableList<TVP> getTvpPhaseData() {
		return tvpPhaseData;
	}

	public void setTvpPhaseData(ObservableList<TVP> tvpPhaseData) {
		this.tvpPhaseData = tvpPhaseData;
	}

	private void itemTVPLang(String lang){
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

    private ComboBox<TVP> comboBox;

    public PhaseComboBoxEditingCell() {
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

        setText(getTyp().getTvp());
        setGraphic(null);
    }

    @Override
    public void updateItem(TVP item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getTyp());
                }
                setText(getTyp().getTvp());
                setGraphic(comboBox);
            } else {
                setText(getTyp().getTvp());
                setGraphic(null);
            }
        }
    }

    private void createComboBox() {
        //langXML();
        //itemTVPLang(langXML);
        comboBox = new ComboBox<>(tvpPhaseData);
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

    private void comboBoxConverter(ComboBox<TVP> comboBox) {
        // Define rendering of the list of values in ComboBox drop down.
        comboBox.setCellFactory((c) -> {
            return new ListCell<TVP>() {
                @Override
                protected void updateItem(TVP item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getTvp());
                    }
                }
            };
        });
    }

    private TVP getTyp() {
        return getItem() == null ? new TVP("") : getItem();
    }
}
