package home.project.am.model.vehicle;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import home.project.am.model.rental.Rental;
import jakarta.persistence.*;

@Entity
@Table(name = "VEHICLE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vehicle {
	@Id
	@Column(name = "ID", nullable = false)
	private String ID;
	
	@Column(name = "model")
	private String model;
	
	@Column(name = "purchasePrice")
	private double purchasePrice;
	
	@Lob
	@Column(name = "picture", columnDefinition = "MEDIUMBLOB")
	private byte[] picture;
	
	@Column(name = "MANUFACTURER_name")
	private String manufacturer;
	
	@Column(name = "status")
	private String status;
	
	@OneToMany(mappedBy = "vehicle", cascade = CascadeType.REMOVE)
	private List<Rental> rentals;
	
	@OneToMany(mappedBy = "vehicle", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonIgnore
	private List<MalfunctionRecord> malfunctionRecords;


	public List<MalfunctionRecord> getMalfunctionRecords() {
        return malfunctionRecords;
    }

    public void setMalfunctionRecords(List<MalfunctionRecord> malfunctionRecords) {
        this.malfunctionRecords = malfunctionRecords;
    }
    
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
