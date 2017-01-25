package helpLib;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Echetik on 24.01.2017.
 */
public interface Command {
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException;
}