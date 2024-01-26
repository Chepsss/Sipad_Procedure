package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.camunda.AssignmentContainer;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaOutputVariable;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaVariable;
import it.almaviva.difesa.cessazione.procedure.model.camunda.ProcessInstance;
import it.almaviva.difesa.cessazione.procedure.model.camunda.RoleContainer;
import it.almaviva.difesa.cessazione.procedure.model.camunda.StateContainer;
import it.almaviva.difesa.cessazione.procedure.model.camunda.Task;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ParereRagVisibilityRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpDostaStatoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrattAttivazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrtpoTprocedimentoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgctpCatpersonaleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgtpoPosizioneStatoDTO;
import it.almaviva.difesa.cessazione.procedure.service.rest.CamundaRestClient;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbDocumento;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.DocumentOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class CamundaService {

    private final CamundaRestClient camundaRestClient;
    private final SipadClient sipadClient;
    private final TpSgtpoPosizioneStatoService tpSgtpoPosizioneStatoService;
    private final TpSgctpCatPersonaleService tpSgctpCatPersonaleService;

    public List<StateContainer> listNextStates(String currentState) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_CURRENT_STATE, new CamundaVariable(currentState, Constant.STRING));
        return camundaRestClient.evaluateStateDecision(variables);
    }

    public List<String> listRolesByStatus(String currentState, String codeCatPers) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_STATE, new CamundaVariable(currentState, Constant.STRING));
        variables.put(Constant.VARIABLE_EMPLOYEE_CATEGORY, new CamundaVariable(codeCatPers, Constant.STRING));
        List<RoleContainer> roleList = camundaRestClient.evaluateRoleDecision(variables);
        return roleList
                .stream()
                .map(it -> it.getRole().getValue().toString())
                .collect(Collectors.toList());
    }

    public Boolean checkIfReassignment(String currentState, String nextState) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_CURRENT_STATE, new CamundaVariable(currentState, Constant.STRING));
        variables.put(Constant.VARIABLE_NEXT_STATE, new CamundaVariable(nextState, Constant.STRING));
        String reassignment = camundaRestClient.evaluateReassignmentDecision(variables).stream()
                .findFirst()
                .map(AssignmentContainer::getReassignment)
                .map(CamundaOutputVariable::getValue)
                .map(Object::toString)
                .orElse(null);
        return Objects.nonNull(reassignment) && reassignment.equalsIgnoreCase(Constant.YES_OUTPUT);
    }

    public String createProcedure() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        return processInstance.getId();
    }

    public void changeState(String processInstanceId, String codeState, String roleManager) {
        String taskId = getTaskId(processInstanceId);
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_NEXT_STATE, new CamundaVariable(codeState, null));
        if (Objects.nonNull(roleManager)) {
            variables.put(Constant.VARIABLE_ROLE, new CamundaVariable(roleManager, null));
        }
        camundaRestClient.completeTask(taskId, variables);
    }

    public void endProcessInstance(String processInstanceId) {
        String taskId = getTaskId(processInstanceId);
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_END_EVENT, new CamundaVariable("closePa", null));
        camundaRestClient.completeTask(taskId, variables);
    }

    private String getTaskId(String processInstanceId) {
        List<Task> processTask = camundaRestClient.listTask(processInstanceId);
        if (processTask.isEmpty()) {
            log.error(String.format("ProcessInstance with Id %s is not found", processInstanceId));
            return null;
        }
        return processTask.get(0).getId();
    }

    public void deleteProcessInstance(String processInstanceId) {
        try {
            camundaRestClient.deleteProcessInstance(processInstanceId);
        } catch (ServiceException e) {
            log.error("Error in cancel camunda process instance with id: {} Error: => {}", processInstanceId, e.getMessage());
        }
    }

    public List<Map<String, CamundaOutputVariable>> getProcedureOpeningDataVisibility(Map<String, CamundaVariable> variables) {
        return camundaRestClient.evaluateOpeningDataVisibility(variables);
    }

    public Map<String, Integer> getDeclarationVisibility(Map<String, CamundaVariable> variables) {
        List<Map<String, CamundaOutputVariable>> declarations = camundaRestClient.evaluateDeclarationsDecision(variables);
        Map<String, Integer> declarationMap = new LinkedHashMap<>();
        if (declarations.isEmpty()) {
            log.debug(String.format("GetDeclarationVisibility with variables %s is not found", variables));
            return declarationMap;
        }
        Map<String, CamundaOutputVariable> declarationList = declarations.get(0);
        declarationList.keySet().forEach(key -> declarationMap.put(key, (Integer) declarationList.get(key).getValue()));
        return declarationMap;
    }

    public List<Map<String, CamundaOutputVariable>> getPensionDataVisibility(Map<String, CamundaVariable> variables) {
        List<Map<String, CamundaOutputVariable>> pensionData = camundaRestClient.evaluatePensionDataDecision(variables);
        List<Map<String, CamundaOutputVariable>> pensionDataListOutput = new ArrayList<>();
        for (Map<String, CamundaOutputVariable> map : pensionData) {
            for (Map.Entry<String, CamundaOutputVariable> entry : map.entrySet()) {
                Map<String, CamundaOutputVariable> m = new LinkedHashMap<>();
                m.put(entry.getKey(), entry.getValue());
                pensionDataListOutput.add(m);
            }
        }
        return pensionDataListOutput;
    }

    public List<Map<String, CamundaOutputVariable>> getProcedureTransitDataVisibility(Map<String, CamundaVariable> variables) {
        List<Map<String, CamundaOutputVariable>> transitDataDecision = camundaRestClient.evaluateTransitDataDecision(variables);
        List<Map<String, CamundaOutputVariable>> dataTransit = new ArrayList<>();
        transitDataDecision.forEach(item -> item.entrySet().forEach((entry -> {
            Map<String, CamundaOutputVariable> dataTransitMap = new LinkedHashMap<>();
            dataTransitMap.put(entry.getKey(), entry.getValue());
            dataTransit.add(dataTransitMap);
        })));
        return dataTransit;
    }

    public Map<String, String> getGmlVisibility(Map<String, CamundaVariable> variables) {
        List<Map<String, CamundaOutputVariable>> gmlDecision = camundaRestClient.evaluateGmlDecision(variables);
        Map<String, String> gmlDataMap = new LinkedHashMap<>();
        if (gmlDecision.isEmpty()) {
            log.debug(String.format("GetGmlVisibility with variables %s is not found", variables));
            return gmlDataMap;
        }
        Map<String, CamundaOutputVariable> gmlDataList = gmlDecision.get(0);
        gmlDataList.keySet().forEach(key -> gmlDataMap.put(key, (String) gmlDataList.get(key).getValue()));
        return gmlDataMap;
    }

    public Map<String, String> getDocumentActionsByProc(String userRole, String stateProcedure) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_ROLE, new CamundaVariable(userRole, Constant.STRING));
        variables.put(Constant.VARIABLE_STATE, new CamundaVariable(stateProcedure, Constant.STRING));
        List<Map<String, CamundaOutputVariable>> documentActionsByProc = camundaRestClient.evaluateDocumentActionsByProcDecision(variables);
        Map<String, String> documentActionsDataMap = new LinkedHashMap<>();
        if (documentActionsByProc.isEmpty()) {
            log.debug(String.format("GetDocumentActionsByProc with variables %s is not found", variables));
            return documentActionsDataMap;
        }
        Map<String, CamundaOutputVariable> documentActionsByProcList = documentActionsByProc.get(0);
        documentActionsDataMap = documentActionsByProcList.entrySet()
                .parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (String) entry.getValue().getValue(), (prev, next) -> next, HashMap::new));
        return documentActionsDataMap;
    }

    public synchronized Map<String, String> getDocumentActionsByDoc(TbDocumento document) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        TpDostaStatoDTO stato = sipadClient.dostaStatoById(document.getIdStato());
        String documentType = document.getIdTipo();
        String documentState = stato.getAcrSta();
        Boolean documentEditable = document.getEditabile();
        String documentSubStato = document.getSubStato();
        return getActions(variables, documentType, documentState, documentEditable, documentSubStato);
    }

    public Map<String, String> getDocumentActionsByDoc(DocumentOutput document) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        TpDostaStatoDTO stato = sipadClient.dostaStatoById(document.getIdStato());
        String documentType = document.getCodTipo();
        String documentState = stato.getAcrSta();
        Boolean documentEditable = document.getEditabile();
        String documentSubStato = document.getSubStato();
        return getActions(variables, documentType, documentState, documentEditable, documentSubStato);
    }

    private Map<String, String> getActions(Map<String, CamundaVariable> variables, String documentType, String documentState, Boolean documentEditable, String documentSubStato) {
        variables.put(Constant.VARIABLE_TIPO_DOCUMENTO, new CamundaVariable(documentType, Constant.STRING));
        variables.put(Constant.VARIABLE_STATO, new CamundaVariable(documentState, Constant.STRING));
        variables.put(Constant.VARIABLE_EDITABILE, new CamundaVariable(documentEditable, Constant.BOOLEAN));
        variables.put(Constant.VARIABLE_SUB_STATO, new CamundaVariable(documentSubStato, Constant.STRING));
        List<Map<String, CamundaOutputVariable>> documentActionsByDoc = camundaRestClient.evaluateDocumentActionsByDocDecision(variables);
        Map<String, String> documentActionsDataMap = new LinkedHashMap<>();
        if (documentActionsByDoc.isEmpty()) {
            log.debug(String.format("GetDocumentActionsByDoc with variables %s is not found", variables));
            return documentActionsDataMap;
        }
        Map<String, CamundaOutputVariable> documentActionsByDocList = documentActionsByDoc.get(0);
        documentActionsDataMap = documentActionsByDocList.entrySet()
                .parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (String) entry.getValue().getValue(), (prev, next) -> next, HashMap::new));
        return documentActionsDataMap;
    }

    public Map<String, String> getRWOpeningDataVisibility(String stateProcedure) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_STATE, new CamundaVariable(stateProcedure, Constant.STRING));
        List<Map<String, CamundaOutputVariable>> rwOpeningDataDecision = camundaRestClient.evaluateRWOpeningDataDecision(variables);
        return getOutputMap(rwOpeningDataDecision);
    }

    public Map<String, String> getRWDomicilioElettoVisibility(String stateProcedure) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_STATE, new CamundaVariable(stateProcedure, Constant.STRING));
        List<Map<String, CamundaOutputVariable>> rwDomicilioElettoDecision = camundaRestClient.evaluateRWDomicilioElettoDecision(variables);
        return getOutputMap(rwDomicilioElettoDecision);
    }

    public Map<String, String> getRWDichiarazioniControlliVisibility(String stateProcedure) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_STATE, new CamundaVariable(stateProcedure, Constant.STRING));
        List<Map<String, CamundaOutputVariable>> rwDichiarazioniControlliDecision = camundaRestClient.evaluateRWDichiarazioniControlliDecision(variables);
        return getOutputMap(rwDichiarazioniControlliDecision);
    }

    public Map<String, String> getRWDatiTransitoVisibility(String stateProcedure) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_STATE, new CamundaVariable(stateProcedure, Constant.STRING));
        List<Map<String, CamundaOutputVariable>> rwDatiTransitoDecision = camundaRestClient.evaluateRWDatiTransitoDecision(variables);
        return getOutputMap(rwDatiTransitoDecision);
    }

    public Map<String, String> getRWDatiPensionisticiVisibility(String stateProcedure) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_STATE, new CamundaVariable(stateProcedure, Constant.STRING));
        List<Map<String, CamundaOutputVariable>> rwDatiPensionisticiDecision = camundaRestClient.evaluateRWDatiPensionisticiDecision(variables);
        return getOutputMap(rwDatiPensionisticiDecision);
    }

    public Map<String, String> getRWParereRagioneriaVisibility(ParereRagVisibilityRequest request) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_STATE, new CamundaVariable(request.getStateProcedure(), Constant.STRING));
        variables.put(Constant.VARIABLE_VISTO_RAGIONERIA, new CamundaVariable(request.getFlVistoRagioneria(), Constant.BOOLEAN));
        List<Map<String, CamundaOutputVariable>> rwParereRagioneriaDecision = camundaRestClient.evaluateRWParereRagioneriaDecision(variables);
        return getOutputMap(rwParereRagioneriaDecision);
    }

    private Map<String, String> getOutputMap(List<Map<String, CamundaOutputVariable>> dataDecision) {
        Map<String, String> dataMap = new LinkedHashMap<>();
        if (!dataDecision.isEmpty()) {
            Map<String, CamundaOutputVariable> rwDataList = dataDecision.get(0);
            if (rwDataList.containsKey(Constant.SHOW_TAB) && rwDataList.get(Constant.SHOW_TAB).getValue().equals(Constant.NO_OUTPUT)) {
                dataMap.put(Constant.SHOW_TAB, rwDataList.get(Constant.SHOW_TAB).getValue().toString());
            } else {
                rwDataList.keySet().forEach(key -> dataMap.put(key, (String) rwDataList.get(key).getValue()));
            }
        }
        return dataMap;
    }

    public String getCongedoSpettante(Procedure procedure, Map<String, CamundaVariable> variables, Boolean D01, Boolean C08, Integer anniServizioEff) {
        if (Objects.nonNull(procedure.getOpeningCessation())) {
            TpPrattAttivazioneDTO dto = sipadClient.prattAttivazioneById(procedure.getOpeningCessation());
            variables.put(Constant.VARIABLE_ID_TIPO_ATTIVAZIONE, new CamundaVariable(dto.getPrattAcrAtt(), Constant.STRING));
        } else {
            variables.put(Constant.VARIABLE_ID_TIPO_ATTIVAZIONE, new CamundaVariable("", Constant.STRING));
        }
        if (Objects.nonNull(procedure.getTypeCessation())) {
            TpPrtpoTprocedimentoDTO dto = sipadClient.tipoProcedimentoById(procedure.getTypeCessation());
            variables.put(Constant.VARIABLE_ID_TIPO_PROCEDIMENTO, new CamundaVariable(dto.getPrtpoAcrProc(), Constant.STRING));
        } else {
            variables.put(Constant.VARIABLE_ID_TIPO_PROCEDIMENTO, new CamundaVariable("", Constant.STRING));
        }
        variables.put(Constant.VARIABLE_ID_MOTIVO, new CamundaVariable(
                Objects.nonNull(procedure.getReasonCessation()) ? procedure.getReasonCessation().getIdMotivo() : "", Constant.STRING));
        if (Objects.nonNull(procedure.getCategLeaveReq())) {
            TpSgtpoPosizioneStatoDTO categoryLeave = tpSgtpoPosizioneStatoService.getCategLeaveReqById(procedure.getCategLeaveReq());
            variables.put(Constant.VARIABLE_CAT_PERSONALE_RICHIESTA, new CamundaVariable(
                    Objects.nonNull(categoryLeave.getSgtpoAcrPosizione()) ? categoryLeave.getSgtpoAcrPosizione() : "", Constant.STRING));
        }
        variables.put(Constant.VARIABLE_D01, new CamundaVariable(D01, Constant.BOOLEAN));
        variables.put(Constant.VARIABLE_C08, new CamundaVariable(C08, Constant.BOOLEAN));
        variables.put(Constant.VARIABLE_ANNI_SERVIZIO_EFF, new CamundaVariable(anniServizioEff, Constant.INTEGER));
        if (Objects.nonNull(procedure.getIdCatMilitare())) {
            TpSgctpCatpersonaleDTO catMilitareDTO = tpSgctpCatPersonaleService.getCatMilitareById(procedure.getIdCatMilitare());
            variables.put(Constant.VARIABLE_CAT_MILITARE, new CamundaVariable(
                    Objects.nonNull(catMilitareDTO.getSgctpCodCatpers()) ? catMilitareDTO.getSgctpCodCatpers() : "", Constant.STRING));
        }
        List<Map<String, CamundaOutputVariable>> congedoSpettanteResult = camundaRestClient.evaluateCongedoSpettanteDecision(variables);
        if (congedoSpettanteResult.isEmpty()) {
            log.debug(String.format("Congedo spettante with variables %s is not found", variables));
            return null;
        }
        Map<String, CamundaOutputVariable> congedoSpettante = congedoSpettanteResult.get(0);
        return congedoSpettante.entrySet().iterator().next().getValue().getValue().toString();
    }

    public Map<String, String> getDocumentAdhocNegativeOutcomeDecision(String statoPredisposizione, String statoProcedimento, String tipoDoc, String statoDoc) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_STATO_RICHIESTA_ADHOC, new CamundaVariable(statoPredisposizione, Constant.STRING));
        variables.put(Constant.VARIABLE_STATO_PROCEDIMENTO, new CamundaVariable(statoProcedimento, Constant.STRING));
        variables.put(Constant.VARIABLE_TIPO_DOCUMENTO, new CamundaVariable(tipoDoc, Constant.STRING));
        variables.put(Constant.VARIABLE_STATO_DOCUMENTO, new CamundaVariable(statoDoc, Constant.STRING));
        List<Map<String, CamundaOutputVariable>> response = camundaRestClient.evaluateDocumentAdhocNegativeOutcomeDecision(variables);
        return getOutputMap(response);
    }

}
