package annanas_manager.services.impl;


import annanas_manager.DTO.CommentForProjectDTO;
import annanas_manager.entities.CommentForProject;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Project;
import annanas_manager.exceptions.CommentException;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.repositories.CommentForProjectRepository;
import annanas_manager.repositories.CustomUserRepository;
import annanas_manager.repositories.ProjectRepository;
import annanas_manager.services.CommentForProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentForProjectServiceImpl implements CommentForProjectService{

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CustomUserRepository userRepository;
    @Autowired
    private CommentForProjectRepository commentRepository;

    //comments
    @Override
    public void addComment(long id, CommentForProjectDTO commentDTO, String email) throws ProjectException {
        try {
            Project project = projectRepository.findOne(id);
            if (hasUserPermission(project, email)){
                CommentForProject comment = CommentForProject.fromDTO(commentDTO);
                comment.setCreateDate(new Date(System.currentTimeMillis()));
                comment.setUserFrom(userRepository.findByEmail(email));
                comment.setProject(project);
                commentRepository.saveAndFlush(comment);
            } else {
                throw new ProjectException("You have no permission to add comment to this project", HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex){
            throw new NullPointerException("Project does not exist");
        }
    }

    @Override
    public void deleteComment(long projectID, long commentId, String email) throws ProjectException, CommentException {
        try {
            Project project = projectRepository.findOne(projectID);
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
        } catch (NullPointerException ex){
            throw new NullPointerException("Project does not exist");
        }
    }

    @Override
    public void editComment(long projectID, CommentForProjectDTO commentDTO, String email) throws CommentException, ProjectException {
        try {
            Project project = projectRepository.findOne(projectID);
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
        } catch (NullPointerException ex){
            throw new NullPointerException("Project does not exist");
        }
    }

    @Override
    public List<CommentForProjectDTO> getAllComments(long id, String email) throws ProjectException {
        try {
            Project project = projectRepository.findOne(id);
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
