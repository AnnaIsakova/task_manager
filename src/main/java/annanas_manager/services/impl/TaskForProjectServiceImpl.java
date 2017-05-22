package annanas_manager.services.impl;


import annanas_manager.DTO.TaskForProjectDTO;
import annanas_manager.entities.*;
import annanas_manager.entities.enums.TaskStatus;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.exceptions.TaskException;
import annanas_manager.repositories.CustomUserRepository;
import annanas_manager.repositories.ProjectRepository;
import annanas_manager.repositories.TaskForProjectRepository;
import annanas_manager.services.TaskForProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskForProjectServiceImpl implements TaskForProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CustomUserRepository userRepository;
    @Autowired
    private TaskForProjectRepository taskRepository;

    //tasks
    @Override
    public void addTask(long projectId, TaskForProjectDTO taskDTO, String email) throws ProjectException {
        try {
            Project project = projectRepository.findById(projectId);
            if (project.getCreatedBy().getEmail().equals(email)){
                taskDTO.setStatus(TaskStatus.NEW);
                taskDTO.setCreateDate(new Date(System.currentTimeMillis()));
                TaskForProject task = TaskForProject.fromDTO(taskDTO);
                task.setProject(project);
                task.setAssignedTo((Developer) userRepository.findOne(taskDTO.getAssignedTo().getId()));
                taskRepository.saveAndFlush(task);
            } else {
                throw new ProjectException("You have no permission to add task to this project", HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex){
            throw new NullPointerException("Project does not exist");
        }
    }

    @Override
    public void deleteTask(long projectId, long taskId, String email) throws ProjectException, TaskException {
        try {
            Project project = projectRepository.findById(projectId);
            if (project.getCreatedBy().getEmail().equals(email)){
                TaskForProject task = taskRepository.findOne(taskId);
                if (project.getTasks().contains(task)){
                    taskRepository.delete(taskId);
                } else {
                    throw new TaskException("Such task does not exist", HttpStatus.NOT_FOUND);
                }
            } else {
                throw new ProjectException("You have no permission to delete tasks from this project", HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex){
            throw new NullPointerException("Project does not exist");
        }
    }

    @Override
    public void editTask(long projectId, TaskForProjectDTO taskDTO, String email) throws TaskException, ProjectException {
        try {
            Project project = projectRepository.findById(projectId);
            if (project.getCreatedBy().getEmail().equals(email)){
                TaskForProject task = taskRepository.findOne(taskDTO.getId());
                if (project.getTasks().contains(task)){
                    task.setDescription(taskDTO.getDescription());
                    task.setDetails(taskDTO.getDetails());
                    task.setDeadline(taskDTO.getDeadline());
                    task.setAssignedTo((Developer) userRepository.findOne(taskDTO.getAssignedTo().getId()));
                    task.setApproved(taskDTO.isApproved());
                    taskRepository.saveAndFlush(task);
                } else {
                    throw new TaskException("Such task does not exist", HttpStatus.NOT_FOUND);
                }
            } else {
                throw new ProjectException("You have no permission to delete tasks from this project", HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex){
            throw new NullPointerException("Project does not exist");
        }
    }

    @Override
    public List<TaskForProjectDTO> getAllTasks(long projectId, String email) throws ProjectException {
        try {
            Project project = projectRepository.findById(projectId);
            if (project.getCreatedBy().getEmail().equals(email)){
                List<TaskForProject> tasks = taskRepository.findByProject(project);
                List<TaskForProjectDTO> tasksDTO = new ArrayList<>();
                for (TaskForProject task:tasks) {
                    tasksDTO.add(task.toDTO());
                }
                return tasksDTO;
            } else {
                throw new ProjectException("You have no permission to fetch tasks from this project", HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex){
            throw new NullPointerException("Project does not exist");
        }
    }

    @Override
    public TaskForProjectDTO findById(long projId, long id, String email) throws ProjectException {
        try {
            Project project = projectRepository.findOne(projId);
            if (hasUserPermission(project, email)){
                TaskForProject task = taskRepository.findOne(id);
                return task.toDTO();
            }
            throw new ProjectException("You have no permission to view this project", HttpStatus.FORBIDDEN);
        } catch (NullPointerException ex){
            throw new NullPointerException("Project does not exist");
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
