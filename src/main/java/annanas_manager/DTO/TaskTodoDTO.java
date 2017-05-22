package annanas_manager.DTO;


import annanas_manager.entities.enums.TaskPriority;
import annanas_manager.entities.enums.TaskStatus;

import java.util.Calendar;
import java.util.Date;

public class TaskTodoDTO extends TaskDTO{

    protected CustomUserDTO createdBy;

    public TaskTodoDTO(long id, String description, TaskPriority priority, TaskStatus status, Date createDate, Calendar deadline, CustomUserDTO createdBy) {
        super(id, description, priority, status, createDate, deadline);
        this.createdBy = createdBy;
    }

    public TaskTodoDTO() {
    }

    public CustomUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CustomUserDTO createdBy) {
        this.createdBy = createdBy;
    }
}
