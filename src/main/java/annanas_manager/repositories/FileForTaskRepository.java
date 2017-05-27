package annanas_manager.repositories;


import annanas_manager.entities.FileForTask;
import annanas_manager.entities.TaskForProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileForTaskRepository extends JpaRepository<FileForTask, Long> {

    @Query("SELECT f FROM FileForTask f WHERE f.task = :task")
    List<FileForTask> findByProject(@Param("task") TaskForProject task);
}
