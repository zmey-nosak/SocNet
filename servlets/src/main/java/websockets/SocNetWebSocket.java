package websockets;

import util.HTMLFilter;


import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Echetik on 16.11.2016.
 */
@ServerEndpoint(value = "/chat", configurator = ExamplesConfig.class)
public class SocNetWebSocket {
    static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void handleOpen(EndpointConfig endpointConfig, Session userSession) {
        userSession.getUserProperties().put("username", endpointConfig.getUserProperties().get("username"));
        users.add(userSession);
    }

    @OnClose
    public void handleClose(Session userSession) {
        users.remove(userSession);
    }

    @OnMessage
    public void handleMessage(String message, Session userSession) {
        String userName = (String) userSession.getUserProperties().get("username");
        if (userName != null) {

            users.stream().forEach(x -> {
                try {
                    x.getBasicRemote().sendText(HTMLFilter.filter(message.toString()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    @OnError
    public void handleError(Throwable t) {
    }
}