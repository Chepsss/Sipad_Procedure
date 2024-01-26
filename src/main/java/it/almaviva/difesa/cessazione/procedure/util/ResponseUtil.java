package it.almaviva.difesa.cessazione.procedure.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface ResponseUtil {
    static <T> ResponseEntity<T> wrapOrNotFound(Optional<T> maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    static <T> ResponseEntity<T> wrapOrNotFound(Optional<T> maybeResponse, HttpHeaders header) {
        return maybeResponse.map((ResponseEntity.ok().headers(header))::body)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}