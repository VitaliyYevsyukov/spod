package roadModel;

import java.util.List;

/**
 * Created by Vitaly on 24.04.2017.
 */
public class RoadModelSettings {

    private String modelIP;
    private String conModId;
	private String connectionTypeName;
    private String db_id_object;
    private String db_name_object;
    private String db_number_object;
    private String db_path;
    private String db_path_options;
    private String configuration_datetime_loaded;
    private String objectId;
    private String hwId;
    private String lastUpdateTime;



	public RoadModelSettings(){
        modelIP = new String();
        conModId = new String();
        connectionTypeName = new String();
        db_id_object = new String();
        db_name_object = new String();
        db_number_object = new String();
        db_path = new String();
        db_path_options = new String();
        configuration_datetime_loaded = new String();
        objectId = new String();
        hwId = new String();
        lastUpdateTime = new String();
        
    }

    public String getModelIP() {
        return modelIP;
    }

    public void setModelIP(String modelIP){
        this.modelIP = modelIP;
    }
    
    public String getConModId() {
		return conModId;
	}

	public void setConModId(String conModId) {
		this.conModId = conModId;
	}

	public String getConnectionTypeName() {
		return connectionTypeName;
	}

	public void setConnectionTypeName(String connectionTypeName) {
		this.connectionTypeName = connectionTypeName;
	}

	public String getDb_id_object() {
		return db_id_object;
	}

	public void setDb_id_object(String db_id_object) {
		this.db_id_object = db_id_object;
	}

	public String getDb_name_object() {
		return db_name_object;
	}

	public void setDb_name_object(String db_name_object) {
		this.db_name_object = db_name_object;
	}

	public String getDb_number_object() {
		return db_number_object;
	}

	public void setDb_number_object(String db_number_object) {
		this.db_number_object = db_number_object;
	}

	public String getDb_path() {
		return db_path;
	}

	public void setDb_path(String db_path) {
		this.db_path = db_path;
	}

	public String getDb_path_options() {
		return db_path_options;
	}

	public void setDb_path_options(String db_path_options) {
		this.db_path_options = db_path_options;
	}

	public String getConfiguration_datetime_loaded() {
		return configuration_datetime_loaded;
	}

	public void setConfiguration_datetime_loaded(String configuration_datetime_loaded) {
		this.configuration_datetime_loaded = configuration_datetime_loaded;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getHwId() {
		return hwId;
	}

	public void setHwId(String hwId) {
		this.hwId = hwId;
	}
	

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
