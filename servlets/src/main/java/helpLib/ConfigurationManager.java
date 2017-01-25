package helpLib;

import java.util.ResourceBundle;

/**
 * Created by Echetik on 24.01.2017.
 */
public class ConfigurationManager {
    private static ConfigurationManager instance;
    private ResourceBundle resourceBundle;
    private static final String BUNDLE_NAME = "config";
    public static final String ERROR_PAGE_PATH = "ERROR_PAGE_PATH";
    public static final String REGISTRATION_PAGE_PATH = "REGISTRATION_PAGE_PATH";
    public static final String LOGIN_PAGE_PATH = "LOGIN_PAGE_PATH";
    public static final String MAIN_PAGE_PATH = "MAIN_PAGE_PATH";

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
            instance.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return instance;
    }

    public String getProperty(String key) {
        return (String) resourceBundle.getObject(key);
    }
}
