package home.project.am.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import home.project.am.model.rental.RentalPrice;

public interface RentalPriceRepository extends JpaRepository<RentalPrice, String> {

}
