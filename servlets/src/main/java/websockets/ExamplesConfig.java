package websockets;

import dao.UserDao;
import model.User;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * Created by Echetik on 16.11.2016.
 */

public class ExamplesConfig extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        try {
            HttpSession session = (HttpSession) request.getHttpSession();
            User user = (User) session.getAttribute("user");
            sec.getUserProperties().put("userId", user.getUserId() + "");
            sec.getUserProperties().put("userDao", (UserDao) ((HttpSession) request.getHttpSession()).getServletContext().getAttribute("userDao"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}