package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDetailResponseDTO implements Serializable {

    private static final long serialVersionUID = -9190742791617340738L;

    private Long employeeId;
    private List<RoleDTO> roles;
    private UserDTO employeeDetail;

}
