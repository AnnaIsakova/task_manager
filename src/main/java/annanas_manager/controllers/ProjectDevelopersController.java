package annanas_manager.controllers;


import annanas_manager.DTO.DeveloperDTO;
import annanas_manager.DTO.FileForProjectDTO;
import annanas_manager.exceptions.CustomUserException;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.services.DeveloperService;
import annanas_manager.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class ProjectDevelopersController {

    @Autowired
    DeveloperService developerService;

    @RequestMapping(value = "/api/projects/{id}/devs/new", method = RequestMethod.POST)
    public ResponseEntity<Void> addDeveloper(
            @PathVariable("id") long id,
            @RequestBody String email,
            Principal principal
    ) throws ProjectException, CustomUserException {
        developerService.addDeveloper(id, email, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{id}/devs/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteDeveloper(
            @PathVariable("id") long projectId,
            @RequestParam("id") long devId,
            @RequestParam("keep_tasks") boolean keepTasks,
            Principal principal
    ) throws ProjectException, CustomUserException {
        System.out.println(keepTasks);
        developerService.deleteDeveloper(projectId, devId, keepTasks, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{id}/devs", method = RequestMethod.GET)
    public ResponseEntity<List<DeveloperDTO>> getAllFiles(
            @PathVariable("id") long project_id,
            Principal principal) throws ProjectException {
        List<DeveloperDTO> files = developerService.getAllDevs(project_id, principal.getName());
        if (files.isEmpty()){
            return new ResponseEntity<List<DeveloperDTO>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<DeveloperDTO>>(files, HttpStatus.OK);
        }
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
