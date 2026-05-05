package home.project.am.model.vehicle;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "CAR")
public class Car {

	@Id
    @Column(name = "VEHICLE_ID", nullable = false)
    private String id;

    @Column(name = "purchaseDate")
    private Date purchaseDate;

    @Column(name = "description")
    private String description;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @MapsId 
    @JoinColumn(name = "VEHICLE_ID", referencedColumnName = "ID")
    private Vehicle vehicle;

	public String getId() {
		return id;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
}
