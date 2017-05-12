package annanas_manager.services.impl;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.ProjectDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Project;
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
    public void add(ProjectDTO projectDTO) {
        projectDTO.setCreateDate(new Date(System.currentTimeMillis()));
        Project project = Project.fromDTO(projectDTO);
        project.setCreatedBy(userRepository.findByEmail(projectDTO.getCreatedBy().getEmail()));
        System.out.println(project);
        projectRepository.saveAndFlush(project);
    }

    @Override
    public void delete(long id, String email) throws ProjectException {
        Project project = projectRepository.findById(id);
        if (project.getCreatedBy().getEmail().equals(email)){
            projectRepository.delete(id);
        } else {
            throw new ProjectException("You have no pesmission to delete this project", HttpStatus.FORBIDDEN);
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
            throw new ProjectException("You have no pesmission to edit this task", HttpStatus.FORBIDDEN);
        }
    }


    @Override
    public List<ProjectDTO> findByUser(CustomUserDTO customUserDTO) {
        CustomUser customUser = userRepository.findByEmail(customUserDTO.getEmail());
        List<Project> projects = projectRepository.findByUsername(customUser);
        List<ProjectDTO> projectDTOs = new ArrayList<>();
        for (Project task:projects) {
            projectDTOs.add(task.toDTO());
        }
        return projectDTOs;
    }
}
