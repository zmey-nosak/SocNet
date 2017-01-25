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
    UserInfo getUserInfo(int user_id);

    User getUserIdByEmail(String email);

    int registerUser(User user);

    boolean emailExists(String email);

    Collection<User> getAllUsers();

    Collection<User> getFriends(int user_id);

    Collection<Integer> getUserBooks(int user_id);

    User getUser(int user_id);

    int sendMessage(int user_id_from, int user_id_to, String message, DateTime dateTime);

    Collection<UserCommunication> getUserCommunications(int user_id);

    Collection<UserMessage> getUserMessages(int user_id, int communication_id);

    User getPartnerByCommunication(int user_id, int communication_id);

    void updatePhoto(String patch, int user_id);

    void updateMessages(String messageList);

    boolean addBook(int book_id, int user_id);

    void addFriend(int whoId, int whomId);

    void activateFriendship(int friendId, int userId);

    void deleteFriend(int owner_id, int friend_id);

    Collection<FriendRequest> getFriendRequests(int userId);

    Collection<Integer> getOwnerRequests(int userId);

    int getUnreadMessCount(int userId);

    Collection<User> getFriendReqDetail(int user_id);

    String getPsw(int user_id);

    void updateProfile(User user);

    boolean checkLogin(String login, String password);

}
