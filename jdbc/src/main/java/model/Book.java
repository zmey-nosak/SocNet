package model;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by Echetik on 26.10.2016.
 */
@Value
@AllArgsConstructor
public class Book {
    private final int book_id;
    private final String book_name;
    private final int pages_count;
    private final int author_id;
    private final String year_name;
    private final String image_src;
    private final int genre_id;
}
