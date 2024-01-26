package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.DeclarationProcedure;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.DeclarationProcedureDTOResponse;
import it.almaviva.difesa.cessazione.procedure.model.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link DeclarationProcedure} and its DTO {@link DeclarationProcedureDTOResponse}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DeclarationProcedureMapper extends GenericMapper<DeclarationProcedureDTOResponse, DeclarationProcedure> {
}
