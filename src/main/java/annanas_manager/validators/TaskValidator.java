package annanas_manager.validators;


import annanas_manager.DTO.TaskDTO;
import annanas_manager.entities.Task;
import annanas_manager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    private boolean isDeadlineValid(Calendar deadline){
        Calendar now = Calendar.getInstance();
        now.clear(Calendar.HOUR_OF_DAY);
        now.clear(Calendar.MINUTE);
        now.clear(Calendar.SECOND);
        now.clear(Calendar.MILLISECOND);
        if (!now.after(deadline)){
            return true;
        }
        return false;
    }
}
