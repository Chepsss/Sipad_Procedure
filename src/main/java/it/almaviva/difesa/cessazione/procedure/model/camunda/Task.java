package it.almaviva.difesa.cessazione.procedure.model.camunda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private String id;
    private String name;
    private String assignee;
    private String created;
    private String due;
    private String followUp;
    private String delegationState;
    private String description;
    private String executionId;
    private String owner;
    private String parentTaskId;
    private Integer priority;
    private String processDefinitionId;
    private String processInstanceId;
    private String caseDefinitionId;
    private String caseInstanceId;
    private String caseExecutionId;
    private String taskDefinitionKey;
    private Boolean suspended;
    private String formKey;
    private String camundaFormRef;
    private String tenantId;

}
