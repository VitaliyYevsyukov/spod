package presenters.intensity;

import controllers.RoadController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import roadModel.IRoadModel;
import roadModel.RoadModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Vitaly on 14.03.2017.
 */
public class IntensityPresenter {

    @FXML
    private TextField textFieldPathToDB;
    @FXML
    private Button buttonDownload, buttonOpen;
    @FXML
    private ChoiceBox<String> choiceBoxGroupBy, choiceBoxYear, choiceBoxMounth;
    @FXML
    private Label labelPathToDB, labelYear, labelMounth, lableGroupBy;
    @FXML
    private TableView<ObservableList<String>> tableView;

    @FXML
    //private static ResourceBundle bundleGUI, bundleAlert;
    //private static Locale localeGUI, localeAlert;
    //static String langXML = null;

    private ObservableList<ObservableList<String>> data;

    IntensityFile intensFile;
    IRoadModel roadModel;

    RoadController roadController;
    public void setRoadController(RoadController rController){
        this.roadController = rController;
    }

    Map<String, IntensityFile> mapMounthesInYear = new HashMap<String, IntensityFile>();
    Map<String, Map<String, IntensityFile>> mapCalendar = new HashMap<String, Map<String, IntensityFile>>();

    String path;
    String linkHref;
    String linkText;
    String linkDownloadFile;
    String fileNameForDisk;
    String fileYearFromWeb;
    String fileMonth;
    String fileDate;
    String fileYearFromDisk;
    String ipIntens;

    Thread thread;

    public void show(IRoadModel roadModel){
        this.roadModel = roadModel;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////                                LOCALE                   /////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*private void loadLang(String lang){
        localeGUI = new Locale(lang);
        bundleGUI = ResourceBundle.getBundle("bundle.locale_lang", localeGUI);

        buttonDownload.setText(bundleGUI.getString("buttonDownload"));
        buttonOpen.setText(bundleGUI.getString("buttonOpen"));
        labelPathToDB.setText(bundleGUI.getString("labelPathToDB"));
        labelYear.setText(bundleGUI.getString("labelYear"));
        labelMounth.setText(bundleGUI.getString("labelMounth"));
        lableGroupBy.setText(bundleGUI.getString("lableGroupBy"));
    }

    private static void alertLang(String lang){
        localeAlert = new Locale(lang);
        bundleAlert = ResourceBundle.getBundle("localeAlert_lang", localeAlert);
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


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////                                  МЕТОД                                              ///////////////
    ////////////// Подключение к базе данных. Получение всех имеющихся колонок в файле и их параметров ///////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings({"unchecked"})
    public void buildData(){

        tableView.getColumns().clear();
        data = FXCollections.observableArrayList();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{

            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + path;
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            String selectID = "SELECT * FROM results";

            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectID);
            for(int i = 0; i < resultSet.getMetaData().getColumnCount(); i++){
                final int j = i;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(resultSet.getMetaData().getColumnName(i+1));

                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);			// Добавляем в ТableView все имеющиеся колонки
            }

            while(resultSet.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=resultSet.getMetaData().getColumnCount(); i++){
                    row.add(resultSet.getString(i));
                }
                data.add(row);
            }

            tableView.setItems(data);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////                                  МЕТОД                                              ///////////////
    //////////////    При отсудствии подключения к сети интернет, смотрим какие файлы есть на диске    ///////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void noInternetConnection(){

        //alertLang(langXML);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Внимание");
        alert.setHeaderText("Не удалось подключиться к КДК по адресу" + " " + ipIntens);
        alert.showAndWait();

