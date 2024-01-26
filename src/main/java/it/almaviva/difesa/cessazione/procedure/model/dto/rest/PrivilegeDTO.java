package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeDTO implements Serializable {

    private static final long serialVersionUID = 1891577867359169059L;

    private Long id;
    private String privilegeCode;
    private String description;

}
