package home.project.am.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import home.project.am.model.vehicle.Scooter;
import home.project.am.model.vehicle.Vehicle;
import home.project.am.repository.ScooterRepository;
import home.project.am.securityutil.SecurityUtil;

@Service
public class ScooterService {
	private final ScooterRepository repository;
	private static final String SCOOTER_ILLUSTRATION_PHOTO = "C:\\Users\\PC\\Desktop\\Fakultet\\IPFinal\\Project_NemanjaDavidovic_1194_15\\photos\\scooter-ill.jpg";
	
	public ScooterService(ScooterRepository repository) {
		this.repository = repository;
	}
	
	public List<Scooter> getAllScooters(){
		return repository.findAll();
	}
	
    public Page<Scooter> getScootersByPage(int page, int itemsPerPage) {
        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        return repository.findAll(pageable);
    }
    
	public void saveScooter(Scooter scooter) {
		Vehicle vehicle = scooter.getVehicle();
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle objekat je obavezan.");
        }
        
        if(!SecurityUtil.isSafeCredential(scooter.getId()) || !SecurityUtil.isSafeCredential(vehicle.getManufacturer())
        		|| !SecurityUtil.isSafeCredential(vehicle.getModel()))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
        
        if(vehicle.getPicture() == null) {
        	vehicle.setPicture(loadRandomVehicleImage());
        }
        repository.save(scooter);
    }
	
	public void deleteScooter(String id) {
		if(!SecurityUtil.isSafeCredential(id))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
		repository.deleteById(id);
	}
	
	private byte[] loadRandomVehicleImage() {
	    try {
	        return Files.readAllBytes(Paths.get(SCOOTER_ILLUSTRATION_PHOTO)); // Konvertuje sliku u byte[]
	    } catch (IOException e) {
	        throw new RuntimeException("Greška pri učitavanju slike", e);
	    }
	}
}
