package home.project.model;

public class Scooter extends Vehicle {
	private int maxSpeed;
	
	public Scooter(String iD, String model, String status, String manufacturer, double purchasePrice, String image, int maxSpeed) {
		super(iD, model, status, manufacturer, purchasePrice, image);
		this.maxSpeed = maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}

	@Override
	public String toString() {
		return super.toString() + ", " + maxSpeed + " km/h";
	}
}
