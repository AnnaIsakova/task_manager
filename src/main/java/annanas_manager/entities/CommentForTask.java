package annanas_manager.entities;


import annanas_manager.DTO.CommentForTaskDTO;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "T")
public class CommentForTask extends Comment{

    @ManyToOne
    @JoinColumn(name = "task_id")
    protected TaskForProject task;

    public CommentForTask() {
    }

    public CommentForTask(String text) {
        super(text);
    }

    @Override
    public CommentForTaskDTO toDTO() {
        return new CommentForTaskDTO(id, text, userFrom.toDTO(), createDate, lastModified);
    }

    public static CommentForTask fromDTO(CommentForTaskDTO dto){
        return new CommentForTask(dto.getText());
    }

    public TaskForProject getTask() {
        return task;
    }

    public void setTask(TaskForProject task) {
        this.task = task;
    }
}
