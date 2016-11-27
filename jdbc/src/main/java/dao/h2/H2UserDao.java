package dao.h2;

import ch.qos.logback.core.db.dialect.PostgreSQLDialect;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import model.*;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.Date;
import java.util.function.Supplier;

/**
 * Created by Echetik on 04.11.2016.
 */
@AllArgsConstructor
public class H2UserDao implements UserDao {
    private Supplier<Connection> connectionSupplier;

    @SneakyThrows
    public long getUserIdByEmail(String email) {
        long user_id = 0;

        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM USERS where email=?")) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    user_id = resultSet.getLong("user_id");
                }
            }
        }
        return user_id;
    }

    @Override
    @SneakyThrows
    public long registerUser(User user) {
        File file = new File("C:\\Users\\Echetik\\finalWebProject\\SocNet\\servlets\\src\\main\\webapp\\images\\rod.gif");
        long new_user_id;
        try (Connection connection = connectionSupplier.get();
             CallableStatement statement = connection.prepareCall("{ ? = CALL f_register_user(?,?,?,?,?,?)}");
             FileInputStream fis = new FileInputStream(file)) {
            statement.registerOutParameter(1, Types.INTEGER);
            statement.setString(2, user.getF_name());
            statement.setString(3, user.getI_name());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.setDate(6, java.sql.Date.valueOf(user.getDob()));
            statement.setBinaryStream(7, fis, (int) file.length());
            statement.execute();
            new_user_id = statement.getInt(1);

        }
        return new_user_id;
    }

    @Override
    @SneakyThrows
    public boolean emailExists(String email) {
        boolean res = false;
        try (Connection connection = connectionSupplier.get();
             CallableStatement statement = connection.prepareCall("{ ? = CALL f_is_exist_email(?)}")) {
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setString(2, email);
            statement.execute();
            boolean b = statement.getBoolean(1);
            return b;
        }
    }

    @Override
    @SneakyThrows
    public Collection<User> getAllUsers() {
        Collection<User> users = new HashSet<>();
        try (Connection connection = connectionSupplier.get();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT user_id,f_name,i_name,photo FROM USERS")) {
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("user_id"),
                        null,
                        null,
                        rs.getString("f_name"),
                        rs.getString("i_name"),
                        null,
                        null,
                        Base64.encode(rs.getBytes("photo"))));
            }
        }
        return users;
    }

    @Override
    @SneakyThrows
    public User getUser(long id) {
        User user = null;
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM USERS where email=?")) {
            statement.setInt(1, (int) id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    user = new User(
                            rs.getLong("user_id"),
                            null,
                            null,
                            rs.getString("f_name"),
                            rs.getString("i_name"),
                            null,
                            null,
                            Base64.encode(rs.getBytes("photo")));
                }
            }
            return user;
        }
    }

    @Override
    @SneakyThrows
    public Collection<User> getFriends(long id) {
        Collection<User> friends = new HashSet<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT user_id,f_name,i_name,photo FROM FRIENDS where p_user_id=?")) {
            statement.setInt(1, (int) id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    friends.add(new User(
                            rs.getLong("user_id"),
                            null,
                            null,
                            rs.getString("f_name"),
                            rs.getString("i_name"),
                            null,
                            null,
                            Base64.encode(rs.getBytes("photo"))));
                }
            }
            return friends;
        }
    }

    @SneakyThrows
    public UserInfo getUserInfo(long id) {

        Collection<User> friends = new HashSet<>();
        Collection<Book> books = new HashSet<>();
        UserInfo userInfo = new UserInfo();
        try (Connection con = connectionSupplier.get();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM get_user_info(?)")) {
            // для PostgreSQL сначала нужно создать транзакцию (AutoCommit == false)...
            con.setAutoCommit(false);
            statement.setInt(1, (int) id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            friends.add(new User(
                                    rs1.getLong("user_id"),
                                    null,
                                    null,
                                    rs1.getString("f_name"),
                                    rs1.getString("i_name"),
                                    null,
                                    null,
                                    Base64.encode(rs1.getBytes("photo"))));
                        }
                    }
                    userInfo.setUser_friends(friends);
                }
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            books.add(new Book(
                                    rs1.getLong("book_id"),
                                    rs1.getString("book_name"),
                                    0,
                                    0,
                                    null,
                                    rs1.getString("image_src"),
                                    0));
                        }
                    }
                    userInfo.setUser_books(books);
                }
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            userInfo.setId(rs1.getLong("user_id"));
                            userInfo.setF_name(rs1.getString("f_name"));
                            userInfo.setI_name(rs1.getString("i_name"));
                            userInfo.setDob(rs1.getDate("dob").toLocalDate());
                            userInfo.setPhoto(Base64.encode(rs1.getBytes("photo")));
                        }
                    }
                }
            }
            con.setAutoCommit(true);
        }
        return userInfo;
    }

    @Override
    @SneakyThrows
    public boolean sendMessage(int user_id_from, int user_id_to, String message) {
        boolean res = false;
        try (Connection connection = connectionSupplier.get();
             CallableStatement statement = connection.prepareCall("{ ? = CALL put_message(?,?,?)}");
        ) {
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setInt(2, user_id_from);
            statement.setInt(3, user_id_to);
            statement.setString(4, message);
            statement.execute();
            res = statement.getBoolean(1);
        }
        return res;
    }


    @SneakyThrows
    public Collection<UserCommunications> getUserCommunications(int user_id) {
        Collection<UserCommunications> communicationses = new HashSet<>();
        try (Connection con = connectionSupplier.get();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM get_communications(?)")) {
            // для PostgreSQL сначала нужно создать транзакцию (AutoCommit == false)...
            con.setAutoCommit(false);
            statement.setInt(1, (int) user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            UserCommunications communications = new UserCommunications();
                            communications.setUser_from(rs1.getInt("user_from"));
                            communications.setF_name(rs1.getString("f_name"));
                            communications.setI_name(rs1.getString("i_name"));
                            communications.setMessage(rs1.getString("message"));
                            communications.setActive(rs1.getInt("active"));
                            communications.setCommunication_id(rs1.getInt("communication_id"));
                            communications.setDate(rs1.getDate("date"));
                            communications.setPhoto(rs1.getString("photo"));
                            communicationses.add(communications);
                        }
                    }
                    con.setAutoCommit(true);
                }
            }
        }
        return communicationses;
    }
}