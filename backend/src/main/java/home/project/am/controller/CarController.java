package home.project.am.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

import home.project.am.dto.PagedResponseDTO;
import home.project.am.model.vehicle.Car;
import home.project.am.service.CarService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/cars")
public class CarController {
	private final CarService service;
	
	public CarController(CarService service) {
		this.service = service;
	}
	
	@GetMapping
	public PagedResponseDTO<Car> getAllCars(
	    @RequestParam(defaultValue = "1") int page,
	    @RequestParam(defaultValue = "8") int itemsPerPage) {
	    
	    Page<Car> pageResult = service.getCarsByPage(page, itemsPerPage);
	    
	    return new PagedResponseDTO<>(
	        pageResult.getContent(),
	        pageResult.getTotalPages(),
	        pageResult.getTotalElements(),
	        page
	    );
	}

	@PostMapping
    public ResponseEntity<String> addCar(@RequestBody Car car) {
        try {
            service.saveCar(car);
            return ResponseEntity.status(HttpStatus.CREATED).body("Car added successfully!");
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding car: " + e.getMessage());
        }
    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCar(@PathVariable String id) {
	    try {
	        service.deleteCar(id);
	        return ResponseEntity.status(HttpStatus.OK).body("Car deleted successfully!");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting car: " + e.getMessage());
	    }
	}
}
