package dao.postgres;

import dao.ColumnsDB;
import dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import model.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.sql.*;
import java.util.*;
import java.util.function.Supplier;

/**
 * Created by Echetik on 04.11.2016.
 */
@AllArgsConstructor
public class PostgresUserDao implements UserDao {
    private Supplier<Connection> connectionSupplier;
    private final String GET_USER_ID_BY_EMAIL = "SELECT user_id,f_name,i_name,email, dob, photo_src FROM USERS where email=?";
    private final String REGISTER_USER = "{ ? = CALL f_register_user(?,?,?,?,?,?)}";
    private final String DOES_EMAIL_EXIST = "{ ? = CALL f_is_exist_email(?)}";


    private User mappingUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt(ColumnsDB.USER_ID_COLUMN_DB));
        user.setSurname(resultSet.getString(ColumnsDB.USER_SURNAME_COLUMN_DB));
        user.setName(resultSet.getString(ColumnsDB.USER_NAME_COLUMN_DB));
        user.setEmail(resultSet.getString(ColumnsDB.USER_EMAIL_COLUMN_DB));
        user.setPhotoSrc(resultSet.getString(ColumnsDB.USER_PHOTO_SRC_COLUMN_DB));
        user.setDateOfBirth(new LocalDate(resultSet.getDate(ColumnsDB.USER_DATE_OF_BIRTH_COLUMN_DB).getTime()));
        return user;
    }

    private Book mappingBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setBookId(resultSet.getInt(ColumnsDB.BOOK_ID_COLUMN_DB));
        book.setBookName(resultSet.getString(ColumnsDB.BOOK_NAME_COLUMN_DB));
        book.setImageSrc(resultSet.getString(ColumnsDB.BOOK_IMG_COLUMN_DB));
        return book;
    }

    private UserCommunication mappingCommunication(ResultSet resultSet) throws SQLException {
        UserCommunication communication = new UserCommunication();
        communication.setUserIdFrom(resultSet.getInt(ColumnsDB.COMMUNICATION_USER_ID_FROM_COLUMN_DB));
        communication.setSurname(resultSet.getString(ColumnsDB.USER_SURNAME_COLUMN_DB));
        communication.setName(resultSet.getString(ColumnsDB.USER_NAME_COLUMN_DB));
        communication.setMessage(resultSet.getString(ColumnsDB.COMMUNICATION_MESSAGE_COLUMN_DB));
        communication.setActive(resultSet.getInt(ColumnsDB.ACTIVE_COLUMN_DB));
        communication.setCommunicationId(resultSet.getInt(ColumnsDB.COMMUNICATION_ID_COLUMN_DB));
        communication.setDate(new DateTime(resultSet.getTimestamp(ColumnsDB.DATE_COLUMN_DB).getTime()));
        communication.setPhotoSrc(resultSet.getString(ColumnsDB.USER_PHOTO_SRC_COLUMN_DB));
        communication.setOwnerPhotoSrc(resultSet.getString(ColumnsDB.COMMUNICATION_OWNER_PHOTO_COLUMN_DB));
        communication.setPartner(resultSet.getInt(ColumnsDB.COMMUNICATION_PARTNER_ID_COLUMN_DB));
        return communication;
    }

    private UserMessage mappingMessage(ResultSet resultSet) throws SQLException {
        UserMessage message = new UserMessage();
        message.setUserId(resultSet.getInt(ColumnsDB.USER_ID_COLUMN_DB));
        message.setMessage(resultSet.getString(ColumnsDB.COMMUNICATION_MESSAGE_COLUMN_DB));
        message.setActive(resultSet.getInt(ColumnsDB.ACTIVE_COLUMN_DB));
        message.setDate(new DateTime(resultSet.getTimestamp(ColumnsDB.DATE_COLUMN_DB).getTime()));
        message.setId(resultSet.getInt(ColumnsDB.MESSAGE_ID_COLUMN_DB));
        return message;
    }

    @SneakyThrows
    public User getUserIdByEmail(String email) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(GET_USER_ID_BY_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mappingUser(resultSet);
                } else return null;
            }
        }
    }

    @Override
    @SneakyThrows
    public int registerUser(User user) {
        int new_user_id;
        try (Connection connection = connectionSupplier.get();
             CallableStatement statement = connection.prepareCall(REGISTER_USER);
        ) {
            statement.registerOutParameter(1, Types.INTEGER);
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.setDate(6, new java.sql.Date(user.getDateOfBirth().toDate().getTime()));
            statement.setString(7, "\\common\\avatar.gif");
            statement.execute();
            new_user_id = statement.getInt(1);
        }
        return new_user_id;
    }

    @Override
    @SneakyThrows
    public boolean doesEmailExist(String email) {
        boolean res = false;
        try (Connection connection = connectionSupplier.get();
             CallableStatement statement = connection.prepareCall(DOES_EMAIL_EXIST)) {
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setString(2, email);
            statement.execute();
            boolean b = statement.getBoolean(1);
            return b;
        }
    }

    @Override
    @SneakyThrows
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT user_id, f_name,i_name, email, photo_src, dob FROM USERS")) {
            while (resultSet.next()) {
                users.add(mappingUser(resultSet));
            }
        }
        return users;
    }

    @Override
    @SneakyThrows
    public User getUser(int userId) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT user_id, f_name,i_name, email, photo_src, dob FROM USERS where user_id=?")) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mappingUser(resultSet);
                } else return null;
            }
        }
    }

    @Override
    @SneakyThrows
    public ArrayList<User> getFriends(int userId) {
        ArrayList<User> friends = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT f.child_user_id as user_id,us.f_name,us.i_name, us.photo_src, us.email, us.dob " +
                             "FROM FRIENDSHIP f " +
                             "JOIN USERS us ON us.user_id=f.child_user_id " +
                             "where f.parent_user_id=?")) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    friends.add(mappingUser(resultSet));
                }
            }
            return friends;
        }
    }

    @SneakyThrows
    public UserInfo getUserInfo(int userId) {

        ArrayList<User> friends = new ArrayList<>();
        ArrayList<Book> books = new ArrayList<>();
        UserInfo userInfo = new UserInfo();
        try (Connection con = connectionSupplier.get();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM get_user_info(?)")) {
            con.setAutoCommit(false);
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            friends.add(mappingUser(rs1));
                        }
                    }
                    userInfo.setUserFriends(friends);
                }
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            books.add(mappingBook(rs1));
                        }
                        Collections.sort(books, ((o1, o2) -> o1.getBookName().compareTo(o2.getBookName())));
                        userInfo.setUserBooks(books);
                    }
                }
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            userInfo.setUserId(rs1.getInt(ColumnsDB.USER_ID_COLUMN_DB));
                            userInfo.setSurname(rs1.getString(ColumnsDB.USER_SURNAME_COLUMN_DB));
                            userInfo.setName(rs1.getString(ColumnsDB.USER_NAME_COLUMN_DB));
                            userInfo.setDateOfBirth(new LocalDate(rs1.getDate(ColumnsDB.USER_DATE_OF_BIRTH_COLUMN_DB).getTime()));
                            userInfo.setPhotoSrc(rs1.getString(ColumnsDB.USER_PHOTO_SRC_COLUMN_DB));
                            userInfo.setUser(mappingUser(rs1));
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
    public int sendMessage(int userIdFrom, int userIdTo, String message, DateTime dateTime) {
        int res = 0;
        try (Connection connection = connectionSupplier.get();
             CallableStatement statement = connection.prepareCall("{ ? = CALL put_message(?,?,?,?)}");
        ) {
            statement.registerOutParameter(1, Types.INTEGER);
            statement.setInt(2, userIdFrom);
            statement.setInt(3, userIdTo);
            statement.setString(4, message);
            statement.setTimestamp(5, new Timestamp(dateTime.getMillis()));
            statement.execute();
            res = statement.getInt(1);
        }
        return res;
    }


    @SneakyThrows
    public Collection<UserCommunication> getUserCommunications(int userId) {
        Collection<UserCommunication> communications = new ArrayList<>();
        try (Connection con = connectionSupplier.get();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM get_communications(?)")) {
            // для PostgreSQL сначала нужно создать транзакцию (AutoCommit == false)...
            con.setAutoCommit(false);
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            communications.add(mappingCommunication(rs1));
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
    public Collection<UserMessage> getUserMessages(int userId, int communicationId) {
        Collection<UserMessage> messages = new ArrayList<>();
        try (Connection con = connectionSupplier.get();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM get_messages(?,?)")) {
            // для PostgreSQL сначала нужно создать транзакцию (AutoCommit == false)...
            con.setAutoCommit(false);
            statement.setInt(1, userId);
            statement.setInt(2, communicationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    try (ResultSet rs1 = (ResultSet) resultSet.getObject(1)) {
                        while (rs1.next()) {
                            messages.add(mappingMessage(rs1));
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
    public User getPartnerByCommunication(int userId, int communicationId) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement("SELECT u.user_id, u.f_name, u.i_name,u.photo_src, u.email, u.dob " +
                     "                                                  FROM user_communications uc" +
                     "                                                  JOIN USERS u ON u.user_id=uc.user_id" +
                     "                                                   where uc.user_id!=? and uc.communication_id=?")) {
            statement.setInt(1, userId);
            statement.setInt(2, communicationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mappingUser(resultSet);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    @SneakyThrows
    public void updateUserPhoto(String path, int userId) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE USERS " +
                             " SET photo_src= ? " +
                             " where user_id=?");
        ) {
            statement.setString(1, path);
            statement.setInt(2, userId);
            statement.execute();

        }
    }

    @Override
    @SneakyThrows
    public void deleteFriend(int ownerId, int friendId) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM friendship " +
                             " where parent_user_id=? AND child_user_id=?");
        ) {
            statement.setInt(1, ownerId);
            statement.setInt(2, friendId);
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
    public boolean addBook(int bookId, int userId) {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO user_books(user_id,book_id) VALUES(?,?)");
        ) {

            statement.setInt(1, userId);
            statement.setInt(2, bookId);
            statement.execute();
        }
        return true;
    }

    @Override
    @SneakyThrows
    public ArrayList<Integer> getUserBooks(int userId) {
        ArrayList<Integer> userBooks = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT book_id FROM user_books where user_id=?");
        ) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    userBooks.add(resultSet.getInt(ColumnsDB.BOOK_ID_COLUMN_DB));
                }
                return userBooks;
            }
        }
    }

    @Override
    @SneakyThrows
    public ArrayList<FriendRequest> getFriendRequests(int userId) {
        ArrayList<FriendRequest> friendRequests = new ArrayList<>();
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
    public Collection<User> getFriendReqDetail(int userId) {
        Collection<User> friends = new ArrayList<>();
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT us.user_id,us.f_name,us.i_name, us.photo_src, us.dob, us.email " +
                             "FROM friend_requests req " +
                             "JOIN USERS us ON req.user_id_req=us.user_id AND req.user_id_resp=?")) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    friends.add(mappingUser(resultSet));
                }
            }
            return friends;
        }
    }

    @Override
    @SneakyThrows
    public String getPsw(int userId) {
        int usCnt = 0;
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT password FROM USERS" +
                             " WHERE user_id=?");
        ) {
            statement.setInt(1, userId);
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
            statement.setString(1, user.getSurname());
            statement.setString(2, user.getName());
            statement.setDate(3, new java.sql.Date(user.getDateOfBirth().toDate().getTime()));
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getEmail());
            statement.setInt(6, user.getUserId());
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