package home.project.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import home.project.beans.UserBean;
import home.project.container.PasswordContainer;
import home.project.dto.User;
import home.project.logger.ClientLogger;

public class UserDAO {

	private static ConnectionPool connectionPool;
	
	static {
        try {
            connectionPool = ConnectionPool.getConnectionPool();
        } catch (Exception e) {
        	ClientLogger.logError("SQLException occurred: " + e.getMessage());
            throw new RuntimeException("Neuspjeh u inicijalizaciji ConnectionPool-a.", e);
        }
    }
	
	private static final int MIN_CREDENTIALS_LENGTH = 4;
	
	private static final String SQL_SELECT_BY_USERNAME_FROM_USER = "SELECT * FROM user WHERE userName=?;";
	private static final String SQL_SELECT_BY_USERNAME_FROM_CLIENT = "SELECT * FROM client WHERE USER_userName=?;";
	private static final String SQL_CHANGE_PASSWORD = "UPDATE user SET password=? WHERE userName=? AND password=?;";
	private static final String SQL_INSERT_USER = "INSERT INTO user VALUES(?, ?, ?, ?, ?);";
	private static final String SQL_INSERT_CLIENT = "INSERT INTO client VALUES(?, ?, ?, ?, ?);";
	private static final String SQL_DEACTIVATE = "UPDATE user SET isBlocked=true WHERE userName=?;";
	
	public static boolean deactivateAccount(String userName) {
		if(userName == null || userName.length() < MIN_CREDENTIALS_LENGTH)
			return false;
		
		if(!validateEntry(userName))
			return false;
		
		boolean result = false;
		Connection conn = null;
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_DEACTIVATE);
			
			ps.setString(1, userName);
			result = ps.executeUpdate() == 1 ? true : false;
			
		} catch(SQLException e) {
			ClientLogger.logError("SQLException occurred: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
		return result;
	}
	
	public static boolean insertUser(User user) {
		if(!checkUser(user))
			return false;
		
		boolean result = false;
		Connection conn = null;
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_INSERT_USER);
			
			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getUserName());
			ps.setString(4, hashPass(user.getPassword()));
			ps.setBoolean(5, false);
			
			if(ps.executeUpdate() == 1) {
				ps = conn.prepareStatement(SQL_INSERT_CLIENT);
				
				ps.setString(1, user.getDocumentId());
				ps.setString(2, user.getEmail());
				ps.setString(3, user.getTelephone());
				ps.setBytes(4, Base64.getDecoder().decode(user.getPictureAsStringBase64()));
				ps.setString(5, user.getUserName());
				
				result = ps.executeUpdate() == 1 ? true : false;
			}
			
		} catch(SQLException e) {
			ClientLogger.logError("SQLException occurred: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
		return result;
	}
	
	public static boolean changePassword(String userName, PasswordContainer container) {
		boolean result = false;
		
		if(userName == null || userName.length() < MIN_CREDENTIALS_LENGTH || !checkContainer(container))
			return result;
		
		Connection conn = null;
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_CHANGE_PASSWORD);
			
			ps.setString(1, hashPass(container.getNewPassword()));
			ps.setString(2, userName);
			ps.setString(3, hashPass(container.getOldPassword()));
			
			result = ps.executeUpdate() == 1 ? true : false;
			
		} catch(SQLException e) {
			ClientLogger.logError("SQLException occurred: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
		return result;
	}
	
	public static UserBean loginUser(String userName, String password) {

		if(userName == null || userName.length() < MIN_CREDENTIALS_LENGTH)
			return null;
		
		if(password == null || password.length() < MIN_CREDENTIALS_LENGTH)
			return null;
		
		if(!validateEntry(userName) || !validateEntry(password))
			return null;
		
		ResultSet rs = null;
		Connection conn = null;
		
		UserBean tmpBean = null;
		User user = null;
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_USERNAME_FROM_USER);

			ps.setString(1, userName);
			rs = ps.executeQuery();
			
			if(rs.next() && rs.getString("password").equals(hashPass(password)) && !rs.getBoolean("isBlocked")) {
				
				user = new User();
				user.setUserName(userName);
				user.setPassword(null);
				user.setFirstName(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				user.setBlocked(false);
				
				ps = conn.prepareStatement(SQL_SELECT_BY_USERNAME_FROM_CLIENT);
				ps.setString(1, userName);
				
				rs = ps.executeQuery();
				
				if(rs.next()) {
					user.setDocumentId(rs.getString("documentId"));
					user.setEmail(rs.getString("email"));
					user.setTelephone(rs.getString("telephone"));
					
					Blob blob = rs.getBlob("picture");
					byte[] pictureBytes = blob.getBytes(1, (int) blob.length());
					user.setPictureAsStringBase64(Base64.getEncoder().encodeToString(pictureBytes));
					
					tmpBean = new UserBean();
					tmpBean.setUser(user);
					tmpBean.setLoggedIn(true);
				}
			}
			
		} catch(SQLException e) {
			ClientLogger.logError("SQLException occurred: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
		return tmpBean;
	}
	
	private static boolean checkUser(User user) {
		if(user == null)
			return false;
		if(user.getUserName() == null || user.getPassword() == null || user.getFirstName() == null || user.getLastName() == null)
			return false;
		if(!validateEntry(user.getUserName()) || !validateEntry(user.getFirstName()) || !validateEntry(user.getLastName()) || !validateEntry(user.getEmail()))
			return false;
		return true;
	}
	
	private static boolean checkContainer(PasswordContainer container) {
		if(container == null)
			return false;
		if(container.getOldPassword() == null || container.getOldPassword().length() < MIN_CREDENTIALS_LENGTH)
			return false;
		if(container.getNewPassword() == null || container.getNewPassword().length() < MIN_CREDENTIALS_LENGTH)
			return false;
		return true;
	}

 	private static String hashPass(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            ClientLogger.logError("NoSuchAlgorithmException occurred: " + e.getMessage());
            throw new RuntimeException("Error: Alghoritm not founded: ", e);
        }
    }
	
	private static boolean validateEntry(String entry) {
		
		String sqlInjectionPattern = "('|--|;|/\\*|\\*/)";
		if (entry.matches(".*" + sqlInjectionPattern + ".*")) {
			System.out.println("The entry: " + entry + " contains potential SQL Injection characters!");
			return false;
		}

		String xssPattern = "<script.*?>.*?</script>|<.*?>";
		if (entry.matches(".*" + xssPattern + ".*")) {
			System.out.println("The entry: " + entry + " contains a potential XSS attack!");
			return false;
		}
		
		int maxLength = 50;
		if (entry.length() > maxLength) {
			System.out.println("The input: " + entry + " is too long and may cause a buffer overflow!");
			return false;
		}	       
		return true; 
	}
}
