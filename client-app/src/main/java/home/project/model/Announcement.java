package home.project.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Announcement {
	
	private int idAnnouncement;
	private String title, content;
	private boolean isPromotion;
	private Date expirationDate;

	public int getIdAnnouncement() {
		return idAnnouncement;
	}

	public void setIdAnnouncement(int idAnnouncement) {
		this.idAnnouncement = idAnnouncement;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean IsPromotion() {
		return isPromotion;
	}

	public void setIsPromotion(boolean isPromotion) {
		this.isPromotion = isPromotion;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    try {
	        this.expirationDate = dateFormat.parse(expirationDate);
	    } catch (ParseException e) {
	        System.err.println("Invalid date format: " + expirationDate);
	        this.expirationDate = null;
	    }	}
}