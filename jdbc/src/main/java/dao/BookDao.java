package dao;

/**
 * Created by Echetik on 26.10.2016.
 */

import model.Book;
import model.Review;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public interface BookDao {
    Collection<Book> getAll();


    Book getBookById(int bookId);

    void setBookReview(int userId, int bookId, String review);

    ArrayList<Review> getReviewById(int bookId);

    ArrayList<Book> getByAuthorId(int authorId);

    ArrayList<Book> getByGenreId(int genreId);
}
