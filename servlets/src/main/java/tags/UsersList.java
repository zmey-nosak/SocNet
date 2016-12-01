package tags;

import lombok.Setter;
import lombok.SneakyThrows;
import model.User;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Collection;

import static javax.servlet.jsp.tagext.Tag.SKIP_BODY;

/**
 * Created by Echetik on 12.11.2016.
 */
public class UsersList implements Printable {
    private Collection<User> list;

    public UsersList(Collection<User> list) {
        this.list = list;
    }

    public String getString() {
        StringBuffer out = new StringBuffer();
        out.append("<table padding=0px margin=0px border=\"1\">");
        for (User user : list) {
            out.append("<tr><td height=150 align=\"center\"><image src=data:image/jpg;base64,")
                    .append(user.getPhoto())
                    .append(" width=\"100\" height=\"150\"/>")
                    .append("</td>")
                    .append("<td><a href=\"/userpage/?user_id=")
                    .append(user.getUser_id()).append("\">")
                    .append(user.getF_name() + " " + user.getI_name())
                    .append("</a><br> <INPUT TYPE=button VALUE=\"Send Message\" ONCLICK=\"showParameters('" + user.getPhoto() + "',"
                            + user.getUser_id() + ",'"
                            + user.getF_name() + " "
                            + user.getI_name() + "');\">" +
                            "</td></tr>");
        }
        out.append("</table>");
        return out.toString();

    }
}
