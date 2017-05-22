package annanas_manager.entities;


import annanas_manager.DTO.FileForTaskDTO;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.File;

@Entity
@DiscriminatorValue(value = "T")
public class FileForTask extends CustomFile {

    @ManyToOne
    @JoinColumn(name = "task_id")
    protected TaskForProject task;

    public FileForTask(String name, File file) {
        super(name);
    }

    public FileForTask() {
    }

    @Override
    public FileForTaskDTO toDTO() {
        return new FileForTaskDTO(id, name);
    }

    public TaskForProject getTask() {
        return task;
    }

    public void setTask(TaskForProject task) {
        this.task = task;
    }
}
