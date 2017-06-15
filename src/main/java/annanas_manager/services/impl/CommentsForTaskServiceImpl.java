package annanas_manager.services.impl;


import annanas_manager.DTO.CommentForTaskDTO;
import annanas_manager.entities.CommentForTask;
import annanas_manager.entities.Project;
import annanas_manager.entities.TaskForProject;
import annanas_manager.exceptions.CommentException;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.exceptions.TaskException;
import annanas_manager.repositories.CommentForTaskRepository;
import annanas_manager.repositories.CustomUserRepository;
import annanas_manager.repositories.ProjectRepository;
import annanas_manager.repositories.TaskForProjectRepository;
import annanas_manager.services.CommentsForTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CommentsForTaskServiceImpl implements CommentsForTaskService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CustomUserRepository userRepository;
    @Autowired
    private CommentForTaskRepository commentRepository;
    @Autowired
    private TaskForProjectRepository taskRepository;

    @Override
    public void addComment(long projectId, long taskId, CommentForTaskDTO commentDTO, String email) throws ProjectException, TaskException {
        Project project = projectRepository.findOne(projectId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        TaskForProject task = taskRepository.findOne(taskId);
        if (!project.getTasks().contains(task)){
            throw new TaskException("Task does not exist", HttpStatus.NOT_FOUND);
        }
        if (!hasUserPermission(project, task, email)){
            throw new TaskException("You have no permission to add comment to this task", HttpStatus.FORBIDDEN);
        }
        CommentForTask comment = CommentForTask.fromDTO(commentDTO);
        comment.setCreateDate(new Date(System.currentTimeMillis()));
        comment.setUserFrom(userRepository.findByEmail(email));
        comment.setTask(task);
        commentRepository.saveAndFlush(comment);
    }

    @Override
    public void deleteComment(long projectId, long taskId, long commentId, String email) throws ProjectException, CommentException, TaskException {
        Project project = projectRepository.findOne(projectId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        TaskForProject task = taskRepository.findOne(taskId);
        if (task == null || !project.getTasks().contains(task)){
            throw new TaskException("Task does not exist", HttpStatus.NOT_FOUND);
        }
        CommentForTask comment = commentRepository.findOne(commentId);
        if (comment != null && comment.getUserFrom().getEmail().equals(email) && task.getComments().contains(comment)){
            task.getComments().remove(comment);
            commentRepository.delete(commentId);
        } else {
            throw new CommentException("Comment does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void editComment(long projectId, long taskId, CommentForTaskDTO commentDTO, String email) throws CommentException, ProjectException, TaskException {
        Project project = projectRepository.findOne(projectId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        TaskForProject task = taskRepository.findOne(taskId);
        if (task == null || !project.getTasks().contains(task)){
            throw new TaskException("Task does not exist", HttpStatus.NOT_FOUND);
        }
        CommentForTask comment = commentRepository.findOne(commentDTO.getId());
        if (comment != null && comment.getUserFrom().getEmail().equals(email) && task.getComments().contains(comment)){
            comment.setText(commentDTO.getText());
            comment.setLastModified(new Date(System.currentTimeMillis()));
            commentRepository.saveAndFlush(comment);
        } else {
            throw new CommentException("Comment does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<CommentForTaskDTO> getAllComments(long projectId, long taskId, String email) throws ProjectException, TaskException {
        Project project = projectRepository.findOne(projectId);
        if (project == null){
            throw new NullPointerException("Project does not exist");
        }
        TaskForProject task = taskRepository.findOne(taskId);
        if (!project.getTasks().contains(task)){
            throw new TaskException("Task does not exist", HttpStatus.NOT_FOUND);
        }
        if (!hasUserPermission(project, task, email)){
            throw new TaskException("You have no permission to fetch comments from this task", HttpStatus.FORBIDDEN);
        }
        List<CommentForTask> comments = commentRepository.findByTask(task);
        List<CommentForTaskDTO> commentsDTO = new ArrayList<>();
        for (CommentForTask comment:comments) {
            commentsDTO.add(comment.toDTO());
        }
        return commentsDTO;
    }

    private boolean hasUserPermission(Project project, TaskForProject task, String email){
        if (project.getCreatedBy().getEmail().equals(email) || task.getAssignedTo().getEmail().equals(email)){
            return true;
        }
        return false;
    }
}
