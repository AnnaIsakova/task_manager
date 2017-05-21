package annanas_manager.validators;


import annanas_manager.DTO.ProjectDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Calendar;

@Component
public class ProjectValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ProjectDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProjectDTO projectDTO = (ProjectDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deadline", "NotEmpty");
        if (!DeadlineValidator.isDeadlineValid(projectDTO.getDeadline())){
            errors.rejectValue("deadline", "Before.deadline");
        }
    }


}
