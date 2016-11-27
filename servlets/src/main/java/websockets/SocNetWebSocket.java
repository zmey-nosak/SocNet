package websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDao;
import jdk.nashorn.internal.parser.JSONParser;
import lombok.SneakyThrows;
import util.HTMLFilter;


import javax.json.Json;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Echetik on 16.11.2016.
 */
@ServerEndpoint(value = "/chat", configurator = ExamplesConfig.class)
public class SocNetWebSocket {
    static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void handleOpen(EndpointConfig endpointConfig, Session userSession) {
        userSession.getUserProperties().put("user_id", endpointConfig.getUserProperties().get("user_id"));
        users.add(userSession);
    }

    @OnClose
    public void handleClose(Session userSession) {
        users.remove(userSession);
    }

    @OnMessage

    public void handleMessage(String message, Session userSession/*, @PathParam("user_id")String user_id*/) {

        String user_id_from = (String) userSession.getUserProperties().get("user_id");
        UserDao userDao = (UserDao) userSession.getUserProperties().get("userDao");

        if (user_id_from != null) {
            try{
            ObjectMapper mapper = new ObjectMapper();
            Mess mess = mapper.readValue(message, Mess.class);
            Optional<Session> session = users.stream().filter(x -> x.getUserProperties().get("user_id").equals(mess.getUser_id_to())).findAny();
            userDao.sendMessage(Integer.parseInt(user_id_from), Integer.parseInt(mess.user_id_to), mess.message);
            if (session.isPresent()) {
                session.get().getBasicRemote().sendText(HTMLFilter.filter(mess.message));
            }}catch (Exception e){
                e.printStackTrace();
            }
        }
        ;
    }

    @OnError
    public void handleError(Throwable t) {
    }

    private static class Mess {
        private String message;
        private String user_id_to;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUser_id_to() {
            return user_id_to;
        }

        public void setUser_id_to(String user_id_to) {
            this.user_id_to = user_id_to;
        }
    }
}