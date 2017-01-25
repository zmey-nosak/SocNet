package dao.h2;

import dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import model.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.function.Supplier;

/**
 * Created by Echetik on 04.11.2016.
 */
@AllArgsConstructor
public class H2UserDao implements UserDao {
    private Supplier<Connection> connectionSupplier;
    private final String STORAGE_PATH = "D:\\user_images\\";

    @SneakyThrows
    public User getUserIdByEmail(String email) {
        User user = new User();

        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT user_id,f_name,i_name,email, dob, photo_src FROM USERS where email=?")) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    user.setUser_id(resultSet.getInt("user_id"));
                    user.setF_name(resultSet.getString("f_name"));
                    user.setI_name(resultSet.getString("i_name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPhoto_src(resultSet.getString("photo_src"));
                    user.setDob(new LocalDate(resultSet.getDate("dob").getTime()));
                }
            }
        }
        return user;
    }

    @Override
    @SneakyThrows
    public int registerUser(User user) {
        int new_user_id;
        try (Connection connection = connectionSupplier.get();
             CallableStatement statement = connection.prepareCall("{ ? = CALL f_register_user(?,?,?,?,?,?)}");
        ) {
            statement.registerOutParameter(1, Types.INTEGER);
            statement.setString(2, user.getF_name());
            statement.setString(3, user.getI_name());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.setDate(6, new java.sql.Date(user.getDob().toDate().getTime()));
            statement.setString(7, "\\common\\avatar.gif");
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
             ResultSet rs = statement.executeQuery("SELECT user_id,f_name,i_name, photo_src FROM USERS")) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        null,
                        null,
                        rs.getString("f_name"),
                        rs.getString("i_name"),
                        null,
                        null,
                        rs.getString("photo_src")
                ));
            }
        }
        return users;
    }

    @Override
    @SneakyThrows
    public User getUser(int id) {
        User user = null;
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT user_id, f_name,i_name, photo_src FROM USERS where user_id=?")) {
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
                            rs.getString("photo_src")
                    );
                }
            }
            return user;
        }
    }

    @Override
    @SneakyThrows
    public Collection<User> getFriends(int id) {
        Collection<User> friends = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT f.child_user_id,us.f_name,us.i_name, us.photo_src " +
                     "FROM FRIENDSHIP f " +
                     "JOIN USERS us ON us.user_id=f.child_user_id " +
                     "where f.parent_user_id=?")) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    friends.add(new User(
                                    rs.getInt("child_user_id"),
                                    null,
                                    null,
                                    rs.getString("f_name"),
                                    rs.getString("i_name"),
                                    null,
                                    null,
                                    rs.getString("photo_src")
                            )

                    );
                }
            }
            return friends;
        }
    }

    @SneakyThrows
    public UserInfo getUserInfo(int id) {

        Collection<User> friends = new HashSet<>();
        Collection<Book> books = new ArrayList<>();
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
                                            rs1.getString("photo_src")
                                    )

                            );
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
                    Collections.sort((ArrayList) books, ((o1, o2) -> {
                        Book b1 = (Book) o1;
                        Book b2 = (Book) o2;
                        return b1.getBook_name().compareTo(b2.getBook_name()) > 0 ? 1 : b1.getBook_name().equals(b2.getBook_name()) ? 0 : -1;
                    }));

                    userInfo.setUser_books(books);
                }
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            userInfo.setUser_id(rs1.getInt("user_id"));
                            userInfo.setF_name(rs1.getString("f_name"));
                            userInfo.setI_name(rs1.getString("i_name"));
                            userInfo.setDob(new LocalDate(rs1.getDate("dob").getTime()));
                            userInfo.setPhoto_src(rs1.getString("photo_src"));
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
    public int sendMessage(int user_id_from, int user_id_to, String message, DateTime dateTime) {
        int res = 0;
        try (Connection connection = connectionSupplier.get();
             CallableStatement statement = connection.prepareCall("{ ? = CALL put_message(?,?,?,?)}");
        ) {
            statement.registerOutParameter(1, Types.INTEGER);
            statement.setInt(2, user_id_from);
            statement.setInt(3, user_id_to);
            statement.setString(4, message);
            statement.setTimestamp(5, new Timestamp(dateTime.getMillis()));
            statement.execute();
            res = statement.getInt(1);
        }
        return res;
    }


    @SneakyThrows
    public Collection<UserCommunication> getUserCommunications(int user_id) {

        Collection<UserCommunication> communications = new ArrayList<>();
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
                            communication.setPhoto_src(rs1.getString("photo_src"));
                            communication.setOwnerPhoto_src(rs1.getString("own_photo_src"));
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
                            message.setMessage(rs1.getString("message"));
                            message.setActive(rs1.getInt("active"));
                            message.setDate(new DateTime(rs1.getTimestamp("date").getTime()));
                            message.setGroupNum(rs1.getInt("group_num"));
                            message.setId(rs1.getInt("id"));
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
             PreparedStatement statement = connection.prepareStatement("SELECT u.user_id, u.f_name, u.i_name,u.photo_src " +
                     "                                                  FROM user_communications uc" +
                     "                                                  JOIN USERS u ON u.user_id=uc.user_id" +
                     "                                                   where uc.user_id!=? and uc.communication_id=?")) {
            statement.setInt(1, user_id);
            statement.setInt(2, communication_id);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    user.setF_name(resultSet.getString("f_name"));
                    user.setI_name(resultSet.getString("i_name"));
                    user.setUser_id(resultSet.getInt("user_id"));
                    user.setPhoto_src(resultSet.getString("photo_src"));
                }
            }
        }
        return user;
    }

    @Override
    @SneakyThrows
    public void updatePhoto(String path, int user_id) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE USERS " +
                             " SET photo_src= ? " +
                             " where user_id=?");
        ) {
            statement.setString(1, path);
            statement.setInt(2, user_id);
            statement.execute();

        }
    }

    @Override
    @SneakyThrows
    public void deleteFriend(int owner_id, int friend_id) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM friendship " +
                             " where parent_user_id=? AND child_user_id=?");
        ) {
            statement.setInt(1, owner_id);
            statement.setInt(2, friend_id);
            statement.execute();

        }
    }


    @Override
    @SneakyThrows
    public void updateMessages(String messageList) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM update_messages(?)");
        ) {

            statement.setString(1, messageList);
            statement.execute();
        }
    }

    @Override
    @SneakyThrows
    public void addFriend(int whoId, int whomId) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO friend_requests(user_id_req,user_id_resp,active) VALUES(?,?,?)");
        ) {
            statement.setInt(1, whoId);
            statement.setInt(2, whomId);
            statement.setInt(3, 1);
            statement.execute();
        }
    }

    @Override
    @SneakyThrows
    public void activateFriendship(int friendId, int userId) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE friend_requests SET active=0 WHERE user_id_req=? AND user_id_resp=?");
        ) {
            statement.setInt(1, friendId);
            statement.setInt(2, userId);
            statement.execute();
        }
    }

    @Override
    @SneakyThrows
    public boolean addBook(int book_id, int user_id) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO user_books(user_id,book_id) VALUES(?,?)");
        ) {

            statement.setInt(1, user_id);
            statement.setInt(2, book_id);
            statement.execute();
        }
        return true;
    }

    @Override
    @SneakyThrows
    public Collection<Integer> getUserBooks(int user_id) {
        Collection<Integer> userBooks = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT book_id FROM user_books where user_id=?");
        ) {
            statement.setInt(1, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    userBooks.add(resultSet.getInt("book_id"));
                }
                return userBooks;
            }
        }
    }

    @Override
    @SneakyThrows
    public Collection<FriendRequest> getFriendRequests(int userId) {
        Collection<FriendRequest> friendRequests = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT request_id,user_id_req FROM friend_requests WHERE user_id_resp=? AND active=1");
        ) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    friendRequests.add(new FriendRequest(resultSet.getInt("request_id"), resultSet.getInt("user_id_req")));
                }
                return friendRequests;
            }
        }
    }

    @Override
    @SneakyThrows
    public Collection<Integer> getOwnerRequests(int userId) {
        Collection<Integer> ownerRequests = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT user_id_resp FROM friend_requests WHERE user_id_req=? AND active=1");
        ) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ownerRequests.add(resultSet.getInt("user_id_resp"));
                }
                return ownerRequests;
            }
        }
    }

    @Override
    @SneakyThrows
    public int getUnreadMessCount(int userId) {
        int messCnt = 0;
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(1) as cnt FROM messages" +
                             " WHERE user_to=?" +
                             " AND active=1");
        ) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    messCnt = resultSet.getInt("cnt");
                }
                return messCnt;
            }
        }
    }


    @Override
    @SneakyThrows
    public Collection<User> getFriendReqDetail(int id) {
        Collection<User> friends = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT us.user_id,us.f_name,us.i_name, us.photo_src " +
                             "FROM friend_requests req " +
                             "JOIN USERS us ON req.user_id_req=us.user_id AND req.user_id_resp=?")) {
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
                                    rs.getString("photo_src")
                            )

                    );
                }
            }
            return friends;
        }
    }

    @Override
    @SneakyThrows
    public String getPsw(int user_id) {
        int usCnt = 0;
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT password FROM USERS" +
                             " WHERE user_id=?");
        ) {
            statement.setInt(1, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    return resultSet.getString("password");
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }


    @Override
    @SneakyThrows
    public void updateProfile(User user) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE USERS " +
                             "SET f_name=?, i_name=?, dob=?, password=?, email=?" +
                             " WHERE user_id=?");
        ) {
            statement.setString(1, user.getF_name());
            statement.setString(2, user.getI_name());
            statement.setDate(3, new java.sql.Date(user.getDob().toDate().getTime()));
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getEmail());
            statement.setInt(6, user.getUser_id());
            statement.execute();
        }
    }

    @Override
    @SneakyThrows
    public boolean checkLogin(String login, String password) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT user_id" +
                             " FROM USERS WHERE email=? AND password=?");) {

            statement.setString(1, login);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    return true;
                }
                return false;
            }
        }
    }
}