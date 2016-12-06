package dao;

import model.User;
import model.UserCommunication;
import model.UserInfo;
import model.UserMessage;
import org.joda.time.DateTime;

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

    User getUser(int user_id);

    boolean sendMessage(int user_id_from, int user_id_to, String message, DateTime dateTime);

    Collection<UserCommunication> getUserCommunications(int user_id);

    Collection<UserMessage> getUserMessages(int user_id, int communication_id);

    User getPartnerByCommunication(int user_id, int communication_id);

    Collection<UserMessage> getLastMessage(int user_id, DateTime dateTime,int communication_id);

    //Collection<UserCommunication> getUserCommunications(long user_id);

}
