package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.FaseProcedure;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FaseProcedure entity.
 */
@Repository
public interface FaseProcedureRepository extends GenericRepository<FaseProcedure, Long>, GenericSearchRepository<FaseProcedure> {

    @Query("select f.descFase from FaseProcedure f where f.codeFase = :codeFaseCentral")
    String getDescFaseByCodeFaseCentral(String codeFaseCentral);

    @Query(value = "select f.codeFase " +
            "from Procedure p " +
            "inner join StateProcedure st " +
            "on st.id = p.stateProcedure.id " +
            "inner join FaseProcedure f " +
            "on f.id = st.faseProcedure.id " +
            "where p.id = :idProcedure")
    String getCurrentProcedureFase(Long idProcedure);

}
