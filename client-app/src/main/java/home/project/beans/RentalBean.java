package home.project.beans;

import java.io.Serializable;
import java.util.ArrayList;

import home.project.model.Rental;

public class RentalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<Rental> rentals;

	public ArrayList<Rental> getRentals() {
		return rentals;
	}

	public void setRentals(ArrayList<Rental> rentals) {
		this.rentals = rentals;
	}
}
