package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCegmlGiudMedLegale;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCetipCessazione;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureOpeningDataDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.GenericMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Procedure} and its DTO {@link ProcedureOpeningDataDTO}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ProcedureOpeningDataMapper extends GenericMapper<ProcedureOpeningDataDTO, Procedure> {

    ProcedureOpeningDataDTO toDto(Procedure entity);

    @Mapping(target = "stateProcedure", source = "stateProcedureId", qualifiedByName = "stateProcedureId")
    @Mapping(target = "openingCessation", source = "idTipoAttivazione")
    @Mapping(target = "typeCessation", source = "idTipoProcedimento")
    @Mapping(target = "reasonCessation", source = "idTipoCessazione", qualifiedByName = "idTipoCessazione")
    @Mapping(target = "categLeaveReq", source = "idCatPersRichiesta")
    @Mapping(target = "idCatMilitare", source = "idCatPers")
    @Mapping(target = "catDocumento", source = "idTipoComunicazione")
    @Mapping(target = "tpCegmlGiudMedLegale", source = "idTipoGml1", qualifiedByName = "idTipoGml1")
    @Mapping(target = "tpCegmlGiudMedLegale2", source = "idTipoGml2", qualifiedByName = "idTipoGml2")
    Procedure toEntity(ProcedureOpeningDataDTO dto);

    @Named("stateProcedureId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StateProcedure toStateProcedure(Long id);

    @Named("idTipoCessazione")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TpCetipCessazione toTpCetipCessazione(Long id);

    @Named("idTipoGml1")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TpCegmlGiudMedLegale toTpCegmlGiudMedLegale(String id);

    @Named("idTipoGml2")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TpCegmlGiudMedLegale toTpCegmlGiudMedLegale2(String id);

    @Mapping(target = "stateProcedure", source = "stateProcedureId", qualifiedByName = "stateProcedureId")
    @Mapping(target = "openingCessation", source = "idTipoAttivazione")
    @Mapping(target = "typeCessation", source = "idTipoProcedimento")
    @Mapping(target = "reasonCessation", source = "idTipoCessazione", qualifiedByName = "idTipoCessazione")
    @Mapping(target = "categLeaveReq", source = "idCatPersRichiesta")
    @Mapping(target = "idCatMilitare", source = "idCatPers")
    @Mapping(target = "catDocumento", source = "idTipoComunicazione")
    @Mapping(target = "tpCegmlGiudMedLegale", source = "idTipoGml1", qualifiedByName = "idTipoGml1")
    @Mapping(target = "tpCegmlGiudMedLegale2", source = "idTipoGml2", qualifiedByName = "idTipoGml2")
    void updateProcedureFromDTO(@MappingTarget Procedure destination, ProcedureOpeningDataDTO dto);

}
