package annanas_manager.entities.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.List;

@JsonFormat(shape=JsonFormat.Shape.STRING)
public enum TaskStatus {

    NEW,
    IN_PROGRESS,
    COMPLETE;

    public static TaskStatus fromValue(final String value) {
        if (value.equalsIgnoreCase("new")){
            return NEW;
        } else if (value.equalsIgnoreCase("in_progress")){
            return IN_PROGRESS;
        } else if (value.equalsIgnoreCase("complete")){
            return COMPLETE;
        } else {
            return null;
        }
    }

    public static List<TaskStatus> getAllStatus() {
        List<TaskStatus> list = new ArrayList<>();
        for (TaskStatus status : TaskStatus.values()) {
            list.add(status);
        }
        return list;
    }
}
