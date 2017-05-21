package annanas_manager.validators;


import annanas_manager.DTO.TaskForProjectDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component
public class TaskForProjectValidator extends TaskValidator{
    @Override
    public void validate(Object o, Errors errors) {
        super.validate(o, errors);
        TaskForProjectDTO task = (TaskForProjectDTO) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "assignedTo", "NotEmpty");
    }
}
