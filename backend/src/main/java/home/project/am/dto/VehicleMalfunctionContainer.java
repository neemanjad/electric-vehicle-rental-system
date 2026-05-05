package home.project.am.dto;

import home.project.am.model.vehicle.Vehicle;

public class VehicleMalfunctionContainer {
	private Vehicle vehicle;
    private int malfunctionCount;

    public VehicleMalfunctionContainer(Vehicle vehicle, int malfunctionCount) {
        this.vehicle = vehicle;
        this.malfunctionCount = malfunctionCount;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getMalfunctionCount() {
        return malfunctionCount;
    }
}
