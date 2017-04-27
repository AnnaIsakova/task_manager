package annanas_manager.services;

import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.entities.CustomUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface CustomUserService {

    CustomUser add(CustomUserDTO user);
    void delete(long id);
    CustomUser getByName(String name);
    CustomUser getByFullName(String firstName, String lastName);
    CustomUser getByEmail(String email);
    CustomUser edit(CustomUser customUser);
    List<CustomUser> getAll();
    boolean isUserExist(CustomUserDTO customUserDTO);
}
