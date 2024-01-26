package it.almaviva.difesa.cessazione.procedure.service;

import it.almaviva.difesa.cessazione.procedure.ProcedureApplication;
import it.almaviva.difesa.cessazione.procedure.constant.ProcedureStateConst;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.StateProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.ProcedureMapper;
import it.almaviva.difesa.cessazione.procedure.service.msproc.ProcedureService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.StateProcedureService;
import it.almaviva.difesa.cessazione.procedure.service.rest.CamundaRestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {CamundaRestClient.class, ProcedureApplication.class})
@ActiveProfiles("test")
public class StateProcedureServiceIntegrationTest {

    @Autowired
    StateProcedureService stateProcedureService;

    @Autowired
    ProcedureService procedureService;

    @Autowired
    ProcedureMapper procedureMapper;

    @Autowired
    CamundaRestClient camundaRestClient;

    @Test
    public void listNextStates() {
        String currentStateCode = ProcedureStateConst.BOZZA;
        Optional<StateProcedureDTO> currentState = stateProcedureService.findByCodeState(currentStateCode);
        Page<StateProcedureDTO> statesList = stateProcedureService.listNextStates(currentState.get().getCodeState());

        assertNotNull(statesList);
        assertEquals(statesList.getSize(), 1);
        assertEquals(statesList.getContent().get(0).getCodeState(), ProcedureStateConst.LAVORAZIONE);
        assertEquals(statesList.getContent().get(0).getDescState(), "Crea PA");

    }

    /*@Test
    public void changeState() {
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
        ChangeProcedureStateDTO changeStatusFormDTO = new ChangeProcedureStateDTO();
        changeStatusFormDTO.setCodeState(ProcedureStateConst.LAVORAZIONE);
        changeStatusFormDTO.setAssigneeId(1L);
        changeStatusFormDTO.setNote("NOTE");
        ProcedureDTO updatedProcedure = stateProcedureService.changeStateDto(procedure.getId(), changeStatusFormDTO);
        assertNotNull(updatedProcedure);
        assertEquals(updatedProcedure.getStateProcedure().getCodeState(), ProcedureStateConst.LAVORAZIONE);

        String camundaProcessInstanceId = updatedProcedure.getBpmnProcessId();
        assertNotNull(camundaProcessInstanceId);
        List<Task> task = camundaRestClient.listTask(camundaProcessInstanceId);
        assertNotNull(task);
        assertEquals(task.size() , 1);
        assertEquals(task.get(0).getTaskDefinitionKey(), ProcedureStateConst.LAVORAZIONE);

        //Delete procedure
        Long procedureId = procedure.getId();
        procedureService.delete(procedureId);
        assertNull(procedureService.findOne(procedureId));

        //Delete process
        camundaRestClient.deleteProcessInstance(camundaProcessInstanceId);
        WebClientResponseException processException = assertThrows(WebClientResponseException.class, () -> camundaRestClient.getProcessInstance(camundaProcessInstanceId));
        assertNotNull(processException);
        assertEquals(processException.getStatusCode(), HttpStatus.NOT_FOUND);
    }*/

}
