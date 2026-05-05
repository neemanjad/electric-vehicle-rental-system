package home.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import home.project.logger.ClientLogger;
import home.project.model.Rental;

public class RentalDAO {
	
	private static final ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	
	private static final String SQL_INSERT_RENTAL = 
			"INSERT INTO rental (dateTime, startX, startY, endX, endY, price, VEHICLE_ID, CLIENT_USER_userName, licenceNumber, documentNumber, seconds) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static final String SQL_GET_RENTALS = "SELECT * FROM rental WHERE CLIENT_USER_userName=?;";
	
	private static final int MIN_USERNAME_LENGTH = 3;
								
	public static boolean insertRental(Rental rental) {
		boolean result = false;
		
		if(rental == null)
			return result;
		
		Connection conn = null;
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_INSERT_RENTAL);
			
			ps.setString(1, rental.getDateTime());
			ps.setInt(2, rental.getStartX());
			ps.setInt(3, rental.getStartY());
			ps.setInt(4, rental.getEndX());
			ps.setInt(5, rental.getEndY());
			ps.setDouble(6, rental.getPrice());
			ps.setString(7, rental.getVehicleID());
			ps.setString(8, rental.getUserName());
			ps.setString(9, rental.getLicenceNumber());
			ps.setString(10, rental.getDocumentNumber());
			ps.setInt(11, rental.getSeconds());
			
			result = ps.executeUpdate() == 1 ? true : false;
			
		} catch (SQLException e) {
		    ClientLogger.logError("SQLException occurred: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
		return result;
	}
	
	public static ArrayList<Rental> getRentals(String userName){
		if(userName == null || userName.length() < MIN_USERNAME_LENGTH)
			return null;
		
		ResultSet rs = null;
		Connection conn = null;
		
		ArrayList<Rental> rentals = new ArrayList<>();
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_GET_RENTALS);
			ps.setString(1, userName);
			
			rs = ps.executeQuery();
			while(rs.next())
				rentals.add(new Rental(
							rs.getInt("idRental"), rs.getString("dateTime"), rs.getInt("startX"), rs.getInt("startY"), rs.getInt("endX"),
							rs.getInt("endY"), rs.getDouble("price"), rs.getString("VEHICLE_ID"), userName, rs.getString("licenceNumber"), 
							rs.getString("documentNumber"), rs.getInt("seconds")));
			
		} catch(SQLException e) {
			ClientLogger.logError("SQLException occurred: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
		return rentals;
	}
}
