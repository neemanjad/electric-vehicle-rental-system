package home.project.am.controller;

import org.springframework.web.bind.annotation.*;

import home.project.am.dto.RentalPriceContainer;
import home.project.am.service.RentalPriceService;

@RestController
@RequestMapping("/api/rental-prices")
public class RentalPriceController {
    private final RentalPriceService service;

    public RentalPriceController(RentalPriceService service) {
        this.service = service;
    }

    @GetMapping
    public RentalPriceContainer getAllRentalPrices() {
        return service.getAllPrices();
    }

    @PutMapping
    public RentalPriceContainer updateAllPrices(@RequestBody RentalPriceContainer newPrices) {
        return service.updateAllPrices(newPrices);
    }
}
