package controllers;

import dao.UserDao;
import model.User;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Echetik on 11.01.2017.
 */
//Сервлет редактирования профиля пользователя
@WebServlet("/profile")
public class Profile extends HttpServlet {
    UserDao userDao;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        super.service(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        req.setAttribute("user", user);
        req.getRequestDispatcher("/profile.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = "" ;
        String url = "" ;
        User owner = (User) req.getSession().getAttribute("user");
        model.User user = new model.User();
        req.setCharacterEncoding("UTF-8");
        String surname = req.getParameter("surname");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        if (surname.isEmpty()) {
            message += "First Name cant be empty.<br>" ;
        } else {
            user.setSurname(surname);
        }
        if (name.isEmpty()) {
            message += "Last Name cant be empty.<br>" ;
        } else {
            user.setName(name);
        }
        if (email.isEmpty()) {
            message += "Email cant be empty.<br>" ;
        } else {
            if (!email.equals(owner.getEmail())) {
                if (userDao.doesEmailExist(user.getEmail())) {
                    message += "This email address already exists.<br>" +
                            "Please enter another email address." ;
                }
            } else {
                user.setEmail(owner.getEmail());
            }
        }
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("d.MM.yyyy");
            formatter = formatter.withLocale(req.getLocale());
            LocalDate date = formatter.parseLocalDate(req.getParameter("dateOfBirth"));
            user.setDateOfBirth(date);
        } catch (Exception ex) {
            message += "Date is not valid.<br>" ;
        }

        ////////////////UPDATE PASSWORD////////////////
        String newP = req.getParameter("new_password");
        String confP = req.getParameter("conf_password");
        String oldP = req.getParameter("old_password");
        if (!newP.isEmpty() || !confP.isEmpty() || !oldP.isEmpty()) {
            if (userDao.getPsw(owner.getUserId()).equals(oldP)) {
                if (newP.isEmpty() && confP.isEmpty()) {
                    message += "Password cant be empty.<br>" ;
                } else if (!newP.equals(confP.isEmpty())) {
                    message += "Passwords labels are not equal.<br>" ;
                } else {
                    user.setPassword(newP);
                }
            } else {
                message += "current password is not correct!<br>" ;
            }
        } else {
            user.setPassword(userDao.getPsw(owner.getUserId()));
        }


        if (message.isEmpty()) {
            user.setUserId(((User) req.getSession().getAttribute("user")).getUserId());
            userDao.updateProfile(user);
            url = "/userpage?userId=" + user.getUserId();
            req.getSession().setAttribute("user", user);
        } else {
            url = "/profile.jsp" ;
        }
        req.setAttribute("user", user);
        req.setAttribute("message", message);
        req.getRequestDispatcher(url).forward(req, resp);
    }


    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        userDao = (UserDao) servletContext.getAttribute("userDao");
    }
}
