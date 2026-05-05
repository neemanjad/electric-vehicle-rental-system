package home.project.beans;

import java.io.Serializable;

import home.project.dto.User;

public class UserBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private User user = new User();
	private boolean isLoggedIn = false;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
}
