package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import lombok.Data;

import java.io.Serializable;

@Data
public class ModelloDTO implements Serializable {

    private static final long serialVersionUID = -9112852395161055987L;

    private String modello;
    private Long id;

}
