package it.almaviva.difesa.cessazione.procedure.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ChangeStateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int statusCode;
    private String globalError;
    private final HttpStatus status;

    public ChangeStateException(String globalError, HttpStatus status) {
        super(globalError);
        this.status = status;
    }

}
