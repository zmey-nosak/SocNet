package tags;

import model.Book;
import model.User;
import model.UserInfo;

import java.util.Collection;

/**
 * Created by Echetik on 01.12.2016.
 */
public class BooksListMini implements Printable {
    private UserInfo userInfo;

    public BooksListMini(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getString() {
        StringBuffer out = new StringBuffer();
        int i = 1;
        Collection<Book> books = userInfo.getUser_books();
        int size = books.size();
        int cnt = size / 3;
        for (Book book : userInfo.getUser_books()) {
            if (i % cnt == 0) {
                out.append("<td width=50px align=center>");
            }
            out.append("<p><a href=#>" + book.getBook_name() + "</a></p></td>");

            if (i % cnt == 0) {
                out.append("</td>");
            }
            i++;
        }
        return out.toString();

    }
}