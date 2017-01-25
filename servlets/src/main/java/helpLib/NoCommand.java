package helpLib;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Echetik on 24.01.2017.
 */
public class NoCommand implements Command {

    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
/*в случае прямого обращения к контроллеру переадресация на страницу ввода
логина*/
        String page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.LOGIN_PAGE_PATH);
        return page;
    }
}