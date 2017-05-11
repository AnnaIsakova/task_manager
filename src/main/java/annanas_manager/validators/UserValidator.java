package annanas_manager.validators;


import annanas_manager.DTO.CustomUserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^(\\S+)@(\\S+)\\.(\\S+)$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean supports(Class<?> aClass) {
        return CustomUserDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CustomUserDTO customUser = (CustomUserDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty");
        if (customUser.getFirstName().length() < 3 || customUser.getFirstName().length() > 32) {
            errors.rejectValue("firstName", "Size.firstName");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty");
        if (customUser.getLastName().length() < 3 || customUser.getLastName().length() > 32) {
            errors.rejectValue("lastName", "Size.lastName");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (customUser.getEmail().length() < 3 || customUser.getEmail().length() > 32) {
            errors.rejectValue("email", "Size.email");
        }
        if (!isValidEmail(customUser.getEmail())){
            errors.rejectValue("email", "Invalid.email");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (customUser.getPassword().length() < 5 || customUser.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.password");
        }
        if (customUser.getUserRole() == null) {
            errors.reject("role");
        }
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }
}
