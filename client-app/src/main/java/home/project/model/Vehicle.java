package home.project.model;

import java.util.Objects;

public class Vehicle {
	
	private String ID, model, status, manufacturer, pictureAsStringBase64;
	private double purchasePrice;
	
	public Vehicle(String iD, String model, String status, String manufacturer, double purchasePrice, String pictureAsStringBase64) {
		super();
		ID = iD;
		this.model = model;
		this.status = status;
		this.manufacturer = manufacturer;
		this.purchasePrice = purchasePrice;
		this.pictureAsStringBase64 = pictureAsStringBase64;
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
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public double getPurchasePrice() {
		return purchasePrice;
	}
	
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getPictureAsStringBase64() {
		return pictureAsStringBase64;
	}

	public void setPictureAsStringBase64(String pictureAsStringBase64) {
		this.pictureAsStringBase64 = pictureAsStringBase64;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehicle other = (Vehicle) obj;
		return Objects.equals(ID, other.ID);
	}

	@Override
	public String toString() {
		return ID + ", " + model + ", " + status + ", " + manufacturer + ", " + purchasePrice;
	}
}
