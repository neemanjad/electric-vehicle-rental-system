package home.project.am.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import home.project.am.model.vehicle.Scooter;

public interface ScooterRepository extends JpaRepository<Scooter, String> {
	Page<Scooter> findAll(Pageable pageable);
	
	@Modifying
	@Query("DELETE FROM Scooter s WHERE s.vehicle.ID = :vehicleId")
	void deleteByVehicleId(@Param("vehicleId") String vehicleId);
	
	@Query("SELECT COUNT(s) > 0 FROM Scooter s WHERE s.vehicle.ID = :vehicleId")
	boolean existsByVehicleId(@Param("vehicleId") String vehicleId);
}
