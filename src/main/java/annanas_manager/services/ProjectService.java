package annanas_manager.services;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.ProjectDTO;
import annanas_manager.exceptions.ProjectException;

import java.util.List;

public interface ProjectService {

    void add(ProjectDTO taskDTO);
    void delete(long id, String email) throws ProjectException;
    void edit(ProjectDTO task , String email) throws ProjectException;
    List<ProjectDTO> findByUser(CustomUserDTO customUserDTO);
}
