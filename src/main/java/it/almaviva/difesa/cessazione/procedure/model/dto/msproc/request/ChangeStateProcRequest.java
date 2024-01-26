package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import it.almaviva.difesa.cessazione.procedure.model.common.GenericRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChangeStateProcRequest extends GenericRequest implements Serializable {

    private static final long serialVersionUID = -5877869033647121487L;

    @NotNull
    private Long idProc;
    private String codeState;
    private Long assigneeId;
    private String note;
    private Integer priorita;

}
