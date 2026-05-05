package home.project.am.controller;

import home.project.am.dto.PagedResponseDTO;
import home.project.am.model.rental.Rental;
import home.project.am.service.RentalService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping
    public PagedResponseDTO<Rental> getAllRentals(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "6") int size) {
        Page<Rental> rentalPage = rentalService.getAllRentals(page, size);
        return new PagedResponseDTO<>(
            rentalPage.getContent(),
            rentalPage.getTotalPages(),
            rentalPage.getTotalElements(),
            rentalPage.getNumber() + 1 
        );
    }

    @GetMapping("/{vehicleId}")
    public PagedResponseDTO<Rental> getRentalsByVehicleId(@PathVariable String vehicleId,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "6") int size) {
        Page<Rental> rentalPage = rentalService.getRentalsByVehicleId(vehicleId, page, size);
        return new PagedResponseDTO<>(
            rentalPage.getContent(),
            rentalPage.getTotalPages(),
            rentalPage.getTotalElements(),
            rentalPage.getNumber() + 1
        );
    }
    
    @GetMapping("/available-months")
    public List<String> getAvailableMonths() {
        return rentalService.getAvailableMonths();
    }
    
    @GetMapping("/revenue/{monthYear}")
    public Map<Integer, Double> getRevenueByMonth(@PathVariable String monthYear) {
        return rentalService.getRevenueByMonth(monthYear);
    }
    
    @GetMapping("/revenueByType")
    public Map<String, Double> getRevenueByVehicleType() {
        return rentalService.getRevenueByVehicleType();
    }
}
