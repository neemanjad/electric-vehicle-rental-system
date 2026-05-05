package home.project.dto;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String userName, password, firstName, lastName, documentId, email, telephone;
	private String pictureAsStringBase64;
	private boolean isBlocked;
	
	public User(String userName, String password, String firstName, String lastName, String documentId, String email,
			String telephone, String picture, boolean isBlocked) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.documentId = documentId;
		this.email = email;
		this.telephone = telephone;
		this.pictureAsStringBase64 = picture;
		this.isBlocked = isBlocked;
	}

	public User() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPictureAsStringBase64() {
		return pictureAsStringBase64;
	}

	public void setPictureAsStringBase64(String picture) {
		this.pictureAsStringBase64 = picture;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(userName, other.userName);
	}
}
