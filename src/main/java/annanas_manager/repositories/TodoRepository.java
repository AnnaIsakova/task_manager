package annanas_manager.repositories;


import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Task;
import annanas_manager.entities.TaskTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Task, Long> {

    @Query("SELECT u FROM TaskTodo u WHERE u.createdBy = :createdBy")
    List<TaskTodo> findByUser(@Param("createdBy") CustomUser customUser);
}
