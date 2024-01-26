package it.almaviva.difesa.cessazione.procedure.model.mapper.sipad;

import it.almaviva.difesa.cessazione.procedure.model.dto.rest.EmployeeResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwSg155StgiuridicoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the DTO {@link EmployeeResponseDTO} and DTO {@link VwSg155StgiuridicoDTO}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface EmployeeResponseMapper {

    @Mapping(target = "employeeId", source = "sg155IdDip")
    @Mapping(target = "lastName", source = "sg155Cognome")
    @Mapping(target = "firstName", source = "sg155Nome")
    @Mapping(target = "fiscalCode", source = "sg155CodiceFiscale")
    @Mapping(target = "gender", source = "sg155Sesso")
    @Mapping(target = "serialNumber", source = "sg155Matricola")
    @Mapping(target = "birthDate", source = "sg155DataNascita")
    @Mapping(target = "email", source = "sg155MailUfficio")
    @Mapping(target = "armedForceId", source = "sg155CodFfaa")
    @Mapping(target = "armedForceDescription", source = "sg155DescrFfaa")
    @Mapping(target = "staffPositionId", source = "sg155IdPosser")
    @Mapping(target = "staffPositionDescription", source = "sg155DescrPosser")
    @Mapping(target = "staffCategoryId", source = "sg155IdCatmil")
    @Mapping(target = "staffCategoryDescription", source = "sg155DescrCatmil")
    @Mapping(target = "rankId", source = "sg155CodGrado")
    @Mapping(target = "rankDescription", source = "sg155DescrGrado")
    @Mapping(target = "roleId", source = "sg155CodRuolo")
    @Mapping(target = "roleDescription", source = "sg155DescrRuolo")
    EmployeeResponseDTO copyProperties(VwSg155StgiuridicoDTO source);

}
