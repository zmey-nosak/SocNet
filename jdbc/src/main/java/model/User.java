package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long user_id;
    private String email;
    private String password;
    private String f_name;
    private String i_name;
    private String o_name;
    private LocalDate dob;
    private String photo;
    public User() {
    }
}
