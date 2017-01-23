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
        Collection<Author> authors = authorDao.getAll();
        Collections.sort((ArrayList)authors, ((o1, o2) -> {
            Author a1=(Author)o1;
            Author a2=(Author)o2;
            return a1.getF_name().compareTo(a2.getF_name())>0?1:a1.getF_name().equals(a2.getF_name())?0:-1;
        }));
        req.setAttribute("authors", authors);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/authors/authorsList.jsp");
        requestDispatcher.forward(req, resp);
    }
}
