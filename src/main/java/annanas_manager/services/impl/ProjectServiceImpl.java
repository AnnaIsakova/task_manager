package annanas_manager.services.impl;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.ProjectDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Developer;
import annanas_manager.entities.Project;
import annanas_manager.entities.Teamlead;
import annanas_manager.entities.enums.UserRole;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.repositories.CustomUserRepository;
import annanas_manager.repositories.ProjectRepository;
import annanas_manager.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CustomUserRepository userRepository;

    @Override
    public void add(ProjectDTO projectDTO, String email) {
        Project project = Project.fromDTO(projectDTO);
        project.setCreateDate(new Date(System.currentTimeMillis()));
        CustomUser user = userRepository.findByEmail(email);
        project.setCreatedBy(user);
        projectRepository.saveAndFlush(project);
    }

    @Override
    public void delete(long id, String email) throws ProjectException {
        Project project = projectRepository.findById(id);
        if (project.getCreatedBy().getEmail().equals(email)){
            projectRepository.delete(id);
        } else {
            throw new ProjectException("You have no permission to delete this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void edit(ProjectDTO projectDTO, String email) throws ProjectException {
        Project project = projectRepository.findById(projectDTO.getId());
        if (project.getCreatedBy().getEmail().equals(email)){
            project.setDescription(projectDTO.getDescription());
            project.setDetails(projectDTO.getDescription());
            project.setDeadline(projectDTO.getDeadline());
            System.out.println("task from service: " + projectDTO);
            projectRepository.saveAndFlush(project);
        } else {
            throw new ProjectException("You have no permission to edit this task", HttpStatus.FORBIDDEN);
        }
    }


    @Override
    public List<ProjectDTO> findByUser(String email) {
        CustomUser customUser = userRepository.findByEmail(email);
        List<Project> projects = projectRepository.findByUsername(customUser);
        List<ProjectDTO> projectDTOs = new ArrayList<>();
        for (Project project:projects) {
            projectDTOs.add(project.toDTO());
        }
        return projectDTOs;
    }

    @Override
    public ProjectDTO findById(long id, String email) throws ProjectException {
        CustomUser customUser = userRepository.findByEmail(email);
        Project project = projectRepository.findById(id);
        if (project.getCreatedBy().equals(customUser) || project.getDevelopers().contains(customUser)){
            return project.toDTO();
        }
        throw new ProjectException("You have no permission to view this project", HttpStatus.FORBIDDEN);
    }

    @Override
    public void addDeveloper(long id, String emailDev, String emailCreatedBy) throws ProjectException {
        CustomUser createdBy = userRepository.findByEmail(emailCreatedBy);
        Project project = projectRepository.findById(id);
        if (project.getCreatedBy().equals(createdBy)){
            CustomUser userDev = userRepository.findByEmail(emailDev);
            if (userDev instanceof Developer){
                Developer developer = (Developer) userDev;
                if (!project.getDevelopers().contains(developer)){
                    project.getDevelopers().add(developer);
                    projectRepository.saveAndFlush(project);
                } else {
                    throw new ProjectException("Such developer is already in your team", HttpStatus.CONFLICT);
                }
            } else {
                throw new ProjectException("Such developer does not exist", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ProjectException("You have no permission to add developer to this project", HttpStatus.FORBIDDEN);
        }
    }
}