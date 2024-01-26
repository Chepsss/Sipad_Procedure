package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrtpoTprocedimentoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgctpCatpersonaleDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TpCetipCessazioneCompleteDTO extends TpCetipCessazioneDTO {

    private static final long serialVersionUID = -860472890413760634L;

    private TpSgctpCatpersonaleDTO cetipSgctpSeqPk;
    private TpPrtpoTprocedimentoDTO cetipPrtpoSeqPk;

    public TpCetipCessazioneCompleteDTO(TpCetipCessazioneDTO source) {
        super();
        this.setId(source.getId());
        this.setCetipMotivoCessazione(source.getCetipMotivoCessazione());
        this.setCetipSgtpoSeqPk(source.getCetipSgtpoSeqPk());
        this.setCetipAcrTiv(source.getCetipAcrTiv());
        this.setIdMotivo(source.getIdMotivo());
        this.setIdCetipSgctpSeqPk(source.getIdCetipSgctpSeqPk());
        this.setIdCetipPrtpoSeqPk(source.getIdCetipPrtpoSeqPk());
        this.setCetipCenorSeqPk(source.getCetipCenorSeqPk());
    }

}
