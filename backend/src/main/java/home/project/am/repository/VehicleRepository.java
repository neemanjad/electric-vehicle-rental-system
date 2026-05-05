package home.project.am.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import home.project.am.model.vehicle.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    int countByManufacturer(String MANUFACTURER_name);
    List<Vehicle> findByManufacturer(String manufacturerName);
}
