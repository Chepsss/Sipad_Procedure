package it.almaviva.difesa.cessazione.procedure.repository.common;

import it.almaviva.difesa.cessazione.procedure.domain.common.GenericCriteriaModel;
import it.almaviva.difesa.cessazione.procedure.domain.common.GenericEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public interface GenericSpecification<E extends GenericEntity, C extends GenericCriteriaModel> {

    Specification<E> getSpecification(C c);

}