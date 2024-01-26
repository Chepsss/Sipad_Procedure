package it.almaviva.difesa.cessazione.procedure.repository.specification;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.ProcedureStateConst;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureAssignment;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureHistory;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.RiassegnazioneSearchRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.UserSearchDTO;
import it.almaviva.difesa.cessazione.procedure.service.rest.AuthServiceClient;
import it.almaviva.difesa.cessazione.procedure.util.CommonUtilsMethod;
import it.almaviva.difesa.cessazione.procedure.util.DataUtilsMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class RiassegnazioneSpecification {

    private final AuthServiceClient authServiceClient;
    private Long oldEmployeeId;

    public Specification<Procedure> getSpecification(RiassegnazioneSearchRequest request, Pageable pageable) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            DataUtilsMethod.addFieldToPredicatesIfNotEmptyOrBlank(root, cb, request.getIdState(), Constant.STATE_PROCEDURE).ifPresent(predicates::add);

            addEmployeeAssignedToPredicatesIfNotEmptyOrBlank(root, cb, request).ifPresent(predicates::add);

            ProcedureFiltersSpecification.setOrderByToCriteria(root, query, cb, pageable);

            Join<Procedure, StateProcedure> stateProcedureJoin = root.join(Constant.STATE_PROCEDURE, JoinType.INNER);
            Predicate procStateNotClosed = cb.not(cb.upper(stateProcedureJoin.get(Constant.CODE_STATE)).in(List.of(ProcedureStateConst.CHIUSO, ProcedureStateConst.CHIUSO_ANT)));
            predicates.add(procStateNotClosed);

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Optional<Predicate> addEmployeeAssignedToPredicatesIfNotEmptyOrBlank(Root<Procedure> root, CriteriaBuilder cb, RiassegnazioneSearchRequest request) {
        Predicate predicate = null;
        try {
            UserSearchDTO req = getEmployeeSearchDTO(request);
            req.setRoleCode(request.getRoleCode());
            this.oldEmployeeId = authServiceClient.searchUserByRoleCode(req);
            List<Predicate> predicates = new ArrayList<>();
            log.debug("***EmployeeId " + oldEmployeeId);
            Join<Procedure, ProcedureHistory> historyJoin = root.join(Constant.PROCEDURE_HISTORIES, JoinType.INNER);
            Join<ProcedureHistory, ProcedureAssignment> assignmentJoin = historyJoin.join(Constant.PROCEDURE_ASSIGNMENTS, JoinType.INNER);

            if (Objects.nonNull(request.getTipoTask())) {
                if (request.getTipoTask().equalsIgnoreCase(Constant.ASSEGNATI)) {
                    predicates.add(cb.equal(historyJoin.get(Constant.FLAG_ATTUALE), true));
                }
                if (request.getTipoTask().equalsIgnoreCase(Constant.LAVORATI)) {
                    predicates.add(cb.in(historyJoin.get(Constant.FLAG_ATTUALE)).value(List.of(true, false)));
                }
            }

            predicates.add(cb.equal(historyJoin.get(Constant.ROLE_CODE), request.getRoleCode()));
            predicates.add(cb.equal(assignmentJoin.get(Constant.ID_ASSIGNED_TO), oldEmployeeId));
            Predicate betweenToday = cb.between(cb.literal(LocalDateTime.now()),
                    assignmentJoin.get(Constant.START_DATE),
                    assignmentJoin.get(Constant.END_DATE));
            predicates.add(betweenToday);
            predicate = cb.and(predicates.toArray(new Predicate[0]));

        } catch (Exception ex) {
            log.error("ERROR => ", ex);
        }
        return Optional.ofNullable(predicate);
    }

    public static UserSearchDTO getEmployeeSearchDTO(RiassegnazioneSearchRequest request) {
        UserSearchDTO req = new UserSearchDTO();
        if (CommonUtilsMethod.isNotBlankString(request.getEmployeeFiscalCode())) {
            req.setFiscalCode(request.getEmployeeFiscalCode());
        }
        if (CommonUtilsMethod.isNotBlankString(request.getEmployeeSurname())) {
            req.setLastName(request.getEmployeeSurname());
        }
        if (CommonUtilsMethod.isNotBlankString(request.getEmployeeName())) {
            req.setFirstName(request.getEmployeeName());
        }
        return req;
    }

}
