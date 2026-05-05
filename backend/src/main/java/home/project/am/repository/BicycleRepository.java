package home.project.am.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import home.project.am.model.vehicle.Bicycle;

public interface BicycleRepository extends JpaRepository<Bicycle, String> {
	Page<Bicycle> findAll(Pageable pageable);
	
	@Modifying
	@Query("DELETE FROM Bicycle b WHERE b.vehicle.ID = :vehicleId")
	void deleteByVehicleId(@Param("vehicleId") String vehicleId);
	
	@Query("SELECT COUNT(b) > 0 FROM Bicycle b WHERE b.vehicle.ID = :vehicleId")
	boolean existsByVehicleId(@Param("vehicleId") String vehicleId);
}
