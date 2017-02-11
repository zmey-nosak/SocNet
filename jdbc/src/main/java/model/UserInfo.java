package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;

import java.util.Collection;

/**
 * Created by Echetik on 04.11.2016.
 */
@Setter
@Getter
@EqualsAndHashCode
public class UserInfo {
    Collection<User> userFriends;
    Collection<Book> userBooks;
    User user;
    private String surname;
    private String name;
    private LocalDate dateOfBirth;
    private String photoSrc;
    private int userId;

}
