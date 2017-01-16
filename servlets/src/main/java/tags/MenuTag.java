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
    int user_id;
    ResourceBundle textContent;
    private Locale rusLocale = new Locale("ru", "RU");

    public MenuTag(int userId) {
        this.user_id = userId;
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
        String str = "<table class=\"table\">" +
                "<tr><td valign=\"top\" height=\"15\" align=right><a class=\"a\" href=\"/logout\">Выход</a></td></tr><tr>" +
                "<td height=\"80\" align=\"left\" valign=\"top\" padding=\"0\">" +
                "<ul class=\"mmenuu\"><li><a href=\"/userpage?userId=" + this.user_id + "\">" + textContent.getString("myPage") + "</a></li>" +
                "<li><a href =#>Книги</a><ul class=\"ssubmenuu\">" +
                "<li><a href = \"/authors/\">По авторам</a></li>" +
                "<li><a href = \"/genres/\">По жанрам</a></li></ul>" +
                "</li><li><a href =#>Библиотеки</a><ul class=\"ssubmenuu\">" +
                "<li><a href =\"#\">Онлайн</a></li>" +
                "<li><a href =\"#\">В городе</a></li>" +
                "</ul></li><li><a href =\"#\">Рецензии</a>" +
                "<ul class=\"ssubmenuu\"><li ><a href =#>По авторам</a></li>" +
                "<li><a href =\"#\"> По книгам</a></li>" +
                "</ul></li><li><a href =#>Книжные новинки</a></li>" +
                "</ul></td></tr></table>" ;
        byte[] bytes = str.getBytes();
        String utf = new String(bytes, "UTF-8");
        pageContext.getOut().print(utf);
        return SKIP_BODY;
    }
}
