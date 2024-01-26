package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSearchDTO implements Serializable {

    private static final long serialVersionUID = 7703159980514525888L;

    private String fiscalCode;
    private String lastName;
    private String firstName;

}
