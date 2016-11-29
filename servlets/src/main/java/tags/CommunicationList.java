package tags;

import lombok.Setter;
import lombok.SneakyThrows;
import model.User;
import model.UserCommunication;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Echetik on 28.11.2016.
 */
public class CommunicationList implements Printable {
    private Collection<UserCommunication> list;

    public CommunicationList(Collection<UserCommunication> list) {
        this.list = list;
    }

    public String getString() {
        StringBuffer out = new StringBuffer();
        for (UserCommunication communication : list)
            out.append("<tr class=tr><td width=\"60px\" height=\"60px\" valign=top>")
                    .append("<image src=data:image/jpg;base64,")
                    .append(communication.getPhoto())
                    .append(" width=\"100px\" height=\"150px\"/>")
                    .append("</td>")
                    .append("<td cellpadding=0 cellspacing=0  width=\"500px\" valign=\"top\">")
                    .append("<div> " + communication.getF_name() + " " + communication.getI_name())
                    .append("</div>")
                    .append("<div margin:\"0px\" float=left><image src=data:image/jpg;base64,")
                    .append(communication.getOwnerPhoto())
                    .append(" width=\"45px\" height=\"70px\" />")
                    .append("</div>")
                    .append("<div> " + communication.getMessage() + "</div>")
                    .append("</td>")
                    .append("</tr>");
        return out.toString();
    }
}
