package annanas_manager.repositories;


import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p WHERE p.createdBy = :createdBy")
    List<Project> findByTeamlead(@Param("createdBy") CustomUser customUser);

    @Query("SELECT p FROM Project p WHERE p.id = :id")
    Project findById(@Param("id") long id);
}
