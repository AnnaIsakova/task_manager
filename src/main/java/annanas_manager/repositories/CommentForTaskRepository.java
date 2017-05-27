package annanas_manager.repositories;


import annanas_manager.entities.CommentForTask;
import annanas_manager.entities.TaskForProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentForTaskRepository  extends JpaRepository<CommentForTask, Long> {

    @Query("SELECT c FROM CommentForTask c WHERE c.task = :task")
    List<CommentForTask> findByTask(@Param("task") TaskForProject project);
}
