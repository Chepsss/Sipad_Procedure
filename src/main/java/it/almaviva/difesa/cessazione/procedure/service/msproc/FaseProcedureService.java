package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.FaseProcedure;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.FaseProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.FaseProcedureMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.FaseProcedureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FaseProcedure}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaseProcedureService {

    private final FaseProcedureRepository faseProcedureRepository;
    private final FaseProcedureMapper faseProcedureMapper;

    /**
     * Get all the faseProcedures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<FaseProcedureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FaseProcedures");
        return faseProcedureRepository.findAll(pageable).map(faseProcedureMapper::toDto);
    }

    /**
     * Get one faseProcedure by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<FaseProcedureDTO> findOne(Long id) {
        log.debug("Request to get FaseProcedure : {}", id);
        return faseProcedureRepository.findById(id).map(faseProcedureMapper::toDto);
    }

    public String getDescriptionByCodeFaseCentral(String codeFaseCentral) {
        return faseProcedureRepository.getDescFaseByCodeFaseCentral(codeFaseCentral);
    }

    public String getCurrentFase(Long idProcedure) {
        return faseProcedureRepository.getCurrentProcedureFase(idProcedure);
    }

}
