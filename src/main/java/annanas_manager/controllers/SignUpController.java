package annanas_manager.controllers;

import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.entities.enums.UserRole;
import annanas_manager.services.CustomUserService;
import annanas_manager.services.SecurityService;
import annanas_manager.validators.UserValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
public class SignUpController {
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserValidator userValidator;


    @RequestMapping(value = "api/roles", method = RequestMethod.GET)
    @ResponseBody
    public List<UserRole> getRoles(){
        return UserRole.getRoles();
    }

    @RequestMapping(value = "api/user/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<CustomUserDTO>> getAllUsers(){
        List<CustomUser> users = customUserService.getAll();
        List<CustomUserDTO> usersDTO = new ArrayList<>();
        for(CustomUser user : users){
            usersDTO.add(user.toDTO());
        }
        if(users.isEmpty()){
            return new ResponseEntity<List<CustomUserDTO>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<CustomUserDTO>>(usersDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "api/user/", method = RequestMethod.POST)
    public ResponseEntity<Long> createUser(@RequestBody CustomUserDTO user, UriComponentsBuilder ucBuilder, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()){
            System.out.println("errors from validator: " + bindingResult.getAllErrors());
            return new ResponseEntity<Long>(HttpStatus.CONFLICT);
        }
        customUserService.add(user);
        securityService.autologin(user.getEmail(), user.getPassword());
        long id = customUserService.getByEmail(user.getEmail()).getId();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(id).toUri());
        System.out.println(id + "=> ID");
        return new ResponseEntity<Long>(id, HttpStatus.CREATED);
    }

    //for async validation on front-end
    @RequestMapping(value = "/api/user/{email:.+}", method = RequestMethod.GET)
    public ResponseEntity<Void> validateEmail(@PathVariable("email") String email) {
        if (customUserService.getByEmail(email) != null){
            return new ResponseEntity<Void>(HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/afterRegister", method = RequestMethod.GET)
    public ResponseEntity<Void> afterTest() {
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
