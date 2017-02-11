package controllers;

import dao.BookDao;
import dao.UserDao;
import model.Review;
import model.User;
import model.Book;
import model.UserInfo;
import tags.BookReviews;
import tags.BooksListMini;

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
@WebServlet(urlPatterns = {"/books"})
public class Books extends HttpServlet {

    private BookDao bookDao;
    private UserDao userDao;

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        bookDao = (BookDao) servletContext.getAttribute("bookDao");
        userDao = (UserDao) servletContext.getAttribute("userDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        req.setAttribute("userId", user.getUserId());
        UserInfo userInfo = userDao.getUserInfo(user.getUserId());
        Map<String, String[]> parameterMap = req.getParameterMap();
        ArrayList<Book> books = new ArrayList<>();
        if (parameterMap.containsKey("authorId")) {
            books = bookDao.getByAuthorId(Integer.parseInt(parameterMap.get("authorId")[0]));
        } else if (parameterMap.containsKey("genreId")) {
            books = bookDao.getByGenreId(Integer.parseInt(parameterMap.get("genreId")[0]));
        } else if (parameterMap.containsKey("bookId")) {
            Book book = bookDao.getBookById(Integer.parseInt(parameterMap.get("bookId")[0]));
            Collection<Review> reviews = bookDao.getReviewById(Integer.parseInt(parameterMap.get("bookId")[0]));
            Comparator<Review> comparator = (o1, o2) -> o1.getDate().getMillis() > o2.getDate().getMillis() ? 1 : o1.getDate().getMillis() == o2.getDate().getMillis() ? 0 : -1;
            Collections.sort((ArrayList) reviews, comparator);
            BookReviews bookReviews = new BookReviews(reviews);
            req.setAttribute("book", book);
            req.setAttribute("bookReviews", bookReviews);
            req.setCharacterEncoding("UTF-8");
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/books/books_detail.jsp");
            requestDispatcher.forward(req, resp);
        }

        Collections.sort(books, ((o1, o2) -> o1.getBookName().compareTo(o2.getBookName())));
        BooksListMini booksListMini = new BooksListMini(userInfo, books);
        req.setAttribute("bookList", booksListMini);
        req.setCharacterEncoding("UTF-8");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/books/filterValues.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String str = req.getParameter("confirmationText");
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (parameterMap.containsKey("bookId")) {
            int book_id = Integer.parseInt(parameterMap.get("bookId")[0]);
            User user = (User) req.getSession().getAttribute("user");
            bookDao.setBookReview(user.getUserId(), book_id, str);
            Book book = bookDao.getBookById(Integer.parseInt(parameterMap.get("bookId")[0]));
            Collection<Review> reviews = bookDao.getReviewById(Integer.parseInt(parameterMap.get("bookId")[0]));
            BookReviews bookReviews = new BookReviews(reviews);
            req.setAttribute("book", book);
            req.setAttribute("bookReviews", bookReviews);
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("UTF-8");
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/books/books_detail.jsp");
            requestDispatcher.forward(req, resp);
        }
    }
}
