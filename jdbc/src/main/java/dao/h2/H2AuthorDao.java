package dao.h2;

import dao.AuthorDao;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import model.Author;
import org.joda.time.LocalDate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Created by Echetik on 26.10.2016.
 */
@Log
public class H2AuthorDao implements AuthorDao {

    private Supplier<Connection> connectionSupplier;

    @java.beans.ConstructorProperties({"connectionSupplier"})
    public H2AuthorDao(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    @SneakyThrows
    @Override
    public Collection<Author> getAll() {
        Collection<Author> authors = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT author_id, f_name, i_name, o_name, dob, country_id FROM AUTHORS")) {
            while (resultSet.next()) {
                authors.add(new Author(
                        resultSet.getInt("author_id"),
                        resultSet.getString("f_name"),
                        resultSet.getString("i_name"),
                        resultSet.getString("o_name"),
                        new LocalDate(resultSet.getDate("dob").getTime()),
                        0));
            }
            return authors;
        }
    }
}
