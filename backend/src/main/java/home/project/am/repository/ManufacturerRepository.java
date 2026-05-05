package home.project.am.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import home.project.am.model.vehicle.Manufacturer;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, String> {
	Page<Manufacturer> findAll(Pageable pageable);
}
