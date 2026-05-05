package home.project.am.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import home.project.am.model.vehicle.Car;
import home.project.am.model.vehicle.Vehicle;
import home.project.am.repository.CarRepository;
import home.project.am.securityutil.SecurityUtil;

@Service
public class CarService {
	private final CarRepository repository;
	private static final String CAR_ILLUSTRATION_PHOTO = "C:\\Users\\PC\\Desktop\\Fakultet\\IPFinal\\Project_NemanjaDavidovic_1194_15\\photos\\car-ill.jpg";
    
    public CarService(CarRepository repository) {
        this.repository = repository;
    }
    
    public void saveCar(Car car) {
        Vehicle vehicle = car.getVehicle();
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle objekat je obavezan.");
        }
        
        if(!SecurityUtil.isSafeCredential(car.getDescription()) || !SecurityUtil.isSafeCredential(vehicle.getManufacturer())
        		|| !SecurityUtil.isSafeCredential(vehicle.getModel()))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
        
        if(vehicle.getPicture() == null) {
        	vehicle.setPicture(loadRandomVehicleImage());
        }
        
        repository.save(car);
    }
	
	public List<Car> getAllCars(){
		return repository.findAll();
	}
	
	public Page<Car> getCarsByPage(int page, int itemsPerPage) {
        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        return repository.findAll(pageable);
	}
	
	public void deleteCar(String id) {
		if(!SecurityUtil.isSafeCredential(id))
			throw new IllegalArgumentException("Neispravan korisnički naziv!");
		  
	    repository.deleteById(id);
	}
	
	private byte[] loadRandomVehicleImage() {
	    try {
	        return Files.readAllBytes(Paths.get(CAR_ILLUSTRATION_PHOTO)); // Konvertuje sliku u byte[]
	    } catch (IOException e) {
	        throw new RuntimeException("Greška pri učitavanju slike", e);
	    }
	}
}
