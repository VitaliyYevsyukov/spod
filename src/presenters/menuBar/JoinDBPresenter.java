package presenters.menuBar;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class JoinDBPresenter {

    @FXML
    Button btnOK, btnPathToDB;
    @FXML
    TextField txtMsDBName, txtMsPort, txtMsLocalHost, txtMsUserName, txtFbPathToDB, txtFbPort, txtFbUserName;
    @FXML
    PasswordField txtFbPassword, txtMsPassword;

    
    String portFB = null;
    String pathFB = null;
    String userFB = null;
    String passwordFB = null;
    
    String dbMSName = null;
    String localHostMS = null;
    String userNameMS = null;
    String portMS = null;
    String passwordMS = null;
    
    boolean fieldIsDirect = false;
    
    public boolean connectDone() {
    	if(fieldIsDirect == true) {
    		return true;
    	}else
    		return false;
    	
    }

    public void buttonOkEvent(){
    	dataBaseConnection();
    	if(connectDone() == true) {
    		Stage stage = (Stage) btnOK.getScene().getWindow();
            stage.close();
            connectDone();
    	}
    }

    public void btnPathToDBEvent(){
        if(txtFbPathToDB.getText().equals("")){
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("DB files (.fdb)", "*.fdb");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showOpenDialog(null);
            if(file != null){
                String path = file.getPath();
                txtFbPathToDB.setText(path);
            }
        }
    }

    public void dataBaseConnection(){

    	portFB = txtFbPort.getText();
        pathFB = txtFbPathToDB.getText();
        userFB = txtFbUserName.getText();
        passwordFB = txtFbPassword.getText();
        
        dbMSName = txtMsDBName.getText();
        localHostMS = txtMsLocalHost.getText();
        userNameMS = txtMsUserName.getText();
        portMS = txtMsPort.getText();
        passwordMS = txtMsPassword.getText();
        
        if(!portFB.equals("") & !pathFB.equals("") & !userFB.equals("") & !passwordFB.equals("")
        		& !dbMSName.equals("") & !localHostMS.equals("") & !userNameMS.equals("") & !portMS.equals("") & !passwordMS.equals("")) {
        	System.out.println("ALL FIELDS ARE DIRECT");
        	
        	fieldIsDirect = true;
        	
        	String fileName = System.getProperty("user.dir") +"\\" + "configuration.xml";
            String workingDirectory = System.getProperty("user.dir");
            String filePath = "";

            filePath = workingDirectory + File.separator + fileName;

            File xmlFile = new File(filePath);
            
    			try {
    				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    				Document document = documentBuilder.parse(xmlFile);
    				
    				Node nodePordFB = document.getElementsByTagName("portToFBDB").item(0); 
    	            nodePordFB.setTextContent(portFB);
    	            
    	            Node nodePathFB = document.getElementsByTagName("pathToFBDB").item(0);
    	            nodePathFB.setTextContent(pathFB);
    	            
    	            Node nodeUserFB = document.getElementsByTagName("userFBDB").item(0);
    	            nodeUserFB.setTextContent(userFB);
    	            
    	            Node nodePasswordFB = document.getElementsByTagName("passwordFBDB").item(0);
    	            nodePasswordFB.setTextContent(passwordFB);
    	            
    	            Node nodePort = document.getElementsByTagName("port").item(0);
                    nodePort.setTextContent(portMS);

                    Node nodeLocalHost = document.getElementsByTagName("localHost").item(0);
                    nodeLocalHost.setTextContent(localHostMS);

                    Node nodeUserName = document.getElementsByTagName("userName").item(0);
                    nodeUserName.setTextContent(userNameMS);

                    Node nodePassword = document.getElementsByTagName("password").item(0);
                    nodePassword.setTextContent(passwordMS);

                    Node nodeDBName = document.getElementsByTagName("dbName").item(0);
                    nodeDBName.setTextContent(dbMSName);
    	            
    	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
    	            Transformer transformer = transformerFactory.newTransformer();
    	            DOMSource source = new DOMSource(document);
    	            StreamResult result = new StreamResult(new File(filePath));
    	            transformer.transform(source, result);
    	            
    			} catch (ParserConfigurationException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (SAXException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (TransformerConfigurationException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (TransformerException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	
        }else {
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setContentText("��������� ���������� ��� ����");
        	alert.show();
        }

    }

    @FXML
    public void initialize(){

    }
}
