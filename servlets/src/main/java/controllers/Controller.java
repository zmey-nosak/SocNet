package controllers;

import helpLib.Command;
import helpLib.ConfigurationManager;
import helpLib.MessageManager;
import helpLib.RequestHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Echetik on 24.01.2017.
 */
@WebServlet("/controller")
public class Controller extends HttpServlet
        implements javax.servlet.Servlet {
    //объект, содержащий список возможных команд
    RequestHelper requestHelper = RequestHelper.getInstance();

    public Controller() {
        super();
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String page = null;
        try {
//определение команды, пришедшей из JSP
            Command command = requestHelper.getCommand(request);
/*вызов реализованного метода execute() интерфейса Command и передача
параметров классу-обработчику конкретной команды*/
            page = command.execute(request, response);
// метод возвращает страницу ответа
        } catch (ServletException e) {
            e.printStackTrace();
//генерация сообщения об ошибке
            request.setAttribute("errorMessage",
                    MessageManager.getInstance().getProperty(
                            MessageManager.SERVLET_EXCEPTION_ERROR_MESSAGE));
//вызов JSP-страницы c cообщением об ошибке
            page = ConfigurationManager.getInstance()
                    .getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", MessageManager.getInstance().getProperty(MessageManager.IO_EXCEPTION_ERROR_MESSAGE));
            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        }
//вызов страницы ответа на запрос
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }
}

