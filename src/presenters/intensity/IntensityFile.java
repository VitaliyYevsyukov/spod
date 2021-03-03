package presenters.intensity;

/**
 * Created by Vitaly on 14.03.2017.
 */
public class IntensityFile {

    private String nameURL;
    private String nameLocal;
    private String HTTPPort;

    public String getHTTPPort(){
        return HTTPPort;
    }
    

    public void setHTTPPort(String HTTPPort){
        this.HTTPPort = HTTPPort;
    }

    public String getWebUrl() {
        return nameURL;
    }

    public void setWebUrl(String nameURL) {
        this.nameURL = nameURL;
    }

    public String getLocalPath(){
        return nameLocal;
    }

    public void setLocalPath(String nameLocal){
        this.nameLocal = nameLocal;
    }

    public String toString(){
        return nameURL + " " + nameLocal + " " + HTTPPort;
    }

}
