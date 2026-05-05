package home.project.beans;

import java.io.Serializable;
import java.util.Objects;

public class PromotionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title, description, expirationDate;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, expirationDate, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PromotionBean other = (PromotionBean) obj;
		return Objects.equals(description, other.description) && Objects.equals(expirationDate, other.expirationDate)
				&& Objects.equals(title, other.title);
	}
}
