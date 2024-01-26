package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCegmlGiudMedLegale;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCegmlGiudMedLegaleDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link TpCegmlGiudMedLegale} and its DTO {@link TpCegmlGiudMedLegaleDTO}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TpCegmlGiudMedLegaleMapper extends GenericMapper<TpCegmlGiudMedLegaleDTO, TpCegmlGiudMedLegale> {
}
