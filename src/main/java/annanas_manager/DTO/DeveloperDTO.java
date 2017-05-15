package annanas_manager.DTO;


import annanas_manager.entities.enums.UserRole;

public class DeveloperDTO extends CustomUserDTO{

    public DeveloperDTO() {
    }

    public DeveloperDTO(long id, String firstName, String lastName, String password, String email, UserRole role) {
        super(id, firstName, lastName, password, email, role);
    }

}
