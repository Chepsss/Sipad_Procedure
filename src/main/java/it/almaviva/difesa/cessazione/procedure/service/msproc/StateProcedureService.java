package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.ProcedureStateConst;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureAssignment;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import it.almaviva.difesa.cessazione.procedure.model.camunda.StateContainer;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.StateProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.RoleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.UserDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgctpCatpersonaleDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.StateProcedureMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureRepository;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.StateProcedureRepository;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import it.almaviva.difesa.cessazione.procedure.service.rest.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link StateProcedure}.
 */
@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class StateProcedureService {

    private final StateProcedureRepository stateProcedureRepository;
    private final StateProcedureMapper stateProcedureMapper;
    private final ProcedureRepository procedureRepository;
    private final CamundaService camundaService;
    private final AuthServiceClient authServiceClient;
    private final SecurityService securityService;
    private final TpSgctpCatPersonaleService tpSgctpCatPersonaleService;
    private final ProcedureAssignmentService procedureAssignmentService;

    /**
     * Get all the stateProcedures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<StateProcedureDTO> findAll(Long phaseId, Pageable pageable) {
        log.debug("Request to get all StateProcedures");
        Specification<StateProcedure> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(phaseId)) {
                Predicate fase = cb.equal(root.get(Constant.FASE_PROCEDURE), phaseId);
                predicates.add(fase);
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return stateProcedureRepository.findAll(specification, pageable).map(stateProcedureMapper::toDto);
    }


    /**
     * Get all the stateProcedures.
     *
     * @return the list of entities.
     */
    public List<StateProcedureDTO> findAllStateFiltered() {
        log.debug("Request to get all findAllStateFiltered");
        return stateProcedureRepository.findAllByCodeStateNotInOrderByOrdState(List.of(ProcedureStateConst.CHIUSO, ProcedureStateConst.CHIUSO_ANT)).stream()
                .map(stateProcedureMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one stateProcedure by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<StateProcedureDTO> findOne(Long id) {
        log.debug("Request to get StateProcedure : {}", id);
        return findStateById(id).map(stateProcedureMapper::toDto);
    }

    public Optional<StateProcedure> findStateById(Long id) {
        return stateProcedureRepository.findById(id);
    }

    public Optional<StateProcedureDTO> findByCodeState(String code) {
        log.debug("Request to get StateProcedure : {}", code);
        return findByCode(code).map(stateProcedureMapper::toDto);
    }

    public Optional<StateProcedure> findByCode(String code) {
        return stateProcedureRepository.findByCodeState(code);
    }

    public Optional<StateProcedure> getCurrentStateProcedureById(Long idProc) {
        return stateProcedureRepository.getCurrentStateProcedureById(idProc);
    }

    public Page<StateProcedureDTO> listNextStates(Long procedureId) {
        log.debug("Request to get listNextStates : {}", procedureId);
        Procedure procedure = procedureRepository.getById(procedureId);
        StateProcedure currentState = procedure.getStateProcedure();
        return listNextStates(currentState.getCodeState());
    }

    public Page<StateProcedureDTO> listNextStates(String currentStateCode) {
        List<StateContainer> nextStatesList = camundaService.listNextStates(currentStateCode);
        AtomicReference<List<StateContainer>> nextStatesListFiltered = new AtomicReference<>();
        Set<String> userRoles = securityService.getUserRoles();
        AtomicBoolean foundRoleManager = new AtomicBoolean(false);
        Set<String> userRolesFiltered = new HashSet<>();
        userRoles.forEach(userRole -> {
            if (userRole.startsWith(Constant.ROLE_PREFIX)) {
                userRolesFiltered.add(userRole);
            }
        });
        userRolesFiltered.forEach(role -> {
            if (role.startsWith(Constant.MANAGER_ROLE_PREFIX)) {
                foundRoleManager.set(true);
            }
        });
        if (!foundRoleManager.get()) {
            nextStatesListFiltered.set(nextStatesList.stream()
                    .filter(cam ->
                            !cam.getNextState().getValue().toString().equalsIgnoreCase(ProcedureStateConst.CHIUSO)
                                    && !cam.getNextState().getValue().toString().equalsIgnoreCase(ProcedureStateConst.CHIUSO_ANT)
                    ).collect(Collectors.toList()));
        } else {
            nextStatesListFiltered.set(nextStatesList);
        }
        List<StateProcedureDTO> stateProcedureDTOList = new ArrayList<>();
        nextStatesListFiltered.get().forEach(stateContainer -> {
            var state = stateProcedureRepository.findByCodeState(stateContainer.getNextState().getValue().toString()).orElse(null);
            if (Objects.isNull(state)) {
                return;
            }
            StateProcedureDTO dto = new StateProcedureDTO();
            dto.setId(state.getId());
            dto.setCodeState(state.getCodeState());
            dto.setDescState(stateContainer.getLabel().getValue().toString());
            stateProcedureDTOList.add(dto);
        });
        return new PageImpl<>(stateProcedureDTOList, Pageable.unpaged(), nextStatesList.size());
    }

    public List<RoleDTO> listRolesByStatus(String currentStateCode, String codeCatPers) {
        List<String> rolesByStatusList = camundaService.listRolesByStatus(currentStateCode, codeCatPers);
        List<RoleDTO> filteredRoleDTOS = new ArrayList<>();
        if (!rolesByStatusList.isEmpty()) {
            List<RoleDTO> roleDTOS = authServiceClient.getRoles();
            filteredRoleDTOS = roleDTOS
                    .stream()
                    .filter(it -> rolesByStatusList.contains(it.getRoleCode()))
                    .collect(Collectors.toList());
        }
        return filteredRoleDTOS;
    }

    public List<UserDTO> listUsersByStatusAndCatMilitare(Long procedureId, Long procedureStateId, Long idCatMilitare) {
        Procedure procedure = procedureRepository.getById(procedureId);
        StateProcedure stateProcedure = stateProcedureRepository.getById(procedureStateId);
        StateProcedure currentStateProcedure = stateProcedureRepository.getById(procedure.getStateProcedure().getId());
        Boolean isReassignment = camundaService.checkIfReassignment(currentStateProcedure.getCodeState(), stateProcedure.getCodeState());
        TpSgctpCatpersonaleDTO catMilitare = tpSgctpCatPersonaleService.getCatMilitareById(idCatMilitare);
        List<RoleDTO> roles = listRolesByStatus(stateProcedure.getCodeState(), catMilitare.getSgctpCodCatpers());
        List<Long> roleIds = roles.stream().map(RoleDTO::getId).collect(Collectors.toList());
        List<UserDTO> users = listUsersByRoles(roleIds);
        List<UserDTO> usersFiltered;
        if (Boolean.TRUE.equals(isReassignment)) {
            Long previousAssignee = getPreviousAssignee(procedure, procedureStateId);
            usersFiltered = users.stream().filter(user -> user.getEmployeeId().equals(previousAssignee)).collect(Collectors.toList());
            if (!usersFiltered.isEmpty()) {
                return usersFiltered;
            }
        }
        usersFiltered = users.stream().filter(user -> Objects.nonNull(user.getUserId())).collect(Collectors.toList());
        if (procedure.getStateProcedure().getCodeState().equals(stateProcedure.getCodeState())) {
            Long currentUserId = securityService.getUserDetails().getUserId();
            return usersFiltered.stream().filter(user -> !user.getUserId().equals(currentUserId)).collect(Collectors.toList());
        }
        return usersFiltered;
    }

    private Long getPreviousAssignee(Procedure procedure, Long nextStateId) {
        List<ProcedureAssignment> list = procedureAssignmentService.findAssignments(procedure.getId(), nextStateId);
        return list.stream().findFirst().map(ProcedureAssignment::getIdAssignedTo).orElse(null);
    }

    public List<UserDTO> listUsersByRoles(List<Long> roleIds) {
        log.debug("Request to get listUsersByRoles");
        return authServiceClient.listByRoles(roleIds);
    }

    public String getDescriptionByCodeStateCentral(String codeStateCentral) {
        return stateProcedureRepository.getDescStateByCodeStateCentral(codeStateCentral);
    }

}
