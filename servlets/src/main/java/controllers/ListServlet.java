package controllers;

import dao.UserDao;
import model.*;
import tags.CommunicationList;
import tags.MessageList;
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
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Echetik on 12.11.2016.
 */
//@WebServlet(value = {"/list", "/list/friends", "list/messages"})
@WebServlet("/list/*")
public class ListServlet extends HttpServlet {
    UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        model.User user;
        req.setAttribute("css", "");
        req.setAttribute("js", "");
        req.setAttribute("print_object", "");
        if (req.getSession().getAttribute("user") != null) {
            user = (model.User) req.getSession().getAttribute("user");
            if (req.getRequestURI().contains("/list/friends")) {
                req.setAttribute("list", new UsersList(userDao.getAllUsers()));
                req.setAttribute("css", "<link rel=\"stylesheet\" href=\"/lists.css\">");
            } else if (req.getRequestURI().contains("/list/communications")) {
                req.setAttribute("list", new CommunicationList(userDao.getUserCommunications(user.getUser_id())));
                req.setAttribute("css", CommunicationList.CSS);
            } else if (req.getRequestURI().contains("/list/messages")) {
                int communication_id;
                if (req.getParameterMap().containsKey("communication_id")) {
                    communication_id = Integer.parseInt(req.getParameter("communication_id"));
                    model.User partner = userDao.getPartnerByCommunication(user.getUser_id(), communication_id);
                    req.setAttribute("partner", partner.getUser_id());
                    req.setAttribute("list", new MessageList(userDao.getUserMessages(user.getUser_id(), communication_id),
                            communication_id,
                            user,
                            partner));
                    req.setAttribute("css", MessageList.CSS);
                    req.setAttribute("js", MessageList.JS);
                }
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
