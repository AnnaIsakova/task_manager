package annanas_manager.services.impl;

import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.repositories.CustomUserRepository;
import annanas_manager.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserServiceImpl implements CustomUserService {

    @Autowired
    private CustomUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public CustomUser add(CustomUserDTO user) {
        CustomUser customUser = CustomUser.fromDTO(user);
        String encryptedPass = bCryptPasswordEncoder.encode(customUser.getPassword());
        customUser.setPassword(encryptedPass);
        return userRepository.saveAndFlush(customUser);
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    @Override
    public CustomUser getByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public CustomUser getByFullName(String firstName, String lastName) {
        return userRepository.findByFullName(firstName, lastName);
    }

    @Override
    public CustomUser getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public CustomUser edit(CustomUser user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public List<CustomUser> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean isUserExist(CustomUserDTO user) {
        return getByEmail(user.getEmail())!=null;
    }
}
