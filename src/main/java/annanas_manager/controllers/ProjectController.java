package annanas_manager.controllers;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.FileForProjectDTO;
import annanas_manager.DTO.ProjectDTO;
import annanas_manager.exceptions.CustomFileException;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.services.CustomUserService;
import annanas_manager.services.ProjectService;
import annanas_manager.validators.ProjectValidator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;

import static annanas_manager.services.impl.ProjectServiceImpl.DIR_PATH;

@RestController
public class ProjectController {

    @Autowired
    ProjectService projectService;
    @Autowired
    ProjectValidator projectValidator;
    @Autowired
    CustomUserService customUserService;

    @RequestMapping(value = "api/projects", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ProjectDTO>> getAllProjects(Principal principal)
    {

        List<ProjectDTO> projects = projectService.findByUser(principal.getName());
        if(projects.isEmpty()){
            return new ResponseEntity<List<ProjectDTO>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<ProjectDTO>>(projects, HttpStatus.OK);
    }

    @RequestMapping(value = "api/projects/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ProjectDTO> getProject(
            @PathVariable("id") long id,
            Principal principal) throws ProjectException
    {
        ProjectDTO project = projectService.findById(id, principal.getName());
        if(project == null){
            return new ResponseEntity<ProjectDTO>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<ProjectDTO>(project, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/new", method = RequestMethod.POST)
    public ResponseEntity<Void> createProject(
            @RequestBody ProjectDTO projectDTO,
            Principal principal,
            BindingResult bindingResult) throws ProjectException
    {
        ignoreDeadlineTime(projectDTO.getDeadline());
        projectValidator.validate(projectDTO, bindingResult);
        if (bindingResult.hasErrors()){
            throw new ProjectException("Invalid project form", HttpStatus.BAD_REQUEST);
        }

        projectService.add(projectDTO, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/projects/add_developer", method = RequestMethod.POST)
    public ResponseEntity<Void> addDeveloper(
            @RequestParam("id") long id,
            @RequestParam("email") String email,
            Principal principal
            ) throws ProjectException
    {
        System.out.println(id);
        System.out.println(email);
        projectService.addDeveloper(id, email, principal.getName());
        System.out.println(email + "email");
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<Void> uploadFile(
            @RequestParam("id") long id,
            @RequestBody MultipartFile file,
            Principal principal
            ) throws ProjectException
    {
        projectService.addFile(id, file, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{project_id}/download/{file_id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getFile(
            @PathVariable("project_id") long project_id,
            @PathVariable("file_id") long file_id,
            Principal principal,
            HttpServletResponse response) throws ProjectException, CustomFileException, IOException {
        FileForProjectDTO fileDTO = projectService.getFile(project_id, file_id, principal.getName());
        try {
            File file = new File(DIR_PATH + fileDTO.getName());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            String type = Files.probeContentType(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.set("File-Name", fileDTO.getName());
            headers.setContentType(MediaType.parseMediaType(type));
            return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        }
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
