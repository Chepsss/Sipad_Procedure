package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcTransito;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.TbCeProcTransitoDTORequest;
import it.almaviva.difesa.cessazione.procedure.model.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link TbCeProcTransito} and its DTO {@link TbCeProcTransitoDTORequest}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TbCeProcTransitoMapper extends GenericMapper<TbCeProcTransitoDTORequest, TbCeProcTransito> {

    @Named("updateTbCeProcTransitoFromDto")
    @Mapping(source = "flagIdoneoTransito", target = "flagIdoneoTransito", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "flagPresentataIstanza", target = "flagPresentataIstanza", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateTbCeProcTransitoFromTransitRequestDTO(@MappingTarget TbCeProcTransito transito, TbCeProcTransitoDTORequest request);

}
