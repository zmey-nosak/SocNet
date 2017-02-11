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
        userSession.getUserProperties().put("userId", endpointConfig.getUserProperties().get("userId"));
        users.add(userSession);
    }

    @OnClose
    public void handleClose(Session userSession) {
        users.remove(userSession);
    }

    @OnMessage
    public void handleMessage(String message, Session userSession) {

        String userIdFrom = (String) userSession.getUserProperties().get("userId");
        UserDao userDao = (UserDao) userSession.getUserProperties().get("userDao");
        if (userIdFrom != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                TechnicalMessage mess = mapper.readValue(message, TechnicalMessage.class);

                int type = Integer.parseInt(mess.getType());
                switch (type) {
                    case 0:
                        int messId = userDao.sendMessage(Integer.parseInt(userIdFrom), Integer.parseInt(mess.userIdTo), mess.message, DateTime.parse(mess.date));

                        mess.setId(String.valueOf(messId));
                        sendMessOnlineUser(mess.getUserIdTo(), toJson(mess));//send message to partner

                        mess.setUserIdTo(mess.getUserId());//messageForMyself
                        userSession.getBasicRemote().sendText(toJson(mess));

                        break;
                    case 1:
                        userDao.updateMessages(mess.getMessagesForUpdate());
                        userSession.getBasicRemote().sendText(message);
                        sendMessOnlineUser(mess.getUserIdTo(), message);
                        break;
                    case 2:
                        sendMessOnlineUser(mess.getUserIdTo(), message);
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
        Optional<Session> session = users.stream().filter(x -> x.getUserProperties().get("userId").equals(whom)).findAny();
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
        private String userIdTo;
        private String userId;
        private String date;
        private String type;
        private String newFriendId;
        private String messagesForUpdate;
        private String id;
        private String active;
    }
}