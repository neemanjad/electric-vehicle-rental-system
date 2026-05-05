package home.project.am.model.announcement;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "ANNOUNCEMENT")
public class Announcement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idAnnouncement")
	private Integer idAnnouncement;
	
	@Column(name = "title", nullable = false, length = 30)
	private String title;
	
	@Column(name = "content", length = 80)
	private String content;
	
	@Column(name = "isPromotion", nullable = false)
	private Boolean isPromotion;
	
	@Column(name = "expirationDate")
	@Temporal(TemporalType.DATE)
	private Date expirationDate;

	public Integer getIdAnnouncement() {
		return idAnnouncement;
	}

	public void setIdAnnouncement(Integer idAnnouncement) {
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

	public Boolean getIsPromotion() {
		return isPromotion;
	}

	public void setIsPromotion(Boolean isPromotion) {
		this.isPromotion = isPromotion;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
}