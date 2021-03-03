package presenters.programs;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Vitaly on 10.01.2017.
 */
public class BackupProgram {

    private final SimpleStringProperty backup;

    public BackupProgram(String backup){
        this.backup = new SimpleStringProperty(backup);
    }
    

    public String getBackupProgram(){
        return this.backup.get();
    }

    public StringProperty backupProgramProperty() {
        return this.backup;
    }

    public void setBackupProgram(String backup){
        this.backup.set(backup);
    }

    @Override
    public String toString(){
        return backup.get();
    }

}
