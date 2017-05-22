package annanas_manager.DTO;


import annanas_manager.entities.CustomUser;
import annanas_manager.entities.enums.TaskPriority;
import annanas_manager.entities.enums.TaskPriorityDeserializer;
import annanas_manager.entities.enums.TaskStatus;
import annanas_manager.entities.enums.TaskStatusDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Calendar;
import java.util.Date;

public class TaskDTO {

    protected long id;
    protected String description;
    protected TaskPriority priority;
    protected TaskStatus status;
    protected Date createDate;
    protected Calendar deadline;

    public TaskDTO(long id, String description, TaskPriority priority, TaskStatus status, Date createDate, Calendar deadline) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createDate = createDate;
        this.deadline = deadline;
    }

    public TaskDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("priority")
    public TaskPriority getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    @JsonDeserialize(using = TaskPriorityDeserializer.class)
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    @JsonProperty("status")
    public TaskStatus getStatus() {
        return status;
    }

    @JsonProperty("status")
    @JsonDeserialize(using = TaskStatusDeserializer.class)
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", createDate=" + createDate +
                ", deadline=" + deadline +
                '}';
    }
}
