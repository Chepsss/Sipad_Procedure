package it.almaviva.difesa.cessazione.procedure.service;

import it.almaviva.difesa.cessazione.procedure.ProcedureApplication;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.ProcedureStateConst;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaVariable;
import it.almaviva.difesa.cessazione.procedure.model.camunda.ProcessInstance;
import it.almaviva.difesa.cessazione.procedure.model.camunda.Task;
import it.almaviva.difesa.cessazione.procedure.service.msproc.CamundaService;
import it.almaviva.difesa.cessazione.procedure.service.rest.CamundaRestClient;
import it.almaviva.difesa.cessazione.procedure.validation.ValidationProcedure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CamundaRestClient.class, ProcedureApplication.class})
@ActiveProfiles("test")
public class CamundaServiceIntegrationTest {

    @Autowired
    CamundaService camundaService;

    @Autowired
    CamundaRestClient camundaRestClient;

    @Test
    public void testCreateProcedure() {
        String processId = camundaService.createProcedure();
        assertNotNull(processId);
        ProcessInstance checkProcessInstance = camundaRestClient.getProcessInstance(processId);
        assertNotNull(checkProcessInstance);

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processId);
        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> camundaRestClient.getProcessInstance(processId));
        assertNotNull(exception);
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testChangeStatus() {
        ProcessInstance processInstance = camundaRestClient.startNewProcess();
        assertNotNull(processInstance);
        String processInstanceId = processInstance.getId();
        List<Task> taskList = camundaRestClient.listTask(processInstanceId);
        assertNotNull(taskList);
        assertEquals(taskList.size(), 1);
        camundaService.changeState(processInstanceId, ProcedureStateConst.LAVORAZIONE, null);

        // Delete the process instance and verify
        camundaRestClient.deleteProcessInstance(processInstanceId);
        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> camundaRestClient.getProcessInstance(processInstanceId));
        assertNotNull(exception);
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGmlData() {
        Map<String, CamundaVariable> variables = new HashMap<>();
        variables.put(ValidationProcedure.INSTANCE, new CamundaVariable(2, Constant.INTEGER));
        variables.put(ValidationProcedure.GML, new CamundaVariable("PNI al s.m.i. in forma parziale in riserva", Constant.STRING));
        variables.put(ValidationProcedure.D05, new CamundaVariable(false, Constant.BOOLEAN));
        variables.put(ValidationProcedure.SGTPO_ACR_POSIZIONE, new CamundaVariable("AUS", Constant.STRING));

        Map<String, String> gmlData = camundaService.getGmlVisibility(variables);
        assertNotNull(gmlData);
        assertEquals(gmlData.size(), 2);
    }

}
