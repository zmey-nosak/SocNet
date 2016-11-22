package dao;

import model.Author;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Echetik on 26.10.2016.
 */
@FunctionalInterface
public interface AuthorDao {
    Collection<Author> getAll();

    default Optional<Author> getById(long author_id) {
        return getAll().stream().filter(author -> author.getAuthor_id() == author_id).findAny();
    }
}
