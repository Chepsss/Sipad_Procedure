package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import lombok.Data;

import java.io.Serializable;

@Data
public class InputDocumentExt implements Serializable {

    private static final long serialVersionUID = 7307905969361673017L;

    private String registro;
    private String anno;
    private String numero;
    private String protocollo;

}
