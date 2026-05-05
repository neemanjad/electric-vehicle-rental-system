package home.project.model;

public class Bicycle extends Vehicle {
	private int range;
	
	public Bicycle(String iD, String model, String status, String manufacturer, double purchasePrice, String image, int range) {
		super(iD, model, status, manufacturer, purchasePrice, image);
		this.range = range;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", " + range + " m";
	}
}
