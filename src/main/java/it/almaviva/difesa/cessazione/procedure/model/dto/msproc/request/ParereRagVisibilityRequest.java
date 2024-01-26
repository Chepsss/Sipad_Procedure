package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParereRagVisibilityRequest implements Serializable {

    private static final long serialVersionUID = 3787539280388964983L;

    private String stateProcedure;
    private Boolean flVistoRagioneria;

}
