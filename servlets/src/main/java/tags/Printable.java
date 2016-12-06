package tags;

import java.util.Collection;

/**
 * Created by Echetik on 29.11.2016.
 */
public interface Printable {
    String getString();

    String getPrintObject();

    default String getEventElementName() {
        return "start_sending";
    }

    default String getContentElementName() {
        return "content_element";
    }

    default String getResponseElementName() {
        return "response_element";
    }
}
