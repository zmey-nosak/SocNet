package tags;

import lombok.Setter;
import lombok.SneakyThrows;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Echetik on 15.01.2017.
 */
@Setter
public class MenuTag extends TagSupport {
    int userId;
    ResourceBundle textContent;
    private Locale rusLocale = new Locale("ru", "RU");

    public MenuTag(int userId) {
        this.userId = userId;
        init();
    }

    private void init() {
        if (Locale.getDefault().equals(Locale.US)) {
            textContent = ResourceBundle.getBundle("TextContent", Locale.US);
        } else if (Locale.getDefault().equals(rusLocale)) {
            textContent = ResourceBundle.getBundle("TextContent", Locale.getDefault());
        }
    }


    public MenuTag() {
        init();
    }


    @SneakyThrows
    public int doStartTag() {
        StringBuffer sb = new StringBuffer("");
        sb.append("<table class=\"table\"><tr><td valign=\"top\" height=\"15\" align=right><a class=\"a\" href=\"/logout\">")
                .append(textContent.getString("exit"))
                .append("</a></td></tr><tr>")
                .append("<td height=\"80\" align=\"left\" valign=\"top\" padding=\"0\">")
                .append("<ul class=\"mmenuu\"><li><a href=\"/userpage?userId=")
                .append(this.userId + "\">")
                .append(textContent.getString("myPage"))
                .append("</a></li><li><a href =#>")
                .append(textContent.getString("books"))
                .append("</a><ul class=\"ssubmenuu\"><li><a href = \"/authors/\">")
                .append(textContent.getString("byAuthors"))
                .append("</a></li><li><a href = \"/genres/\">")
                .append(textContent.getString("byGenres"))
                .append("</a></li></ul></li><li><a href =#>")
                .append(textContent.getString("libraries"))
                .append("</a><ul class=\"ssubmenuu\"><li><a href =\"#\">")
                .append(textContent.getString("online"))
                .append("</a></li><li><a href =\"#\">")
                .append(textContent.getString("city"))
                .append("</a></li></ul></li><li><a href =\"#\">")
                .append(textContent.getString("reviews"))
                .append("</a><ul class=\"ssubmenuu\"><li ><a href =#>")
                .append(textContent.getString("byAuthors"))
                .append("</a></li><li><a href =\"#\">")
                .append(textContent.getString("byBooks"))
                .append("</a></li></ul></li><li><a href =#>")
                .append(textContent.getString("newBooks"))
                .append("</a></li></ul></td></tr></table>");
        pageContext.getOut().print(sb.toString());
        return SKIP_BODY;
    }
}
