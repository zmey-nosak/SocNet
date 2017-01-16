package openapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.JSONPObject;
import dao.UserDao;
import jdk.nashorn.internal.parser.JSONParser;
import listeners.Initializer;
import lombok.SneakyThrows;
import model.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.JSONP;
import org.h2.store.fs.FileUtils;
import org.joda.time.DateTime;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.*;
import javax.ws.rs.*;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("users")

@Produces(APPLICATION_JSON)
public class UserResource implements JsonRestfulWebResource {

    private UserDao userDao;
    private String REAL_PATH;
    private HttpServletRequest request;

    @Context
    public void init(ServletContext context, HttpServletRequest request) {
        userDao = (UserDao) context.getAttribute(Initializer.USER_DAO);
        REAL_PATH = context.getRealPath("/");
        this.request = request;
    }


    @GET
    public Response getAll() {
        final Collection<model.User> users = userDao.getAllUsers();
        return users.size() > 0 ? ok(users) : noContent();
    }

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") int id) {
        final User user = userDao.getUser(id);
        if (user != null) {
            return this.ok(user);
        } else {
            return noContent();
        }
    }

    @GET
    @Path("owner")
    public Response getUser() {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                UserInfo userInfo = userDao.getUserInfo(user.getUser_id());
                if (userInfo != null) {
                    return this.ok(userInfo);
                }
            }
        }
        return noContent();
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
        Comparator<UserCommunication> comparator = new Comparator<UserCommunication>() {
            @Override
            public int compare(UserCommunication o1, UserCommunication o2) {
                return o1.getDate().getMillis() > o2.getDate().getMillis() ? 1 : o1.getDate().getMillis() == o2.getDate().getMillis() ? 0 : -1;
            }
        };
        Collections.sort((ArrayList) communications, comparator);
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
        Comparator<UserCommunication> comparator = new Comparator<UserCommunication>() {
            @Override
            public int compare(UserCommunication o1, UserCommunication o2) {
                return o1.getDate().getMillis() > o2.getDate().getMillis() ? 1 : o1.getDate().getMillis() == o2.getDate().getMillis() ? 0 : -1;
            }
        };
        Collections.sort((ArrayList) communications, comparator);
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


    @GET
    @Path("{userId}/userInfo")
    public Response getUserInfo(@PathParam("userId") int user_id) {
        final UserInfo userInfo = userDao.getUserInfo(user_id);
        if (userInfo != null) {
            return this.ok(userInfo);
        } else {
            return noContent();
        }
    }

    @GET
    @Path("{userId}/books")
    public Response getUserBooks(@PathParam("userId") int user_id) {
        final Collection<Integer> books = userDao.getUserBooks(user_id);
        if (books != null) {
            return this.ok(books);
        } else {
            return noContent();
        }
    }


    @POST
    @Path("{userId}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetail,
                               @PathParam("userId") int user_id) {
        String path = saveToDisk(uploadedInputStream, fileDetail, user_id);
        userDao.updatePhoto(user_id + "\\" + fileDetail.getFileName(), user_id);
        return noContent();
    }

    @SneakyThrows
    private String saveToDisk(InputStream uploadedInputStream, FormDataContentDisposition fileDetail, int user_id) {
        String path = "D://user_images//" + user_id;
        new File(path).mkdir();
        String uploadedFileLocation = path + "//" + fileDetail.getFileName();
        try (OutputStream out = new FileOutputStream(new File(uploadedFileLocation))) {
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        }
        return uploadedFileLocation;
    }


    @POST
    @Path("/updateMessages")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMessages(String string) {
        return noContent();
    }

    @POST
    @Path("/books/{bookId}")
    public Response addBook(@PathParam("bookId") int bookId) {

        HttpSession session = request.getSession();

        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                userDao.addBook(bookId, user.getUser_id());
                return this.ok("Ok");
            }
        }
        return noContent();
    }


    @DELETE
    @Path("/deleteFriend/{friendId}")
    public Response getAll(@PathParam("friendId") int friendId) {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                userDao.deleteFriend(user.getUser_id(), friendId);
            }
        }
        return this.ok(null);
    }

    @POST
    @Path("/friends/add/{friendId}")
    public Response addFriend(@PathParam("friendId") int friendId) {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                userDao.addFriend(user.getUser_id(), friendId);
            }
        }
        return this.ok(null);
    }

    @GET
    @Path("friendRequests")
    public Response getFriendRequests() {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                final Collection<FriendRequest> friendRequests = userDao.getFriendRequests(user.getUser_id());
                if (friendRequests.size() > 0) {
                    return this.ok(friendRequests);
                } else {
                    return noContent();
                }
            }
        }
        return noContent();
    }

    @GET
    @Path("ownerRequests")
    public Response getOwnerRequests() {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                final Collection<Integer> ownerRequests = userDao.getOwnerRequests(user.getUser_id());
                if (ownerRequests.size() > 0) {
                    return this.ok(ownerRequests);
                } else {
                    return noContent();
                }
            }
        }
        return noContent();
    }

    @GET
    @Path("unreadMessCnt")
    public Response getUnreadMessCnt() {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                final int messCnt = userDao.getUnreadMessCount(user.getUser_id());
                if (messCnt > 0) {
                    return this.ok(messCnt);
                } else {
                    return noContent();
                }
            }
        }
        return noContent();
    }

    @GET
    @Path("friendReqDetail")
    public Response getFriendReqDetail() {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                final Collection<User> users = userDao.getFriendReqDetail(user.getUser_id());
                if (users.size() > 0) {
                    return this.ok(users);
                } else {
                    return noContent();
                }
            }
        }
        return noContent();
    }

    @POST
    @Path("{ownerId}/friendship/{friendId}/activate")
    public Response activateFriendship(@PathParam("ownerId") int userId, @PathParam("friendId") int friendId) {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null && user.getUser_id() == userId) {
                userDao.activateFriendship(friendId, userId);
                return this.ok(null);
            }
            return noContent();
        }
        return noContent();
    }

}