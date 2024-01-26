package it.almaviva.difesa.cessazione.procedure.model.dto.sipad;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class TpPrtpoTprocedimentoDTO implements Serializable {

    private static final long serialVersionUID = 4479413477547136832L;

    private Long id;
    private String prtpoDescrProc;
    private String prtpoAcrProc;
    private LocalDate prtpoDataIniz;
    private LocalDate prtpoDataFine;
    private Long prtpoNumGgDurlav;
    private String prtpoStatoProc;
    private Character prtpoFlagGrado;
    private Character prtpoFlagColl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TpPrtpaTproceduraDTO prtpoPrtpa;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TpPrtpoTprocedimentoDTO prtpoPrtpoIdArs;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TpPrtpoTprocedimentoDTO prtpoPrtpoIdSup;

}
