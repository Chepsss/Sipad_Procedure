package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatoPredisposizioneMessage implements Serializable {

    private String message;
    private HttpStatus esito;

}
