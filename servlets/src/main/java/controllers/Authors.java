package controllers;

import dao.AuthorDao;
import model.Author;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Echetik on 26.10.2016.
 */
@WebServlet("/authors/")
public class Authors extends HttpServlet {
    private AuthorDao authorDao;

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        authorDao = (AuthorDao) servletContext.getAttribute("authorDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      /*  ArrayList<Author> authors = authorDao.getAll();
        Collections.sort(authors, (o1, o2) -> {
            return o1.getSurname().compareTo(o2.getSurname());
        });
        req.setAttribute("authors", authors);*/
        int offset = 0;
        if (req.getParameterMap().containsKey("offset")) {
            offset = Integer.parseInt(req.getParameter("offset"));
        }
        req.setAttribute("offset", offset);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/authors/authorsList.jsp");
        requestDispatcher.forward(req, resp);
    }
}
