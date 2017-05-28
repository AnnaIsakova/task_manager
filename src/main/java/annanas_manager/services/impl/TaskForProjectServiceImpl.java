package annanas_manager.services.impl;


import annanas_manager.DTO.CustomUserDTO;
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
        Project project = projectRepository.findOne(projectId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        if (project.getCreatedBy().getEmail().equals(email)){
            taskDTO.setStatus(TaskStatus.NEW);
            taskDTO.setCreateDate(new Date(System.currentTimeMillis()));
            TaskForProject task = TaskForProject.fromDTO(taskDTO);
            task.setProject(project);
            try{
                task.setAssignedTo(userRepository.findOne(taskDTO.getAssignedTo().getId()));
            } catch (NullPointerException ex){
                task.setAssignedTo(null);
            }
            taskRepository.saveAndFlush(task);
        } else {
            throw new ProjectException("You have no permission to add task to this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void deleteTask(long projectId, long taskId, String email) throws ProjectException, TaskException {
        Project project = projectRepository.findOne(projectId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        if (project.getCreatedBy().getEmail().equals(email)){
            TaskForProject task = taskRepository.findOne(taskId);
            if (project.getTasks().contains(task)){
                project.getTasks().remove(task);
                taskRepository.delete(taskId);
            } else {
                throw new TaskException("Such task does not exist", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ProjectException("You have no permission to delete tasks from this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void editTask(long projectId, TaskForProjectDTO taskDTO, String email) throws TaskException, ProjectException {
        Project project = projectRepository.findOne(projectId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        if (project.getCreatedBy().getEmail().equals(email)){
            TaskForProject task = taskRepository.findOne(taskDTO.getId());
            if (project.getTasks().contains(task)){
                task.setDescription(taskDTO.getDescription());
                task.setDetails(taskDTO.getDetails());
                task.setDeadline(taskDTO.getDeadline());
                task.setPriority(taskDTO.getPriority());
                try{
                    task.setAssignedTo(userRepository.findOne(taskDTO.getAssignedTo().getId()));
                } catch (NullPointerException ex){
                    task.setAssignedTo(null);
                }
                task.setApproved(taskDTO.isApproved());
                taskRepository.saveAndFlush(task);
            } else {
                throw new TaskException("Such task does not exist", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ProjectException("You have no permission to delete tasks from this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public List<TaskForProjectDTO> getAllTasks(long projectId, String email) throws ProjectException {
        Project project = projectRepository.findOne(projectId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        List<TaskForProjectDTO> tasksDTO;
        CustomUser user = userRepository.findByEmail(email);
        if (project.getCreatedBy().getEmail().equals(email)){
            List<TaskForProject> tasks = project.getTasks();
            tasksDTO = new ArrayList<>();
            for (TaskForProject task:tasks) {
                tasksDTO.add(task.toDTO());
            }

        } else if (project.getDevelopers().contains(user)){
            Developer dev = (Developer) user;
            List<TaskForProject> tasks = dev.getTasks();
            tasksDTO = new ArrayList<>();
            for (TaskForProject task:tasks) {
                tasksDTO.add(task.toDTO());
            }
        } else {
            throw new ProjectException("You have no permission to fetch tasks from this project", HttpStatus.FORBIDDEN);
        }
        return tasksDTO;
    }

    @Override
    public TaskForProjectDTO findById(long projId, long id, String email) throws ProjectException {
        Project project = projectRepository.findOne(projId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        TaskForProject task = taskRepository.findOne(id);
        if (hasUserPermission(project, task, email)){
            return task.toDTO();
        }
        throw new ProjectException("You have no permission to view this project", HttpStatus.FORBIDDEN);
    }

    @Override
    public void changeStatus(long projId, long taskId, TaskStatus status, String email) throws TaskException {
        Project project = projectRepository.findOne(projId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        TaskForProject task = taskRepository.findOne(taskId);
        if (task != null && project.getTasks().contains(task) && task.getAssignedTo().getEmail().equals(email)){
            if (task.isApproved()){
                throw new TaskException("You can't change status of approved task", HttpStatus.CONFLICT);
            }
            task.setStatus(status);
            taskRepository.saveAndFlush(task);
        } else {
            throw new TaskException("Such task does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void approveTask(long projId, long taskId, String email) throws TaskException {
        Project project = projectRepository.findOne(projId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        TaskForProject task = taskRepository.findOne(taskId);
        if (task != null && project.getTasks().contains(task) && project.getCreatedBy().getEmail().equals(email)){
            if (!task.getStatus().equals(TaskStatus.COMPLETE)) {
                throw new TaskException("You can't approve not completed task", HttpStatus.CONFLICT);
            }
            task.setApproved(true);
            taskRepository.saveAndFlush(task);
        } else {
            throw new TaskException("Such task does not exist", HttpStatus.NOT_FOUND);
        }
    }

    private boolean hasUserPermission(Project project, TaskForProject task, String email){
        CustomUser user = userRepository.findByEmail(email);
        if (!project.getCreatedBy().equals(user) && task.getAssignedTo() == null){
            return false;
        }
        if ((project.getCreatedBy().equals(user) || task.getAssignedTo().getEmail().equals(email)) && project.getTasks().contains(task)){
            return true;
        }
        return false;
    }
}
