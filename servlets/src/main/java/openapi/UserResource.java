package openapi;

import dao.UserDao;
import listeners.Initializer;
import model.UserCommunication;
import model.UserMessage;
import org.joda.time.DateTime;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("users")
@Produces(APPLICATION_JSON)
public class UserResource implements JsonRestfulWebResource {

    private UserDao userDao;

    @Context
    public void init(ServletContext context) {
        userDao = (UserDao) context.getAttribute(Initializer.USER_DAO);
    }

    @GET
    public Response getAll() {
        final Collection<model.User> users = userDao.getAllUsers();
        return users.size() > 0 ? ok(users) : noContent();
    }

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") int id) {
        model.User user = userDao.getUser(id);
        if (user != null) {
            return this.ok(user);
        } else {
            return noContent();
        }
    }

    @GET
    @Path("{id}/friends")
    public Response getFriends(@PathParam("id") int id) {
        final Collection<model.User> users = userDao.getFriends(id);
        if (users != null) {
            return this.ok(users);
        } else {
            return noContent();
        }
    }

    @GET
    @Path("{id}/communications")
    public Response getCommunications(@PathParam("id") int id) {
        final Collection<UserCommunication> communications = userDao.getUserCommunications(id);
        if (!communications.isEmpty()) {
            return this.ok(communications);
        } else {
            return noContent();
        }
    }

    @GET
    @Path("{id}/communications/{communication_id}")
    public Response getUserCommunicationInfo(@PathParam("id") int id, @PathParam("id") int communication_id) {
        final Collection<UserCommunication> communications = userDao.getUserCommunications(id);
        if (!communications.isEmpty()) {
            return this.ok(communications);
        } else {
            return noContent();
        }
    }

    @GET
    @Path("{userId}/communications/{communicationId}/messages")
    public Response getMessages(@PathParam("userId") int user_id, @PathParam("communicationId") int communication_id) {
        final Collection<UserMessage> messages = userDao.getUserMessages(user_id, communication_id);
        Comparator<UserMessage> comparator = new Comparator<UserMessage>() {
            @Override
            public int compare(UserMessage o1, UserMessage o2) {
                return o1.getDate().getMillis() > o2.getDate().getMillis() ? 1 : o1.getDate().getMillis() == o2.getDate().getMillis() ? 0 : -1;
            }
        };
        Collections.sort((ArrayList) messages, comparator);
        if (!messages.isEmpty()) {
            return this.ok(messages);
        } else {
            return noContent();
        }
    }
}