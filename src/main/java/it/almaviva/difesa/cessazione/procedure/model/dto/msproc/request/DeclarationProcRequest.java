package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import it.almaviva.difesa.cessazione.procedure.model.common.GenericRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeclarationProcRequest extends GenericRequest implements Serializable {

    private static final long serialVersionUID = -4189718037320183086L;

    @NotNull
    private Long idDich;
    @NotNull
    private Boolean flagDich;
    @NotNull
    private Date dataDich;
    private String numVerbCommAv;

}
