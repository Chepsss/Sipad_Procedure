package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureHistory;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ProcedureHistory} entity.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcedureHistoryDTO implements Serializable {

    private static final long serialVersionUID = 382837481848047500L;

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime assignmentDate;
    private ProcedureDTO procedure;
    private StateProcedureDTO stateProcedure;
    private String roleCode;
    private Boolean flagAttuale;

}
