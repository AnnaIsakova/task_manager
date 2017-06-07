package annanas_manager.controllers;


import annanas_manager.DTO.FileForTaskDTO;
import annanas_manager.exceptions.CustomFileException;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.exceptions.TaskException;
import annanas_manager.services.FilesForTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
public class TaskFilesController {

    @Autowired
    FilesForTaskService fileService;

    @RequestMapping(value = "/api/projects/{proj_id}/tasks/{task_id}/files/new", method = RequestMethod.POST)
    public ResponseEntity<Void> uploadFile(
            @PathVariable("proj_id") long projectId,
            @PathVariable("task_id") long taskId,
            @RequestBody MultipartFile file,
            Principal principal
    ) throws ProjectException, CustomFileException, TaskException {
        if (file == null){
            throw new CustomFileException("Not a valid file", HttpStatus.BAD_REQUEST);
        }
        fileService.addFile(projectId, taskId, file, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{proj_id}/tasks/{task_id}/files/{file_id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getFile(
            @PathVariable("proj_id") long projectId,
            @PathVariable("task_id") long taskId,
            @PathVariable("file_id") long file_id,
            Principal principal,
            HttpServletResponse response) throws ProjectException, CustomFileException, TaskException, IOException {
        FileForTaskDTO fileDTO = fileService.getFile(projectId,taskId, file_id, principal.getName());
        return Helper.getFile(fileDTO.getName(), fileDTO.getCurrentTime());
    }

    @RequestMapping(value = "/api/projects/{proj_id}/tasks/{task_id}/files/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteFile(
            @PathVariable("proj_id") long projectId,
            @PathVariable("task_id") long taskId,
            @RequestParam("id") long fileId,
            Principal principal
    ) throws ProjectException, CustomFileException, TaskException {
        fileService.deleteFile(projectId, taskId, fileId, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/{proj_id}/tasks/{task_id}/files", method = RequestMethod.GET)
    public ResponseEntity<List<FileForTaskDTO>> getAllFiles(
            @PathVariable("proj_id") long projecIid,
            @PathVariable("task_id") long taskIid,
            Principal principal) throws ProjectException, TaskException {
        List<FileForTaskDTO> files = fileService.getAllFiles(projecIid, taskIid, principal.getName());
        if (files.isEmpty()){
            return new ResponseEntity<List<FileForTaskDTO>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<FileForTaskDTO>>(files, HttpStatus.OK);
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
