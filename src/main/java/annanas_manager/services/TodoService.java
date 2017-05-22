package annanas_manager.services;


import annanas_manager.DTO.TaskTodoDTO;
import annanas_manager.exceptions.TaskException;

import java.util.List;

public interface TodoService {
    void add(TaskTodoDTO taskDTO, String email);
    void delete(long id, String email) throws TaskException;
    void edit(TaskTodoDTO taskDTO , String email) throws TaskException;
    List<TaskTodoDTO> findByUser(String email);
}
