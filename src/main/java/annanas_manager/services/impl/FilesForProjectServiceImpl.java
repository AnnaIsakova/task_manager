package annanas_manager.services.impl;


import annanas_manager.DTO.FileForProjectDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.FileForProject;
import annanas_manager.entities.Project;
import annanas_manager.exceptions.CustomFileException;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.repositories.CustomUserRepository;
import annanas_manager.repositories.FileForProjectRepository;
import annanas_manager.repositories.ProjectRepository;
import annanas_manager.services.FilesForProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class FilesForProjectServiceImpl implements FilesForProjectService{

    @Autowired
    private FileForProjectRepository filesRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CustomUserRepository userRepository;

    public static final String DIR_PATH = "D:\\Study_prog\\Java\\AnnanasManager\\src\\main\\resources\\static\\uploaded_files\\";

    @Override
    public void addFile(long id, MultipartFile multipartFile, String emailCreatedBy) throws ProjectException, CustomFileException {
        Project project = projectRepository.findOne(id);
        if(project == null){
            throw new NullPointerException("Project does not exist");
        }
        if (project.getCreatedBy().getEmail().equals(emailCreatedBy)){
            long currTime = System.currentTimeMillis();
            FileForProject forProject = convertFile(multipartFile, currTime);
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
        Project project = projectRepository.findOne(projectID);
        if(project == null){
            throw new NullPointerException("Project does not exist");
        }
        if (hasUserPermission(project, email)){
            FileForProject file = filesRepository.getOne(fileId);
            if (file != null && project.getFiles().contains(file)){
                return file.toDTO();
            } else {
                throw new CustomFileException("Such file does no exist", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ProjectException("You have no permission to download file from this project", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void deleteFile(long projectID, long fileId, String emailCreatedBy) throws ProjectException, CustomFileException {
        Project project = projectRepository.findOne(projectID);
        if(project == null){
            throw new NullPointerException("Project does not exist");
        }
            if (project.getCreatedBy().getEmail().equals(emailCreatedBy)){
                FileForProject file = filesRepository.getOne(fileId);
                if (file != null && project.getFiles().contains(file)){
                    project.getFiles().remove(file);
                    filesRepository.delete(fileId);
                    File file1 = new File(DIR_PATH + file.getCurrentTime() + "-" + file.getName());
                    file1.delete();
                } else {
                    throw new CustomFileException("Such file does no exist", HttpStatus.NOT_FOUND);
                }
            } else {
                throw new ProjectException("You have no permission to delete file from this project", HttpStatus.FORBIDDEN);
            }
    }

    @Override
    public List<FileForProjectDTO> getAllFiles(long id, String email) throws ProjectException {
        Project project = projectRepository.findOne(id);
        if(project == null){
            throw new NullPointerException("Project does not exist");
        }
        if (hasUserPermission(project, email)){
            List<FileForProject> files = filesRepository.findByProject(project);
            List<FileForProjectDTO> filesDTO = new ArrayList<>();
            for (FileForProject file:files) {
                filesDTO.add(file.toDTO());
            }
            return filesDTO;
        } else {
            throw new ProjectException("You have no permission to fetch files from this project", HttpStatus.FORBIDDEN);
        }
    }

    private boolean hasUserPermission(Project project, String email){
        CustomUser user = userRepository.findByEmail(email);
        if (project.getCreatedBy().equals(user) || project.getDevelopers().contains(user)){
            return true;
        }
        return false;
    }

    private FileForProject convertFile(MultipartFile multipartFile, long currTime) throws CustomFileException {
        File convFile = new File(DIR_PATH + currTime + "-" + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(convFile);
        } catch (IOException e) {
            throw new CustomFileException("Some problems with file happened", HttpStatus.BAD_REQUEST);
        }
        return new FileForProject(multipartFile.getOriginalFilename());
    }
}
