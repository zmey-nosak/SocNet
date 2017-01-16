package controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

/**
 * Created by Echetik on 08.01.2017.
 */
@WebServlet("/files/*")
public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String str = request.getPathInfo();
        // String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
        File file = new File("D:\\user_images\\" + str);
        String filename = fooBar2(str);
        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }

    String fooBar2(String s) {
        int bslashIdx = s.lastIndexOf('/');
        if (bslashIdx != -1) {
            return s.substring(bslashIdx + 1, s.length());
        }
        return s;
    }
}
