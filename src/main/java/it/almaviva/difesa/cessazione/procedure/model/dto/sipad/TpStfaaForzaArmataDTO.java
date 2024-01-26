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
public class TpStfaaForzaArmataDTO implements Serializable {

    private static final long serialVersionUID = 350870485324222841L;

    private String id;
    private String stfaaDescrFfaa;
    private String stfaaAcrFfaa;
    private LocalDate stfaaDataIniz;
    private LocalDate stfaaDataFine;
    private Character stfaaFlagEsposiz;
    private String stfaaCodUid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TpStfaaForzaArmataDTO stfaaStfaa;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TpAntprPersonaleDTO stfaaAntpr;

}
