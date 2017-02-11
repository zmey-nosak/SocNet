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
 * Created by Echetik on 05.11.2016.
 */
//Сервлет формы регистрации
@WebServlet("/register/")
public class RegistrationServlet extends HttpServlet {
    UserDao userDao;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        super.service(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/filterValues.jsp";
        String message = "";
        boolean isExistMistake = false;
        String action = req.getParameter("action");
        if (action == null) {
            url = "/register.jsp";
        } else if (action.equals("add")) {
            User user = new User();
            req.setCharacterEncoding("UTF-8");
            user.setSurname(req.getParameter("surname"));
            user.setName(req.getParameter("name"));
            user.setEmail(req.getParameter("email"));
            user.setPhotoSrc("D://user_images//rod.gif");
            try {
                DateTimeFormatter formatter = DateTimeFormat.forPattern("d.MM.yyyy");
                formatter = formatter.withLocale(req.getLocale());  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
                LocalDate date = formatter.parseLocalDate(req.getParameter("dateOfBirth"));
                user.setDateOfBirth(date);
            } catch (Exception ex) {
                message += "Date is not valid.<br>";
                url = "/register.jsp";
                isExistMistake = true;

            }
            user.setPassword(req.getParameter("password"));
            if (userDao.doesEmailExist(user.getEmail())) {
                message += "This email address already exists.<br>" +
                        "Please enter another email address.";
                url = "/register.jsp";
                isExistMistake = true;
            } else if (!isExistMistake) {

                long new_user_id = userDao.registerUser(user);
                url = "/userpage?userId="+new_user_id;
                req.getSession().setAttribute("userId", new_user_id);
            }
            req.setAttribute("user", user);
            req.setAttribute("message", message);
        }
        req.getRequestDispatcher(url).forward(req, resp);

    }


    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        userDao = (UserDao) servletContext.getAttribute("userDao");
    }
}
