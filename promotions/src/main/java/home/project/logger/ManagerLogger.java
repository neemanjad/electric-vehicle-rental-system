package home.project.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ManagerLogger {
	private static final Logger logger = Logger.getLogger(ManagerLogger.class.getName());
    private static final String LOGGER_FILE = "C:\\Users\\PC\\Desktop\\Fakultet\\IPFinal\\Project_NemanjaDavidovic_1194_15\\ETFBL_IP_Managers\\manager_logs.log"; 

    static {
        try {
            FileHandler fileHandler = new FileHandler(LOGGER_FILE, true); 
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL); 
        } catch (IOException e) {
            System.err.println("Error initializing ManagerLogger: " + e.getMessage());
        }
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logWarning(String message) {
        logger.warning(message);
    }

    public static void logError(String message) {
        logger.severe(message);
    }
}
