package dao;

import model.*;
import org.joda.time.DateTime;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;

/**
 * Created by Echetik on 04.11.2016.
 */
public interface UserDao {
    UserInfo getUserInfo(int userId);

    User getUserIdByEmail(String email);

    int registerUser(User user);

    boolean doesEmailExist(String email);

    Collection<User> getAllUsers();

    Collection<User> getFriends(int userId);

    Collection<Integer> getUserBooks(int userId);

    User getUser(int user_id);

    int sendMessage(int userIdFrom, int userIdTo, String message, DateTime dateTime);

    Collection<UserCommunication> getUserCommunications(int userId);

    Collection<UserMessage> getUserMessages(int userId, int communicationId);

    User getPartnerByCommunication(int userId, int communicationId);

    void updateUserPhoto(String patch, int userId);

    void updateMessages(String messageList);

    boolean addBook(int book_id, int userId);

    void addFriend(int whoId, int whomId);

    void activateFriendship(int friendId, int userId);

    void deleteFriend(int ownerId, int friendId);

    Collection<FriendRequest> getFriendRequests(int userId);

    Collection<Integer> getOwnerRequests(int userId);

    int getUnreadMessCount(int userId);

    Collection<User> getFriendReqDetail(int userId);

    String getPsw(int userId);

    void updateProfile(User user);

    boolean checkLogin(String login, String password);

}
