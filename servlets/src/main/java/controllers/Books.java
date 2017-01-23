package controllers;

import dao.BookDao;
import dao.UserDao;
import model.Review;
import model.User;
import model.Book;
import model.UserInfo;
import tags.BookReviews;
import tags.BooksListMini;
import tags.MenuTag;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
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
        req.setAttribute("userId", user.getUser_id());
        UserInfo userInfo = userDao.getUserInfo(user.getUser_id());
        Map<String, String[]> parameterMap = req.getParameterMap();
        Collection<Book> books = new ArrayList<>();
        if (parameterMap.containsKey("author_id")) {
            books = bookDao.getByAuthorId(Long.parseLong(parameterMap.get("author_id")[0]));
        } else if (parameterMap.containsKey("genre_id")) {
            books = bookDao.getByGenreId(Long.parseLong(parameterMap.get("genre_id")[0]));
        } else if (parameterMap.containsKey("book_id")) {
            Book book = bookDao.getById(Integer.parseInt(parameterMap.get("book_id")[0]));
            Collection<Review> reviews = bookDao.getReviewById(Integer.parseInt(parameterMap.get("book_id")[0]));
            Comparator<Review> comparator = new Comparator<Review>() {
                @Override
                public int compare(Review o1, Review o2) {
                    return o1.getDate().getMillis() > o2.getDate().getMillis() ? 1 : o1.getDate().getMillis() == o2.getDate().getMillis() ? 0 : -1;
                }
            };
            Collections.sort((ArrayList) reviews, comparator);

            BookReviews bookReviews = new BookReviews(reviews);
            req.setAttribute("book", book);
            req.setAttribute("bookReviews", bookReviews);
            req.setCharacterEncoding("UTF-8");
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/books/books_detail.jsp");
            requestDispatcher.forward(req, resp);
        }
        Collections.sort((ArrayList) books, ((o1, o2) -> {
            Book b1 = (Book) o1;
            Book b2 = (Book) o2;
            return b1.getBook_name().compareTo(b2.getBook_name()) > 0 ? 1 : b1.getBook_name().equals(b2.getBook_name()) ? 0 : -1;
        }));

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
        if (parameterMap.containsKey("book_id")) {
            int book_id = Integer.parseInt(parameterMap.get("book_id")[0]);
            User user = (User) req.getSession().getAttribute("user");
            bookDao.setReview(user.getUser_id(), book_id, str);

            Book book = bookDao.getById(Integer.parseInt(parameterMap.get("book_id")[0]));
            Collection<Review> reviews = bookDao.getReviewById(Integer.parseInt(parameterMap.get("book_id")[0]));
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
