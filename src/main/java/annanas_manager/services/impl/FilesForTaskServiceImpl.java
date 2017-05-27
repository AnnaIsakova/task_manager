package annanas_manager.services.impl;


import annanas_manager.DTO.FileForTaskDTO;
import annanas_manager.entities.FileForTask;
import annanas_manager.entities.Project;
import annanas_manager.entities.TaskForProject;
import annanas_manager.exceptions.CustomFileException;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.exceptions.TaskException;
import annanas_manager.repositories.CustomUserRepository;
import annanas_manager.repositories.FileForTaskRepository;
import annanas_manager.repositories.ProjectRepository;
import annanas_manager.repositories.TaskForProjectRepository;
import annanas_manager.services.FilesForTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class FilesForTaskServiceImpl implements FilesForTaskService {

    @Autowired
    private FileForTaskRepository filesRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskForProjectRepository taskRepository;

    public static final String DIR_PATH = "D:\\Study_prog\\Java\\AnnanasManager\\src\\main\\resources\\static\\uploaded_files\\";

    @Override
    public void addFile(long projectId, long taskId, MultipartFile multipartFile, String emailCreatedBy) throws ProjectException, CustomFileException, TaskException {
        Project project = projectRepository.findOne(projectId);
        if(project == null){
            throw new NullPointerException("Project does not exist");
        }
        if (!project.getCreatedBy().getEmail().equals(emailCreatedBy)){
            throw new ProjectException("You have no permission to add file to this project", HttpStatus.FORBIDDEN);
        }
        TaskForProject task = taskRepository.findOne(taskId);
        if (task == null || !project.getTasks().contains(task)){
            throw new TaskException("Such task does not exist", HttpStatus.NOT_FOUND);
        }
        long currTime = System.currentTimeMillis();
        FileForTask forTask = convertFile(multipartFile, currTime);
        forTask.setTask(task);
        forTask.setCurrentTime(currTime);
        task.getFiles().add(forTask);
        filesRepository.saveAndFlush(forTask);
    }

    @Override
    public FileForTaskDTO getFile(long projectID, long taskId, long fileId, String email) throws ProjectException, CustomFileException, TaskException {
        Project project = projectRepository.findOne(projectID);
        if(project == null){
            throw new NullPointerException("Project does not exist");
        }
        TaskForProject task = taskRepository.findOne(taskId);
        if (!project.getCreatedBy().getEmail().equals(email) || !task.getAssignedTo().getEmail().equals(email)) {
            throw new TaskException("You have no permission to download file from this task", HttpStatus.FORBIDDEN);
        }

        FileForTask file = filesRepository.getOne(fileId);
        if (file != null && task.getFiles().contains(file)){
            return file.toDTO();
        } else {
            throw new CustomFileException("Such file does no exist", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteFile(long projectID, long taskId, long fileId, String email) throws ProjectException, CustomFileException, TaskException {
        Project project = projectRepository.findOne(projectID);
        if(project == null){
            throw new NullPointerException("Project does not exist");
        }
        if (!project.getCreatedBy().getEmail().equals(email)){
            throw new ProjectException("You have no permission to delete file from this project", HttpStatus.FORBIDDEN);
        }
        TaskForProject task = taskRepository.findOne(taskId);
        if (task == null || !project.getTasks().contains(task)){
            throw new TaskException("Such task does not exist", HttpStatus.NOT_FOUND);
        }
        FileForTask file = filesRepository.getOne(fileId);
        if (file == null || !task.getFiles().contains(file)){
            throw new CustomFileException("Such file does no exist", HttpStatus.NOT_FOUND);
        }
        task.getFiles().remove(file);
        filesRepository.delete(fileId);
        File file1 = new File(DIR_PATH + file.getCurrentTime() + "-" + file.getName());
        file1.delete();
    }

    @Override
    public List<FileForTaskDTO> getAllFiles(long projectID, long taskId, String email) throws ProjectException, TaskException {
        Project project = projectRepository.findOne(projectID);
        if(project == null){
            throw new NullPointerException("Project does not exist");
        }
        TaskForProject task = taskRepository.findOne(taskId);
        if (!project.getCreatedBy().getEmail().equals(email) && !task.getAssignedTo().getEmail().equals(email)) {
            throw new TaskException("You have no permission to fetch files from this task", HttpStatus.FORBIDDEN);
        }
        List<FileForTask> files = filesRepository.findByTask(task);
        List<FileForTaskDTO> filesDTO = new ArrayList<>();
        for (FileForTask file:files) {
            filesDTO.add(file.toDTO());
        }
        return filesDTO;
    }

    private FileForTask convertFile(MultipartFile multipartFile, long currTime) throws CustomFileException {
        File convFile = new File(DIR_PATH + currTime + "-" + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(convFile);
        } catch (IOException e) {
            throw new CustomFileException("Some problems with file happened", HttpStatus.BAD_REQUEST);
        }
        return new FileForTask(multipartFile.getOriginalFilename());
    }
}
