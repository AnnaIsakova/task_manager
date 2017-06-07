package annanas_manager.controllers;


import annanas_manager.DTO.CommentForProjectDTO;
import annanas_manager.exceptions.CommentException;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.services.CommentForProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class ProjectCommentsController {

    @Autowired
    CommentForProjectService commentForProjectService;

    @RequestMapping(value = "/api/projects/{id}/comments/new", method = RequestMethod.POST)
    public ResponseEntity<Void> addComment(
            @PathVariable("id") long id,
            @RequestBody CommentForProjectDTO commentDTO,
            Principal principal
    ) throws ProjectException
    {
        commentForProjectService.addComment(id, commentDTO, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{id}/comments", method = RequestMethod.GET)
    public ResponseEntity<List<CommentForProjectDTO>> getAllComments(
            @PathVariable("id") long project_id,
            Principal principal) throws ProjectException {
        List<CommentForProjectDTO> comments = commentForProjectService.getAllComments(project_id, principal.getName());
        if (comments.isEmpty()){
            return new ResponseEntity<List<CommentForProjectDTO>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<CommentForProjectDTO>>(comments, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/api/projects/{id}/comments/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteComment(
            @PathVariable("id") long projectId,
            @RequestParam("id") long commentId,
            Principal principal
    ) throws ProjectException, CommentException {
        commentForProjectService.deleteComment(projectId, commentId, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{id}/comments/edit", method = RequestMethod.POST)
    public ResponseEntity<Void> editComment(
            @PathVariable("id") long projectId,
            @RequestBody CommentForProjectDTO commentDTO,
            Principal principal
    ) throws ProjectException, CommentException {
        commentForProjectService.editComment(projectId, commentDTO, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ExceptionHandler(ProjectException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(ProjectException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getHttpStatus().value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, ex.getHttpStatus());
    }
}
