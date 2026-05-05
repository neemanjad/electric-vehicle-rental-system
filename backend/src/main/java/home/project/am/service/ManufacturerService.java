package home.project.am.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import home.project.am.dto.ManufacturerContainer;
import home.project.am.dto.PagedResponseDTO;
import home.project.am.model.vehicle.Manufacturer;
import home.project.am.model.vehicle.Vehicle;
import home.project.am.repository.BicycleRepository;
import home.project.am.repository.CarRepository;
import home.project.am.repository.ManufacturerRepository;
import home.project.am.repository.RentalRepository;
import home.project.am.repository.ScooterRepository;
import home.project.am.repository.VehicleRepository;
import home.project.am.securityutil.SecurityUtil;
import jakarta.transaction.Transactional;

@Service
public class ManufacturerService {

	private final ManufacturerRepository repository;
	private final VehicleRepository vRepository;
	private final RentalRepository rRepository;
	private final CarRepository cRepository;
	private final BicycleRepository bRepository;
	private final ScooterRepository sRepository;
	
	public ManufacturerService(ManufacturerRepository repository, VehicleRepository vRepository, 
			RentalRepository rRepository, CarRepository cRepository, BicycleRepository bRepository, ScooterRepository sRepository) {
		this.repository = repository;
		this.vRepository = vRepository;
		this.rRepository = rRepository;
		this.bRepository = bRepository;
		this.sRepository = sRepository;
		this.cRepository = cRepository;
	}
	
	public PagedResponseDTO<ManufacturerContainer> getManufacturersByPage(int page, int itemsPerPage) {
	    Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
	    Page<Manufacturer> manufacturersPage = repository.findAll(pageable);

	    List<ManufacturerContainer> manufacturerContainers = manufacturersPage.getContent().stream()
	        .map(manufacturer -> {
	            int numberOfVehicles = vRepository.countByManufacturer(manufacturer.getName());
	            ManufacturerContainer container = new ManufacturerContainer();
	            container.setManufacturer(manufacturer);
	            container.setNumberOfProducedVehicles(numberOfVehicles);
	            return container;
	        })
	        .toList();

	    return new PagedResponseDTO<>(
	        manufacturerContainers, 
	        manufacturersPage.getTotalPages(),
	        manufacturersPage.getTotalElements(),
	        manufacturersPage.getNumber() + 1
	    );
	}

	public void addManufacturer(Manufacturer manufacturer) {
		if(!SecurityUtil.isSafeCredential(manufacturer.getAddress()) || !SecurityUtil.isSafeCredential(manufacturer.getCountry()) 
				|| !SecurityUtil.isSafeCredential(manufacturer.getName()) || !SecurityUtil.isSafeCredential(manufacturer.getEmail()))
			throw new IllegalArgumentException("Neispravan korisnički naziv!");
		
	    repository.saveAndFlush(manufacturer);
	}
	
	@Transactional
	public void deleteManufacturer(String manufacturerName) {
		if(!SecurityUtil.isSafeCredential(manufacturerName))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
	    
		List<Vehicle> vehicles = vRepository.findByManufacturer(manufacturerName);
	    for (Vehicle vehicle : vehicles) {
	        rRepository.deleteByVehicleId(vehicle.getID()); 
	        
	        if (cRepository.existsByVehicleId(vehicle.getID())) {
	            cRepository.deleteByVehicleId(vehicle.getID());
	        }
	        if (sRepository.existsByVehicleId(vehicle.getID())) {
	            sRepository.deleteByVehicleId(vehicle.getID());
	        }
	        if (bRepository.existsByVehicleId(vehicle.getID())) {
	            bRepository.deleteByVehicleId(vehicle.getID());
	        }
	        vRepository.delete(vehicle); 
	    }

	    repository.deleteById(manufacturerName); 
	}
	
	@Transactional
	public void updateManufacturerDifferentName(String manufacturerName, Manufacturer manufacturer) {
		if(!SecurityUtil.isSafeCredential(manufacturerName))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
		
		addManufacturer(manufacturer);
		
		List<Vehicle> vehicles = vRepository.findByManufacturer(manufacturerName);
		for(Vehicle vehicle : vehicles) {
			vehicle.setManufacturer(manufacturer.getName()); 
	        vRepository.save(vehicle); 
		}
		
		deleteManufacturer(manufacturerName);
	}
	
	@Transactional
	public void updateManufacturer(Manufacturer manufacturer) {
		if(!SecurityUtil.isSafeCredential(manufacturer.getAddress()) || !SecurityUtil.isSafeCredential(manufacturer.getCountry()) 
				|| !SecurityUtil.isSafeCredential(manufacturer.getName()) || !SecurityUtil.isSafeCredential(manufacturer.getEmail()))
			throw new IllegalArgumentException("Neispravan korisnički naziv!");
		
		Manufacturer existingManufacturer = repository.findById(manufacturer.getName())
		        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
		
		existingManufacturer.setAddress(manufacturer.getAddress());
		existingManufacturer.setCountry(manufacturer.getCountry());
		existingManufacturer.setEmail(manufacturer.getEmail());
		existingManufacturer.setFax(manufacturer.getFax());
		existingManufacturer.setName(manufacturer.getName());
		existingManufacturer.setTelephone(manufacturer.getTelephone());
		
	    repository.save(existingManufacturer);
	}	
}