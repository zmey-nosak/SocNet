package model;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by Echetik on 26.10.2016.
 */
@Value
@AllArgsConstructor
public class Book {
    private final long book_id;
    private final String book_name;
    private final int pages_count;
    private final long author_id;
    private final String year_name;
    private final String image_src;
    private final long genre_id;
}
