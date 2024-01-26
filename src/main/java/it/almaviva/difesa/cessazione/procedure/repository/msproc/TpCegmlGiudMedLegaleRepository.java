package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCegmlGiudMedLegale;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TpCegmlGiudMedLegaleRepository extends GenericRepository<TpCegmlGiudMedLegale, String>, GenericSearchRepository<TpCegmlGiudMedLegale> {

    List<TpCegmlGiudMedLegale> getTpCegmlGiudMedLegaleByListaGml1IsTrueOrderById();

    List<TpCegmlGiudMedLegale> getTpCegmlGiudMedLegaleByListaGml2IsTrueOrderById();

}