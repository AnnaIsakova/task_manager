package annanas_manager.entities;

import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.entities.enums.UserRole;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "users")
public class CustomUser {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    @Column(name = "id", length = 6, nullable = false)
    private long id;

    @Size(min = 3)
    @Column(name = "firstName", length = 32, nullable = false)
    private String firstName;

    @Column(name = "lastName", length = 32, nullable = false)
    private String lastName;

    @Size(min = 3)
    @Column(name = "password", length = 32, nullable = false)
    private String password;

    @Size(min = 3)
    @Column(name = "email", length = 32, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "createdBy", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Task> todoList;

    public CustomUser() {}

    public CustomUser(String firstName, String lastName, String password, String email, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public CustomUserDTO toDTO() {
        return new CustomUserDTO(id, firstName, lastName, password, email, role);
    }

    public static CustomUser fromDTO(CustomUserDTO dto) {
        return new CustomUser(dto.getFirstName(), dto.getLastName(), dto.getPassword(), dto.getEmail(), dto.getUserRole());
    }

    @Override
    public String toString() {
        return "CustomUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Set<Task> getTodoList() {
        return todoList;
    }

    public void setTodoList(Set<Task> todoList) {
        this.todoList = todoList;
    }
}
