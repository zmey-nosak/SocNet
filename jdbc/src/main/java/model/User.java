package model;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


@Setter
@AllArgsConstructor
public class User {
    private int userId;
    private String email;
    private String password;
    private String surname;
    private String name;
    private LocalDate dateOfBirth;
    private String photoSrc;

    public User() {}

    public int getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getName() {
        return this.name;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public String getDobString() {
        if (this.dateOfBirth == null) return null;
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy");
        String str = dtf.print(this.dateOfBirth);
        return str;
    }

    public String getPhotoSrc() {
        return this.photoSrc;
    }
}
