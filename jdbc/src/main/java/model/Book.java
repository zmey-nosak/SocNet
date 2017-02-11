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
    private int bookId;
    private String bookName;
    private int pagesCount;
    private int authorId;
    private String yearName;
    private String imageSrc;
    private int genreId;
    public Book() {
    }

    ;
}
