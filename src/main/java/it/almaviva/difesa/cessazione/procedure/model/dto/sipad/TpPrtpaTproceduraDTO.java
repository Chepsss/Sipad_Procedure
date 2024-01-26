package it.almaviva.difesa.cessazione.procedure.model.dto.sipad;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TpPrtpaTproceduraDTO implements Serializable {

    private static final long serialVersionUID = 789290496803761934L;

    private Long id;
    private String prtpaDescrProc;
    private String prtpaAcrProc;
    private LocalDate prtpaDataIniz;
    private LocalDate prtpaDataFine;

}
