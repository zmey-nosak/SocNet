package controllers;

import dao.UserDao;
import model.UserInfo;

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
@WebServlet("/userpage/")
public class User extends HttpServlet {
    UserDao userDao;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserInfo userInfo;
        if (req.getParameterMap().containsKey("user_id")) {
            userInfo = userDao.getUserInfo(Long.parseLong(req.getParameter("user_id")));
        } else {
            userInfo = userDao.getUserInfo((long) (req.getSession().getAttribute("user_id")));
        }
         req.setAttribute("userInfo", userInfo);
         RequestDispatcher requestDispatcher = req.getRequestDispatcher("/userpage/index.jsp");
         requestDispatcher.forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        userDao = (UserDao) servletContext.getAttribute("userDao");
    }
}
