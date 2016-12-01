package dao;

import model.User;
import model.UserCommunication;
import model.UserInfo;
import model.UserMessage;

import java.util.Collection;

/**
 * Created by Echetik on 04.11.2016.
 */
public interface UserDao {
    UserInfo getUserInfo(long user_id);

    long getUserIdByEmail(String email);

    long registerUser(User user);

    boolean emailExists(String email);

    Collection<User> getAllUsers();

    Collection<User> getFriends(long user_id);

    User getUser(long user_id);

    boolean sendMessage(int user_id_from, int user_id_to, String message);

    Collection<UserCommunication> getUserCommunications(int user_id);

    Collection<UserMessage> getUserMessages(int user_id, int communication_id);
}
