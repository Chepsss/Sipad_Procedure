package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureHistory;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureHistoryDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.RoleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgctpCatpersonaleDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.ProcedureHistoryMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ProcedureHistory}.
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class ProcedureHistoryService {

    private final ProcedureHistoryRepository procedureHistoryRepository;
    private final ProcedureHistoryMapper procedureHistoryMapper;
    private final TpSgctpCatPersonaleService tpSgctpCatPersonaleService;
    private final StateProcedureService stateProcedureService;

    /**
     * Save or Update a procedureHistory.
     *
     * @param procedureHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ProcedureHistoryDTO saveOrUpdate(ProcedureHistoryDTO procedureHistoryDTO) {
        log.debug("Request to save or update ProcedureHistory : {}", procedureHistoryDTO);
        ProcedureHistory procedureHistory = procedureHistoryMapper.toEntity(procedureHistoryDTO);
        procedureHistory = procedureHistoryRepository.save(procedureHistory);
        return procedureHistoryMapper.toDto(procedureHistory);
    }

    /**
     * Get all the procedureHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProcedureHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcedureHistories");
        return procedureHistoryRepository.findAll(pageable).map(procedureHistoryMapper::toDto);
    }

    /**
     * Get one procedureHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProcedureHistoryDTO> findOne(Long id) {
        log.debug("Request to get ProcedureHistory : {}", id);
        return procedureHistoryRepository.findById(id).map(procedureHistoryMapper::toDto);
    }

    /**
     * Delete the procedureHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProcedureHistory : {}", id);
        procedureHistoryRepository.deleteById(id);
    }

    public ProcedureHistory getPreviousHistory(Long idProc, Long idStato, String roleCode) {
        return this.findByProcedureIdAndStateProcedureIdAndRoleCode(idProc, idStato, roleCode, false);
    }

    public ProcedureHistory getCurrentHistory(Long idProc, Long idStato, String roleCode) {
        return this.findByProcedureIdAndStateProcedureIdAndRoleCode(idProc, idStato, roleCode, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateAllOldHistoryOfProcedure(Long idProc) {
        procedureHistoryRepository.updateAllOldHistoryOfProcedure(idProc, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public ProcedureHistory findByProcedureIdAndStateProcedureIdAndRoleCode(Long idProc, Long idStato, String roleCode, boolean flagAttuale) {
        return procedureHistoryRepository.findByProcedureIdAndStateProcedureIdAndRoleCode(idProc, idStato, roleCode, flagAttuale)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public String getCurrentRoleOfProcedure(Long idProc) {
        return procedureHistoryRepository.getCurrentRoleOfProcedure(idProc);
    }

    public String getRoleOfCurrentUserAssigned(String currentState, Long idCatMilitare) {
        TpSgctpCatpersonaleDTO catMilitare = tpSgctpCatPersonaleService.getCatMilitareById(idCatMilitare);
        List<RoleDTO> roles = stateProcedureService.listRolesByStatus(currentState, catMilitare.getSgctpCodCatpers());
        if (!roles.isEmpty()) {
            return roles.get(0).getRoleCode();
        } else {
            return null;
        }
    }

}
