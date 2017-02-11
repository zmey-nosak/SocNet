package dao;

import model.Author;
import model.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by Echetik on 26.10.2016.
 */

public interface AuthorDao {
    ArrayList<Author> getAll();
    int countAuthorsRecords();
    Author getById(int authorId);
    Response<Author> getAll(int limit, int offset);
}
