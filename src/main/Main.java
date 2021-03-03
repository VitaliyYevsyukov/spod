package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import controllers.MainController;
import presenters.menuBar.MenuBarController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import org.dockfx.DockPane;


public class Main extends Application {

    //private ResourceBundle bundleGUI, bundleAlert;
    //private Locale localeGUI, localeAlert;
    String langXML = null;
    
    public static File f;
    public static FileChannel channel;
    public static FileLock lock;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////                                LOCALE                   /////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*private void loadLang(String lang){
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

    public static void unlock() {
    	try {
    		
    		if(lock != null) {
    			lock.release();
    			channel.close();
    			f.delete();
    		}
    		
    		
    	}catch(IOException ex){
    		ex.printStackTrace();
    	}
    }
    
    
    private static BorderPane root;
    public static BorderPane getBorderPane() {
        return root;
    }

    private static DockPane dockPane;
    public static DockPane getDockPane() {
        return dockPane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //langXML();
        //loadLang(langXML);   	
    	

        MenuBarController menuBarController;
        MainController mainController = new MainController();
        
        
        primaryStage.setTitle("КомКон-СПОД");

        root = new BorderPane();
        dockPane = new DockPane();
        dockPane.setStyle("-fx-background-color: #1d3356;");

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setMinHeight(768);
        primaryStage.setMinWidth(1024);
        primaryStage.getIcons().add(new Image("image/other/komkon_logo_title.png"));

        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/componentTreeView/Settings.fxml"));
        //AnchorPane anchorPane = fxmlLoader.load();
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MenuBar1.fxml"));
        MenuBar menuBar = fxmlLoader.load();
        
        menuBarController = fxmlLoader.<MenuBarController>getController();
        menuBarController.setMainController(mainController);
        
        
        
        /*menuBarController.getMenuObject().setText(bundleGUI.getString("menuObject"));
        //menuBarController.getMenuDataPreparation().setText(bundleGUI.getString("menuDataPreparation"));
        menuBarController.getMenuTools().setText(bundleGUI.getString("menuTools"));
        menuBarController.getMenuService().setText(bundleGUI.getString("menuService"));
        menuBarController.getMenuCOMCON_ACADEM().setText(bundleGUI.getString("menuCOMCON_ACADEM"));
        menuBarController.getMenuAWP_technologist().setText(bundleGUI.getString("menuAWP_technologist"));
        menuBarController.getMenuDirectories().setText(bundleGUI.getString("menuDirectories"));
        menuBarController.getMenuReference().setText(bundleGUI.getString("menuReference"));
        menuBarController.getMenuItemObjects().setText(bundleGUI.getString("menuItemObjects"));
        menuBarController.getMenuItemRefresh1().setText(bundleGUI.getString("menuItemRefresh"));
        menuBarController.getMenuItemExportObject().setText(bundleGUI.getString("menuItemExportObject"));
        menuBarController.getMenuItemImportObject().setText(bundleGUI.getString("menuItemImportObject"));
        menuBarController.getMenuItemServiseDB().setText(bundleGUI.getString("menuItemServiseDB"));
        menuBarController.getMenuItemExit().setText(bundleGUI.getString("menuItemExit"));
        menuBarController.getMenuItemObject().setText(bundleGUI.getString("labelObject"));
        menuBarController.getMenuItemDirections().setText(bundleGUI.getString("labelDirection"));
        menuBarController.getMenuItemPhase().setText(bundleGUI.getString("labelPhase"));
        menuBarController.getMenuItemProgram().setText(bundleGUI.getString("labelProgramm"));
        menuBarController.getMenuItemPromtact().setText(bundleGUI.getString("labelPromtactu"));
        menuBarController.getMenuItemConflict().setText(bundleGUI.getString("labelConflicts"));
        menuBarController.getMenuItemScheme().setText(bundleGUI.getString("labelScheme"));
        menuBarController.getMenuItemBasicPromtacts().setText(bundleGUI.getString("menuItemBasicPromtacts"));
        menuBarController.getMenuItemGroupControl().setText(bundleGUI.getString("menuItemGroupControl"));
        menuBarController.getMenuItemConflictTable().setText(bundleGUI.getString("menuItemConflictTable"));
        menuBarController.getMenuItemTableOfBasicIndustrialItems().setText(bundleGUI.getString("menuItemTableOfBasicIndustrialItems"));
        menuBarController.getMenuItemChannelParameters().setText(bundleGUI.getString("menuItemChannelParameters"));
        menuBarController.getMenuItemChannelParametersTable().setText(bundleGUI.getString("menuItemChannelParametersTable"));
        menuBarController.getMenuItemIntensity().setText(bundleGUI.getString("menuItemIntensity"));
        menuBarController.getMenuItemSettings().setText(bundleGUI.getString("menuItemSettings"));
        menuBarController.getMenuItemRefresh2().setText(bundleGUI.getString("menuItemRefresh"));
        menuBarController.getMenuItemDiagram().setText(bundleGUI.getString("menuItemDiagram"));
        menuBarController.getMenuItemCheck().setText(bundleGUI.getString("menuItemCheck"));
        menuBarController.getMenuItemEntry().setText(bundleGUI.getString("menuItemEntry"));
        menuBarController.getMenuItemReport().setText(bundleGUI.getString("menuItemReport"));
        menuBarController.getMenuItemWorkSimulator().setText(bundleGUI.getString("menuItemWorkSimulator"));
        menuBarController.getMenuItemProtectionWhenDownloadingTheProgram().setText(bundleGUI.getString("menuItemProtectionWhenDownloadingTheProgram"));
        menuBarController.getMenuItemDefaultSettings().setText(bundleGUI.getString("menuItemDefaultSettings"));
        menuBarController.getMenuItemExportingDataToACSDD().setText(bundleGUI.getString("menuItemExportingDataToACSDD"));
        menuBarController.getMenuItemActivateWorkWithACSDD().setText(bundleGUI.getString("menuItemActivateWorkWithACSDD"));
        menuBarController.getMenuItemImportingDataFromTheWorkstation().setText(bundleGUI.getString("menuItemImportingDataFromTheWorkstation"));
        menuBarController.getMenuItemDesignMode().setText(bundleGUI.getString("menuItemDesignMode"));
        menuBarController.getMenuItemReferenceBookOfControllers().setText(bundleGUI.getString("menuItemReferenceBookOfControllers"));
        menuBarController.getMenuItemDirectionOfReferrals().setText(bundleGUI.getString("menuItemDirectionOfReferrals"));
        menuBarController.getMenuItemHelp().setText(bundleGUI.getString("menuItemHelp"));
        menuBarController.getMenuItemAbout().setText(bundleGUI.getString("menuItemAbout"));*/

        VBox topBox = new VBox(menuBar);

        root.setTop(topBox);
        root.setCenter(dockPane);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        //DockPane.initializeDefaultUserAgentStylesheet();

        primaryStage.show();
    }

    public static void main(String[] args) {
        
    	try {
    		
    		f = new File("process");
    		
    		if(f.exists()) {
    			f.delete();
    		}
    		
    		channel = new RandomAccessFile(f, "rw").getChannel();
    		lock = channel.tryLock();
    		
    		if(lock == null) {
    			channel.close();
    			throw new RuntimeException("Only 1 instance of this program can be run at a time");
    			
    			
    		}
    		
    		Thread shutdown = new Thread(new Runnable() {
				
				@Override
				public void run() {
					unlock();					
				}
			});
    		
    		Runtime.getRuntime().addShutdownHook(shutdown);
    		
    		launch(args);
    		
    		
    	}catch (IOException e) {
			throw new RuntimeException("Could not start process...", e);
		}    	
    	
    }
}