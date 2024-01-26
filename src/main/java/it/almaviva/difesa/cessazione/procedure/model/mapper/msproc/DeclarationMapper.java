package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.Declaration;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.DeclarationDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Declaration} and its DTO {@link DeclarationDTO}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DeclarationMapper extends GenericMapper<DeclarationDTO, Declaration> {
}
