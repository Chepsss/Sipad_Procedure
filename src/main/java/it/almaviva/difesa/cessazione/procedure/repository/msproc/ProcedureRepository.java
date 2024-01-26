package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Procedure entity.
 */
@Repository
public interface ProcedureRepository extends GenericRepository<Procedure, Long>, GenericSearchRepository<Procedure> {

    Procedure findProcedureById(Long idProc);

    List<Procedure> findProcedureByEmployeeId(Long employeeId);

    Boolean existsByEmployeeId(Long employeeId);

    List<Procedure> findAllByIdIn(List<Long> ids);

    @Query(value = "select ph.procedure " +
            "from ProcedureAssignment pa " +
            "inner join ProcedureHistory ph " +
            "on ph.id = pa.procedureHistory.id " +
            "where pa.idAssignedTo = :idAssignedTo " +
            "and ph.stateProcedure.codeState not in (:listCodeState) " +
            "and ph.flagAttuale = true ")
    List<Procedure> getProceduresNotClosedByIdAssignedTo(Long idAssignedTo, List<String> listCodeState);

}
