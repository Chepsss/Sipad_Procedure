package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TbCeProcParereRagDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class ParereRagRequest extends TbCeProcParereRagDTO {

    private static final long serialVersionUID = 3715055863001010894L;

    private Long idProc;
    @NotNull
    private Long employeeId;
    private Long idCatPers;

}
