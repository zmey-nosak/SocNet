package helpLib;

import dao.UserDao;

/**
 * Created by Echetik on 24.01.2017.
 */
public class LoginLogic {
    UserDao userDao;

    public LoginLogic(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean checkLogin(String login, String password) {
        return userDao.checkLogin(login, password);
    }
}