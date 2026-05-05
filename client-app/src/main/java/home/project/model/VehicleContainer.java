package home.project.model;

import java.util.ArrayList;

public class VehicleContainer<T> {
	private double price;
	private ArrayList<T> vehicles;
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public ArrayList<T> getVehicles() {
		return vehicles;
	}
	
	public void setVehicles(ArrayList<T> vehicles) {
		this.vehicles = vehicles;
	}
}
