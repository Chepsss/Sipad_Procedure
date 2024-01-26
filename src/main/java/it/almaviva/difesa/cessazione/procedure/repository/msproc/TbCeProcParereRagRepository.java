package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcParereRag;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TbCeProcParereRagRepository extends GenericRepository<TbCeProcParereRag, Long>, GenericSearchRepository<TbCeProcParereRag> {
}