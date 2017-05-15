package annanas_manager.exceptions;


import org.springframework.http.HttpStatus;

public class CustomFileException extends Exception {
    private HttpStatus httpStatus;
    private String errorMessage;


    public CustomFileException(String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
    public CustomFileException() {
        super();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
