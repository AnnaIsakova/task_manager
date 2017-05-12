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
public class Task {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    @Column(name = "id", length = 6, nullable = false)
    private long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private CustomUser createdBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(nullable = false)
    @Type(type="timestamp")
    private Date createDate;

    @Column(name = "deadline", nullable = false)
    private Calendar deadline;

    public Task(String description, TaskPriority priority, TaskStatus status, Date createDate, Calendar deadline) {
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createDate = createDate;
        this.deadline = deadline;
    }

    public Task() {}

    public TaskDTO toDTO() {
        return new TaskDTO(id, description, createdBy.toDTO(), priority, status, createDate, deadline);
    }

    public static Task fromDTO(TaskDTO dto) {
        return new Task(
                dto.getDescription(),
                dto.getPriority(),
                dto.getStatus(),
                dto.getCreateDate(),
                dto.getDeadline());
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", createdBy=" + createdBy +
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

    public CustomUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CustomUser createdBy) {
        this.createdBy = createdBy;
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
