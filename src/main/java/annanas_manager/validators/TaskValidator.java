package annanas_manager.validators;


import annanas_manager.DTO.TaskDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Calendar;


@Component
public class TaskValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return TaskDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TaskDTO task = (TaskDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty");
        if (task.getDescription().length() > 255){
            errors.rejectValue("description", "Size.description");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "priority", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deadline", "NotEmpty");
        if (!DeadlineValidator.isDeadlineValid(task.getDeadline())){
            errors.rejectValue("deadline", "Before.deadline");
        }
    }
}
