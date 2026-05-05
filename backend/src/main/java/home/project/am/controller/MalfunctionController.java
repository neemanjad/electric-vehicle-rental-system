package home.project.am.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import home.project.am.dto.MalfunctionContainer;
import home.project.am.dto.PagedResponseDTO;
import home.project.am.dto.VehicleMalfunctionContainer;
import home.project.am.service.MalfunctionService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/malfunctions")
public class MalfunctionController {
	private final MalfunctionService service;
	
	public MalfunctionController(MalfunctionService service) {
		this.service = service;
	}
	
	@GetMapping("/{id}")
	public PagedResponseDTO<MalfunctionContainer> getAllMalfunctions(
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "6") int itemsPerPage,
	        @PathVariable String id) {

	    Page<MalfunctionContainer> pageResult = service.getMalfunctionsForVehicleByPage(page, itemsPerPage, id);

	    return new PagedResponseDTO<>(
	            pageResult.getContent(),
	            pageResult.getTotalPages(),
	            pageResult.getTotalElements(),
	            page
	    );
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMalfunction(@PathVariable int id) {
	    try {
	        service.deleteMalfunctionById(id);
	        return ResponseEntity.ok().build();
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().build(); 
	    }
	}
	
	@PostMapping
	public ResponseEntity<Void> addMalfunction(@RequestBody MalfunctionContainer malfunctionRequest) {
	    try {
	        service.addMalfunction(malfunctionRequest);
	        return ResponseEntity.status(201).build();
	        
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().build();
	    }
	}

	@GetMapping("/all")
	public PagedResponseDTO<VehicleMalfunctionContainer> getMalfunctionCountPaged(
	        @RequestParam(defaultValue = "1") int page, 
	        @RequestParam(defaultValue = "6") int size) {
	        
	        return service.getMalfunctionsPaged(page, size);
	    }
}
