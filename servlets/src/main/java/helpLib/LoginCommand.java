package helpLib;

import dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Echetik on 24.01.2017.
 */
public class LoginCommand implements Command {
    private static final String PARAM_NAME_LOGIN = "login";
    private static final String PARAM_NAME_PASSWORD = "password";

    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String page = null;
//извлечение из запроса логина и пароля
        String login = request.getParameter(PARAM_NAME_LOGIN);
        String pass = request.getParameter(PARAM_NAME_PASSWORD);
//проверка логина и пароля
        LoginLogic loginLogic = new LoginLogic((UserDao) request.getServletContext().getAttribute("userDao"));
        if (loginLogic.checkLogin(login, pass)) {
            UserDao userDao = (UserDao) request.getServletContext().getAttribute("userDao");
            model.User user = userDao.getUserIdByEmail(login);
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("userId", user.getUserId());
            if (request.getSession().getAttribute("locale") == null) {
                request.getSession().setAttribute("locale", "rus");
            }
//определение пути к main.jsp
            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.MAIN_PAGE_PATH);
        } else {
            request.setAttribute("errorMessage", MessageManager.getInstance().getProperty(MessageManager.LOGIN_ERROR_MESSAGE));
            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        }
        return page;
    }
}
