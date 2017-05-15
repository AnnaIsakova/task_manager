package annanas_manager.controllers;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.TaskDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Task;
import annanas_manager.entities.enums.TaskPriority;
import annanas_manager.entities.enums.TaskStatus;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.exceptions.TaskException;
import annanas_manager.services.CustomUserService;
import annanas_manager.services.TaskService;
import annanas_manager.validators.TaskValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

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


    @RequestMapping(value = "api/todo", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<TaskDTO>> getAllTasks(Principal principal){
        CustomUserDTO userDTO = userService.getByEmail(principal.getName());
        List<TaskDTO> tasks = taskService.findByUser(userDTO);
        if(tasks.isEmpty()){
            return new ResponseEntity<List<TaskDTO>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<TaskDTO>>(tasks, HttpStatus.OK);
    }


    @RequestMapping(value = "/api/todo/new", method = RequestMethod.POST)
    public ResponseEntity<Void> createTask(
            @RequestBody TaskDTO taskDTO,
            Principal principal,
            BindingResult bindingResult) throws TaskException {

        ignoreDeadlineTime(taskDTO.getDeadline());
        taskValidator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()){
            throw new TaskException("Invalid task form", HttpStatus.BAD_REQUEST);
        }
        CustomUserDTO user = taskService.findUser(principal.getName());
        taskDTO.setCreatedBy(user);
        taskService.add(taskDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }


    @RequestMapping(value = "/api/todo/edit", method = RequestMethod.POST)
    public ResponseEntity<Void> editTask(
            @RequestBody TaskDTO taskDTO,
            Principal principal,
            BindingResult bindingResult) throws TaskException {

        ignoreDeadlineTime(taskDTO.getDeadline());
        taskValidator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            throw new TaskException("Invalid task form", HttpStatus.BAD_REQUEST);
        }
        taskService.edit(taskDTO, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @RequestMapping(value = "/api/todo/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteTask(
            @RequestBody long id,
            Principal principal) throws TaskException {

        System.out.println(id);
        taskService.delete(id, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    private void ignoreDeadlineTime(Calendar deadline){
//        deadline.set(Calendar.HOUR_OF_DAY, 0);
        deadline.set(Calendar.MINUTE, 0);
        deadline.set(Calendar.SECOND, 0);
        deadline.set(Calendar.MILLISECOND, 0);
    }


    @ExceptionHandler(TaskException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(TaskException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getHttpStatus().value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, ex.getHttpStatus());
    }
}
