package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class EmployeeResponseDTO implements Serializable {

    private static final long serialVersionUID = 5373311269203961458L;

    private Long employeeId;
    private String lastName;
    private String firstName;
    private String fiscalCode;
    private String gender;
    private String serialNumber;
    private boolean alreadyPresent;
    private LocalDate birthDate;
    private String email;
    private String armedForceId;
    private String armedForceDescription;
    private String staffPositionId;
    private String staffPositionDescription;
    private Short staffCategoryId;
    private String staffCategoryDescription;
    private String rankId;
    private String rankDescription;
    private String roleId;
    private String roleDescription;

}
