package home.project.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class PromBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String prom_title, prom_description;
	private Date prom_expirationDate;
	
	public String getProm_title() {
		return prom_title;
	}
	
	public void setProm_title(String title) {
		this.prom_title = title;
	}
	
	public String getProm_description() {
		return prom_description;
	}
	
	public void setProm_description(String description) {
		this.prom_description = description;
	}
	
	public Date getProm_expirationDate() {
		return prom_expirationDate;
	}
	
	public void setProm_expirationDate(Date expirationDate) {
		this.prom_expirationDate = expirationDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(prom_expirationDate, prom_title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PromBean other = (PromBean) obj;
		return Objects.equals(prom_expirationDate, other.prom_expirationDate) && Objects.equals(prom_title, other.prom_title);
	}
}
