package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

/**
 * Created by Echetik on 26.10.2016.
 */
@Getter
@AllArgsConstructor
@Setter
public class Book {
    private int book_id;
    private String book_name;
    private int pages_count;
    private int author_id;
    private String year_name;
    private String image_src;
    private int genre_id;
    public Book() {
    }

    ;
}
