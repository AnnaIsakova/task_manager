package annanas_manager.DTO;

import annanas_manager.entities.enums.UserRole;
import annanas_manager.entities.enums.UserRoleDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CustomUserDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private UserRole role;

    public CustomUserDTO() {}

    public CustomUserDTO(long id, String firstName, String lastName, String password, String email, UserRole role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @JsonProperty("role")
    public UserRole getUserRole() {
        return role;
    }

    @JsonProperty("role")
    @JsonDeserialize(using = UserRoleDeserializer.class)
    public void setUserRole(UserRole role) {
        this.role = role;
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

    @Override
    public String toString() {
        return "CustomUserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
