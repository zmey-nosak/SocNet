package helpLib;

import dao.UserDao;
import model.User;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import util.StringEncryptUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Echetik on 24.01.2017.
 */
public class RegisterCommand implements Command {
    private static final String PARAM_NAME_LOGIN = "login";
    private static final String PARAM_NAME_PASSWORD = "password";
    private static final String PARAM_NAME_CONFIRM_PASSWORD = "confirm_password";
    private static final String PARAM_NAME_F_NAME = "surname";
    private static final String PARAM_NAME_I_NAME = "name";
    private static final String PARAM_NAME_DOB = "dateOfBirth";

    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String page = null;
//извлечение из запроса логина и пароля
        String login = request.getParameter(PARAM_NAME_LOGIN);
        String pass = request.getParameter(PARAM_NAME_PASSWORD);
        String confPSW = request.getParameter(PARAM_NAME_CONFIRM_PASSWORD);
        String fName = request.getParameter(PARAM_NAME_F_NAME);
        String iName = request.getParameter(PARAM_NAME_I_NAME);
        String dob = request.getParameter(PARAM_NAME_DOB);
//проверка логина и пароля

        RegisterLogic registerLogic = new RegisterLogic((UserDao) request.getServletContext().getAttribute("userDao"));
        model.User user = new User();
        user.setEmail(login);
        user.setName(iName);
        user.setSurname(fName);
        String messages = "";
        if (registerLogic.checkLogin(login)) {
            messages += "Login already used<br/>";
        }
        if (!confPSW.equals(pass)) {
            messages += "Password and confirm password are not equal<br/>";
        }
        if (pass.length() < 5) {
            messages += "Password cant be less then 5 symbols";
        }
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("d.MM.yyyy");
            formatter = formatter.withLocale(request.getLocale());  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
            LocalDate date = formatter.parseLocalDate(dob);
            user.setDateOfBirth(date);
        } catch (Exception ex) {
            messages += "Date is not valid.<br\\>";
        }
        if (messages.isEmpty()) {
            try {
                user.setPassword(StringEncryptUtil.encrypt(pass));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            int userId = registerLogic.registerUser(user);
            User registeredUser = registerLogic.getRegisteredUser(userId);
            request.getSession().setAttribute("user", registeredUser);
            request.getSession().setAttribute("userId", registeredUser.getUserId());
            if (request.getSession().getAttribute("locale") == null) {
                request.getSession().setAttribute("locale", "rus");
            }
            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.MAIN_PAGE_PATH);
        } else {
            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.REGISTRATION_PAGE_PATH);
            request.setAttribute("messages", messages);
            request.setAttribute("user", user);
        }
        return page;
    }
}
