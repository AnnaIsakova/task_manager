package annanas_manager.services.impl;

import annanas_manager.DTO.DeveloperDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Developer;
import annanas_manager.entities.Project;
import annanas_manager.entities.TaskForProject;
import annanas_manager.exceptions.CustomUserException;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.repositories.CustomUserRepository;
import annanas_manager.repositories.ProjectRepository;
import annanas_manager.repositories.TaskForProjectRepository;
import annanas_manager.services.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DeveloperServiceImpl implements DeveloperService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CustomUserRepository userRepository;
    @Autowired
    private TaskForProjectRepository taskRepository;

    @Override
    public void addDeveloper(long id, String emailDev, String emailCreatedBy) throws ProjectException, CustomUserException {
        Project project = projectRepository.findOne(id);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        if (project.getCreatedBy().getEmail().equals(emailCreatedBy)){
            CustomUser userDev = userRepository.findByEmail(emailDev);
            if (userDev != null && userDev instanceof Developer){
                Developer developer = (Developer) userDev;
                if (!project.getDevelopers().contains(developer)){
                    project.getDevelopers().add(developer);
                    projectRepository.saveAndFlush(project);
                } else {
                    throw new ProjectException("Such developer is already in your team", HttpStatus.CONFLICT);
                }
            } else {
                throw new CustomUserException("Such developer does not exist", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ProjectException("You have no permission to add developer to this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void deleteDeveloper(long projectId, long devId, String emailCreatedBy) throws ProjectException, CustomUserException {
        Project project = projectRepository.findOne(projectId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        if (project.getCreatedBy().getEmail().equals(emailCreatedBy)){
            CustomUser userDev = userRepository.findOne(devId);
            if (userDev instanceof Developer){
                List<TaskForProject> tasks = taskRepository.findByDeveloper((Developer) userDev);
                for (TaskForProject task:tasks) {
                    taskRepository.delete(task.getId());
                }
                project.getDevelopers().remove(userDev);
                projectRepository.saveAndFlush(project);
            } else {
                throw new CustomUserException("Such developer does not exist", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ProjectException("You have no permission to delete developer from this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public List<DeveloperDTO> getAllDevs(long projectId, String emailCreatedBy) throws ProjectException {
        Project project = projectRepository.findOne(projectId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        if (project.getCreatedBy().getEmail().equals(emailCreatedBy)){
            List<Developer> devs = project.getDevelopers();
            List<DeveloperDTO> devsDTO = new ArrayList<>();
            for (Developer dev:devs) {
                devsDTO.add(dev.toDTO());
            }
            return devsDTO;
        } else {
            throw new ProjectException("You have no permission to fetch developers from this project", HttpStatus.FORBIDDEN);
        }
    }
}
