package annanas_manager.validators;


import annanas_manager.DTO.TaskDTO;
import annanas_manager.entities.Task;
import annanas_manager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Date;


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
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "priority", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deadline", "NotEmpty");
        if (!isDeadlineValid(task.getDeadline())){
            errors.rejectValue("deadline", "Before.deadline");
        }
    }

    private boolean isDeadlineValid(Date deadline){
        Date today = new Date(System.currentTimeMillis());
        if (!deadline.before(today)){
            return true;
        } else {
            return false;
        }
    }
}
