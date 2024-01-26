package it.almaviva.difesa.cessazione.procedure.service;

import it.almaviva.difesa.cessazione.procedure.ProcedureApplication;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.model.camunda.Task;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureOpeningDataDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.ProcedureMapper;
import it.almaviva.difesa.cessazione.procedure.service.msproc.ProcedureService;
import it.almaviva.difesa.cessazione.procedure.service.rest.CamundaRestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CamundaRestClient.class, ProcedureApplication.class})
@ActiveProfiles("test")
public class ProcedureServiceIntegrationTest {

    @Autowired
    ProcedureService procedureService;

    @Autowired
    CamundaRestClient camundaRestClient;

    @Autowired
    ProcedureMapper procedureMapper;

    @Test
    public void testCreateProcedure() {
        ProcedureOpeningDataDTO procedureOpeningDataDTO = new ProcedureOpeningDataDTO();
        procedureOpeningDataDTO.setIdAuthor(1L);
        procedureOpeningDataDTO.setIdAssignedTo(1L);
        procedureOpeningDataDTO.setEmployeeId(1L);
        procedureOpeningDataDTO.setStateProcedureId(1L);
        procedureOpeningDataDTO.setIdTipoAttivazione(2L);
        procedureOpeningDataDTO.setIdTipoProcedimento(32L);
        procedureOpeningDataDTO.setIdTipoCessazione(90L);
        ProcedureDTO procedureDTO = procedureService.createOrUpdateProcedureOpeningData(procedureOpeningDataDTO);
        assertNotNull(procedureDTO);
        Procedure procedure = procedureMapper.toEntity(procedureDTO);
        String camundaProcessInstanceId = procedure.getBpmnProcessId();
        assertNotNull(camundaProcessInstanceId);
        List<Task> task = camundaRestClient.listTask(camundaProcessInstanceId);
        assertNotNull(task);
        assertEquals(task.size(), 1);

        //Delete procedure
        Long procedureId = procedure.getId();
        procedureService.delete(procedureId);
        assertNull(procedureService.findOne(procedureId));

        //Delete process
        camundaRestClient.deleteProcessInstance(camundaProcessInstanceId);
        WebClientResponseException processException = assertThrows(
                WebClientResponseException.class, () -> camundaRestClient.getProcessInstance(camundaProcessInstanceId));
        assertNotNull(processException);
        assertEquals(processException.getStatusCode(), HttpStatus.NOT_FOUND);
    }

}
