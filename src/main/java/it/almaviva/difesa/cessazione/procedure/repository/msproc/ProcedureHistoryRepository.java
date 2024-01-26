package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureHistory;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Spring Data SQL repository for the ProcedureHistory entity.
 */
@Repository
public interface ProcedureHistoryRepository extends GenericRepository<ProcedureHistory, Long>, GenericSearchRepository<ProcedureHistory> {

    @Query("select p " +
            "from ProcedureHistory p " +
            "where p.procedure.id = :idProc " +
            "and p.stateProcedure.id = :idStato " +
            "and p.roleCode = :roleCode " +
            "and p.flagAttuale = :flagAttuale")
    Optional<ProcedureHistory> findByProcedureIdAndStateProcedureIdAndRoleCode(@Param(Constant.ID_PROC) Long idProc,
                                                                               @Param(Constant.ID_STATO) Long idStato,
                                                                               @Param(Constant.ROLE_CODE) String roleCode,
                                                                               @Param(Constant.FLAG_ATTUALE) boolean flagAttuale);

    @Query("select p.roleCode " +
            "from ProcedureHistory p " +
            "where p.procedure.id = :idProc " +
            "and p.flagAttuale = true")
    String getCurrentRoleOfProcedure(@Param(Constant.ID_PROC) Long idProc);

    @Modifying
    @Query("update ProcedureHistory h set h.flagAttuale = false, h.lastModifiedDate = :lastModifiedDate where h.procedure.id = :idProc")
    void updateAllOldHistoryOfProcedure(@Param(Constant.ID_PROC) Long idProc, @Param(Constant.LAST_MODIFIED_DATE) LocalDateTime lastModifiedDate);

}
