package annanas_manager.entities;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.entities.enums.UserRole;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "D")
public class Developer extends CustomUser{

    @ManyToMany(mappedBy = "developers", cascade = CascadeType.ALL)
    private List<Project> projects  = new ArrayList<>();

    public Developer() {
    }

    public Developer(String firstName, String lastName, String password, String email) {
        super(firstName, lastName, password, email);
        super.setRole(UserRole.DEVELOPER);
    }

    public static Developer fromDTO(CustomUserDTO dto) {
        return new Developer(dto.getFirstName(), dto.getLastName(), dto.getPassword(), dto.getEmail());
    }

}
