package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCetipCessazione;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCetipCessazioneCompleteDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCetipCessazioneDTO;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TpCetipCessazioneCompleteMapper {

    private final SipadClient sipadClient;
    private final TpCetipCessazioneMapper mapper;

    public synchronized TpCetipCessazioneCompleteDTO toDto(TpCetipCessazione tpCetipCessazione) {
        TpCetipCessazioneDTO tpCetipCessazioneDTO = mapper.toDto(tpCetipCessazione);
        return toDto(tpCetipCessazioneDTO);
    }

    public TpCetipCessazioneCompleteDTO toDto(TpCetipCessazioneDTO source) {
        TpCetipCessazioneCompleteDTO target = new TpCetipCessazioneCompleteDTO(source);

        if (Objects.nonNull(source.getIdCetipPrtpoSeqPk())) {
            target.setCetipPrtpoSeqPk(sipadClient.tipoProcedimentoById(source.getIdCetipPrtpoSeqPk()));
        }
        if (Objects.nonNull(source.getIdCetipSgctpSeqPk())) {
            target.setCetipSgctpSeqPk(sipadClient.catMilitareById(source.getIdCetipSgctpSeqPk()));
        }
        return target;
    }

}
