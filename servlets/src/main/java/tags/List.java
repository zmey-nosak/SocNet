package tags;

import lombok.Setter;
import lombok.SneakyThrows;
import model.User;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Echetik on 28.11.2016.
 */
public class List<T> extends TagSupport {
    Printable printable;

    public List(Printable printable) {
        this.printable = printable;
    }

    public List() {
    }

    ;

    @SneakyThrows
    public int doStartTag() throws JspException {
        pageContext.getOut().print(printList(printable));
        return SKIP_BODY;
    }

    public static String printList(Printable printable) throws IOException {

        return printable.getString();
    }
}
