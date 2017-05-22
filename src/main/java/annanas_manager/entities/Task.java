package annanas_manager.entities;


import annanas_manager.DTO.TaskDTO;
import annanas_manager.entities.enums.TaskPriority;
import annanas_manager.entities.enums.TaskStatus;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "tasks")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "task_role")
public abstract class Task {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    @Column(name = "id", length = 6, nullable = false)
    protected long id;

    @Column(name = "description", nullable = false)
    protected String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected TaskPriority priority;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected TaskStatus status;

    @Column(nullable = false)
    @Type(type="timestamp")
    protected Date createDate;

    @Column(name = "deadline", nullable = false)
    protected Calendar deadline;

    public Task(String description, TaskPriority priority, TaskStatus status, Date createDate, Calendar deadline) {
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createDate = createDate;
        this.deadline = deadline;
    }

    public Task() {}

    public TaskDTO toDTO() {
        return new TaskDTO(id, description, priority, status, createDate, deadline);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", createDate=" + createDate +
                ", deadline=" + deadline +
                '}';
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

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

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
}
