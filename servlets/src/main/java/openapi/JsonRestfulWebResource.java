package openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashMap;

interface JsonRestfulWebResource {

    ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @SneakyThrows
    default String toJson(Object o) {
        return writer.writeValueAsString(o);
    }

    default Response ok(Collection<?> objects) {
        return Response.ok(toJson(objects)).build();
    }

    default Response ok(Object o) {
        return Response.ok(toJson(o)).build();
    }

    default Response noContent() {
        return Response.noContent().build();
    }
}