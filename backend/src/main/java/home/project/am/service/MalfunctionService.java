package home.project.am.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import home.project.am.dto.MalfunctionContainer;
import home.project.am.dto.PagedResponseDTO;
import home.project.am.dto.VehicleMalfunctionContainer;
import home.project.am.model.vehicle.Malfunction;
import home.project.am.model.vehicle.MalfunctionRecord;
import home.project.am.model.vehicle.MalfunctionRecordId;
import home.project.am.model.vehicle.Vehicle;
import home.project.am.repository.MalfunctionRecordRepository;
import home.project.am.repository.MalfunctionRepository;
import home.project.am.repository.VehicleRepository;
import home.project.am.securityutil.SecurityUtil;
import jakarta.transaction.Transactional;

@Service
public class MalfunctionService {
	private final MalfunctionRepository repository;
	private final MalfunctionRecordRepository mrRepository;
	private final VehicleRepository vRepository;

	public MalfunctionService(MalfunctionRepository repository, MalfunctionRecordRepository mrRepository, VehicleRepository vRepository) {
		this.repository = repository;
		this.mrRepository = mrRepository;
		this.vRepository = vRepository;
	}
	
	public Page<MalfunctionContainer> getMalfunctionsForVehicleByPage(int page, int itemsPerPage, String vehicleId) {
		if(!SecurityUtil.isSafeCredential(vehicleId))
			throw new IllegalArgumentException("Neispravan korisnički naziv!");
		
	    Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
	    return repository.findMalfunctionsByVehicleId(vehicleId, pageable);
	}

	@Transactional
	public void deleteMalfunctionById(int id) {
	    try {
	        Optional<Malfunction> malfunctionOpt = repository.findById(id);
	        if (malfunctionOpt.isPresent()) {
	            Malfunction malfunction = malfunctionOpt.get();

	            malfunction.getMalfunctionRecords().forEach(record -> { updateVehicleStatus(record.getVehicle(), true); });

	            repository.deleteById(id);
	        } else {
	            throw new IllegalArgumentException("Malfunction with ID " + id + " does not exist.");
	        }
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("Error: " + e.getMessage());
	    }
	}
	
	public void addMalfunction(MalfunctionContainer malfunctionRequest) {
		if(!SecurityUtil.isSafeCredential(malfunctionRequest.getDescription()) 
				|| !SecurityUtil.isSafeCredential(malfunctionRequest.getMalfunctionDate()))
			throw new IllegalArgumentException("Neispravan korisnički naziv!");
		
		Optional<Vehicle> vehicle = vRepository.findById(malfunctionRequest.getVehicleId());
	    if (!vehicle.isPresent()) {
	        throw new IllegalArgumentException("Vehicle not found with ID: " + malfunctionRequest.getVehicleId());
	    }
	    
	    Malfunction malfunction = new Malfunction();
	    malfunction.setDescription(malfunctionRequest.getDescription());
	    malfunction.setRepairCosts(malfunctionRequest.getRepairCosts());

	    malfunction = repository.save(malfunction);

	    MalfunctionRecordId recordId = new MalfunctionRecordId();
	    recordId.setMalfunctionId(malfunction.getIdMalfunction()); 
	    recordId.setVehicleId(malfunctionRequest.getVehicleId());  

	    MalfunctionRecord malfunctionRecord = new MalfunctionRecord();
	    malfunctionRecord.setId(recordId);
	    malfunctionRecord.setMalfunction(malfunction); 
	    malfunctionRecord.setMalfunctionDate(malfunctionRequest.getMalfunctionDate());
	    malfunctionRecord.setRepairDate(malfunctionRequest.getRepairDate());
	    malfunctionRecord.setVehicle(vehicle.get());
	    updateVehicleStatus(vehicle.get(), false);

	    mrRepository.save(malfunctionRecord);
	}
	
	public PagedResponseDTO<VehicleMalfunctionContainer> getMalfunctionsPaged(int page, int size) {
	    Page<Object[]> results = mrRepository.countMalfunctionsByVehicle(PageRequest.of(page, size));

	    List<VehicleMalfunctionContainer> content = results.getContent().stream()
	        .map(row -> new VehicleMalfunctionContainer((Vehicle) row[0], ((Long) row[1]).intValue()))
	        .toList();
	    
	    return new PagedResponseDTO<>(content, results.getTotalPages(), results.getTotalElements(), results.getNumber());
	}

	private void updateVehicleStatus(Vehicle vehicle, boolean freeFlag) {
		if(vehicle == null ||  !SecurityUtil.isSafeCredential(vehicle.getManufacturer()) || !SecurityUtil.isSafeCredential(vehicle.getModel()))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");

		if(freeFlag)
			vehicle.setStatus("free");
		else
			vehicle.setStatus("broken");
		
		vRepository.save(vehicle);
	}
}
