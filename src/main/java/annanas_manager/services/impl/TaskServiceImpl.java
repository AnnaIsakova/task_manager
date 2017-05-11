package annanas_manager.services.impl;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.TaskDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Task;
import annanas_manager.entities.enums.TaskStatus;
import annanas_manager.exceptions.TaskException;
import annanas_manager.repositories.CustomUserRepository;
import annanas_manager.repositories.TaskRepository;
import annanas_manager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CustomUserRepository userRepository;

    @Override
    public void add(TaskDTO taskDTO) {
        taskDTO.setCreateDate(new Date(System.currentTimeMillis()));
        taskDTO.setStatus(TaskStatus.NEW);
        Task task = Task.fromDTO(taskDTO);
        task.setCreatedBy(userRepository.findByEmail(taskDTO.getCreatedBy().getEmail()));
        System.out.println(task);
        taskRepository.saveAndFlush(task);
    }

    @Override
    public void delete(long id, String email) throws TaskException {
        Task task = taskRepository.findById(id);
        CustomUser user = userRepository.findByEmail(email);
        if (task.getCreatedBy().equals(user)){
            taskRepository.delete(id);
        }
        throw new TaskException("You have no pesmission to delete this task", HttpStatus.FORBIDDEN);
    }

    @Override
    public void edit(TaskDTO taskDTO, String email) throws TaskException {
        Task task = taskRepository.findById(taskDTO.getId());
        CustomUser user = userRepository.findByEmail(email);
        if (task.getCreatedBy().equals(user)){
            task.setDescription(taskDTO.getDescription());
            task.setPriority(taskDTO.getPriority());
            task.setStatus(taskDTO.getStatus());
            task.setDeadline(taskDTO.getDeadline());
            System.out.println("task from service: " + taskDTO);
            taskRepository.saveAndFlush(task);
        }
        throw new TaskException("You have no pesmission to edit this task", HttpStatus.FORBIDDEN);
    }

    @Override
    @Transactional(readOnly=true)
    public CustomUserDTO findUser(String email) {
        CustomUser user = userRepository.findByEmail(email);
        return user.toDTO();
    }

    @Override
    public List<TaskDTO> findByUser(CustomUserDTO customUserDTO) {
        CustomUser customUser = userRepository.findByEmail(customUserDTO.getEmail());
        List<Task> tasks = taskRepository.findByUsername(customUser);
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (Task task:tasks) {
            taskDTOs.add(task.toDTO());
        }
        return taskDTOs;
    }
}
