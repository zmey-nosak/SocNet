package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * Created by Echetik on 01.12.2016.
 */
@Setter
public class UserMessage {
    private String message;
    private int user_id;
    private int active;
    private DateTime date;
    private int groupNum;

    public int getId() {
        return id;
    }

    private int id;

    public String getMessage() {
        return this.message;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public int getActive() {
        return this.active;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public DateTime getDate() {
        return this.date;
    }

    public int getGroupNum() {
        return this.groupNum;
    }
}
