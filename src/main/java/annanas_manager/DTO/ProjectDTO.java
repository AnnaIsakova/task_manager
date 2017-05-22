package annanas_manager.DTO;


import annanas_manager.entities.CommentForProject;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Developer;
import annanas_manager.entities.FileForProject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProjectDTO {

    private long id;
    private String name;
    private String description;
    private String details;
    private CustomUserDTO createdBy;
    private Date createDate;
    private Calendar deadline;
    private List<DeveloperDTO> developers;
    private List<FileForProjectDTO> files;
    private List<CommentForProjectDTO> comments;
    private List<TaskForProjectDTO> tasks;

    public ProjectDTO() {
    }

    public ProjectDTO(long id,
                      String name,
                      String description,
                      String details,
                      CustomUserDTO createdBy,
                      Date createDate,
                      Calendar deadline,
                      List<DeveloperDTO> developers,
                      List<FileForProjectDTO> files,
                      List<CommentForProjectDTO> comments,
                      List<TaskForProjectDTO> tasks) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.details = details;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.deadline = deadline;
        this.developers = developers;
        this.files = files;
        this.comments = comments;
        this.tasks = tasks;
    }

    public ProjectDTO(long id, String name, String description, String details, CustomUserDTO createdBy, Date createDate, Calendar deadline) {
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<DeveloperDTO> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<DeveloperDTO> developers) {
        this.developers = developers;
    }

    public List<FileForProjectDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileForProjectDTO> files) {
        this.files = files;
    }

    public List<CommentForProjectDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentForProjectDTO> comments) {
        this.comments = comments;
    }

    public List<TaskForProjectDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskForProjectDTO> tasks) {
        this.tasks = tasks;
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
