package annanas_manager.services;

import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.entities.CustomUser;

import java.util.List;


public interface CustomUserService {

    void add(CustomUserDTO customUserDTO);
    void delete(long id);
    CustomUserDTO getByName(String name);
    CustomUserDTO getByFullName(String firstName, String lastName);
    CustomUserDTO getByEmail(String email);
    void edit(CustomUserDTO customUserDTO);
    List<CustomUserDTO> getAll();
    boolean isUserExist(CustomUserDTO customUserDTO);
}
