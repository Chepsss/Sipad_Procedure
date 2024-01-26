package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureAssignment;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureHistory;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.FaseProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureEmployeeDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.RiassegnazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.StateProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.RiassegnazioneRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.RiassegnazioneSearchRequest;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.RiassegnazioneMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureAssignmentRepository;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureRepository;
import it.almaviva.difesa.cessazione.procedure.repository.specification.RiassegnazioneSpecification;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import it.almaviva.difesa.cessazione.procedure.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiassegnazioneService {

    private final ProcedureRepository procedureRepository;
    private final SecurityService securityService;
    private final RiassegnazioneSpecification riassegnazioneSpecification;
    private final RiassegnazioneMapper riassegnazioneMapper;
    private final ProcedureService procedureService;
    private final ProcedureAssignmentRepository procedureAssignmentRepository;
    private final ProcedureAssignmentService procedureAssignmentService;

    @Transactional(readOnly = true)
    public Page<RiassegnazioneDTO> search(RiassegnazioneSearchRequest request, Pageable pageable) {
        String jwtToken = securityService.getUserToken();
        Specification<Procedure> specification = riassegnazioneSpecification.getSpecification(request, pageable);
        Page<Procedure> procedures = procedureRepository.findAll(specification, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        Page<RiassegnazioneDTO> procedureDTOS = procedures.map(riassegnazioneMapper::toDto);
        procedureDTOS.getContent().forEach(procedure -> mapOtherFields(procedure, jwtToken));
        return sortProcedures(procedureDTOS, pageable);
    }

    @Transactional
    public RestApiResponse reassignment(RiassegnazioneRequest request) {
        RestApiResponse response = new RestApiResponse();
        try {
            List<ProcedureAssignment> assignmentToUpdate = new ArrayList<>();
            Set<Procedure> procedureSet = new HashSet<>();
            Map<Long, ProcedureHistory> currentHistory = new HashMap<>();
            List<ProcedureAssignment> procedureAssignmentList = procedureAssignmentService
                    .getCurrentAssignmentsByProcedureIdsRoleCodeAndEmployeeId(request.getProcedureIds(), request.getRoleCode(), request.getSelectedAll());

            // Update OLD assegnazioni
            procedureAssignmentList.forEach(assignment -> {
                ProcedureHistory history = assignment.getProcedureHistory();

                Procedure procedure = history.getProcedure();
                procedureSet.add(procedure);

                currentHistory.put(procedure.getId(), history);

                assignment.setProcedureHistory(history);
                assignment.setEndDate(LocalDateTime.now());
                assignmentToUpdate.add(assignment);
            });
            procedureAssignmentRepository.saveAll(assignmentToUpdate);

            // Create NEW assegnazioni
            procedureSet.forEach(procedure -> {
                ProcedureAssignment newAssignment = new ProcedureAssignment();
                newAssignment.setProcedureHistory(currentHistory.get(procedure.getId()));
                newAssignment.setIdAssignedTo(request.getNewEmployeeId());
                newAssignment.setDefinitive(true);
                newAssignment.setFlagLavorato(false);
                newAssignment.setStartDate(LocalDateTime.now().plusSeconds(1));
                newAssignment.setEndDate(LocalDateTime.of(9999, 12, 31, 0, 0, 0));
                procedureAssignmentRepository.save(newAssignment);

            });
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage(Constant.REASSIGNMENT_OK);
        } catch (Exception e) {
            log.error("Error => ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorsConst.ERRORE_IN_REASSIGNMENT);
        }
        return response;
    }

    public void mapOtherFields(RiassegnazioneDTO procedure, String jwtToken) {
        Long oldEmployeeId = riassegnazioneSpecification.getOldEmployeeId();
        ProcedureAssignment assignment = procedureAssignmentService.findProcedureAssignmentByIdProcAndIdAssignedTo(procedure.getId(), oldEmployeeId);
        Long idAssignedTo = assignment.getIdAssignedTo();
        procedure.setEmployee(procedureService.getEmployeeDetail(procedure.getEmployeeId()));
        procedure.setAssignedTo(procedureService.getUserDetail(idAssignedTo, jwtToken));
    }

    public Page<RiassegnazioneDTO> sortProcedures(Page<RiassegnazioneDTO> procedureDTOS, Pageable pageable) {
        String property = pageable.getSort().stream().map(Sort.Order::getProperty).findFirst().orElse("");
        Sort.Direction direction = pageable.getSort().stream().map(Sort.Order::getDirection).findFirst().orElse(null);
        List<RiassegnazioneDTO> newListSorted = getListOrdered(procedureDTOS.getContent(), direction, List.of(property.split("[.]")));
        return new PageImpl<>(newListSorted, pageable, procedureDTOS.getTotalElements());
    }

    private List<RiassegnazioneDTO> getListOrdered(List<RiassegnazioneDTO> procedures, Sort.Direction direction, List<String> properties) {
        if (Objects.nonNull(direction) && !properties.isEmpty()) {
            Comparator<RiassegnazioneDTO> comparator = (o1, o2) -> 0;
            if (properties.contains(Constant.LAST_NAME.toLowerCase())) {
                comparator = Comparator.comparing(RiassegnazioneDTO::getEmployee, Comparator.comparing(ProcedureEmployeeDTO::getLastname));
            } else if (properties.contains(Constant.FIRST_NAME.toLowerCase())) {
                comparator = Comparator.comparing(RiassegnazioneDTO::getEmployee, Comparator.comparing(ProcedureEmployeeDTO::getFirstname));
            } else if (properties.contains(Constant.FASE_PROCEDURE)) {
                comparator = Comparator.comparing(RiassegnazioneDTO::getFaseProcedure, Comparator.nullsFirst(Comparator.comparing(FaseProcedureDTO::getDescFase)));
            } else if (properties.contains(Constant.STATE_PROCEDURE)) {
                comparator = Comparator.comparing(RiassegnazioneDTO::getStateProcedure, Comparator.nullsFirst(Comparator.comparing(StateProcedureDTO::getDescState)));
            }
            return Utils.sortList(procedures, direction, comparator);
        }
        return procedures;
    }

}
