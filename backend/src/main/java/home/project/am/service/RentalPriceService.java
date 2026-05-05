package home.project.am.service;

import org.springframework.stereotype.Service;
import home.project.am.dto.RentalPriceContainer;
import home.project.am.repository.RentalPriceRepository;

@Service
public class RentalPriceService {

    private final RentalPriceRepository repository;

    public RentalPriceService(RentalPriceRepository repository) {
        this.repository = repository;
    }

    public RentalPriceContainer getAllPrices() {
        return new RentalPriceContainer(repository.findAll());
    }

    public RentalPriceContainer updateAllPrices(RentalPriceContainer newPrices) {
        return new RentalPriceContainer(repository.saveAll(newPrices.getPrices()));
    }
}
