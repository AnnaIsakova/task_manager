package annanas_manager.services;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.FileForProjectDTO;
import annanas_manager.DTO.ProjectDTO;
import annanas_manager.exceptions.ProjectException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {

    void add(ProjectDTO projectDTO, String email);
    void delete(long id, String email) throws ProjectException;
    void edit(ProjectDTO projectDTO , String email) throws ProjectException;
    List<ProjectDTO> findByUser(String email);
    ProjectDTO findById(long id, String email) throws ProjectException;
    void addDeveloper(long id, String emailDev, String emailCreatedBy) throws ProjectException;
    void addFile(long id, MultipartFile multipartFile, String emailCreatedBy) throws ProjectException;
//    List<FileForProjectDTO> getAllFiles(long id);
}
