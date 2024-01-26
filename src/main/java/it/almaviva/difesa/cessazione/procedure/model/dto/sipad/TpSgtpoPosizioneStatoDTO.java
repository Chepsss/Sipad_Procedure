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
public class TpSgtpoPosizioneStatoDTO implements Serializable {

    private static final long serialVersionUID = 7495320502519103614L;

    private String id;
    private String sgtpoDescrPosizione;
    private String sgtpoAcrPosizione;
    private LocalDate sgtpoDataIniz;
    private LocalDate sgtpoDataFine;
    private Long sgtpoSgctsId;
    private TpSgctpCatpersonaleDTO sgtpoSgctp;
    private String sgtpoCodSipad1;
    private String sgtpoCodUid;
    private String sgtpoCodConf;
    private Long sgtpoValFermin;
    private Long sgtpoValFermax;
    private String sgtpoFlagProl;
    private Long sgtpoValFeragg;

}
