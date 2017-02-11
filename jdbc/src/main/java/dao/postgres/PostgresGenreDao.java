package dao.postgres;

import dao.GenreDao;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import model.Genre;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Created by Echetik on 30.10.2016.
 */
@AllArgsConstructor
public class PostgresGenreDao implements GenreDao {
    private Supplier<Connection> connectionSupplier;

    @SneakyThrows
    @Override
    public Collection<Genre> getAll() {
        Collection<Genre> genres = new ArrayList<>();
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
