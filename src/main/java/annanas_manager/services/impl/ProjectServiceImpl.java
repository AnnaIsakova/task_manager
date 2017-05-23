package annanas_manager.services.impl;


import annanas_manager.DTO.*;
import annanas_manager.entities.*;
import annanas_manager.entities.enums.TaskStatus;
import annanas_manager.entities.enums.UserRole;
import annanas_manager.exceptions.*;
import annanas_manager.repositories.*;
import annanas_manager.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static annanas_manager.services.impl.FilesForProjectServiceImpl.DIR_PATH;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CustomUserRepository userRepository;

    //project
    @Override
    public void add(ProjectDTO projectDTO, String email) throws ProjectException {
        CustomUser user = userRepository.findByEmail(email);
        if (user instanceof Teamlead){
            Project project = Project.fromDTO(projectDTO);
            project.setCreateDate(new Date(System.currentTimeMillis()));
            project.setCreatedBy(user);
            projectRepository.saveAndFlush(project);
        } else {
            throw new ProjectException("You have no permission to create project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void delete(long id, String email) throws ProjectException {
        try {
            Project project = projectRepository.findOne(id);
            if (project.getCreatedBy().getEmail().equals(email)){
                List<FileForProject> files = project.getFiles();
                File file1;
                for (FileForProject file:files) {
                    file1 = new File(DIR_PATH + file.getCurrentTime() + "-" + file.getName());
                    file1.delete();
                }
                projectRepository.delete(id);
            } else {
                throw new ProjectException("You have no permission to delete this project", HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex){
            throw new NullPointerException("Such project does not exist");
        }
    }

    @Override
    public void edit(ProjectDTO projectDTO, String email) throws ProjectException {
        try {
            Project project = projectRepository.findOne(projectDTO.getId());
            if (project.getCreatedBy().getEmail().equals(email)){
                project.setDescription(projectDTO.getDescription());
                project.setDetails(projectDTO.getDetails());
                project.setDeadline(projectDTO.getDeadline());
                projectRepository.saveAndFlush(project);
            } else {
                throw new ProjectException("You have no permission to edit this project", HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex){
            throw new NullPointerException("Such project does not exist");
        }
    }


    @Override
    public List<ProjectDTO> findByUser(String email) {
        CustomUser customUser = userRepository.findByEmail(email);
        List<Project> projects;
        if (customUser instanceof Teamlead){
            projects = ((Teamlead) customUser).getProjects();
        } else {
            projects = ((Developer) customUser).getProjects();
        }
        List<ProjectDTO> projectDTOs = new ArrayList<>();
        for (Project project:projects) {
            projectDTOs.add(project.toDTO());
        }
        return projectDTOs;
    }

    @Override
    public ProjectDTO findById(long id, String email) throws ProjectException {
        try{
            Project project = projectRepository.findOne(id);
            if (hasUserPermission(project, email)){
                return project.toDTO();
            }
            throw new ProjectException("You have no permission to view this project", HttpStatus.FORBIDDEN);
        } catch (NullPointerException ex){
            throw new NullPointerException("Such project doesn't exist");
        }

    }

    private boolean hasUserPermission(Project project, String email){
        CustomUser user = userRepository.findByEmail(email);
        if (project.getCreatedBy().equals(user) || project.getDevelopers().contains(user)){
            return true;
        }
        return false;
    }
}
