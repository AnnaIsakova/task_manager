package annanas_manager.exceptions;


import org.springframework.http.HttpStatus;

public class TaskForProjectException extends Exception {
    private HttpStatus httpStatus;
    private String errorMessage;

    public TaskForProjectException(String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
    public TaskForProjectException() {
        super();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
