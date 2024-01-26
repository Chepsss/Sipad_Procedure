package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.model.dto.difesa_sipad_main_be_centrale_api.ProcedimentoQueue;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.ProceduresArchiveDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwDo007ProcedimentiDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the DTO {@link ProceduresArchiveDTO} , DTO {@link VwDo007ProcedimentiDTO} and DTO {@link ProcedimentoQueue}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface ProceduresArchiveMapper {

    @Mapping(target = "codFisc", source = "do007CodFisc")
    @Mapping(target = "prproId", source = "do007PrproId")
    @Mapping(target = "prproCodPro", source = "do007PrproCodPro")
    @Mapping(target = "dataAvvio", source = "do007DataAvvio")
    @Mapping(target = "dataFine", source = "do007DataFine")
    @Mapping(target = "fase", source = "do007Fase")
    @Mapping(target = "stato", source = "do007Stato")
    @Mapping(target = "tipoProc", source = "do007TipoProc")
    @Mapping(target = "autore", source = "do007Autore")
    ProceduresArchiveDTO copyProperties(VwDo007ProcedimentiDTO source);

    @Mapping(target = "codFisc", source = "codiceFiscaleAutoreProcedimento")
    @Mapping(target = "prproId", source = "idProcedimento")
    @Mapping(target = "prproCodPro", source = "codiceProcedimento")
    @Mapping(target = "dataAvvio", source = "dataAvvioProcedimento")
    @Mapping(target = "dataFine", source = "dataFineProcedimento")
    @Mapping(target = "fase", source = "faseProcedimento")
    @Mapping(target = "stato", source = "statoProcedimento")
    @Mapping(target = "tipoProc", source = "tipoProcedimento")
    @Mapping(target = "autore", source = "codiceFiscaleAutoreProcedimento")
    ProceduresArchiveDTO copyProperties(ProcedimentoQueue source);

}
