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
    private int userId;
    private int communicationId;
    private int partnerId;

    public CommunicationList(int userId) {
        this.userId = userId;
    }

    public CommunicationList(int userId, int communicationId, int partnerId) {
        this.userId = userId;
        this.communicationId = communicationId;
        this.partnerId = partnerId;
    }

    public String getString() {
        return null;
    }


    @Override
    public String getScript() {
        if (communicationId != 0 && partnerId != 0) {
            return "messageList = new MessageList(socket," + communicationId + "," + this.userId + "," + this.partnerId + ");" ;
        }
        return "communicationList = new CommunicationList(" + this.userId + ");" ;
    }

}