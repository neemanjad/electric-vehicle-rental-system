package home.project.beans;

import java.io.Serializable;
import java.util.Objects;

public class AnnounBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ann_title, ann_content;

	public String getAnn_title() {
		return ann_title;
	}

	public void setAnn_title(String title) {
		this.ann_title = title;
	}

	public String getAnn_content() {
		return ann_content;
	}

	public void setAnn_content(String content) {
		this.ann_content = content;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ann_content, ann_title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnnounBean other = (AnnounBean) obj;
		return Objects.equals(ann_content, other.ann_content) && Objects.equals(ann_title, other.ann_title);
	}
}