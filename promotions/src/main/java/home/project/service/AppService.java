package home.project.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import home.project.beans.AnnounBean;
import home.project.beans.PromBean;
import home.project.beans.UserBean;
import home.project.logger.ManagerLogger;

public class AppService {
	
	private static final ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	
	private static final String SQL_SELECT_BY_USERNAME = "SELECT * FROM user WHERE userName=?;";
	private static final String SQL_SELECT_ROLE_FOR_EMPLOYEE = "SELECT role FROM employee WHERE USER_userName=?;";
	private static final String SQL_INSERT_IN_ANNOUNCEMENT = "INSERT INTO announcement(title, content, isPromotion, expirationDate) VALUES(?, ?, ?, ?);";
	private static final String SQL_GET_ALL_ANNOUNCEMENTS = "SELECT * FROM announcement;";
	
	private static final String MANAGER_ROLE = "manager";
	
	private static final ArrayList<PromBean> promotionPosts = new ArrayList<>();
	private static final ArrayList<AnnounBean> announPosts = new ArrayList<>();
	
	static{
		loadPosts();
	}	
	
	public UserBean loginUser(UserBean userBean) {

		if(userBean == null)
			return null;
		
		if(!validateEntry(userBean.getUserName()) || !validateEntry(userBean.getPassword()))
			return null;
		
		ResultSet rs = null;
		Connection conn = null;
		
		UserBean tmpBean = null;
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ROLE_FOR_EMPLOYEE);

			ps.setString(1, userBean.getUserName());
			rs = ps.executeQuery();
			
			if(rs.next() && MANAGER_ROLE.equals(rs.getString("role"))) {

				ps = conn.prepareStatement(SQL_SELECT_BY_USERNAME);
				ps.setString(1, userBean.getUserName());
				
				rs = ps.executeQuery();
				
				if(rs.next() && hashPass(userBean.getPassword()).equals(rs.getString("password")) && !rs.getBoolean("isBlocked")) {

					tmpBean = new UserBean();
					tmpBean.setUserName(userBean.getUserName());
					tmpBean.setFirstName(rs.getString("firstName"));
					tmpBean.setLastName(rs.getString("lastName"));
					tmpBean.setPassword(null);
				}
			}
			
		} catch(SQLException e) {
	    	ManagerLogger.logError("Error communication with database: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
		return tmpBean;
	}
	
	public ArrayList<AnnounBean> getAnnounByContent(String content){
		
		if(content == null || content.length() < 1)
			return new ArrayList<>();
		
		ArrayList<AnnounBean> announs = new ArrayList<>();
		for(AnnounBean ann : announPosts)
			if(ann.getAnn_content().contains(content))
				announs.add(ann);
		return announs;
	}
	
	public ArrayList<PromBean> getPromotionPosts(){
		if(promotionPosts.size() == 0)
			return new ArrayList<>();
		else {
			ArrayList<PromBean> pBs = new ArrayList<PromBean>(promotionPosts);
			pBs.sort(Comparator.comparing(PromBean::getProm_expirationDate));
			return pBs;
		}
	}
	
	public ArrayList<AnnounBean> getAnnounPosts(String content){
		if(content == null || "".equals(content))
			return new ArrayList<AnnounBean>(announPosts);
		else {
			ArrayList<AnnounBean> announs = new ArrayList<>();
			for(AnnounBean aB : announPosts)
				if(aB.getAnn_content().contains(content))
					announs.add(aB);
			return announs;
		}
	}
	
	public boolean insertProm(PromBean postBean) {
		
		if(postBean == null)
			return false;
		
		if(!validateEntry(postBean.getProm_title()) || !validateEntry(postBean.getProm_description()) || !validateEntry(postBean.getProm_expirationDate().toString()))
			return false;
		
		Connection conn = null;
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_INSERT_IN_ANNOUNCEMENT);
			
			ps.setString(1, postBean.getProm_title());
			ps.setString(2, postBean.getProm_description());
			ps.setBoolean(3, true);
			ps.setDate(4, (java.sql.Date) postBean.getProm_expirationDate());
			
			if(ps.executeUpdate() == 1) {
				promotionPosts.add(postBean);
				return true;
			} else
				return false;
			
		} catch(SQLException e) {
	    	ManagerLogger.logError("Error communication with database: " + e.getMessage());
			return false;
		} finally {
			connectionPool.checkIn(conn);
		}
	}
	
	public boolean insertAnnoun(AnnounBean announBean) {
		
		if(announBean == null)
			return false;
		
		if(!validateEntry(announBean.getAnn_title()) || !validateEntry(announBean.getAnn_content()))
			return false;
		
		Connection conn = null;
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_INSERT_IN_ANNOUNCEMENT);
			
			ps.setString(1, announBean.getAnn_title());
			ps.setString(2, announBean.getAnn_content());
			ps.setBoolean(3, false);
			ps.setDate(4, null);
			
			if(ps.executeUpdate() == 1) {
				announPosts.add(announBean);
				return true;
			} else
				return false;
			
		} catch(SQLException e) {
	    	ManagerLogger.logError("Error communication with database: " + e.getMessage());
			return false;
		} finally {
			connectionPool.checkIn(conn);
		}
	}
	
	public java.sql.Date convertStringToSqlDate(String dateString) {
	    try {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date utilDate = dateFormat.parse(dateString);
	       
	        return new java.sql.Date(utilDate.getTime());
	        
	    } catch (ParseException e) {
	    	ManagerLogger.logError("Error during parsing: " + e.getMessage());
	        return null;
	    }
	}
	
	private static void loadPosts() {
		
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = connectionPool.checkOut();
			PreparedStatement ps = conn.prepareStatement(SQL_GET_ALL_ANNOUNCEMENTS);
			
			rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getBoolean("isPromotion")) {
					PromBean pB = new PromBean();
					pB.setProm_title(rs.getString("title"));
					pB.setProm_description(rs.getString("content"));
					pB.setProm_expirationDate((Date) rs.getDate("expirationDate"));
					promotionPosts.add(pB);
				} else {
					AnnounBean aB = new AnnounBean();
					aB.setAnn_title(rs.getString("title"));
					aB.setAnn_content(rs.getString("content"));
					announPosts.add(aB);
				}
			}
			System.out.println("Posts successfully loaded!");
			
		} catch(SQLException e) {
	    	ManagerLogger.logError("Error communication with database: " + e.getMessage());
		} finally {
			connectionPool.checkIn(conn);
		}
	}
	
 	private String hashPass(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
	    	ManagerLogger.logError("Error: Alghoritm not founded: " + e.getMessage());
            throw new RuntimeException("Error: Alghoritm not founded: ", e);
        }
    }
	
	private boolean validateEntry(String entry) {
		
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
		
		int maxLength = 800;
		if (entry.length() > maxLength) {
			System.out.println("The input: " + entry + " is too long and may cause a buffer overflow!");
			return false;
		}	       
		return true; 
	}
}
