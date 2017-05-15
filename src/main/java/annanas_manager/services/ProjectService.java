package annanas_manager.services;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.ProjectDTO;
import annanas_manager.exceptions.ProjectException;

import java.util.List;

public interface ProjectService {

    void add(ProjectDTO taskDTO, String email);
    void delete(long id, String email) throws ProjectException;
    void edit(ProjectDTO task , String email) throws ProjectException;
    List<ProjectDTO> findByUser(String email);
    ProjectDTO findById(long id, String email) throws ProjectException;
    void addDeveloper(long id, String emailDev, String emailCreatedBy) throws ProjectException;
}
