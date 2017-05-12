package annanas_manager.services.impl;

import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.Developer;
import annanas_manager.entities.Teamlead;
import annanas_manager.entities.enums.UserRole;
import annanas_manager.exceptions.CustomUserException;
import annanas_manager.repositories.CustomUserRepository;
import annanas_manager.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserServiceImpl implements CustomUserService, UserDetailsService {

    @Autowired
    private CustomUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) {
        CustomUser user = userRepository.findByUsername(email);
        if (user == null) {
            System.out.println("NOT FOUND");
            throw new UsernameNotFoundException(email);
        }
        return new CustomUserPrincipal(user);
    }

    @Override
    public void add(CustomUserDTO user) throws CustomUserException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new CustomUserException("Such user already exists", HttpStatus.CONFLICT);
        }
        CustomUser customUser = null;
        if (user.getUserRole().equals(UserRole.DEVELOPER)){
            customUser = Developer.fromDTO(user);
        } else if (user.getUserRole().equals(UserRole.TEAMLEAD)){
            customUser = Teamlead.fromDTO(user);
        }

        String encryptedPass = bCryptPasswordEncoder.encode(customUser.getPassword());
        customUser.setPassword(encryptedPass);
        userRepository.saveAndFlush(customUser);
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    @Override
    public CustomUserDTO getByName(String name) {
        CustomUser user = userRepository.findByName(name);
        return user.toDTO();
    }

    @Override
    public CustomUserDTO getByFullName(String firstName, String lastName) {
        CustomUser user = userRepository.findByFullName(firstName, lastName);
        return user.toDTO();
    }

    @Override
    public CustomUserDTO getByEmail(String email) {
        CustomUser user = userRepository.findByEmail(email);
        if (user != null){
            return user.toDTO();
        }
        return null;
    }

    @Override
    public void edit(CustomUserDTO customUserDTO) {
        CustomUser customUser = null;
        if (customUserDTO.getUserRole().equals(UserRole.DEVELOPER)){
            customUser = Developer.fromDTO(customUserDTO);
        } else if (customUserDTO.getUserRole().equals(UserRole.TEAMLEAD)){
            customUser = Teamlead.fromDTO(customUserDTO);
        }
        userRepository.saveAndFlush(customUser);
    }

    @Override
    public List<CustomUserDTO> getAll() {
        List<CustomUser> users = userRepository.findAll();
        List<CustomUserDTO> userDTOs = new ArrayList<>();
        for (CustomUser user:users) {
            userDTOs.add(user.toDTO());
        }
        return userDTOs;
    }

    @Override
    public boolean isUserExist(CustomUserDTO user) {
        return getByEmail(user.getEmail())!=null;
    }
}
