package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Created by Echetik on 04.11.2016.
 */
@Setter
@Getter
@EqualsAndHashCode
public class UserInfo {
    Collection<User> user_friends;
    Collection<Book> user_books;
    private String f_name;
    private String i_name;
    private LocalDate dob;
    private String photo;
    private Long id;

}
