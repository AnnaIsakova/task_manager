package annanas_manager.repositories;


import annanas_manager.entities.CustomFile;
import annanas_manager.entities.FileForProject;
import annanas_manager.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileForProjectRepository extends JpaRepository<FileForProject, Long> {

    @Query("SELECT f FROM FileForProject f WHERE f.project = :project")
    List<FileForProject> findByProject(@Param("project") Project project);
}
