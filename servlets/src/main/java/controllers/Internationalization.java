package controllers;

import model.Genre;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

/**
 * Created by Echetik on 15.01.2017.
 */
@WebServlet("/locale")
public class Internationalization extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("locale").equals("rus")) {
            Locale.setDefault(new Locale("ru", "RU"));
        } else {
            Locale.setDefault(Locale.ENGLISH);
        }
    }
}
