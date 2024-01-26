package it.almaviva.difesa.cessazione.procedure.model.camunda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentContainer {

    private CamundaOutputVariable reassignment;

}
