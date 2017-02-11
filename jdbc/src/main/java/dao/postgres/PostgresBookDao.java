package dao.postgres;

import dao.BookDao;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import model.Book;
import model.Review;
import org.joda.time.DateTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;


@AllArgsConstructor
public class PostgresBookDao implements BookDao {
    private Supplier<Connection> connectionSupplier;

    @SneakyThrows
    @Override
    public ArrayList<Book> getAll() {
        ArrayList<Book> books = new ArrayList<>();
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

    @SneakyThrows
    @Override
    public ArrayList<Book> getByAuthorId(int authorId) {
        ArrayList<Book> books = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT book_id, book_name, pages_count, author_id,year_name,image_src,genre_id FROM BOOKS WHERE author_id=?")) {
            statement.setInt(1, authorId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                            rs.getInt("book_id"),
                            rs.getString("book_name"),
                            rs.getInt("pages_count"),
                            rs.getInt("author_id"),
                            rs.getString("year_name"),
                            rs.getString("image_src"),
                            rs.getInt("genre_id")));
                }
                return books;
            }
        }
    }


    @Override
    @SneakyThrows
    public ArrayList<Book> getByGenreId(int genreId) {
        ArrayList<Book> books = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT book_id, book_name, pages_count, author_id,year_name,image_src,genre_id" +
                                                                        " FROM BOOKS" +
                                                                        "  WHERE genre_id=?")) {
            statement.setInt(1, genreId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                            rs.getInt("book_id"),
                            rs.getString("book_name"),
                            rs.getInt("pages_count"),
                            rs.getInt("author_id"),
                            rs.getString("year_name"),
                            rs.getString("image_src"),
                            rs.getInt("genre_id")));
                }
                return books;
            }
        }
    }

    @Override
    @SneakyThrows
    public Book getBookById(int bookId) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT book_id, book_name, pages_count, author_id,year_name,image_src,genre_id FROM BOOKS WHERE book_id=?")) {
            statement.setInt(1, bookId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    return new Book(
                            rs.getInt("book_id"),
                            rs.getString("book_name"),
                            rs.getInt("pages_count"),
                            rs.getInt("author_id"),
                            rs.getString("year_name"),
                            rs.getString("image_src"),
                            rs.getInt("genre_id"));
                }
            }
        }
        return null;
    }

    @Override
    @SneakyThrows
    public ArrayList<Review> getReviewById(int bookId) {
        ArrayList<Review> reviews = new ArrayList<>();
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
            statement.setInt(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Review review = new Review();
                    review.setBookId(resultSet.getInt("book_id"));
                    review.setUserId(resultSet.getInt("user_id"));
                    review.setReview(resultSet.getString("review"));
                    review.setSurname(resultSet.getString("f_name"));
                    review.setName(resultSet.getString("i_name"));
                    review.setPhotoSrc(resultSet.getString("photo_src"));
                    review.setDate(new DateTime(resultSet.getTimestamp("date").getTime()));
                    reviews.add(review);
                }
                return reviews;
            }
        }
    }

    @Override
    public void setBookReview(int userId, int bookId, String review) {
        ArrayList<Review> reviews = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO reviews(user_id,book_id,review,date) VALUES (?,?,?,?)");
        ) {
            statement.setInt(1, userId);
            statement.setInt(2, bookId);
            statement.setString(3, review);
            statement.setTimestamp(4, new Timestamp(new DateTime().getMillis()));
            statement.execute();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}

