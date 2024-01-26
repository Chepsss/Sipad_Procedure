package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TpCetipCessazioneDTO implements Serializable {

    private static final long serialVersionUID = 5547582747033828898L;

    private Long id;
    private String cetipMotivoCessazione;
    private String cetipSgtpoSeqPk;
    private String cetipAcrTiv;
    private String idMotivo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long idCetipSgctpSeqPk;

    private Long idCetipPrtpoSeqPk;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TpCenorNormativaDTO cetipCenorSeqPk;

}
