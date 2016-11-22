package controllers;

import dao.AuthorDao;
import dao.BookDao;
import model.Author;
import model.Book;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Echetik on 30.10.2016.
 */
@WebServlet("/books")
public class Books extends HttpServlet {

    private BookDao bookDao;

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        bookDao = (BookDao) servletContext.getAttribute("bookDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        Collection<Book> books = new ArrayList<>();
        if (parameterMap.containsKey("author_id")) {
            books = bookDao.getByAuthorId(Long.parseLong(parameterMap.get("author_id")[0]));
        } else if (parameterMap.containsKey("genre_id")) {
            books = bookDao.getByGenreId(Long.parseLong(parameterMap.get("genre_id")[0]));
        }
        req.setAttribute("books", books);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/books/index.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
