package it.almaviva.difesa.cessazione.procedure.repository.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.DeclarationProcedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericRepository;
import it.almaviva.difesa.cessazione.procedure.repository.common.GenericSearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeclarationProcedureRepository extends GenericRepository<DeclarationProcedure, Long>, GenericSearchRepository<DeclarationProcedure> {

    List<DeclarationProcedure> findDeclarationProcedureByIdProc(Procedure procedure);

    void deleteDeclarationProcedureByIdProcId(Long idProc);

}