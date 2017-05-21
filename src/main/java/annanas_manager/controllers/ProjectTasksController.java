package annanas_manager.controllers;


import annanas_manager.DTO.TaskForProjectDTO;
import annanas_manager.entities.TaskForProject;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.exceptions.TaskForProjectException;
import annanas_manager.services.ProjectService;
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
    ProjectService projectService;
    @Autowired
    TaskForProjectValidator taskValidator;

    @RequestMapping(value = "/api/projects/{project_id}/getTasks", method = RequestMethod.GET)
    public ResponseEntity<List<TaskForProjectDTO>> getAllTasks(
            @PathVariable("project_id") long project_id,
            Principal principal) throws ProjectException {
        List<TaskForProjectDTO> tasks = projectService.getAllTasks(project_id, principal.getName());
        if (tasks.isEmpty()){
            return new ResponseEntity<List<TaskForProjectDTO>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<TaskForProjectDTO>>(tasks, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/api/projects/{project_id}/tasks/new", method = RequestMethod.POST)
    public ResponseEntity<Void> createTask(
            @PathVariable("project_id") long project_id,
            @RequestBody TaskForProjectDTO taskDTO,
            Principal principal,
            BindingResult bindingResult) throws ProjectException
    {
        ignoreDeadlineTime(taskDTO.getDeadline());
        taskValidator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()){
            throw new ProjectException("Invalid task form", HttpStatus.BAD_REQUEST);
        }

        projectService.addTask(project_id, taskDTO, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.CREATED);
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
