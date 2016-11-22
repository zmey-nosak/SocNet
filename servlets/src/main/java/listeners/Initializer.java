package listeners;

import dao.AuthorDao;
import dao.BookDao;
import dao.GenreDao;
import dao.UserDao;
import dao.h2.H2AuthorDao;
import dao.h2.H2BookDao;
import dao.h2.H2GenreDao;
import dao.h2.H2UserDao;
import lombok.SneakyThrows;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * Created by Echetik on 24.10.2016.
 */
@WebListener
public class Initializer implements ServletContextListener {

    public final static String AUTHOR_DAO="authorDao";
    public final static String BOOK_DAO="bookDao";
    public final static String GENRE_DAO="genreDao";
    public final static String USER_DAO="userDao";
    @Override
    @SneakyThrows
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
       // String pathToDbConfig = servletContext.getRealPath("/") + "WEB-INF/classes/";
        Supplier<Connection> connectionPool;

        //= ConnectionPool.create(pathToDbConfig + "db.properties", pathToDbConfig + "schema.sql");
        connectionPool = getConnectionSupplier();


        AuthorDao authorDao = new H2AuthorDao(connectionPool);
        BookDao bookDao = new H2BookDao(connectionPool);
        GenreDao genreDao = new H2GenreDao(connectionPool);
        UserDao userDao=new H2UserDao(connectionPool);
        servletContext.setAttribute("authorDao", authorDao);
        servletContext.setAttribute("bookDao", bookDao);
        servletContext.setAttribute("genreDao", genreDao);
        servletContext.setAttribute("userDao", userDao);
    }

    private Supplier<Connection> getConnectionSupplier() throws NamingException {
        Context initContext=new InitialContext();
        Context envContext=(Context)initContext.lookup("java:/comp/env");
        DataSource dataSource=(DataSource)envContext.lookup("jdbc/myDb");

        return () -> {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };
    }

}
