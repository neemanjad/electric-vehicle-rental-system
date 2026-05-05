package home.project.service;

import home.project.container.VehicleContainer;
import home.project.dao.VehicleDAO;
import home.project.model.Bicycle;
import home.project.model.Car;
import home.project.model.Scooter;

public class VehicleService {
	private static final String CAR = "car";
	private static final String SCOOTER = "scooter";
	private static final String BICYCLE = "bicycle";
	
	public static VehicleContainer<Car> getCars(){
		VehicleContainer<Car> container = new VehicleContainer<>();
		container.setVehicles(VehicleDAO.getCars());
		container.setPrice(VehicleDAO.getPrice(CAR));
		return container;
	}
	
	public static VehicleContainer<Bicycle> getBicycles(){
		VehicleContainer<Bicycle> container = new VehicleContainer<>();
		container.setVehicles(VehicleDAO.getBicycles());
		container.setPrice(VehicleDAO.getPrice(BICYCLE));
		return container;
	}
	
	public static VehicleContainer<Scooter> getScooters(){
		VehicleContainer<Scooter> container = new VehicleContainer<>();
		container.setVehicles(VehicleDAO.getScooters());
		container.setPrice(VehicleDAO.getPrice(SCOOTER));
		return container;
	}
	
	public static boolean setRentedVehicleStatus(String ID) {
		return VehicleDAO.setVehicleStatus(ID, false);
	}
	
	public static boolean setFreeVehicleStatus(String ID) {
		return VehicleDAO.setVehicleStatus(ID, true);
	}
}