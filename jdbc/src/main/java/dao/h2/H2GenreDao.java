package dao.h2;

import common.ConnectionPool;
import dao.GenreDao;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import model.Book;
import model.Genre;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;

/**
 * Created by Echetik on 30.10.2016.
 */
@AllArgsConstructor
public class H2GenreDao implements GenreDao {
    private Supplier<Connection> connectionSupplier;

    @SneakyThrows
    @Override
    public Collection<Genre> getAll() {
        Collection<Genre> genres = new HashSet<>();
        try (Connection connection = connectionSupplier.get();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT genre_id, genre_name FROM genres")) {
            while (resultSet.next()) {
                genres.add(new Genre(
                        resultSet.getInt("genre_id"),
                        resultSet.getString("genre_name")));
            }
            return genres;
        }
    }
}
