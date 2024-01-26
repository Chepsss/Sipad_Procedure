package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.FaseProcedure;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link FaseProcedure} entity.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaseProcedureDTO implements Serializable {

    private static final long serialVersionUID = -7487489239289058299L;

    private final Long id;
    private final String codeFase;
    private final String descFase;

}
