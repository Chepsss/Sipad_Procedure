package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.model.common.GenericRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddressElectedRequest extends GenericRequest implements Serializable {

    private static final long serialVersionUID = -8890541374581514058L;

    private Long idProc;
    @NotNull
    private Long employeeId;
    private Long idProvincia;
    private Long idComune;
    @Size(max = 5, message = ErrorsConst.CAP_NOT_VALID)
    private String cap;
    private String indirizzo;
    private Long idNazione;
    private Long idCatPers;

}
