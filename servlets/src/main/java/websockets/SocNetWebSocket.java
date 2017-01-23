package websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import dao.UserDao;
import jdk.nashorn.internal.parser.JSONParser;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import util.HTMLFilter;


import javax.json.Json;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

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
    public void handleMessage(String message, Session userSession) {

        String user_id_from = (String) userSession.getUserProperties().get("user_id");
        UserDao userDao = (UserDao) userSession.getUserProperties().get("userDao");
        if (user_id_from != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                TechnicalMessage mess = mapper.readValue(message, TechnicalMessage.class);

                int type = Integer.parseInt(mess.getType());
                switch (type) {
                    case 0:
                        int messId = userDao.sendMessage(Integer.parseInt(user_id_from), Integer.parseInt(mess.user_id_to), mess.message, DateTime.parse(mess.date));

                        mess.setId(String.valueOf(messId));
                        sendMessOnlineUser(mess.getUser_id_to(), toJson(mess));//send message to partner

                        mess.setUser_id_to(mess.getUser_id());//messageForMyself
                        userSession.getBasicRemote().sendText(toJson(mess));

                        break;
                    case 1:
                        userDao.updateMessages(mess.getMessages_for_update());
                        userSession.getBasicRemote().sendText(message);
                        sendMessOnlineUser(mess.getUser_id_to(), message);
                        break;
                    case 2:
                        sendMessOnlineUser(mess.getUser_id_to(), message);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ;
    }

    @OnError
    public void handleError(Throwable t) {
    }

    @SneakyThrows
    private void sendMessOnlineUser(String whom, String mess) {
        Optional<Session> session = users.stream().filter(x -> x.getUserProperties().get("user_id").equals(whom)).findAny();
        if (session.isPresent()) {
            session.get().getBasicRemote().sendText(mess);
        }
    }

    @SneakyThrows
    private String toJson(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writer().writeValueAsString(o);
    }

    @Setter
    @Getter
    private static class TechnicalMessage {
        private String message;
        private String user_id_to;
        private String user_id;
        private String date;
        private String type;
        private String new_friend_id;
        private String messages_for_update;
        private String id;
        private String active;
    }
}