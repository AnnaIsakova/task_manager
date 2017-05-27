package annanas_manager.services;


import annanas_manager.DTO.FileForTaskDTO;
import annanas_manager.exceptions.CustomFileException;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.exceptions.TaskException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FilesForTaskService {
    void addFile(long projectId, long taskId, MultipartFile multipartFile, String emailCreatedBy) throws ProjectException, CustomFileException, TaskException;
    FileForTaskDTO getFile(long projectID, long taskId, long fileId, String email) throws ProjectException, CustomFileException, TaskException;
    void deleteFile(long projectID, long taskId, long fileId, String email) throws ProjectException, CustomFileException, TaskException;
    List<FileForTaskDTO> getAllFiles(long projectID, long taskId, String email) throws ProjectException, TaskException;
}
