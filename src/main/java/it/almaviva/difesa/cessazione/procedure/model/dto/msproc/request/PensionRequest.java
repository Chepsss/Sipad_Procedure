package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TbCeProcPensioneDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class PensionRequest extends TbCeProcPensioneDTO {

    private static final long serialVersionUID = 2400050284613233112L;

    private Long idProc;
    @NotNull
    private Long employeeId;
    private Long idCatPers;

}
