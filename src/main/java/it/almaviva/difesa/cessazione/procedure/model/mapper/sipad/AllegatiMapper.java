package it.almaviva.difesa.cessazione.procedure.model.mapper.sipad;

import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.AllegatoDto;
import it.almaviva.difesa.documenti.document.model.dto.response.allegati.AllegatiOutput;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface AllegatiMapper {

    AllegatoDto copyProperties(AllegatiOutput allegatiOutput);

}
