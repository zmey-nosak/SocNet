package websockets;

import dao.UserDao;

import javax.servlet.http.HttpSession;
import javax.websocket.Endpoint;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Echetik on 16.11.2016.
 */

public class ExamplesConfig extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        sec.getUserProperties().put("user_id", (String) ((HttpSession) request.getHttpSession()).getAttribute("user_id").toString());
        sec.getUserProperties().put("userDao", (UserDao) ((HttpSession) request.getHttpSession()).getServletContext().getAttribute("userDao"));
    }
}