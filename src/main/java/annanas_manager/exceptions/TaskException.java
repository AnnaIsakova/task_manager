package annanas_manager.exceptions;


import org.springframework.http.HttpStatus;

public class TaskException extends Exception {
    private HttpStatus httpStatus;
    private String errorMessage;


    public TaskException(String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
    public TaskException() {
        super();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
