package dao;

/**
 * Created by Echetik on 26.10.2016.
 */

import model.Book;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@FunctionalInterface
public interface BookDao {
    Collection<Book> getAll();

    default Optional<Book> getById(long book_id) {
        return getAll().stream().filter(book -> book.getBook_id() == book_id).findAny();
    }

    default Collection<Book> getByAuthorId(long author_id) {
        return getAll().stream().filter(book -> book.getAuthor_id() == author_id).collect(Collectors.toList());
    }

    default Collection<Book> getByGenreId(long genre_id) {
        return getAll().stream().filter(book -> book.getGenre_id() == genre_id).collect(Collectors.toList());
    }
}
