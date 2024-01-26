package it.almaviva.difesa.cessazione.procedure.util;

import it.almaviva.difesa.cessazione.procedure.domain.common.GenericEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataUtilsMethod {

    public static <E extends GenericEntity> Optional<Predicate> addFieldToPredicatesIfNotEmptyOrBlank(Root<E> root, CriteriaBuilder criteriaBuilder, Object value, String fieldName) {
        Predicate predicate = null;
        if (value instanceof String && CommonUtilsMethod.isNotBlankString((String) value)) {
            predicate = criteriaBuilder.equal(criteriaBuilder.upper(root.get(fieldName)), ((String) value).toUpperCase());
        } else if (!ObjectUtils.isEmpty(value)) {
            predicate = criteriaBuilder.equal(root.get(fieldName), value);
        }
        return Optional.ofNullable(predicate);
    }

}
