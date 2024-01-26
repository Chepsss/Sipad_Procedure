package it.almaviva.difesa.cessazione.procedure.service;

import it.almaviva.difesa.cessazione.procedure.ProcedureApplication;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.ProcedureStateConst;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.camunda.*;
import it.almaviva.difesa.cessazione.procedure.service.rest.CamundaRestClient;
import it.almaviva.difesa.cessazione.procedure.validation.ValidationProcedure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CamundaRestClient.class, ProcedureApplication.class})
@ActiveProfiles("test")
public class CamundaRestClientIntegrationTest {

    @Autowired
    CamundaRestClient camundaRestClient;

    @Test
    public void testStartNewProcess() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);
        ProcessInstance checkProcessInstance = camundaRestClient.getProcessInstance(processInstance.getId());
        assertNotNull(checkProcessInstance);

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstance.getId());
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            camundaRestClient.getProcessInstance(processInstance.getId());
        });
        assertNotNull(exception);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testListTask() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);

        List<Task> taskList = camundaRestClient.listTask(processInstance.getId());
        assertNotNull(taskList);
        assertEquals(taskList.size(), 1);

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstance.getId());
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            camundaRestClient.getProcessInstance(processInstance.getId());
        });
        assertNotNull(exception);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCompleteTask() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);

        List<Task> taskList = camundaRestClient.listTask(processInstance.getId());
        assertNotNull(taskList);
        assertEquals(taskList.size(), 1);
        Task task = taskList.get(0);

        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_NEXT_STATE, new CamundaVariable(ProcedureStateConst.LAVORAZIONE, null));
        camundaRestClient.completeTask(task.getId(), variables);

        List<Task> updateTaskList = camundaRestClient.listTask(processInstance.getId());
        assertNotNull(updateTaskList);
        assertEquals(updateTaskList.size(), 1);
        Task nextTask = updateTaskList.get(0);

        assertNotEquals(nextTask.getId(), task.getId());

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstance.getId());
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            camundaRestClient.getProcessInstance(processInstance.getId());
        });
        assertNotNull(exception);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testEvaluateStateDecision() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);

        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_CURRENT_STATE, new CamundaVariable(ProcedureStateConst.LAVORAZIONE, Constant.STRING));

        List<StateContainer> stateList = camundaRestClient.evaluateStateDecision(variables);
        assertNotNull(stateList);
        assertEquals(stateList.size(), 4);

        boolean find = false;
        for (StateContainer stateContainer : stateList) {
            if (stateContainer.getNextState().getValue().equals(ProcedureStateConst.CHIUSURA_PA_DINIEGO) &&
                    stateContainer.getLabel().getValue().equals("Invia a chiusura PA (diniego)")) {
                find = true;
                break;
            }
        }
        assertTrue(find);

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstance.getId());
        ServiceException exception = assertThrows(ServiceException.class, () -> camundaRestClient.getProcessInstance(processInstance.getId()));
        assertNotNull(exception);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testEvaluateRoleDecision() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);

        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_STATE, new CamundaVariable(ProcedureStateConst.LAVORAZIONE, Constant.STRING));
        variables.put(Constant.VARIABLE_EMPLOYEE_CATEGORY, new CamundaVariable("Gra.", Constant.STRING));

        List<RoleContainer> roleList = camundaRestClient.evaluateRoleDecision(variables);
        assertNotNull(roleList);
        assertEquals(roleList.size(), 1);

        assertEquals(roleList.get(0).getRole().getValue(), Constant.INSTRUCTOR_GRADUATED_ROLE_ID);

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstance.getId());
        ServiceException exception = assertThrows(ServiceException.class, () -> camundaRestClient.getProcessInstance(processInstance.getId()));
        assertNotNull(exception);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testEvaluateProcedureOpeningDataVisibility() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);

        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_ID_TIPO_ATTIVAZIONE, new CamundaVariable("ISTPAR", Constant.STRING));
        variables.put(Constant.VARIABLE_ID_TIPO_PROCEDIMENTO, new CamundaVariable("CszLimitiEtà", Constant.STRING));

        List<Map<String, CamundaOutputVariable>> openingDataVisibility = camundaRestClient.evaluateOpeningDataVisibility(variables);
        assertNotNull(openingDataVisibility);
        assertEquals(openingDataVisibility.size(), 15);

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstance.getId());
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            camundaRestClient.getProcessInstance(processInstance.getId());
        });
        assertNotNull(exception);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testEvaluateDichiarazioniDecision() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);

        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_ID_TIPO_PROCEDIMENTO, new CamundaVariable("CszAusiliaria", Constant.STRING));
        variables.put(Constant.VARIABLE_FASE, new CamundaVariable("ISTRUTTORIA", Constant.STRING));

        List<Map<String, CamundaOutputVariable>> declarationsControls = camundaRestClient.evaluateDeclarationsDecision(variables);
        assertNotNull(declarationsControls);
        assertEquals(declarationsControls.get(0).size(), 20);
        assertTrue(declarationsControls.get(0).containsKey("D02"));
        assertTrue(declarationsControls.get(0).containsKey("C15"));

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstance.getId());
        ServiceException exception = assertThrows(ServiceException.class, () -> camundaRestClient.getProcessInstance(processInstance.getId()));
        assertNotNull(exception);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testEvaluateLeaveDecision() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);

        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_ID_TIPO_ATTIVAZIONE, new CamundaVariable("ISTPAR", Constant.STRING));
        variables.put(Constant.VARIABLE_ID_TIPO_PROCEDIMENTO, new CamundaVariable("CszAusiliaria", Constant.STRING));
        variables.put(ValidationProcedure.ID_TIPO_CESSAZIONE, new CamundaVariable("BD154", Constant.STRING));
        variables.put(ValidationProcedure.SGTPO_ACR_POSIZIONE, new CamundaVariable("AUS", Constant.STRING));
        variables.put(Constant.VARIABLE_D01, new CamundaVariable(false, Constant.BOOLEAN));
        variables.put(Constant.VARIABLE_ANNI_SERVIZIO_EFF, new CamundaVariable(32, "Integer"));
        variables.put("sgctpCodCatpers", new CamundaVariable("SottUff.", Constant.STRING));

        List<Map<String, CamundaOutputVariable>> leaveVisibility = camundaRestClient.evaluateLeaveDecision(variables);
        assertNotNull(leaveVisibility);
        assertEquals(leaveVisibility.get(0).size(), 1);
        assertTrue(leaveVisibility.get(0).containsKey("idCatPersSpettante"));
        assertEquals("RIS", leaveVisibility.get(0).entrySet().stream().findFirst().get().getValue().getValue());

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstance.getId());
        ServiceException exception = assertThrows(ServiceException.class, () -> camundaRestClient.getProcessInstance(processInstance.getId()));
        assertNotNull(exception);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testEvaluateTransitDataDecision() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);

        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(Constant.VARIABLE_FASE, new CamundaVariable("ISTRUTTORIA", Constant.STRING));
        variables.put("flagIdoneoTransito", new CamundaVariable(true, Constant.BOOLEAN));
        variables.put("flagPresentataIstanza", new CamundaVariable(true, Constant.BOOLEAN));
        variables.put("flagPresRinuncia", new CamundaVariable(false, Constant.BOOLEAN));
        variables.put("flagNoautoriz", new CamundaVariable(false, Constant.BOOLEAN));
        variables.put("flagFirmaContratto", new CamundaVariable(null, Constant.BOOLEAN));

        List<Map<String, CamundaOutputVariable>> transitDataVisibility = camundaRestClient.evaluateTransitDataDecision(variables);
        assertNotNull(transitDataVisibility);
        assertEquals(transitDataVisibility.size(), 3);
        assertTrue(transitDataVisibility.get(0).containsKey("protDomandaPersomil"));
        assertTrue(transitDataVisibility.get(1).containsKey("dataRinunciaPersomil"));
        assertTrue(transitDataVisibility.get(2).containsKey("protNoautPersociv"));

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstance.getId());
        ServiceException exception = assertThrows(ServiceException.class, () -> camundaRestClient.getProcessInstance(processInstance.getId()));
        assertNotNull(exception);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testEvaluatePensionDataDecision() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);

        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put("modCompilazione", new CamundaVariable("A", Constant.STRING));

        List<Map<String, CamundaOutputVariable>> pensionDataVisibility = camundaRestClient.evaluatePensionDataDecision(variables);
        assertNotNull(pensionDataVisibility);
        assertEquals(pensionDataVisibility.size(), 1);
        assertTrue(pensionDataVisibility.get(0).containsKey("anniAnzContr"));
        assertTrue(pensionDataVisibility.get(0).containsKey("mesiAnzContr"));
        assertEquals("readonly", pensionDataVisibility.get(0).entrySet().stream().findFirst().get().getValue().getValue());

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstance.getId());
        ServiceException exception = assertThrows(ServiceException.class, () -> camundaRestClient.getProcessInstance(processInstance.getId()));
        assertNotNull(exception);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testEvaluateGmlDecision() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);

        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(ValidationProcedure.INSTANCE, new CamundaVariable(2, Constant.INTEGER));
        variables.put(ValidationProcedure.GML, new CamundaVariable("PNI al s.m.i. in forma parziale in riserva", Constant.STRING));
        variables.put(ValidationProcedure.D05, new CamundaVariable(false, Constant.BOOLEAN));
        variables.put(ValidationProcedure.SGTPO_ACR_POSIZIONE, new CamundaVariable("RIS", Constant.STRING));

        List<Map<String, CamundaOutputVariable>> gmlVisibility = camundaRestClient.evaluateGmlDecision(variables);
        assertNotNull(gmlVisibility);
        assertEquals(gmlVisibility.size(), 1);
        assertTrue(gmlVisibility.get(0).containsKey("response"));
        assertEquals("ok", gmlVisibility.get(0).entrySet().stream().findFirst().get().getValue().getValue());

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstance.getId());
        ServiceException exception = assertThrows(ServiceException.class, () -> camundaRestClient.getProcessInstance(processInstance.getId()));
        assertNotNull(exception);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

}
