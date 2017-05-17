package annanas_manager.exceptions;


import org.springframework.http.HttpStatus;

public class CommentException extends Exception {
    private HttpStatus httpStatus;
    private String errorMessage;


    public CommentException(String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
    public CommentException() {
        super();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
