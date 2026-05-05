package home.project.am.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import home.project.am.dto.PagedResponseDTO;
import home.project.am.model.vehicle.Bicycle;
import home.project.am.service.BicycleService;

@RestController
@RequestMapping("/api/bicycles")
public class BicycleController {
	private final BicycleService service;
	
	public BicycleController(BicycleService service) {
		this.service = service;
	}
	
	@GetMapping
	public PagedResponseDTO<Bicycle> getAllScooters(
	    @RequestParam(defaultValue = "1") int page,
	    @RequestParam(defaultValue = "8") int itemsPerPage) {
	    
	    Page<Bicycle> pageResult = service.getScootersByPage(page, itemsPerPage);
	    
	    return new PagedResponseDTO<>(
	        pageResult.getContent(),
	        pageResult.getTotalPages(),
	        pageResult.getTotalElements(),
	        page
	    );
	}
	
	@PostMapping
    public ResponseEntity<String> addCar(@RequestBody Bicycle bicycle) {
        try {
            service.saveBicycle(bicycle);
            return ResponseEntity.status(HttpStatus.CREATED).body("Bicycle added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding bicycle: " + e.getMessage());
        }
    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBicycle(@PathVariable String id){
		try {
			service.deleteBicycle(id);
			return ResponseEntity.status(HttpStatus.OK).body("Bicycle deleted successfully!");
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting bicycle: " + e.getMessage());
		}
	}
}
