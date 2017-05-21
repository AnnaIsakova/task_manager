package annanas_manager.repositories;


import annanas_manager.entities.Developer;
import annanas_manager.entities.Project;
import annanas_manager.entities.TaskForProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskForProjectRepository extends JpaRepository<TaskForProject, Long> {

    @Query("SELECT u FROM TaskForProject u WHERE u.assignedTo = :assignedTo")
    List<TaskForProject> findByDeveloper(@Param("assignedTo") Developer developer);

    @Query("SELECT u FROM TaskForProject u WHERE u.project = :project")
    List<TaskForProject> findByProject(@Param("project") Project project);
}
