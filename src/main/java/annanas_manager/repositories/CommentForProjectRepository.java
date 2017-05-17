package annanas_manager.repositories;


import annanas_manager.entities.CommentForProject;
import annanas_manager.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentForProjectRepository  extends JpaRepository<CommentForProject, Long> {

    @Query("SELECT c FROM CommentForProject c WHERE c.project = :project")
    List<CommentForProject> findByProject(@Param("project") Project project);
}
