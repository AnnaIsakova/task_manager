package annanas_manager.exceptions;


import org.springframework.http.HttpStatus;

public class ProjectException extends Exception {
    private HttpStatus httpStatus;
    private String errorMessage;

    public ProjectException(String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
    public ProjectException() {
        super();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
