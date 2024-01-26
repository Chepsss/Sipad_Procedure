package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.model.common.GenericRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TbCeProcParereRagDTO extends GenericRequest implements Serializable {

    private static final long serialVersionUID = -1284498537117252158L;

    private Boolean esito;
    private Long numRegistrazione;
    private LocalDate dataEsito;

}
