package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.FaseProcedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.FaseProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.StateProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.GenericMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Procedure} and its DTO {@link ProcedureDTO}.
 */
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface ProcedureMapper extends GenericMapper<ProcedureDTO, Procedure> {

    @Mapping(target = "stateProcedure", source = "stateProcedure", qualifiedByName = "stateProcedureId")
    @Mapping(target = "faseProcedure", source = "stateProcedure.faseProcedure", qualifiedByName = "faseProcedureId")
    @Mapping(target = "reasonCessation", source = "reasonCessation")
    @Mapping(target = "openingCessationId", source = "openingCessation")
    @Mapping(target = "typeCessationId", source = "typeCessation")
    @Mapping(target = "categLeaveReqId", source = "categLeaveReq")
    @Mapping(target = "catMilitareId", source = "idCatMilitare")
    @Mapping(target = "authorityId", source = "idEnte")
    @Mapping(target = "authorityId_cc1", source = "idEnte_cc1")
    @Mapping(target = "authorityId_cc2", source = "idEnte_cc2")
    @Mapping(target = "authorityId_cc3", source = "idEnte_cc3")
    @Mapping(target = "catDocumentoId", source = "catDocumento")
    @Mapping(target = "tpStproProvinciaId", source = "idProvincia")
    ProcedureDTO toDto(Procedure s);

    @Named("stateProcedureId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codeState", source = "codeState")
    @Mapping(target = "descState", source = "descState")
    StateProcedureDTO toDtoStateProcedureId(StateProcedure stateProcedure);

    @Named("faseProcedureId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FaseProcedureDTO toDtoFaseProcedureId(FaseProcedure faseProcedure);

}
