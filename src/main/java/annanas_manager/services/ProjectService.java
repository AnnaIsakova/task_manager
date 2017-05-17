package annanas_manager.services;


import annanas_manager.DTO.*;
import annanas_manager.entities.CommentForProject;
import annanas_manager.exceptions.CommentException;
import annanas_manager.exceptions.CustomFileException;
import annanas_manager.exceptions.CustomUserException;
import annanas_manager.exceptions.ProjectException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {

    //methods for project
    void add(ProjectDTO projectDTO, String email);
    void delete(long id, String email) throws ProjectException;
    void edit(ProjectDTO projectDTO , String email) throws ProjectException;
    List<ProjectDTO> findByUser(String email);
    ProjectDTO findById(long id, String email) throws ProjectException;

    //methods for project developers
    void addDeveloper(long id, String emailDev, String emailCreatedBy) throws ProjectException, CustomUserException;
    void deleteDeveloper(long projectId, long devId, String emailCreatedBy) throws ProjectException, CustomUserException;
    List<DeveloperDTO> getAllDevs(long projectId, String emailCreatedBy) throws ProjectException;

    //methods for project files
    void addFile(long id, MultipartFile multipartFile, String emailCreatedBy) throws ProjectException;
    FileForProjectDTO getFile(long projectID, long fileId, String email) throws ProjectException, CustomFileException;
    void deleteFile(long projectID, long fileId, String email) throws ProjectException, CustomFileException;
    List<FileForProjectDTO> getAllFiles(long id, String email) throws ProjectException;

    //methods for comments
    void addComment(long id, CommentForProjectDTO commentDTO, String email) throws ProjectException;
    void deleteComment(long projectID, long commentId, String email) throws ProjectException, CommentException;
    List<CommentForProjectDTO> getAllComments(long id, String email) throws ProjectException;
}
