package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import it.almaviva.difesa.cessazione.procedure.model.common.GenericRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReasonCessationRequest extends GenericRequest implements Serializable {

    private static final long serialVersionUID = -620043600693047379L;

    @NotNull
    private String prattAcrAtt;
    @NotNull
    private String prtpoAcrProc;
    @NotNull
    private String stfaaAcrFfaa;
    @NotNull
    private Long categoryPers;

}
