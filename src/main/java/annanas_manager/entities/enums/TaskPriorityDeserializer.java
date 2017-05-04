package annanas_manager.entities.enums;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class TaskPriorityDeserializer extends JsonDeserializer {
    @Override
    public TaskPriority deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        return TaskPriority.fromValue(jp.getValueAsString());
    }
}
