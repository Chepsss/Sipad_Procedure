package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcTransito;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TbCeProcTransitoRepository extends GenericRepository<TbCeProcTransito, Long>, GenericSearchRepository<TbCeProcTransito> {
}