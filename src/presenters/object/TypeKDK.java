package presenters.object;

public class TypeKDK {

	String name_KDK;
	String chanels;
	String programs;
	String directions;
	String phases;
	String max_TVP;
	String idKDK;

	public TypeKDK(String idKDK, String name_KDK, String chanels, String programs, String directions, String phases, String max_TVP) {
		this.idKDK = idKDK;
		this.name_KDK = name_KDK;
		this.chanels = chanels;
		this.programs = programs;
		this.directions = directions;
		this.phases = phases;
		this.max_TVP = max_TVP;
	}
	

	public String getName_KDK() {
		return name_KDK;
	}

	public void setName_KDK(String name_KDK) {
		this.name_KDK = name_KDK;
	}

	public String getChanels() {
		return chanels;
	}

	public void setChanels(String chanels) {
		this.chanels = chanels;
	}

	public String getPrograms() {
		return programs;
	}

	public void setPrograms(String programs) {
		this.programs = programs;
	}

	public String getDirections() {
		return directions;
	}

	public void setDirections(String directions) {
		this.directions = directions;
	}

	public String getPhases() {
		return phases;
	}

	public void setPhases(String phases) {
		this.phases = phases;
	}

	public String getMax_TVP() {
		return max_TVP;
	}

	public void setMax_TVP(String max_TVP) {
		this.max_TVP = max_TVP;
	}

	public String getIdKDK() {
		return idKDK;
	}

	public void setIdKDK(String idKDK) {
		this.idKDK = idKDK;
	}

}
