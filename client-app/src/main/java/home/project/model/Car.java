package home.project.model;

public class Car extends Vehicle {
	private String purchaseDate, description;
	
	public Car(String iD, String model, String status, String manufacturer, double purchasePrice, String image, String purchaseDate, String description) {
		super(iD, model, status, manufacturer, purchasePrice, image);
		this.purchaseDate = purchaseDate;
		this.description = description;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
