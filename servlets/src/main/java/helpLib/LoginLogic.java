package helpLib;

import dao.UserDao;
import util.StringEncryptUtil;

import java.security.NoSuchAlgorithmException;

/**
 * Created by Echetik on 24.01.2017.
 */
public class LoginLogic {
    UserDao userDao;

    public LoginLogic(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean checkLogin(String login, String password) {
        try {
            return userDao.checkLogin(login, StringEncryptUtil.encrypt(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
}