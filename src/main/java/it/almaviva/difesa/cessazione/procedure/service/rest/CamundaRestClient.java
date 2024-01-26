package it.almaviva.difesa.cessazione.procedure.service.rest;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.camunda.AssignmentContainer;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaOutputVariable;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaVariable;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaVariablesReq;
import it.almaviva.difesa.cessazione.procedure.model.camunda.ProcessInstance;
import it.almaviva.difesa.cessazione.procedure.model.camunda.RoleContainer;
import it.almaviva.difesa.cessazione.procedure.model.camunda.StateContainer;
import it.almaviva.difesa.cessazione.procedure.model.camunda.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CamundaRestClient extends BaseRestClient {

    @Value("${application.camunda.baseurl}")
    String camundaBaseUrl;

    @Value("${application.camunda.processKey}")
    String processKey;

    @Value("${application.camunda.processDefinitionPath}")
    String processDefinitionPath;

    @Value("${application.camunda.processInstancePath}")
    String processInstancePath;

    @Value("${application.camunda.taskPath}")
    String taskPath;

    @Value("${application.camunda.decisionDefinitionPath}")
    String decisionDefinitionPath;

    @Value("${application.camunda.stateDecisionKey}")
    String stateDecisionKey;

    @Value("${application.camunda.roleDecisionKey}")
    String roleDecisionKey;

    @Value("${application.camunda.openingDataVisibilityKey}")
    String openingDataVisibilityKey;

    @Value("${application.camunda.dichiarazioniDecisionKey}")
    String dichiarazioniDecisionKey;

    @Value("${application.camunda.leaveDecisionKey}")
    String leaveDecisionKey;

    @Value("${application.camunda.transitDataDecisionKey}")
    String transitDataDecisionKey;

    @Value("${application.camunda.pensionDataDecisionKey}")
    String pensionDataDecisionKey;

    @Value("${application.camunda.gmlDecisionKey}")
    String gmlDataDecisionKey;

    @Value("${application.camunda.documentActionsByProcDecisionKey}")
    String documentActionsByProcDecisionKey;

    @Value("${application.camunda.documentActionsByDocDecisionKey}")
    String documentActionsByDocDecisionKey;

    @Value("${application.camunda.rwOpeningDataDecisionKey}")
    String rwOpeningDataDecisionKey;

    @Value("${application.camunda.rwDomicilioElettoDecisionKey}")
    String rwDomicilioElettoDecisionKey;

    @Value("${application.camunda.rwDichiarazioniDecisionKey}")
    String rwDichiarazioniDecisionKey;

    @Value("${application.camunda.rwDatiTransitoDecisionKey}")
    String rwDatiTransitoDecisionKey;

    @Value("${application.camunda.rwDatiPensionisticiDecisionKey}")
    String rwDatiPensionisticiDecisionKey;

    @Value("${application.camunda.rwParereRagioneriaDecisionKey}")
    String rwParereRagioneriaDecisionKey;

    @Value("${application.camunda.congedoSpettanteDecisionKey}")
    String congedoSpettanteDecisionKey;

    @Value("${application.camunda.documentAdhocNegativeOutcomeDecisionKey}")
    String documentAdhocNegativeOutcomeDecisionKey;

    @Value("${application.camunda.reassignmentDecisionKey}")
    String reassignmentDecisionKey;

    @Value("${application.camunda.tenant-id}")
    String tenantId;

    public ProcessInstance startNewProcess() {
        String uri = String.format(Constant.CAMUNDA_START_PROCESS, camundaBaseUrl, processDefinitionPath, processKey, tenantId);
        return postCall(uri, "{}", ProcessInstance.class);
    }

    public void deleteProcessInstance(String processInstanceId) {
        String uri = String.format(Constant.CAMUNDA_PROCESS, camundaBaseUrl, processInstancePath, processInstanceId);
        deleteCall(uri, String.class);
    }

    public ProcessInstance getProcessInstance(String processInstanceId) {
        String uri = String.format(Constant.CAMUNDA_PROCESS, camundaBaseUrl, processInstancePath, processInstanceId);
        return getCall(uri, ProcessInstance.class);
    }

    public List<Task> listTask(String processInstanceId) {
        String uri = String.format(Constant.CAMUNDA_LIST_TASK, camundaBaseUrl, taskPath, processInstanceId);
        return getCall(uri, new ParameterizedTypeReference<>() {
        });
    }

    public void completeTask(String taskId, Map<String, CamundaVariable> variables) {
        String uri = String.format(Constant.CAMUNDA_COMPLETE_TASK, camundaBaseUrl, taskPath, taskId);
        CamundaVariablesReq camundaVariablesReq = new CamundaVariablesReq(variables);
        postCall(uri, camundaVariablesReq, ProcessInstance.class);
    }

    public List<StateContainer> evaluateStateDecision(Map<String, CamundaVariable> variables) {
        String uri = String.format(Constant.CAMUNDA_KEY_S_EVALUATE, camundaBaseUrl, decisionDefinitionPath, stateDecisionKey, tenantId);
        CamundaVariablesReq camundaVariablesReq = new CamundaVariablesReq(variables);
        return postCall(uri, camundaVariablesReq, new ParameterizedTypeReference<>() {
        });
    }

    public List<RoleContainer> evaluateRoleDecision(Map<String, CamundaVariable> variables) {
        String uri = String.format(Constant.CAMUNDA_KEY_S_EVALUATE, camundaBaseUrl, decisionDefinitionPath, roleDecisionKey, tenantId);
        CamundaVariablesReq camundaVariablesReq = new CamundaVariablesReq(variables);
        return postCall(uri, camundaVariablesReq, new ParameterizedTypeReference<>() {
        });
    }

    public List<AssignmentContainer> evaluateReassignmentDecision(Map<String, CamundaVariable> variables) {
        String uri = String.format(Constant.CAMUNDA_KEY_S_EVALUATE, camundaBaseUrl, decisionDefinitionPath, reassignmentDecisionKey, tenantId);
        CamundaVariablesReq camundaVariablesReq = new CamundaVariablesReq(variables);
        return postCall(uri, camundaVariablesReq, new ParameterizedTypeReference<>() {
        });
    }

    public List<Map<String, CamundaOutputVariable>> evaluateOpeningDataVisibility(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, openingDataVisibilityKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateDeclarationsDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, dichiarazioniDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateLeaveDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, leaveDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateTransitDataDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, transitDataDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluatePensionDataDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, pensionDataDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateGmlDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, gmlDataDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateDocumentActionsByProcDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, documentActionsByProcDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateDocumentActionsByDocDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, documentActionsByDocDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateRWOpeningDataDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, rwOpeningDataDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateRWDomicilioElettoDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, rwDomicilioElettoDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateRWDichiarazioniControlliDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, rwDichiarazioniDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateRWDatiTransitoDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, rwDatiTransitoDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateRWDatiPensionisticiDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, rwDatiPensionisticiDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateRWParereRagioneriaDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, rwParereRagioneriaDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateCongedoSpettanteDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, congedoSpettanteDecisionKey);
    }

    public List<Map<String, CamundaOutputVariable>> evaluateDocumentAdhocNegativeOutcomeDecision(Map<String, CamundaVariable> variables) {
        return getCamundaResult(variables, documentAdhocNegativeOutcomeDecisionKey);
    }

    private List<Map<String, CamundaOutputVariable>> getCamundaResult(Map<String, CamundaVariable> variables, String key) {
        String uri = String.format(Constant.CAMUNDA_KEY_S_EVALUATE, camundaBaseUrl, decisionDefinitionPath, key, tenantId);
        CamundaVariablesReq camundaVariablesReq = new CamundaVariablesReq(variables);
        return postCall(uri, camundaVariablesReq, new ParameterizedTypeReference<>() {
        });
    }

}
