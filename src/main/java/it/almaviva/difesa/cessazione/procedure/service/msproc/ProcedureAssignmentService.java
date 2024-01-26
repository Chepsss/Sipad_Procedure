package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureAssignment;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureAssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcedureAssignmentService {

    private final ProcedureAssignmentRepository procedureAssignmentRepository;

    @Transactional(readOnly = true)
    public List<ProcedureAssignment> findAssignments(Long idProc, Long nextStateId) {
        return procedureAssignmentRepository.findAssignments(idProc, nextStateId);
    }

    @Transactional(readOnly = true)
    public ProcedureAssignment findCurrentProcedureAssignedTo(Long idProc) {
        return procedureAssignmentRepository.findCurrentProcedureAssignedTo(idProc).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ProcedureAssignment> getCurrentAssignmentsByProcedureIdsRoleCodeAndEmployeeId(Set<Long> procedureIds, String roleCode, Boolean selectedAll) {
        if (Objects.nonNull(selectedAll) && Boolean.TRUE.equals(selectedAll)) {
            return procedureAssignmentRepository.getAllCurrentAssignmentsByProcedureIdsRoleCodeAndEmployeeId(roleCode);
        } else {
            return procedureAssignmentRepository.getCurrentAssignmentsByProcedureIdsRoleCodeAndEmployeeId(procedureIds, roleCode);
        }
    }

    @Transactional(readOnly = true)
    public ProcedureAssignment findProcedureAssignmentByIdProcAndIdAssignedTo(Long idProc, Long idAssignedTo) {
        return procedureAssignmentRepository.findProcedureAssignmentByIdProcAndIdAssignedTo(idProc, idAssignedTo).stream().findFirst().orElse(null);
    }

}
