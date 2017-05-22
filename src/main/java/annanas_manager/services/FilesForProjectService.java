package annanas_manager.services;


import annanas_manager.DTO.FileForProjectDTO;
import annanas_manager.exceptions.CustomFileException;
import annanas_manager.exceptions.ProjectException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FilesForProjectService {
    //methods for project files
    void addFile(long id, MultipartFile multipartFile, String emailCreatedBy) throws ProjectException, CustomFileException;
    FileForProjectDTO getFile(long projectID, long fileId, String email) throws ProjectException, CustomFileException;
    void deleteFile(long projectID, long fileId, String email) throws ProjectException, CustomFileException;
    List<FileForProjectDTO> getAllFiles(long id, String email) throws ProjectException;
}
