package dao.h2;

import common.ConnectionPool;
import dao.AuthorDao;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import model.Author;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;

/**
 * Created by Echetik on 26.10.2016.
 */
@Log
@AllArgsConstructor
public class H2AuthorDao implements AuthorDao {

    private Supplier<Connection> connectionSupplier;
    @SneakyThrows
    @Override
    public Collection<Author> getAll() {
        Collection<Author> authors = new HashSet<>();
        try (Connection connection = connectionSupplier.get();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT author_id, f_name, i_name, o_name, dob, country_id FROM AUTHORS")) {
            while (resultSet.next()) {
                authors.add(new Author(
                        resultSet.getInt("author_id"),
                        resultSet.getString("f_name"),
                        resultSet.getString("i_name"),
                        resultSet.getString("o_name"),
                        resultSet.getDate("dob").toLocalDate(),
                        0));
            }
            return authors;
        }
    }
}
