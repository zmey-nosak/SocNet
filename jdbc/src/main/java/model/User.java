package model;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


@Setter
@AllArgsConstructor
public class User {
    private int user_id;
    private String email;
    private String password;
    private String f_name;
    private String i_name;
    private String o_name;
    private LocalDate dob;
    private String photo_src;

    public User() {
    }

    public int getUser_id() {
        return this.user_id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getF_name() {
        return this.f_name;
    }

    public String getI_name() {
        return this.i_name;
    }

    public String getO_name() {
        return this.o_name;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public String getDobString() {
        if (this.dob == null) return null;
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy");
        String str = dtf.print(this.dob);
        return str;
    }

    public String getPhoto_src() {
        return this.photo_src;
    }
}
