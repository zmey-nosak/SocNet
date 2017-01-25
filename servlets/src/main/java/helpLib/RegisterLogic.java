package helpLib;

import dao.UserDao;

/**
 * Created by Echetik on 25.01.2017.
 */
public class RegisterLogic {
    UserDao userDao;

    public RegisterLogic(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean checkLogin(String login) {
        return userDao.emailExists(login);
    }

    public int registerUser(model.User user) {
        return userDao.registerUser(user);
    }
    public model.User getRegisteredUser(int userId) {
        return userDao.getUser(userId);
    }
}
