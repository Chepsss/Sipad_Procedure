package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateDocumentDTO implements Serializable {

    private static final long serialVersionUID = -6488877930820351103L;

    private String idDocumento;
    private String file;

}
