package annanas_manager.entities;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.entities.enums.UserRole;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@DiscriminatorValue(value = "T")
public class Teamlead extends CustomUser{

    @OneToMany(mappedBy = "createdBy", cascade= CascadeType.ALL)
    private List<Project> projects;

    public Teamlead() {
    }

    public Teamlead(String firstName, String lastName, String password, String email) {
        super(firstName, lastName, password, email);
        super.setRole(UserRole.TEAMLEAD);
    }

    public static Teamlead fromDTO(CustomUserDTO dto) {
        return new Teamlead(dto.getFirstName(), dto.getLastName(), dto.getPassword(), dto.getEmail());
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
