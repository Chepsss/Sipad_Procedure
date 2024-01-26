package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCenorNormativa;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCenorNormativaDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link TpCenorNormativa} and its DTO {@link TpCenorNormativaDTO}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TpCenorNormativaMapper extends GenericMapper<TpCenorNormativaDTO, TpCenorNormativa> {
}