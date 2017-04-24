package annanas_manager.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.catalina.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@JsonFormat(shape=JsonFormat.Shape.STRING)
public enum UserRole {

    TEAMLEAD,
    DEVELOPER;

    public static UserRole fromValue(final String value) {
        if (value.equalsIgnoreCase("teamlead")){
            return TEAMLEAD;
        } else if (value.equalsIgnoreCase("developer")){
            return DEVELOPER;
        } else {
            return null;
        }
    }

    public static List<UserRole> getRoles() {
        List<UserRole> list = new ArrayList<>();
        for (UserRole type : UserRole.values()) {
            list.add(type);
        }
        return list;
    }

}
