package home.project.am.dto;

public class LoginResponse {
	private String role, jsonToken;
	
	public LoginResponse(String role, String jsonToken) {
		super();
		this.role = role;
		this.jsonToken = jsonToken;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getJsonToken() {
		return jsonToken;
	}

	public void setJsonToken(String jsonToken) {
		this.jsonToken = jsonToken;
	}
}
