package tags;

import model.User;
import model.UserCommunication;
import model.UserMessage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Echetik on 01.12.2016.
 */
public class MessageList implements Printable {
    private Collection<UserMessage> list;
    public final static String CSS = "<link rel=\"stylesheet\" href=\"/list_messages.css\">";
    public final static String JS = "<script src=\"/scripts/MessageList.js\"></script><script src=\"/scripts/TechnoMessage.js\"></script>";

    private long communication_id;
    private User partner;
    private User owner;

    public MessageList(Collection<UserMessage> list, long communication_id, User owner, User partner) {
        this.list = list;
        this.communication_id = communication_id;
        this.partner = partner;
        this.owner = owner;
    }

    protected User getUser(long user_id) {
        if (owner.getUser_id() == user_id) {
            return owner;
        } else {
            return partner;
        }
    }

    public String getPrintObject() {
        return "new MessageList(" + communication_id + "," + owner.getUser_id() + "," + partner.getUser_id() + ")";
    }

    public String getString() {

        StringBuffer out = new StringBuffer();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("H:mm");
        DateTimeFormatter formatterDate = DateTimeFormat.forPattern("dd.MM.yyyy");
        DateTimeFormatter formatterDate2 = DateTimeFormat.forPattern("dd.MM.yyyy H:mm");
        Comparator<UserMessage> comparator = new Comparator<UserMessage>() {
            @Override
            public int compare(UserMessage o1, UserMessage o2) {
                return o1.getDate().getMillis() > o2.getDate().getMillis() ? 1 : o1.getDate().getMillis() == o2.getDate().getMillis() ? 0 : -1;
            }
        };
        Collections.sort((ArrayList<UserMessage>) list, comparator);
        long user_id = 0;
        String dt = "";
        boolean b = false;
        out.append("<div class=prokrutka id=" + getResponseElementName() + ">");
        User user = null;
        for (UserMessage userMessage : list) {
            user = getUser(userMessage.getUser_id());
            if (userMessage.getGroupNum() == 1) {
                out.append("<div class=dateheader align=center>" + formatterDate.print(userMessage.getDate()) + "</div>");
            }
            out.append("<div class=container>");
            if (user_id == userMessage.getUser_id() && dt.equals(formatterDate2.print(userMessage.getDate()))) {
                b = false;
            } else {
                out.append("<div class=mainImage><image src=data:image/jpg;base64,")
                        .append(user.getPhoto())
                        .append(" width=\"30px\" height=\"40px\"></div>");
                b = true;
            }
            out.append("<div class=head>");
            if (b) {
                out.append(user.getI_name() + " ")
                        .append(formatter.print(userMessage.getDate()));
            }
            out.append("</div>")
                    .append("<div class=message>")
                    .append(userMessage.getMessage())
                    .append("</div>")
                    .append("</div>");
            user_id = userMessage.getUser_id();
            dt = formatterDate2.print(userMessage.getDate());
        }
        out.append("</div>");
        return out.toString();
    }

}
