package annanas_manager.services;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.TaskDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Task;

import java.util.List;

public interface TaskService {
    void add(TaskDTO taskDTO);
    boolean delete(long id, String email);
    boolean edit(TaskDTO task , String email);
    List<TaskDTO> findByUser(CustomUserDTO customUserDTO);
    CustomUserDTO findUser(String email);
}
