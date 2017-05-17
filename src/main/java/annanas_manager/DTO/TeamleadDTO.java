package annanas_manager.DTO;


import annanas_manager.entities.enums.UserRole;

public class TeamleadDTO extends CustomUserDTO {

    public TeamleadDTO() {
    }

    public TeamleadDTO(long id, String firstName, String lastName, String password, String email, UserRole role) {
        super(id, firstName, lastName, password, email, role);
    }
}
