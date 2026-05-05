package home.project.am.securityutil;

public class SecurityUtil {
	private static final int INPUT_LENGTH = 150;
	private static final String REGEX_PATTERN = "^[a-zA-Z0-9_.@\\s]+$";
	
	public static boolean isSafeCredential(String credential) {
	    if (credential == null || credential.isEmpty()) {
	        return false;
	    }

	    if (credential.length() > INPUT_LENGTH) {
	        return false;
	    }

	    return credential.matches(REGEX_PATTERN);
	}
}
