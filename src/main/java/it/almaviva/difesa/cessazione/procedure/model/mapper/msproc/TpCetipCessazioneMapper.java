package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCetipCessazione;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCetipCessazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TpCetipCessazioneMapper extends GenericMapper<TpCetipCessazioneDTO, TpCetipCessazione> {

    @Mapping(target = "idCetipSgctpSeqPk", source = "cetipSgctpSeqPk")
    @Mapping(target = "idCetipPrtpoSeqPk", source = "cetipPrtpoSeqPk")
    TpCetipCessazioneDTO toDto(TpCetipCessazione entity);

}
