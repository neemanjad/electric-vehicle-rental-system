package home.project.am.dto;

import home.project.am.model.vehicle.Manufacturer;

public class ManufacturerContainer {
	private int numberOfProducedVehicles;
	private Manufacturer manufacturer;
	
	public int getNumberOfProducedVehicles() {
		return numberOfProducedVehicles;
	}
	public void setNumberOfProducedVehicles(int numberOfProducedVehicles) {
		this.numberOfProducedVehicles = numberOfProducedVehicles;
	}
	public Manufacturer getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}
}
