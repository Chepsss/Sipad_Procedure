package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link Procedure} entity.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProcedureSearchRequest extends RiassegnazioneSearchRequest implements Serializable {

    private static final long serialVersionUID = -8696079601003024237L;

    private Long idLoggedInUser;
    private String visibility;
    private Long idFase;
    private String assignedFullName;
    private String procedureCode;
    private LocalDateTime assignmentDate;

}