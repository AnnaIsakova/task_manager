package annanas_manager.controllers;


import annanas_manager.DTO.CommentForProjectDTO;
import annanas_manager.exceptions.CommentException;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class ProjectCommentsController {

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/api/projects/addComment", method = RequestMethod.POST)
    public ResponseEntity<Void> addComment(
            @RequestParam("id") long id,
            @RequestBody CommentForProjectDTO commentDTO,
            Principal principal
    ) throws ProjectException
    {
        System.out.println(id);
        System.out.println(commentDTO.getText());
        projectService.addComment(id, commentDTO, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{project_id}/getComments", method = RequestMethod.GET)
    public ResponseEntity<List<CommentForProjectDTO>> getAllComments(
            @PathVariable("project_id") long project_id,
            Principal principal) throws ProjectException {
        List<CommentForProjectDTO> comments = projectService.getAllComments(project_id, principal.getName());
        if (comments.isEmpty()){
            return new ResponseEntity<List<CommentForProjectDTO>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<CommentForProjectDTO>>(comments, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/api/projects/deleteComment", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteComment(
            @RequestParam("projectId") long projectId,
            @RequestParam("commentId") long commentId,
            Principal principal
    ) throws ProjectException, CommentException {
        projectService.deleteComment(projectId, commentId, principal.getName());
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
