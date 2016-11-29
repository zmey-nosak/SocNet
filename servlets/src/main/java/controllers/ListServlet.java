package controllers;

import dao.UserDao;
import tags.CommunicationList;
import tags.Printable;
import tags.UsersList;

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
//@WebServlet(value = {"/list", "/list/friends", "list/messages"})
@WebServlet("/list/*")
public class ListServlet extends HttpServlet {
    UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long user_id;
        if (req.getSession().getAttribute("user_id") != null) {
            user_id = (long) req.getSession().getAttribute("user_id");
            if (req.getRequestURI().contains("/list/friends")) {
                req.setAttribute("list", new UsersList(userDao.getAllUsers()));
            } else if (req.getRequestURI().contains("/list/messages")) {
                req.setAttribute("list", new CommunicationList(userDao.getUserCommunications((int) user_id)));
            }
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/lists/list.jsp");
            requestDispatcher.forward(req, resp);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        userDao = (UserDao) config.getServletContext().getAttribute("userDao");
    }
}