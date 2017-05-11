package annanas_manager.services;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.TaskDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Task;
import annanas_manager.exceptions.TaskException;

import java.util.List;

public interface TaskService {
    void add(TaskDTO taskDTO);
    void delete(long id, String email) throws TaskException;
    void edit(TaskDTO task , String email) throws TaskException;
    List<TaskDTO> findByUser(CustomUserDTO customUserDTO);
    CustomUserDTO findUser(String email);
}
