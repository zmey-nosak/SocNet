package listeners;

import common.ConnectionPool;
import dao.AuthorDao;
import dao.BookDao;
import dao.GenreDao;
import dao.UserDao;
import dao.postgres.PostgresAuthorDao;
import dao.postgres.PostgresBookDao;
import dao.postgres.PostgresGenreDao;
import dao.postgres.PostgresUserDao;
import lombok.SneakyThrows;
import util.StringEncryptUtil;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Supplier;

/**
 * Created by Echetik on 24.10.2016.
 */
@WebListener
public class Initializer implements ServletContextListener {

    public final static String AUTHOR_DAO = "authorDao";
    public final static String BOOK_DAO = "bookDao";
    public final static String GENRE_DAO = "genreDao";
    public final static String USER_DAO = "userDao";

    @Override

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        Supplier<Connection> connectionPool;

        try {
            //инициализация пула соединений с БД
            connectionPool = getConnectionSupplier();

            AuthorDao authorDao = new PostgresAuthorDao(connectionPool);
            BookDao bookDao = new PostgresBookDao(connectionPool);
            GenreDao genreDao = new PostgresGenreDao(connectionPool);
            UserDao userDao = new PostgresUserDao(connectionPool);
            servletContext.setAttribute("authorDao", authorDao);
            servletContext.setAttribute("bookDao", bookDao);
            servletContext.setAttribute("genreDao", genreDao);
            servletContext.setAttribute("userDao", userDao);
           // initDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Supplier<Connection> getConnectionSupplier() throws NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource dataSource = (DataSource) envContext.lookup("jdbc/myDb");
        return () -> {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };
    }

    @SneakyThrows
    private void initDB() {
        Connection connection = getConnectionSupplier().get();
        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT password,email FROM USERS");
             Statement statement = connection.createStatement()) {
            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String pass = resultSet.getString("password");
                statement.addBatch("UPDATE USERS SET password='" + StringEncryptUtil.encrypt(pass) + "' WHERE email=" + "'" + email + "'");
            }
            statement.executeBatch();
        }

    }

}
