package annanas_manager.services.impl;


import annanas_manager.DTO.*;
import annanas_manager.entities.*;
import annanas_manager.entities.enums.TaskStatus;
import annanas_manager.entities.enums.UserRole;
import annanas_manager.exceptions.CommentException;
import annanas_manager.exceptions.CustomFileException;
import annanas_manager.exceptions.CustomUserException;
import annanas_manager.exceptions.ProjectException;
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

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CustomUserRepository userRepository;
    @Autowired
    private FileForProjectRepository filesRepository;
    @Autowired
    private CommentForProjectRepository commentRepository;
    @Autowired
    private TaskForProjectRepository taskRepository;

    public static final String DIR_PATH = "D:\\Study_prog\\Java\\AnnanasManager\\src\\main\\resources\\static\\uploaded_files\\";

    //project
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
    }

    @Override
    public void edit(ProjectDTO projectDTO, String email) throws ProjectException {
        Project project = projectRepository.findById(projectDTO.getId());
        if (project.getCreatedBy().getEmail().equals(email)){
            project.setDescription(projectDTO.getDescription());
            project.setDetails(projectDTO.getDetails());
            project.setDeadline(projectDTO.getDeadline());
            System.out.println("project from service: " + projectDTO);
            projectRepository.saveAndFlush(project);
        } else {
            throw new ProjectException("You have no permission to edit this task", HttpStatus.FORBIDDEN);
        }
    }


    @Override
    public List<ProjectDTO> findByUser(String email) {
        CustomUser customUser = userRepository.findByEmail(email);
        List<Project> projects = null;
        if (customUser instanceof Teamlead){
            projects = ((Teamlead) customUser).getProjects();
        } else if (customUser instanceof Developer){
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
        Project project = projectRepository.findOne(id);
        if (hasUserPermission(project, email)){
            return project.toDTO();
        }
        throw new ProjectException("You have no permission to view this project", HttpStatus.FORBIDDEN);
    }

    //developers
    @Override
    public void addDeveloper(long id, String emailDev, String emailCreatedBy) throws ProjectException, CustomUserException {
        Project project = projectRepository.findById(id);
        if (project.getCreatedBy().getEmail().equals(emailCreatedBy)){
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
                throw new CustomUserException("Such developer does not exist", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ProjectException("You have no permission to add developer to this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void deleteDeveloper(long projectId, long devId, String emailCreatedBy) throws ProjectException, CustomUserException {
        Project project = projectRepository.findById(projectId);
        if (project.getCreatedBy().getEmail().equals(emailCreatedBy)){
            CustomUser userDev = userRepository.findOne(devId);
            if (userDev instanceof Developer){
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
        Project project = projectRepository.findById(projectId);
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

    //files
    @Override
    public void addFile(long id, MultipartFile multipartFile, String emailCreatedBy) throws ProjectException {
        Project project = projectRepository.findById(id);
        if (project.getCreatedBy().getEmail().equals(emailCreatedBy)){
            long currTime = System.currentTimeMillis();
            File convFile = new File(DIR_PATH + currTime + "-" + multipartFile.getOriginalFilename());
            try {
                multipartFile.transferTo(convFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileForProject forProject = new FileForProject(multipartFile.getOriginalFilename(), convFile);
            forProject.setProject(project);
            forProject.setCurrentTime(currTime);
            project.getFiles().add(forProject);
            filesRepository.saveAndFlush(forProject);
        } else {
            throw new ProjectException("You have no permission to add file to this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public FileForProjectDTO getFile(long projectID, long fileId, String email) throws ProjectException, CustomFileException {
        Project project = projectRepository.findById(projectID);
        if (hasUserPermission(project, email)){
            FileForProject file = filesRepository.getOne(fileId);
            if (project.getFiles().contains(file)){
                return file.toDTO();
            } else {
                throw new CustomFileException("This file doesn't belong to this project", HttpStatus.CONFLICT);
            }
        } else {
            throw new ProjectException("You have no permission to download file from this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void deleteFile(long projectID, long fileId, String emailCreatedBy) throws ProjectException, CustomFileException {
        Project project = projectRepository.findById(projectID);
        if (project.getCreatedBy().getEmail().equals(emailCreatedBy)){
            FileForProject file = filesRepository.getOne(fileId);
            if (project.getFiles().contains(file)){
                project.getFiles().remove(file);
                filesRepository.delete(fileId);
                File file1 = new File(DIR_PATH + file.getCurrentTime() + "-" + file.getName());
                file1.delete();
                System.out.println(fileId);
            } else {
                throw new CustomFileException("This file doesn't belong to this project", HttpStatus.CONFLICT);
            }
        } else {
            throw new ProjectException("You have no permission to delete file from this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public List<FileForProjectDTO> getAllFiles(long id, String email) throws ProjectException {
        Project project = projectRepository.findById(id);
        if (hasUserPermission(project, email)){
            List<FileForProject> files = filesRepository.findByProject(project);
            List<FileForProjectDTO> filesDTO = new ArrayList<>();
            for (FileForProject file:files) {
                filesDTO.add(file.toDTO());
            }
            return filesDTO;
        } else {
            throw new ProjectException("You have no permission to download file from this project", HttpStatus.FORBIDDEN);
        }
    }

    //comments
    @Override
    public void addComment(long id, CommentForProjectDTO commentDTO, String email) throws ProjectException {
        Project project = projectRepository.findById(id);
        if (hasUserPermission(project, email)){
            CommentForProject comment = CommentForProject.fromDTO(commentDTO);
            comment.setCreateDate(new Date(System.currentTimeMillis()));
            comment.setUserFrom(userRepository.findByEmail(email));
            comment.setProject(project);
            commentRepository.saveAndFlush(comment);
        } else {
            throw new ProjectException("You have no permission to add comment to this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void deleteComment(long projectID, long commentId, String email) throws ProjectException, CommentException {
        Project project = projectRepository.findById(projectID);
        if (hasUserPermission(project, email)){
            CommentForProject comment = commentRepository.findOne(commentId);
            if (comment.getUserFrom().getEmail().equals(email)){
                commentRepository.delete(commentId);
            } else {
                throw new CommentException("You have no permission to delete this comment", HttpStatus.FORBIDDEN);
            }
        } else {
            throw new ProjectException("You have no permission to delete comment from this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void editComment(long projectID, CommentForProjectDTO commentDTO, String email) throws CommentException, ProjectException {
        Project project = projectRepository.findById(projectID);
        if (hasUserPermission(project, email)){
            CommentForProject comment = commentRepository.findOne(commentDTO.getId());
            if (comment.getUserFrom().getEmail().equals(email)){
                comment.setText(commentDTO.getText());
                comment.setLastModified(new Date(System.currentTimeMillis()));
                commentRepository.saveAndFlush(comment);
            } else {
                throw new CommentException("You have no permission to edit this comment", HttpStatus.FORBIDDEN);
            }
        } else {
            throw new ProjectException("You have no permission to edit comment from this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public List<CommentForProjectDTO> getAllComments(long id, String email) throws ProjectException {
        Project project = projectRepository.findById(id);
        if (hasUserPermission(project, email)){
            List<CommentForProject> comments = commentRepository.findByProject(project);
            List<CommentForProjectDTO> commentsDTO = new ArrayList<>();
            for (CommentForProject comment:comments) {
                commentsDTO.add(comment.toDTO());
            }
            return commentsDTO;
        } else {
            throw new ProjectException("You have no permission to fetch comments from this project", HttpStatus.FORBIDDEN);
        }
    }

    //tasks
    @Override
    public void addTask(long projectId, TaskForProjectDTO taskDTO, String email) throws ProjectException {
        Project project = projectRepository.findById(projectId);
        if (project.getCreatedBy().getEmail().equals(email)){
            taskDTO.setStatus(TaskStatus.NEW);
            taskDTO.setCreateDate(new Date(System.currentTimeMillis()));
            TaskForProject task = TaskForProject.fromDTO(taskDTO);
            task.setCreatedBy(userRepository.findByEmail(email));
            task.setProject(project);
            task.setAssignedTo((Developer) userRepository.findOne(taskDTO.getAssignedTo().getId()));
            System.out.println(task);
            taskRepository.saveAndFlush(task);
        } else {
            throw new ProjectException("You have no permission to add task to this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void deleteTask(long projectId, long taskId, String email) {

    }

    @Override
    public void editTask(long projectId, TaskForProjectDTO taskDTO, String email) {

    }

    @Override
    public List<TaskForProjectDTO> getAllTasks(long projectId, String email) throws ProjectException {
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
    }

    private boolean hasUserPermission(Project project, String email){
        CustomUser user = userRepository.findByEmail(email);
        if (project.getCreatedBy().equals(user) || project.getDevelopers().contains(user)){
            return true;
        }
        return false;
    }
}
