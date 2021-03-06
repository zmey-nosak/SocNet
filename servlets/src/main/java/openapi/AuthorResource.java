package openapi;

import dao.AuthorDao;
import dao.UserDao;
import listeners.Initializer;
import lombok.SneakyThrows;
import model.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("authors")

@Produces(APPLICATION_JSON)
public class AuthorResource implements JsonRestfulWebResource {

    private AuthorDao authorDao;
    private String REAL_PATH;
    private HttpServletRequest request;
    private final int DEFAULT_LIMIT = 100;
    private final int DEFAULT_OFFSET = 0;

    @Context
    public void init(ServletContext context, HttpServletRequest request) {
        authorDao = (AuthorDao) context.getAttribute(Initializer.AUTHOR_DAO);
        REAL_PATH = context.getRealPath("/");
        this.request = request;
    }


    //Получение списка всех пользователей
    @GET
    public Response getAll(@QueryParam("limit") int limit, @QueryParam("offset") int offset) {
        final model.Response<Author> authors = authorDao.getAll(limit == 0 ? DEFAULT_LIMIT : limit, offset == 0 ? DEFAULT_OFFSET : offset);
        return authors.getObjects().size() > 0 ? ok(authors) : noContent();
    }


   /*
    //Получение конкретного пользователя
    @GET
    @Path("{userId}")
    public Response getUser(@PathParam("userId") int userId) {
        final User user = userDao.getUser(userId);
        if (user != null) {
            return this.ok(user);
        } else {
            return noContent();
        }
    }
//Получение текущего пользователя
    @GET
    @Path("owner")
    public Response getUser() {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                UserInfo userInfo = userDao.getUserInfo(user.getUserId());
                if (userInfo != null) {
                    return this.ok(userInfo);
                }
            }
        }
        return noContent();
    }

    //Получение списка друзей пользователя
    @GET
    @Path("{userId}/friends")
    public Response getFriends(@PathParam("userId") int userId) {
        final Collection<User> users = userDao.getFriends(userId);
        if (users != null) {
            Collections.sort((ArrayList) users, ((o1, o2) -> {
                User u1 = (User) o1;
                User u2 = (User) o2;
                return u1.getSurname().compareTo(u2.getSurname()) > 0 ? 1 : u1.getSurname().equals(u2.getSurname()) ? 0 : -1;
            }));
            return this.ok(users);
        } else {
            return noContent();
        }
    }

    //Получение списка диалогов пользователя
    @GET
    @Path("{userId}/communications")
    public Response getCommunications(@PathParam("userId") int id) {
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

    //Получение аттрибутов диалога пользователя
    @GET
    @Path("{userId}/communications/{communication_id}")
    public Response getUserCommunicationInfo(@PathParam("userId") int id, @PathParam("communication_id") int communication_id) {
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

    //Получение списка сообщений диалога пользователя
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

    //Получение аттрибутов пользователя
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

    //Получение списка книг пользователя
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

    //Загрузка файла(изображения) пользователя
    @POST
    @Path("{userId}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetail,
                               @PathParam("userId") int user_id) {
        String path = saveToDisk(uploadedInputStream, fileDetail, user_id);
        userDao.updateUserPhoto(user_id + "\\" + fileDetail.getFileName(), user_id);
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

    //Обновление аттрибутов сообщения
    @POST
    @Path("/updateMessages")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMessages(String string) {
        return noContent();
    }

    //Добавленике книги для пользователя
    @POST
    @Path("/books/{bookId}")
    public Response addBook(@PathParam("bookId") int bookId) {

        HttpSession session = request.getSession();

        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                userDao.addBook(bookId, user.getUserId());
                return this.ok("Ok");
            }
        }
        return noContent();
    }

    //Удаление друга из списка друзей пользователя
    @DELETE
    @Path("/deleteFriend/{friendId}")
    public Response getAll(@PathParam("friendId") int friendId) {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                userDao.deleteFriend(user.getUserId(), friendId);
            }
        }
        return this.ok(null);
    }

    //Добавления друга в список друзей пользователя
    @POST
    @Path("/friends/add/{friendId}")
    public Response addFriend(@PathParam("friendId") int friendId) {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                userDao.addFriend(user.getUserId(), friendId);
            }
        }
        return this.ok(null);
    }

    //Получение списка заявок на добавление в список друзей пользователя
    @GET
    @Path("friendRequests")
    public Response getFriendRequests() {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                final Collection<FriendRequest> friendRequests = userDao.getFriendRequests(user.getUserId());
                if (friendRequests.size() > 0) {
                    return this.ok(friendRequests);
                } else {
                    return noContent();
                }
            }
        }
        return noContent();
    }

    //Получение списка собственных заявок в друзья пользователя
    @GET
    @Path("ownerRequests")
    public Response getOwnerRequests() {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                final Collection<Integer> ownerRequests = userDao.getOwnerRequests(user.getUserId());
                if (ownerRequests.size() > 0) {
                    return this.ok(ownerRequests);
                } else {
                    return noContent();
                }
            }
        }
        return noContent();
    }

    //Получение количества непрочитанных сообщений
    @GET
    @Path("unreadMessCnt")
    public Response getUnreadMessCnt() {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                final int messCnt = userDao.getUnreadMessCount(user.getUserId());
                if (messCnt > 0) {
                    return this.ok(messCnt);
                } else {
                    return noContent();
                }
            }
        }
        return noContent();
    }

    //Получение аттрибутов заявок в друзья
    @GET
    @Path("friendReqDetail")
    public Response getFriendReqDetail() {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                final Collection<User> users = userDao.getFriendReqDetail(user.getUserId());
                if (users.size() > 0) {
                    return this.ok(users);
                } else {
                    return noContent();
                }
            }
        }
        return noContent();
    }

    //Активация заявки в друзья
    @POST
    @Path("{ownerId}/friendship/{friendId}/activate")
    public Response activateFriendship(@PathParam("ownerId") int userId, @PathParam("friendId") int friendId) {
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null && user.getUserId() == userId) {
                userDao.activateFriendship(friendId, userId);
                return this.ok(null);
            }
            return noContent();
        }
        return noContent();
    }
*/
}