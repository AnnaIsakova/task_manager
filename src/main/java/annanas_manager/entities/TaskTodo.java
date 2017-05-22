package annanas_manager.entities;


import annanas_manager.DTO.DeveloperDTO;
import annanas_manager.DTO.TaskTodoDTO;
import annanas_manager.DTO.TeamleadDTO;
import annanas_manager.entities.enums.TaskPriority;
import annanas_manager.entities.enums.TaskStatus;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Calendar;
import java.util.Date;

@Entity
@DiscriminatorValue(value = "T")
public class TaskTodo extends Task{

    @ManyToOne
    @JoinColumn(name = "user_id")
    protected CustomUser createdBy;

    public TaskTodo(String description, TaskPriority priority, TaskStatus status, Date createDate, Calendar deadline, CustomUser createdBy) {
        super(description, priority, status, createDate, deadline);
        this.createdBy = createdBy;
    }

    public TaskTodo() {
    }

    @Override
    public TaskTodoDTO toDTO() {
        return new TaskTodoDTO(
                id,
                description,
                priority,
                status,
                createDate,
                deadline,
                createdBy.toDTO());
    }

    public static TaskTodo fromDTO(TaskTodoDTO dto){
        CustomUser user = null;
        if (dto.getCreatedBy() instanceof DeveloperDTO){
            user = Developer.fromDTO(dto.getCreatedBy());
        } else if (dto.getCreatedBy() instanceof TeamleadDTO){
            user = Teamlead.fromDTO(dto.getCreatedBy());
        }
        return new TaskTodo(dto.getDescription(), dto.getPriority(), dto.getStatus(), dto.getCreateDate(), dto.getDeadline(), user);
    }

    public CustomUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CustomUser createdBy) {
        this.createdBy = createdBy;
    }
}
