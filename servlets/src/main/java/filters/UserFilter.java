package filters;


import dao.UserDao;
import model.User;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;

/**
 * Created by Echetik on 05.11.2016.
 */
@WebFilter(urlPatterns = {"/index.jsp", "/books", "/*"})
public class UserFilter implements HttpFilter {
    UserDao userDao;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            userDao = (UserDao) request.getServletContext().getAttribute("userDao");
            String email = request.getRemoteUser();
            user = userDao.getUserIdByEmail(email);
            request.getSession().setAttribute("user", user);
            if (request.getSession().getAttribute("locale") == null) {
                request.getSession().setAttribute("locale", "rus");
            }
        }
        chain.doFilter(request, response);
    }
}
