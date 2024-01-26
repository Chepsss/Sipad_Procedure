package it.almaviva.difesa.cessazione.procedure.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CamundaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus status;

    public CamundaException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
