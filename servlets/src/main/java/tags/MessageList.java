package tags;

import model.UserCommunication;
import model.UserMessage;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collection;

/**
 * Created by Echetik on 01.12.2016.
 */
public class MessageList implements Printable {
    private Collection<UserMessage> list;

    public MessageList(Collection<UserMessage> list) {
        this.list = list;
    }

    public String getString() {
        StringBuffer out = new StringBuffer();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("H:mm");
        for (UserMessage userMessage : list)
            out.append("<div id=container>")
                    .append("<div id=mainImage><image src=data:image/jpg;base64,")
                    .append(userMessage.getPhoto())
                    .append(" width=\"40px\" height=\"50px\"></div>")
                    .append("<div id=head>")
                    .append(userMessage.getI_name() + " (")
                    .append(formatter.print(userMessage.getDate())).append(")</div>")
                    .append("<div id=message><a href=#>")
                    .append(userMessage.getMessage())
                    .append("</a></div>")
                    .append("</div>");
        return out.toString();
    }
}
