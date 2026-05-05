package home.project.am.model.vehicle;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCOOTER")
public class Scooter {
	@Id
    @Column(name = "VEHICLE_ID", nullable = false)
    private String id;
	
	@Column(name = "maxSpeed")
	private int maxSpeed;
	
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @MapsId 
    @JoinColumn(name = "VEHICLE_ID", referencedColumnName = "ID")
    private Vehicle vehicle;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
}
