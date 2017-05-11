package annanas_manager.exceptions;


import org.springframework.http.HttpStatus;

public class CustomUserException extends Exception {
    private HttpStatus httpStatus;
    private String errorMessage;


    public CustomUserException(String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
    public CustomUserException() {
        super();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
