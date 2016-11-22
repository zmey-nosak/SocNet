package controllers;

import dao.BookDao;
import dao.GenreDao;
import model.Author;
import model.Genre;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Echetik on 30.10.2016.
 */
@WebServlet("/genres/")
public class Genres extends HttpServlet {
    GenreDao genreDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Genre> genres = genreDao.getAll();
        req.setAttribute("genres", genres);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/genres/index.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        genreDao = (GenreDao) servletContext.getAttribute("genreDao");
    }
}
