package home.project.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import home.project.beans.UserBean;
import home.project.container.PasswordContainer;
import home.project.dao.UserDAO;
import home.project.dto.User;
import home.project.logger.ClientLogger;

public class UserService {
	
	private static final String UNKNOWN_PICTURE_PATH = "photos/unknown.png";
	
	public static boolean deactivateAccount(String userName) {
		return UserDAO.deactivateAccount(userName);
	}
	
	public static boolean createUser(User user) {
		return UserDAO.insertUser(user);
	}
	
	public static UserBean login(String userName, String password) {
		UserBean userBean = UserDAO.loginUser(userName, password);
		if(userBean != null && userBean.getUser().getPictureAsStringBase64() == null) {
			byte[] picture = convertImageToBlob(UNKNOWN_PICTURE_PATH);
			if(picture != null)
				userBean.getUser().setPictureAsStringBase64(Base64.getEncoder().encodeToString(picture));
		}
		return userBean;
	}
	
	public static boolean changePassword(String userName, PasswordContainer container) {
		return UserDAO.changePassword(userName, container);
	}
	
	public static byte[] convertImageToBlob(String imagePath) {
        File file = new File(imagePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] imageBytes = new byte[(int) file.length()];
            fis.read(imageBytes);
            return imageBytes;
        } catch(IOException e) {
        	ClientLogger.logError("IOException during photo handling: " + e.getMessage());
        	return null;
        }
 	}
}
