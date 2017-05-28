package annanas_manager.services;


import annanas_manager.DTO.CommentForTaskDTO;
import annanas_manager.exceptions.CommentException;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.exceptions.TaskException;

import java.util.List;

public interface CommentsForTaskService {

    void addComment(long projectId, long taskId, CommentForTaskDTO commentDTO, String email) throws ProjectException, TaskException;
    void deleteComment(long projectId, long taskId, long commentId, String email) throws ProjectException, CommentException, TaskException;
    void editComment(long projectId, long taskId, CommentForTaskDTO commentDTO, String email) throws CommentException, ProjectException, TaskException;
    List<CommentForTaskDTO> getAllComments(long id, long taskId, String email) throws ProjectException, TaskException;
}
