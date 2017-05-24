package annanas_manager.entities;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.DeveloperDTO;
import annanas_manager.entities.enums.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "D")
public class Developer extends CustomUser{

    @ManyToMany(mappedBy = "developers", cascade = CascadeType.ALL)
    private List<Project> projects  = new ArrayList<>();

    @OneToMany(mappedBy = "assignedTo", cascade= CascadeType.PERSIST)
    private List<TaskForProject> tasks = new ArrayList<>();

    public Developer() {
    }

    public Developer(String firstName, String lastName, String password, String email) {
        super(firstName, lastName, password, email);
        super.setRole(UserRole.DEVELOPER);
    }

    public static Developer fromDTO(CustomUserDTO dto) {
        return new Developer(dto.getFirstName(), dto.getLastName(), dto.getPassword(), dto.getEmail());
    }

    @Override
    public DeveloperDTO toDTO() {
        return new DeveloperDTO(id, firstName, lastName, password, email, role);
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<TaskForProject> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskForProject> tasks) {
        this.tasks = tasks;
    }
}
