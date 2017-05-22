package annanas_manager.services;


import annanas_manager.DTO.CommentForProjectDTO;
import annanas_manager.exceptions.CommentException;
import annanas_manager.exceptions.ProjectException;

import java.util.List;

public interface CommentForProjectService {
    //methods for comments
    void addComment(long id, CommentForProjectDTO commentDTO, String email) throws ProjectException;
    void deleteComment(long projectID, long commentId, String email) throws ProjectException, CommentException;
    void editComment(long projectID, CommentForProjectDTO commentDTO, String email) throws CommentException, ProjectException;
    List<CommentForProjectDTO> getAllComments(long id, String email) throws ProjectException;
}
