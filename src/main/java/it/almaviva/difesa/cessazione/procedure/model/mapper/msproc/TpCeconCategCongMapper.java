package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCeconCategCong;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCeconCategCongDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link TpCeconCategCong} and its DTO {@link TpCeconCategCongDTO}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TpCeconCategCongMapper extends GenericMapper<TpCeconCategCongDTO, TpCeconCategCong> {
}
