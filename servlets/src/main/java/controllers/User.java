package controllers;

import dao.UserDao;
import tags.Printable;
import tags.UserPage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by Echetik on 02.11.2016.
 */
@WebServlet(urlPatterns = {"/userpage"})
public class User extends HttpServlet {
    UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Printable printable = new UserPage(Integer.parseInt(req.getParameter("userId")));
        req.setAttribute("printable", printable);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/lists/list3.jsp");
        requestDispatcher.forward(req, resp);
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
