package annanas_manager.entities;


import annanas_manager.DTO.CommentForTaskDTO;
import annanas_manager.DTO.FileForTaskDTO;
import annanas_manager.DTO.TaskForProjectDTO;
import annanas_manager.entities.enums.TaskPriority;
import annanas_manager.entities.enums.TaskStatus;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue(value = "P")
public class TaskForProject extends Task{

    @Lob
    @Column
    protected String details;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    protected Developer assignedTo;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    protected boolean approved;

    @OneToMany(mappedBy = "task", cascade= CascadeType.ALL)
    protected List<FileForTask> files = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade= CascadeType.ALL)
    protected List<CommentForTask> comments  = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "project_id")
    protected Project project;

    public TaskForProject(String description, TaskPriority priority, TaskStatus status, Date createDate, Calendar deadline, String details, Developer assignedTo, boolean approved) {
        super(description, priority, status, createDate, deadline);
        this.details = details;
        this.assignedTo = assignedTo;
        this.approved = approved;
    }

    public TaskForProject(String details, Developer assignedTo, boolean approved, Project project) {
        this.details = details;
        this.assignedTo = assignedTo;
        this.approved = approved;
        this.project = project;
    }

    public TaskForProject() {
    }

    @Override
    public TaskForProjectDTO toDTO() {
        TaskForProjectDTO taskDTO = new TaskForProjectDTO(
                id,
                description,
                priority,
                status,
                createDate,
                deadline,
                details,
                assignedTo.toDTO(),
                approved);
        try{
            List<FileForTaskDTO> filesDTO = new ArrayList<>();
            for (FileForTask file : this.files) {
                filesDTO.add(file.toDTO());
            }
            taskDTO.setFiles(filesDTO);
        } catch (NullPointerException ex){}

        try{
            List<CommentForTaskDTO> commentsDTO = new ArrayList<>();
            for (CommentForTask comment:this.comments) {
                commentsDTO.add(comment.toDTO());
            }
            taskDTO.setComments(commentsDTO);
        } catch (NullPointerException ex){}

        return taskDTO;
    }

    public static TaskForProject fromDTO(TaskForProjectDTO dto){
        return new TaskForProject(
                dto.getDescription(),
                dto.getPriority(),
                dto.getStatus(),
                dto.getCreateDate(),
                dto.getDeadline(),
                dto.getDetails(),
                Developer.fromDTO(dto.getAssignedTo()),
                dto.isApproved());
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Developer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Developer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public List<FileForTask> getFiles() {
        return files;
    }

    public void setFiles(List<FileForTask> files) {
        this.files = files;
    }

    public List<CommentForTask> getComments() {
        return comments;
    }

    public void setComments(List<CommentForTask> comments) {
        this.comments = comments;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
