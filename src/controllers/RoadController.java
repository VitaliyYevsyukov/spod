package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import presenters.bottomBar.BottomBarPresenter;
import presenters.intensity.IntensityPresenter;
import roadModel.IRoadModel;
import presenters.conflicts.RoadConflictsPresenter;
import presenters.detector.RoadDetectorPresenter;
import presenters.directions.RoadDirectionsPresenter;
import presenters.object.RoadObjectPresenter;
import presenters.phase.RoadPhasePresenter;
import presenters.programs.RoadProgramsPresenter;
import presenters.promtactu.RoadPromtactuPresenter;
import presenters.scheme.RoadSchemePresenter;

import java.io.IOException;

/**
 * Created by Vitaly on 06.12.2016.
 */
public class RoadController {

    private IRoadModel iRoadModel;

    public IRoadModel getIRoadModel() {
    	return iRoadModel;
    }
    
    public RoadController(IRoadModel iRoadModel){
        this.iRoadModel = iRoadModel;
    }

    private RoadObjectPresenter roadObjectPresenter;
    private RoadDirectionsPresenter roadDirectionsPresenter;
    private RoadPhasePresenter roadPhasePresenter;
    private RoadDetectorPresenter roadDetectorPresenter;
    private RoadProgramsPresenter roadProgramsPresenter;
    private RoadPromtactuPresenter roadPromtactuPresenter;
    private RoadConflictsPresenter roadConflictsPresenter;
    private RoadSchemePresenter roadSchemePresenter;
    private BottomBarPresenter bottomBarPresenter;
    private IntensityPresenter intensityPresenter;

    private String currentModel = "";
    private void saveCurrentModel() {
        if (currentModel.equals("object")){
            roadObjectPresenter.save(iRoadModel);
        }
        if (currentModel.equals("directions")){

        }
        if (currentModel.equals("phase")) {
        	roadPhasePresenter.save(iRoadModel);
        }
        if (currentModel.equals("programs")) {
            roadProgramsPresenter.save(iRoadModel);
        }
        if (currentModel.equals("promtactu")){
            roadPromtactuPresenter.save(iRoadModel);
        }
        if (currentModel.equals("conflicts")) {

        }
        if(currentModel.equals("detectors")){
        	roadDetectorPresenter.save(iRoadModel);
        }
        if (currentModel.equals("scheme")) {
            roadSchemePresenter.save(iRoadModel);
        }
    }


    public void showRoadObject(BorderPane borderPane_CHILDE){
        try {
            ScrollPane scrollPane = new ScrollPane();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Road_object.fxml"));
            Pane object = fxmlLoader.load();

            roadObjectPresenter = fxmlLoader.<RoadObjectPresenter>getController();
            saveCurrentModel();
            currentModel = "object";
            roadObjectPresenter.show(this.iRoadModel);

            scrollPane.setContent(object);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            borderPane_CHILDE.setCenter(scrollPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRoadDirections(BorderPane borderPane_CHILDE){
        try {
            ScrollPane scrollPane = new ScrollPane();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Road_directions.fxml"));
            Pane direction = fxmlLoader.load();

            roadDirectionsPresenter = fxmlLoader.<RoadDirectionsPresenter>getController();
            
            saveCurrentModel();
            currentModel = "directions";
            roadDirectionsPresenter.show(iRoadModel);

            scrollPane.setContent(direction);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            borderPane_CHILDE.setCenter(scrollPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRoadPhase(BorderPane borderPane_CHILDE){
        try {
            ScrollPane scrollPane = new ScrollPane();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Road_phase.fxml"));
            Pane phase = fxmlLoader.load();

            roadPhasePresenter = fxmlLoader.<RoadPhasePresenter>getController();
            saveCurrentModel();
            currentModel = "phase";
            roadPhasePresenter.show(iRoadModel);

            scrollPane.setContent(phase);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            borderPane_CHILDE.setCenter(scrollPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRoadDetector(BorderPane borderPane_CHILDE) {
    	try {
    		ScrollPane scrollPane = new ScrollPane();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Road_detectors.fxml"));
            Pane detector = fxmlLoader.load();
            
            roadDetectorPresenter = fxmlLoader.<RoadDetectorPresenter>getController();
            saveCurrentModel();
            currentModel = "detectors";
            roadDetectorPresenter.show(iRoadModel);

            scrollPane.setContent(detector);
            
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            
            borderPane_CHILDE.setCenter(scrollPane);
    	}catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showRoadPrograms(BorderPane borderPane_CHILDE){
        try {
            ScrollPane scrollPane = new ScrollPane();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Road_programs.fxml"));
            Pane programs = fxmlLoader.load();
            roadProgramsPresenter = fxmlLoader.<RoadProgramsPresenter>getController();
            
            saveCurrentModel();
            currentModel = "programs";
            roadProgramsPresenter.show(iRoadModel);

            scrollPane.setContent(programs);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            borderPane_CHILDE.setCenter(scrollPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRoadConflicts(BorderPane borderPane_CHILDE){
        try {
            ScrollPane scrollPane = new ScrollPane();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Road_conflicts.fxml"));
            Pane conflicts = fxmlLoader.load();
            roadConflictsPresenter = fxmlLoader.<RoadConflictsPresenter>getController();
            saveCurrentModel();
            currentModel = "conflicts";
            roadConflictsPresenter.show(iRoadModel);

            scrollPane.setContent(conflicts);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            borderPane_CHILDE.setCenter(scrollPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRoadPromtactu(BorderPane borderPane_CHILDE){
        try {
            ScrollPane scrollPane = new ScrollPane();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Road_promtactu.fxml"));
            Pane promtakty = fxmlLoader.load();
            roadPromtactuPresenter = fxmlLoader.<RoadPromtactuPresenter>getController();
            saveCurrentModel();
            currentModel = "promtactu";
            roadPromtactuPresenter.show(iRoadModel);

            scrollPane.setContent(promtakty);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            borderPane_CHILDE.setCenter(scrollPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRoadScheme(BorderPane borderPane_CHILDE){
        try {
            ScrollPane scrollPane = new ScrollPane();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Road_scheme.fxml"));
            Pane scheme = fxmlLoader.load();
            roadSchemePresenter = fxmlLoader.<RoadSchemePresenter>getController();
            saveCurrentModel();
            currentModel = "scheme";
            roadSchemePresenter.show(iRoadModel);

            scrollPane.setContent(scheme);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            borderPane_CHILDE.setCenter(scrollPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBottomBar(BorderPane borderPane_CHILDE){
        try {
            ScrollPane scrollPane = new ScrollPane();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BottomBar.fxml"));
            Pane bottomBar = fxmlLoader.load();

            bottomBarPresenter = fxmlLoader.<BottomBarPresenter>getController();
            bottomBarPresenter.showBottomBar(iRoadModel);

            scrollPane.setContent(bottomBar);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            borderPane_CHILDE.setBottom(scrollPane);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showIntensity(){
       /* try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Intensity.fxml"));
            Pane object = fxmlLoader.load();

            intensityPresenter = fxmlLoader.<IntensityPresenter>getController();
            intensityPresenter.show(iRoadModel);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void showConfigurator(BorderPane borderPane_CHILDE){
        try {

            ScrollPane scrollPane = new ScrollPane();

            Pane configurator = FXMLLoader.load(getClass().getResource("/fxml/Configurator.fxml"));

            scrollPane.setContent(configurator);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            borderPane_CHILDE.setCenter(scrollPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}