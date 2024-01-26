package it.almaviva.difesa.cessazione.procedure.repository.common;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericSearchRepository<E> extends JpaSpecificationExecutor<E> {
}