package model;

import com.fasterxml.jackson.annotation.*;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * Created by Echetik on 27.11.2016.
 */
@Setter
public class UserCommunication {
    private int communicationId;
    private String message;
    private int userIdFrom;
    private String surname;
    private String name;
    private String photoSrc;
    private String ownerPhotoSrc;
    private int active;
    private DateTime date;
    private int partner;

    public String getPhotoSrc() {
        return photoSrc;
    }

    public String getOwnerPhotoSrc() {
        return ownerPhotoSrc;
    }

    public int getCommunicationId() {
        return this.communicationId;
    }

    public String getMessage() {
        return this.message;
    }

    public int getUserIdFrom() {
        return this.userIdFrom;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getName() {
        return this.name;
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
