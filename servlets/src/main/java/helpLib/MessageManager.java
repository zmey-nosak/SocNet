package helpLib;

import java.util.ResourceBundle;

/**
 * Created by Echetik on 24.01.2017.
 */
public class MessageManager {
    private static MessageManager instance;
    private ResourceBundle resourceBundle;
    //класс извлекает информацию из файла TextContent.properties
    private static final String BUNDLE_NAME = "TextContent";
    public static final String LOGIN_ERROR_MESSAGE = "LOGIN_ERROR_MESSAGE";
    public static final String SERVLET_EXCEPTION_ERROR_MESSAGE = "SERVLET_EXCEPTION_ERROR_MESSAGE";
    public static final String IO_EXCEPTION_ERROR_MESSAGE = "IO_EXCEPTION_ERROR_MESSAGE";

    public static MessageManager getInstance() {
        if (instance == null) {
            instance = new MessageManager();
            instance.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return instance;
    }

    public String getProperty(String key) {
        return (String) resourceBundle.getObject(key);
    }
}