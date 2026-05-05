package home.project.am.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import home.project.am.model.rental.Rental;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
	Page<Rental> findByVehicle_ID(String vehicleId, Pageable pageable);
	
	@Modifying
	@Query("DELETE FROM Rental r WHERE r.vehicle.ID = :vehicleId")
	void deleteByVehicleId(@Param("vehicleId") String vehicleId);
	
    Page<Rental> findAllByOrderByDateTimeAsc(Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE FUNCTION('SUBSTRING_INDEX', r.dateTime, ' ', -1) = :year " +
            "AND FUNCTION('SUBSTRING_INDEX', FUNCTION('SUBSTRING_INDEX', r.dateTime, ' ', 2), ' ', -1) = :month")
     List<Rental> findByMonthAndYear(@Param("month") String month, @Param("year") String year);
    
    @Query("SELECT " +
            "CASE " +
            "  WHEN r.vehicle.ID LIKE 'C%' THEN 'CAR' " +
            "  WHEN r.vehicle.ID LIKE 'B%' THEN 'BICYCLE' " +
            "  WHEN r.vehicle.ID LIKE 'S%' THEN 'SCOOTER' " +
            "  ELSE 'UNKNOWN' " +
            "END, " +
            "SUM(r.price) " +
            "FROM Rental r GROUP BY " +
            "CASE " +
            "  WHEN r.vehicle.ID LIKE 'C%' THEN 'CAR' " +
            "  WHEN r.vehicle.ID LIKE 'B%' THEN 'BICYCLE' " +
            "  WHEN r.vehicle.ID LIKE 'S%' THEN 'SCOOTER' " +
            "  ELSE 'UNKNOWN' " +
            "END")
     List<Object[]> calculateRevenueByVehicleType();
 }
