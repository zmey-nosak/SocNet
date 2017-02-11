package model;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Echetik on 26.10.2016.
 */
@Value
@AllArgsConstructor
public class Author {
    private final int authorId;
    private final String surname;
    private final String name;
    private final LocalDate dob;
    private final int countryId;

    public int getAuthorId() {
        return this.authorId;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getName() {
        return this.name;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public String getStringDob() {
        if (this.dob == null) return null;
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy");
        String str = dtf.print(this.dob);
        return str;
    }

    public int getCountryId() {
        return this.countryId;
    }
}