package it.almaviva.difesa.cessazione.procedure.service.msproc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CheckProcedureService {

    private final ProcedureService procedureService;

    public boolean checkEmployeeStatusOfCessation(Long employeeId) {
        return procedureService.checkEmployeeStatusOfCessation(employeeId);
    }

}
