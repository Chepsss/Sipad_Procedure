package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.UserDetailResponseDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RiassegnazioneDTO implements Serializable {

    private static final long serialVersionUID = 1995128668032108259L;

    private Long id;
    private String codeProcess;
    private String author;
    @NotNull
    private Long idAssignedTo;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime assignmentDate;

    @NotNull
    private Long employeeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime lastModifiedDate;

    private ProcedureEmployeeDTO employee;
    private UserDetailResponseDTO assignedTo;

    private StateProcedureDTO stateProcedure;
    private FaseProcedureDTO faseProcedure;

}
