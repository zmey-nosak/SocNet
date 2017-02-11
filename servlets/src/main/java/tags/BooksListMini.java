package tags;

import model.Book;
import model.UserInfo;

import java.nio.charset.Charset;
import java.util.Collection;

/**
 * Created by Echetik on 25.12.2016.
 */
public class BooksListMini implements Printable {
    private UserInfo userInfo;
    private Collection<Book> books;

    public BooksListMini(UserInfo userInfo, Collection<Book> books) {
        this.userInfo = userInfo;
        this.books = books;
    }

    public String getString() {
        StringBuffer out = new StringBuffer();
        for (Book book : books) {
            out.append("<tr><td><a href=\"/books?bookId=").append(book.getBookId()).append("\">")
                    .append(book.getBookName()).append("</a></td>");
            out.append("<td><img src=\"").append(book.getImageSrc()).append("\" width=\"60\" height=\"100\"></td>");
            out.append("<td><button onclick=\"addBook(").append(book.getBookId()).append(")\"");
            out.append("<td>").append(new String("Добавить к себе".getBytes(), Charset.forName("utf-8"))).append("</button>");
            out.append("</td></tr>");
        }
        return out.toString();

    }
}
