package presenters.scheme;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import roadModel.IRoadModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Vitaly on 06.12.2016.
 */
public class RoadSchemePresenter {

    @FXML
    private ChoiceBox<String> chBoxTypeJunction, chBoxArrowType;
    @FXML
    private Label labelScheme, labelTypeJunction, labelAnotherTypeJunction, labelDirectionOfMovement, labelTypeArrow, labelTransformation, labelSchemeObjectPhases;
    @FXML
    private Button buttonAddAllDirections, buttonLoadFromFile, buttonForm, btnNewLine, btnRotateRight, btnNewText, btnRotateLeft, btnFlipVertically, btnChangeText,
                    btnFlipHorizontally, btnDelete;

    @FXML
   // private ResourceBundle bundleGUI, bundleAlert;
    //private Locale localeGUI, localeAlert;
    //String langXML = null;

    ObservableList<String> observableListTypeJunction = FXCollections.observableArrayList();
    ObservableList<String> observableListArrowType = FXCollections.observableArrayList();

    private IRoadModel iRoadModel;

    public void show(IRoadModel iRoadModel){
        this.iRoadModel = iRoadModel;
        chBoxTypeJunction.setValue(iRoadModel.getModel().getRoadSchemeModel().getRoadSchemeTypeJunction());
        
        observableListTypeJunction.addAll("Неизвестный", "Х - образный", "Т - образный",
                "Четыре островка", "Два островка", "Главная дорога",
                "Въезд справа", "Въезд слева", "Другой");
        
        observableListArrowType.addAll("Прямая", "Поворотная", "Диагональная",
                "Прямая + поворотная", "Пешеходная", "Разворотная",
                "Три направления", "Два направления");
    }
    public void save(IRoadModel iRoadModel){
        this.iRoadModel = iRoadModel;
        //iRoadModel.getModel().getRoadSchemeModel().setRoadSchemeTypeJunction(chBoxTypeJunction.getValue().toString());
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////                                LOCALE                   /////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*private void loadLang(String lang){
        localeGUI = new Locale(lang);
        bundleGUI = ResourceBundle.getBundle("bundle.locale_lang", localeGUI);

        labelScheme.setText(bundleGUI.getString("labelScheme"));
        labelTypeJunction.setText(bundleGUI.getString("labelTypeJunction"));
        labelAnotherTypeJunction.setText(bundleGUI.getString("labelAnotherTypeJunction"));
        labelDirectionOfMovement.setText(bundleGUI.getString("labelDirectionOfMovement"));
        labelTypeArrow.setText(bundleGUI.getString("labelTypeArrow"));
        labelTransformation.setText(bundleGUI.getString("labelTransformation"));
        labelSchemeObjectPhases.setText(bundleGUI.getString("labelSchemeObjectPhases"));
        buttonAddAllDirections.setText(bundleGUI.getString("buttonAddAllDirections"));
        buttonLoadFromFile.setText(bundleGUI.getString("buttonLoadFromFile"));
        buttonForm.setText(bundleGUI.getString("buttonForm"));
        btnNewLine.setTooltip(new Tooltip(bundleGUI.getString("toolTipNewLine")));
        btnRotateRight.setTooltip(new Tooltip(bundleGUI.getString("toolTipRotateRight")));
        btnNewText.setTooltip(new Tooltip(bundleGUI.getString("toolTipNewText")));
        btnRotateLeft.setTooltip(new Tooltip(bundleGUI.getString("toolTipRotateLeft")));
        btnFlipVertically.setTooltip(new Tooltip(bundleGUI.getString("toolTipFlipVertically")));
        btnChangeText.setTooltip(new Tooltip(bundleGUI.getString("toolTipChangeText")));
        btnFlipHorizontally.setTooltip(new Tooltip(bundleGUI.getString("toolTipFlipHorizontally")));
        btnDelete.setTooltip(new Tooltip(bundleGUI.getString("buttonDelete")));
    }*/
    /*private void itemLang(String lang){
        localeGUI = new Locale(lang);
        bundleGUI = ResourceBundle.getBundle("locale_lang", localeGUI);

        observableListTypeJunction.addAll(bundleGUI.getString("stringUnknown"), bundleGUI.getString("stringX-shaped"), bundleGUI.getString("stringT-shaped"),
                bundleGUI.getString("stringFourIslets"), bundleGUI.getString("stringTwoIslets"), bundleGUI.getString("stringMainRoad"),
                bundleGUI.getString("stringEntranceRight"), bundleGUI.getString("stringEntranceLeft"), bundleGUI.getString("stringOther"));

        observableListArrowType.addAll(bundleGUI.getString("stringStraight"), bundleGUI.getString("stringRotary"), bundleGUI.getString("stringDiagonal"),
                bundleGUI.getString("stringDirect+Rotary"), bundleGUI.getString("stringPedestrian"), bundleGUI.getString("stringReversal"),
                bundleGUI.getString("stringThreeDirections"), bundleGUI.getString("stringTwoDirections"));
    }*/

    private void createChoiceBoxTypeJunction(){
        chBoxTypeJunction.setItems(observableListTypeJunction);
    }
    private void createChoiceBoxArrowType(){
        chBoxArrowType.setItems(observableListArrowType);
    }

    @FXML
    public void initialize() {

        //langXML();
        //loadLang(langXML);
        //itemLang(langXML);

        createChoiceBoxTypeJunction();
        createChoiceBoxArrowType();


    }

}
