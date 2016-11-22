package model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

/**
 * Created by Echetik on 26.10.2016.
 */
@Value
@AllArgsConstructor
public class Author {
    private final long author_id;
    private final String f_name;
    private final String i_name;
    private final String o_name;
    private final LocalDate dob;
    private final long country_id;
}