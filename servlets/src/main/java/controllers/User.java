package controllers;

import dao.UserDao;
import model.UserInfo;
import tags.BooksListMini;
import tags.UsersList;
import tags.UsersListMini;

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
            userInfo = userDao.getUserInfo(Integer.parseInt(req.getParameter("user_id")));
        } else {
            userInfo = userDao.getUserInfo((((model.User) req.getSession().getAttribute("user")).getUser_id()));
        }
        req.setAttribute("userInfo", userInfo);
        req.setAttribute("miniFriendList", new UsersListMini(userInfo));
        req.setAttribute("miniBookList", new BooksListMini(userInfo));
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/userpage/index2.jsp");
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
