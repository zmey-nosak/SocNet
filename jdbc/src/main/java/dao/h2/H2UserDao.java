package dao.h2;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import model.*;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.*;
import java.util.function.Supplier;

/**
 * Created by Echetik on 04.11.2016.
 */
@AllArgsConstructor
public class H2UserDao implements UserDao {
    private Supplier<Connection> connectionSupplier;

    @SneakyThrows
    public User getUserIdByEmail(String email) {
        User user = new User();

        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT user_id,f_name,i_name, photo FROM USERS where email=?")) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    user.setUser_id(resultSet.getInt("user_id"));
                    user.setF_name(resultSet.getString("f_name"));
                    user.setI_name(resultSet.getString("i_name"));
                    user.setPhoto(resultSet.getString("photo"));
                }
            }
        }
        return user;
    }

    @Override
    @SneakyThrows
    public int registerUser(User user) {
        File file = new File("C:\\Users\\Echetik\\finalWebProject\\SocNet\\servlets\\src\\main\\webapp\\images\\rod.gif");
        int new_user_id;
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
                        rs.getInt("user_id"),
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
    public User getUser(int id) {
        User user = null;
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT user_id, f_name,i_name,photo FROM USERS where user_id=?")) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    user = new User(
                            rs.getInt("user_id"),
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
    public Collection<User> getFriends(int id) {
        Collection<User> friends = new HashSet<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT user_id,f_name,i_name,photo FROM FRIENDS where p_user_id=?")) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    friends.add(new User(
                            rs.getInt("user_id"),
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
    public UserInfo getUserInfo(int id) {

        Collection<User> friends = new HashSet<>();
        Collection<Book> books = new HashSet<>();
        UserInfo userInfo = new UserInfo();
        try (Connection con = connectionSupplier.get();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM get_user_info(?)")) {
            // для PostgreSQL сначала нужно создать транзакцию (AutoCommit == false)...
            con.setAutoCommit(false);
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            friends.add(new User(
                                    rs1.getInt("user_id"),
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
                                    rs1.getInt("book_id"),
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
                            userInfo.setId(rs1.getInt("user_id"));
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
    public boolean sendMessage(int user_id_from, int user_id_to, String message, DateTime dateTime) {
        boolean res = false;
        try (Connection connection = connectionSupplier.get();
             CallableStatement statement = connection.prepareCall("{ ? = CALL put_message(?,?,?,?)}");
        ) {
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setInt(2, user_id_from);
            statement.setInt(3, user_id_to);
            statement.setString(4, message);
            statement.setTimestamp(5, new Timestamp(dateTime.getMillis()));
            statement.execute();
            res = statement.getBoolean(1);
        }
        return res;
    }


    @SneakyThrows
    public Collection<UserCommunication> getUserCommunications(int user_id) {

        Collection<UserCommunication> communications = new HashSet<>();
        try (Connection con = connectionSupplier.get();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM get_communications(?)")) {
            // для PostgreSQL сначала нужно создать транзакцию (AutoCommit == false)...
            con.setAutoCommit(false);
            statement.setInt(1, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            UserCommunication communication = new UserCommunication();
                            communication.setUser_from(rs1.getInt("user_from"));
                            communication.setF_name(rs1.getString("f_name"));
                            communication.setI_name(rs1.getString("i_name"));
                            communication.setMessage(rs1.getString("message"));
                            communication.setActive(rs1.getInt("active"));
                            communication.setCommunication_id(rs1.getInt("communication_id"));
                            communication.setDate(new DateTime(rs1.getTimestamp("date").getTime()));
                            communication.setPhoto(Base64.encode(rs1.getBytes("photo")));
                            communication.setOwnerPhoto(Base64.encode(rs1.getBytes("photo")));
                            communication.setPartner(rs1.getInt("partner"));
                            communications.add(communication);
                        }
                    }
                    con.setAutoCommit(true);
                }
            }
        }
        return communications;
    }

    @Override
    @SneakyThrows
    public Collection<UserMessage> getUserMessages(int user_id, int communication_id) {
        Collection<UserMessage> messages = new ArrayList<>();
        try (Connection con = connectionSupplier.get();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM get_messages(?,?)")) {
            // для PostgreSQL сначала нужно создать транзакцию (AutoCommit == false)...
            con.setAutoCommit(false);
            statement.setInt(1, user_id);
            statement.setInt(2, communication_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            UserMessage message = new UserMessage();
                            message.setUser_id(rs1.getInt("user_id"));
                            //  message.setF_name(rs1.getString("f_name"));
                            //  message.setI_name(rs1.getString("i_name"));
                            message.setMessage(rs1.getString("message"));
                            message.setActive(rs1.getInt("active"));
                            message.setDate(new DateTime(rs1.getTimestamp("date").getTime()));
                            //   message.setPhoto(Base64.encode(rs1.getBytes("photo")));
                            message.setGroupNum(rs1.getInt("group_num"));
                            messages.add(message);
                        }
                    }
                    con.setAutoCommit(true);
                }
            }
        }
        return messages;
    }

    @Override
    @SneakyThrows
    public User getPartnerByCommunication(int user_id, int communication_id) {
        User user = new User();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT u.user_id, u.f_name, u.i_name, u.photo " +
                     "                                                  FROM user_communications uc" +
                     "                                                  JOIN USERS u ON u.user_id=uc.user_id" +
                     "                                                   where uc.user_id!=? and communication_id=?")) {
            statement.setInt(1, user_id);
            statement.setInt(2, communication_id);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    user.setF_name(resultSet.getString("f_name"));
                    user.setI_name(resultSet.getString("i_name"));
                    user.setUser_id(resultSet.getInt("user_id"));
                    user.setPhoto(resultSet.getString("photo"));
                }
            }
        }
        return user;
    }

    @Override
    public Collection<UserMessage> getLastMessage(int user_id, DateTime dateTime, int communication_id) {
        return null;
    }
/*
    @Override
    @SneakyThrows
    public Collection<UserMessage> getLastMessage(int user_id, DateTime dateTime, int communication_id) {
        Collection<UserMessage> messages = new ArrayList<>();
        try (Connection con = connectionSupplier.get();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM get_last_messages(?,?,?)")) {
            // для PostgreSQL сначала нужно создать транзакцию (AutoCommit == false)...
            con.setAutoCommit(false);
            statement.setInt(1, (int) user_id);
            statement.setInt(2, (int) communication_id);
            statement.setTimestamp(3, new Timestamp(dateTime.getMillis()));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            UserMessage message = new UserMessage();
                            message.setUser_id(rs1.getInt("user_id"));
                            message.setF_name(rs1.getString("f_name"));
                            message.setI_name(rs1.getString("i_name"));
                            message.setMessage(rs1.getString("message"));
                            message.setActive(rs1.getInt("active"));
                            message.setDate(new DateTime(rs1.getTimestamp("date").getTime()));
                            message.setPhoto(Base64.encode(rs1.getBytes("photo")));
                            message.setGroupNum(rs1.getInt("group_num"));
                            messages.add(message);
                        }
                    }
                    con.setAutoCommit(true);
                }
            }
        }
        return messages;
    }*/
}