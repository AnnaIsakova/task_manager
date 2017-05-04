package annanas_manager.repositories;


import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT u FROM Task u WHERE u.createdBy = :createdBy")
    List<Task> findByUsername(@Param("createdBy") CustomUser customUser);
}
