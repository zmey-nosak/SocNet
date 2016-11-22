package tags;

import lombok.Setter;
import lombok.SneakyThrows;
import model.User;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Echetik on 12.11.2016.
 */
public class Users extends TagSupport {
    @Setter
    private Collection<User> users;

    @Override
    @SneakyThrows
    public int doStartTag() throws JspException {
        pageContext.getOut().print(userList(users));
        return SKIP_BODY;
    }

    public static String userList(Collection<User> users) throws IOException {
        StringBuffer out = new StringBuffer();
        for (User user : users)
            out.append("<tr><td height=150 align=\"center\"><image src=data:image/jpg;base64,")
                    .append(user.getPhoto())
                    .append(" width=\"100\" height=\"150\"/>")
                    .append("</td>")
                    .append("<td><a href=\"/userpage/?user_id=")
                    .append(user.getUser_id()).append("\">")
                    .append(user.getF_name() + " " + user.getI_name())
                    .append("</a><br> <INPUT TYPE=button VALUE=\"Send Message\" ONCLICK=\"showParameters('"+user.getPhoto()+"',"
                                                                                                        + user.getUser_id()+",'"
                                                                                                        + user.getF_name() + " "
                                                                                                        + user.getI_name() + "');\">" +
                            "</td></tr>");
        return out.toString();
    }
}
