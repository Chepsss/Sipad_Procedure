package it.almaviva.difesa.cessazione.procedure.repository.specification;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCetipCessazione;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TpCetipCessazioneSpecification {

    public static Specification<TpCetipCessazione> reasonOfCessationSpecification(List<Long> ids,
                                                                                  String stfaaAcrFfaa,
                                                                                  Long categoryPers) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(Constant.CETIP_STFAA_SEQ_PK), stfaaAcrFfaa));
            predicates.add(cb.equal(root.get(Constant.CETIP_SGCTP_SEQ_PK), categoryPers));
            if (Objects.nonNull(ids) && !ids.isEmpty()) {
                predicates.add(root.get(Constant.CETIP_PRTPO_SEQ_PK).in(ids));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
