package annanas_manager.services.impl;


import annanas_manager.DTO.TaskDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Task;
import annanas_manager.repositories.TaskRepository;
import annanas_manager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task add(TaskDTO taskDTO) {
        Task task = Task.fromDTO(taskDTO);
        return taskRepository.saveAndFlush(task);
    }

    @Override
    public void delete(long id) {
        taskRepository.delete(id);
    }

    @Override
    public Task edit(Task task) {
        return taskRepository.saveAndFlush(task);
    }

    @Override
    public List<Task> getAll(CustomUser id) {
        return taskRepository.findByUsername(id);
    }
}
