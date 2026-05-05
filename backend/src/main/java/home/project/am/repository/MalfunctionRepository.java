package home.project.am.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import home.project.am.dto.MalfunctionContainer;
import home.project.am.model.vehicle.Malfunction;

public interface MalfunctionRepository extends JpaRepository<Malfunction, Integer> {
	@Query("SELECT new home.project.am.dto.MalfunctionContainer(m.idMalfunction, m.description, m.repairCosts, r.malfunctionDate, r.repairDate) " +
		       "FROM Malfunction m JOIN MalfunctionRecord r ON m.idMalfunction = r.id.malfunctionId " +
		       "WHERE r.id.vehicleId = :vehicleId")
		Page<MalfunctionContainer> findMalfunctionsByVehicleId(@Param("vehicleId") String vehicleId, Pageable pageable);

}
