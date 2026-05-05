package home.project.am.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import home.project.am.model.vehicle.Car;

public interface CarRepository extends JpaRepository<Car, String> {
	Page<Car> findAll(Pageable pageable);
	
	@Modifying
	@Query(value = "DELETE FROM CAR WHERE VEHICLE_ID = :vehicleId", nativeQuery = true)
	void deleteByVehicleId(@Param("vehicleId") String vehicleId);
	
	@Query("SELECT COUNT(c) > 0 FROM Car c WHERE c.vehicle.ID = :vehicleId")
	boolean existsByVehicleId(@Param("vehicleId") String vehicleId);
}
