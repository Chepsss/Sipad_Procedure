package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class RiassegnazioneSearchRequest implements Serializable {

    private static final long serialVersionUID = 4569525422733825810L;

    private Long idState;
    @NotNull
    private String roleCode;
    private String employeeFiscalCode;
    private String employeeSurname;
    private String employeeName;
    @NotNull
    private String tipoTask;

}