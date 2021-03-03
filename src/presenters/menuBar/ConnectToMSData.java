package presenters.menuBar;

public class ConnectToMSData {

    private String msDBName;
    private String msPort;
    private String msLocalHost;
    private String msUserName;
    private String msPassword;

    public ConnectToMSData(){
        msDBName = new String();
        msPort = new String();
        msLocalHost = new String();
        msUserName = new String();
        msPassword = new String();
    }
    

    public String getMsDBName() {
        return msDBName;
    }

    public void setMsDBName(String msDBName) {
        this.msDBName = msDBName;
    }

    public String getMsPort() {
        return msPort;
    }

    public void setMsPort(String msPort) {
        this.msPort = msPort;
    }

    public String getMsLocalHost() {
        return msLocalHost;
    }

    public void setMsLocalHost(String msLocalHost) {
        this.msLocalHost = msLocalHost;
    }

    public String getMsUserName() {
        return msUserName;
    }

    public void setMsUserName(String msUserName) {
        this.msUserName = msUserName;
    }

    public String getMsPassword() {
        return msPassword;
    }

    public void setMsPassword(String msPassword) {
        this.msPassword = msPassword;
    }
}
