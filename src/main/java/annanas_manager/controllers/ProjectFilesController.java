package annanas_manager.controllers;


import annanas_manager.DTO.FileForProjectDTO;
import annanas_manager.exceptions.CustomFileException;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.AssertFalse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;

import static annanas_manager.services.impl.ProjectServiceImpl.DIR_PATH;

@RestController
public class ProjectFilesController {

    @Autowired
    ProjectService projectService;

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
            throw new FileNotFoundException("File not found");
        } catch (IOException e) {
            throw new IOException();
        }
    }

    @RequestMapping(value = "/api/projects/deleteFile", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteFile(
            @RequestParam("projectId") long projectId,
            @RequestParam("fileId") long fileId,
            Principal principal
    ) throws ProjectException, CustomFileException {
        projectService.deleteFile(projectId, fileId, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{project_id}/getFiles", method = RequestMethod.GET)
    public ResponseEntity<List<FileForProjectDTO>> getAllFiles(
            @PathVariable("project_id") long project_id,
            Principal principal) throws ProjectException {
        List<FileForProjectDTO> files = projectService.getAllFiles(project_id, principal.getName());
        if (files.isEmpty()){
            return new ResponseEntity<List<FileForProjectDTO>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<FileForProjectDTO>>(files, HttpStatus.OK);
        }
    }

    @ExceptionHandler(CustomFileException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(CustomFileException ex) {
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
}
