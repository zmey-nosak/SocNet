package openapi;

import controllers.User;
import dao.UserDao;
import listeners.Initializer;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Collection;

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
}