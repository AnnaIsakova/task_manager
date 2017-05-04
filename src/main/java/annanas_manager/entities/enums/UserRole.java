package annanas_manager.entities.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.List;


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
