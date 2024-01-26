package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureAssignment;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data SQL repository for the ProcedureAssignment entity.
 */
@Repository
public interface ProcedureAssignmentRepository extends GenericRepository<ProcedureAssignment, Long>, GenericSearchRepository<ProcedureAssignment> {

    @Query(value = "select pa " +
            "from ProcedureAssignment pa " +
            "inner join ProcedureHistory ph " +
            "on ph.id = pa.procedureHistory.id " +
            "where ph.procedure.id = :idProc " +
            "and ph.stateProcedure.id = :idState " +
            "and current_timestamp between pa.startDate and pa.endDate ")
    List<ProcedureAssignment> findAssignments(Long idProc, Long idState);

    @Query(value = "select pa " +
            "from ProcedureAssignment pa " +
            "inner join ProcedureHistory ph " +
            "on ph.id = pa.procedureHistory.id " +
            "where ph.procedure.id = :idProc " +
            "and ph.flagAttuale = true " +
            "and current_timestamp between pa.startDate and pa.endDate")
    Optional<ProcedureAssignment> findCurrentProcedureAssignedTo(Long idProc);

    @Query(value = "select pa " +
            "from ProcedureAssignment pa " +
            "inner join ProcedureHistory ph " +
            "on ph.id = pa.procedureHistory.id " +
            "where ph.procedure.id in :procedureIds " +
            "and ph.roleCode = :roleCode " +
            "and current_timestamp between pa.startDate and pa.endDate ")
    List<ProcedureAssignment> getCurrentAssignmentsByProcedureIdsRoleCodeAndEmployeeId(@Param(Constant.PROCEDURE_IDS) Set<Long> procedureIds,
                                                                                       @Param(Constant.ROLE_CODE) String roleCode);

    @Query(value = "select pa " +
            "from ProcedureAssignment pa " +
            "inner join ProcedureHistory ph " +
            "on ph.id = pa.procedureHistory.id " +
            "where ph.roleCode = :roleCode " +
            "and current_timestamp between pa.startDate and pa.endDate ")
    List<ProcedureAssignment> getAllCurrentAssignmentsByProcedureIdsRoleCodeAndEmployeeId(@Param(Constant.ROLE_CODE) String roleCode);

    @Query(value = "select pa " +
            "from ProcedureAssignment pa " +
            "inner join ProcedureHistory ph " +
            "on ph.id = pa.procedureHistory.id " +
            "where ph.procedure.id = :idProc " +
            "and pa.idAssignedTo = :idAssignedTo ")
    List<ProcedureAssignment> findProcedureAssignmentByIdProcAndIdAssignedTo(Long idProc, Long idAssignedTo);

}
