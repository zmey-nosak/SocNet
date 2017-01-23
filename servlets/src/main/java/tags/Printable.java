package tags;

import java.util.Collection;

/**
 * Created by Echetik on 29.11.2016.
 */
//Интерфейс для унификации поведения подключения JS скриптов
public interface Printable {
    default String getString(){return null;}

    default String getScript(){return null;}

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
