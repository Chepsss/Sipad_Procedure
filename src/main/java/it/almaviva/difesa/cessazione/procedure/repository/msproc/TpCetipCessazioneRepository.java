package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCetipCessazione;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TpCetipCessazioneRepository extends GenericRepository<TpCetipCessazione, Long>, GenericSearchRepository<TpCetipCessazione> {
}