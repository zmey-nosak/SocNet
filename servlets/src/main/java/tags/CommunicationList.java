package tags;

import lombok.Setter;
import lombok.SneakyThrows;
import model.User;
import model.UserCommunication;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Echetik on 28.11.2016.
 */
public class CommunicationList implements Printable {
    private Collection<UserCommunication> list;
    public static String CSS = "<link rel=\"stylesheet\" href=\"/lists.css\">";
    public static String JS = "<script src=\"/scripts/Users.js\"></script>";
    public CommunicationList(Collection<UserCommunication> list) {
        this.list = list;
    }

    public String getString() {
        StringBuffer out = new StringBuffer();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
        for (UserCommunication communication : list)
            out.append("<div class=container>")
                    .append("<div class=mainImage><image src=data:image/jpg;base64,")
                    .append(communication.getPhoto())
                    .append(" width=\"40px\" height=\"50px\"></div>")
                    .append("<div class=head>")
                    .append(communication.getF_name() + " " + communication.getI_name() + " (")
                    .append(formatter.print(communication.getDate())).append(")</div>")
                    .append("<div class=childImage>")
                    .append("<image src=data:image/jpg;base64,")
                    .append(communication.getOwnerPhoto())
                    .append(" width=\"20px\" height=\"25px\"></div>")
                    .append("<div class=message><a href=/list/messages?communication_id=" + communication.getCommunication_id() + " >")
                    .append(communication.getMessage())
                    .append("</a></div>")
                    .append("</div>");
        return out.toString();
    }

    @Override
    public String getPrintObject() {
        return null;
    }
}