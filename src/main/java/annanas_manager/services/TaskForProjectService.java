package annanas_manager.services;


import annanas_manager.DTO.TaskForProjectDTO;
import annanas_manager.entities.enums.TaskStatus;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.exceptions.TaskException;

import java.util.List;

public interface TaskForProjectService {

    void addTask(long projectId, TaskForProjectDTO taskDTO, String email) throws ProjectException;
    void deleteTask(long projectId, long taskId, String email) throws ProjectException, TaskException;
    void editTask (long projectId, TaskForProjectDTO taskDTO, String email) throws TaskException, ProjectException;
    List<TaskForProjectDTO> getAllTasks(long projectId, String email) throws ProjectException;
    TaskForProjectDTO findById(long projId, long taskId, String email) throws ProjectException;
    void changeStatus(long projId, long taskId, TaskStatus status, String email) throws TaskException;
    void approveTask(long projId, long taskId, String email) throws TaskException;
}
