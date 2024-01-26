package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PriorityRequest implements Serializable {

    private static final long serialVersionUID = -6156463838384706686L;

    private Long procedureId;
    private Integer priority;

}
