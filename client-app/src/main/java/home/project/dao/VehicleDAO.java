package home.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import home.project.logger.ClientLogger;
import home.project.model.Bicycle;
import home.project.model.Car;
import home.project.model.Scooter;

public class VehicleDAO {
	
	private static final ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	
	private static final String FREE_STATUS = "free";
	private static final String RENTED_STATUS = "rented";
	
	private static final String SQL_VEHICLE_PART = "SELECT v.ID, v.model, v.purchasePrice, v.picture, v.MANUFACTURER_name, v.status, ";
	private static final String SQL_STATUS_AND_MALFUNCTION_PART = 
			" WHERE v.status='" + FREE_STATUS + "';";
	
	private static final String SQL_SELECT_SCOOTERS = 
				SQL_VEHICLE_PART + "s.maxSpeed FROM vehicle v INNER JOIN scooter s ON v.ID = s.VEHICLE_ID" + SQL_STATUS_AND_MALFUNCTION_PART;
	
	private static final String SQL_SELECT_BICYCLES = 
				SQL_VEHICLE_PART + "b.distanceRange FROM vehicle v INNER JOIN bicycle b ON v.ID = b.VEHICLE_ID" + SQL_STATUS_AND_MALFUNCTION_PART;
	
	private static final String SQL_SELECT_CARS =
				SQL_VEHICLE_PART + "c.purchaseDate, c.description FROM vehicle v INNER JOIN car c ON v.id = c.VEHICLE_ID" + SQL_STATUS_AND_MALFUNCTION_PART;
	
	private static final String SQL_SELECT_RENT_PRICE = "SELECT price FROM rental_price WHERE type=?;";
	
	private static final String SQL_VEHICLE_SET_STATUS = "UPDATE vehicle SET status=? WHERE ID=?;";
	
	private static int MIN_TYPE_LENGTH = 3;
	
	public static boolean setVehicleStatus(String ID, boolean freeFlag) {
		
		if(ID == null || ID.length() < MIN_TYPE_LENGTH)
			return false;
		
		String STATUS = freeFlag ? FREE_STATUS : RENTED_STATUS;
		
		boolean result = false;		
		Connection conn = null;
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_VEHICLE_SET_STATUS);
			ps.setString(1, STATUS);
			ps.setString(2, ID);
			
			result = ps.executeUpdate() == 1 ? true : false;
			
		} catch(SQLException e) {
			ClientLogger.logError("SQLException occurred: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
		return result;
	}
	
	public static double getPrice(String type) {
		
		if(type == null || type.length() < MIN_TYPE_LENGTH)
			return -1;
		
		ResultSet rs = null;
		Connection conn = null;
		double price = -1;
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_SELECT_RENT_PRICE);
			ps.setString(1, type);
			
			rs = ps.executeQuery();
			if(rs.next())
				price = rs.getDouble("price");
			
		} catch(SQLException e) {
			ClientLogger.logError("SQLException occurred: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
		return price;
	}
	
	public static ArrayList<Bicycle> getBicycles(){
		ResultSet rs = null;
		Connection conn = null;
		ArrayList<Bicycle> bicycles = new ArrayList<>();
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BICYCLES);
			
			rs = ps.executeQuery();
			while(rs.next())
				bicycles.add(new Bicycle(rs.getString("ID"), rs.getString("model"), rs.getString("status"), 
						rs.getString("MANUFACTURER_name"), rs.getDouble("purchasePrice"), 
						Base64.getEncoder().encodeToString(rs.getBytes("picture")), rs.getInt("distanceRange")));
			
		} catch(SQLException e) {
			ClientLogger.logError("SQLException occurred: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
		return bicycles;
	}
	
	public static ArrayList<Car> getCars(){
		ResultSet rs = null;
		Connection conn = null;
		ArrayList<Car> cars = new ArrayList<>();
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_SELECT_CARS);
			
			rs = ps.executeQuery();
			while(rs.next())
				cars.add(new Car(rs.getString("ID"), rs.getString("model"), rs.getString("status"), 
						rs.getString("MANUFACTURER_name"), rs.getDouble("purchasePrice"), 
						Base64.getEncoder().encodeToString(rs.getBytes("picture")), rs.getString("purchaseDate"), rs.getString("description")));
			
		} catch(SQLException e) {
			ClientLogger.logError("SQLException occurred: " + e.getMessage());
		} finally{
			connectionPool.checkIn(conn);
		}
		return cars;
	}

	public static ArrayList<Scooter> getScooters(){
		
		ResultSet rs = null;
		Connection conn = null;
		ArrayList<Scooter> scooters = new ArrayList<>();
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_SELECT_SCOOTERS);
			
			rs = ps.executeQuery();
			
			while(rs.next())
				scooters.add(new Scooter(rs.getString("ID"), rs.getString("model"), rs.getString("status"), 
						rs.getString("MANUFACTURER_name"), rs.getDouble("purchasePrice"), 
						Base64.getEncoder().encodeToString(rs.getBytes("picture")), rs.getInt("maxSpeed")));
			
		} catch(SQLException e) {
			ClientLogger.logError("SQLException occurred: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
		return scooters;
	}
}
