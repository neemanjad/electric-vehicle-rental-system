package home.project.beans;

import java.io.Serializable;
import java.util.Objects;

public class AnnouncementBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title, content;

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

	@Override
	public int hashCode() {
		return Objects.hash(content, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnnouncementBean other = (AnnouncementBean) obj;
		return Objects.equals(content, other.content) && Objects.equals(title, other.title);
	}
}
