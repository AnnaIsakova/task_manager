package annanas_manager.controllers;


import annanas_manager.DTO.CommentForProjectDTO;
import annanas_manager.DTO.CommentForTaskDTO;
import annanas_manager.entities.CommentForTask;
import annanas_manager.exceptions.CommentException;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.exceptions.TaskException;
import annanas_manager.services.CommentsForTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class TaskCommentsController {

    @Autowired
    CommentsForTaskService commentService;

    @RequestMapping(value = "/api/projects/{project_id}/tasks/{task_id}/comments/new", method = RequestMethod.POST)
    public ResponseEntity<Void> addComment(
            @PathVariable("project_id") long projectId,
            @PathVariable("task_id") long taskId,
            @RequestBody CommentForTaskDTO commentDTO,
            Principal principal
    ) throws ProjectException, TaskException {
        commentService.addComment(projectId, taskId, commentDTO, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{project_id}/tasks/{task_id}/comments", method = RequestMethod.GET)
    public ResponseEntity<List<CommentForTaskDTO>> getAllComments(
            @PathVariable("project_id") long projectId,
            @PathVariable("task_id") long taskId,
            Principal principal) throws ProjectException, TaskException {
        List<CommentForTaskDTO> comments = commentService.getAllComments(projectId, taskId, principal.getName());
        if (comments.isEmpty()){
            return new ResponseEntity<List<CommentForTaskDTO>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<CommentForTaskDTO>>(comments, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/api/projects/{project_id}/tasks/{task_id}/comments/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteComment(
            @PathVariable("project_id") long projectId,
            @PathVariable("task_id") long taskId,
            @RequestParam("id") long commentId,
            Principal principal
    ) throws ProjectException, CommentException, TaskException {
        commentService.deleteComment(projectId, taskId, commentId, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{project_id}/tasks/{task_id}/comments/edit", method = RequestMethod.POST)
    public ResponseEntity<Void> editComment(
            @PathVariable("project_id") long projectId,
            @PathVariable("task_id") long taskId,
            @RequestBody CommentForTaskDTO commentDTO,
            Principal principal
    ) throws ProjectException, CommentException, TaskException {
        commentService.editComment(projectId, taskId, commentDTO, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ExceptionHandler(ProjectException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(ProjectException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getHttpStatus().value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, ex.getHttpStatus());
    }
    @ExceptionHandler(TaskException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(TaskException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getHttpStatus().value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, ex.getHttpStatus());
    }
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(CommentException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getHttpStatus().value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, ex.getHttpStatus());
    }
}
