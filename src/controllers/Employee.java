package controllers;

/**
 * Created by Vitaly on 10.02.2017.
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dbo.TrafficObjects")
public class Employee {
	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ObjectId")
	private String id;

	@Column(name = "Name")
	private String objectName;

	// @Column(name="lastName")
	// private String lastName;

	public Employee() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	@Override
	public String toString() {
		return "Employee: " + this.id + ", " + this.objectName;// + ", " + this.lastName;
	}

}
