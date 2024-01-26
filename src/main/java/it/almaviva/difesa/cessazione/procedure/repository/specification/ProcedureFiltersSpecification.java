package it.almaviva.difesa.cessazione.procedure.repository.specification;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureAssignment;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureHistory;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ProcedureSearchRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.EmployeeSearchDTO;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import it.almaviva.difesa.cessazione.procedure.service.rest.AuthServiceClient;
import it.almaviva.difesa.cessazione.procedure.util.CommonUtilsMethod;
import it.almaviva.difesa.cessazione.procedure.util.DataUtilsMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProcedureFiltersSpecification {

    private final AuthServiceClient authServiceClient;
    private final SecurityService securityService;

    public Specification<Procedure> getSpecification(ProcedureSearchRequest procedureSearchRequest, Pageable pageable) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            DataUtilsMethod.addFieldToPredicatesIfNotEmptyOrBlank(root, criteriaBuilder, procedureSearchRequest.getAssignedFullName(), Constant.ASSIGNED_TO).ifPresent(predicates::add);
            DataUtilsMethod.addFieldToPredicatesIfNotEmptyOrBlank(root, criteriaBuilder, procedureSearchRequest.getProcedureCode(), Constant.CODE_PROCESS).ifPresent(predicates::add);
            addFaseProcedureToPredicatesIfNotEmptyOrBlank(root, criteriaBuilder, procedureSearchRequest.getIdFase()).ifPresent(predicates::add);
            DataUtilsMethod.addFieldToPredicatesIfNotEmptyOrBlank(root, criteriaBuilder, procedureSearchRequest.getIdState(), Constant.STATE_PROCEDURE).ifPresent(predicates::add);
            addEmployeeToPredicatesIfNotEmptyOrBlank(root, criteriaBuilder, procedureSearchRequest).ifPresent(predicates::add);
            if (!ObjectUtils.isEmpty(procedureSearchRequest.getAssignmentDate())) {
                LocalDateTime dataAssegnazione = procedureSearchRequest.getAssignmentDate();
                LocalDateTime beforeMidnight = LocalDateTime.of(dataAssegnazione.toLocalDate(), LocalTime.of(23, 59, 59));
                Predicate dataAssegnazionePredicate = criteriaBuilder.between(root.get(Constant.ASSIGNMENT_DATE), dataAssegnazione, beforeMidnight);
                predicates.add(dataAssegnazionePredicate);
            }
            addVisibilityToPredicatesIfNotEmptyOrBlank(root, criteriaBuilder, procedureSearchRequest.getVisibility()).ifPresent(predicates::add);
            query.distinct(true);
            setOrderByToCriteria(root, query, criteriaBuilder, pageable);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Optional<Predicate> addFaseProcedureToPredicatesIfNotEmptyOrBlank(Root<Procedure> root, CriteriaBuilder cb, Long value) {
        Predicate predicate = null;
        if (Objects.nonNull(value)) {
            Join<Procedure, StateProcedure> stateProcedureJoin = root.join(Constant.STATE_PROCEDURE, JoinType.INNER);
            predicate = cb.equal(stateProcedureJoin.get(Constant.FASE_PROCEDURE).get(Constant.ID), value);
        }
        return Optional.ofNullable(predicate);
    }

    private Optional<Predicate> addEmployeeToPredicatesIfNotEmptyOrBlank(Root<Procedure> root, CriteriaBuilder cb, ProcedureSearchRequest procedureSearchRequest) {
        Predicate predicate = null;
        if (CommonUtilsMethod.isNotBlankString(procedureSearchRequest.getEmployeeFiscalCode())
                || CommonUtilsMethod.isNotBlankString(procedureSearchRequest.getEmployeeSurname())
                || CommonUtilsMethod.isNotBlankString(procedureSearchRequest.getEmployeeName())
        ) {
            EmployeeSearchDTO req = RiassegnazioneSpecification.getEmployeeSearchDTO(procedureSearchRequest);
            List<Long> employeeIds;
            try {
                employeeIds = authServiceClient.searchEmployee(req);

                if (Objects.nonNull(employeeIds)) {
                    log.debug("Employees ==> {}", employeeIds);
                    predicate = root.get(Constant.EMPLOYEE_ID).in(employeeIds);
                } else {
                    predicate = cb.equal(root.get(Constant.EMPLOYEE_ID), 0);
                }
            } catch (ServiceException e) {
                log.error("ERROR => ", e);
                predicate = cb.equal(root.get(Constant.EMPLOYEE_ID), 0);
            }
        }
        return Optional.ofNullable(predicate);
    }

    private Optional<Predicate> addVisibilityToPredicatesIfNotEmptyOrBlank(Root<Procedure> root, CriteriaBuilder cb, String visibility) {
        Long userLoggedId = securityService.getEmployeeIdOfUserLogged();

        Join<Procedure, ProcedureHistory> procedureProcedureHistoryJoin = root.join(Constant.PROCEDURE_HISTORIES, JoinType.INNER);
        Join<ProcedureHistory, ProcedureAssignment> procedureHistoryProcedureAssignmentJoin = procedureProcedureHistoryJoin
                .join(Constant.PROCEDURE_ASSIGNMENTS, JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        Predicate betweenToday = cb.between(cb.literal(LocalDateTime.now()),
                procedureHistoryProcedureAssignmentJoin.get(Constant.START_DATE),
                procedureHistoryProcedureAssignmentJoin.get(Constant.END_DATE));
        predicates.add(betweenToday);

        Predicate procAssignedTo = cb.equal(procedureHistoryProcedureAssignmentJoin.get(Constant.ID_ASSIGNED_TO), userLoggedId);
        predicates.add(procAssignedTo);

        if (visibility.equalsIgnoreCase(Constant.ME)) {
            predicates.add(cb.equal(procedureProcedureHistoryJoin.get(Constant.FLAG_ATTUALE), true));
        }
        return Optional.ofNullable(cb.and(predicates.toArray(new Predicate[0])));
    }

    public static void setOrderByToCriteria(Root<Procedure> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, Pageable pageable) {
        // To avoid org.postgresql.util.PSQLException: ERRORE: per SELECT DISTINCT, le espressioni ORDER BY devono figurare nella lista di argomenti
        pageable.getSort().forEach(order -> {
            if (order.getProperty().startsWith(Constant.CODE_PROCESS)) {
                buildQuery(root, query, criteriaBuilder, order.getDirection(), Constant.CODE_PROCESS);
            } else if (order.getProperty().startsWith(Constant.LAST_MODIFIED_DATE)) {
                buildQuery(root, query, criteriaBuilder, order.getDirection(), Constant.LAST_MODIFIED_DATE);
            } else if (order.getProperty().startsWith(Constant.PRIORITY)) {
                buildQuery(root, query, criteriaBuilder, order.getDirection(), Constant.PRIORITY);
            } else if (order.getProperty().equalsIgnoreCase(Constant.PROCEDURE_DEFAULT_ORDER)) {
                List<Order> orderList = new ArrayList<>();
                orderList.add(criteriaBuilder.asc(root.get(Constant.PRIORITY)));
                orderList.add(criteriaBuilder.desc(root.get(Constant.LAST_MODIFIED_DATE)));
                query.orderBy(orderList);
            }
        });
    }

    private static void buildQuery(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb, Sort.Direction direction, String... fieldName) {
        Path<?> path = root.get(fieldName[0]);
        for (int i = 1; fieldName.length > 1 && i < fieldName.length; i++) {
            path = path.get(fieldName[i]);
        }
        if (direction.isAscending()) {
            query.orderBy(cb.asc(path));
        } else {
            query.orderBy(cb.desc(path));
        }
    }

}
