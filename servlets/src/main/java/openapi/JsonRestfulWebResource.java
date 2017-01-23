package openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import lombok.SneakyThrows;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.util.Collection;

interface JsonRestfulWebResource {

    //Сериализация объекта в JSON
    @SneakyThrows
    default String toJson(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writer().writeValueAsString(o);
    }
    //Формирование ответа на запрос в виде коллекции
    default Response ok(Collection<?> objects) {
        return Response.ok(toJson(objects)).build();
    }
    //Формирование ответа на запрос в виде объекта
    default Response ok(Object o) {
        return Response.ok(toJson(o)).build();
    }

    //Формирование пустого ответа на запрос
    default Response noContent() {
        return Response.noContent().build();
    }

}