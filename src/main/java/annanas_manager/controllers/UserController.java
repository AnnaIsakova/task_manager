package annanas_manager.controllers;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.exceptions.CustomUserException;
import annanas_manager.exceptions.ErrorResponse;
import annanas_manager.services.CustomUserService;
import annanas_manager.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "api/users/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<CustomUserDTO>> getAllUsers(){
        List<CustomUserDTO> users = customUserService.getAll();
        if(users.isEmpty()){
            return new ResponseEntity<List<CustomUserDTO>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<CustomUserDTO>>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "api/users/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<CustomUserDTO> getMe(
            @PathVariable("id") long id){
        CustomUserDTO user = customUserService.getById(id);
        return new ResponseEntity<CustomUserDTO>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "api/users/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> editUser(
            @RequestBody CustomUserDTO userDTO,
            BindingResult bindingResult,
            Principal principal) throws CustomUserException {
        userValidator.setEditingUser(true);
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()){
            throw new CustomUserException("Invalid form", HttpStatus.BAD_REQUEST);
        }
        customUserService.edit(userDTO, principal.getName());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(CustomUserException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getHttpStatus().value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, ex.getHttpStatus());
    }
}
