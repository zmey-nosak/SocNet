package controllers;

import dao.UserDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Echetik on 12.11.2016.
 */
@WebServlet("/userlist/")
public class UserList extends HttpServlet {
    UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("users", userDao.getAllUsers());
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/lists/list.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        userDao = (UserDao) config.getServletContext().getAttribute("userDao");
    }
}
