package model;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * Created by Echetik on 01.12.2016.
 */
@Getter
@Setter
public class UserMessage {
        private String message;
        private long user_id;
        private String f_name;
        private String i_name;
        private String photo;
        private int active;
        private DateTime date;
}
