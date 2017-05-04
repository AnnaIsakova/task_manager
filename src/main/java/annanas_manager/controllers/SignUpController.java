package annanas_manager.controllers;

import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.entities.enums.UserRole;
import annanas_manager.services.CustomUserService;
import annanas_manager.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
public class SignUpController {

    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public List<UserRole> getRoles(){
        return UserRole.getRoles();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody CustomUserDTO user, UriComponentsBuilder ucBuilder, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()){
            System.out.println("errors from validator: " + bindingResult.getAllErrors());
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        customUserService.add(user);
//        long id = customUserService.getByEmail(user.getEmail()).getId();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(id).toUri());
//        System.out.println(id + "=> ID");
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    //for async validation on front-end
    @RequestMapping(value = "/user/{email:.+}", method = RequestMethod.GET)
    public ResponseEntity<Void> validateEmail(@PathVariable("email") String email) {
        if (customUserService.getByEmail(email) != null){
            return new ResponseEntity<Void>(HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
    }
}
