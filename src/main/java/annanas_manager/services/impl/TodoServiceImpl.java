package annanas_manager.services.impl;


import annanas_manager.DTO.TaskTodoDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Task;
import annanas_manager.entities.TaskTodo;
import annanas_manager.entities.enums.TaskStatus;
import annanas_manager.exceptions.TaskException;
import annanas_manager.repositories.CustomUserRepository;
import annanas_manager.repositories.TodoRepository;
import annanas_manager.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private CustomUserRepository userRepository;

    @Override
    public void add(TaskTodoDTO taskDTO, String email) {
        taskDTO.setCreateDate(new Date(System.currentTimeMillis()));
        taskDTO.setStatus(TaskStatus.NEW);
        TaskTodo task = TaskTodo.fromDTO(taskDTO);
        task.setCreatedBy(userRepository.findByEmail(email));
        todoRepository.saveAndFlush(task);
    }

    @Override
    public void delete(long id, String email) throws TaskException {
        try{
            Task task = todoRepository.findOne(id);
            if (hasUserPermission(task, email)){
                todoRepository.delete(id);
            } else {
                throw new TaskException("You have no permission to delete this task", HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex){
            throw new NullPointerException("Task you're trying to delete doesn't exist");
        }
    }

    @Override
    public void edit(TaskTodoDTO taskDTO, String email) throws TaskException {
        try {
            Task task = todoRepository.findOne(taskDTO.getId());
            if (hasUserPermission(task, email)){
                task.setDescription(taskDTO.getDescription());
                task.setPriority(taskDTO.getPriority());
                task.setStatus(taskDTO.getStatus());
                task.setDeadline(taskDTO.getDeadline());
                todoRepository.saveAndFlush(task);
            } else {
                throw new TaskException("You have no permission to edit this task", HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex){
            throw new NullPointerException("Task you're trying to edit doesn't exist");
        }

    }

    @Override
    public List<TaskTodoDTO> findByUser(String email) {
        CustomUser customUser = userRepository.findByEmail(email);
        List<TaskTodo> tasks = todoRepository.findByUser(customUser);
        List<TaskTodoDTO> taskDTOs = new ArrayList<>();
        for (TaskTodo task:tasks) {
            taskDTOs.add(task.toDTO());
        }
        return taskDTOs;
    }

    private boolean hasUserPermission(Task task, String email){
        if (task instanceof TaskTodo){
            if (((TaskTodo) task).getCreatedBy().getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }
}