        File file = new File(textFieldPathToDB.getText() + "/DB/");
        File[] allDiskFiles = file.listFiles();
        for(File filesDisk : allDiskFiles){
            System.out.println();

            intensFile = new IntensityFile();

            String fileOnDisk = filesDisk.getName();
            System.out.println(fileOnDisk);

            String fileLocalPath = filesDisk.getAbsolutePath();
            System.out.println(fileLocalPath);

            String fileOnDiskYear = fileOnDisk.substring(0,4);
            String fileOnDiskMonth = fileOnDisk.substring(5,7);
            System.out.println("Year: " + fileOnDiskYear + "\nMonth: " + fileOnDiskMonth);

            intensFile.setLocalPath(fileLocalPath);

            mapMounthesInYear.put(fileOnDiskMonth, intensFile);
            choiceBoxMounth.getItems().add(fileOnDiskMonth);
            choiceBoxMounth.setValue(fileOnDiskMonth);
            System.out.println("Map monthes in year: " + mapMounthesInYear);
            if(mapCalendar.containsKey(fileOnDiskYear)){
                mapCalendar.get(fileOnDiskYear).put(fileOnDiskMonth, intensFile);
            }else{
                mapCalendar.put(fileOnDiskYear, mapMounthesInYear);
                choiceBoxYear.getItems().add(fileOnDiskYear);
                choiceBoxYear.setValue(fileOnDiskYear);
            }
            System.out.println("Map mapCalendar: " + mapCalendar);
        }

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////										МЕТОД                                             ///////////////
    //////////////						 Добавить в ChoiceBox доступные для скачивания файлы 			  ///////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void choiceBoxItems(){

