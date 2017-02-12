package dao.postgres;

import dao.AuthorDao;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import model.Author;
import model.Response;
import org.joda.time.LocalDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Created by Echetik on 26.10.2016.
 */
@Log
public class PostgresAuthorDao implements AuthorDao {

    private Supplier<Connection> connectionSupplier;


    @java.beans.ConstructorProperties({"connectionSupplier"})
    public PostgresAuthorDao(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    @SneakyThrows
    public ArrayList<Author> getAll() {
        ArrayList<Author> authors = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT author_id, f_name, i_name, dob, country_id FROM AUTHORS")) {
            while (resultSet.next()) {
                authors.add(new Author(
                        resultSet.getInt("author_id"),
                        resultSet.getString("f_name"),
                        resultSet.getString("i_name"),
                        new LocalDate(resultSet.getDate("dob").getTime()),
                        0));
            }
            return authors;
        }
    }

    @SneakyThrows
    @Override
    public Author getById(int authorId) {
        ArrayList<Author> authors = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT author_id, f_name, i_name, dob, country_id FROM AUTHORS where author_id=?")) {
            statement.setInt(1, authorId);
            try (
                    ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    return new Author(
                            rs.getInt("author_id"),
                            rs.getString("f_name"),
                            rs.getString("i_name"),
                            new LocalDate(rs.getDate("dob").getTime()),
                            0);
                }
            }
        }
        return null;
    }

    @SneakyThrows
    @Override
    public Response<Author> getAll(int limit, int offset) {
        ArrayList<Author> authors = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT  t.author_id, t.f_name, t.i_name, t.dob, t.country_id, t.total" +
                             " FROM (SELECT author_id, f_name, i_name, dob, country_id, count(1) over() as total" +
                             " FROM AUTHORS ORDER BY f_name) as t" +
                             " limit ? offset ?")) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            int total = 0;
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    total = resultSet.getInt("total");
                    authors.add(new Author(
                            resultSet.getInt("author_id"),
                            resultSet.getString("f_name"),
                            resultSet.getString("i_name"),
                            new LocalDate(resultSet.getDate("dob").getTime()),
                            0));
                }
                Response<Author> response = new Response<>();
                response.setLimit(limit);
                response.setOffset(offset);
                response.setTotal(total);
                response.setObjects(authors);
                return response;

            }
        }
    }
}
