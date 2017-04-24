package annanas_manager.entities.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

public class UserRoleDeserializer extends JsonDeserializer{
    @Override
    public UserRole deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        return UserRole.fromValue(jp.getValueAsString());
    }
}
