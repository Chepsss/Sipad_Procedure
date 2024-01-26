package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileOutputResponseDTO implements Serializable {

    private static final long serialVersionUID = -3605286810306158086L;

    private String file;

}
