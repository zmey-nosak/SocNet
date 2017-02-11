package controllers;

import dao.UserDao;
import tags.*;
import tags.FriendRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Echetik on 02.11.2016.
 */
@WebServlet(urlPatterns = {"/friends", "/users", "/friends/requests"})
public class Friends extends HttpServlet {
    UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int user_id = Integer.parseInt(req.getParameter("userId"));
        model.User user = (model.User) req.getSession().getAttribute("user");
        if (user.getUserId() == user_id) {
            Printable printable = null;
            if (req.getRequestURI().contains("/friends/requests")) {
                printable = new FriendRequest(user_id);
            }else if(req.getRequestURI().contains("/friends")){
                printable = new FriendList(user_id);
            }else if(req.getRequestURI().contains("/users")){
                printable = new UsersList(user_id);
            }

            req.setAttribute("printable", printable);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/userpage/userpage.jsp");
            requestDispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        userDao = (UserDao) servletContext.getAttribute("userDao");
    }


}
