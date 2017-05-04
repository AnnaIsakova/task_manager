package annanas_manager.services;


import annanas_manager.DTO.TaskDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Task;

import java.util.List;

public interface TaskService {
    Task add(TaskDTO taskDTO);
    void delete(long id);
    Task edit(Task task);
    List<Task> getAll(CustomUser id);
}
