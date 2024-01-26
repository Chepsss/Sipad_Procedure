package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link StateProcedure} entity.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateProcedureDTO implements Serializable {

    private static long serialVersionUID = -2954097496850894224L;

    private Long id;
    private String codeState;
    private String descState;

}
