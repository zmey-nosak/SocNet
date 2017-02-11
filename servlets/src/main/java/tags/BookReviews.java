package tags;

import model.Review;

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
        for (Review review : reviews) {
            out.append("<tr><td><img src=\"/files/").append(review.getPhotoSrc()).append("\" width=\"60\" height=\"100\"/><br>")
                    .append("<a href=/userpage?userId=").append(review.getUserId()).append(">").append(review.getSurname() + " " + review.getName()).append("</a><br>")
                    .append(review.getStringDate() + "</td>");
            out.append("<td>").append(review.getReview()).append("</td></tr>");
        }
        return out.toString();
    }
}
