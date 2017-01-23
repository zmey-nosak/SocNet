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
    private final int author_id;
    private final String f_name;
    private final String i_name;
    private final String o_name;
    private final LocalDate dob;
    private final int country_id;

    public int getAuthor_id() {
        return this.author_id;
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
    public String getStringDob() {
        if (this.dob == null) return null;
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy");
        String str = dtf.print(this.dob);
        return str;
    }
    public int getCountry_id() {
        return this.country_id;
    }
}