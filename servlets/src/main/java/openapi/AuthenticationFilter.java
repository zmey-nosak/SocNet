package openapi;

import dao.UserDao;
import listeners.Initializer;
import lombok.SneakyThrows;

import javax.annotation.Priority;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by Echetik on 18.12.2016.
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;

    @Override
    @SneakyThrows
    public void filter(ContainerRequestContext requestContext) throws IOException {
        HttpSession s = request.getSession();
        if (s == null) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());

        }
    }
}

