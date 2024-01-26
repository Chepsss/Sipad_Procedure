package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcPensione;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TbCeProcPensioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.PensionRequest;
import it.almaviva.difesa.cessazione.procedure.model.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link TbCeProcPensione} and its DTO {@link TbCeProcPensioneDTO}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TbCeProcPensioneMapper extends GenericMapper<TbCeProcPensioneDTO, TbCeProcPensione> {

    @Named("updateTbCeProcPensioneFromDto")
    void updateTbCeProcPensioneFromPensionRequestDTO(@MappingTarget TbCeProcPensione pensione, PensionRequest request);

}
