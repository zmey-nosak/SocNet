package model;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.sql.Date;

/**
 * Created by Echetik on 27.11.2016.
 */
@Getter
@Setter
public class UserCommunication {
    private long communication_id;
    private String message;
    private long user_from;
    private String f_name;
    private String i_name;
    private String photo;
    private String ownerPhoto;
    private int active;
    private DateTime date;
}
