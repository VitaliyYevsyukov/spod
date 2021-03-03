package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import main.Main;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import org.dockfx.demo.DockFX;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import presenters.intensity.IntensityPresenter;
import presenters.treeView.TreeViewPresenter;
import roadModel.IRoadModel;
import roadModel.RoadModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Vitaly on 05.12.2016.
 */
public class MainController implements IRoadModel {

    RoadModel roadModel;
    RoadController roadController;
    TreeViewPresenter treeViewPresenter;
    IntensityPresenter intensityPresenter;

    //private static ResourceBundle bundleGUI, bundleAlert;
    //private static Locale localeGUI, localeAlert;
    static String langXML = null;

    boolean treeViewIsOpen = true;

    private void init() {
        this.setModel(new RoadModel());
        this.roadController = new RoadController(this);
    }

    public void setModel(RoadModel model){
        this.roadModel = model;
    }
    public RoadModel getModel(){
        return this.roadModel;
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

    }*/

    public void showTreeView(){
        init();
        try {

            DockPane dockPane = Main.getDockPane();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/TreeView.fxml"));
            Pane object = fxmlLoader.load();
            treeViewPresenter = fxmlLoader.<TreeViewPresenter>getController();
            treeViewPresenter.setRoadController(this.roadController);
            if(treeViewIsOpen) {
                treeViewPresenter.openRoadObject();
                treeViewPresenter.openBottomBar();

                DockNode dockObject = new DockNode(object);
                dockObject.setDockTitleBar(null);
                dockObject.dock(dockPane, DockPos.LEFT);
                treeViewIsOpen = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showIntensity(){
        //langXML();
        //loadLang(langXML);
        try{
            ScrollPane scrollPane = new ScrollPane();

            DockPane dockPane = Main.getDockPane();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Intensity.fxml"));
            Pane intensity = fxmlLoader.load();
            intensityPresenter = fxmlLoader.<IntensityPresenter>getController();
            intensityPresenter.setRoadController(this.roadController);
            intensityPresenter.show(this);
            roadController.showIntensity();

            scrollPane.setContent(intensity);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            Image dockImage = new Image(DockFX.class.getResource("docknode.png").toExternalForm());

            DockNode dockConfigurator = new DockNode(scrollPane, "Интенсивности", new ImageView(dockImage));
            dockConfigurator.setPrefSize(300, 150);
            dockConfigurator.dock(dockPane, DockPos.RIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
