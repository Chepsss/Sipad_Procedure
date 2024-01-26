package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the StateProcedure entity.
 */
@Repository
public interface StateProcedureRepository extends GenericRepository<StateProcedure, Long>, GenericSearchRepository<StateProcedure> {

    Optional<StateProcedure> findByCodeState(String codeState);

    @Query("select distinct s.descStateCentral from StateProcedure s where s.codeStateCentral = :codeStateCentral")
    String getDescStateByCodeStateCentral(String codeStateCentral);

    @Query("select s " +
            "from StateProcedure s " +
            "inner join ProcedureHistory h on s.id = h.stateProcedure.id " +
            "where h.procedure.id = :idProc " +
            "and h.flagAttuale = true ")
    Optional<StateProcedure> getCurrentStateProcedureById(Long idProc);

    Page<StateProcedure> findAllByCodeStateIn(List<String> codeStateList, Pageable pageable);

    List<StateProcedure> findAllByCodeStateNotInOrderByOrdState(List<String> codeStateList);

}
