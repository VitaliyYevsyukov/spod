package presenters.menuBar;

public class ConnectToFBData {

    private String fbPathToDB;
    private String fbPort;
    private String fbUserName;
    private String fbPassword;

    public ConnectToFBData(){
        fbPathToDB = new String();
        fbPort = new String();
        fbUserName = new String();
        fbPassword = new String();
    }
    

    public String getFbPathToDB() {
        return fbPathToDB;
    }

    public void setFbPathToDB(String fbPathToDB) {
        this.fbPathToDB = fbPathToDB;
    }

    public String getFbPort() {
        return fbPort;
    }

    public void setFbPort(String fbPort) {
        this.fbPort = fbPort;
    }

    public String getFbUserName() {
        return fbUserName;
    }

    public void setFbUserName(String fbUserName) {
        this.fbUserName = fbUserName;
    }

    public String getFbPassword() {
        return fbPassword;
    }

    public void setFbPassword(String fbPassword) {
        this.fbPassword = fbPassword;
    }
}
