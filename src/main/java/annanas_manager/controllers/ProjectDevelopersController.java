package annanas_manager.controllers;


import annanas_manager.exceptions.CustomUserException;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

public class ProjectDevelopersController {

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/api/projects/add_developer", method = RequestMethod.POST)
    public ResponseEntity<Void> addDeveloper(
            @RequestParam("id") long id,
            @RequestParam("email") String email,
            Principal principal
    ) throws ProjectException, CustomUserException {
        projectService.addDeveloper(id, email, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ExceptionHandler(ProjectException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(ProjectException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getHttpStatus().value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, ex.getHttpStatus());
    }

    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(CustomUserException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getHttpStatus().value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, ex.getHttpStatus());
    }
}
