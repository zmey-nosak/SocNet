package model;

import com.fasterxml.jackson.annotation.*;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * Created by Echetik on 27.11.2016.
 */
@Setter
public class UserCommunication {
    private int communication_id;
    private String message;
    private int user_from;
    private String f_name;
    private String i_name;
    private String photo_src;
    private String ownerPhoto_src;
    private int active;
    private DateTime date;
    private int partner;

    public String getPhoto_src() {
        return photo_src;
    }

    public String getOwnerPhoto_src() {
        return ownerPhoto_src;
    }

    public int getCommunication_id() {
        return this.communication_id;
    }

    public String getMessage() {
        return this.message;
    }

    public int getUser_from() {
        return this.user_from;
    }

    public String getF_name() {
        return this.f_name;
    }

    public String getI_name() {
        return this.i_name;
    }

    public int getActive() {
        return this.active;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public DateTime getDate() {
        return this.date;
    }

    public int getPartner() {
        return this.partner;
    }
}
