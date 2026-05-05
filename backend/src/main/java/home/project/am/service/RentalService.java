package home.project.am.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import home.project.am.model.rental.Rental;
import home.project.am.repository.RentalRepository;
import home.project.am.securityutil.SecurityUtil;

@Service
public class RentalService {
	@Autowired
    private RentalRepository repository;

    public Page<Rental> getAllRentals(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size); 
        return repository.findAll(pageable);
    }

    public Page<Rental> getRentalsByVehicleId(String vehicleId, int page, int size) {
    	if(!SecurityUtil.isSafeCredential(vehicleId))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
    	
        Pageable pageable = PageRequest.of(page - 1, size);
        return repository.findByVehicle_ID(vehicleId, pageable);
    }
    
    public List<String> getAvailableMonths() {
        return repository.findAll().stream()
            .map(rental -> {
                String[] dateParts = rental.getDateTime().split(" ");
                String month = dateParts[1]; 
                String year = dateParts[5];  
                return month + " " + year;   
            })
            .distinct()
            .collect(Collectors.toList());
    }
    
    public Map<Integer, Double> getRevenueByMonth(String monthYear) {
    	if(!SecurityUtil.isSafeCredential(monthYear))
	        throw new IllegalArgumentException("Neispravan korisnički naziv!");
    	
        String[] parts = monthYear.split(" "); 
        String month = parts[0]; 
        String year = parts[1]; 

        List<Rental> rentals = repository.findByMonthAndYear(month, year);

        return rentals.stream()
            .collect(Collectors.groupingBy(
                rental -> Integer.parseInt(rental.getDateTime().split(" ")[2]), 
                Collectors.summingDouble(Rental::getPrice)
            ));
    }
    
    public Map<String, Double> getRevenueByVehicleType() {
        List<Object[]> results = repository.calculateRevenueByVehicleType();
        return results.stream().collect(Collectors.toMap(
            row -> (String) row[0],
            row -> (Double) row[1]
        ));
    }
}
