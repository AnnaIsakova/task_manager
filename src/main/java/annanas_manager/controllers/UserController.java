package annanas_manager.controllers;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.entities.CustomUser;
import annanas_manager.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private CustomUserService customUserService;

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
}
