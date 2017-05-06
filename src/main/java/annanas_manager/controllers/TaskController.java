package annanas_manager.controllers;


import annanas_manager.DTO.TaskDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Task;
import annanas_manager.entities.enums.TaskPriority;
import annanas_manager.entities.enums.TaskStatus;
import annanas_manager.services.CustomUserService;
import annanas_manager.services.TaskService;
import annanas_manager.validators.TaskValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    CustomUserService userService;

    @Autowired
    TaskValidator taskValidator;

    @RequestMapping(value = "/api/status", method = RequestMethod.GET)
    @ResponseBody
    public List<TaskStatus> getAllStatus(){
        return TaskStatus.getAllStatus();
    }

    @RequestMapping(value = "/api/priorities", method = RequestMethod.GET)
    @ResponseBody
    public List<TaskPriority> getPriorities(){
        return TaskPriority.getPriorities();
    }

    @RequestMapping(value = "api/tasks", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<TaskDTO>> getAllTasks(Principal principal){
        CustomUser user = userService.getByEmail(principal.getName());
        Set<Task> tasks = user.getTodoList();
        System.out.println(tasks);
        List<TaskDTO> tasksDTO = new ArrayList<>();
        for(Task task : tasks){
            TaskDTO taskDTO = task.toDTO();
            taskDTO.setCreatedBy(user.toDTO());
            tasksDTO.add(taskDTO);
        }
        System.out.println(tasksDTO);
        if(tasks.isEmpty()){
            return new ResponseEntity<List<TaskDTO>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<TaskDTO>>(tasksDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/new_task", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody TaskDTO taskDTO, Principal principal, BindingResult bindingResult) {
        System.out.println(taskDTO.toString());
        taskValidator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()){
            System.out.println("errors from validator: " + bindingResult.getAllErrors());
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        CustomUser user = userService.getByEmail(principal.getName());
        taskDTO.setCreatedBy(user.toDTO());
        taskService.add(taskDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
