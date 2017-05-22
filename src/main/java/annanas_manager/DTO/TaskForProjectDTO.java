package annanas_manager.DTO;


import annanas_manager.entities.CommentForTask;
import annanas_manager.entities.Developer;
import annanas_manager.entities.FileForTask;
import annanas_manager.entities.Project;
import annanas_manager.entities.enums.TaskPriority;
import annanas_manager.entities.enums.TaskStatus;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskForProjectDTO extends TaskDTO{

    protected String details;
    protected DeveloperDTO assignedTo;
    protected boolean approved;
    protected List<FileForTaskDTO> files;
    protected List<CommentForTaskDTO> comments;

    public TaskForProjectDTO(
            long id,
            String description,
            TaskPriority priority,
            TaskStatus status,
            Date createDate,
            Calendar deadline,
            String details,
            DeveloperDTO assignedTo,
            boolean approved,
            List<FileForTaskDTO> files,
            List<CommentForTaskDTO> comments) {
        super(id, description, priority, status, createDate, deadline);
        this.details = details;
        this.assignedTo = assignedTo;
        this.approved = approved;
        this.files = files;
        this.comments = comments;
    }

    public TaskForProjectDTO() {
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public DeveloperDTO getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(DeveloperDTO assignedTo) {
        this.assignedTo = assignedTo;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public List<FileForTaskDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileForTaskDTO> files) {
        this.files = files;
    }

    public List<CommentForTaskDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentForTaskDTO> comments) {
        this.comments = comments;
    }
}
