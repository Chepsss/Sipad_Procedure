package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCeconCategCong;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TpCeconCategCongRepository extends GenericRepository<TpCeconCategCong, UUID>, GenericSearchRepository<TpCeconCategCong> {

    @Query(value = "select cc.ceconSgtpoSeqPk " +
            "from TpCeconCategCong cc " +
            "inner join TpCetipCessazione mc on mc.id = cc.ceconCetipSeqPk.id " +
            "where mc.id = :idReason")
    List<String> getCategoryOfLeaveIds(Long idReason);

    @Query(value = "select distinct cc.ceconSgtpoSeqPk " +
            "from TpCeconCategCong cc " +
            "inner join TpCetipCessazione mc on mc.id = cc.ceconCetipSeqPk.id " +
            "where mc.cetipPrtpoSeqPk = :idTypeCessation")
    List<String> getCategoryCongIds(Long idTypeCessation);

}