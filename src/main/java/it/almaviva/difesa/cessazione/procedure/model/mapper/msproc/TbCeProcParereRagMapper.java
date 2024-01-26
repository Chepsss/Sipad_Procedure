package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcParereRag;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TbCeProcParereRagDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ParereRagRequest;
import it.almaviva.difesa.cessazione.procedure.model.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link TbCeProcParereRag} and its DTO {@link TbCeProcParereRagDTO}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TbCeProcParereRagMapper extends GenericMapper<TbCeProcParereRagDTO, TbCeProcParereRag> {

    @Named("updateTbCeProcParereRagFromDto")
    void updateTbCeProcParereRagFromParereRagRequestDTO(@MappingTarget TbCeProcParereRag parere, ParereRagRequest request);

}
