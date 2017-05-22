package annanas_manager.services;


import annanas_manager.DTO.DeveloperDTO;
import annanas_manager.exceptions.CustomUserException;
import annanas_manager.exceptions.ProjectException;

import java.util.List;

public interface DeveloperService {
    //methods for project developers
    void addDeveloper(long id, String emailDev, String emailCreatedBy) throws ProjectException, CustomUserException;
    void deleteDeveloper(long projectId, long devId, String emailCreatedBy) throws ProjectException, CustomUserException;
    List<DeveloperDTO> getAllDevs(long projectId, String emailCreatedBy) throws ProjectException;
}
