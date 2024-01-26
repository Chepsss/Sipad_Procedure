package it.almaviva.difesa.cessazione.procedure.repository.dao;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class NumeroAttoDao {

    @PersistenceContext
    private EntityManager manager;

    public Long generaNumAtto() {

        int anno = Calendar.getInstance().get(Calendar.YEAR);

        CriteriaBuilder qb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<Procedure> root = cq.from(Procedure.class);
        cq.select(qb.max(root.get(Constant.NUMERO_ATTO_SIPAD)));
        Long aLong = manager.createQuery(cq.where(qb.equal(root.get(Constant.ANNO_ATTO_SIPAD), anno))).getSingleResult();
        if (Objects.isNull(aLong) || aLong == 0) {
            return 1L;
        }
        return aLong + 1;
    }

}
