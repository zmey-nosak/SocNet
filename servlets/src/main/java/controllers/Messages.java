package controllers;

import dao.UserDao;
import tags.CommunicationList;
import tags.Printable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Echetik on 02.11.2016.
 */
@WebServlet("/messages")
public class Messages extends HttpServlet {
    UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> map = req.getParameterMap();
        Printable printable = null;
        if (map.containsKey("communicationId")) {
            printable = new CommunicationList(Integer.parseInt(req.getParameter("userId")),
                    Integer.parseInt(req.getParameter("communicationId")),
                    Integer.parseInt(req.getParameter("partnerId"))
            );
        } else {
            printable = new CommunicationList(Integer.parseInt(req.getParameter("userId")));
        }
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
