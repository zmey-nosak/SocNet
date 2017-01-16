package dao.h2;

import common.ConnectionPool;
import dao.BookDao;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import model.Author;
import model.Book;
import model.Review;
import org.joda.time.DateTime;

import java.sql.*;
import java.util.ArrayList;
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
                        resultSet.getInt("book_id"),
                        resultSet.getString("book_name"),
                        resultSet.getInt("pages_count"),
                        resultSet.getInt("author_id"),
                        resultSet.getString("year_name"),
                        resultSet.getString("image_src"),
                        resultSet.getInt("genre_id")));
            }
            return books;
        }
    }

    @Override
    @SneakyThrows
    public Book getById(int book_id) {
        Book book = null;
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT book_id, book_name, pages_count, author_id,year_name,image_src,genre_id FROM BOOKS where book_id=?");
        ) {
            statement.setInt(1, book_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                book = new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("book_name"),
                        resultSet.getInt("pages_count"),
                        resultSet.getInt("author_id"),
                        resultSet.getString("year_name"),
                        resultSet.getString("image_src"),
                        resultSet.getInt("genre_id"));
                return book;
            }
        }
    }

    @Override
    @SneakyThrows
    public Collection<Review> getReviewById(int book_id) {
        Collection<Review> reviews = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT r.review_id, " +
                     "                                                         r.user_id," +
                     "                                                         r.book_id," +
                     "                                                         r.review, " +
                     "                                                         u.f_name," +
                     "                                                         u.i_name," +
                     "                                                         u.photo_src," +
                     "                                                         r.date" +
                     "                                                  FROM reviews r " +
                     "                                                  JOIN users u ON u.user_id=r.user_id" +
                     "                                                  where book_id=?");
        ) {
            statement.setInt(1, book_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Review review = new Review();
                    review.setBook_id(resultSet.getInt("book_id"));
                    review.setUser_id(resultSet.getInt("user_id"));
                    review.setReview(resultSet.getString("review"));
                    review.setF_name(resultSet.getString("f_name"));
                    review.setI_name(resultSet.getString("i_name"));
                    review.setPhoto_src(resultSet.getString("photo_src"));
                    review.setDate(new DateTime(resultSet.getTimestamp("date").getTime()));
                    reviews.add(review);
                }
                return reviews;
            }
        }
    }

    @Override
    public void setReview(int user_id, int book_id, String review) {
        Collection<Review> reviews = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO reviews(user_id,book_id,review,date) VALUES (?,?,?,?)");
        ) {
            statement.setInt(1, user_id);
            statement.setInt(2, book_id);
            statement.setString(3, review);
            statement.setTimestamp(4, new Timestamp(new DateTime().getMillis()));
            statement.execute();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}

