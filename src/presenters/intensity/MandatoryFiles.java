package presenters.intensity;

/**
 * Created by Vitaly on 14.03.2017.
 */
public class MandatoryFiles {

    String nameFile;

    public String getName() {
        return nameFile;
    }

    public void setName(String name) {
        this.nameFile = name;
    }

    @Override
    public String toString(){
        return "nameFile = " + this.nameFile;
    }
    

}
