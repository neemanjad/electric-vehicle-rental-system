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
@Table(name = "BICYCLE")
public class Bicycle {
	@Id
    @Column(name = "VEHICLE_ID", nullable = false)
    private String id;
	
	@Column(name = "distanceRange")
	private int distanceRange;
	
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

	public int getDistanceRange() {
		return distanceRange;
	}

	public void setDistanceRange(int range) {
		this.distanceRange = range;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
}
