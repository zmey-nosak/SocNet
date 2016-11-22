package dao.h2;

import common.ConnectionPool;
import dao.BookDao;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import model.Author;
import model.Book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;


@AllArgsConstructor
public class H2BookDao implements BookDao {
    private Supplier<Connection> connectionSupplier;

    @SneakyThrows
    @Override
    public Collection<Book> getAll() {
        Collection<Book> books = new HashSet<>();
        try (Connection connection = connectionSupplier.get();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT book_id, book_name, pages_count, author_id,year_name,image_src,genre_id FROM BOOKS")) {
            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getLong("book_id"),
                        resultSet.getString("book_name"),
                        resultSet.getInt("pages_count"),
                        resultSet.getLong("author_id"),
                        resultSet.getString("year_name"),
                        resultSet.getString("image_src"),
                        resultSet.getLong("genre_id")));
            }
            return books;
        }
    }
}
