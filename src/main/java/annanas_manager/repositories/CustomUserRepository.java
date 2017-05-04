package annanas_manager.repositories;

import annanas_manager.entities.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

    @Query("SELECT u FROM CustomUser u where u.firstName = :name OR u.lastName = :name")
    CustomUser findByName(@Param("name") String name);

    @Query("SELECT u FROM CustomUser u WHERE u.firstName = :firstName AND u.lastName = :lastName")
    CustomUser findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT u FROM CustomUser u WHERE u.email = :email")
    CustomUser findByEmail(@Param("email") String email);

    @Query("SELECT u FROM CustomUser u WHERE u.email = :email")
    CustomUser findByUsername(@Param("email") String email);
}