        try{
            choiceBoxMounth.getItems().clear();

            String httpPort;
            ipIntens = roadModel.getModel().getRoadModelSettings().getModelIP();
            //String ipIntens = "192.168.188.101";
            System.out.println(ipIntens);

            httpPort = "80";

            org.jsoup.nodes.Document doc = Jsoup.connect("http://" + ipIntens + ":" + httpPort + "/cgi-bin/downloadDb.sh").get();		// Соедениться с адрессом
            Elements links = doc.select("table tr a");			// Отобрать теги с элементом 'a'

            System.out.println();
            System.out.println("*** HTTP ***");

            for(Element link : links){
                intensFile = new IntensityFile();
                System.out.println();

                linkHref = link.attr("href");
                linkText = link.text();
                fileDate = link.attr("file-date");							// Атрибут file-date
                linkDownloadFile = linkHref.replace("." + ".", "" + "");	// Линк для скачки нужного файла
                fileNameForDisk = link.attr("file-date");
                System.out.println("File name for disk: " + fileNameForDisk);

                fileYearFromWeb = fileNameForDisk.substring(0,4);
                fileMonth = fileNameForDisk.substring(5,7);

                intensFile.setWebUrl("http://" + ipIntens + ":" + httpPort + linkDownloadFile);
                intensFile.setHTTPPort(httpPort);
                System.out.println("IntensityFile --- " + intensFile);
                System.out.println("Link for downloading the file: " + intensFile.getWebUrl());

                File file = new File(textFieldPathToDB.getText() + "/DB/");
                file.mkdirs();
                File[] allDiskFiles = file.listFiles();					// List всех имеющихся файлов в дирекрории
                for(File filesDisk : allDiskFiles){
                    String name = filesDisk.getName();
                    String fileName = name.substring(0,7);
                    System.out.println(fileName);
                    if(fileDate.equals(fileName)){
                        String localPath = filesDisk.getAbsolutePath();
                        intensFile.setLocalPath(localPath);
                    }
                }
                System.out.println("after disk " + intensFile);

                mapMounthesInYear.put(fileMonth, intensFile);
                choiceBoxMounth.getItems().add(fileMonth);						// Добавить в choicebox значения fileMonth
                System.out.println("Map monthesInYear: " + mapMounthesInYear);

                if(mapCalendar.containsKey(fileYearFromWeb)){
                    mapCalendar.get(fileYearFromWeb).put(fileMonth, intensFile);
                }else{
                    mapCalendar.put(fileYearFromWeb, mapMounthesInYear);
                    choiceBoxYear.getItems().add(fileYearFromWeb);				// Добавить в choicebox значения fileYear
                }
                System.out.println("Map mapCalendar: " + mapCalendar);
            }
        } catch (IOException e) {
            e.printStackTrace();
            noInternetConnection();
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////										МЕТОД                                             ///////////////
    //////////////								 Группировка по суткам 			                          ///////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
    public void groupingByDay(){
        tableView.getColumns().clear();
        data = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultset = null;

        try{

            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + path;

            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");

            String selectID = "SELECT substr(timeVal,1,11), zoneId, sum(ZoneValue) FROM results GROUP BY substr(timeVal,1,11), zoneId";

            statement = conn.createStatement();
            resultset = statement.executeQuery(selectID);

            for(int i = 0; i < resultset.getMetaData().getColumnCount(); i++){
                final int j = i;

                TableColumn<ObservableList<String>, String> col = new TableColumn<>(resultset.getMetaData().getColumnName(i+1));

                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);			// Добавляем в ТableView все имеющиеся колонки
                System.out.println("Column ["+i+"] ");

                System.out.println("Text " + col.getText().toString());

                if(col.getText().toString().equals("substr(timeVal,1,11)")){
                    col.setText("По дням");
                }
                if(col.getText().toString().equals("sum(ZoneValue)")){
                    col.setText("Сумма");
                }
            }

            while(resultset.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=resultset.getMetaData().getColumnCount(); i++){
                    row.add(resultset.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                data.add(row);
            }
            tableView.setItems(data);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try{
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////										МЕТОД                                             ///////////////
    //////////////								 Группировка по часам 			                          ///////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
    public void groupingByHour(){
        tableView.getColumns().clear();
        data = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultset = null;

        try{

            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + path;

            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");

            String selectID = "SELECT substr(timeVal,1,11), zoneId, sum(ZoneValue) FROM results GROUP BY substr(timeVal,1,11), zoneId";

            statement = conn.createStatement();
            resultset = statement.executeQuery(selectID);

            for(int i = 0; i < resultset.getMetaData().getColumnCount(); i++){
                final int j = i;

                TableColumn<ObservableList<String>, String> col = new TableColumn<>(resultset.getMetaData().getColumnName(i+1));

                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);			// Добавляем в ТableView все имеющиеся колонки
                System.out.println("Column ["+i+"] ");

                System.out.println("Text " + col.getText().toString());

                if(col.getText().toString().equals("substr(timeVal,1,11)")){
                    col.setText("По часам");
                }
                if(col.getText().toString().equals("sum(ZoneValue)")){
                    col.setText("Сумма");
                }
            }

            while(resultset.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=resultset.getMetaData().getColumnCount(); i++){
                    row.add(resultset.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                data.add(row);
            }
            tableView.setItems(data);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try{
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    @FXML
    public void initialize(){

        //langXML();
        //loadLang(langXML);

        textFieldPathToDB.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != ""){
                choiceBoxItems();
            }
        });

        choiceBoxGroupBy.getItems().addAll("Default", "By day", "By hour");

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////// 'Listener' for the choice of the year and month////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        choiceBoxYear.getSelectionModel().selectedItemProperty().addListener((observableYear, oldValueYear, newValueYear) -> {
            System.out.println();
            System.out.println("In the menu 'year' is selected: " + newValueYear);
            choiceBoxMounth.getSelectionModel().selectedItemProperty().addListener((observableMonth, oldValueMonth, newValueMonth) -> {
                System.out.println("In the menu 'month' is selected: " + newValueMonth);
                if(mapCalendar.get(newValueYear).get(newValueMonth).getLocalPath() != null){
                    buttonOpen.setDisable(false);
                }else
                    buttonOpen.setDisable(true);
            });
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////             BUTTON 'DOWNLOAD'         ////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        buttonDownload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //alertLang(langXML);

                System.out.println();
                System.out.println("*** Нажата кнопка 'Download' ***");
                System.out.println();


                Stage dialogStage = new Stage();						// Диалоговое окно с индикатором загрузки
                ProgressBar pb = new ProgressBar();
                Label label = new Label();
                pb.indeterminateProperty();

                dialogStage.initStyle(StageStyle.UTILITY);
                dialogStage.initModality(Modality.APPLICATION_MODAL);

                final VBox vb = new VBox();
                vb.setSpacing(10);
                vb.setAlignment(Pos.CENTER);
                vb.getChildren().addAll(pb,label);

                Scene scene = new Scene(vb);
                dialogStage.setHeight(100);
                dialogStage.setWidth(200);
                //dialogStage.setTitle(bundleAlert.getString("loading"));

                dialogStage.setScene(scene);
                dialogStage.setResizable(false);

                String year;
                String month;
                year = choiceBoxYear.getValue();
                month = choiceBoxMounth.getValue();

                if(year == null || month == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Пожалуйста заполните все имеющиеся поля");
                    alert.showAndWait();
                }else{
                    dialogStage.show();
                }

                Task<Integer> task = new Task<Integer>(){
                    @Override
                    protected Integer call() throws Exception {
                        try{

                            String http;
                            String name;

                            name = year + "-" + month + ".db";

                            http = mapCalendar.get(year).get(month).getWebUrl();
                            System.out.println("Web address of the selected file: " + http);

                            URL fileOnWeb = new URL(http);
                            HttpURLConnection conn = (HttpURLConnection) fileOnWeb.openConnection();

                            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

                            File file = new File(textFieldPathToDB.getText() + "/DB/" + name);
                            FileOutputStream fw = new FileOutputStream(file);

                            byte[] b = new byte[1048576];
                            int count = 0;

                            while((count = bis.read(b)) != -1)
                                fw.write(b,0,count);

                            fw.close();
                            path = textFieldPathToDB.getText() + "/DB/" + name;

                        }catch(NullPointerException e){
                            e.printStackTrace();
                            throw e;

                        }catch (IOException e) {
                            e.printStackTrace();
                            throw e;
                        }catch(IllegalStateException e){
                            e.printStackTrace();
                            throw e;
                        }
                        finally{
                            this.succeeded();
                        }
                        return 0;
                    }
                };

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
                    @Override
                    public void handle(WorkerStateEvent event) {
                        dialogStage.close();
                        buildData();
                    }
                });

                thread = new Thread(task);
                thread.start();
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////              BUTTON 'OPEN'            ////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        buttonOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //alertLang(langXML);

                System.out.println();
                System.out.println("*** Нажата кнопка 'Open' ***");
                System.out.println();

                Stage dialogStage = new Stage();						// Диалоговое окно с индикатором загрузки
                ProgressBar pb = new ProgressBar();
                Label label = new Label();
                pb.indeterminateProperty();

                dialogStage.initStyle(StageStyle.UTILITY);
                dialogStage.initModality(Modality.APPLICATION_MODAL);

                final VBox vb = new VBox();
                vb.setSpacing(10);
                vb.setAlignment(Pos.CENTER);
                vb.getChildren().addAll(pb,label);

                Scene scene = new Scene(vb);
                dialogStage.setHeight(100);
                dialogStage.setWidth(200);
                //dialogStage.setTitle(bundleAlert.getString("loading"));

                dialogStage.setScene(scene);
                dialogStage.setResizable(false);
                dialogStage.show();

                Task<Integer> task = new Task<Integer>(){
                    @Override
                    protected Integer call() throws Exception {
                        try{
                            String year;
                            String month;
                            year = choiceBoxYear.getValue();
                            month = choiceBoxMounth.getValue();

                            if(year == null | month == null){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Ошибка");
                                alert.setHeaderText("Пожалуйста заполните все имеющиеся поля");
                                alert.showAndWait();
                            }else{
                                path = mapCalendar.get(choiceBoxYear.getValue()).get(choiceBoxMounth.getValue()).getLocalPath();
                                File fileOnDisk = new File(path);
                                System.out.println(fileOnDisk);
                            }
                        }catch(NullPointerException e){
                            System.out.println("Here is exception ");
                        }
                        finally{
                            this.succeeded();
                        }
                        return 0;
                    }
                };

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
                    @Override
                    public void handle(WorkerStateEvent event) {
                        dialogStage.close();
                        buildData();
                    }
                });

                thread = new Thread(task);
                thread.start();

            }
        });


        choiceBoxGroupBy.getSelectionModel().selectedItemProperty().addListener((observableGroupBy, oldValueGroupBy, newValueGroupBy) ->{
            if(newValueGroupBy.equals("By hour")){
                groupingByHour();
            }
            if(newValueGroupBy.equals("By day")){
                groupingByDay();
            }
            if(newValueGroupBy.equals("Default")){
                buildData();
            }
        });


    }

}
