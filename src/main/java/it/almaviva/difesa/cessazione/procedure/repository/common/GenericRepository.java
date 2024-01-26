package it.almaviva.difesa.cessazione.procedure.repository.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface GenericRepository<E, I extends Serializable> extends JpaRepository<E, I> {

    Page<E> findAll(Specification<E> searchSpecification, Pageable pageable);
}
