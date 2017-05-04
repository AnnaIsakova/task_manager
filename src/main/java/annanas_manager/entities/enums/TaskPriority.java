package annanas_manager.entities.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.List;

@JsonFormat(shape=JsonFormat.Shape.STRING)
public enum TaskPriority {

    HIGH,
    NORMAL,
    LOW;

    public static TaskPriority fromValue(final String value) {
        if (value.equalsIgnoreCase("high")){
            return HIGH;
        } else if (value.equalsIgnoreCase("normal")){
            return NORMAL;
        } else if (value.equalsIgnoreCase("low")){
            return LOW;
        } else {
            return null;
        }
    }

    public static List<TaskPriority> getPriorities() {
        List<TaskPriority> list = new ArrayList<>();
        for (TaskPriority priority : TaskPriority.values()) {
            list.add(priority);
        }
        return list;
    }
}
