package home.project.am.dto;

import java.util.List;

import home.project.am.model.rental.RentalPrice;

public class RentalPriceContainer {
	private List<RentalPrice> prices;
	
	public RentalPriceContainer() {
		super();
	}
	
	public RentalPriceContainer(List<RentalPrice> prices) {
		this.setPrices(prices);
	}

	public List<RentalPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<RentalPrice> prices) {
		this.prices = prices;
	}
}
