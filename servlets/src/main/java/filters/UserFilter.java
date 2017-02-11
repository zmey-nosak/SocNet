package filters;


import helpLib.ConfigurationManager;
import dao.UserDao;
import model.User;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Echetik on 05.11.2016.
 */
//Фильтр на сервлеты для первоначальной иницииализации аттрибутов сессии
@WebFilter(urlPatterns = {"/index.jsp", "/books", "/authors/", "/genres/"})
public class UserFilter implements HttpFilter {
    UserDao userDao;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        User user = (User) request.getSession().getAttribute("user");
       /* if (user == null) {
            userDao = (UserDao) request.getServletContext().getAttribute("userDao");
            String email = request.getRemoteUser();
            user = userDao.getUserIdByEmail(email);
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("userId", user.getUserId());
            if (request.getSession().getAttribute("locale") == null) {
                request.getSession().setAttribute("locale", "rus");
            }
        }*/
        if (user != null) {
            chain.doFilter(request, response);
        } else {
            String page= ConfigurationManager.getInstance().getProperty(ConfigurationManager.LOGIN_PAGE_PATH);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(page);
            requestDispatcher.forward(request, response);
        }

    }
}
