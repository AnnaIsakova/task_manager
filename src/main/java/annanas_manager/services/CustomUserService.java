package annanas_manager.services;

import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.exceptions.CustomUserException;

import java.util.List;


public interface CustomUserService {

    void add(CustomUserDTO customUserDTO) throws CustomUserException;
    void delete(long id);
    CustomUserDTO getByEmail(String email);
    CustomUserDTO getById(long id);
    void edit(CustomUserDTO customUserDTO, String email) throws CustomUserException;
    List<CustomUserDTO> getAll();
}
