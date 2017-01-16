package tags;

import model.Book;
import model.Review;
import model.UserInfo;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.nio.charset.Charset;
import java.util.Collection;

/**
 * Created by Echetik on 25.12.2016.
 */
public class BookReviews implements Printable {
    private Collection<Review> reviews;

    public BookReviews(Collection<Review> reviews) {
        this.reviews = reviews;
    }

    public String getString() {
        StringBuffer out = new StringBuffer();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm");
        for (Review review : reviews) {
            out.append("<tr><td><img src=\"/files/").append(review.getPhoto_src()).append("\" width=\"60\" height=\"100\"/><br>")
                    .append("<a href=/userpage?userId=").append(review.getUser_id()).append(">").append(review.getF_name() + " " + review.getI_name()).append("</a><br>")
                    .append(review.getDate().toString(dtf) + "</td>");
            out.append("<td>").append(review.getReview()).append("</td></tr>");
        }
        return out.toString();
    }

    @Override
    public String getScript() {
        return null;
    }

    @Override
    public String getPrintObject() {
        return null;
    }
}
