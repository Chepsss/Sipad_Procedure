package it.almaviva.difesa.cessazione.procedure.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Setter
@Getter
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final HttpStatus status;
    private final List<String> errors;

    public InvalidTokenException(String message, HttpStatus status, List<String> errors) {
        super(message);
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public InvalidTokenException(String message, HttpStatus status, List<String> errors, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

}
