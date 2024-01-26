package it.almaviva.difesa.cessazione.procedure.converter;

import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureEmployeeDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.EmployeeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcedureEmployeeConverter implements Converter<EmployeeResponseDTO, ProcedureEmployeeDTO> {

    @Override
    public ProcedureEmployeeDTO convert(EmployeeResponseDTO employeeDetailResp) {
        ProcedureEmployeeDTO dto = new ProcedureEmployeeDTO();
        dto.setFirstname(employeeDetailResp.getFirstName());
        dto.setLastname(employeeDetailResp.getLastName());
        dto.setFiscalCode(employeeDetailResp.getFiscalCode());
        dto.setBirthday(employeeDetailResp.getBirthDate());
        dto.setGender(employeeDetailResp.getGender());
        return dto;
    }

}
