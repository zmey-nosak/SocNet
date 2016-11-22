package dao;

import model.Book;

import model.Genre;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Echetik on 30.10.2016.
 */
public interface GenreDao {
    Collection<Genre> getAll();

}
