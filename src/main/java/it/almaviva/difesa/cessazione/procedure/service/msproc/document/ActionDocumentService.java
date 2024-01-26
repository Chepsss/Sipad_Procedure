package it.almaviva.difesa.cessazione.procedure.service.msproc.document;

import it.almaviva.difesa.cessazione.procedure.constant.*;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import it.almaviva.difesa.cessazione.procedure.exception.InvalidTokenException;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.CamundaService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.ProcedureHistoryService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.StateProcedureService;
import it.almaviva.difesa.documenti.document.data.sipad.repository.TbDocumentoRepository;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbDocumento;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.DocumentList;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.SuccessOutput;
import it.almaviva.difesa.documenti.document.service.TbDocumentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ActionDocumentService {

    private final CamundaService camundaService;
    private final TbDocumentoRepository tbDocumentoRepository;
    private final SecurityService securityService;
    private final TbDocumentoService documentoService;
    private final StateProcedureService stateProcedureService;
    private final ProcedureHistoryService procedureHistoryService;

    public DocumentList updateDocumentListWithActions(Long procedureId) {
        DocumentList documentList = new DocumentList();
        Optional<StateProcedure> optionalSP = stateProcedureService.getCurrentStateProcedureById(procedureId);
        if (optionalSP.isPresent()) {
            documentList = documentoService.list(procedureId, null);
            String codeState = optionalSP.get().getCodeState();
            String role = getRoleForActions(procedureId, codeState);
            Map<String, String> actionMapByProc = camundaService.getDocumentActionsByProc(role, codeState);
            documentList.getDocumenti().forEach(doc -> {
                Map<String, String> actionMapByDoc = camundaService.getDocumentActionsByDoc(doc);
                HashMap<String, String> actionsFiltered = actionMapByProc.entrySet()
                        .parallelStream()
                        .filter(map -> actionMapByDoc.containsKey(map.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, HashMap::new));
                doc.setActions(actionsFiltered);

                /* CR 55 :
                    Aggiungere stato 'In Firma', solo sul DECRETO, quando il procedimento
                    è in fase-stato 'Firma-In Firma'
                    TODO : Capire come ottimizzare questo controllo, che ora si basa su una stringa (molto troppo incredibilmente debole)
                 */
                if(doc.getCodTipo().equals(TipoDocumento.DECRETO) && optionalSP.get().getCodeState().equals(ProcedureStateConst.FIRMA) && doc.getStato().equalsIgnoreCase("In approvazione")) {
                    doc.setStato("In firma");
                }


            });
        }
        return documentList;
    }

    private String getRoleForActions(Long procedureId, String codeState) {
        String currentRole = procedureHistoryService.getCurrentRoleOfProcedure(procedureId);
        if (codeState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_DECRETO)) {
            String[] split = currentRole.split(Constant.APPROVER_ROLE_PREFIX);
            Set<String> userRoles = getUserRoles();
            String roleManager = Constant.MANAGER_ROLE_PREFIX.concat(split[1]);
            boolean isUserManager = userRoles.stream().anyMatch(role -> role.equalsIgnoreCase(roleManager));
            if (isUserManager) {
                return roleManager;
            }
        }
        return currentRole;
    }

    private Set<String> getUserRoles() {
        Set<String> userRoles = securityService.getUserRoles();
        Set<String> userRolesFiltered = new HashSet<>();
        userRoles.forEach(userRole -> {
            if (userRole.startsWith(Constant.ROLE_PREFIX)) {
                userRolesFiltered.add(userRole);
            }
        });
        return userRolesFiltered;
    }

    public boolean checkAction(Long documentId, String action) {
        Optional<TbDocumento> document = tbDocumentoRepository.findById(documentId);
        boolean findAction = false;
        if (document.isPresent()) {
            Map<String, String> actions = camundaService.getDocumentActionsByDoc(document.get());
            for (Map.Entry<String, String> map : actions.entrySet()) {
                if (map.getValue().equalsIgnoreCase(action)) {
                    findAction = true;
                    break;
                }
            }
        }
        return findAction;
    }

    public void checkPermissionOnAction(Long documentId, String action) {
        if (!checkAction(documentId, action)) {
            log.warn(String.format("Operazione non Consentita %s su documento id: %d", action, documentId));
            throw new InvalidTokenException(ErrorsConst.UNAUTHORIZED_ACTION, HttpStatus.UNAUTHORIZED, new ArrayList<>());
        }
    }

    public SuccessOutput checkForNewDocumentAction(Long procedureId) {
        Optional<StateProcedure> optionalSP = stateProcedureService.getCurrentStateProcedureById(procedureId);
        SuccessOutput output = new SuccessOutput();
        if (optionalSP.isPresent()) {
            String codeState = optionalSP.get().getCodeState();
            AtomicBoolean isManager = new AtomicBoolean(false);
            AtomicBoolean isInstructor = new AtomicBoolean(false);
            Set<String> userRolesFiltered = getUserRoles();
            userRolesFiltered.forEach(role -> {
                if (role.startsWith(Constant.INSTRUCTOR_ROLE_PREFIX)) {
                    isInstructor.set(true);
                }
                if (role.startsWith(Constant.MANAGER_ROLE_PREFIX)) {
                    isManager.set(true);
                }
            });
            output.setSuccess((isInstructor.get() && codeState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE))
                    || (isManager.get() && (codeState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_DECRETO)
                    || codeState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_DINIEGO)
                    || codeState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_RDP)
                    || codeState.equalsIgnoreCase(ProcedureStateConst.IN_CHIUSURA))));
        } else {
            output.setSuccess(false);
        }
        return output;
    }

}
