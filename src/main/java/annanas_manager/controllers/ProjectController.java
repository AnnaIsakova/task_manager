package annanas_manager.controllers;


import annanas_manager.DTO.CustomUserDTO;
import annanas_manager.DTO.ProjectDTO;
import annanas_manager.exceptions.ProjectException;
import annanas_manager.services.CustomUserService;
import annanas_manager.services.ProjectService;
import annanas_manager.validators.ProjectValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;

@RestController
public class ProjectController {

    @Autowired
    ProjectService projectService;
    @Autowired
    ProjectValidator projectValidator;
    @Autowired
    CustomUserService customUserService;

    @RequestMapping(value = "api/projects", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ProjectDTO>> getAllTasks(Principal principal)
    {
        CustomUserDTO userDTO = customUserService.getByEmail(principal.getName());
        System.out.println(principal.getName());
        List<ProjectDTO> tasks = projectService.findByUser(userDTO);
        if(tasks.isEmpty()){
            return new ResponseEntity<List<ProjectDTO>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<ProjectDTO>>(tasks, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/projects/new", method = RequestMethod.POST)
    public ResponseEntity<Void> createTask(
            @RequestBody ProjectDTO projectDTO,
            Principal principal,
            BindingResult bindingResult) throws ProjectException
    {
        ignoreDeadlineTime(projectDTO.getDeadline());
        projectValidator.validate(projectDTO, bindingResult);
        if (bindingResult.hasErrors()){
            throw new ProjectException("Invalid project form", HttpStatus.BAD_REQUEST);
        }
        CustomUserDTO user = customUserService.getByEmail(principal.getName());
        projectDTO.setCreatedBy(user);
        projectService.add(projectDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    private void ignoreDeadlineTime(Calendar deadline){
        deadline.clear(Calendar.HOUR_OF_DAY);
        deadline.clear(Calendar.MINUTE);
        deadline.clear(Calendar.SECOND);
        deadline.clear(Calendar.MILLISECOND);
    }
}
