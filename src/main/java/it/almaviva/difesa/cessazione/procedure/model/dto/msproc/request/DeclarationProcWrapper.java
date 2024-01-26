package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import it.almaviva.difesa.cessazione.procedure.model.common.GenericRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeclarationProcWrapper extends GenericRequest implements Serializable {

    private static final long serialVersionUID = -5644760685136489642L;

    private Long idProc;
    @NotNull
    private Long employeeId;
    private Long idCatPers;
    @NotEmpty
    private List<DeclarationProcRequest> declarations;

}
