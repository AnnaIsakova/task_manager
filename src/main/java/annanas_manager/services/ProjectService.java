package annanas_manager.services;


import annanas_manager.DTO.*;
import annanas_manager.entities.CommentForProject;
import annanas_manager.exceptions.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {

    //methods for project
    void add(ProjectDTO projectDTO, String email) throws ProjectException;
    void delete(long id, String email) throws ProjectException;
    void edit(ProjectDTO projectDTO , String email) throws ProjectException;
    List<ProjectDTO> findByUser(String email);
    ProjectDTO findById(long id, String email) throws ProjectException;
}
