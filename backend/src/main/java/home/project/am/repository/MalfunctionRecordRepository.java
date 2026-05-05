package home.project.am.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import home.project.am.model.vehicle.MalfunctionRecord;
import home.project.am.model.vehicle.MalfunctionRecordId;

public interface MalfunctionRecordRepository extends JpaRepository<MalfunctionRecord, MalfunctionRecordId> {
	@Query("SELECT v, COUNT(m) FROM Vehicle v JOIN MalfunctionRecord m ON v.ID = m.vehicle.ID GROUP BY v")
	Page<Object[]> countMalfunctionsByVehicle(Pageable pageable);
}
