package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.model.common.CustomError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChangeProcedureStateDTO implements Serializable {

    private static final long serialVersionUID = 6878200492762324009L;

    @JsonIgnore
    private Long idProc;
    @JsonIgnore
    private String oldState;
    @JsonIgnore
    private String newState;
    private String okMSG;
    private String globalErrorMSG;
    private List<CustomError> errors;

}
