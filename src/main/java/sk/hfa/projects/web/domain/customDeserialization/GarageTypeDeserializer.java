package sk.hfa.projects.web.domain.customDeserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;


public class GarageTypeDeserializer extends StdDeserializer<Boolean> {

    public GarageTypeDeserializer() {
        this(null);
    }

    public GarageTypeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Boolean deserialize(
            JsonParser jsonparser, DeserializationContext context)
            throws IOException {
        return (jsonparser.getText().equals("Áno"));
    }
}