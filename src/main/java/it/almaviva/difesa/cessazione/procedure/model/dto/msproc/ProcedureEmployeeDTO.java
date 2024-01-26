package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ProcedureEmployeeDTO implements Serializable {

    private static final long serialVersionUID = -989215952380131187L;

    private String firstname;
    private String lastname;
    private LocalDate birthday;
    private String fiscalCode;
    private String gender;

}
