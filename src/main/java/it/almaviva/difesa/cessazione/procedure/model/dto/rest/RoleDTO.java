package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = -4322809785414510733L;

    private Long id;
    private String roleCode;
    private String name;
    private String description;
    private Set<PrivilegeDTO> privilegeDTOS;

}
