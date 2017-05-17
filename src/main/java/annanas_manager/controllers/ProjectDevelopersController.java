package annanas_manager.controllers;


import annanas_manager.DTO.DeveloperDTO;
import annanas_manager.DTO.FileForProjectDTO;
import annanas_manager.exceptions.CustomUserException;
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

    @RequestMapping(value = "/api/projects/deleteDeveloper", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteDeveloper(
            @RequestParam("projectId") long projectId,
            @RequestParam("devId") long devId,
            Principal principal
    ) throws ProjectException, CustomUserException {
        projectService.deleteDeveloper(projectId, devId, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{project_id}/getDevelopers", method = RequestMethod.GET)
    public ResponseEntity<List<DeveloperDTO>> getAllFiles(
            @PathVariable("project_id") long project_id,
            Principal principal) throws ProjectException {
        List<DeveloperDTO> files = projectService.getAllDevs(project_id, principal.getName());
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
