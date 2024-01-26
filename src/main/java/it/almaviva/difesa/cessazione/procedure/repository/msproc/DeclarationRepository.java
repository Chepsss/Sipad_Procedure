package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.Declaration;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeclarationRepository extends GenericRepository<Declaration, Long>, GenericSearchRepository<Declaration> {

    List<Declaration> findAllByCodiceIn(List<String> declarationsCode);

    Declaration findDeclarationByCodice(String codice);

}