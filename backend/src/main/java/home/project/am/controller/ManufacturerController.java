package home.project.am.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import home.project.am.dto.ManufacturerContainer;
import home.project.am.dto.PagedResponseDTO;
import home.project.am.model.vehicle.Manufacturer;
import home.project.am.service.ManufacturerService;

@RestController
@RequestMapping("api/manufacturers")
public class ManufacturerController {

	private final ManufacturerService service;
	
	public ManufacturerController(ManufacturerService service) {
		this.service = service;
	}
	
	@GetMapping
    public ResponseEntity<PagedResponseDTO<ManufacturerContainer>> getManufacturers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int itemsPerPage) {
        
        PagedResponseDTO<ManufacturerContainer> response = service.getManufacturersByPage(page, itemsPerPage);
        return ResponseEntity.ok(response);
    }
	
	@PostMapping
	public ResponseEntity<String> addManufacturer(@RequestBody Manufacturer manufacturer){
		try {
			service.addManufacturer(manufacturer);
			return ResponseEntity.status(HttpStatus.CREATED).body("Employee unblocked successfully.");
		} catch(RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@DeleteMapping("/{name}")
	public ResponseEntity<String> deleteManufacturer(@PathVariable String name) {
	    try {
	        service.deleteManufacturer(name);
	        return ResponseEntity.ok("Manufacturer deleted successfully.");
	    } catch (RuntimeException e) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found: " + name);
	    }
	}
	
	@PutMapping("/{name}")
	public ResponseEntity<String> updateEmployee(@RequestBody Manufacturer manufacturer, @PathVariable String name){
		try {
	        if (name.equals(manufacturer.getName())) {
	        	service.updateManufacturer(manufacturer);
		        return ResponseEntity.ok("Manufacturer updated successfully.");
	        } else {
	        	service.updateManufacturerDifferentName(name, manufacturer);
	        	return ResponseEntity.ok("Manufacturer updated successfully.");
	        }     
	    } catch (ResponseStatusException e) {
	        throw e; 
	    } catch (RuntimeException e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
	    }
	}
}
