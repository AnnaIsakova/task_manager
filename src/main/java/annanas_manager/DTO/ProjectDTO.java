package annanas_manager.DTO;


import annanas_manager.entities.CustomUser;

import java.util.Calendar;
import java.util.Date;

public class ProjectDTO {

    private long id;
    private String description;
    private String details;
    private CustomUserDTO createdBy;
    private Date createDate;
    private Calendar deadline;

    public ProjectDTO() {
    }

    public ProjectDTO(long id, String description, String details, CustomUserDTO createdBy, Date createDate, Calendar deadline) {
        this.id = id;
        this.description = description;
        this.details = details;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.deadline = deadline;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public CustomUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CustomUserDTO createdBy) {
        this.createdBy = createdBy;
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
        return "ProjectDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", details='" + details + '\'' +
                ", createdBy=" + createdBy +
                ", createDate=" + createDate +
                ", deadline=" + deadline +
                '}';
    }
}
