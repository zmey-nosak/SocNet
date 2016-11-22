package filters;


import dao.UserDao;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Echetik on 05.11.2016.
 */
@WebFilter("/userpage/")
public class UserFilter implements HttpFilter {
    UserDao userDao;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getParameterMap().containsKey("user_id")) {
            chain.doFilter(request, response);
        } else if (!request.getParameterMap().containsKey("user_id")) {
            userDao = (UserDao) request.getServletContext().getAttribute("userDao");
            String email = request.getRemoteUser();
            long user_id = userDao.getUserIdByEmail(email);
            request.getSession().setAttribute("user_id", user_id);
            chain.doFilter(request, response);
        }

    }
}
