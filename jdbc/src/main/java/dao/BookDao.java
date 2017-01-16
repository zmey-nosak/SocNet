package dao;

/**
 * Created by Echetik on 26.10.2016.
 */

import model.Book;
import model.Review;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public interface BookDao {
    Collection<Book> getAll();

    /*default Optional<Book> getById(long book_id) {
        return getAll().stream().filter(book -> book.getBook_id() == book_id).findAny();
    }*/
    Book getById(int book_id);

    void setReview(int user_id, int book_id, String review);

    Collection<Review> getReviewById(int book_id);

    default Collection<Book> getByAuthorId(long author_id) {
        return getAll().stream().filter(book -> book.getAuthor_id() == author_id).collect(Collectors.toList());
    }

    default Collection<Book> getByGenreId(long genre_id) {
        return getAll().stream().filter(book -> book.getGenre_id() == genre_id).collect(Collectors.toList());
    }
}
