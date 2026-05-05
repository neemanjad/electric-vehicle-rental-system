package home.project.am.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import home.project.am.model.vehicle.Bicycle;
import home.project.am.model.vehicle.Vehicle;
import home.project.am.repository.BicycleRepository;
import home.project.am.securityutil.SecurityUtil;

@Service
public class BicycleService {
	private final BicycleRepository repository;
	private static final String BICYCLE_ILLUSTRATION_PHOTO = "C:\\Users\\PC\\Desktop\\Fakultet\\IPFinal\\Project_NemanjaDavidovic_1194_15\\photos\\byke-ill.jpg";
	
	public BicycleService(BicycleRepository repository) {
		this.repository = repository;
	}
	
	public List<Bicycle> getAllBicycles(){
		return repository.findAll();
	}
	
	public Page<Bicycle> getScootersByPage(int page, int itemsPerPage) {
        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        return repository.findAll(pageable);
    }
	
	public void saveBicycle(Bicycle bicycle) {
		Vehicle vehicle = bicycle.getVehicle();
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle objekat je obavezan.");
        }
        
        if(!SecurityUtil.isSafeCredential(vehicle.getManufacturer()) || !SecurityUtil.isSafeCredential(vehicle.getModel()))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
        
        if(vehicle.getPicture() == null) {
        	vehicle.setPicture(loadRandomVehicleImage());
        }
        repository.save(bicycle);
    }
	
	public void deleteBicycle(String id) {
		if(!SecurityUtil.isSafeCredential(id))
			throw new IllegalArgumentException("Neispravan korisnički naziv!");
		
		repository.deleteById(id);
	}
	
	private byte[] loadRandomVehicleImage() {
	    try {
	        return Files.readAllBytes(Paths.get(BICYCLE_ILLUSTRATION_PHOTO)); // Konvertuje sliku u byte[]
	    } catch (IOException e) {
	        throw new RuntimeException("Greška pri učitavanju slike", e);
	    }
	}
}
