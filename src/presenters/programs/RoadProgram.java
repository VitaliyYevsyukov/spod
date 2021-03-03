package presenters.programs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Vitaly on 20.01.2017.
 */
public class RoadProgram {

	private String IdProgram;
	private String progOrigin;
	private SimpleStringProperty roadProgram_number;
    private SimpleObjectProperty<ProgramMode> roadProgram_programMode;
    private SimpleObjectProperty<BackupProgram> roadProgram_backupProgram;

    public RoadProgram() {
    	IdProgram = new String();
    	progOrigin = new String();
        roadProgram_number = new SimpleStringProperty();
        roadProgram_programMode = new SimpleObjectProperty<>();
        roadProgram_backupProgram = new SimpleObjectProperty<>();
    }
    
    
    public String getProgOrigin() {
		return progOrigin;
	}

	public void setProgOrigin(String progOrigin) {
		this.progOrigin = progOrigin;
	}

    public String getRoadProgram_number() {
        return roadProgram_number.get();
    }
    public void setRoadProgram_number(String roadProgram_number) {
        this.roadProgram_number.set(roadProgram_number);
    }
    public StringProperty roadProgramNumberProperty(){
        return this.roadProgram_number;
    }


    public ProgramMode getRoadProgram_programMode() {
        return roadProgram_programMode.get();
    }
    public void setRoadProgram_programMode(ProgramMode roadProgram_programMode) {
        this.roadProgram_programMode.set(roadProgram_programMode);
    }
    public ObjectProperty<ProgramMode> roadProgramProgramModeProperty(){
        return this.roadProgram_programMode;
    }


    public BackupProgram getRoadProgram_backupProgram() {
        return roadProgram_backupProgram.get();
    }
    public void setRoadProgram_backupProgram(BackupProgram roadProgram_backupProgram) {
        this.roadProgram_backupProgram.set(roadProgram_backupProgram);
    }
    public ObjectProperty<BackupProgram> roadProgramBackupProgramProperty(){
        return this.roadProgram_backupProgram;
    }
    
    public String getIdProgram() {
		return IdProgram;
	}

	public void setIdProgram(String idProgram) {
		IdProgram = idProgram;
	}


}
