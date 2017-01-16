package tags;

import java.util.Collection;

/**
 * Created by Echetik on 29.11.2016.
 */
public interface Printable {
    String getString();

    String getScript();

    default String getPrintObject() {
        return "array" ;
    }

    default String getEventElementName() {
        return "start_sending" ;
    }

    default String getContentElementName() {
        return "content_element" ;
    }

    default String getResponseElementName() {
        return "response_element" ;
    }

    default String getArray() {
        return "var array=[];" ;
    }
}
