package tags;

import model.User;
import model.UserInfo;

import java.util.Collection;

/**
 * Created by Echetik on 01.12.2016.
 */
public class UsersListMini implements Printable {
    private UserInfo userInfo;

    public UsersListMini(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getString() {
        StringBuffer out = new StringBuffer();
        int i = 1;
        for (User user : userInfo.getUser_friends()) {
            if (i % 3 == 0) {
                out.append("<tr>");
            }
            out.append("<td width=60px style=\"border:#0000FF solid 1px\" valign=top align=center>" +
                    "       <img width=60px height=60px src=data:image/jpg;base64,"
                    + user.getPhoto() + ">" +
                    "<div><a class=\"link\" href=/userpage?user_id=" + user.getUser_id()
                    + ">" + user.getF_name()
                    + " " + user.getI_name() + "</a></div></td>");
            if (i % 3 == 0) {
                out.append("</tr>");
            }
            i++;
        }
        return out.toString();

    }
}
