package home.project.beans;

import java.io.Serializable;
import java.util.Objects;

public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName, password, firstName, lastName;
	private boolean isLoggedIn, isBlocked;
	
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
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
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
		UserBean other = (UserBean) obj;
		return Objects.equals(userName, other.userName);
	}
}