package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserSearchDTO extends EmployeeSearchDTO {

    private static final long serialVersionUID = 7703159980514525888L;

    private String roleCode;

}
