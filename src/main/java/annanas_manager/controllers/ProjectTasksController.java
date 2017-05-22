package annanas_manager.controllers;


import annanas_manager.DTO.TaskForProjectDTO;
import annanas_manager.entities.TaskForProject;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.exceptions.TaskException;
import annanas_manager.exceptions.TaskForProjectException;
import annanas_manager.services.ProjectService;
import annanas_manager.services.TaskForProjectService;
import annanas_manager.validators.TaskForProjectValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;

@RestController
public class ProjectTasksController {

    @Autowired
    TaskForProjectService taskService;
    @Autowired
    TaskForProjectValidator taskValidator;

    @RequestMapping(value = "/api/projects/{id}/tasks", method = RequestMethod.GET)
    public ResponseEntity<List<TaskForProjectDTO>> getAllTasks(
            @PathVariable("id") long project_id,
            Principal principal) throws ProjectException {
        List<TaskForProjectDTO> tasks = taskService.getAllTasks(project_id, principal.getName());
        if (tasks.isEmpty()){
            return new ResponseEntity<List<TaskForProjectDTO>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<TaskForProjectDTO>>(tasks, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/api/projects/{id}/tasks/new", method = RequestMethod.POST)
    public ResponseEntity<Void> createTask(
            @PathVariable("id") long project_id,
            @RequestBody TaskForProjectDTO taskDTO,
            Principal principal,
            BindingResult bindingResult) throws ProjectException
    {
        ignoreDeadlineTime(taskDTO.getDeadline());
        taskValidator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()){
            throw new ProjectException("Invalid task form", HttpStatus.BAD_REQUEST);
        }
        System.out.println("assTo = " + taskDTO.getAssignedTo().getEmail());
        taskService.addTask(project_id, taskDTO, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/projects/{id}/tasks/{task_id}", method = RequestMethod.GET)
    public ResponseEntity<TaskForProjectDTO> getTask(
            @PathVariable("id") long project_id,
            @PathVariable("task_id") long task_id,
            Principal principal) throws ProjectException {
        TaskForProjectDTO task = taskService.findById(project_id, task_id, principal.getName());
        if (task == null){
            return new ResponseEntity<TaskForProjectDTO>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<TaskForProjectDTO>(task, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{id}/tasks/edit", method = RequestMethod.POST)
    public ResponseEntity<Void> editTask(
            @PathVariable("id") long project_id,
            @RequestBody TaskForProjectDTO taskDTO,
            Principal principal,
            BindingResult bindingResult) throws ProjectException, TaskException {
        ignoreDeadlineTime(taskDTO.getDeadline());
        taskValidator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()){
            throw new ProjectException("Invalid task form", HttpStatus.BAD_REQUEST);
        }
        taskService.editTask(project_id, taskDTO, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/projects/{id}/tasks/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteTask(
            @PathVariable("id") long projectId,
            @RequestParam("id") long taskId,
            Principal principal
    ) throws ProjectException, TaskException {
        taskService.deleteTask(projectId, taskId, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ExceptionHandler(TaskForProjectException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(TaskForProjectException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getHttpStatus().value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, ex.getHttpStatus());
    }

    @ExceptionHandler(ProjectException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(ProjectException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getHttpStatus().value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, ex.getHttpStatus());
    }

    private void ignoreDeadlineTime(Calendar deadline){
        deadline.clear(Calendar.HOUR_OF_DAY);
        deadline.clear(Calendar.MINUTE);
        deadline.clear(Calendar.SECOND);
        deadline.clear(Calendar.MILLISECOND);
    }
}
