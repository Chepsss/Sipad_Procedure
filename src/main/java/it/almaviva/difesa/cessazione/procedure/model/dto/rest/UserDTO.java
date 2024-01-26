package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1959716254174432969L;

    private Long userId;
    private Long employeeId;
    private String fiscalCode;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String sex;
    private String email;
    private String rankId;
    private String rankDescription;
    private String armedForceId;
    private String armedForceDescription;

}
