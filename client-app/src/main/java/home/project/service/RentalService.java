package home.project.service;

import java.util.ArrayList;

import home.project.dao.RentalDAO;
import home.project.model.Rental;

public class RentalService {
	
	public static boolean insertRental(Rental rental) {
		return RentalDAO.insertRental(rental);
	}
	
	public static ArrayList<Rental> getRentals(String userName){
		return RentalDAO.getRentals(userName);
	}
}
